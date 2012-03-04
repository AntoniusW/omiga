/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import Datastructure.Rete.Rete;
import Datastructure.choice.ChoiceUnit;
import Entity.ContextASP;

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
        boolean finished = false;
        c.propagate();
        while(!finished){
            if(c.choice()){
                c.propagate();
                if(!c.isSatisfiable()){
                    c.backtrack();
                }
            }else{
                // No more chocies can be made
                
                if(c.getChoiceUnit().getDecisionLevel() > 0){
                    if(c.isSatisfiable()){
                        if(c.getChoiceUnit().check4AnswerSet()){
                            // We have found an answerset, print it!
                            System.out.println("ANSWERSET FOUND!");
                            c.printAnswerSet();
                            answerSetCount++;
                        }else{
                            // constraint Violation detected: No answerSet!
                        }
                    }
                    c.backtrack();
                    //c.resetSatisfiable();
                }else{
                    finished = true;
                }
            }
        }
        System.out.println("Found: " + this.answerSetCount + " answersets!");
    }
    
    
    
    
}
