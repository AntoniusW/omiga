/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Learning;

import Datastructure.Rete.HeadNode;
import Datastructure.Rete.Node;
import Datastructure.Rete.Rete;
import Entity.Atom;
import Entity.Constant;
import Entity.Instance;
import Entity.Operator;
import Entity.Rule;
import Entity.TrackingInstance;
import Entity.Variable;
import Enumeration.OP;
import Interfaces.OperandI;
import Interfaces.Term;
import java.util.HashMap;

/**
 * Reconstructs a partial implication graph from a given rule and variable
 * assignment.
 *
 * @author Antonius Weinzierl
 */
public class GraphLearner {

    public static int varcount = 0;

    public Rule learnRule(Rule r, Instance var_assignment, HeadNode starting) {
        Rule newrule = new Rule();
        Node current = starting;
        Rete rete = starting.from.rete;

        boolean stop_criterion_met = false;

        while (!stop_criterion_met) {

            // iterate over all body atoms
            for (Atom at : r.getBodyPlus()) {

                // build a grounded selectionCriterion
                Term[] selectionCriterion = new Term[at.getArity()];
                for (int i = 0; i < at.getTerms().length; i++) {
                    Term term = at.getTerms()[i];
                    if (term instanceof Variable) {
                        // ground the variable
                        Variable var = (Variable) term;
                        selectionCriterion[i] = var_assignment.get(
                                starting.from.getVarPositions().get(var)); // get value from variable assignment
                    } else if (term instanceof Constant) {
                        selectionCriterion[i] = term;
                    }

                }
                Instance at_inst;
                at_inst = (Instance) rete.
                        getBasicLayerPlus().get(at.getPredicate()).
                        getMemory().select(selectionCriterion).iterator().next();

                if (at_inst instanceof TrackingInstance) {
                    // instance was derived by a rule, trace subrule
                    TrackingInstance tri = (TrackingInstance) at_inst;
                    Rule subrule = tri.getCreatedByRule(); // copy subrule

                    // rename the variables of the subrule
                    subrule = renameVariables(subrule);

                    // ensure correct variable binding, e.g. X = renamedX, Y = b
                    for (int i = 0; i < at.getTerms().length; i++) {
                        Term selTerm = at.getTerms()[i];
                        Term headTerm = subrule.getHead().getTerms()[i];
                        Operator var_equal = new Operator((OperandI)selTerm,(OperandI)headTerm,OP.EQUAL);
                        newrule.addOperator(var_equal);
                    }

                    // trace rule body
                    Rule n1 = traceBody(subrule, tri, true, rete);

                    // add learned body to current body
                    for (Atom nat : n1.getBodyPlus()) {
                        newrule.addAtomPlus(nat);
                    }
                    for (Atom nat : n1.getBodyMinus()) {
                        newrule.addAtomMinus(nat);
                    }
                    for (Operator nop : n1.getOperators()) {
                        newrule.addOperator(nop);
                    }
                }
                continue;
            }

            current.getVarPositions();

            stop_criterion_met = true;

        }

        return newrule;
    }

    private Rule traceBody(Rule r, TrackingInstance inst, boolean isPositive, Rete rete) {
        Rule ret = new Rule();

        //HeadNode hn = rete.inst.getCreatedByRule().

        return ret;
    }

    public Rule learnRule1UIP() {
        return null;
    }

    private Rule renameVariables(Rule rule) {
        Rule ret = new Rule(rule);
        HashMap<Variable, Variable> varmap = new HashMap<Variable, Variable>();
        
        renameTerm(ret.getHead().getTerms(),varmap);

        for (Atom at : ret.getBodyPlus()) {
            renameTerm(at.getTerms(), varmap);
        }
        for (Atom at : ret.getBodyMinus()) {
            renameTerm(at.getTerms(), varmap);
        }
        for (Operator op : ret.getOperators()) {
            renameOperator(op, varmap);
        }

        return ret;
    }

    private void renameTerm(Term[] t, HashMap<Variable, Variable> varmap) {
        for (int i = 0; i < t.length; i++) {
            Term term = t[i];
            if (term instanceof Variable) {
                Variable var = (Variable) term;
                if (varmap.containsKey(var)) {
                    t[i] = varmap.get(var);
                } else {
                    Variable nvar = Variable.getVariable("learned variable: " + varcount++);
                    t[i] = nvar;
                    varmap.put(var, nvar);
                }
            }
        }
    }

    private void renameOperator(OperandI operand, HashMap<Variable, Variable> varmap) {

        if (operand instanceof Operator) {
            Operator op = (Operator) operand;

            if (op.left instanceof Variable) {
                Variable var = (Variable) op.left;
                if (varmap.containsKey(var)) {
                    op.left = varmap.get(var);
                } else {
                    Variable nvar = Variable.getVariable("learned variable: " + varcount++);
                    op.left = nvar;
                    varmap.put(var, nvar);
                }
            } else {
                renameOperator(op.left, varmap);
            }

            if (op.right instanceof Variable) {
                Variable var = (Variable) op.right;
                if (varmap.containsKey(var)) {
                    op.right = varmap.get(var);
                } else {
                    Variable nvar = Variable.getVariable("learned variable: " + varcount++);
                    op.right = nvar;
                    varmap.put(var, nvar);
                }
            } else {
                renameOperator(op.right, varmap);
            }
        }
    }
}
