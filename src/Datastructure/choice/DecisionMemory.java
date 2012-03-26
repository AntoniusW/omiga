/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.Node;
import Datastructure.Rete.Rete;
import Entity.Instance;
import Entity.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The DecisionMemory is a storage for instances of nodes within a structor of decisionlevels.
 * First you register nodes to the decisionMemory by calling addNode.
 * Then when an instance reaches that Node you call addInstance, such that this instance is inserted into that storage
 * for that node at the actual decisionlevel. This way wehn you want to backtrack you can just call backtrack
 * and all instances will be removed, as for each instance of the lvl you called backtrack, removeInstance
 * for the corresponding node is called.
 * 
 * @param decisionLevel the depth of the actual decisionlevel
 * @param decisionLayer the datastructure we use to store the instances per node per decisonLevel
 * @param nodes here all nodes are registered that are later on saved within the decisionLayer.
 * 
 */
public class DecisionMemory {
    
    /*
     * How do we treat chocie nodes? Can we saver them like normal nodes?
     * Here something is removed when going back or so.
     */
       
    private int decisionLevel;
    private ArrayList<HashMap<Node,HashSet<Instance>>> decisionLayer;
    private ArrayList<Node> nodes;
    
    /**
     * public constructor. Creates a new DecisionMemory of decisionlevel 0
     */
    public DecisionMemory(){
        this.decisionLevel = -1; // start with -1 as addChocepoint increases this by one.
        this.nodes = new ArrayList<Node>();
        this.decisionLayer = new ArrayList<HashMap<Node,HashSet<Instance>>>(); //TODO: Use an ArrayList rather than a hashSet
        this.addChoicePoint();
    }
    
    public ArrayList<Node> getNodes(){
        return nodes;
    }
    
    /**
     * Used to register nodes such that the DecisionMemory knows which nodes are to handle, such that whenever a new decisionlevel
     * is created the new memory for all nodes can be allocated (Key ENtrys within the new HashMap).
     * 
     * @param n the node you want to register
     */
    public void addNode(Node n){
        this.nodes.add(n);
        this.decisionLayer.get(this.decisionLevel).put(n, new HashSet<Instance>());
    }
    
    /**
     * Increases the decisionlevel and creates memory for the new level
     */
    public void addChoicePoint(){
        this.decisionLevel++;
        this.decisionLayer.add(new HashMap<Node,HashSet<Instance>>());
        for(int i = 0; i < nodes.size();i++){
            this.decisionLayer.get(this.decisionLevel).put(nodes.get(i), new HashSet<Instance>());
        }
    }
    
    /**
     * Adds an instance to the decisionMemory, at the actual level
     * 
     * @param n the node the instance belongs to
     * @param instance the instance you want to add
     */
    public void addInstance(Node n, Instance instance){
        if(this.decisionLevel < 1) return;
        this.decisionLayer.get(this.decisionLevel).get(n).add(instance);
    }
    
    /**
     * decreases the decisionlevel by one, deletes all the instances from the nodes and removes the memory for that decisionlevel.
     */
    public void backtrack(){
        //System.out.println("STARTING BACKTRACKING!");
        for(Node n: this.decisionLayer.get(this.decisionLevel).keySet()){
            for(Instance inz: this.decisionLayer.get(this.decisionLevel).get(n)){
                if(n.getClass().equals(ChoiceNode.class)){
                    //System.out.println("Removing from ChoiceNode: " + n + " - " + inz);
                }
                //System.out.println("Trying to remove: " + inz + " from: " + n);
                n.simpleRemoveInstance(inz);
            }
        }
        this.decisionLayer.remove(this.decisionLevel);
        this.decisionLevel--;
        //System.out.println("FINISHED BACKTRACKING!");
    }
    
    /**
     * backtracks to the desired decisionlevel.
     * @param i the decisionlevel you want to backtrack.
     */
    public void backtrackTo(int i){
        while(this.decisionLevel > i) backtrack();
    }
    
    /**
     * prints the DecisionMemory to standard Out sorted by decisionlevel then Node, writing each Instance into one row.
     */
    public void printDecisionMemory(){
        for(int i = 0; i < this.decisionLayer.size();i++){
            System.out.println("DECISION LVL: " + i);
            for(Node n: this.decisionLayer.get(i).keySet()){
                System.out.println("Node: " + n);
                for(Instance inz: this.decisionLayer.get(i).get(n)){
                    System.out.println(inz);
                }
            }
        }
    }
    
    /**
     * 
     * @return the actual decisionlevel
     */
    public int getDecisonLevel(){
        return this.decisionLevel;
    }
    
}
