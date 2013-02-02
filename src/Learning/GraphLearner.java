package Learning;

import Datastructure.Rete.HeadNode;
import Datastructure.Rete.Node;
import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Entity.Atom;
import Entity.Constant;
import Entity.FuncTerm;
import Entity.GlobalSettings;
import Entity.Instance;
import Entity.Operator;
import Entity.Rule;
import Entity.TrackingInstance;
import Entity.Variable;
import Enumeration.OP;
import Exceptions.LearningException;
import Interfaces.OperandI;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Reconstructs a partial implication graph from a given rule and variable
 * assignment.
 *
 * @author Antonius Weinzierl
 */
public class GraphLearner {

    private static int varcount = 1;
    private static String renamed_var_prefix = "lVar:";
    
    // repository of already learned rules to detect duplicates.
    private static ArrayList<Rule> learnedRules = new ArrayList<Rule>();
    
    public void learnRuleAndAddToRete(Rule r, Instance var_assignment, HeadNode starting) throws LearningException {
    if( true) return;
        Rule newrule;// = new Rule();
        Node current = starting;
        Node lastJoin = starting.from;
        Rete rete = lastJoin.rete;
        ReteBuilder rB = new ReteBuilder(rete);

        //boolean stop_criterion_met = false;

        //while (!stop_criterion_met) {

        newrule = traceBody(r, var_assignment, rete, starting, new HashMap<Variable, Variable>());

        removeUnusedEquality(newrule);
        System.out.println("Learned rule body:\n\t" + newrule.toString());

        // assume we learned a constraint
        if (true) { // TODO: check if we learned a constraint
            // remove one atom from rule body and put its negation as head
            for (int i = 0; i < newrule.getBodyPlus().size()
                    + newrule.getBodyMinus().size(); i++) {

                // copy body lists for modification
                @SuppressWarnings("unchecked")
                ArrayList<Atom> atomsPlus = (ArrayList<Atom>) newrule.getBodyPlus().clone();
                @SuppressWarnings("unchecked")
                ArrayList<Atom> atomsMinus = (ArrayList<Atom>) newrule.getBodyMinus().clone();

                Atom head;
                boolean isHeadPositive;
                if (i < newrule.getBodyPlus().size()) {
                    // remove positive body atom and use as head
                    head = atomsPlus.get(i);
                    atomsPlus.remove(i);
                    isHeadPositive = false;
                } else {
                    // remove negative body atom and use as head

                    // TODO: requires a MUST-BE-TRUE, since the head would be
                    // positive. Code removed for the time being.
                    continue;
                    /*
                     head = atomsMinus.get(i-newrule.getBodyPlus().size());
                     atomsMinus.remove(i-newrule.getBodyPlus().size());
                     isHeadPositive = true;
                     */
                }

                // if learned head is from 0-th SCC, skip the rule
                ChoiceUnit cu = GlobalSettings.getGlobalSettings().getManager().getContext().getChoiceUnit();
                if (cu.getPredicateSCC(head.getPredicate()) < cu.getCurrentSCC()) {
                    continue;
                }

                Rule ruleFromConstraint = new Rule(head, atomsPlus, atomsMinus, newrule.getOperators());

                // TODO: variable equalities must be resolved here
                // the following code is just a quick workaround

                // some equality X=c might require to be changed to assignment X is c
                // and vice versa
                HashSet<Variable> bound_vars = boundVars(ruleFromConstraint);
                HashSet<Variable> head_vars = new HashSet<Variable>();
                findVariables(head, head_vars);

                // create variable mapping for replacement
                HashMap<Variable, Term> var_replace = new HashMap<Variable, Term>();

                // if body contains X=c and X only occurs in head, replace X by c
                for (Variable var : head_vars) {
                    if (!bound_vars.contains(var)) {
                        boolean foundEquality = false;
                        for (Iterator<Operator> opIt = ruleFromConstraint.getOperators().iterator(); opIt.hasNext();) {
                            Operator op = opIt.next();
                            if (op.getOP() == OP.EQUAL && op.left == var) {
                                var_replace.put(var, (Term) op.right);
                                opIt.remove();
                                foundEquality = true;
                            }
                        }
                        if (!foundEquality) {
                            throw new LearningException("Failed to find an equality for unbound variable " + var + " in rule " + ruleFromConstraint);
                        }
                    }
                }

                Atom replaced_head = replaceVariable(ruleFromConstraint.getHead(), var_replace);
                Rule final_rule = new Rule(replaced_head, ruleFromConstraint.getBodyPlus(), ruleFromConstraint.getBodyMinus(), ruleFromConstraint.getOperators());
                
                // skip rule, if it was learned already
                if( ruleIsDuplicate(final_rule) ) {
                    continue;
                }

                System.out.println("Adding to Rete: " + final_rule.toString());
                learnedRules.add(final_rule);
                rB.addRuleNeg(final_rule);
//                rB.addPropagationOnlyRule(final_rule, isHeadPositive);
                GlobalSettings.didLearn = true;
            }
        }
        //current.getVarPositions();

        //stop_criterion_met = true;

        //}

        //return newrule;
    }

    private void traceBodyPlusMinus(boolean isPlus, Rule r, Instance varAssignment,
            Rete rete, Node lastJoin, HashMap<Variable, Variable> newvar_to_oldvar, Rule learnedrule) {
        ArrayList<Atom> bodyPart;
        if (isPlus) {
            bodyPart = r.getBodyPlus();
        } else {
            bodyPart = r.getBodyMinus();
        }
        for (Atom at : bodyPart) {

            // build a grounded selectionCriterion
            Term[] selectionCriterion = new Term[at.getArity()];
            for (int i = 0; i < at.getTerms().length; i++) {
                Term term = at.getTerms()[i];
                if (term instanceof Variable) {
                    // ground the variable
                    Variable var;
                    if (newvar_to_oldvar.containsKey((Variable) term)) {
                        // get un-renamed name of variable
                        var = newvar_to_oldvar.get((Variable) term);
                    } else {
                        var = (Variable) term;
                    }
                    selectionCriterion[i] = varAssignment.get(
                            lastJoin.getVarPositions().get(var)); // get value from variable assignment
                } else if (term instanceof Constant) {
                    selectionCriterion[i] = term;
                }

            }
            Instance at_inst;
            if (isPlus) {
                at_inst = (Instance) rete.
                        getBasicLayerPlus().get(at.getPredicate()).
                        getMemory().select(selectionCriterion).iterator().next();
            } else {
                // instance only exists if the node is not closed
                if (!rete.getBasicLayerMinus().get(at.getPredicate()).isClosed()) {
                    at_inst = (Instance) rete.
                            getBasicLayerMinus().get(at.getPredicate()).
                            getMemory().select(selectionCriterion).iterator().next();
                } else {
                    // node is closed, stop tracing
                    learnedrule.addAtomMinus(at);
                    continue;
                }
            }


            if (at_inst instanceof TrackingInstance && ((TrackingInstance) at_inst).decisionLevel == rete.getChoiceUnit().getDecisionLevel()) {
                // instance was derived by a rule at same DL, trace subrule
                TrackingInstance tr_inst = (TrackingInstance) at_inst;
                Rule subrule = tr_inst.getCreatedByRule(); // copy subrule


                HashMap<Variable, Variable> sub_varmap = new HashMap<Variable, Variable>();
                // ensure correct variable renaming, for non-variables, equality operators are added
                for (int i = 0; i < at.getTerms().length; i++) {
                    Term selTerm = at.getTerms()[i];
                    Term headTerm = subrule.getHead().getTerms()[i];
                    if (headTerm instanceof Variable && selTerm instanceof Variable) {
                        sub_varmap.put((Variable) headTerm, (Variable) selTerm);
                    } else {
                        Operator var_equal = new Operator((OperandI) selTerm, (OperandI) headTerm, OP.EQUAL);
                        learnedrule.addOperator(var_equal);
                    }
                }

                // rename the variables of the subrule
                HashMap<Variable, Variable> subrenaming = new HashMap<Variable, Variable>();
                subrule = renameVariables(subrule, subrenaming, sub_varmap);

                // ensure correct variable binding, e.g. X = renamedX, Y = b
                // to that end, add additional equality atoms to the body of the learned rule
/*                for (int i = 0; i < at.getTerms().length; i++) {
                 Term selTerm = at.getTerms()[i];
                 Term headTerm = subrule.getHead().getTerms()[i];
                 Operator var_equal = new Operator((OperandI) selTerm, (OperandI) headTerm, OP.EQUAL);
                 learnedrule.addOperator(var_equal);
                 }*/

                // trace rule body
                Rule n1 = traceBody(subrule, tr_inst, rete, tr_inst.getCreatedByHeadNode(), subrenaming);

                // add learned body to current body
                for (Atom nat : n1.getBodyPlus()) {
                    learnedrule.addAtomPlus(nat);
                }
                for (Atom nat : n1.getBodyMinus()) {
                    learnedrule.addAtomMinus(nat);
                }
                for (Operator nop : n1.getOperators()) {
                    learnedrule.addOperator(nop);
                }
            } else {    // at_inst is a fact or from different decision level
                if (isPlus) {
                    learnedrule.addAtomPlus(at);
                } else {
                    learnedrule.addAtomMinus(at);
                }
            }
        }
    }

    private Rule traceBody(Rule r, Instance varAssignment, Rete rete, Node lastJoin, HashMap<Variable, Variable> newvar_to_oldvar) {
        Rule learnedrule = new Rule();

        Instance fullInstance;
        if (varAssignment instanceof TrackingInstance) {
            fullInstance = ((TrackingInstance) varAssignment).getFullInstance();
        } else {
            fullInstance = varAssignment;
        }
        traceBodyPlusMinus(true, r, fullInstance, rete, lastJoin, newvar_to_oldvar, learnedrule);

        traceBodyPlusMinus(false, r, fullInstance, rete, lastJoin, newvar_to_oldvar, learnedrule);

        for (Operator op : r.getOperators()) {
            learnedrule.addOperator(op);
        }
        return learnedrule;
    }

    public Rule learnRule1UIP() {
        return null;
    }

    private Rule renameVariables(Rule rule, HashMap<Variable, Variable> newvar_to_oldvar, HashMap<Variable, Variable> renaming) {
        HashMap<Variable, Variable> varmap;
        // use renaming if given
        if (renaming != null) {
            varmap = renaming;
        } else {
            varmap = new HashMap<Variable, Variable>();
        }

        Atom rh = rule.getHead();
        Term[] hterm = renameTerm(rh.getTerms(), varmap, newvar_to_oldvar);
        Atom renHead = Atom.getAtom(rh.getName(), rh.getArity(), hterm);

        ArrayList<Atom> renBodyPlus = new ArrayList<Atom>();
        for (Atom at : rule.getBodyPlus()) {
            Term[] newterms = renameTerm(at.getTerms(), varmap, newvar_to_oldvar);
            Atom renAtom;
            renAtom = Atom.getAtom(at.getName(), at.getArity(), newterms);
            renBodyPlus.add(renAtom);
        }
        ArrayList<Atom> renBodyMinus = new ArrayList<Atom>();
        for (Atom at : rule.getBodyMinus()) {
            Term[] newterms = renameTerm(at.getTerms(), varmap, newvar_to_oldvar);
            Atom renAtom = Atom.getAtom(at.getName(), at.getArity(), newterms);
            renBodyMinus.add(renAtom);
        }
        ArrayList<Operator> renOps = new ArrayList<Operator>();
        for (Operator op : rule.getOperators()) {
            renOps.add((Operator) renameOperator(op, varmap, newvar_to_oldvar));
        }

        return new Rule(renHead, renBodyPlus, renBodyMinus, renOps);
    }

    private Term[] renameTerm(Term[] t, HashMap<Variable, Variable> varmap, HashMap<Variable, Variable> newvar_to_oldvar) {
        Term[] newterm = new Term[t.length];
        for (int i = 0; i < t.length; i++) {
            Term term = t[i];
            if (term instanceof Variable) {
                Variable var = (Variable) term;
                if (varmap.containsKey(var)) {
                    newterm[i] = varmap.get(var);
                    newvar_to_oldvar.put(varmap.get(var), var);
                } else {
                    Variable nvar = Variable.getVariable(renamed_var_prefix + varcount++);
                    newterm[i] = nvar;
                    varmap.put(var, nvar);
                    newvar_to_oldvar.put(nvar, var);
                }
            } else {
                newterm[i] = t[i];
            }
        }
        return newterm;
    }

    private OperandI renameOperator(OperandI operand, HashMap<Variable, Variable> varmap, HashMap<Variable, Variable> newvar_to_oldvar) {

        if (operand instanceof Operator) {
            Operator op = (Operator) operand;

            OperandI left;
            OperandI right;
            if (op.left instanceof Variable) {
                Variable var = (Variable) op.left;
                if (varmap.containsKey(var)) {
                    left = varmap.get(var);
                    newvar_to_oldvar.put(varmap.get(var), var);
                } else {
                    Variable nvar = Variable.getVariable(renamed_var_prefix + varcount++);
                    left = nvar;
                    varmap.put(var, nvar);
                    newvar_to_oldvar.put(nvar, var);
                }
            } else {
                left = renameOperator(op.left, varmap, newvar_to_oldvar);
            }

            if (op.right instanceof Variable) {
                Variable var = (Variable) op.right;
                if (varmap.containsKey(var)) {
                    right = varmap.get(var);
                    newvar_to_oldvar.put(varmap.get(var), var);
                } else {
                    Variable nvar = Variable.getVariable(renamed_var_prefix + varcount++);
                    right = nvar;
                    varmap.put(var, nvar);
                    newvar_to_oldvar.put(nvar, var);
                }
            } else {
                right = renameOperator(op.right, varmap, newvar_to_oldvar);
            }
            return new Operator(left, right, op.getOP());
        } else {
            return operand;
        }

    }

    private HashSet<Variable> boundVars(Rule r) {
        HashSet<Variable> vars = new HashSet<Variable>();
        for (Atom at : r.getBodyPlus()) {
            findVariables(at, vars);
        }
        for (Atom at : r.getBodyMinus()) {
            findVariables(at, vars);
        }
        // also add variables from assignment operators
        for (Operator op : r.getOperators()) {
            if (op.getOP() == OP.ASSIGN) {
                vars.add((Variable) op.left);
            }
        }
        return vars;
    }

    /**
     * Searches operators for unneeded equalities.
     */
    private void removeUnusedEquality(Rule newrule) {
        HashSet<Variable> needed_vars = boundVars(newrule);

        ArrayList<Operator> reducedops = new ArrayList<Operator>(newrule.getOperators());
        for (Iterator<Operator> it = reducedops.iterator(); it.hasNext();) {
            Operator op = it.next();

            // remove equality operators only
            if (op.getOP() == OP.EQUAL) {
                // case X = c
                if (op.left instanceof Variable && !(op.right instanceof Variable)) {
                    Variable var = (Variable) op.left;
                    // remove operator if variable occurs nowhere else
                    if (!needed_vars.contains(var)) {
                        it.remove();
                    }
                }
                // case r = r or X = X
                if (op.left == op.right) {
                    it.remove();
                }
            }
        }
        // use reduced operators for the rule
        newrule.setOperators(reducedops);
    }

    /**
     * Collects all variables used in the Atom and adds them to the HashSet.
     */
    private void findVariables(Atom at, HashSet<Variable> needed_vars) {
        for (int i = 0; i < at.getTerms().length; i++) {
            Term term = at.getTerms()[i];
            if (term instanceof Constant) {
                // do nothing
            } else if (term instanceof FuncTerm) {
                FuncTerm ft = (FuncTerm) term;
                needed_vars.addAll(ft.getUsedVariables());
            } else if (term instanceof Variable) {
                if (!needed_vars.contains((Variable) term)) {
                    needed_vars.add((Variable) term);
                }
            }
        }
    }

    /**
     * Replaces the variables in an Atom by Terms, if a mapping is given.
     *
     * @param head the Atom where Variables have to be replaced.
     * @param var_replace the replacement-mapping of Variables to Terms.
     * @return a new Atom with variables replaced.
     */
    private Atom replaceVariable(Atom head, HashMap<Variable, Term> var_replace) {
        Term[] t = head.getTerms();
        Term[] newterm = new Term[t.length];
        for (int i = 0; i < t.length; i++) {
            Term term = t[i];
            if (term instanceof Variable) {
                Variable var = (Variable) term;
                if (var_replace.containsKey(var)) {
                    newterm[i] = var_replace.get(var);
                } else {
                    newterm[i] = t[i];
                }
            } else {
                newterm[i] = t[i];
            }
        }
        return Atom.getAtom(head.getName(), head.getArity(), newterm);
    }

    /**
     * Very simple duplicate check, based on toString() comparison.
     * @param final_rule
     * @return 
     */
    private boolean ruleIsDuplicate(Rule final_rule) {
        for (Rule rule : learnedRules) {
            if( final_rule.toString().equals(rule.toString())) {
                return true;
            }
        }
        return false;
    }
}
