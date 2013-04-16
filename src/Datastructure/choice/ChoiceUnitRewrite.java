/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.Unifyer;
import Entity.Atom;
import Entity.ContextASP;
import Entity.Instance;
import Entity.Pair;
import Entity.Predicate;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
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
public class ChoiceUnitRewrite extends ChoiceUnit {
    
    private Stack<Pair<ChoiceNode,Instance>> choiceStack;
    private boolean nextGuessIsNegative;
    private boolean calculationFinished;
    
    
    
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
        choiceStack = new Stack<Pair<ChoiceNode, Instance>>();
        nextGuessIsNegative = false;
        calculationFinished = false;
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

    @Override
    public void backtrack() {
        throw new RuntimeException("ChoiceUnitRewrite.backtrack: Should not come here.");
    }
    
    

    @Override
    public void backtrack3() {
        Pair<ChoiceNode, Instance> lastChoice;
        
        try {
            lastChoice = choiceStack.peek();
        } catch (EmptyStackException e) {
            // we removed last available decision, computation is over.
            calculationFinished = true;
            c.getRete().satisfiable = false;    // ensures that current interpretation is not treated as answer set
            System.out.println("BACKTRACK: Stack empty.");
            return;
        }
        
        int backtrackTo = lastChoice.getArg2().decisionLevel;
        int numDecisionLevels = getDecisionLevel()-backtrackTo+1;
        System.out.println("BACKTRACKING: "+numDecisionLevels+" DLs from "+getDecisionLevel());
        
        nextGuessIsNegative = true;

        //System.out.println("Decision memory before backtracking:");
        //printDecisionMemory();

        // we call backtrack in the decision memory. This way all insatnces that were added after the last guess are removed from their nodes.
        memory.backtrackTo(backtrackTo);

        //TODO: backtracking on SCC
        while (!this.closedAt.isEmpty() && this.closedAt.peek() >= getDecisionLevel()) {
            this.openActualSCC();
        }
        

        /*if (this.stackybool.pop()) {
            //Positive Guess
            //System.out.println("Last was a POSITVE Guess");
            //We take the node and instacne from the last guess and set them as nextNode/nextInstance.
            //This will lead the enxt guess to guess negative on this node and insatnce when it is called the nxt time.
            nextNode = stackyNode.pop();
            nextInstance = stackyInstance.pop();
            //nextNode.simpleRemoveInstance(nextInstance);
        } else {
            //Negative Guess
            //System.out.println("Last was a NEGATIVE Guess");
            if (getDecisionLevel() > 0) {
                stackyNode.pop();
                stackyInstance.pop();
                //This was not the last guess. Add the last guess back tino the choicenode (since it can be reguessed in the other branch of the guess before)
                //this.stackyNode.pop().simpleAddInstance(this.stackyInstance.pop()); // TODO: Is this needed? We add the choice back into the choice node
                //backtrack(); // TODO: if this is a standalone calculation we can imideatly backtrack once more, since nothing else is possible.
            } else {
                //else we are finished with guessing and at decisoonlevel 0 therefore no more backtracking is needed

                //We set rete.unsatisfiable since such that this is not seen as an answerset
                // We empty all choicenodes such that the guessing is over
                for (ChoiceNode cN : this.choiceNodes) {
                    Stack<Instance> stacky = new Stack<Instance>();
                    for (Instance inz : cN.getAllInstances()) {
                        stacky.push(inz);
                    }
                    while (!stacky.empty()) {
                        cN.simpleRemoveInstance(stacky.pop());
                    }

                }
            }
        }*/
        //System.out.println("Decision memory after backtracking:");
        //printDecisionMemory();
    }
    
    
    
    /**
     * 
     * 
     * 
     * @return if there was a guess that was made, or if there are no more guesses left
     */
    @Override
    public boolean choice(){
        
        // backtracking emptied choiceStack, calculation is over
        if( calculationFinished ) {
            memory.setDecisionLevel(0);
            return false;
        }
        
        if( nextGuessIsNegative ) {
            addChoicePoint();
            Pair<ChoiceNode, Instance> choicePair = choiceStack.pop();
            
            // disable instance for further guessing
            choicePair.getArg1().disableInstance(choicePair.getArg2(), getDecisionLevel());
            
            Instance toAdd = Unifyer.unifyAtom(choicePair.getArg1().getRule().getHead(), choicePair.getArg2(), choicePair.getArg1().getVarPositions());
            toAdd.decisionLevel = getDecisionLevel();
            c.getRete().addInstanceMinus(choicePair.getArg1().getRule().getHead().getPredicate(), toAdd);
            
            System.out.println("Choice point: non-fire "+choicePair.getArg1().getRule()+" "+toAdd+" @DL="+getDecisionLevel());
            
            nextGuessIsNegative = false;
            return true;
        }
/*        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking

            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            //nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //We do not have to have constraints node anymore but then we have to kill the instance of the choice node here.
            nextNode.removeInstance(nextInstance);
            //this is an optimisation: We put the rules head out iff there is no other way of it beeing derived by the rwritten program
            Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
            Instance toAddDL = new Instance(toAdd);
            toAddDL.decisionLevel = getDecisionLevel();
            this.c.getRete().addInstanceMinus(nextNode.getRule().getHead().getPredicate(), toAddDL);
         
            //we push false,nextNode,nextInstance to our stacks, to later on backtracking know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            
            System.out.println("Choice point: non-fire "+nextNode.getRule()+" "+toAdd);
            
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            
            //We return true, since we guessed
            return true;
        }*/
        
        //We need to do a positive guess
        //addChoicePoint();
        memory.setDecisionLevel(getDecisionLevel()+1);  // increase decision level
        for(ChoiceNode cN: SCC.get(actualSCC)){
            Pair<Instance,ArrayList<Pair<Atom, Instance>>> choicePair = cN.nextChoiceableInstance();
            
            // no more choices at cN, continue with next node
            if( choicePair == null ) {
                continue;
            }
            
            ArrayList<Pair<Atom, Instance>> toMakeNegative = choicePair.getArg2();
            Instance choiceInstance = new Instance(choicePair.getArg1());
            //if( toMakeNegative != null ) {
//                System.out.println("POSITIVE Guess.");
                for (Pair<Atom, Instance> pair : toMakeNegative) {
                    c.getRete().addInstanceMinus(pair.getArg1().getPredicate(), pair.getArg2());
//                    System.out.println("Adding: " + pair.getArg2() + " to: "
//                            + pair.getArg1() +" @DL="+pair.getArg2().decisionLevel);
                }
                choiceInstance.decisionLevel = getDecisionLevel();
                choiceStack.add(new Pair<ChoiceNode, Instance>(cN, choiceInstance));
                
                System.out.println("Choice point: fire "+cN.getRule()+" "+choiceInstance+" @DL="+getDecisionLevel());
                
                return true;
            //}
            /*if(!cN.getAllInstances().isEmpty()){
                System.out.println("POSITIVE GUESS possible!");
                this.addChoicePoint();
                Instance inst = cN.getAllInstances().get(0);
                // ChoiceNode cotains original instances from propagation, copy is required first
                Instance inz = new Instance(inst);
                inz.decisionLevel = getDecisionLevel();    // set to current decision level
                for(Atom a: cN.getRule().getBodyMinus()){
                    Instance toAdd = Unifyer.unifyAtom(a,inz, cN.getVarPositions());
                    System.out.println("Adding: " + toAdd + " to: " + a.getPredicate()+" @DL="+toAdd.decisionLevel);
                    c.getRete().addInstanceMinus(a.getPredicate(), toAdd);
                }
                cN.simpleRemoveInstance(inz) ;
                this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).add(inz);
                this.stackyNode.push(cN);
                this.stackybool.push(true); 
                this.stackyInstance.push(inz); 
                
                System.out.println("Choice point: fire "+cN.getRule()+" "+inz);
                
                return true;
            }*/
        }
        memory.setDecisionLevel(getDecisionLevel()-1);  // nothing was added, decrease decision level again.
        
        //if we reach this point this means the actual SCC contains no more guesses
        // therefore we close each Predicate of this SCC (HeadPredicates)
        this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
        if(SCC.size() <= actualSCC) {
            return false;
        } // We have no more ChoiceNode to guess on!

        //We can close all reached SCCs that are of size one (as they have no more input and do not depend on anything else!
        while(this.SCCSize.get(actualSCC) <= 1){
            
            if(SCCSize.get(actualSCC) == 1){
               if(SCCPreds.get(actualSCC).size() > 1) break;
            }
            
            this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
            if(SCC.size() <= actualSCC) {
                return false;
            } // We have no more ChoiceNode to guess on!
        }
        return choice();
    }
    
    @Override
    public void addChoicePoint() {
        memory.setDecisionLevel(memory.getDecisonLevel()+1);
        //memory.addChoicePoint();
        //this.IncreasechoiceNodesDecisionLayer();
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
