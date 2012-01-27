/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
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
    
    HashMap<Predicate, Stack<Term[]>> stackyPlus;
    HashMap<Predicate, Stack<Term[]>> stackyMinus;
    
    boolean satisfiable = true;
    
    public Rete(){
        this.basicLayerPlus = new HashMap<Predicate,BasicNode>();
        this.basicLayerMinus = new HashMap<Predicate,BasicNode>();
        this.stackyPlus = new HashMap<Predicate, Stack<Term[]>>();
        this.stackyMinus = new HashMap<Predicate, Stack<Term[]>>();
    }
    
    /*
     * we push facts into our network til nothing more can be reached
     */
    public void propagate(){
        boolean stacksNotEmpty = true;
        while(stacksNotEmpty){
            stacksNotEmpty = false;
            for(Predicate p: stackyPlus.keySet()){
                if(!stackyPlus.get(p).isEmpty()){
                    Term[] instance = stackyPlus.get(p).pop();
                    if(!this.containsInstance(p, instance, false)){
                        basicLayerPlus.get(p).addInstance(instance);
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
                    Term[] instance = stackyPlus.get(p).pop();
                    if(!this.containsInstance(p, instance, true)){
                        basicLayerMinus.get(p).addInstance(instance);
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
        Node actualNode = this.basicLayerPlus.get(actual.getPredicate()).getChildren().get(actual);
        Atom partner;
        
        while(!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()){
            if(!atomsPlus.isEmpty()){
                partner = getBestPartner(atomsPlus, actualNode);
                actualNode = this.createJoin(actualNode, partner, true);
            }else{
                if(!atomsMinus.isEmpty()){
                    partner = getBestPartner(atomsMinus, actualNode);
                    actualNode = this.createJoin(actualNode, partner, false);
                }else{
                    // Do something cool for operators!
                }
            }
        }
        actualNode.addChild(new HeadNode(r.getHead(),this));
    }
    
    private Node createJoin(Node aNode, Atom b, boolean bPositive){
        SelectionNode bNode;
        if(bPositive){
            bNode = this.basicLayerPlus.get(b.getPredicate()).getChildren().get(b);
        }else{
            bNode = this.basicLayerMinus.get(b.getPredicate()).getChildren().get(b);
        }
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
            this.basicLayerPlus.put(atom.getPredicate(), new BasicNode(atom.getArity(),this));
            this.stackyPlus.put(atom.getPredicate(), new Stack<Term[]>());
        }    
        basicLayerPlus.get(atom.getPredicate()).AddPredInRule(atom);
        
    }
    
    public void addAtomMinus(Atom atom){
        if(!basicLayerMinus.containsKey(atom.getPredicate())){
            this.basicLayerMinus.put(atom.getPredicate(), new BasicNode(atom.getArity(),this));
            this.stackyMinus.put(atom.getPredicate(), new Stack<Term[]>());
        }   
        basicLayerMinus.get(atom.getPredicate()).AddPredInRule(atom);  
    }
    
    public void addInstancePlus(Predicate p, Term[] instance){
        //basicLayerPlus.get(p).addInstance(instance);
        this.stackyPlus.get(p).push(instance);
    }
    
     
    public void addInstanceMinus(Predicate p, Term[] instance){
        //basicLayerMinus.get(p).addInstance(instance);
        this.stackyMinus.get(p).push(instance);
    }
    
    public boolean containsInstance(Predicate p, Term[] instance, boolean positive){
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
