/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.DependencyGraph.DGraph;
import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.HeadNode;
import Datastructure.Rete.HeadNodeConstraint;
import Datastructure.Rete.Node;
import Datastructure.Rete.Unifyer;
import Entity.Atom;
import Entity.Constant;
import Entity.ContextASP;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Exceptions.FactSizeException;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import org.jgrapht.graph.DirectedSubgraph;


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
    
    protected ContextASP c;
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
    
    private void closeActualSCC(){
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
    
    /**
     * 
     * 
     * 
     * @return if there was a guess that was made, or if there are no more guesses left
     */
    int i = 0; //TODO: Remove this counter
    public boolean choice(){
        //System.out.println("CHOICE IS CALLED!");
        //System.out.println("Choice is called!");
        //TODO: replace foreach loops with iterator loops.
        i++;
        /*if(i==471) {
            c.printAnswerSet();
            c.getRete().printRete();
        }*/
        //System.out.println("Choice is called! " + this.memory.getDecisonLevel());
        //this.printAllChoiceNodes();
        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //System.out.println("LvL: " + this.memory.getDecisonLevel() + "Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!\n" + i);
            //we push false,nextNode,nextInstance to our stacks, to later on backtracking know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            
            //We return true, since we guessed
            return true;
        }
        
        //We need to do a positive guess
        for(ChoiceNode cN: SCC.get(actualSCC)){
            if(!cN.getAllInstances().isEmpty()){
                //System.out.println("POSITIVE GUESS possible!");
                this.addChoicePoint();
                Instance inz = cN.getAllInstances().get(0);
                //System.out.println("LvL: " + this.memory.getDecisonLevel() + "Guesing on: " + cN.getRule() + " - with VarAsign: " + inz + " to be true!\n" + i);
                for(Atom a: cN.getRule().getBodyMinus()){
                    Instance toAdd = Unifyer.unifyAtom(a,inz, cN.getVarPositions());
                    //System.out.println("Adding: " + toAdd + " to: " + a.getPredicate());
                    c.getRete().addInstanceMinus(a.getPredicate(), toAdd);
                }
                cN.simpleRemoveInstance(inz) ;
                this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).add(inz);
                this.stackyNode.push(cN);
                this.stackybool.push(true); 
                this.stackyInstance.push(inz); 
                return true;
            }
        }
        //System.out.println("WHATSUP?!");
        //if we reach this point this means the actual SCC contains no more guesses
        // therefore we close each Predicate of this SCC (HeadPredicates)
        this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
        if(SCC.size() <= actualSCC) {
            //System.out.println("Guess returns false, because all SCC are through");
            return false;
        } // We have no more ChoiceNode to guess on!
        //System.out.println("OMG?");
        //We can close all reached SCCs that are of size one (as they have no more input and do not depend on anything else!
        while(this.SCCSize.get(actualSCC) <= 1){
            this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
            //c.propagate(); //TODO Remove this propaagte its already in the close SCC MEthod!
            if(SCC.size() <= actualSCC) {
                return false;
            } // We have no more ChoiceNode to guess on!
        }
        //System.out.println("Doing another choice!?!");
        return choice();
    }
    
    protected void addChoicePoint(){
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
     * 
     * registers the adding of an instance to a node into the decision memory.
     * 
     * @param n the node to which the instance was added
     * @param instance the instance that was added
     */
    public void addInstance(Node n, Instance instance){
        this.memory.addInstance(n, instance);
    }
    
    /**
     * 
     * this resets the choiceNodes into the state before the last guess(Except for the guessed one)
     * 
     */
    public void backtrackchoiceNodesDecisionLayer(){
        for(ChoiceNode cN: this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).keySet()){
            for(Instance inz: this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN)){
                //System.out.println("ADDING to CHoiceNode: " + cN + " - " + inz);
                cN.simpleAddInstance(inz);
            }
        }
        this.choiceNodesDecisionLayer.remove(this.choiceNodesDecisionLayer.size()-1);
    }
    
    /**
     * 
     * Adds an Instance into the choiceNodeDecisionlayer. Such that this instance is added after backtracking.
     * 
     * @param n the Node of the instance
     * @param instance the instance you want to add
     */
    public void addInstanceForRemovement(ChoiceNode n, Instance instance){
        //System.err.println("ADDING INSTANCE FOR REMOVEMENT!");
        this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(n).add(instance);
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
    public void backtrack(){
        
        //System.err.println(this.stackybool.size() + " vs: " + this.memory.getDecisonLevel());
        //System.out.println("BACKTRACKING!");
        //this.printAllChoiceNodes();
        // we call backtrack in the decision memory. This way all insatnces that were added after the last guess are removed from their nodes.
        this.backtrackchoiceNodesDecisionLayer();
        this.memory.backtrack();
        
        //TODO: backtracking on SCC
        while(!this.closedAt.isEmpty() && this.closedAt.peek() >= this.memory.getDecisonLevel()){
                this.openActualSCC();
        }
        
        if(this.stackybool.pop()){
            //Positive Guess
            //System.out.println("Last was a POSITVE Guess");
            //We take the node and instacne from the last guess and set them as nextNode/nextInstance.
            //This will lead the enxt guess to guess negative on this node and insatnce when it is called the nxt time.
            nextNode = stackyNode.pop();
            nextInstance = stackyInstance.pop();
            //nextNode.simpleRemoveInstance(nextInstance);
        }else{
            //Negative Guess
            //System.out.println("Last was a NEGATIVE Guess");
            if(memory.getDecisonLevel() > 0) {
                stackyNode.pop();
                stackyInstance.pop();
                //This was not the last guess. Add the last guess back tino the choicenode (since it can be reguessed in the other branch of the guess before)
                //this.stackyNode.pop().simpleAddInstance(this.stackyInstance.pop()); // TODO: Is this needed? We add the choice back into the choice node
                //backtrack(); // TODO: if this is a standalone calculation we can imideatly backtrack once more, since nothing else is possible.
            }else{
                //else we are finished with guessing and at decisoonlevel 0 therefore no more backtracking is needed

                //We set rete.unsatisfiable since such that this is not seen as an answerset
                // We empty all choicenodes such that the guessing is over
                //System.out.println("BACKTRACKING setzt unsat!");
                //this.c.getRete().satisfiable = false;
                for(ChoiceNode cN: this.choiceNodes){
                    Stack<Instance> stacky = new Stack<Instance>();
                    for(Instance inz: cN.getAllInstances()){
                        stacky.push(inz);
                    }
                    while(!stacky.empty()){
                        cN.simpleRemoveInstance(stacky.pop());
                    }
                    
                }
            }
        }
        //c.printAnswerSet(null);
        /*System.out.println("AnswerSetAfter BACKTRACKING:");
        for(Node n: this.memory.getNodes()){
            if(!n.getClass().equals(HeadNode.class)){
                System.out.println(n +" contains: ");
                System.out.println(n.memory.getAllInstances());
            }
            
        }*/
    }
    
    
    /**
     * backtrack one decisionlevel and bring memory back to state before last guess
     */
    public void backtrack2(){
        //TODO: This is used -_> remove othe rbacktrackings!
        //DANGER: NORMAL backtrack() could be needed for MCS thingy, as there we do not want to backtrack multiple tims.
        while(!this.stackybool.isEmpty() && !this.stackybool.peek()){
            this.stackybool.pop();
            this.backtrackchoiceNodesDecisionLayer();
            this.memory.backtrack();
        
            //TODO: backtracking on SCC
            if(!this.closedAt.isEmpty()){
                if(this.closedAt.peek() >= this.memory.getDecisonLevel()){
                    this.openActualSCC();
                }
            }
            if(memory.getDecisonLevel() > 0) {
                stackyNode.pop();
                stackyInstance.pop();
                //This was not the last guess. Add the last guess back tino the choicenode (since it can be reguessed in the other branch of the guess before)
                //this.stackyNode.pop().simpleAddInstance(this.stackyInstance.pop()); // TODO: Is this needed? We add the choice back into the choice node
                //backtrack(); // TODO: if this is a standalone calculation we can imideatly backtrack once more, since nothing else is possible.
            }else{
                //else we are finished with guessing and at decisoonlevel 0 therefore no more backtracking is needed

                //We set rete.unsatisfiable since such that this is not seen as an answerset
                // We empty all choicenodes such that the guessing is over
                //System.out.println("BACKTRACKING setzt unsat!");
                //this.c.getRete().satisfiable = false;
                for(ChoiceNode cN: this.choiceNodes){
                    Stack<Instance> stacky = new Stack<Instance>();
                    for(Instance inz: cN.getAllInstances()){
                        stacky.push(inz);
                    }
                    while(!stacky.empty()){
                        cN.simpleRemoveInstance(stacky.pop());
                    }
                    
                }
                return;
            }
            
            //backtrack();
        }
        if(!this.stackybool.isEmpty()) {
            this.backtrackchoiceNodesDecisionLayer();
            this.memory.backtrack();
        
            //TODO: backtracking on SCC
            if(!this.closedAt.isEmpty()){
                if(this.closedAt.peek() >= this.memory.getDecisonLevel()){
                    this.openActualSCC();
                }
            }
            stackybool.pop();
            nextNode = stackyNode.pop();
            nextInstance = stackyInstance.pop();
            //backtrack();
        }
    }
    
     /**
     * backtrack one decisionlevel and bring memory back to state before last guess
     */
    public void backtrack3(){
        while(!this.stackybool.isEmpty() && !this.stackybool.peek()){
            backtrack();
        }
        if(!this.stackybool.isEmpty()) {
            backtrack();
        }
    }
    
    
    
    /**
     * 
     * @return the actual decision level
     */
    public int getDecisionLevel(){
        return this.memory.getDecisonLevel();
    }
    
    public boolean check4AnswerSet(){
        // TODO: Constraints of the context!
        //System.out.println("Check4AnswerSet!");
        boolean flag = false;
        for(ChoiceNode cN: this.choiceNodes){
            HeadNodeConstraint con = cN.getConstraintNode();
            //System.out.println(con.getAllInstances());
            //System.out.println("HEADCONSTRAINT: " + cN.getRule());
            for(Instance inz: con.getAllInstances()){
                //System.out.println("INZ: " + inz);
                flag = true;
                for(Atom a: cN.getRule().getBodyMinus()){
                    Instance unified = Unifyer.unifyAtom(a, inz, cN.getVarPositions());
                    if(c.getRete().containsInstance(a.getPredicate(),unified,true)){
                        flag = false;
                        break;
                    } //TODO does it work for c.containsInstances as well?
                }
                if(flag) {
                    //System.out.println("NO ANSWERSET DUE To CONSTRAINTS!");
                    return false;
                } // No AnswerSet since one constraint is fullfilled!
            }
        }
        return true;
    }
    
    protected ArrayList<ArrayList<ChoiceNode>> SCC;
    protected ArrayList<ArrayList<Predicate>> SCCPreds;
    protected ArrayList<Integer> SCCSize;
    protected int actualSCC;
    protected Stack<Integer> closedAt;
    public void DeriveSCC(){
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
        for(int i = 0; i < g.getSortedSCCs().size();i++){
            SCC.add(new ArrayList<ChoiceNode>());
            SCCPreds.add(g.getSortedSCCs().get(i));
            //SCCSize.add(g.getSortedSCCs().get(i).size());
            for(int j = 0; j < g.getSortedSCCs().get(i).size();j++){
               for(ChoiceNode cN: this.choiceNodes){
                   if(cN.getRule().getHead().getPredicate().equals(g.getSortedSCCs().get(i).get(j))){
                       SCC.get(i).add(cN);
                   }
               }
            }
             SCCSize.add(SCC.get(i).size());
        }
        
        /*System.out.println("DGraph initialized. SCCSize: " + this.SCC.size());
        for(int i = 0; i < this.SCC.size();i++){
            System.out.println("SCC" + i + " is of size: " + this.SCCSize.get(i));
        }
        for(int i = 0; i < this.SCC.size();i++){
            System.out.println("SCC" + i + " is of size: " + this.SCCSize.get(i) + " :::: " + this.SCC.get(i));
        }
        int i = 0;
        for(DirectedSubgraph gsg: g.getSCCs()){
            i++;
            System.out.println("SCC: " + i);
            System.out.println(gsg.vertexSet());
        }*/
    }
    
    public boolean killSoloSCC(){
        if(SCCSize.isEmpty()) return false;
        while(SCCSize.get(actualSCC)<=1){
            this.closeActualSCC();
            if(actualSCC >= SCC.size()) return false;
        }
        return true;
    }
    
    public DecisionMemory getMemory(){
        return this.memory;
    }
    
    /**
     * used for the MCS calculation. returns wether there is a next alternative to the actual guess
     * @return 
     */
    public boolean nextAlternative(){
        if(this.nextNode != null) return true;
        return false;
    }
    
    public HashMap<Predicate, HashSet<Instance>> deriveNewFactsSindsDecisionLevel(int lvl){
        return this.memory.deriveNewFactsSindsDecisionLevel(lvl);
    }

    
    
}
