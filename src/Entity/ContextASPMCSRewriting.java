/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Datastructure.choice.ChoiceUnitMCS;
import Datastructure.choice.ChoiceUnitMCSRewrite;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.ContextMCSInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
    
    /**
     * public constructor
     * initializes the data structures for rules, facts and rete.
     */
    public ContextASPMCSRewriting(){
        //super();
        rules = new ArrayList<Rule>();
        factsIN = new HashMap<Predicate, ArrayList<Instance>>();
        factsOUT = new HashMap<Predicate, ArrayList<Instance>>();
        this.choiceUnit = new ChoiceUnitMCSRewrite(this);
        this.rete = new Rete(choiceUnit);
        this.reteBuilder = new ReteBuilder(rete);
        this.id=getNextID();
        negRules = new ArrayList<Rule>();
        this.fromOutside = new HashMap<Predicate,Boolean>();
    }
    
    /*public void definePredicateFromOutSide(ContextASPMCSRewriting c, Predicate p){
        Predicate toAdd = Predicate.getPredicate(c.getID() +"_"+ p.getName(), p.getArity());
        this.fromOutside.put(toAdd,true);
        this.rete.addPredicateMinus(toAdd);
    }
    
    public void informOfClosureFromOutside(ContextASPMCSRewriting c, Predicate p){
        Predicate toInform = Predicate.getPredicate(c.getID() +"_"+ p.getName(), p.getArity());
        fromOutside.put(toInform, false);
    }*/
    
    /**
     * 
     * returns wether predicate p can come from outside or not
     * 
     * @param p
     * @return 
     */
    public boolean getClosureStatusForOutside(Predicate p){
        if(!this.fromOutside.containsKey(p)) return false;
        return this.fromOutside.get(p);
    }

    /**
     * 
     * @return wether there is a next alternative or not (So is there a false guess or not left)
     */
    @Override
    public boolean nextAlternative() {
        return this.choiceUnit.nextAlternative();
    }

    /**
     * backtracks to the given decision level
     * @param level 
     */
    @Override
    public void backtrackTo(int level) {
        this.resetSatisfiable();
        while(this.choiceUnit.getDecisionLevel() > level){
            this.choiceUnit.backtrack3();
        }
    }

    /**
     * registers a fact from outside such that the context knows that this fact can arrive from outside
     * @param p the Predicate you want to register
     */
    @Override
    public void registerFactFromOutside(Predicate p) {
        this.fromOutside.put(p,true);
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
        ((ChoiceUnitMCSRewrite)this.rete.getChoiceUnit()).addExternalNode();
        this.rete.addInstancePlus(p, inz);
    }

    /**
     * tell the context that a predicate p cannot be derived from outside anymore
     * 
     * @param p the predicate that cannot come form outside anymore
     */
    @Override
    public void closeFactFromOutside(Predicate p) {
        this.fromOutside.put(p, false);
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
    public HashMap<Predicate, HashSet<Instance>> deriveNewFacts(int since) {
        return this.choiceUnit.deriveNewFactsSindsDecisionLevel(since);
    }
    
    @Override
    public void backtrack(){
        this.choiceUnit.backtrack();
    }

    
    
}
