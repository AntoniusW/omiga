/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
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
    
    private Node a;
    private Node b;
    private int[] instanceOrdering; // which position of which node goes where. 1 means position 0 of Node a, -2 means position 1 of Node b
    private Integer[] selectionCriterion1; // store the positions for a lookup
    private Integer[] selectionCriterion2;
    private Term[] selCrit1; // Temporaere Vars fuer das selectionCriterion der anderen Node by INstance add
    private Term[] selCrit2;
    
    private Variable tempVar; // used as a Variable for the lookup
    
    
    /**
     * 
     * resets the tempVarPosition by clearing it and then adding all variables from both parentNodes.
     * This method also initializes the instanceOrdering, where the variables of node a are set to
     * the first n positions, and the variables from b, that are not contained in a, make up the rest.
     * 
     * @param a the node that is the first join partner
     * @param b the node that is the second join partner
     */
    public void resetVarPosition(Node a, Node b){
        tempVarPosition.clear();
        HashMap<Variable,Integer> termsA = a.getVarPositions();
        HashMap<Variable, Integer> termsB = b.getVarPositions();
        // first we add all Variables of Node a (These will reach from 0 to a.arity)
        for(Variable v: termsA.keySet()){
            this.tempVarPosition.put(v,a.getVarPositions().get(v));
            instanceOrdering[a.getVarPositions().get(v)] = a.getVarPositions().get(v)+1; // +1  because we have to distinguish between Node a,b. and +0 = -0. SO we map the positions to 1..n, -1..-n
            //System.err.println("Got: " + v)
        }
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        for(Variable v: termsB.keySet()){
            if(!this.tempVarPosition.containsKey(v)){
                this.tempVarPosition.put(v,this.tempVarPosition.size());
                instanceOrdering[this.tempVarPosition.size()-1] = (b.getVarPositions().get(v)+1)*-1;
            }
            
        }
        /*String testTemp = "[";
        for(int i: instanceOrdering){
            testTemp = testTemp + i + ",";
        }
        testTemp = testTemp + "]";
        System.err.println("LOLRAGARIAGA: " + testTemp);*/
    }
    
    
    
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
    public JoinNode(Node a, Node b, Rete rete){
        super(rete); // registering this node within the ChoiceUnit

        this.a = a;
        this.b = b;

        HashMap<Variable,Integer> temp = new HashMap<Variable, Integer>();
        temp.putAll(a.getVarPositions());
        temp.putAll(b.getVarPositions());
        this.instanceOrdering = new int[temp.size()];
        
        this.memory = new Storage(instanceOrdering.length);
        
        this.a.addChild(this);
        this.b.addChild(this);
        
        // We set the selectionCriterias by putting the number of the other predicates position for that position into the array
        this.selectionCriterion1 = new Integer[a.getVarPositions().size()];
        for(Variable v: b.getVarPositions().keySet()){
            if(a.getVarPositions().containsKey(v)){
                selectionCriterion1[a.getVarPositions().get(v)] = b.getVarPositions().get(v);
            }
        }
        this.selectionCriterion2 = new Integer[b.getVarPositions().size()];
        for(Variable v: a.getVarPositions().keySet()){
            if(b.getVarPositions().containsKey(v)){
                selectionCriterion2[b.getVarPositions().get(v)] = a.getVarPositions().get(v);
            }
        }
        selCrit1 = new Term[selectionCriterion1.length];
        selCrit2 = new Term[selectionCriterion2.length];
        
        this.tempVar = Variable.getVariable("temp:Var");
        this.resetVarPosition(a, b); // init tempVarPositions and instanceordering
        
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
    @Override
    public void addInstance(Instance instance, Node n){
        Term[] selectionCriteria; // TODO: check if putting thise two variable outside this method decreases runtime
        Node selectFromHere; // TODO: check if putting thise two variable outside this method decreases runtime
        
        // we determine from which joinpartner the instance arrived, and build our selectionCriteria accordingly
        // by setting the position to be a variable if selectionCriterionX is null, and to the term of the insatnce's position
        // otherwise. This way we'll obtain all joinable instances from the other joinpartner.
        if(n.equals(a)){
            //The instance came from node a
            selectFromHere = b;
            for(int i = 0; i < selectionCriterion2.length;i++){
                if (selectionCriterion2[i] == null){
                    selCrit2[i] = tempVar;
                }else{
                    selCrit2[i] = instance.get( selectionCriterion2[i]);
                }
            }
            selectionCriteria = selCrit2;;
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
            Term[] toAdd = new Term[this.instanceOrdering.length];
            for(int i = 0; i < this.instanceOrdering.length;i++){
                if(n.equals(a)){
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
            Instance instance2Add = Instance.getInstance(toAdd);
            //System.out.println(this + "Adding " + instance2Add + " because of " + instance + " from " + n);
            this.memory.addInstance(instance2Add);
            super.addInstance(instance2Add, this); // register the adding of this variableassignment within the choiceUnit
            
            // We inform all children of this node that a new instance has arrived
            for(Node child: this.children){
                child.addInstance(instance2Add,this);
            }
        }
    }
    
    /**
     * 
     * @return the string representation of this join node
     */
    @Override
    public String toString(){
        return "JoinNode: " + this.a.toString() + this.b.toString();
    }
    
    
    
    
}
