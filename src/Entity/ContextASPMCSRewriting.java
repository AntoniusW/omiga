/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.Node;
import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Interfaces.ContextMCSInterface;
import java.util.*;
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
public class ContextASPMCSRewriting extends ContextASPRewriting implements ContextMCSInterface {
    
    private HashMap<Predicate, Boolean> fromOutside; // stores the closure status for all predicates that come from outside
    
    
    //TODO: DANGER: Choice: No more false guesses on same decion level! Does this matter?! ATM only positive guesses increase the decision level
    // sinc ebakctrack only removes decision levels for positive guesses!

    public HashMap<Predicate, Boolean> getFromOutside() {
        return fromOutside;
    }
    
    
    
    /**
     * public constructor
     * initializes the data structures for rules, facts and rete.
     */
    public ContextASPMCSRewriting(){
        //super();
        rules = new ArrayList<Rule>();
        factsIN = new HashMap<Predicate, ArrayList<Instance>>();
        factsOUT = new HashMap<Predicate, ArrayList<Instance>>();
        this.choiceUnit = null; //TODO: removing that ChoiceUnit for standalone testing; new ChoiceUnitMCSRewrite(this);
        this.rete = new Rete(choiceUnit);
        this.reteBuilder = new ReteBuilder(rete);
        this.id=getNextID();
        negRules = new ArrayList<Rule>();
        this.fromOutside = new HashMap<Predicate,Boolean>();
    }
    

    /**
     * 
     * @return whether there is a next alternative or not (So is there a false guess or not left)
     */
    @Override
    public ReplyMessage nextBranch() {
        if (this.choiceUnit.nextBranch())
        {
            return ReplyMessage.HAS_BRANCH;
        }
        else
        {
            if (this.choiceUnit.getDecisionLevel() == 0)
                return ReplyMessage.NO_MORE_ALTERNATIVE;
            else
                return ReplyMessage.NO_MORE_BRANCH;
        }
    }


    /**
     * registers a fact from outside such that the context knows that this fact can arrive from outside
     * @param p the Predicate you want to register
     */
    @Override
    public void registerFactFromOutside(Predicate p) {
        //System.out.println("Registering fact from outside: "+p);
        this.fromOutside.put(p,false);
        this.rete.addPredicateMinus(p);
        this.rete.addPredicatePlus(p);
    }
    
    /**
     * pushes a fact into the context. Pleas ebe aware that this facts predicate had to be registered at startup!
     * @param p the predicate of the fact
     * @param inz the actual instance of the fact
     */
    @Override
    public void addFactFromOutside(Predicate p, Instance inz) {
    }
    
     /**
     * pushes all facts into the context. Pleas be aware that the predicates of these facts have to be registred at startup!
     * @param facts a HashMap containing all the facts (Instances for predicates)
     */
    public void addFactsFromOutside(HashMap<Predicate,ArrayList<Instance>> facts) {
    }

    /**
     * tell the context that a predicate p cannot be derived from outside anymore
     * 
     * @param p the predicate that cannot come form outside anymore
     */
    @Override
    public void closeFactFromOutside(Predicate p) {
    }
    
    //Do not use this method for reopening on backtracking. This is done by the sover himself.
    public void openFactFromOutside(Predicate p) {
        this.fromOutside.put(p,false);
    }
    
    @Override
    public Predicate registerRuleFromOutside(Rule r) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void addRuleFromOutside(Predicate p, Instance inz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void closeRuleFromOutside(Predicate p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @return the actual decision level
     */
    @Override
    public int getDecisionLevel() {
        return this.choiceUnit.getDecisionLevel();
    }

    @Override
    public ArrayList<LinkedList<Pair<Node,Instance>>> deriveNewFacts() {
        return this.choiceUnit.deriveNewFactsSindsDecisionLevel();
    }
    
    @Override
    public void backtrack(){
        this.resetSatisfiable();
        this.choiceUnit.backtrack3();
    }

    
    
}
