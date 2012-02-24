/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Instance;
import Entity.Atom;
import Entity.Predicate;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author User
 */
public class BasicNode extends Node{
    
    private Storage memory;
    private ArrayList<SelectionNode> basicChildren;
    private Rete rete;
    Stack<Instance> toPropagate;
    Predicate pred;
    
    public BasicNode(int arity, Rete rete, Predicate pred){
        super(rete);
        System.out.println("BasicNode Created!: " + pred);
        this.memory = new Storage(arity);
        //this.children = new HashMap<Atom,SelectionNode>();
        this.basicChildren = new ArrayList<SelectionNode>();
        this.rete = rete;
        this.toPropagate = new Stack<Instance>();
        this.pred = pred;
    }
    
    public void addInstance(Instance instance){
        //System.err.println("Adding something to basicNode: " + this + " instance= "+ instance + " __ CHILDSIZE = " + this.basicChildren);
        super.addInstance(instance, this);
        //System.out.println("BasicNode Add Instance");
        //System.err.println("Basic NOde add instance is called with: " + instance + " : " + toPropagate.size());
        memory.addInstance(instance);
        this.toPropagate.add(instance); 
        //System.err.println("toPropagate = " + this.toPropagate);
        /*for(SelectionNode sn: children.values()){
        //for(SelectionNode sn: children){
            sn.addInstance(instance, null);
        }*/
    }
    
    public boolean propagate(){
        //System.err.println("CHILDREN OF BASIC NODE: " + this);
        //System.err.println("TOPROP OF BASIC NODE: " + this.toPropagate.size());
        boolean ret = this.toPropagate.size() > 0;
        while(!this.toPropagate.isEmpty() && rete.satisfiable){
            Instance ins = toPropagate.pop();
            //System.err.println("WHATSUP!?: " + ins);
            for(Node sn: basicChildren){
                //System.err.println("Giving instance: " + ins + " to : " + sn);
                sn.addInstance(ins, null);
            }
        }
        return ret;
    }
    
    public void removeInstance(Instance instance){
        memory.removeInstance(instance);
        //TODO: Children!
    }
    
    public void AddPredInRule(Atom pir){
        //System.err.println("AddPredInRule called " + this + " Atom = " + this.getChildNode(pir.getAtomAsReteKey()) + " __Childsize: " + this.basicChildren);
        if(this.getChildNode(pir.getAtomAsReteKey()) == null){
            //System.err.println("AddPredInRule if works!" + this.basicChildren);
            this.basicChildren.add(new SelectionNode(pir.getAtomAsReteKey(), this.rete));
            //System.err.println("AddPredInRule if works!2" + this.basicChildren);
        }
        /*if(!this.children.containsKey(pir.getAtomAsReteKey())){
            this.children.put(pir.getAtomAsReteKey(),new SelectionNode(pir, this.rete));
        }*/
            
        //System.err.println("Children of BasicNode: " + this.basicChildren);
    }
    
    public Collection<Instance> select(Term[] selectionCriteria){
        return memory.select(selectionCriteria);
    }
    public boolean containsInstance(Instance instance){
        return memory.containsInstance(instance);
    }
    
    public void printAllInstances(){
        memory.printAllInstances();
    }
    
    public ArrayList<Node> getChildren(){
        ArrayList<Node> aL = new ArrayList<Node>();
        for(SelectionNode sn: this.basicChildren){
            aL.add(sn);
        }
        return aL;
    }
    
    public SelectionNode getChildNode(Atom a){
        //System.err.println("GetChildNode: " + a);
        //System.err.println("Children: " + this.basicChildren);
        for(int i = 0; i < this.basicChildren.size();i++){
            if(this.basicChildren.get(i).getAtom().equals(a)){
                return this.basicChildren.get(i);
            }
        }
        return null;
    }
    
    public void addChild(Node n){
        if(n.getClass().equals(ChoiceNode.class)){
            System.out.println("NODE: Choice node is added to: " + this);
        }
            
        if (!this.basicChildren.contains((SelectionNode)n)) this.basicChildren.add((SelectionNode)n);
    }
    
    /*public HashMap<Atom,SelectionNode> getChildren(){
        return this.children;
    }*/
    
    /*public ArrayList<SelectionNode> getChildren(){
        return this.children;
    }
    
    public SelectionNode getSelectionNode(Atom a){
        for(SelectionNode sL: this.children){
            if (sL.getAtom().equals(a)){
                return sL;
            }
        }
        return null;
    }*/
    
    
    @Override
    public String toString(){
        return "BasicNode: " + pred + " - Childrensize: " + this.basicChildren.size();
    }
    
    public void resetPropagation(){
        this.toPropagate.clear();
    }
    
}
