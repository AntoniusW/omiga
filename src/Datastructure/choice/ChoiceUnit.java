/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.DependencyGraph.DGraph;
import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.HeadNodeConstraint;
import Datastructure.Rete.Node;
import Datastructure.Rete.Unifyer;
import Entity.Atom;
import Entity.ContextASP;
import Entity.Instance;
import Entity.Pair;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;
import java.util.*;
import network.ReplyMessage;
import Datastructure.Rete.Node;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The choiceUnit controls all the choices that are made.
 * 
 * @param memory The DecisionMemory used to store the instances per node per decision level
 * @param choiceNodes a List of ChoiceNodes, such that the choice Unit knows on what to make a choice on
 * @param c The context this choiceUnit should work on
 * 
 * @param stackyNode a stack were we push nodes on which's instance we guessed
 * @param stackybool a stack were we push if the last guess was a positive or negative one
 * @param stackyInstance a stack were we push the instance guessed on
 * 
 * @param nextNode when null a positive guess has to be done on the next possible choice. Otherwise we have to guess negativly on this node with the nextInstance
 * @param nextInstance the instance we have to guess negativly on nextNode if nextNode is not null.
 * 
 * 
 * @param choiceNodesDecisionLayer A Datastructure for storing instances fo choiceNodes per decisionlevel. Theses insatnces have to be added after backtracking
 */
public class ChoiceUnit {
    
    //TODO: remove choice node instances if they'll fail (because a negative atom is already positive)
    
    //TODO: Is there a problem if we guess, and remove choiceNodes other than the one we guessed on, because propagation lead to some headNodefullfillment. These nodes are not reverted?
    
    // TODO: HeadNodeCOnstraints are not treated correctly (remove is called to often why?
    
    public ContextASP c;
    protected ArrayList<ChoiceNode> choiceNodes;
    protected DecisionMemory memory;
    
    protected Stack<ChoiceNode> stackyNode;
    protected Stack<Boolean> stackybool;
    protected Stack<Instance> stackyInstance;
    protected ChoiceNode nextNode = null;
    protected Instance nextInstance = null;
    
    protected ArrayList<HashMap<ChoiceNode,HashSet<Instance>>> choiceNodesDecisionLayer;
    
    
    
    public ChoiceUnit(){
        
    }
    
    /**
     * 
     * public constructor. Creates a new ChoiceUnit with initialized data structures.
     * 
     * @param c The ASP Context you want to use this choice unit for
     */
    public ChoiceUnit(ContextASP c){
        this.memory = new DecisionMemory();
        this.choiceNodes = new ArrayList<ChoiceNode>();
        this.c = c;
        this.stackyNode = new Stack<ChoiceNode>();
        this.stackybool = new Stack<Boolean>();
        this.stackyInstance = new Stack<Instance>();
        this.choiceNodesDecisionLayer = new ArrayList<HashMap<ChoiceNode,HashSet<Instance>>>();
        this.choiceNodesDecisionLayer.add(new HashMap<ChoiceNode,HashSet<Instance>>());
    }
    
    protected void closeActualSCC(){
        c.getRete().propagate();
        for(Predicate p: SCCPreds.get(actualSCC)){
           if(c.getRete().containsPredicate(p, false)) {
               //System.out.println("Closing Predicate: " + p);
               c.getRete().getBasicNodeMinus(p).close();
           }
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());
    }
    
    protected void openActualSCC(){
        this.actualSCC--;
        for(Predicate p: SCCPreds.get(actualSCC)){
           if(c.getRete().containsPredicate(p, false)) c.getRete().getBasicNodeMinus(p).unclose();
        }
        this.closedAt.pop();
    }
    
    public int getCurrentSCC() {
        return actualSCC;
    }
    
    public int getPredicateSCC(Predicate pred) {
        for (int i = 0; i < SCCPreds.size(); i++) {
            ArrayList<Predicate> preds_in_scc = SCCPreds.get(i);
            if( preds_in_scc.contains(pred)) {
                return i;
            }
        }
        // pred is not found, hence pred did not occur in any rule,
        // hence it only occurs as facts or in constraints: thus it is in SCC 0
        return 0;
    }
    
    /**
     * 
     * 
     * 
     * @return if there was a guess that was made, or if there are no more guesses left
     */
    //int i = 0; //TODO: Remove this counter
    public boolean choice(){
        throw new UnsupportedOperationException("ChoiceUnit.choice() called. Work does ChoiceUnitRewrite.");
    }
    
    public void addChoicePoint(){
        this.memory.addChoicePoint();
        this.IncreasechoiceNodesDecisionLayer();
    }
    
    /**
     * 
     * registers a ChoiceNode
     * 
     * @param cN the choice node you want to register in this choiceunit
     */
    public void addChoiceNode(ChoiceNode cN){
        this.choiceNodesDecisionLayer.get(0).put(cN, new HashSet<Instance>());
        this.choiceNodes.add(cN);
    }
    
     /**
     * 
     * Prints all choiceNodes and their instances contained, to standard out.
     * 
     */
    public void printAllChoiceNodes(){
        System.out.println("Printing choice nodes");
        System.out.println("_________________________");
        for(ChoiceNode cN: choiceNodes){
            System.out.println("ChoiceNode: " + cN);
            Term[] selectionCriteria = new Term[cN.getVarPositions().size()];
            for(int i = 0; i < cN.getVarPositions().size();i++){
                selectionCriteria[i] = Variable.getVariable("X");
            }
            for(Instance i: cN.select(selectionCriteria)){
                System.out.println("Instance: " + i);
            }
        }
        System.out.println("_________________________");
    }
    
    
    /**
     * Adds the next Decisionlevel to the choiceNodeDecisionLayer
     */
    public void IncreasechoiceNodesDecisionLayer(){
        HashMap<ChoiceNode,HashSet<Instance>> hm = new HashMap<ChoiceNode,HashSet<Instance>>();
        for(ChoiceNode cN: this.choiceNodes){
            hm.put(cN, new HashSet<Instance>());
        }
        this.choiceNodesDecisionLayer.add(hm);
    }
    
    /**
     * prints the decision memory to standard out
     */
    public void printDecisionMemory(){
        memory.printDecisionMemory();
    }
    
    /**
     * 
     * registers a node in the decisionmemory such that the decision memory can initialize required space for that node
     * 
     * @param n The node you want to register in the decision memory.
     */
    public void addNode(Node n){
        this.memory.addNode(n);
    }
       
     /**
     * backtrack one decisionlevel and bring memory back to state before last guess
     */
    public void backtrack3(){
    }
    
    
    
    /**
     * 
     * @return the actual decision level
     */
    public int getDecisionLevel(){
        return this.memory.getDecisonLevel();
    }
    
    protected ArrayList<ArrayList<ChoiceNode>> SCC;
    protected ArrayList<ArrayList<Predicate>> SCCPreds;
    protected ArrayList<Integer> SCCSize;       // number of ChoiceNodes in SCC
    protected int actualSCC;
    protected Stack<Integer> closedAt;
    public void DeriveSCC(){
        //System.out.println("DeriveSCC called in ChoiceUnit.");
        SCC = new ArrayList<ArrayList<ChoiceNode>>();
        SCCPreds = new ArrayList<ArrayList<Predicate>>();
        SCCSize = new ArrayList<Integer>();
        actualSCC = 0;
        this.closedAt = new Stack<Integer>();
        DGraph g = new DGraph();
        for(Rule r: c.getAllRules()){
            g.addRule(r);
        }
        //g.gd.addEdge(Predicate.getPredicate("p",2), Predicate.getPredicate("s",1));
        // for each SCC
        ArrayList<ArrayList<Predicate>> sorted_scc = g.getSortedSCCs();
        for(int i = 0; i < sorted_scc.size();i++){
            SCC.add(new ArrayList<ChoiceNode>());
            SCCPreds.add(sorted_scc.get(i));
            //SCCSize.add(g.getSortedSCCs().get(i).size());
            // for every predicate in an SCC
            for(int j = 0; j < sorted_scc.get(i).size();j++){
               for(ChoiceNode cN: this.choiceNodes){
                   // find the ChoiceNode to the predicate
                   if(cN.getRule().getHead().getPredicate().equals(g.getSortedSCCs().get(i).get(j))){
                       SCC.get(i).add(cN);
                   }
               }
            }
             SCCSize.add(SCC.get(i).size());
        }
    }
    
    public boolean killSoloSCC(){
        if(SCCSize.isEmpty()) {
            return false;
        }
        while(SCCSize.get(actualSCC)<=1){
            if(SCCSize.get(actualSCC)==1 && SCCPreds.get(actualSCC).size() > 1) break;
            this.closeActualSCC();
            if(actualSCC >= SCC.size()) return false;
        }
        return true;
    }

    /**
     * used for the MCS calculation. returns wether there is a next alternative to the actual guess
     * @return 
     */
    public boolean nextBranch(){
        if(this.nextNode != null) return true;
        return false;
    }
    
    public ArrayList<LinkedList<Pair<Node,Instance>>> deriveNewFactsSindsDecisionLevel(){
        return this.memory.deriveNewFactsSindsDecisionLevel();
    }

    public ReplyMessage choicePlusInfo() {
        throw new UnsupportedOperationException("Not yet implemented");
    }    
}
