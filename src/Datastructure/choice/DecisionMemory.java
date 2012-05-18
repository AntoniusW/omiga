/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.Node;
import Datastructure.Rete.Rete;
import Entity.Instance;
import Entity.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import network.Pair;

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
    //private ArrayList<HashMap<Node,HashSet<Instance>>> decisionLayer;
    private ArrayList<LinkedList<Pair<Node,Instance>>> decisionLayer;
    private ArrayList<Node> nodes;
    
    /**
     * public constructor. Creates a new DecisionMemory of decisionlevel 0
     */
    public DecisionMemory(){
        this.decisionLevel = -1; // start with -1 as addChocepoint increases this by one.
        this.nodes = new ArrayList<Node>();
        //this.decisionLayer = new ArrayList<HashMap<Node,HashSet<Instance>>>(); //TODO: Use an ArrayList rather than a hashSet
        this.decisionLayer = new ArrayList<LinkedList<Pair<Node,Instance>>>();
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
        //NOT NEEDED ANZMORE SINCE WE NOW USE A LIST of LISTS of PAIRS,no need to register nodes
        //this.nodes.add(n);
        //this.decisionLayer.get(this.decisionLevel).put(n, new LinkedList<Instance>());
        //this.decisionLayer.get(this.decisionLevel).put(n, new HashSet<Instance>());
    }
    
    /**
     * Increases the decisionlevel and creates memory for the new level
     */
    public void addChoicePoint(){
        System.out.println("DecisionMemory.addChoicePoint: method entry.");
        this.decisionLevel++;
        this.decisionLayer.add(new LinkedList<Pair<Node,Instance>>());
        
        /*System.out.println("DecisionMemory.addChoicePoint: added new HashMap to decisionLayer.");
        System.out.println("DecisionLevel is: "+decisionLevel);
        for(int i = 0; i < nodes.size();i++){
            try {
                if(decisionLevel > 277)
                    System.out.println("Nodes.size is: "+nodes.size() + " i="+i);
                HashSet<Instance> new_hs = new HashSet<Instance>();
                if(decisionLevel >277)
                    System.out.println("New HashSet created.");
                this.decisionLayer.get(this.decisionLevel).put(nodes.get(i), new_hs);
                if(decisionLevel >277)
                    System.out.println("HashSet put.");
            } catch (Exception e) {
                System.out.println("Some exception occured."+e);
                e.printStackTrace();
            }
            
        }*/
        System.out.println("DecisionMemory.addChoicePoint: method end.");
    }
    
    /**
     * Adds an instance to the decisionMemory, at the actual level
     * 
     * @param n the node the instance belongs to
     * @param instance the instance you want to add
     */
    public void addInstance(Node n, Instance instance){
        //if(this.decisionLevel < 1) return;  // TODO AW out-commented this, as it leads to ignoring all facts, was there another reason for this?
       this.decisionLayer.get(decisionLevel).add(new Pair<Node, Instance>(n,instance));
        // this.decisionLayer.get(this.decisionLevel).get(n).add(instance);
    }
    
    /**
     * decreases the decisionlevel by one, deletes all the instances from the nodes and removes the memory for that decisionlevel.
     */
    public void backtrack(){
        //System.out.println("STARTING BACKTRACKING!");
        for(Pair<Node,Instance> pa: this.decisionLayer.get(decisionLevel)){
            pa.getArg1().simpleRemoveInstance(pa.getArg2());
        }
        /*for(Node n: this.decisionLayer.get(this.decisionLevel).keySet()){
            for(Instance inz: this.decisionLayer.get(this.decisionLevel).get(n)){
                //System.out.println("Trying to remove: " + inz + " from: " + n);
                n.simpleRemoveInstance(inz);
            }
        }*/
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
            for(Pair<Node,Instance> pa: this.decisionLayer.get(decisionLevel)){
                System.out.println(pa);
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
    
    /**
     * 
     * @param lvl the Decisionlevel from which on you want all added facts
     * @return A HashMap of Predicates with corrsponding instances
     */
    public ArrayList<LinkedList<Pair<Node,Instance>>> deriveNewFactsSindsDecisionLevel(){
        
        return this.decisionLayer;
        
    }
    
}
