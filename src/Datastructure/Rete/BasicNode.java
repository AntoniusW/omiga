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
    private Rete rete;
    
    public BasicNode(int arity, Rete rete){
        this.memory = new Storage(arity);
        this.children = new HashMap<Atom,SelectionNode>();
        this.rete = rete;
    }
    
    public void addInstance(Term[] instance){
        //System.out.println("BasicNode Add Instance");
        //System.out.println("Basic NOde add instance is called with: " + Instance.getInstanceAsString(instance));
        memory.addInstance(instance);
        for(SelectionNode sn: children.values()){
            sn.addInstance(instance, null);
        }
    }
    
    public void removeInstance(Term[] instance){
        memory.removeInstance(instance);
    }
    
    public void AddPredInRule(Atom pir){
        SelectionNode sL = new SelectionNode(pir, this.rete);
        if (!children.containsKey(pir))
            this.children.put(pir,sL);
    }
    
    public Collection<Term[]> select(Term[] selectionCriteria){
        return memory.select(selectionCriteria);
    }
    public boolean containsInstance(Term[] instance){
        return memory.containsInstance(instance);
    }
    
    public void printAllInstances(){
        memory.printAllInstances();
    }
    
    public HashMap<Atom,SelectionNode> getChildren(){
        return this.children;
    }
    
    @Override
    public String toString(){
        return "BasicNode - Childrensize: " + this.children.size();
    }
    
}
