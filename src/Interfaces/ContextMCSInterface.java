package Interfaces;

import Datastructure.Rete.Node;
import Entity.Instance;
import Entity.Pair;
import Entity.Predicate;
import Entity.Rule;
import java.util.ArrayList;
import java.util.LinkedList;
import network.ReplyMessage;

/**
 *
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
    public ArrayList<LinkedList<Pair<Node,Instance>>> deriveNewFacts();
    
}
