/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.storage;

import Entity.Constant;
import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * @version 1.0
 * 
 * This class is our datastructure for storing instances. Basically it's an array of HashMaps with Terms as Keys and
 * HashSets of instances as values, and methods for insertion and removal into this structure. Herby references to instances
 * are saved into each HashMap of the Array, such that when making a selection, we can simply look into the corresponding
 * HashMap and directly get all instances that have a certain term at that position.
 * 
 * 
 */
public class Storage {
    
    HashMap<Term,HashSet<Instance>>[] memory; // this is where we store our stuff
    
    /**
     * Constructs an memory array of size arity and fills it with HashMaps
     * 
     * @param arity The arity of the predicate this storage is used for
     */
    @SuppressWarnings("unchecked") // AW: workaround for array conversion
    public Storage(int arity){
        this.memory = new HashMap[arity];
        for(int i = 0; i < memory.length;i++){
            memory[i] = new HashMap<Term,HashSet<Instance>>();
        }
    }
    
    /**
     * This method adds an instance to our data structure in such a way, that the instance is inserted into each 
     * HashMap of our memory array, where the key is the corresponding Term of the instance at the position corresponding
     * to the position of the HashMap within the array.
     * 
     * @param instance the instance that shall be added into this data structure
     * 
     */
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
    
    /**
     * This method removes the instance from all HashMaps of our memory array.
     * 
     * Precondition: the structure for that instance must exist, otherwise we reach a null pointer exception
     * This method should only be called for instances for which we know they are within the data structure
     * 
     * @param instance an instance that is within our data structure memory that should be removed
     */
    
    public void removeInstance(Instance instance){
        for(int i = 0; i < instance.getSize();i++){
            memory[i].get(instance.get(i)).remove(instance);
        }
    }
    
    /**
     * checks wether an instance is within our data structure or not
     * 
     * @param instance An instance for which we want to check if it is contained within our memory
     * @return true or false, wether the instance is contained or not
     */
    public boolean containsInstance(Instance instance){   
        //memory[0].get(instance.get(0)) = null
        try{
            return memory[0].get(instance.get(0)).contains(instance);
        }catch(Exception e){
            return false;
        }
    }
    
    
    // Following variables are defined public, since the select method is called very often during calculation
    // Therefore many objects would be created if these variables were defined within the method itself
    // Having them outside and just resetting them is much faster
    private ArrayList<HashSet> selected = new ArrayList<HashSet>(); // used within method select, to store the hashsets that are treated by the actual guess
    private HashSet<Instance> smallest;
    private ArrayList<Instance> ret = new ArrayList<Instance>(); // the arrayList that is returned by the select
    boolean flag; // a flag needed in method select
    
    
    
    /**
     * 
     * Returns all instances that match the selectionCriterion. 
     * Please be aware that a selection Criterion [X,X] is not treated differently than [X,Y]
     * This selection does not enable you to specify equality over several positions
     * This kind of selection is ensured over the Rete Network
     * 
     * PreCondition: selectionCriterion has to be of equal size to the memory's array size
     * 
     * @param selectionCriterion this array of terms defines what has to be at the positions of the instances that are selected.
     *                           If at position i there is a constant, or a functerm, only instances with that constant or functerm
     *                           at position i are selected. if it is a variable then for this slot all instances are selected.
     *                           The size of the selectionCriterion must be equal to this class memory length
     * @return a List of all Instances contained in the memory, that follow all slots of the selection criteria
     */
    @SuppressWarnings("unchecked") // AW: workaround for array conversion
    public Collection<Instance> select(Term[] selectionCriterion){
        
        // We reset the list we return and clear the temp list selected
        ret.clear();
        selected.clear();
        
        // for each term of the selectionCriterion we add the HashSet of that Key to our selected List.
        // If there is no entry for that term in the corresponding HashMap then there is no instance to return
        // and we return an empty list
        // if the term is a Variable we do nothing, since a variable is no restriction on the instances
        for(int i = 0; i < selectionCriterion.length;i++){
            if(!selectionCriterion[i].getClass().equals(Variable.class)){
                if(!memory[i].containsKey(selectionCriterion[i])) {
                    // There is not even an entry for this key --> no instances
                    return ret;
                }
                selected.add(memory[i].get(selectionCriterion[i]));
            }  
        }
        
        // if selected is empty, this means the selectionCriterion consisted only of variables
        // This means we have to return everything. So we add all instances of our first HashMap to ret, and return it.
        if(selected.isEmpty()) {
            for(HashSet hS: memory[0].values()){
                ret.addAll(hS);
            }
            return ret;
        }else{
            // There is a selection Criterion so we return only those instances that are contained in each set.
            
            // We find the set with least ammount of instances and remove it from selected
            smallest = selected.get(0);
            for(int i = 1; i < selected.size();i++){
                if(selected.get(i).size() < smallest.size()) smallest = selected.get(i);
            }
            selected.remove(smallest);
            // for each Instance within smallest we check if the instance is containd in all other sets
            // if so the instance fullfills the selectioncriterion and is added to ret
            /*for(Instance instance: smallest){
                flag = true;
                for(HashSet hS: selected){
                    //To Check: Just go trough the instances of the smallest one, and check their positions
                    if(!hS.contains(instance)){
                        flag = false;
                        break;
                    }
                }
                if(flag) ret.add(instance);
            }*/
            
            // for each instance within smallest, we check if the instance fullfills the complete selectionCriteria
            if(selected.isEmpty()){
                return smallest;
            }else{
                for(Instance instance: smallest){
                    for(int i = 1; i < selectionCriterion.length;i++){
                        flag = true;
                        if (!selectionCriterion[i].getClass().equals(Variable.class)){
                            if(!instance.get(i).equals(selectionCriterion[i])) flag = false;
                        }
                    }
                    if(flag) ret.add(instance);
                }
            }
        }
        return ret;
    }
    
    /**
     * prints all instances that are contained within this memory to standard out.
     */
    public void printAllInstances(){
        for(HashSet<Instance> hS: memory[0].values()){
            for(Instance instance: hS){
                System.out.println(instance);
            }
        }
        
    }
    
    
}
