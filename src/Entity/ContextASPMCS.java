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
        Predicate toAdd = Predicate.getPredicate(c.getID() +"_"+ p.getName(), p.getArity());
        this.fromOutside.put(toAdd,true);
        this.rete.addPredicateMinus(toAdd);
    }
    
    public void informOfClosureFromOutside(ContextASPMCS c, Predicate p){
        Predicate toInform = Predicate.getPredicate(c.getID() +"_"+ p.getName(), p.getArity());
        fromOutside.put(toInform, false);
    }
    
    public boolean getClosureStatusForOutside(Predicate p){
        if(!this.fromOutside.containsKey(p)) return false;
        return this.fromOutside.get(p);
    }

    
    
}
