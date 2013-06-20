/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Entity.ContextASP;
import Entity.GlobalSettings;
import Learning.GraphLearner;
import Datastructure.Rete.Node;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The Manager class is used to manage a calculation. This Manager is written to
 * perform a calculation on a simple ASP context. If you want a calculation over an
 * MCS create your own manager.
 * 
 * @param c the ASP-Context you want to complelty calculate
 * @param int answerSetCount the number of answersets that are found during the calculation
 * 
 */
public class Manager {
    
    private ContextASP c;
    private int answerSetCount;

    public ContextASP getContext() {
        return c;
    }
    
    /**
     * public constructor
     * 
     * @param c the context you want to calculate
     */
    public Manager(ContextASP c){
        this.c = c;
        //this.rete = c.getRete();
        //this.choiceUnit = c.getChoiceUnit();
        answerSetCount = 0;
    }
    
    public void calculate(Integer answersets, boolean output, String filter){
        boolean finished = false;
        c.propagate();
        if( !c.isSatisfiable() ) {
            System.out.println("Program is unsatisfiable after first propagation (no choices done).");
            return;
        }
        c.getChoiceUnit().DeriveSCC();
        if(!c.getChoiceUnit().killSoloSCC()){
            //System.err.println("Killed all SCC: " + System.currentTimeMillis());
            //We killed all SCC --> This context is guessfree
            if(c.isSatisfiable()){
                if(output){
                    c.printAnswerSet(filter);
                }
                System.out.println("GuessFree Program found 1 AnswerSet");
            }else{
                System.out.println("GuessFree Program UNSATISFIABLE!");
            }
            return;
        }
        //System.out.println("Preparing to Guess: " + System.currentTimeMillis());
        //c.printAnswerSet();
        while(!finished){
//getContext().getChoiceUnit().getMemory().debug_isEveryInstanceCovered(); // debug
            //System.out.println("Doing while!");
            if(c.choice()){
                if (GlobalSettings.debugOutput) {
                    System.out.println("After choice, interpretation is:");
                    c.printAnswerSet(null);
                }
                //System.out.println("choice was done!");
                //System.out.println("Found a choice. Now propagate");
//getContext().getChoiceUnit().getMemory().debug_isEveryInstanceCovered(); // debug
                c.propagate();
//            getContext().getChoiceUnit().getMemory().debug_isEveryInstanceCovered(); // debug
                if (GlobalSettings.debugOutput) {
                    System.out.println("After propagation, interpretation is:");
                    c.printAnswerSet(null);
                }
                if(!c.isSatisfiable()){
                    if( GlobalSettings.debugDecision ) System.out.println("UNSAT. Backtrack now!");
                    c.backtrack();
                    if (GlobalSettings.debugOutput) {
                        System.out.println("After backtracking. Interpretation is:");
                        c.printAnswerSet(null);
                    }
                }
            }else{
                if( GlobalSettings.debugLearning ) System.out.println("No more choice at level = " + c.getChoiceUnit().getDecisionLevel());
                if(c.getChoiceUnit().getDecisionLevel() >= 0){
                    if(c.isSatisfiable()){
                        if( GlobalSettings.debugLearning ) System.out.println("Triggering final propagation after closing to derive non-MBT.");
                        c.propagate();
                        if( c.ContainsNoMustBeTrue() ) {
                        //if(c.getChoiceUnit().check4AnswerSet()){
                            if(output){
                                System.out.println("Answer set: " + (answerSetCount+1));
                                c.printAnswerSet(filter);
                            }
                            answerSetCount++;
                            //System.out.println(answerSetCount);
                            if(answersets != null && answerSetCount == answersets) break;
                        //}
                        }
                    }
                    else
                    {
                        //System.out.println("No more choice but UNSAT");
                    }
                   if (c.getChoiceUnit().getDecisionLevel() > 0) {
                       c.backtrack();
                   }else{
                        finished = true;
                   }
                }
            }
        }
    }
    
    public String printStatus() {
        return "Learned rules are:\n"+GraphLearner.printLearnedRules()+"\n"+
        "Found: " + this.answerSetCount + " answer sets."+"\n"+
        "Rejected learned rules because of size: "+GraphLearner.rejectedLargeLearnedRules+"\n"+
        "Rejected double-learned rules: "+GraphLearner.rejectedDoubleLearnedRules+"\n"+
        "Total number of nodes in Rete: " + Node.nodes.size()+"\n"+
        "Total decisions done: " + GlobalSettings.decisionCounter+"\n"+
        "Maximum decision level: "+GlobalSettings.maxDecisionLevel+"\n"+
        "Learned rules: " + GraphLearner.getNumLearnedRules()+"\n";
    }
    
    
    
    
}
