/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
    private ArrayList<Instance> allInstances;
    
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
        this.memory = new Storage(arity);
        System.out.println("ChoiceNode created!");
        rete.getChoiceUnit().addChoiceNode(this); // The choiceNode is registered on creation within the choiceUnit
        this.tempVarPosition = varPosition;
        this.constraintNode = constraintNode;
        constraintNode.setChoicenode(this);
        this.allInstances = new ArrayList<Instance>();
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
    public void addInstance(Instance instance, Node from){
        super.addInstance(instance, null);
        this.memory.addInstance(instance);
        this.allInstances.add(instance);    
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
        if(memory.containsInstance(instance)){
            this.memory.removeInstance(instance);
            this.allInstances.remove(instance);
            this.rete.getChoiceUnit().addInstanceForRemovement(this, instance);
        }
        
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
        //TODO: Is this method used?
        this.memory.removeInstance(instance);
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
        this.memory.addInstance(instance);
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
    public String toString(){
        return "ChoiceNode: " + this.r;
    }
    
    /**
     * 
     * @return allInstances that are stored within this choice node
     */
    public ArrayList<Instance> getAllInstances(){
        return this.allInstances;
    }
    
    
}
