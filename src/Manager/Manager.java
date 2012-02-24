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
 * @author g.weidinger
 */
public class Manager {
    
    private ContextASP c;
    private Rete rete;
    private ChoiceUnit choiceUnit;
    
    private int answerSetCount;
    
    public Manager(ContextASP c){
        this.c = c;
        this.rete = c.getRete();
        this.choiceUnit = c.getChoiceUnit();
        answerSetCount = 0;
    }
    
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
                        // We have found an answerset, print it!
                        System.out.println("ANSWERSET FOUND!");
                        c.printAnswerSet();
                        answerSetCount++;
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
