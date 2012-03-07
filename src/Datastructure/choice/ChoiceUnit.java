/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.HeadNodeConstraint;
import Datastructure.Rete.Node;
import Datastructure.Rete.Unifyer;
import Entity.Atom;
import Entity.Constant;
import Entity.ContextASP;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import Exceptions.FactSizeException;
import Interfaces.Context;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;


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
    
    //TODO: Is there a problem if we guess, and remove choiceNodes other than the one we guessed on, because propagation lead to some headNodefullfillment. These nodes are not reverted?
    
    // TODO: HeadNodeCOnstraints are not treated correctly (remove is called to often why?
    
    private ContextASP c;
    private ArrayList<ChoiceNode> choiceNodes;
    private DecisionMemory memory;
    
    private Stack<ChoiceNode> stackyNode;
    private Stack<Boolean> stackybool;
    private Stack<Instance> stackyInstance;
    private ChoiceNode nextNode = null;
    private Instance nextInstance = null;
    
    private ArrayList<HashMap<ChoiceNode,HashSet<Instance>>> choiceNodesDecisionLayer;
    
    
    
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
    
    /**
     * 
     * 
     * 
     * @return if there was a guess that was made, or if there are no more guesses left
     */
    public boolean choice(){
        //System.out.println("Choice is called! " + this.memory.getDecisonLevel());
        //this.printAllChoiceNodes();
        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //System.out.println("Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!");
            //we push false,nextNode,nextInstance to our stacks, to later on backtracking know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            
            //We return true, since we guessed
            return true;
        }
        //nextNode was not set, therefore we have to make a positive guess if possible
        for(int i = 0;i < choiceNodes.size();i++){
            //We go through all nodes that may contain instances for choice
            //TODO: [PERFORMANCE INCREASE]Maybe we can increase runtrime if we delete nodes from here when no instances to guess on are left, and add them again when such an insatnce is added to it
            if(!choiceNodes.get(i).getAllInstances().isEmpty()){
                //The actual choicenode contains at least one instance, so we guess on it and add a choicePoint
                this.addChoicePoint();
                //We take the first instance that is in this node
                Instance inz = ((Instance)choiceNodes.get(i).getAllInstances().get(0));
                //System.out.println("Guesing on: " + choiceNodes.get(i).getRule() + " - with VarAsign: " + inz + " to be true!");
                /*String varPos = "[";
                for(Variable v: choiceNodes.get(i).getVarPositions().keySet()){
                    varPos = varPos + v + "=" + choiceNodes.get(i).getVarPositions().get(v) + ",";
                }
                varPos = varPos + "]";
                System.out.println("VarPos: ");
                System.out.println(varPos);*/
                
                //Since we do a positive guess we put one unifyed instance for each negative atom of the corresponding rule into our OUT memory
                //Since we add all negative parts to the outset, and all positive parts have to be fullfilled since, the isntance reached the choiceNode
                //This rule is fullfilled for the actual groundinstance and the head will be added within the next propagationstep
                //Unification works here, since all variables have to to be set at a choiceNode, since we only work on save rules
                for(Atom a: choiceNodes.get(i).getRule().getBodyMinus()){
                    Instance toAdd = Unifyer.unifyAtom(a,inz, choiceNodes.get(i).getVarPositions());
                    c.getRete().addInstanceMinus(a.getPredicate(), toAdd);
                }
                choiceNodes.get(i).simpleRemoveInstance(inz) ;
                this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(choiceNodes.get(i)).add(inz);
                //Be aware that the instance we just guessed on will be deleted automatially within the next propagationstep, so we do not need to remove it here.
                
                //We push the node and instance we just guessed on, as well as true, onto our stacks, to let backtrackign know a positive guess was made
                this.stackyNode.push(choiceNodes.get(i));
                this.stackybool.push(true); 
                this.stackyInstance.push(inz); 
                //System.out.println("GUESSED: " + choiceNodes.get(i).getRule() + " with: " + inz);

                //We return true since we made a guess
                return true;
                
            }
        }
        //No negative guess was there to make, and all choiceNodes are empty, therefore no guess at all can be made anymore and we return false
        return false;
    }
    
    private void addChoicePoint(){
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
                //System.out.println("RESETTING: " + cN + " - " + inz);
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
        //System.out.println("BACKTRACKING!");
        // we call backtrack in the decision memory. This way all insatnces that were added after the last guess are removed from their nodes.
        this.backtrackchoiceNodesDecisionLayer();
        this.memory.backtrack();
        
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
                backtrack(); // TODO: if this is a standalone calculation we can imideatly backtrack once more, since nothing else is possible.
            }else{
                //else we are finished with guessing and at decisoonlevel 0 therefore no more backtracking is needed

                //We set rete.unsatisfiable since such that this is not seen as an answerset
                // We empty all choicenodes such that the guessing is over
                this.c.getRete().satisfiable = false;
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
        boolean flag = false;
        for(ChoiceNode cN: this.choiceNodes){
            HeadNodeConstraint con = cN.getConstraintNode();
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

    
    
}
