/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.storage;

import Entity.Constant;
import Entity.Instance;
import Interfaces.Term;
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
    
    HashMap<Term,HashSet<Instance>>[] memory;
    
    
    public Storage(int arity){
        
        
        this.memory = new HashMap[arity];
        for(int i = 0; i < memory.length;i++){
            memory[i] = new HashMap<Term,HashSet<Instance>>();
        }
    }
    
    public void addInstance(Instance instance){
        for(int i = 0; i < instance.getSize();i++){
            // The try stuff is definitly faster than the if block ~3seconds for 2kk instances
            try{
                memory[i].get(instance.get(i)).add(instance);
            }catch(Exception e){
                memory[i].put(instance.get(i), new HashSet<Instance>());
                memory[i].get(instance.get(i)).add(instance);
            }
            /*if(!memory[i].containsKey(instance.get(i))) 
                memory[i].put(instance.get(i), new HashSet<Instance>());
            
            //if(!memory[i].get(instance.get(i)).contains(instance)) {
                memory[i].get(instance.get(i)).add(instance);
            //}*/
        }
        //System.out.println(this + " Added: " + Instance.getInstanceAsString(instance));
    }
    
    /*
     * Precondition: the structure for that instance must exist, otherwise we reach a null pointer exception
     * This method should only be called for instance for which we know they are within the datastructure
     */
    public void removeInstance(Instance instance){
        for(int i = 0; i < instance.getSize();i++){
            memory[i].get(instance.get(i)).remove(instance);
        }
    }
    
    public boolean containsInstance(Instance instance){   
        //memory[0].get(instance.get(0)) = null
        try{
            return memory[0].get(instance.get(0)).contains(instance);
        }catch(Exception e){
            return false;
        }
    }
    
    /*
     * Returns all instances that match the selectionCriterion. 
     * Please be aware that a selection Criterion [X,X] is not treated differently than [X,Y]
     * This selection does not enable you to specify equality of positions over all keys
     * This kind of selection is ensured over the Rete Network
     * 
     * PreCondition: selectionCriterion has to be of equalsize to the memory's Arraysize
     */
    
    private ArrayList<HashSet> selected = new ArrayList<HashSet>();
    ArrayList<Instance> ret = new ArrayList<Instance>();
    int i = 0;
    boolean flag;
    public Collection<Instance> select(Term[] selectionCriterion){
        //ArrayList<Instance> ret = new ArrayList<Instance>();
        ret.clear();
        //ArrayList<HashSet> selected = new ArrayList<HashSet>();
        selected.clear();
        
        
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
            for(HashSet hS: memory[0].values()){
                ret.addAll(hS);
            }
            return ret;
        }else{
            // There is a selection Criterion so we return only those instances that are contained in each set.
            HashSet<Instance> smallest = selected.get(0);
            for(int i = 1; i < selected.size();i++){
                if(selected.get(i).size() < smallest.size()) smallest = selected.get(i);
            }
            selected.remove(smallest);
            //boolean flag;
            for(Instance instance: smallest){
                flag = true;
                for(HashSet hS: selected){
                    //To Check: Just go trough the instances of the smallest one, and check their positions
                    if(!hS.contains(instance)){
                        flag = false;
                        break;
                    }
                }
                if(flag) ret.add(instance);
            }
            /*Instance[] inz = new Instance[smallest.size()];
            smallest.toArray(inz);
            HashSet[] hss = new HashSet[selected.size()];
            selected.toArray(hss);
            boolean flag;
            for(int i = 0; i < inz.length;i++){
                flag = true;
                for(int j = 0; j < hss.length;j++){
                    if(!hss[j].contains(inz[i])){
                        flag = false;
                    }
                }
                if(flag) ret.add(inz[i]);
            }*/
        }
        return ret;
    }
    
    
    public void printAllInstances(){
        for(HashSet<Instance> hS: memory[0].values()){
            for(Instance instance: hS){
                System.out.println(instance);
            }
        }
        
    }
    
    
}
