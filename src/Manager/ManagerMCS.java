/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Datastructure.DependencyGraph.DGraph;
import Datastructure.Rete.Rete;
import Datastructure.choice.ChoiceUnit;
import Entity.Constant;
import Entity.ContextASP;
import Entity.ContextASPMCSRewriting;
import Entity.Predicate;
import Entity.Rule;
import java.io.FileNotFoundException;
import network.ReplyMessage;
import org.jgrapht.graph.DirectedSubgraph;

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
public class ManagerMCS {
    
    private ContextASPMCSRewriting c;
    private int answerSetCount;
    
    /**
     * public constructor
     * 
     * @param c the context you want to calculate
     */
    public ManagerMCS(ContextASPMCSRewriting c){
        this.c = c;
        //this.rete = c.getRete();
        //this.choiceUnit = c.getChoiceUnit();
        answerSetCount = 0;
    }
    
    public void calculate(Integer answersets, boolean output, String filter){
        boolean finished = false;
        c.propagate();
        c.getChoiceUnit().DeriveSCC();
        if(!c.getChoiceUnit().killSoloSCC()){
            //System.err.println("Killed all SCC: " + System.currentTimeMillis());
            //We killed all SCC --> This context is guessfree
            if(c.isSatisfiable()){
                if(output){
                    c.printAnswerSet(filter);
                }
                System.out.println("GuessFree Context found 1 AnswerSet");
            }else{
                System.out.println("GuessFree Context UNSATISFIABLE!");
            }
            return;
        }
        System.out.println("Preparing to Guess: " + System.currentTimeMillis());
        //c.printAnswerSet();
        while(!finished){
            boolean flag = false;
            ReplyMessage lol = c.nextBranch();
            if(lol.equals(ReplyMessage.NO_MORE_ALTERNATIVE)) {
                finished = true;
            }
            if(lol.equals(ReplyMessage.HAS_BRANCH)){
                flag = true;
            }else{
                if(c.choice()){
                    flag = true;
                }
            }
            if(flag){
                c.propagate();
                if(!c.isSatisfiable()){
                    c.backtrack();
                }
            }else{
                
                if(c.getChoiceUnit().getDecisionLevel() > 0){
                    if(c.isSatisfiable()){
                        //if(c.getChoiceUnit().check4AnswerSet()){
                            if(output){
                                System.out.println("Found AD: " + answerSetCount);
                                c.printAnswerSet(filter);
                            }
                            answerSetCount++;
                            System.out.println(answerSetCount);
                            if(answersets != null && answerSetCount == answersets) break;
                        //}
                    }
                    c.backtrack();
                }else{
                    finished = true;
                }
            }
        }
        System.out.println("Found: " + this.answerSetCount + " answersets!");
        
    }
    
    
    
    
}
