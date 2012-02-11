/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.Rete;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author g.weidinger
 */
public class ContextASP {
    
    private ArrayList<Rule> rules;
    private HashMap<Predicate, ArrayList<Instance>> facts;
    private Rete rete;
    
    public ContextASP(){
        rules = new ArrayList<Rule>();
        facts = new HashMap<Predicate, ArrayList<Instance>>();
        this.rete = new Rete();
    }
    
    public void propagate(){
        this.rete.propagate();
    }
    
    public void initializeRete(){
        for(Rule r: this.rules){
            rete.addRule(r);
        }
        for(Predicate p: facts.keySet()){
            for(Instance i: facts.get(p)){
                if(!rete.containsPredicate(p, true)) rete.addPredicatePlus(p);
                rete.addInstancePlus(p, i);
            }
        }
    }
    
    public void addRule(Rule r) throws RuleNotSafeException{
        if(r.isSafe()) {
            rules.add(r);
        }else{
            throw new RuleNotSafeException("This rule is not safe: " + r);
        }
    }
    
    public void addFact(Predicate p, Instance instance) throws FactSizeException{
        if(instance.getSize() != p.getArity()) {
            String s = "Fact: " + instance.toString() + " and Predicate arity of: " + p + " do not match: " + instance.getSize() + " != " + p.getArity();
            throw new FactSizeException(s);
        }
        if(!facts.containsKey(p)) facts.put(p, new ArrayList<Instance>());
        facts.get(p).add(instance);
    }
    
    public ArrayList<Rule> getAllRules(){
        return rules;
    }
    
    public HashMap<Predicate, ArrayList<Instance>> getAllFacts(){
        return facts;
    }
    
    
    
    public void printContext(){
        System.out.println("Printing Context: ");
        System.out.println("Rulesize: " + rules.size());
        for(Rule r: this.rules){
            System.out.println(r);
        }
        for(Predicate p: facts.keySet()){
            System.out.println("Factsize: " + facts.get(p).size());
            for(Instance i: facts.get(p)){
                System.out.println(p.getName()+ i);
            }
        }
    }
    
    
    public void printAnswerSet(){
        rete.printAnswerSet();
    }
    
    public Rete getRete(){
        return rete;
    }
    
    
}
