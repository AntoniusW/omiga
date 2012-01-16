/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructures.storage;

import Entity.Instance;
import Interfaces.PredAtom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author User
 */
public class HashInstances{
    
    // TOCHECK: Maybe it is really faster to make the instances classes!
    // Then we can compare Speicheradressen
    
    private HashMap<Integer, ArrayList<PredAtom[]>> memory;
    
    public HashInstances(){
        memory = new HashMap<Integer, ArrayList<PredAtom[]>>();
    }
    
    // TOCHECK: if we ensure that each instance is added only once via the Rete Network this gets much faster as we can skip the contains!
    public void add(PredAtom[] instance){
        if(!memory.containsKey(Instance.hash(instance))){
            ArrayList<PredAtom[]> aL = new ArrayList<PredAtom[]>();
            aL.add(instance);
            memory.put(Instance.hash(instance), aL);
            
        }else{
            if(!contains(instance)) {
                memory.get(Instance.hash(instance)).add(instance);
            }
        }
        
    }
    
    public int size(){
        return this.getAll().size();
    }
    
    private boolean compareInstances(PredAtom[] ins1, PredAtom[] ins2){
        for(int i = 0; i < ins1.length; i++){
            if (!ins1[i].equals(ins2[i])) return false;
        }
        return true;
    }
    
    public void remove(PredAtom[] instance){
        ArrayList<PredAtom[]> aL = memory.get(Instance.hash(instance));
        for(PredAtom[] ins: aL){
            if (compareInstances(ins,instance)) {
                aL.remove(ins);
            }
        }
    }
    
    public boolean contains(PredAtom[] instance){
        //System.out.println("LOL: " + Instance.getInstanceAsString(instance));
        if(!memory.containsKey(Instance.hash(instance))) {
            return false;
        } //TOCHECK: Maybe not needed
        for(PredAtom[] ins:  memory.get(Instance.hash(instance))){
            //System.out.println(Instance.getInstanceAsString2(instance) + " vs " + Instance.getInstanceAsString2(ins));
            if (compareInstances(ins, instance)) return true;
        }
        return false;
    }
    
    // TOCHECK: Maybe we should have a seperate list with all instances in it to return here
    // this would lead to increased removement costs
    public Collection<PredAtom[]> getAll(){
        ArrayList<PredAtom[]> ret = new ArrayList<PredAtom[]>();
        for(ArrayList aL: this.memory.values()){
            ret.addAll(aL);
        }
        return ret;
    }
    
}