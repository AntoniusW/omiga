/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.Node;
import Datastructure.Rete.Rete;
import Entity.Instance;
import Entity.Predicate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class DecisionMemory {
    
    /*
     * How do we treat chocie nodes? Can we saver them like normal nodes?
     * Here something is removed when going back or so.
     */
    
    private Rete rete;
    
    private int decisionLevel;
    
    private ArrayList<HashMap<Node,ArrayList<Instance>>> decisionLayer;
    private ArrayList<Node> nodes;
    
    /**
     * public constructor. Creates a new DecisionMemory of decisionlevel 0
     */
    public DecisionMemory(){
        this.decisionLevel = -1; // start with -1 as addChocepoint increases this by one.
        this.nodes = new ArrayList<Node>();
        this.decisionLayer = new ArrayList<HashMap<Node,ArrayList<Instance>>>();
        this.addChoicePoint();
    }
    
    /**
     * Used to register nodes such that the DecisionMemory knows which nodes are to handle, such that whenever a new decisionlevel
     * is created the new memory for all nodes can be allocated (Key ENtrys within the new HashMap).
     * 
     * @param n the node you want to register
     */
    public void addNode(Node n){
        this.nodes.add(n);
        this.decisionLayer.get(this.decisionLevel).put(n, new ArrayList<Instance>());
    }
    
    /**
     * Increases the decisionlevel and creates memory for the new level
     */
    public void addChoicePoint(){
        this.decisionLevel++;
        this.decisionLayer.add(new HashMap<Node,ArrayList<Instance>>());
        for(int i = 0; i < nodes.size();i++){
            this.decisionLayer.get(this.decisionLevel).put(nodes.get(i), new ArrayList<Instance>());
        }
    }
    
    /**
     * Adds an instance to the decisionMemory, at the actual level
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
        for(Node n: this.decisionLayer.get(this.decisionLevel).keySet()){
            for(int i = 0; i < this.decisionLayer.get(this.decisionLevel).get(n).size();i++){
                n.removeInstance(this.decisionLayer.get(this.decisionLevel).get(n).get(i));
            }
        }
        this.decisionLayer.remove(this.decisionLevel);
        this.decisionLevel--;
    }
    
    /**
     * backtracks to the desired decisionlevel.
     * @param i the decisionlevel you want to backtrack.
     */
    public void backtrackTo(int i){
        while(this.decisionLevel > i) backtrack();
    }
    
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
    
    public int getDecisonLevel(){
        return this.decisionLevel;
    }
    
}
