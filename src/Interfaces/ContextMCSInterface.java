/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.Instance;
import Entity.Predicate;
import Entity.Rule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import network.ReplyMessage;

/**
 *
 * @author User
 */
public interface ContextMCSInterface {
    
    public void propagate();
    public ReplyMessage nextBranch();
    public boolean choice();
    public void backtrack();
    public void backtrackTo(int level);
    public void addFactFromOutside(Predicate p, Instance inz);
    public void addRuleFromOutside(Predicate p, Instance inz);
    public void registerFactFromOutside(Predicate p);
    public Predicate registerRuleFromOutside(Rule r);
    public void closeFactFromOutside(Predicate p);
    public void closeRuleFromOutside(Predicate p);
    public int getDecisionLevel();
    public HashMap<Predicate,HashSet<Instance>> deriveNewFacts(int since);
    
}
