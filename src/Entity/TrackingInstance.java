/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Datastructure.Rete.HeadNode;
import Interfaces.Term;

/**
 *
 * @author Antonius Weinzierl
 */
public class TrackingInstance extends Instance {

    private Rule createdByRule;
    private HeadNode createdByHeadNode;

    public HeadNode getCreatedByHeadNode() {
        return createdByHeadNode;
    }

    public void setCreatedByHeadNode(HeadNode createdByHeadNode) {
        this.createdByHeadNode = createdByHeadNode;
    }

    private Instance fullInstance;

    public Instance getFullInstance() {
        return fullInstance;
    }

    public void setFullInstance(Instance fullInstance) {
        this.fullInstance = fullInstance;
    }

    public Rule getCreatedByRule() {
        return createdByRule;
    }

    public void setCreatedByRule(Rule createdByRule) {
        this.createdByRule = createdByRule;
    }

    public TrackingInstance(Term[] terms, int propagationLevel, int decisionLevel) {
        super(terms, propagationLevel,decisionLevel);
    }
}
