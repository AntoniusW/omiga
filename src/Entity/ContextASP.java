/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.BasicNodeNegative;
import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
public class ContextASP {
    
    protected static int nextID = 0;
    
    protected static int getNextID(){
        nextID++;
        return nextID;
    }
    
    protected ArrayList<Rule> rules;
    protected HashMap<Predicate, ArrayList<Instance>> factsIN;
    protected HashMap<Predicate, ArrayList<Instance>> factsOUT;
    protected ReteBuilder reteBuilder;
    protected Rete rete;
    protected ChoiceUnit choiceUnit;
    protected int id;
    
    
    /**
     * public constructor
     * initializes the data structures for rules, facts and rete.
     */
    public ContextASP(){
        rules = new ArrayList<Rule>();
        factsIN = new HashMap<Predicate, ArrayList<Instance>>();
        factsOUT = new HashMap<Predicate, ArrayList<Instance>>();
        this.choiceUnit = new ChoiceUnit(this);
        this.rete = new Rete(choiceUnit);
        this.reteBuilder = new ReteBuilder(rete);
        this.id=getNextID();
    }
    
    public int getID(){
        return this.id;
    }
    
    /**
     * commands the rete network to propagate. The rete network must have been initialized before!
     */
    public void propagate(){
        this.rete.propagate();
    }
    
    public boolean choice(){
        return this.choiceUnit.choice();
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
        if(!factsIN.containsKey(p)) {
            factsIN.put(p, new ArrayList<Instance>());
            if(!rete.containsPredicate(p, true)){
                rete.addPredicatePlus(p);
            }    
        }
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
    
    public void setINFActs(HashMap<Predicate,ArrayList<Instance>> facts){
        this.factsIN = facts;
    }
    
     /**
     * @return all facts per predicate that are OUT
     */
    public HashMap<Predicate, ArrayList<Instance>> getAllOUTFacts(){
        return factsOUT;
    }
    
    public void setOUTFActs(HashMap<Predicate,ArrayList<Instance>> facts){
        this.factsOUT = facts;
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
    public void printAnswerSet(String filter){
        rete.printAnswerSet(filter);
    }
    
    /**
     * 
     * @return the rete Entrypoint of this context
     */
    public Rete getRete(){
        return rete;
    }
    
    public ChoiceUnit getChoiceUnit(){
        return this.choiceUnit;
    }
    
    public void setChoiceUnit(ChoiceUnit choice){
        this.choiceUnit = choice;
    }
    
    public boolean isSatisfiable(){
        return rete.satisfiable;
    }
    
    public void backtrack(){
        this.resetSatisfiable();
        this.choiceUnit.backtrack3();
        
        printAnswerSet(null);
        
        // we might have learned some new rule, start its basic propagation
        for (Map.Entry<Predicate, BasicNode> basicEntry : rete.getBasicLayerPlus().entrySet()) {
            basicEntry.getValue().propagateAfterLearning();
        }
        for (Map.Entry<Predicate, BasicNodeNegative> basicEntry : rete.getBasicLayerMinus().entrySet()) {
            basicEntry.getValue().propagateAfterLearning();
        }
        
        printAnswerSet(null);
        
    }
    
    public void backtrackTo(int decisionlevel){
        while(this.rete.getChoiceUnit().getDecisionLevel() > decisionlevel){
            backtrack();
        }
    }
    
    public void resetSatisfiable(){
        this.rete.satisfiable = true;
    }

    public ReplyMessage choicePlusInfo() {
        return this.choiceUnit.choicePlusInfo();
    }
    
    
}
