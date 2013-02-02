/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
import Entity.Instance;
import Entity.Pair;
import Entity.Rule;
import Entity.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The ChoiceNode class extends the Node class for easy use within the rete network, although choice nodes are no standard nodes.
 * A ChoiceNodes appears whenever the positive + operator part of a rule is fullfilled, and a negative body exists.
 * Whenever there is a variable assignment that fullfills the positive body + operators of a rule, this variable assignment
 * is added into the choice node. Should this variable assignment reach a headNode it is remove again from the choiceNode.
 * 
 * Later when doing a guess we just have to go through our choice Nodes and guess on the instances
 * that are stored within the choice nodes. This is a fast way to determine on which rules, and which variable assignments we
 * can guess. Since we only have to guess on those rule instances for which the positive body + operators are fullfilled, which
 * did not already fire.
 * 
 * 
 * @param r The corresponding rule of this choice Node
 * @param constraintNode The HeadNodeConstraint corresponding to this choiceNode
 * @param allInstances a List of all instances that are stored within this nodes memory. This way we can return all instances quite easily, and this is needed very often by the choiceUnit.
 */
public class ChoiceNode extends Node{
    
    //TODO: [PERFORMANCE ISSUE] Maybe use a HashSet rather than an ArrayList for allInstances, since we also remove Instances here, which has O(n) rather than O(1). (But then the needed iterator within choice unit may slow the system down)
    
    
    private Rule r;
    private HeadNodeConstraint constraintNode;
    //private ArrayList<Instance> allInstances;
    private HashSet<Instance> allInstances;
    private HashMap<Instance,Integer> disabledInstances;    // DL at which an instance was no longer guessable due to some negative atom being derived positively
    
    /**
     * 
     * public constructor. Creates a new Choice Node with initialized data structures.
     * 
     * @param rete The rete Network this node is in
     * @param arity the arity of the stored variable assignments
     * @param r the corresponding rule
     * @param varPosition a mapping, mapping Variables to a position of the stored istances
     * @param constraintNode the corresponding HeadNodeConstraint
     */
    public ChoiceNode(Rete rete, int arity, Rule r, HashMap<Variable,Integer> varPosition, HeadNodeConstraint constraintNode){
        super(rete);
        this.r = r;
        /*if(arity == 0){
            memory.initStorage(100);
        }else{
            memory.initStorage(arity);
        }*/
        //System.out.println("ChoiceNode created!");
        rete.getChoiceUnit().addChoiceNode(this); // The choiceNode is registered on creation within the choiceUnit
        this.tempVarPosition = varPosition;
        this.constraintNode = constraintNode;
        //constraintNode.setChoicenode(this);
        //this.allInstances = new ArrayList<Instance>(); //TODO Init size of this list?
        this.allInstances = new HashSet<Instance>();
        //this.rete.getChoiceUnit().addNode(this);
        disabledInstances = new HashMap<Instance, Integer>();
    }
    
    /**
     * 
     * @return the corresponding constraint node
     */
    public HeadNodeConstraint getConstraintNode(){
        return this.constraintNode;
    }
    
    /**
     * 
     * Instances that reach a choiceNode are saved within it's memory and the allInstancelist.
     * Further more they are not stored within the ChoiceUnit's DecisionMemory, as they are treated differently during guesses and backtracking, and do not play a role for propagation
     * 
     * @param instance the instance that should be added
     * @param from the node the instance comes from. (Can be null. just needed to extend class Node)
     */
    @Override
    public void addInstance(Instance instance){
        //memory.addInstanceWithoutBacktracking(instance);
        allInstances.add(instance);  
    }
    
    /**
     * 
     * removes an instance from the memory and allInstanceList
     * 
     * @param instance 
     */
    @Override
    public void removeInstance(Instance instance){
//        rete.getChoiceUnit().AddInstanceRemovement(this, instance);
        //super.removeInstance(instance);
        if(allInstances.contains(instance)){
        //if(memory.containsInstance(instance)){
            //this.memory.removeInstance(instance);
            this.allInstances.remove(instance);
            this.rete.getChoiceUnit().addInstanceForRemovement(this, instance);
        }
        //System.out.println("RemoveInstance: " + this + " - " + instance);
        
    }
    
    /**
     * 
     * Adds an instance into the memory and "allInstances" without registering this adding in the ChoiceUnit
     * 
     * 
     * @param instance 
     */
    @Override
    public void simpleRemoveInstance(Instance instance){
        //TODO: Is this method used? (It's not used when no guessing occures)
        //this.memory.removeInstance(instance);
        this.allInstances.remove(instance);
    }
    /**
     * 
     * Removes an instance from the memoery and "allInstances" without registering this removement in the ChoiceUnit
     * 
     * @param instance 
     */
    @Override
    public void simpleAddInstance(Instance instance){
        //memory.addInstanceWithoutBacktracking(instance);
        this.allInstances.add(instance);
    }
    
    /**
     * 
     * @return the corresponding rule
     */
    public Rule getRule(){
        return r;
    }
    
    /**
     * 
     * @return the string representation of this Choice Node
     */
    @Override
    public String toString(){
        return "ChoiceNode: " + this.r;
    }
    
    /**
     * 
     * @return all instances that are stored within this choice node
     */
    public ArrayList<Instance> getAllInstances(){
        ArrayList<Instance> ret = new ArrayList<Instance>(allInstances);
        
        return ret;
        //return new ArrayList<Instance>(this.allInstances);
    }

    /**
     * ChoiceNode has specific memory, this method should not be called.
     * @return a RuntimeException if called.
     */
    @Override
    public Storage getMemory() {
        throw new RuntimeException("Bug: ChoideNode.getMemory() called. ChoiceNode has no standard memory.");
    }

    @Override
    public void backtrackTo(int decisionLevel) {
        // remove instances for guessing
        for (Iterator<Instance> inst = allInstances.iterator(); inst.hasNext();) {
            Instance instance = inst.next();
            if( instance.decisionLevel >= decisionLevel ) {
                inst.remove();
            }
        }
        // re-enable instances that were disabled
        for (Iterator<Map.Entry<Instance, Integer>> it = disabledInstances.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Instance, Integer> entry = it.next();
            // only re-enable higher decision levels, since disabling is one decision level "late"
            if( entry.getValue() >= decisionLevel ) {
                it.remove();
            }
        }
    }
    
    /**
     * Finds an instance in this ChoiceNode which can be added, i.e., whose
     * negative body is not in conflict with the current partial interpretation.
     *
     * @return a list of atoms and instances which must be added to negative
     * memory to make a rule fire.
     */
    public Pair<Instance,ArrayList<Pair<Atom, Instance>>> nextChoiceableInstance() {
        ArrayList<Pair<Atom, Instance>> toMakeNegative;
        // check all instances stored in this node
        for (Iterator<Instance> it = allInstances.iterator(); it.hasNext();) {
            Instance instance = it.next();
            // skip disabled instances
            if( disabledInstances.containsKey(instance)) {
                continue;
            }
            toMakeNegative = new ArrayList<Pair<Atom, Instance>>();
            boolean groundAtomInPositiveMemory = false;
            // check all atoms in its negative body
            for (Atom at : r.getBodyMinus()) {
                Instance toAdd = Unifyer.unifyAtom(at, instance, getVarPositions());
                toAdd.decisionLevel = rete.getChoiceUnit().getDecisionLevel();   // set new decision level
                toAdd.propagationLevel = 0;
                // if ground atom is in positive memory, skip this ground rule.
                if (rete.containsInstanceInBasicNode(at, toAdd, true)) {
                    groundAtomInPositiveMemory = true;
                    // disable instance for further guesses
                    disabledInstances.put(instance, rete.getChoiceUnit().getDecisionLevel());
                    break;
                } else {
                    toMakeNegative.add(new Pair(at, toAdd));
                }
            }
            if (!groundAtomInPositiveMemory) {
                // disable instance to avoid it being multiply guessed.
                disabledInstances.put(instance, rete.getChoiceUnit().getDecisionLevel());
                return new Pair<Instance, ArrayList<Pair<Atom, Instance>>>(instance, toMakeNegative);
            }
        }
        // no instance found where a positive guess would not directly yield inconsistency.
        return null;
    }    

    public void printChoiceNodeInstances() {
        for (Instance inst : allInstances) {
            System.out.print(inst.toString()+" ");
        }
        System.out.print("\n  Disabled:");
        for (Map.Entry<Instance, Integer> entry : disabledInstances.entrySet()) {
            System.out.print(entry.getKey().toString()+"@"+entry.getValue()+" ");
        }
        System.out.println();
    }
    
    public void disableInstance(Instance inst, int decisionLevel) {
        disabledInstances.put(inst, decisionLevel);
    }
    
    
}
