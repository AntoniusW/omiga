/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructures.storage;

import Entity.Constant;
import Entity.Instance;
import Interfaces.PredAtom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class Storage {
    
    // TODO: use this: HashMap<PredAtom,HashMap<Integer,ArrayList<PredAtom[]>>>[];
    // where:                             keyOfInstance, List of all Instances that are mapped to that key
    
    // TOCHECK: Maybe it is really faster if we make the instances classes!
    
    HashMap<PredAtom,HashInstances>[] memory;
    
    public Storage(int arity){
        this.memory = new HashMap[arity];
        for(int i = 0; i < memory.length;i++){
            memory[i] = new HashMap<PredAtom,HashInstances>();
        }
    }
    
    public void addInstance(PredAtom[] instance){
        for(int i = 0; i < instance.length;i++){
            if(!memory[i].containsKey(instance[i])) memory[i].put(instance[i], new HashInstances());
            if(!memory[i].get(instance[i]).contains(instance)) {
                memory[i].get(instance[i]).add(instance);
            }
        }
        System.out.println(this + " Added: " + Instance.getInstanceAsString(instance));
    }
    
    /*
     * Precondition: the structure for that instance must exist, otherwise we reach a null pointer exception
     * This method should only be called for instance for which we know they are within the datastructure
     */
    public void removeInstance(PredAtom[] instance){
        for(int i = 0; i < instance.length;i++){
            memory[i].get(instance[i]).remove(instance);
        }
    }
    
    public boolean containsInstance(PredAtom[] instance){    
        return memory[0].get(instance[0]).contains(instance);
    }
    
    /*
     * Returns all instances that match the selectionCriterion. 
     * Please be aware that a selection Criterion [X,X] is not treated differently than [X,Y]
     * This selection does not enable you to specify equality of positions over all keys
     * This kind of selection is ensured over the Rete Network
     * 
     * PreCondition: selectionCriterion has to be of equalsize to the memory's Arraysize
     */
    public Collection<PredAtom[]> select(PredAtom[] selectionCriterion){
        ArrayList<PredAtom[]> ret = new ArrayList<PredAtom[]>();
        
        ArrayList<HashInstances> selected = new ArrayList<HashInstances>();
        
        for(int i = 0; i < selectionCriterion.length;i++){
            // We go through the selectionCriterion and create Sets of the instances per position that were selected
            // If it is a variable we do not have to add anything, as a variable gets all instances of the memory --> the intercection will not change if there is another Set
            if(!selectionCriterion[i].isVariable()){
                if(!memory[i].containsKey(selectionCriterion[i])) {
                    return ret;
                } // Key not in memory --> no entrys --> no instances
                selected.add(memory[i].get(selectionCriterion[i]));
            }  
        }
        
        if(selected.isEmpty()) {
            //if selected is empty the selectionCriterion consisted only of Variables and we have to return everything
            //if this is used often we should consider having a seperate list of all instances of a memory, because we have to iterate through all keys here
            //Anyway I assume joins were no Variables are equal, to not happen that often
            for(HashInstances hI: memory[0].values()){
                ret.addAll(hI.getAll());
            }
            return ret;
        }else{
            // There is a selection Criterion so we return only those instances that are contained in each set.
            HashInstances smallest = selected.get(0);
            for(int i = 1; i < selected.size();i++){
                if(selected.get(i).size() < smallest.size()) smallest = selected.get(i);
            }
            selected.remove(smallest);
            for(PredAtom[] instance: smallest.getAll()){
                boolean flag = true;
                for(HashInstances hI: selected){
                    if(!hI.contains(instance)){
                        flag = false;
                        break;
                    }
                }
                if(flag) ret.add(instance);
            }
        }
        
        return ret;
    }
    
    public void printAllInstances(){
        for(HashInstances hI: memory[0].values()){
            for(PredAtom[] instance: hI.getAll()){
                System.out.println(Instance.getInstanceAsString(instance));
            }
        }
        
    }
    
    
}
