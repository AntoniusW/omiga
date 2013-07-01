package Interfaces;

import Entity.Instance;
import Entity.Predicate;
import Entity.Rule;
import java.util.Collection;

/**
 *
 */
public interface Context {
    
    public void addRule(Rule r);
    public boolean containsFactPlus(Predicate p, Instance a);
    public boolean containsFactMinus(Predicate p, Instance a);
    public void addFactToINMemory(Predicate p, Instance a);
    public void addFactToOUTMemory(Predicate p, Instance a);
    public Collection<Term[]> selectFacts(Predicate p, Term[] a);
    
    public void propagate();
    public void choice();
    public void backtrack();
    public void resolveConflict();
    
    public void printFacts();
    public void printRules();
    public Collection<Term[]> getAnswerset();
    
}
