/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.Rule;
import java.util.Collection;

/**
 *
 * @author User
 */
public interface Context {
    
    public void addRule(Rule r);
    public boolean containsFact(Term[] a);
    public boolean addFact(Term[] a);
    public Collection<Term[]> selectFacts(Term[] a);
    
    public void propagate();
    public void choice();
    public void backtrack();
    public void resolveConflict();
    
    public void printFacts();
    public void printRules();
    public Collection<Term[]> getAnswerset();
    
}
