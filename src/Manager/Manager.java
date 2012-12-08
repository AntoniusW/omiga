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
import Entity.Predicate;
import Entity.Rule;
import java.io.FileNotFoundException;
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
    
    /**
     * calculates all answersets of the given ASP-Context, by first applieng propagation, and then
     * guessing til no more guessing can be done, (while after each guess propagation is
     * done til no more facts can be propagated). When unsatisfiability is reached or
     * no more guesses can be done, the calculation backtracks to the last choice,
     * and guesses the other way. (If it already was the other way, we backtrack once more).
     * If we cannot backtrakc anymore the calculation is finished.
     */
    public void calculate(){
        System.out.println("Standard calc called --> WRONG!");
        boolean finished = false;
        c.propagate();
        //System.err.println("First Propagation finsihed: " + System.currentTimeMillis());
        c.getChoiceUnit().DeriveSCC();
        if(!c.getChoiceUnit().killSoloSCC()){
            //System.err.println("Killed all SCC: " + System.currentTimeMillis());
            //We killed all SCC --> This context is guessfree
            if(c.isSatisfiable()){
                c.printAnswerSet(null);
                System.out.println("GuessFree Context found 1 AnswerSet");
            }else{
                System.out.println("GuessFree Context UNSATISFIABLE!");
            }
            return;
        }
        //System.out.println("Preparing to Guess: " + System.currentTimeMillis());
        //c.printAnswerSet();
        while(!finished){
            if(c.choice()){
                //System.out.println("choice Reurned true");
                //System.out.println("StartPropagation: " + System.currentTimeMillis());
                c.propagate();
                //c.printAnswerSet();
                if(!c.isSatisfiable()){
                    //System.out.println("UNSAT why?");
                    c.backtrack();
                }
                 //System.out.println("RESAT?? " + c.isSatisfiable());
                 //System.out.println("Next Choice!");
            }else{
                //System.out.println("Chocie returned false");
                // No more chocies can be made
                
                if(c.getChoiceUnit().getDecisionLevel() > 0){
                    if(c.isSatisfiable()){
                        if(c.getChoiceUnit().check4AnswerSet()){
                            // We have found an answerset, print it!
                            //System.out.println("ANSWERSET FOUND!");
                            //c.printAnswerSet();
                            answerSetCount++;
                            //break;
                        }else{
                            // constraint Violation detected: No answerSet!
                            //System.out.println("CONSTRAINT VIOLATION!");
                        }
                    }
                    c.backtrack();
                    //c.resetSatisfiable();
                }else{
                    finished = true;
                    /*if(this.answerSetCount == 0){
                        if(c.getChoiceUnit().check4AnswerSet()){
                            System.out.println("AnswerSet Found! There is only one AnswerSet and that's without apllying any guesses: ");
                            c.printAnswerSet();
                            answerSetCount++;
                        }
                    }*/
                }
            }
        }
        //System.out.println("Found: " + this.answerSetCount + " answersets!");
        //System.out.println(this.c.getChoiceUnit().getMemory().getNodes());
        /*System.out.println("HashCode red: " + Constant.getConstant("red"));
        System.out.println("HashCode green: " + Constant.getConstant("green"));
        System.out.println("Equals? red = green: " + Constant.getConstant("red").equals(Constant.getConstant("green")));*/
        
        /*DGraph g = new DGraph();
        for(Rule r: c.getAllRules()){
            g.addRule(r);
        }
        //g.gd.addEdge(Predicate.getPredicate("p",2), Predicate.getPredicate("s",1));
        int i = 0;
        for(DirectedSubgraph gsg: g.getSCCs()){
            i++;
            System.out.println("SCC: " + i);
            System.out.println(gsg.vertexSet());
        }
        
        System.out.println(g.gd.getAllEdges(Predicate.getPredicate("q", 1), Predicate.getPredicate("s",1)));*/
        
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
        //System.out.println("Preparing to Guess: " + System.currentTimeMillis());
        //c.printAnswerSet();
        while(!finished){
            //System.out.println("Doing while!");
            if(c.choice()){
                //System.out.println("choice was done!");
                //System.out.println("Found a choice. Now propagate");
                c.propagate();
                //System.out.println("After propagation. Interpretation is:");
                //c.printAnswerSet(null);
                if(!c.isSatisfiable()){
                    //System.out.println("UNSAT. Backtrack now!");
                    c.backtrack();
                    //System.out.println("After backtracking. Interpretation is:");
                    //c.printAnswerSet(null);
                }
            }else{
                //System.out.println("No more choice at level = " + c.getChoiceUnit().getDecisionLevel());
                if(c.getChoiceUnit().getDecisionLevel() >= 0){
                    if(c.isSatisfiable()){
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
        System.out.println("Found: " + this.answerSetCount + " answer sets.");
        
    }
    
    
    
    
}
