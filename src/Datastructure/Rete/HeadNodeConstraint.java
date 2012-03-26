/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * Each rule with a negative body leads to a HeadNodeConstraint. here constraints for this rule are stored.
 * These constraints are created during negative guesses within the ChoiceUnit.
 * Whenever an instances reaches a headNode it also reaches the HeadConstraintNode. And if this instance
 * is constrained, unsatisfiable is reached.
 * 
 * @param cN the corresponding choiceNode
 * 
 */
public class HeadNodeConstraint extends Node{
    
    //TODO: This nodes do not work. Somehow nothing is added here!
    
    private ChoiceNode cN;
    
    /**
     * 
     * public constructor. Creates a new HeadNodeConstraint with initialized memory.
     * 
     * @param rete The rete network this node is on
     * @param arity the arity of the added instances
     */
    public HeadNodeConstraint(Rete rete, int arity){
        super(rete); // HeadNodeConstraints are saved within the DecisionMemory of the choiceUnit!
        this.memory = new Storage(arity);
        //this.rete.getChoiceUnit().addNode(this);
    }
    
    /**
     * 
     * @param cN the choice node you want to set
     */
    public void setChoicenode(ChoiceNode cN){
        this.cN = cN;
    }
    
    /**
     * 
     * saves a constraint to this node.
     * 
     * @param instance The instance that should not reach the node. (If it does, unsatisfiability is reached)
     */
    public void saveConstraintInstance(Instance instance){
        // we call super because super only handles the istance storage in the choice layer.
        // so we can use it althaugh this is not the addFunction in common sense
        super.addInstance(instance, true);
        this.memory.addInstance(instance);
        //System.out.println("The constraintNode removes an instance: " + this + " becasue of: " + instance);
        this.cN.removeInstance(instance); // we remove the corresponding instance from the choice node, since this instance shall not be true, and therefore should never be guessed
    }
    
    /*public void removeInstance(Instance inz){
        System.out.println("TRying to remove: " + inz + " from memory of size: " + this.memory.arity);
        this.memory.removeInstance(inz);
    }/*/
    
    /**
     * 
     * A normal Instance reaches this node. Check if this instance is contained within the nodes memory.
     * If so yield unsatisfiable.
     * 
     * @param instance 
     */
    @Override
    public void addInstance(Instance instance, boolean from){
        /*Term[] selectionCriteria = new Term[instance.getSize()];
            for(int j = 0; j < instance.getSize();j++){
                selectionCriteria[j] = Variable.getVariable("X");
            }
            System.out.println("Instanzen der HeadConstraint Node!: " + this.memory.select(selectionCriteria).size());
            for(Instance inz: this.memory.select(selectionCriteria)){
                System.out.println("Instance: " + inz);
            }*/
            
            
        if(this.memory.containsInstance(instance)){
            rete.satisfiable = false;
            //System.out.println("UNSATISFIABLE because of HeadConstraintNode! Printing AnswerSet: " + instance);
            //rete.printAnswerSet();
        }
    }
    
    /**
     * 
     * @return All Instances of this memory
     */
    public ArrayList<Instance> getAllInstances(){
        return this.memory.getAllInstances();
    }
    
    
}
