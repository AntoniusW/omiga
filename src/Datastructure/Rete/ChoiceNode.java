/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class ChoiceNode extends Node{
    
    private Rule r;
    private HeadNodeConstraint constraintNode;
    
    public ChoiceNode(Rete rete, int i, Rule r, HashMap<Variable,Integer> varPosition, HeadNodeConstraint constraintNode){
        super(rete);
        this.r = r;
        this.memory = new Storage(i);
        System.out.println("ChoiceNode created!");
        rete.getChoiceUnit().addChoiceNode(this);
        this.tempVarPosition = varPosition;
        this.constraintNode = constraintNode;
    }
    
    public HeadNodeConstraint getConstraintNode(){
        return this.constraintNode;
    }
    
    @Override
    public void addInstance(Instance instance, Node from){
        //super.addInstance(instance, null);
        System.out.println("Something is added into a choice node");
        this.memory.addInstance(instance);
    }
    
    @Override
    public void removeInstance(Instance instance){
//        rete.getChoiceUnit().AddInstanceRemovement(this, instance);
        this.memory.removeInstance(instance);
    }
    
    public Rule getRule(){
        return r;
    }
    
    public String toString(){
        return "ChoiceNode: " + this.r;
    }
    
}
