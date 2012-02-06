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

/**
 *
 * @author User
 */
public class BasicNode {
    
    private Storage memory;
    private HashMap<Atom,SelectionNode> children;
    //private ArrayList<SelectionNode> children;
    private Rete rete;
    
    public BasicNode(int arity, Rete rete){
        //System.out.println("BasicNode Created!");
        this.memory = new Storage(arity);
        this.children = new HashMap<Atom,SelectionNode>();
        //this.children = new ArrayList<SelectionNode>();
        this.rete = rete;
    }
    
    public void addInstance(Instance instance){
        //System.out.println("BasicNode Add Instance");
        //System.out.println("Basic NOde add instance is called with: " + Instance.getInstanceAsString(instance));
        memory.addInstance(instance);
        for(SelectionNode sn: children.values()){
        //for(SelectionNode sn: children){
            sn.addInstance(instance, null);
        }
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
