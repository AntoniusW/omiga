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
import network.ReplyMessage;
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
public class ChoiceUnitRewrite extends ChoiceUnit {
    
    
    
    
    public ChoiceUnitRewrite(){
        
    }
    
    /**
     * 
     * public constructor. Creates a new ChoiceUnit with initialized data structures.
     * 
     * @param c The ASP Context you want to use this choice unit for
     */
    public ChoiceUnitRewrite(ContextASP c){
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
               c.getRete().getBasicNodeMinus(p).close();
           }
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());
    }
    
    protected void openActualSCC(){
        this.actualSCC--;
        //System.out.println("c=" + c);
        for(Predicate p: SCCPreds.get(actualSCC)){
           if(c.getRete().containsPredicate(p, false)){
               c.getRete().getBasicNodeMinus(p).unclose();
           }
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
    @Override
    public boolean choice(){
        //System.out.println(this.choiceNodes);
        /*System.out.println("CHOICE IS CALLED!");
        System.out.println(this.choiceNodes);
        System.out.println(this.choiceNodes.get(1).getAllInstances());*/
        //System.out.println("Choice is called!");
        //TODO: replace foreach loops with iterator loops.
        i++;
        /*if(i==13) {
            System.out.println("SPECIAL:");
            c.printAnswerSet(null);
            //c.getRete().printRete();
        }*/
        //System.out.println("Choice is called! " + this.memory.getDecisonLevel());
        //this.printAllChoiceNodes();
        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            //nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //We do not have to have constraints node anymore but then we have to kill the instance of the choice node here.
            nextNode.removeInstance(nextInstance);
            //Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
            //System.out.println("OLD: Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + nextInstance + " to OUT!");
            //System.out.println("Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + toAdd + " to OUT!");
            //if(nextNode.getRule().isHeadFixed()) {
                //this is an optimisation: We put the rules head out iff there is no other way of it beeing derived by the rwritten program
                Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
                this.c.getRete().addInstanceMinus(nextNode.getRule().getHead().getPredicate(), toAdd);
            //}
            
            
            //System.out.println("LvL: " + this.memory.getDecisonLevel() + ". Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!\n" + i);
            //we push false,nextNode,nextInstance to our stacks, to later on backtracking know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            
            //We return true, since we guessed
            //c.printAnswerSet(null);
            return true;
        }
        
        //We need to do a positive guess
        for(ChoiceNode cN: SCC.get(actualSCC)){
            if(!cN.getAllInstances().isEmpty()){
                //System.out.println("POSITIVE GUESS possible!");
                this.addChoicePoint();
                Instance inz = cN.getAllInstances().get(0);
                //System.out.println("LvL: " + this.memory.getDecisonLevel() + ". Guesing on: " + cN.getRule() + " - with VarAsign: " + inz + " to be true!\n" + i);
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
                //c.printAnswerSet(null);
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
            
            if(SCCSize.get(actualSCC) == 1){
               if(SCCPreds.get(actualSCC).size() > 1) break;
            }
            
            this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
            //c.propagate(); //TODO Remove this propaagte its already in the close SCC MEthod!
            if(SCC.size() <= actualSCC) {
                return false;
            } // We have no more ChoiceNode to guess on!
        }
        //System.out.println("Doing another choice!?!");
        return choice();
    }
    
    
    
    public ReplyMessage checkBranch()
    {
        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            //nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //We do not have to have constraints node anymore but then we have to kill the instance of the choice node here.
            nextNode.removeInstance(nextInstance);
            Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
            //System.out.println("OLD: Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + nextInstance + " to OUT!");
            //System.out.println("Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + toAdd + " to OUT!");
            this.c.getRete().addInstanceMinus(nextNode.getRule().getHead().getPredicate(), toAdd);
            //System.out.println("LvL: " + this.memory.getDecisonLevel() + ". Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!\n" + i);
            //we push false,nextNode,nextInstance to our stacks, to later on backtracking know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            
            //We return true, since we guessed
            //c.printAnswerSet(null);
            return ReplyMessage.HAS_BRANCH;
        }
        
        return ReplyMessage.NO_MORE_BRANCH;
    }
    
    public ReplyMessage choicePlusInfo(){
        //System.out.println("CHOICE IS CALLED! : ChoiceUNIT REWRITE");
        //System.out.println(this.choiceNodes);
        /*System.out.println("CHOICE IS CALLED!");
        System.out.println(this.choiceNodes);
        System.out.println(this.choiceNodes.get(1).getAllInstances());*/
        //System.out.println("Choice is called!");
        //TODO: replace foreach loops with iterator loops.
        //i++;
        /*if(i==13) {
            System.out.println("SPECIAL:");
            c.printAnswerSet(null);
            //c.getRete().printRete();
        }*/
        //System.out.println("Choice is called! " + this.memory.getDecisonLevel());
        //this.printAllChoiceNodes();
        if(this.nextNode != null){
            System.out.println("SHOULD NOT COME HERE");
            assert (false);
            
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            //nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //We do not have to have constraints node anymore but then we have to kill the instance of the choice node here.
            nextNode.removeInstance(nextInstance);
            Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
            //System.out.println("OLD: Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + nextInstance + " to OUT!");
            //System.out.println("Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + toAdd + " to OUT!");
            this.c.getRete().addInstanceMinus(nextNode.getRule().getHead().getPredicate(), toAdd);
            //System.out.println("LvL: " + this.memory.getDecisonLevel() + ". Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!\n" + i);
            //we push false,nextNode,nextInstance to our stacks, to later on backtracking know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            
            //We return true, since we guessed
            //c.printAnswerSet(null);
            return ReplyMessage.HAS_BRANCH;
        }
        
        if (actualSCC >= SCC.size())
        {
            return ReplyMessage.NO_MORE_CHOICE;
        }
        
        //We need to do a positive guess
        for(ChoiceNode cN: SCC.get(actualSCC)){
            if(!cN.getAllInstances().isEmpty()){
                //System.out.println("POSITIVE GUESS possible!");
                this.addChoicePoint();
                Instance inz = cN.getAllInstances().get(0);
                //System.out.println("LvL: " + this.memory.getDecisonLevel() + ". Guesing on: " + cN.getRule() + " - with VarAsign: " + inz + " to be true!\n" + i);
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
                //c.printAnswerSet(null);
                return ReplyMessage.HAS_CHOICE;
            }
        }
        //System.out.println("WHATSUP?!");
        //if we reach this point this means the actual SCC contains no more guesses
        // therefore we close each Predicate of this SCC (HeadPredicates)
        this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
        if(SCC.size() <= actualSCC) {
            //System.out.println("Guess returns false, because all SCC are through");
            return ReplyMessage.NO_MORE_CHOICE;
        } // We have no more ChoiceNode to guess on!
        //System.out.println("OMG?");
        //We can close all reached SCCs that are of size one (as they have no more input and do not depend on anything else!
        
        while(this.SCCSize.get(actualSCC) <= 1){
            this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
            //c.propagate(); //TODO Remove this propaagte its already in the close SCC MEthod!
            if(SCC.size() <= actualSCC) {
                return ReplyMessage.NO_MORE_CHOICE;
            } // We have no more ChoiceNode to guess on!
        }
        //System.out.println("Doing another choice!?!");
        return choicePlusInfo();
    }  
    
}
