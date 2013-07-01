/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.Unifyer;
import Entity.Atom;
import Entity.ContextASP;
import Entity.GlobalSettings;
import Entity.Instance;
import Entity.Pair;
import Entity.Predicate;
import Exceptions.ImmediateBacktrackingException;
import java.util.ArrayList;
import java.util.EmptyStackException;
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
    
    protected void closeActualSCC() {
            //memory.setDecisionLevel(getDecisionLevel()+1);
            c.getRete().propagate();
            for (Predicate p : SCCPreds.get(actualSCC)) {
                if (c.getRete().containsPredicate(p, false)) {
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
    public void printDecisionMemory() {
        System.out.println("ChoiceStack: "+choiceStack);
        super.printDecisionMemory();
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
            if( GlobalSettings.debugDecision ) System.out.println("BACKTRACK: Stack empty.");
            return;
        }
        
        int backtrackTo = lastChoice.getArg2().decisionLevel;
        int numDecisionLevels = getDecisionLevel()-backtrackTo+1;
        if( GlobalSettings.debugDecision ) System.out.println("BACKTRACKING: "+numDecisionLevels+" DLs from "+getDecisionLevel());
        
        nextGuessIsNegative = true;

//        System.out.println("Decision memory before backtracking:");
//        printDecisionMemory();

        // we call backtrack in the decision memory. This way all insatnces that were added after the last guess are removed from their nodes.
        memory.backtrackTo(backtrackTo);

        //TODO: backtracking on SCC
        while (!this.closedAt.isEmpty() && this.closedAt.peek() >= getDecisionLevel()) {
            this.openActualSCC();
        }

//        System.out.println("Decision memory after backtracking:");
//        printDecisionMemory();
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
            
            if( GlobalSettings.debugDecision ) System.out.println("Choice point: non-fire "+choicePair.getArg1().getRule()+" "+toAdd+" @DL="+getDecisionLevel());
            
            nextGuessIsNegative = false;
            return true;
        }

        
        //We need to do a positive guess
        //addChoicePoint();
        if( GlobalSettings.debugDecision ) System.out.println("Searching positive guess:");
        memory.setDecisionLevel(getDecisionLevel()+1);  // increase decision level
        for(ChoiceNode cN: SCC.get(actualSCC)){
            Pair<Instance,ArrayList<Pair<Atom, Instance>>> choicePair = cN.nextChoiceableInstance();
            
            // no more choices at cN, continue with next node
            if( choicePair == null ) {
                continue;
            }
            
            ArrayList<Pair<Atom, Instance>> toMakeNegative = choicePair.getArg2();
            Instance choiceInstance = new Instance(choicePair.getArg1());
                for (Pair<Atom, Instance> pair : toMakeNegative) {
                    c.getRete().addInstanceMinus(pair.getArg1().getPredicate(), pair.getArg2());
//                    System.out.println("Adding: " + pair.getArg2() + " to: "
//                            + pair.getArg1() +" @DL="+pair.getArg2().decisionLevel);
                }
                choiceInstance.decisionLevel = getDecisionLevel();
                choiceStack.add(new Pair<ChoiceNode, Instance>(cN, choiceInstance));
                
                if( GlobalSettings.debugDecision ) System.out.println("Choice point: fire "+cN.getRule()+" "+choiceInstance+" @DL="+getDecisionLevel());
                GlobalSettings.decisionCounter++;   // for statistics, count total decisions done.
                return true;
            //}
            
        }
        if( GlobalSettings.debugDecision ) System.out.println("No positive guess found in current SCC, closing SCCs now.");
        memory.setDecisionLevel(getDecisionLevel()-1);  // nothing was added, decrease decision level again.
        
        //if we reach this point this means the actual SCC contains no more guesses
        // therefore we close each Predicate of this SCC (HeadPredicates)
        try {
            this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
            if (SCC.size() <= actualSCC) {
                return false;
            } // We have no more ChoiceNode to guess on!

            //We can close all reached SCCs that are of size one (as they have no more input and do not depend on anything else!
            while (this.SCCSize.get(actualSCC) <= 1) {
                if (SCCSize.get(actualSCC) == 1) {
                    if (SCCPreds.get(actualSCC).size() > 1) {
                        break;
                    }
                }

                this.closeActualSCC(); // closing the SCC increases actual SCC to the next SCC
                if (SCC.size() <= actualSCC) {
                    return false;
                } // We have no more ChoiceNode to guess on!
            }
        } catch (ImmediateBacktrackingException e) {
            // closing resulted in inconsistency, backtrack

            // Since we store facts within the basic Nodes on a stack, we have to empty this stack
            // in case of unsatisfiability, since the stack has to be empty for the next propagation after backtracking
            for(BasicNode bn: c.getRete().getBasicLayerPlus().values()){
                bn.resetPropagation();
            }
            for(BasicNode bn: c.getRete().getBasicLayerMinus().values()){
                bn.resetPropagation();
            }
            c.backtrack();
        }
        return choice();
    }
    
    @Override
    public void addChoicePoint() {
        memory.setDecisionLevel(memory.getDecisonLevel()+1);
    }
    
}