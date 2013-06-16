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
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * JoinNodes are store the joined variable assignments of two other nodes.
 * 
 * @param a the node that is the first join partner
 * @param b the node that is the second join partner
 * @param instanceOrdering defines how the variable assignment that is saved is calculated from to instance (one form a one from b). 2 means: take position 1 of node a. -1 means take position 0 of node b
 * @param selectionCriterion1 which position of an instance coming from node b resembles this position of node a, null means no one
 * @param selectionCriterion2 which position of an instance coming from node a resembles this position of node b, null means no one
 * 
 * @param selCrit1 temporary memory, for the selectionCriterion of Node a. (used within method addInstance)
 * @param selCrit2 temporary memory, for the selectionCriterion of Node b. (used within method addInstance)
 * @param tempVar A Variable that is used for the lookUp within addInstance. (It's a global variable so we do not have to create it all the time add instance is called)
 * 
 */
public class JoinNode extends Node{
    
    protected Node a;
    protected Node b;
    protected int[] instanceOrdering; // which position of which node goes where. 1 means position 0 of Node a, -2 means position 1 of Node b
    public Integer[] selectionCriterion1; // store the positions for a lookup
    public Integer[] selectionCriterion2;
    protected Term[] selCrit1; // Temporaere Vars fuer das selectionCriterion der anderen Node by INstance add
    protected Term[] selCrit2;
    
    protected Variable tempVar; // used as a Variable for the lookup
    
    /*
     * Select: Wir gehen einfach das Variable Ordering der anderen Node durch und setzen alle Variablen nach der Instanz davon
     */
    
    /**
     * 
     * public constructor. Creates a new joinNode with initialized data structures.
     * 
     * @param a the node that is the first join partner
     * @param bthe node that is the second join partner
     * @param rete the rete this join node is in
     */
    public JoinNode(Node a, Node b, Rete rete, HashMap<Variable,Integer> varPosA, HashMap<Variable,Integer> varPosB){
        super(rete); // registering this node within the ChoiceUnit

        this.a = a;
        this.b = b;

        //We initialize the instance Ordering array
        HashMap<Variable,Integer> temp = new HashMap<Variable, Integer>();
        temp.putAll(varPosA);
        temp.putAll(varPosB);
        this.instanceOrdering = new int[temp.size()];
        
        //We initialize this nodes memory
        if(instanceOrdering.length == 0){
            memory.initStorage(a.getMemory().arity + b.getMemory().arity);
            //this.memory = new Storage(a.getMemory().arity + b.getMemory().arity);
        }else{
            memory.initStorage(instanceOrdering.length);
            //this.memory = new Storage(instanceOrdering.length);
        }
        
        
        //We register this joinNode within it's two partners as child
        a.addChild(this);
        b.addChild(this);
        
        // We set the selectionCriterias by putting the number of the other predicates position for that position into the array
        this.selectionCriterion1 = new Integer[varPosA.size()];
        for(Variable v: varPosB.keySet()){
            if(varPosA.containsKey(v)){
                selectionCriterion1[varPosA.get(v)] = varPosB.get(v);
            }
        }
        this.selectionCriterion2 = new Integer[varPosB.size()];
        for(Variable v: varPosA.keySet()){
            if(varPosB.containsKey(v)){
                selectionCriterion2[varPosB.get(v)] = varPosA.get(v);
            }
        }
        selCrit1 = new Term[selectionCriterion1.length];
        selCrit2 = new Term[selectionCriterion2.length];
        
        //We initialize a temp variable. that is laters used for lookups
        this.tempVar = Variable.getVariable("temp:Var");
        
        //this.resetVarPosition(a,b);
        
        //We initialize the instanceOrdering: Variables of the Left Partner get numbers from 1 to arity
        //while Variables of the right partner get negative numbers from -1 to - arity
        HashMap<Variable,Integer> temp2 = new HashMap<Variable,Integer>();
        
        
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        //TODO: Hier muss der bug sein!
        // In the JoinNodeNegative we ush instances from theleft to the actual join node
        // but they are of different position if we do not add the left partners variables here first!
        //TODO. Remove the TODO above. Add text: This must be like this because we push instaces in neg joinnodes
        
        // We first add the left Partner. This is important since we push instances from left to right
        // in negative join nodes. If we did not do it like this, the var ordering would not match anymore.
        // A recalculation on adding would cost time. So stay with this.
        for(Variable v: varPosB.keySet()){
            temp2.put(v, varPosB.get(v));
            instanceOrdering[varPosB.get(v)] = (varPosB.get(v)+1)*-1;// +1  because we have to distinguish between Node a,b. and +0 = -0. SO we map the positions to 1..n, -1..-n
            //instanceOrdering[temp2.size()-1] = (varPosB.get(v)+1)*-1; // +1  because we have to distinguish between Node a,b. and +0 = -0. SO we map the positions to 1..n, -1..-n
        }
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        for(Variable v: varPosA.keySet()){
            if(!temp2.containsKey(v)){
                temp2.put(v,temp2.size());
                instanceOrdering[temp2.size()-1] = (varPosA.get(v)+1);
            }
            
        }

        this.tempVarPosition = temp2;
    }
    
    /**
     * 
     * Informs the joinNode of a new instance of it's parent nodes. The join node then 
     * calculates all joined variable assignments for this new instance, and adds them
     * to it's memory.
     * 
     * @param instance the new instance that has arrived
     * @param n the node where the instance arrived (a or b)
     */
    public void addInstance(Instance instance, Node fromNode){
        boolean from; // TODO: check boolean value
        if( a == fromNode) {
            from = true;
        } else if ( b == fromNode ) {
            from = false;
        } else {
            throw new RuntimeException("JoinNode received input from unknown parent node");
        }
        //System.err.println("ADD Instance called: " + instance + " -" + from + " - " + this);
        Term[] selectionCriteria; // TODO: check if putting thise two variable outside this method decreases runtime
        Node selectFromHere; // TODO: check if putting thise two variable outside this method decreases runtime
        
        // we determine from which joinpartner the instance arrived, and build our selectionCriteria accordingly
        // by setting the position to be a variable if selectionCriterionX is null, and to the term of the insatnce's position
        // otherwise. This way we'll obtain all joinable instances from the other joinpartner.
        if(from){
            //The instance came from node a
            selectFromHere = b;
            for(int i = 0; i < selectionCriterion2.length;i++){
                if (selectionCriterion2[i] == null){
                    selCrit2[i] = tempVar;
                }else{
                    selCrit2[i] = instance.get( selectionCriterion2[i]);
                }
            }
            selectionCriteria = selCrit2;
        }else{
            //the instance came from node b
            selectFromHere = a;
            for(int i = 0; i < selectionCriterion1.length;i++){
                if (selectionCriterion1[i] == null){
                    selCrit1[i] = tempVar;
                }else{
                    selCrit1[i] = instance.get( selectionCriterion1[i]);
                }
            }
            selectionCriteria = selCrit1;
        }
        
        // We select all instances that match the selectionCriterion, and therefore are joinable with the new instance, from the other node
        Collection<Instance> joinPartners = selectFromHere.select(selectionCriteria);

        // for each joinpartner we build a variable assignment we then add to this joinnodes memory
        for(Instance varAssignment: joinPartners){
            /*int decisionLevel;
            int propagationLevel;
            // use higher decision level
            if( instance.decisionLevel > varAssignment.decisionLevel ) {
                decisionLevel = instance.decisionLevel;
                propagationLevel = instance.propagationLevel;
            } else if ( instance.decisionLevel == varAssignment.decisionLevel ) {
                decisionLevel = varAssignment.decisionLevel;
                // use higher propagation level if both decision levels are equal
                propagationLevel = instance.propagationLevel > varAssignment.propagationLevel ? 
                        instance.propagationLevel : varAssignment.propagationLevel;
            } else {
                decisionLevel = varAssignment.decisionLevel;
                propagationLevel = varAssignment.propagationLevel;                
            }*/
            Term[] toAdd = new Term[this.instanceOrdering.length];
            for(int i = 0; i < this.instanceOrdering.length;i++){
                if(from){
                    if(instanceOrdering[i] < 0){
                        toAdd[i] = varAssignment.get((instanceOrdering[i]*-1)-1);
                    }else{
                        toAdd[i] = instance.get(instanceOrdering[i] - 1);
                    }
                }else{
                    if(instanceOrdering[i] >= 0){
                        toAdd[i] = varAssignment.get(instanceOrdering[i] - 1);
                    }else{
                        toAdd[i] = instance.get((instanceOrdering[i]*-1)-1);
                    }
                }
            }
            Instance tempInst = Instance.getInstance(toAdd, 0, 0);
            Instance instance2Add = newInstanceFromJoin(varAssignment, instance, tempInst);
            //Instance instance2Add = Instance.getInstance(toAdd, propagationLevel, decisionLevel);

            memory.addInstance(instance2Add);
            //super.addInstance(instance2Add); // register the adding of this variableassignment within the choiceUnit
            // We inform all children of this node that a new instance has arrived
            
            for (Node child : children) {
                sendInstanceToChild(instance2Add, child);
            }
        }
    }
    
    /**
     * 
     * @return the string representation of this join node
     */
    @Override
    public String toString(){
        return "JoinNode" + this.hashCode() + ": " + "[" + this.b.toString() + "] [" + this.a.toString() + "] ";
    }
    
    //Actually this method is only called for JoinNodeNegative
    


    @Override
    public void addInstance(Instance instance) {
        throw new RuntimeException("JoinNode.addInstance called without parameter fromNode.");
    }
    
    /**
     * Helper for joining: creates a new instance based on another instance
     * where decision level and propagation level are set according to two
     * joined instances.
     *
     * @param joinA join partner one
     * @param joinB join partner two
     * @param base the instance the returned node is based on
     * @return a (shallow) copy of base where DL and PL are the maximum of joinA
     * and joinB
     */
    public static Instance newInstanceFromJoin(Instance joinA, Instance joinB, Instance base) {
        int decisionLevel;
        int propagationLevel;
        // use higher decision level
        if (joinA.decisionLevel > joinB.decisionLevel) {
            decisionLevel = joinA.decisionLevel;
            propagationLevel = joinA.propagationLevel;
        } else if (joinA.decisionLevel == joinB.decisionLevel) {
            decisionLevel = joinB.decisionLevel;
            // use higher propagation level if both decision levels are equal
            propagationLevel = joinA.propagationLevel > joinB.propagationLevel
                    ? joinA.propagationLevel : joinB.propagationLevel;
        } else {
            decisionLevel = joinB.decisionLevel;
            propagationLevel = joinB.propagationLevel;
        }
        Instance ret = new Instance(base);
        ret.decisionLevel = decisionLevel;
        ret.propagationLevel = propagationLevel;
        ret.isMustBeTrue = ( joinA.isMustBeTrue || joinB.isMustBeTrue );
        return ret;
    }
    
    
}
