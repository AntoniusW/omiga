/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnitRewrite;
import java.util.ArrayList;
import java.util.HashMap;
import network.ReplyMessage;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * ContextASP represents an ASP-Context. Mainly storing facts per predicate and rules.
 * It also has a rete reference, which is the entrypoint to the rete network, that is built around this context.
 * The hole propagation is handled over this rete component.
 * 
 */
public class ContextASPRewriting extends ContextASP{

    
    /**
     * public constructor
     * initializes the data structures for rules, facts and rete.
     */
    public ContextASPRewriting(){
        //super();
        rules = new ArrayList<Rule>();
        factsIN = new HashMap<Predicate, ArrayList<Instance>>();
        factsOUT = new HashMap<Predicate, ArrayList<Instance>>();
        this.choiceUnit = new ChoiceUnitRewrite(this);
        this.rete = new Rete(choiceUnit);
        this.reteBuilder = new ReteBuilder(rete);
        this.id=getNextID();
        
    }

    
    /**
     * prints all rules and facts of this context to standard out
     */
    public void printContext(){
        System.out.println("Printing Context: ");
        System.out.println("Rulesize: " + (rules.size() + this.negRules.size()));
        for(Rule r: this.rules){
            System.out.println(r);
        }
        for(Rule r: this.negRules){
            System.out.println("-" + r);
        }
        System.out.println("IN Set:");
        for(Predicate p: factsIN.keySet()){
            System.out.println("Factsize: " + factsIN.get(p).size());
            for(Instance i: factsIN.get(p)){
                System.out.println(p.getName()+ i);
            }
        }
        System.out.println("OUT Set:");
        for(Predicate p: factsOUT.keySet()){
            System.out.println("Factsize: " + factsOUT.get(p).size());
            for(Instance i: factsOUT.get(p)){
                System.out.println(p.getName()+ i);
            }
        }
    }
    
      
}
