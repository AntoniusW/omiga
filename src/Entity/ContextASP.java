/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.Rete;
import Interfaces.Context;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class ContextASP implements Context{
    
    Rete rete;
    
    
    public ContextASP(){
        rete = new Rete();
    }

    @Override
    public void addRule(Rule r) {
        rete.addRule(r);
    }

    @Override
    public boolean containsFactPlus(Predicate p, Term[] a) {
        return rete.containsInstance(p, a, true);
    }
    @Override
    public boolean containsFactMinus(Predicate p, Term[] a) {
        return rete.containsInstance(p, a, false);
    }

    @Override
    public void addFactToINMemory(Predicate p, Term[] a) {
        this.rete.addInstancePlus(p, a);
    }
    
    @Override
    public void addFactToOUTMemory(Predicate p, Term[] a) {
        this.rete.addInstanceMinus(p, a);
    }

    @Override
    public Collection<Term[]> selectFacts(Predicate p, Term[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void propagate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void choice() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void backtrack() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resolveConflict() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void printFacts() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void printRules() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Term[]> getAnswerset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
}
