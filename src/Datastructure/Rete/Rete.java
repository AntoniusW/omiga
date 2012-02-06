/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author User
 */
public class Rete {
    
    HashMap<Predicate, BasicNode> basicLayerPlus;
    HashMap<Predicate, BasicNode> basicLayerMinus;
    
    HashMap<Predicate, Stack<Instance>> stackyPlus;
    HashMap<Predicate, Stack<Instance>> stackyMinus;
    
    boolean satisfiable = true;
    
    public Rete(){
        this.basicLayerPlus = new HashMap<Predicate,BasicNode>();
        this.basicLayerMinus = new HashMap<Predicate,BasicNode>();
        this.stackyPlus = new HashMap<Predicate, Stack<Instance>>();
        this.stackyMinus = new HashMap<Predicate, Stack<Instance>>();
    }
    
    /*
     * we push facts into our network til nothing more can be reached
     */
    public void propagate(){
        boolean stacksNotEmpty = true;
        while(stacksNotEmpty){
            /*System.out.println("ROUND:");
            for(Stack s: this.stackyPlus.values()){
                System.out.println(s.size());
            }*/
            stacksNotEmpty = false;
            for(Predicate p: stackyPlus.keySet()){
                if(!stackyPlus.get(p).isEmpty()){
                    Instance instance = stackyPlus.get(p).pop();
                    //System.out.println("Adding Instance: " + instance);
                    if(!this.containsInstance(p, instance, false)){
                        if (!this.containsInstance(p, instance, true)) {
                            basicLayerPlus.get(p).addInstance(instance);
                        }else{
                            //System.out.println("This instance is already contained!");
                        }
                    }else{
                        satisfiable = false;
                        System.err.println("Unsatisfiable!");
                        return;
                    }
                    stacksNotEmpty = true;
                    //System.out.println("Added Something!");
                }
            }
            for(Predicate p: stackyMinus.keySet()){
                if(!stackyMinus.get(p).isEmpty()){
                    Instance instance = stackyPlus.get(p).pop();
                    if(!this.containsInstance(p, instance, true)){
                        if (!this.containsInstance(p, instance, false)) basicLayerMinus.get(p).addInstance(instance);
                    }else{
                        satisfiable = false;
                        System.err.println("Unsatisfiable!");
                        return;
                    }
                    stacksNotEmpty = true;
                    //System.out.println("Added Something!");
                }
            }
        }
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
        
        ArrayList<Atom> atomsPlus = (ArrayList<Atom>) r.getBodyPlus().clone();
        ArrayList<Atom> atomsMinus = (ArrayList<Atom>) r.getBodyMinus().clone();
        ArrayList<Operator> operators = (ArrayList<Operator>) r.getOperators().clone();
        Atom actual = getBestNextAtom(atomsPlus);
        
        /*System.err.println("BasicLayer.get: " + this.basicLayerPlus.get(actual.getPredicate()));
        System.err.println("BasicLayer.get: " + this.basicLayerPlus.get(actual.getPredicate()).getChildren());
        System.err.println("KEY: " + actual.getAtomAsReteKey());
        System.err.println("Get Key: " + this.basicLayerPlus.get(actual.getPredicate()).getChildren().get(actual.getAtomAsReteKey()));
       */
        
        Node actualNode = this.basicLayerPlus.get(actual.getPredicate()).getChildren().get(actual.getAtomAsReteKey());
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
        actualNode.addChild(new HeadNode(r.getHead(),this, actualNode));
    }
    
    private Node createJoin(Node aNode, Atom b, boolean bPositive){
        SelectionNode bNode;
        if(bPositive){
            bNode = this.basicLayerPlus.get(b.getPredicate()).getChildren().get(b.getAtomAsReteKey());
        }else{
            bNode = this.basicLayerMinus.get(b.getPredicate()).getChildren().get(b.getAtomAsReteKey());
        }
        bNode.resetVarPosition(b);
        // TOCHECK: Keep track of all JoinNodes such that we create each joinNode only once!
        return new JoinNode(aNode,bNode, this);  
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
        if(!basicLayerPlus.containsKey(atom.getPredicate())){
            //System.out.println("Creating BasicNode: " + atom.getPredicate());
            this.basicLayerPlus.put(atom.getPredicate(), new BasicNode(atom.getArity(),this));
            this.stackyPlus.put(atom.getPredicate(), new Stack<Instance>());
        }    
        basicLayerPlus.get(atom.getPredicate()).AddPredInRule(atom);
        
    }
    
    public void addAtomMinus(Atom atom){
        if(!basicLayerMinus.containsKey(atom.getPredicate())){
            this.basicLayerMinus.put(atom.getPredicate(), new BasicNode(atom.getArity(),this));
            this.stackyMinus.put(atom.getPredicate(), new Stack<Instance>());
        }   
        basicLayerMinus.get(atom.getPredicate()).AddPredInRule(atom);  
    }
    
    public void addInstancePlus(Predicate p, Instance instance){
        //basicLayerPlus.get(p).addInstance(instance);
        this.stackyPlus.get(p).push(instance);
    }
    
     
    public void addInstanceMinus(Predicate p,Instance instance){
        //basicLayerMinus.get(p).addInstance(instance);
        this.stackyMinus.get(p).push(instance);
    }
    
    public boolean containsInstance(Predicate p, Instance instance, boolean positive){
        if(positive){
            if(this.basicLayerPlus.containsKey(p)){
                return basicLayerPlus.get(p).containsInstance(instance);
            }else{
                return false;
            }
        }else{
            if(this.basicLayerMinus.containsKey(p)){
                return basicLayerMinus.get(p).containsInstance(instance);
            }else{
                return false;
            }
        }
    }
    
    /*
     * Only used for testing?
     */
    public BasicNode getBasicNodePlus(Predicate p){
        return basicLayerPlus.get(p);
    }
    
    public void printAnswerSet(){
        for(Predicate p: this.basicLayerPlus.keySet()){
            Term[] selectionCriteria = new Term[p.getArity()];
            for(int i = 0; i < p.getArity();i++){
                selectionCriteria[i] = Variable.getVariable("X");
            }
            System.out.println("Instances for: " + p + " " + this.basicLayerPlus.get(p).select(selectionCriteria).size());
            //this.basicLayerPlus.get(p).printAllInstances();
        }
    }
    
    
}
