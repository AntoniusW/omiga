package Datastructure.Rete;

import Entity.Instance;
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
        memory.initStorage(arity);
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
     * A normal Instance reaches this node. Check if this instance is contained within the nodes memory.
     * If so yield unsatisfiable.
     * 
     * @param instance 
     */
    @Override
    public void addInstance(Instance instance){
            
            
        if(this.memory.containsInstance(instance)){
            rete.satisfiable = false;
            System.out.println("UNSATISFIABLE because of HeadConstraintNode! Printing interpretation: " + instance);
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
