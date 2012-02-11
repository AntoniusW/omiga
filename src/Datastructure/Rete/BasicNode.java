/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Instance;
import Entity.Atom;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author User
 */
public class BasicNode {
    
    private Storage memory;
    private HashMap<Atom,SelectionNode> children;
    //private ArrayList<SelectionNode> children;
    private Rete rete;
    Stack<Instance> toPropagate;
    
    public BasicNode(int arity, Rete rete){
        //System.out.println("BasicNode Created!");
        this.memory = new Storage(arity);
        this.children = new HashMap<Atom,SelectionNode>();
        //this.children = new ArrayList<SelectionNode>();
        this.rete = rete;
        this.toPropagate = new Stack<Instance>();
    }
    
    public void addInstance(Instance instance){
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
        //System.err.println("CHILDREN OF BASIC NODE: " + this.children.size());
        boolean ret = this.toPropagate.size() > 0;
        while(!this.toPropagate.isEmpty() /*&& rete.satisfiable*/){
            //System.err.println("WHATSUP!?");
            Instance ins = toPropagate.pop();
            for(SelectionNode sn: children.values()){
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
        if(!this.children.containsKey(pir.getAtomAsReteKey())){
            this.children.put(pir.getAtomAsReteKey(),new SelectionNode(pir, this.rete));
        }
            
        System.out.println("Children of BasicNode: " + this.children);
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
    
    public HashMap<Atom,SelectionNode> getChildren(){
        return this.children;
    }
    
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
        return "BasicNode - Childrensize: " + this.children.size();
    }
    
}
