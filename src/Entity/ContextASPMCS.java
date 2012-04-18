/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Datastructure.choice.ChoiceUnitMCS;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
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
public class ContextASPMCS extends ContextASP{
    
    private HashMap<Predicate, Boolean> fromOutside;
    
    
    /**
     * public constructor
     * initializes the data structures for rules, facts and rete.
     */
    public ContextASPMCS(){
        super();
        this.fromOutside = new HashMap<Predicate,Boolean>();
    }
    
    public void definePredicateFromOutSide(ContextASPMCS c, Predicate p){
        //TODO: No more different predicates! Just use the predicate itself, and one outside Pred! No this is bad then we cannot close!anymore?!
        Predicate toAdd = Predicate.getPredicate(c.getID() +"_"+ p.getName(), p.getArity());
        this.fromOutside.put(toAdd,true);
        this.rete.addPredicateMinus(toAdd);
        
        // We add a new rule that describes that the actual Predicate p follows from the outside Predicate p:c.id
        Rule r = new Rule();
        Term atomT[] = new Term[p.getArity()];
        for(int i = 0; i < p.getArity();i++){
            atomT[i] = Variable.getVariable("Special_Var: " + i);
        }
        r.setHead(Atom.getAtom(toAdd.getName(), toAdd.getArity(), atomT));
        r.addAtomPlus(Atom.getAtom(p.getName(),p.getArity(),atomT));
    }
    //READ: If closure status from outside is taken into consideration we can use same predname right?
    public void defineRuleFromOutside(Rule r){
        
    }
    
    public void informOfClosureFromOutside(ContextASPMCS c, Predicate p){
        Predicate toInform = Predicate.getPredicate(c.getID() +"_"+ p.getName(), p.getArity());
        fromOutside.put(toInform, false);
        //TODO: Close!
    }
    
    public boolean getClosureStatusForOutside(Predicate p){
        if(!this.fromOutside.containsKey(p)) return false;
        return this.fromOutside.get(p);
    }
    
    public void addFactFromOutside(ContextASPMCS c, Predicate p, Instance inz){
        Predicate toAdd = Predicate.getPredicate(c.getID() +"_"+ p.getName(), p.getArity());
        this.rete.addInstancePlus(toAdd, inz);
    }

    public void addRuleFromOutside(ContextASPMCS c, Rule r){
        this.reteBuilder.addRule(r);
        //TODO: Reevaluate this rule by adding everyhting from the selection node a new?
        //TODO: Backtrack on Rules
        //TODO: SCC on new Rules! --> All rules have to be registered before! and yes we cannot close even if the rule is not yet there, becaus eit can be added later then closure would be logically false!
    }
    
    
}
