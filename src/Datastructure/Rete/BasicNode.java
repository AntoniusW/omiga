/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructures.storage.Storage;
import Entity.Instance;
import Entity.PredInRule;
import Interfaces.PredAtom;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author User
 */
public class BasicNode {
    
    private Storage memory;
    private ArrayList<SelectionNode> children;
    
    public BasicNode(int arity){
        this.memory = new Storage(arity);
        this.children = new ArrayList<SelectionNode>();
    }
    
    public void addInstance(PredAtom[] instance){
        System.out.println("BasicNode Add Instance");
        System.out.println("Basic NOde add instance is called with: " + Instance.getInstanceAsString(instance));
        memory.addInstance(instance);
        for(SelectionNode sn: children){
            if(sn.fits(instance)) sn.addInstance(instance);
        }
    }
    
    public void removeInstance(PredAtom[] instance){
        memory.removeInstance(instance);
    }
    
    public void AddPredInRule(PredInRule pir){
        SelectionNode sL = new SelectionNode(pir);
        if (!children.contains(sL))
            this.children.add(sL);
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
    
    public ArrayList<SelectionNode> getChildren(){
        return this.children;
    }
    
    
    
}
