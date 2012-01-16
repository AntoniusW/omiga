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
    public boolean containsFact(Atom a);
    public boolean addFact(Atom a);
    public Collection<Atom> selectFacts(Atom a);
    
    public void propagate();
    public void choice();
    public void backtrack();
    public void resolveConflict();
    
    public void printFacts();
    public void printRules();
    public Collection<Atom> getAnswerset();
    
}
