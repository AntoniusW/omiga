/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.Predicate;
import Entity.Rule;
import java.util.Collection;

/**
 *
 * @author User
 */
public interface Context {
    
    public void addRule(Rule r);
    public boolean containsFactPlus(Predicate p, Term[] a);
    public boolean containsFactMinus(Predicate p, Term[] a);
    public void addFactToINMemory(Predicate p, Term[] a);
    public void addFactToOUTMemory(Predicate p, Term[] a);
    public Collection<Term[]> selectFacts(Predicate p, Term[] a);
    
    public void propagate();
    public void choice();
    public void backtrack();
    public void resolveConflict();
    
    public void printFacts();
    public void printRules();
    public Collection<Term[]> getAnswerset();
    
}
