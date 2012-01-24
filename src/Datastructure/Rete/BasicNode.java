/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructures.storage.Storage;
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
    
    public BasicNode(int arity){
        this.memory = new Storage(arity);
        this.children = new HashMap<Atom,SelectionNode>();
    }
    
    public void addInstance(Term[] instance){
        System.out.println("BasicNode Add Instance");
        System.out.println("Basic NOde add instance is called with: " + Instance.getInstanceAsString(instance));
        memory.addInstance(instance);
        for(SelectionNode sn: children.values()){
            sn.addInstance(instance);
        }
    }
    
    public void removeInstance(Term[] instance){
        memory.removeInstance(instance);
    }
    
    public void AddPredInRule(Atom pir){
        SelectionNode sL = new SelectionNode(pir);
        if (!children.containsKey(pir))
            this.children.put(pir,sL);
    }
    
    /*public Collection<PredAtom[]> select(PredAtom[] selectionCriteria){
        return memory.select(selectionCriteria);
    }
    public boolean containsInstance(PredAtom[] instance){
        return memory.containsInstance(instance);
    }*/
    
    public void printAllInstances(){
        memory.printAllInstances();
    }
    
    public HashMap<Atom,SelectionNode> getChildren(){
        return this.children;
    }
    
    
    
}
