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
 * @author User
 */
public class ReteBuilder {
    
    Rete rete;
    
    public ReteBuilder(ChoiceUnit choiceUnit){
        this.rete = new Rete(choiceUnit);
    }
    
    public ReteBuilder(Rete rete){
        this.rete = rete;
    }
    
    public void addRule(Rule r){
        //We first add all Atoms of the rule to out retenetwork, so we then can work with the selectionnodes that are already there
        if(r.getHead()!=null) this.addAtomPlus(r.getHead());
        
        for(Atom a: r.getBodyPlus()){
            this.addAtomPlus(a);
        }
        for(Atom a: r.getBodyMinus()){
            this.addAtomMinus(a);
        }
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsPlus = (ArrayList<Atom>) r.getBodyPlus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsMinus = (ArrayList<Atom>) r.getBodyMinus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Operator> operators = (ArrayList<Operator>) r.getOperators().clone();
        Atom actual = getBestNextAtom(atomsPlus);
        
        /*System.err.println("BasicLayer.get: " + this.basicLayerPlus.get(actual.getPredicate()));
        System.err.println("BasicLayer.get: " + this.basicLayerPlus.get(actual.getPredicate()).getChildren());
        System.err.println("KEY: " + actual.getAtomAsReteKey());
        System.err.println("Get Key: " + this.basicLayerPlus.get(actual.getPredicate()).getChildren().get(actual.getAtomAsReteKey()));
       */
        
        //Node actualNode = this .rete.getBasicLayerPlus().get(actual.getPredicate()).getChildren().get(actual.getAtomAsReteKey());
        Node actualNode = this.rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        System.err.println(actualNode);
        ((SelectionNode)actualNode).resetVarPosition(actual);
        System.err.println("ASA: " + actualNode + " actual Pred= " + actual.getPredicate());
        /*Node actualNode = this.basicLayerPlus.get(actual.getPredicate()).getSelectionNode(actual.getAtomAsReteKey());
        System.err.println("Get Key: " + this.basicLayerPlus.get(actual.getPredicate()).getSelectionNode(actual.getAtomAsReteKey()));*/
        System.err.println("Actual Node: " + actualNode);
        Atom partner;
        
        ChoiceNode cN = null;
        HeadNodeConstraint constraintNode = null;
        
        
        if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty()){
                    // if the rule consisted only of one positive atom no operators and has a negative body we have to create the choice Node here.
                    constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
                    cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(),r,actualNode.tempVarPosition, constraintNode);
                    System.err.println("ChoiceNode: " + cN);
                    actualNode.addChild(cN);
                }
        
        
        while(!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()){
            System.out.println("OMG: " + atomsPlus.isEmpty() + atomsMinus.isEmpty() + operators.isEmpty());
            if(!atomsPlus.isEmpty()){
                partner = getBestPartner(atomsPlus, actualNode);
                System.err.println("Partner: " + partner);
                actualNode = this.createJoin(actualNode, partner, true);
                if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty()){
                    System.out.println("HALLÖLÖ?!");
                    // if atomPlus is now empty  we removed the last atom from here.
                    // If there is a negative part and no operators are within this Rule then we now add the ChoiceNode
                    // since the positive part of the rule is satisfied now
                    constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
                    cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(),r,actualNode.tempVarPosition, constraintNode);
                    System.err.println("ChoiceNode: " + cN);
                    actualNode.addChild(cN);
                }
            }else{
                if(!atomsMinus.isEmpty()){
                    partner = getBestPartner(atomsMinus, actualNode);
                    System.err.println("Partner: " + partner);
                    actualNode = this.createJoin(actualNode, partner, false);
                }else{
                    // TODO: Do this before atomsMinus, and create cN Node here as well!
                    // Do something cool for operators!
                }
            }
        }
        HeadNode hN = new HeadNode(r.getHead(),rete, actualNode);
        actualNode.addChild(hN);
        if(constraintNode != null) actualNode.addChild(constraintNode);
        if(cN!=null) hN.addChild(cN);
    }
    
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
    
    
    private Atom getBestPartner(ArrayList<Atom> atoms, Node node){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    private Atom getBestNextAtom(ArrayList<Atom> atoms){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    public void addAtomPlus(Atom atom){   
        System.err.println("AddAtomPlus:");
        if(!this.rete.getBasicLayerPlus().containsKey(atom.getPredicate())){
            System.err.println("AddAtomPlus: Creating BasicNode: " + atom.getPredicate());
            this.rete.getBasicLayerPlus().put(atom.getPredicate(), new BasicNode(atom.getArity(),rete, atom.getPredicate()));
            //this.stackyPlus.put(atom.getPredicate(), new Stack<Instance>());
        }    
        this.rete.getBasicLayerPlus().get(atom.getPredicate()).AddPredInRule(atom);
        
    }
    
    public void addAtomMinus(Atom atom){
        if(!this.rete.getBasicLayerMinus().containsKey(atom.getPredicate())){
            this.rete.getBasicLayerMinus().put(atom.getPredicate(), new BasicNode(atom.getArity(),rete, atom.getPredicate()));
            //this.stackyMinus.put(atom.getPredicate(), new Stack<Instance>());
        }   
        this.rete.getBasicLayerMinus().get(atom.getPredicate()).AddPredInRule(atom);  
    }
    
}
