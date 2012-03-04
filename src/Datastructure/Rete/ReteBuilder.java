/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.choice.ChoiceUnit;
import Entity.Atom;
import Entity.ContextASP;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class ReteBuilder {
    
    private Rete rete;
    
    /**
     * 
     * public constrcutor. Creates a new ReteBuilder.
     * 
     * @param choiceUnit the ChoiceUnit you want your rete to connect with.
     */
    public ReteBuilder(ChoiceUnit choiceUnit){
        this.rete = new Rete(choiceUnit);
    }
    
    /**
     * 
     * public constrcutor. Creates a new ReteBuilder.
     * 
     * @param rete the rete network you wanna work with
     */
    public ReteBuilder(Rete rete){
        this.rete = rete;
    }
    
    /**
     * 
     * adds a rule into the rete, by creating all needed nodes and connecting them into the right join order.
     * 
     * @param r the rule that should be added
     */
    public void addRule(Rule r){
        //We first add all Atoms of the rule to out retenetwork, so we then can work with the selectionnodes that are already there
        if(r.getHead()!=null) this.addAtomPlus(r.getHead());
        
        for(Atom a: r.getBodyPlus()){
            this.addAtomPlus(a);
        }
        for(Atom a: r.getBodyMinus()){
            this.addAtomMinus(a);
        }
        
        // We clone the different parts of the rules body, so we can change them without changing the rule
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsPlus = (ArrayList<Atom>) r.getBodyPlus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsMinus = (ArrayList<Atom>) r.getBodyMinus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Operator> operators = (ArrayList<Operator>) r.getOperators().clone();
        
        //we choose an atom with which we want to start
        Atom actual = getBestNextAtom(atomsPlus);
        
        // From the actual Atom we easily derive the corresponding node
        Node actualNode = this.rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        //We cast to selectionNode, since this is always a selection Node
        ((SelectionNode)actualNode).resetVarPosition(actual);

        //We create variables for the partner, the ChoiceNode and HeadNodeConstraint for this rule
        Atom partner;
        ChoiceNode cN = null;
        HeadNodeConstraint constraintNode = null;
        
        
        if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty()){
            // if the rule consisted only of one positive atom no operators and has a negative body we have to create the choice and constraint Node here.
            constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
            cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(),r,actualNode.tempVarPosition, constraintNode);
            actualNode.addChild(cN);
        }
        
        
        while(!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()){
            //While there is still seomthing in the rules body
            if(!atomsPlus.isEmpty()){
                //There is still something within the positive body of the rule --> take it --> it's the new partner
                partner = getBestPartner(atomsPlus, actualNode);
                //Create a joinNode from the actualNode and the partner
                actualNode = this.createJoin(actualNode, partner, true);
                
                if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty()){
                    // if atomPlus is now empty  we removed the last atom from here.
                    // If there is a negative part and no operators are within this Rule then we now add the ChoiceNode and constraintNode
                    // since the positive part of the rule is satisfied now
                    constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
                    cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(),r,actualNode.tempVarPosition, constraintNode);
                    actualNode.addChild(cN);
                }
            }else{
                if(!atomsMinus.isEmpty()){
                    //There is still something within the negative body of the rule --> take it --> it's the new partner
                    partner = getBestPartner(atomsMinus, actualNode);
                    //Create a joinNode from the actualNode and the partner
                    actualNode = this.createJoin(actualNode, partner, false);
                }else{
                    // TODO: Do this before atomsMinus, and create cN Node here as well!
                    // Do something cool for operators!
                }
            }
        }
        //We define a headNode and add it to the actualNode (which is the last within this rules joinorder, since we are finsihed now)
        HeadNode hN = new HeadNode(r.getHead(),rete, actualNode);
        actualNode.addChild(hN);
        //If we did contruct a constraintNode we add it to the actual Node as well
        if(constraintNode != null) actualNode.addChild(constraintNode);
        //if we did construct a ChoiceNode we add it to the headNode
        if(cN!=null) hN.addChild(cN);
    }
    
    /**
     * 
     * Creates and returns a JoinNode
     * 
     * @param aNode the actual Node
     * @param b the Atom you want to combine the node with
     * @param bPositive wether b is from the positive or negative memory of the rete
     * @return the resulting joinnode with a and b as children.
     */
    private Node createJoin(Node aNode, Atom b, boolean bPositive){
        SelectionNode bNode;
        if(bPositive){
            bNode = this.rete.getBasicLayerPlus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }else{
            bNode = this.rete.getBasicLayerMinus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }
        bNode.resetVarPosition(b);
        // TOCHECK: Keep track of all JoinNodes such that we create each joinNode only once!
        return new JoinNode(aNode,bNode, rete);  
    }
    
    /**
     * Finds an atom to combine the node with. Maybe something cool can be implemented here.
     * At the moment we just take the first.
     * 
     * @param atoms a list of atoms you want to choose from
     * @param node a node representing a memory for variable assignments
     * @return the best partner atom for the node
     */
    private Atom getBestPartner(ArrayList<Atom> atoms, Node node){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    /**
     * 
     * Finds an atom to start with. Maybe something cool can be implemented here.
     * At the moment we just take the first.
     * 
     * @param atoms a list of atoms you want to choose from
     * @return the best atom to start with
     */
    private Atom getBestNextAtom(ArrayList<Atom> atoms){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    /**
     * 
     * Adds an atom to the positive memory of the rete network, by creating a new SelectionNode for that atom and adding it as a child to the right basicNode
     * 
     * @param atom the atom you want to add
     */
    public void addAtomPlus(Atom atom){   
        //System.err.println("AddAtomPlus:");
        if(!this.rete.getBasicLayerPlus().containsKey(atom.getPredicate())){
            //System.err.println("AddAtomPlus: Creating BasicNode: " + atom.getPredicate());
            this.rete.getBasicLayerPlus().put(atom.getPredicate(), new BasicNode(atom.getArity(),rete, atom.getPredicate()));
            //this.stackyPlus.put(atom.getPredicate(), new Stack<Instance>());
        }    
        this.rete.getBasicLayerPlus().get(atom.getPredicate()).AddAtom(atom);
        
    }
    
    /**
     * Adds an atom to the negative memory of the rete network, by creating a new SelectionNode for that atom and adding it as a child to the right basicNode
     * 
     * @param atom the atom you want to add
     */
    public void addAtomMinus(Atom atom){
        if(!this.rete.getBasicLayerMinus().containsKey(atom.getPredicate())){
            this.rete.getBasicLayerMinus().put(atom.getPredicate(), new BasicNode(atom.getArity(),rete, atom.getPredicate()));
            //this.stackyMinus.put(atom.getPredicate(), new Stack<Instance>());
        }   
        this.rete.getBasicLayerMinus().get(atom.getPredicate()).AddAtom(atom);  
    }
    
}
