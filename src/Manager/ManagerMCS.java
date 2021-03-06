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
import network.Action;
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
        System.out.println("MCS Calculater!");
        //long pure_solving_time = 0;
        //long start_action;
        
        boolean finished = false;
        //start_action = System.currentTimeMillis();
        c.propagate();
        c.getChoiceUnit().DeriveSCC();
        
        boolean kill_solo = c.getChoiceUnit().killSoloSCC();
        
        //pure_solving_time = pure_solving_time + System.currentTimeMillis() - start_action;
        
        if(!kill_solo){
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
        //System.out.println("Preparing to Guess: " + System.currentTimeMillis());
        //c.printAnswerSet();

        
        /*
        Action action = Action.MAKE_CHOICE;
        while (action != Action.FINISH)
        {
            if (action == Action.MAKE_CHOICE)
            {
                start_action = System.currentTimeMillis();
                boolean has_choice = c.choice();
                pure_solving_time = pure_solving_time + System.currentTimeMillis() - start_action;
                
                if (has_choice)
                {
                    start_action = System.currentTimeMillis();
                    c.propagate();
                    pure_solving_time = pure_solving_time + System.currentTimeMillis() - start_action;
                    
                    if (!c.isSatisfiable())
                        action = Action.MAKE_BRANCH;
                }
                else
                {
                    if(c.getChoiceUnit().getDecisionLevel() >= 0)
                    {
                        start_action = System.currentTimeMillis();
                        c.propagate();
                        pure_solving_time = pure_solving_time + System.currentTimeMillis() - start_action;
                        
                        if (c.isSatisfiable())
                        {
                            answerSetCount++;
                            if (output)
                            {
                                System.out.println("Found Answerset: " + answerSetCount);
                                c.printAnswerSet(filter);
                            }
                            if(answersets != 0 && answerSetCount == answersets) break;
                        }
                        
                        start_action = System.currentTimeMillis();
                        c.backtrack();
                        pure_solving_time = pure_solving_time + System.currentTimeMillis() - start_action;
                    }
                }
            }
            else if (action == Action.MAKE_BRANCH)
            {
                start_action = System.currentTimeMillis();
                c.backtrack();
                ReplyMessage rm = c.nextBranch();
                pure_solving_time = pure_solving_time + System.currentTimeMillis() - start_action;
                
                switch (rm)
                {
                    case HAS_BRANCH:
                        start_action = System.currentTimeMillis();
                        c.propagate();
                        pure_solving_time = pure_solving_time + System.currentTimeMillis() - start_action;
                        if (c.isSatisfiable())
                            action = Action.MAKE_CHOICE;
                        break;
                    case NO_MORE_BRANCH:                        
                        break;
                    case NO_MORE_ALTERNATIVE:
                        action = Action.FINISH;
                        break;
                }
            }
        }
        
        System.out.println("INFO: Pure solving time = " + 1.0*pure_solving_time/1000);*/
        
        
        while(!finished){
            //boolean flag = false;
            
            //c.propagate();
            if(c.choice()){
                c.propagate();
                if(!c.isSatisfiable()){
                    c.backtrack();
                    ReplyMessage rm = c.nextBranch();
                    while(rm != ReplyMessage.HAS_BRANCH && !finished){
                        if(rm == ReplyMessage.NO_MORE_ALTERNATIVE) finished = true;
                        c.backtrack();
                        rm = c.nextBranch();
                        c.propagate();
                        if(!c.isSatisfiable()) rm = ReplyMessage.NO_MORE_BRANCH; // we backtrack since the new branch directly lead to unsatisfiability
                        //System.out.println("RM= " + rm);
                    }
                    
                }
            }else{
                //System.out.println("ManagerMCS: Choice false.");
                if(c.getChoiceUnit().getDecisionLevel() >= 0){
                    //System.out.println("REACHED AN END");
                    c.propagate();
                    if(c.isSatisfiable()){
                        //System.out.println("That is satisfiable");
                        //if(c.getChoiceUnit().check4AnswerSet()){
                            if(output){
                                System.out.println("Found Answerset: " + answerSetCount);
                                c.printAnswerSet(filter);
                            }
                            answerSetCount++;
                            //System.out.println(answerSetCount);
                            //if(answersets != null && answerSetCount == answersets) break;
                        //}
                    }else{
                        //System.out.println("That is NOT satisfiable!");
                    }
                    c.backtrack();
                    ReplyMessage rm = c.nextBranch();
                    while(rm != ReplyMessage.HAS_BRANCH && !finished){
                        if(rm == ReplyMessage.NO_MORE_ALTERNATIVE) finished = true;
                        c.backtrack();
                        rm = c.nextBranch();
                        c.propagate();
                        if(!c.isSatisfiable()) rm = ReplyMessage.NO_MORE_BRANCH; // we backtrack since the new branch directly lead to unsatisfiability
                        //System.out.println("RM= " + rm);
                    }
                }else{
                    finished = true;
                }
            }
        }
        
        
        System.out.println("Found: " + this.answerSetCount + " answersets!");
        
    }
    
    
    
    
}
