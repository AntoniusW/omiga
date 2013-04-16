/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.Node;
import Entity.Instance;
import java.util.ArrayList;
import java.util.LinkedList;
import Entity.Pair;
import java.util.HashMap;

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
        //System.out.println("DecisionMemory.addChoicePoint: method entry.");
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
        //System.out.println("DecisionMemory.addChoicePoint: method end.");
    }
    
    /**
     * Adds an instance to the decisionMemory, at the decision level of the
     * given instance.
     * 
     * @param n the node the instance belongs to
     * @param instance the instance to add
     */
    public void addInstance(Node n, Instance instance){
        decisionLayer.get(instance.decisionLevel).add(new Pair<Node, Instance>(n,instance));
        //if(this.decisionLevel < 1) return;  // TODO AW out-commented this, as it leads to ignoring all facts, was there another reason for this?
// previous code      this.decisionLayer.get(decisionLevel).add(new Pair<Node, Instance>(n,instance));
        // this.decisionLayer.get(this.decisionLevel).get(n).add(instance);
    }
    
    /**
     * decreases the decisionlevel by one, deletes all the instances from the nodes and removes the memory for that decisionlevel.
     */
    public void backtrack(){
        Node n;
        for (Node node : Node.nodes) {
            node.backtrackTo(decisionLevel-1);
        }

        // TODO remove/change code below
        //System.out.println("STARTING BACKTRACKING!");
//        for(Pair<Node,Instance> pa: this.decisionLayer.get(decisionLevel)){
//            pa.getArg1().simpleRemoveInstance(pa.getArg2());
//        }
        /*for(Node n: this.decisionLayer.get(this.decisionLevel).keySet()){
            for(Instance inz: this.decisionLayer.get(this.decisionLevel).get(n)){
                //System.out.println("Trying to remove: " + inz + " from: " + n);
                n.simpleRemoveInstance(inz);
            }
        }*/
//        this.decisionLayer.remove(this.decisionLevel);
        decisionLevel--;
        //System.out.println("FINISHED BACKTRACKING!");
    }
    
    /**
     * backtracks to the desired decisionlevel.
     * @param i the decisionlevel you want to backtrack.
     */
    public void backtrackTo(int i){
        for (Node node : Node.nodes) {
            node.backtrackTo(i);
        }
        decisionLevel = i-1;    // choice adds 1 to decisionLevel at first
//        while(this.decisionLevel > i) backtrack();
    }
    
    /**
     * prints the DecisionMemory to standard Out sorted by decisionlevel then Node, writing each Instance into one row.
     */
    public void printDecisionMemory(){
        System.out.println("Decision memory:");
        for (Node node : Node.nodes) {
                if( node instanceof ChoiceNode ) {
                    System.out.println("  Node "+node.toString());
                    System.out.print("    ");
                    ((ChoiceNode)node).printChoiceNodeInstances();
                    continue;
                }
        }
        if( true ) {    // debug only
            return;
        }
        for (int i = 0; i <= decisionLevel; i++) {
            System.out.println("DecisionLevel: "+i);
            for (Node node : Node.nodes) {
                if( node instanceof ChoiceNode ) {
                    System.out.println("  Node "+node.toString());
                    System.out.print("    ");
                    ((ChoiceNode)node).printChoiceNodeInstances();
                    continue;
                }
                if( node.getMemory().getBacktrackInstances() == null) {
                    continue;
                }
                ArrayList<Instance> instances_in_dl = node.getMemory().getBacktrackInstances().get(i);
                if( instances_in_dl != null && !instances_in_dl.isEmpty()) {
                    System.out.println("  Node "+node.toString());
                    System.out.print("    ");
                    for (Instance instance : instances_in_dl) {
                        System.out.print(instance.toString());
                        System.out.print(" ");
                    }
                    System.out.println();
                }
            }
            
        }
        System.out.println("DecisionLevels end.");
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
        // search all nodes for instances in new decision
        
        return this.decisionLayer;
        
    }
    
    /**
     * Debug helper to find missing entries in the decision memory.
     * @return 
     */
    public boolean debug_isEveryInstanceCovered() {
        if( true) { // debug: disable method
            return true;
        }
        // collect all instances of all nodes
        ArrayList<Pair<Node,Instance>> all_instances = new ArrayList<Pair<Node, Instance>>();
        for (Node node : Node.nodes) {
            if( node.getMemory() == null || node instanceof ChoiceNode) {
                continue;
            }
            for (Instance inst : node.getMemory().getAllInstances()) {
                all_instances.add(new Pair<Node, Instance>(node, inst));
            }
        }
        
        // check that every one is covered in the decisionLayer
        for (Pair<Node, Instance> inst : all_instances) {
            // skip facts
            if( inst.getArg2().decisionLevel == 0) {
                continue;
            }
            boolean covered = false;
            int decLevelCounter = -1;
            for (LinkedList<Pair<Node, Instance>> dL : decisionLayer) {
                decLevelCounter++;
                for (Pair<Node, Instance> dec_pair : dL) {
                    if( dec_pair.getArg1() == inst.getArg1() && dec_pair.getArg2() == inst.getArg2()) {
                        covered = true;
                        // check if decision levels mismatch
                        if( decLevelCounter != inst.getArg2().decisionLevel ) {
                            continue;
                        }
                    }
                }
            }
            if ( !covered ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets decisionLevel.
     * @param decisionLevel 
     */
    public void setDecisionLevel(int decisionLevel) {
        this.decisionLevel = decisionLevel;
    }
}
