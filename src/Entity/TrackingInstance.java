/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.Term;

/**
 *
 * @author Antonius Weinzierl
 */
public class TrackingInstance extends Instance {

    private Rule createdByRule;

    public Rule getCreatedByRule() {
        return createdByRule;
    }

    public void setCreatedByRule(Rule createdByRule) {
        this.createdByRule = createdByRule;
    }

    public TrackingInstance(Term[] terms) {
        super(terms);
    }
}
