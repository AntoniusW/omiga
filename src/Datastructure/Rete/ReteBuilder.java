/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

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
    
    public ReteBuilder(){
        this.rete = new Rete();
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
        
        Node actualNode = this .rete.getBasicLayerPlus().get(actual.getPredicate()).getChildren().get(actual.getAtomAsReteKey());
        ((SelectionNode)actualNode).resetVarPosition(actual);
        /*Node actualNode = this.basicLayerPlus.get(actual.getPredicate()).getSelectionNode(actual.getAtomAsReteKey());
        System.err.println("Get Key: " + this.basicLayerPlus.get(actual.getPredicate()).getSelectionNode(actual.getAtomAsReteKey()));*/
        System.err.println("Actual Node: " + actualNode);
        Atom partner;
        
        while(!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()){
            if(!atomsPlus.isEmpty()){
                partner = getBestPartner(atomsPlus, actualNode);
                System.err.println("Partner: " + partner);
                actualNode = this.createJoin(actualNode, partner, true);
            }else{
                if(!atomsMinus.isEmpty()){
                    partner = getBestPartner(atomsMinus, actualNode);
                    System.err.println("Partner: " + partner);
                    actualNode = this.createJoin(actualNode, partner, false);
                }else{
                    // Do something cool for operators!
                }
            }
        }
        actualNode.addChild(new HeadNode(r.getHead(),rete, actualNode));
    }
    
    private Node createJoin(Node aNode, Atom b, boolean bPositive){
        SelectionNode bNode;
        if(bPositive){
            bNode = this.rete.getBasicLayerPlus().get(b.getPredicate()).getChildren().get(b.getAtomAsReteKey());
        }else{
            bNode = this.rete.getBasicLayerMinus().get(b.getPredicate()).getChildren().get(b.getAtomAsReteKey());
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
        if(!this.rete.getBasicLayerPlus().containsKey(atom.getPredicate())){
            //System.out.println("Creating BasicNode: " + atom.getPredicate());
            this.rete.getBasicLayerPlus().put(atom.getPredicate(), new BasicNode(atom.getArity(),rete));
            //this.stackyPlus.put(atom.getPredicate(), new Stack<Instance>());
        }    
        this.rete.getBasicLayerPlus().get(atom.getPredicate()).AddPredInRule(atom);
        
    }
    
    public void addAtomMinus(Atom atom){
        if(!this.rete.getBasicLayerMinus().containsKey(atom.getPredicate())){
            this.rete.getBasicLayerMinus().put(atom.getPredicate(), new BasicNode(atom.getArity(),rete));
            //this.stackyMinus.put(atom.getPredicate(), new Stack<Instance>());
        }   
        this.rete.getBasicLayerMinus().get(atom.getPredicate()).AddPredInRule(atom);  
    }
    
}
