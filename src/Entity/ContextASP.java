/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * ContextASP represents an ASP-Context. Mainly storing facts per predicate and rules.
 * It also has a rete reference, which is the entrypoint to the rete network, that is built around this context.
 * The hole propagation is handled over this rete component.
 * 
 */
public class ContextASP {
    
    private ArrayList<Rule> rules;
    private HashMap<Predicate, ArrayList<Instance>> factsIN;
    private HashMap<Predicate, ArrayList<Instance>> factsOUT;
    private ReteBuilder reteBuilder;
    private Rete rete;
    
    /**
     * public constructor
     * initializes the data structures for rules, facts and rete.
     */
    public ContextASP(){
        rules = new ArrayList<Rule>();
        factsIN = new HashMap<Predicate, ArrayList<Instance>>();
        factsOUT = new HashMap<Predicate, ArrayList<Instance>>();
        this.rete = new Rete();
        this.reteBuilder = new ReteBuilder(rete);
    }
    
    /**
     * commands the rete network to propagate. The rete network must have been initialized before!
     */
    public void propagate(){
        this.rete.propagate();
    }
    
    /**
     * adds a rule to this contexts rules
     * 
     * @param r the rule you want to add
     * @throws RuleNotSafeException is thrown if r is not safe
     */
    public void addRule(Rule r) throws RuleNotSafeException{
        if(r.isSafe()) {
            rules.add(r);
            reteBuilder.addRule(r);
        }else{
            throw new RuleNotSafeException("This rule is not safe: " + r);
        }
    }
    
    /**
     * adds a Fact to this context IN-Set
     * 
     * @param p the predicate this fact belongs to
     * @param instance the instantiation of this fact
     * @throws FactSizeException if the instance size does not match the predicates arity, this exception is thrown
     */
    public void addFact2IN(Predicate p, Instance instance) throws FactSizeException{
        if(instance.getSize() != p.getArity()) {
            String s = "Fact: " + instance.toString() + " and Predicate arity of: " + p + " do not match: " + instance.getSize() + " != " + p.getArity();
            throw new FactSizeException(s);
        }
        if(!factsIN.containsKey(p)) factsIN.put(p, new ArrayList<Instance>());
        factsIN.get(p).add(instance);
        rete.addInstancePlus(p, instance);
    }
    
    /**
     * adds a Fact to this context OUT-Set
     * 
     * @param p the predicate this fact belongs to
     * @param instance the instantiation of this fact
     * @throws FactSizeException if the instance size does not match the predicates arity, this exception is thrown
     */
    public void addFact2OUT(Predicate p, Instance instance) throws FactSizeException{
        if(instance.getSize() != p.getArity()) {
            String s = "Fact: " + instance.toString() + " and Predicate arity of: " + p + " do not match: " + instance.getSize() + " != " + p.getArity();
            throw new FactSizeException(s);
        }
        if(!factsOUT.containsKey(p)) factsOUT.put(p, new ArrayList<Instance>());
        factsOUT.get(p).add(instance);
        rete.addInstanceMinus(p, instance);
    }
    
    /**
     * 
     * @return a list of all rules of this context
     */
    public ArrayList<Rule> getAllRules(){
        return rules;
    }
    
    /**
     * @return all facts per predicate that are IN
     */
    public HashMap<Predicate, ArrayList<Instance>> getAllINFacts(){
        return factsIN;
    }
    
     /**
     * @return all facts per predicate that are OUT
     */
    public HashMap<Predicate, ArrayList<Instance>> getAllOUTFacts(){
        return factsOUT;
    }
    
    
    /**
     * prints all rules and facts of this context to standard out
     */
    public void printContext(){
        System.out.println("Printing Context: ");
        System.out.println("Rulesize: " + rules.size());
        for(Rule r: this.rules){
            System.out.println(r);
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
    
    /**
     * prints the answerSet of this context (All facts that werde derived)
     */
    public void printAnswerSet(){
        rete.printAnswerSet();
    }
    
    /**
     * 
     * @return the rete Entrypoint of this context
     */
    public Rete getRete(){
        return rete;
    }
    
    
}
