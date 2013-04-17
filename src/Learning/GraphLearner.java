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
import Entity.Pair;
import Entity.Rule;
import Entity.TrackingInstance;
import Entity.Variable;
import Enumeration.OP;
import Exceptions.ImmediateBacktrackingException;
import Exceptions.LearningException;
import Interfaces.OperandI;
import Interfaces.Term;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.antlr.misc.OrderedHashSet;
import org.jgrapht.Graph;
import org.jgrapht.alg.TransitiveClosure;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

/**
 * Reconstructs a partial implication graph from a given rule and variable
 * assignment.
 *
 * @author Antonius Weinzierl
 */
public class GraphLearner {

    private static int varcount = 1;
    private static String renamed_var_prefix = "lV";
    
    private static int debugRecursionDepth = 0;
    
    private String recursionIndent(int indentation) {
        String ret = "";
        for (int i = 0; i < indentation; i++) {
            ret += "  ";
        }
        return ret;
    }
    
    // repository of already learned rules to detect duplicates.
    private static ArrayList<Rule> learnedRules = new ArrayList<Rule>();
    
    private Atom cloneAndRenameVariablesInAtom(Atom atom, Map<Variable, Variable> oldVar2newVar) {
        Term[] oldTerms = atom.getTerms();
        Term[] newTerms = new Term[oldTerms.length];

        // rename each term of the atom
        for (int i = 0; i < oldTerms.length; i++) {
            if (oldTerms[i] instanceof Variable) {
                // rename variable
                if (oldVar2newVar.containsKey((Variable) oldTerms[i])) {
                    // variable occured already
                    newTerms[i] = oldVar2newVar.get((Variable) oldTerms[i]);
                } else {
                    // put new variable
                    Variable newVar = Variable.getVariable(renamed_var_prefix + varcount++);
                    oldVar2newVar.put((Variable) oldTerms[i], newVar);
                    newTerms[i] = newVar;
                }
            } else if (oldTerms[i] instanceof Constant) {
                // put constants without cloning
                newTerms[i] = oldTerms[i];
            } else {
                // FuncTerms not treated yet
                throw new RuntimeException("Variable renaming in " + oldTerms[i].getClass() + " not yet implemented.");
            }

        }
        Atom renamedAtom = Atom.getAtom(atom.getName(), atom.getArity(), newTerms);
        return renamedAtom;
    }

    private Pair<Rule, Map<Variable,Variable>> cloneAndRenameVariables(Rule derivingRule) {
        Rule freshRule = new Rule();
        Map<Variable,Variable> oldVar2newVar = new HashMap<Variable, Variable>();

        // for positive and negative body
        for (Body part : Body.values()) {
            boolean isPositive = part == Body.POSITIVE_BODY
                    ? true : false;
            ArrayList<Atom> bodyPart = isPositive
                    ? derivingRule.getBodyPlus() : derivingRule.getBodyMinus();

            // rename each atom
            for (Atom atom : bodyPart) {
                
                Atom renamedAtom = cloneAndRenameVariablesInAtom(atom, oldVar2newVar);
                // add renamed atom to respective body
                if (isPositive) {
                    freshRule.addAtomPlus(renamedAtom);
                } else {
                    freshRule.addAtomMinus(renamedAtom);
                }
            }
        }
        
        // rename operators
        for (Operator op : derivingRule.getOperators()) {
            Operator newOp = renameVariablesInOperator(op,oldVar2newVar);
            freshRule.addOperator(newOp);
        }
        
        // rename head
        Atom oldHead = derivingRule.getHead();
        Atom newHead = cloneAndRenameVariablesInAtom(oldHead, oldVar2newVar);
        freshRule.setHead(newHead);
       
        return new Pair<Rule, Map<Variable, Variable>>(freshRule, oldVar2newVar);
    }

    private Operator renameVariablesInOperator(Operator op, Map<Variable, Variable> oldVar2newVar) {
        OperandI left = null, right = null;
        for (int i = 0; i < 2; i++) {
            boolean isLeft = (i==0) ? true : false;
            OperandI childOp = isLeft ? op.left : op.right;

            OperandI newChild = null;
            
            // switch on type of childOp
            if (childOp instanceof Operator) {
                newChild = renameVariablesInOperator((Operator) childOp, oldVar2newVar);
            } else if (childOp instanceof Variable) {
                Variable oldVar = (Variable) childOp;
                if (oldVar2newVar.containsKey(oldVar)) {
                    newChild = oldVar2newVar.get(oldVar);
                } else {
                    Variable newVar = Variable.getVariable(renamed_var_prefix + varcount++);
                    oldVar2newVar.put(oldVar, newVar);
                    newChild = newVar;
                }
            } else if (childOp instanceof Constant) {
                newChild = childOp;
            } else {
                throw new RuntimeException("Variable renaming for operator of type " + childOp.getClass() + " not yet implemented.");
            }

            // store renamed result
            if (isLeft) {
                left = newChild;
            } else {
                right = newChild;
            }
        }

        return new Operator(left, right, op.getOP());
    }

    private HashMap<Variable, Term> unify(Atom atom1, Atom atom2) {
        HashMap<Variable, Term> unifier = new HashMap<Variable, Term>();
        
        Term[] terms1 = atom1.getTerms();
        Term[] terms2 = atom2.getTerms();
        
        // atoms must have the same arity
        if( terms1.length != terms2.length) {
            return null;
        }
        
        for (int i = 0; i < terms1.length; i++) {
            Term term1 = terms1[i];
            Term term2 = terms2[i];
            boolean unified = false;
            
            // constants must be the same
            if( term1 instanceof Constant && term2 instanceof Constant) {
                if (!term1.equals(term2)) {
                    return null;
                }
                unified = true;
            }
            
            if( term1 instanceof Variable && term2 instanceof Constant) {
                // check that constant is new or equals the previous value
                if ( unifier.containsKey((Variable)term1) && !unifier.get((Variable)term1).equals(term2))  {
                    return null;
                } else {
                    // replace all occurences of the variable on the right side by the constant
                    HashMap<Variable,Term> newUnifier = new HashMap<Variable, Term>();
                    for (Map.Entry<Variable, Term> entry : unifier.entrySet()) {
                        Term value = entry.getValue();
                        // replace right-hand variables by the constant
                        if( entry.getValue().equals(term1)) {
                            value = term2;
                        }
                        newUnifier.put(entry.getKey(), value);
                    }
                    unifier = newUnifier;
                    unifier.put((Variable)term1, term2);
                }
                unified = true;
            }
            
            if( term1 instanceof Constant && term2 instanceof Variable) {
                // check that constant is new or equals the previous value
                if ( unifier.containsKey((Variable)term2) && !unifier.get((Variable)term2).equals(term1))  {
                    return null;
                } else {
                    unifier.put((Variable)term2, term1);
                }
                unified = true;
            }
            
            if( term1 instanceof Variable && term2 instanceof Variable) {
                // term1 is always kept
                unifier.put((Variable)term1, term1);
                unifier.put((Variable)term2, term1);
                unified = true;
            }
            
            if( !unified ) {
                throw new RuntimeException("Unify encountered uncovered case.");
            }
        }
        
        return unifier;
    }

    private void resolveEquality(Rule unfoldedConstraint) {
        // TODO: the following works only for Variables and Constants, not FuncTerms!
        HashMap<Variable,Term> resolvedVariables = new HashMap<Variable, Term>();
        
        // map each Variable/Constant to a unique position
        HashMap<Term,Integer> termPosition = new HashMap<Term, Integer>();
        HashMap<Integer, Term> reverseTermPosition = new HashMap<Integer, Term>();
        for (Pair<Variable, Term> pair : unfoldedConstraint.variableEqualities) {
            int curSize = termPosition.size();
            if( !termPosition.containsKey(pair.getArg1())) {
                termPosition.put(pair.getArg1(), curSize);
                reverseTermPosition.put(curSize, pair.getArg1());
            }
            curSize = termPosition.size();
            if( !termPosition.containsKey(pair.getArg2())) {
                termPosition.put(pair.getArg2(), curSize);
                reverseTermPosition.put(curSize, pair.getArg2());
            }
        }
        
        int numVertices = termPosition.size();
        
        // make equalities relation symmetric
        boolean[][] eqRelation = new boolean[numVertices][numVertices];
        for (Pair<Variable, Term> pair : unfoldedConstraint.variableEqualities) {
            int t1Pos = termPosition.get(pair.getArg1());
            int t2Pos = termPosition.get(pair.getArg2());
            eqRelation[t1Pos][t2Pos] = true;
            eqRelation[t2Pos][t1Pos] = true;
        }
        // make it reflexive
        for (int i = 0; i < numVertices; i++) {
            eqRelation[i][i]=true;
            
        }

        // compute transitive-closure (Floyd-Warshall)
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    eqRelation[i][j] = eqRelation[i][j] || (eqRelation[i][k] && eqRelation[k][j]);
                }
            }
        }
        
        // successively remove all vertices and ensure they are replaced by lowest vertex that they reach
        // start with Constants first:
        boolean[] termProcessed = new boolean[numVertices];
        for (Iterator<Term> it = termPosition.keySet().iterator(); it.hasNext();) {
            Term term = it.next();
            int termPos = termPosition.get(term);
            if( termProcessed[termPos]) {   // skip already processed Terms
                continue;
            }
            if( term instanceof Constant) {
                // find all variables that equal the constant transitively
                for (int i = 0; i < numVertices; i++) {
                    if( eqRelation[termPos][i] /*&& !termProcessed[i]*/) {
                        Term equalTerm = reverseTermPosition.get(i);
                        if( equalTerm instanceof Constant) {
                            // check that constants are the same (should always be the case)
                            if( equalTerm != term) {
                                throw new RuntimeException("Resolving equality of variables failed: "+term+ " != "+equalTerm);
                            }
                        } else if (equalTerm instanceof Variable) {
                            resolvedVariables.put((Variable)equalTerm, term);
                            termProcessed[i] = true;
                        }
                    }
                }
                termProcessed[termPos] = true;
            }
        }
        
        // now process all remaining variables (which do not equal some constant)
        for (Iterator<Term> it = termPosition.keySet().iterator(); it.hasNext();) {
            Term term = it.next();
            int termPos = termPosition.get(term);
            if( termProcessed[termPos]) {   // skip already processed Terms
                continue;
            }
            if( term instanceof Variable) {
                // consider all smaller variables
                for (int i = 0; i <= termPos; i++) {
                    if ( eqRelation[termPos][i]) {
                        resolvedVariables.put((Variable)term, reverseTermPosition.get(i));
                        termProcessed[termPos] = true;
                        break;
                    }
                }
            }
        }
        
        for (int i = 0; i < numVertices; i++) {
            if( !termProcessed[i]) {
                throw new RuntimeException("Term "+reverseTermPosition.get(i)+" has not been processed.");
            }
        }

        
        //System.out.println("Variable equalities are: "+unfoldedConstraint.variableEqualities);
        //System.out.println("Resolved equalities are: "+resolvedVariables);
        
        replaceVariables(unfoldedConstraint, resolvedVariables);
        unfoldedConstraint.variableEqualities = null;

        
/*        // TODO: final renaming may be skipped
        // rename all remaining variables to X1, X2, ...
        HashMap<Variable,Variable> renamedVars = new HashMap<Variable, Variable>();
        int varCounter = 0;
        for (Map.Entry<Variable, Term> entry : resolvedVariables.entrySet()) {
            if (entry.getValue() instanceof Variable ) {
                if( !renamedVars.containsKey((Variable)entry.getValue())) {
                    renamedVars.put((Variable)entry.getValue(), Variable.getVariable("X"+varCounter++));
                }
                entry.setValue(renamedVars.get((Variable)entry.getValue()));
            }
        }
*/        
//        System.out.println("Resolved constraint is:\n\t"+unfoldedConstraint);
    }

    private void replaceVariables(Rule unfoldedConstraint, HashMap<Variable, Term> resolvedVariables) {
        ArrayList<Atom> replacePositiveBody = new ArrayList<Atom>(), replaceNegativeBody = new ArrayList<Atom>();
        // for positive and negative body
        for (int i = 0; i < 2; i++) {
            boolean isPositive = (i==1) ? true : false;
            ArrayList<Atom> bodyPart = isPositive ?
                    unfoldedConstraint.getBodyPlus() : unfoldedConstraint.getBodyMinus();
            // create new atoms where all variables in terms are replaced
            for (Atom atom : bodyPart) {
                Term[] terms = atom.getTerms();
                Term[] replaceTerms = new Term[terms.length];
                for (int j = 0; j < terms.length; j++) {
                    if( terms[j] instanceof Variable && resolvedVariables.containsKey((Variable)terms[j])) {
                        replaceTerms[j] = resolvedVariables.get((Variable)terms[j]);
                    } else {
                        replaceTerms[j] = terms[j];
                    }
                }
                Atom replaceAtom = Atom.getAtom(atom.getName(), atom.getArity(), replaceTerms);
                if( isPositive ) {
                    replacePositiveBody.add(replaceAtom);
                } else {
                    replaceNegativeBody.add(replaceAtom);
                }
            }
        }
        
        // replace existing atoms with new ones
        unfoldedConstraint.setBodyPlus(replacePositiveBody);
        unfoldedConstraint.setBodyMinus(replaceNegativeBody);

        
        // replace all variables in operators
        // requires new operators since those have not been cloned earlier
        ArrayList<Operator> newOperators = new ArrayList<Operator>();
        for (Operator operator : unfoldedConstraint.getOperators()) {
            newOperators.add(replaceVariables(operator, resolvedVariables));
        }
        unfoldedConstraint.setOperators(newOperators);
    }

    private Operator replaceVariables(Operator operator, HashMap<Variable, Term> resolvedVariables) {
        OperandI left = null, right = null;
        for (int i = 0; i < 2; i++) {
            boolean isLeft = (i==0) ? true : false;
            OperandI operand = isLeft ? operator.left : operator.right;
            
            if( operand instanceof Variable ) {
                if( resolvedVariables.containsKey((Variable)operand)) {
                    operand = resolvedVariables.get((Variable)operand);
                }
            } else if (operand instanceof Operator) {
                operand = replaceVariables((Operator)operand, resolvedVariables);
            } else if (operand instanceof Constant) {
                
            } else {
                throw new RuntimeException("Replacing variables in operator misses case for: "+operator);
            }
            
            if( isLeft ) {
                left = operand;
            } else {
                right = operand;
            }
        }
        return new Operator(left, right, operator.getOP());
    }


    // helper
    enum Body { POSITIVE_BODY, NEGATIVE_BODY };
    
    public void learnRuleFromConflict(Rule r, Instance varAssignment, HeadNode starting, Rete rete) {
        
        // for benchmarks: skip learning if required
        if (GlobalSettings.noLearning) {
            rete.satisfiable = false;
            System.out.println("Interpretation unsatisfiable, halting propagation with ImmediateBacktrackingException");
            throw new ImmediateBacktrackingException(rete.getChoiceUnit().getDecisionLevel());
        }
        
        int conflictCauseAtDL;

        //System.out.println("Start unfoldConstraint.");
        Pair<Rule,Integer> learned = unfoldConstraint(r, varAssignment, starting, new HashMap<Variable, Variable>(),rete);
        Rule learnedConstraint = learned.getArg1();
        conflictCauseAtDL = learned.getArg2();
        //System.out.println("Unfolded constraint:\n\t"+learnedConstraint);
        //System.out.println("Equalities:\n\t"+learnedConstraint.variableEqualities);
        
        // clean-up equalities in the constraint
        resolveEquality(learnedConstraint);
        //System.out.println("Resolved equalities:\n\t"+learnedConstraint);
        System.out.println("Learned constraint: "+learnedConstraint);
        System.out.println("Conflict cause @DL="+conflictCauseAtDL);
        System.out.println("Decision Level: "+rete.getChoiceUnit().getDecisionLevel());
        
        ChoiceUnit cu = GlobalSettings.getGlobalSettings().getManager().getContext().getChoiceUnit();
        
        // avoid learning too big rules, restrict rule/constraint size
        int bodySize = learnedConstraint.getBodyPlus().size() + learnedConstraint.getBodyMinus().size();
        if (bodySize > 6 + Math.sqrt(cu.getDecisionLevel())) {
            System.out.println("Skipping too large rules of size " + bodySize + " at DL=" + cu.getDecisionLevel());
        } else {


            // derive safe constraint-induced rules
            ReteBuilder reteBuilder = new ReteBuilder(rete);
            int currentSCC = cu.getCurrentSCC();
            for (int i = 0; i < learnedConstraint.getBodyPlus().size()
                    + learnedConstraint.getBodyMinus().size(); i++) {

                // copy body lists for modification
                @SuppressWarnings("unchecked")
                ArrayList<Atom> atomsPlus = (ArrayList<Atom>) learnedConstraint.getBodyPlus().clone();
                @SuppressWarnings("unchecked")
                ArrayList<Atom> atomsMinus = (ArrayList<Atom>) learnedConstraint.getBodyMinus().clone();

                Atom head;
                if (i < learnedConstraint.getBodyPlus().size()) {
                    // remove positive body atom and use as head
                    head = atomsPlus.get(i);
                    atomsPlus.remove(i);
                } else {
                    // remove negative body atom and use as head

                    // TODO: requires a MUST-BE-TRUE, since the head would be
                    // positive. Code removed for the time being.
                    continue;
                }

                // if learned head is from smaller SCC, skip the rule
                // TODO: maybe 0-th SCC is better?
                // FUTURE WORK: due to learning, some constraint might be violated at lower DL than we currently are.
                //      Learning only from current SCC also prevents useless learning for lower DLs
                if (cu.getPredicateSCC(head.getPredicate()) < currentSCC) {
                    //if (cu.getPredicateSCC(head.getPredicate()) == 0) {
                    continue;
                }

                Rule ruleFromConstraint = new Rule(head, atomsPlus, atomsMinus, learnedConstraint.getOperators());

                // skip if head is not safe
                if (!ruleFromConstraint.isSafePropagation()) {
                    System.out.println("Rule not safe, skipping: " + ruleFromConstraint);
                    continue;
                }

                // skip rule, if it was learned already
                if (ruleIsDuplicate(ruleFromConstraint)) {
                    //System.out.println("Skipping duplicate rule: "+ruleFromConstraint);
                    continue;
                }

                // add constraint-induced rule to rete
                System.out.println("Adding to Rete: " + ruleFromConstraint.toString());
                learnedRules.add(ruleFromConstraint);
                reteBuilder.addRuleNeg(ruleFromConstraint);
                GlobalSettings.didLearn = true;

            }
        }
        if(! GlobalSettings.didLearn) {
            //System.out.println("Skipped learned constraint: "+learnedConstraint);
        }
        
        // everything went well, halt propagation immediately
        rete.satisfiable = false;
        System.out.println("Interpretation unsatisfiable, halting propagation with ImmediateBacktrackingException");
        if ( conflictCauseAtDL != rete.getChoiceUnit().getDecisionLevel()) {
            conflictCauseAtDL = conflictCauseAtDL;
        }
        throw new ImmediateBacktrackingException(conflictCauseAtDL);
    }
    
    private Term[] getGroundInstance(Atom at, Instance varAssignment, HeadNode starting, Map<Variable, Variable> oldVar2newVar) {
        Term[] selectionCriterion = new Term[at.getArity()];
        for (int i = 0; i < at.getTerms().length; i++) {
            Term term = at.getTerms()[i];
            if (term instanceof Variable) {
                // ground the variable
/*                        Variable var;
                 if (newvar_to_oldvar.containsKey((Variable) term)) {
                 // get un-renamed name of variable
                 var = newvar_to_oldvar.get((Variable) term);
                 } else {
                 var = (Variable) term;
                 }
                 */
                // check if variable was renamed, i.e., reverted-search in oldVar2newVar
                Variable oldVar = null;
                boolean isRenamed = false;
                for (Map.Entry<Variable, Variable> entry : oldVar2newVar.entrySet()) {
                    if( entry.getValue() == (Variable)term ) {
                        oldVar = entry.getKey();
                        isRenamed = true;
                    }
                }
                if ( !isRenamed ) {
                    oldVar = (Variable)term;
                }
                    
                selectionCriterion[i] = varAssignment.get(
                        starting.getVarPositions().get(oldVar)); // get value from variable assignment
            } else if (term instanceof Constant) {
                selectionCriterion[i] = term;
            }

        }
        return selectionCriterion;
    }
    
    public Pair<Rule,Integer> unfoldConstraint(Rule r, Instance varAssignment, HeadNode starting, Map<Variable, Variable> oldVar2newVar, Rete rete) {

//        debugRecursionDepth++;
        int conflictCauseAtDL = 0;
        
        Rule unfoldedBody = new Rule();
        
        Instance fullInstance;
        if (varAssignment instanceof TrackingInstance) {
            fullInstance = ((TrackingInstance) varAssignment).getFullInstance();
        } else {
            fullInstance = varAssignment;
        }
 
//        System.out.println("Rule to unfold: "+r);
        
        ArrayList<Atom> bodyPart = null;
        for (Body bodyParts : Body.values()) {
            if (bodyParts == Body.POSITIVE_BODY) {
                bodyPart = r.getBodyPlus();
            }
            if (bodyParts == Body.NEGATIVE_BODY) {
                bodyPart = r.getBodyMinus();
            }

            for (Atom atom : bodyPart) {
                // build ground instance
                Term[] groundInstance = getGroundInstance(atom, varAssignment, starting, oldVar2newVar);
//                System.out.println("Nonground Atom is: "+atom);
//                System.out.println("Grounding: "+Arrays.toString(groundInstance));

                // find instance in Rete memory
                Instance at_inst;
                if (bodyParts == Body.POSITIVE_BODY) {
                    at_inst = (Instance) rete.
                            getBasicLayerPlus().get(atom.getPredicate()).
                            getMemory().select(groundInstance).iterator().next();
                } else {
                    // instance only exists if the node is not closed
                    if (!rete.getBasicLayerMinus().get(atom.getPredicate()).isClosed()) {
                        at_inst = (Instance) rete.
                                getBasicLayerMinus().get(atom.getPredicate()).
                                getMemory().select(groundInstance).iterator().next();
                    } else {
                        // node is closed, stop tracing
                        int closedAtDL = rete.getBasicLayerMinus().get(atom.getPredicate()).getClosedAtDL();
                        if( closedAtDL > conflictCauseAtDL) {
                            conflictCauseAtDL = closedAtDL;
                        }
                        System.out.println(atom.toString()+"closed @"+conflictCauseAtDL);
                        unfoldedBody.addAtomMinus(atom);
                        continue;
                    }
                }
                // save DL of cause
                if( at_inst.decisionLevel > conflictCauseAtDL) {
                    conflictCauseAtDL = at_inst.decisionLevel;
                }
                System.out.println(atom.toString()+at_inst+"@"+conflictCauseAtDL);
                

                
                // check if we should continue tracing
                if (at_inst instanceof TrackingInstance
                        && ((TrackingInstance) at_inst).decisionLevel == rete.getChoiceUnit().getDecisionLevel()) {
                    // instance was derived by a rule at same DL, trace subrule
                    
                    // find deriving rule
                    TrackingInstance tr_inst = (TrackingInstance) at_inst;
                    Rule derivingRule = tr_inst.getCreatedByRule(); // copy subrule
                    
//                    System.out.println(recursionIndent(debugRecursionDepth)+"Subrule @DL="+tr_inst.decisionLevel+": "+derivingRule);
                    
                    // clone it and rename all variables in deriving rule
                    Pair<Rule, Map<Variable, Variable>> renamedPair = cloneAndRenameVariables(derivingRule);
                    
                    Rule freshRule = renamedPair.getArg1();
                    Map<Variable, Variable> freshOldVar2newVar = renamedPair.getArg2();
                    
//                    System.out.println("Deriving rule: "+derivingRule);
//                    System.out.println("Cloned and renamed: "+freshRule);
                    //System.out.println("OldVar2NewVar (fresh): "+freshOldVar2newVar);
                    
                    // unify renamed rule's head with atom
                    HashMap<Variable, Term> unifier;
                    unifier = unify(atom, freshRule.getHead());
//                    System.out.println("Unifier is: "+unifier.toString());

//                    System.out.println("Start recursive unfolding.");
                    // recursively unfold rule
                    Pair<Rule,Integer> recursive = unfoldConstraint(freshRule, tr_inst.getFullInstance(), tr_inst.getCreatedByHeadNode(), freshOldVar2newVar, rete);
                    Rule recursiveUnfold = recursive.getArg1();
                    int recursiveCauseAtDL = recursive.getArg2();
                    if( recursiveCauseAtDL > conflictCauseAtDL) {
                        conflictCauseAtDL = recursiveCauseAtDL;
                    }
//                    System.out.println("Return from recursive unfolding.");


                    // add recursively unfolded rule's body to unfoldedBody
                    unfoldedBody.getBodyPlus().addAll(recursiveUnfold.getBodyPlus());
                    unfoldedBody.getBodyMinus().addAll(recursiveUnfold.getBodyMinus());
                    unfoldedBody.getOperators().addAll(recursiveUnfold.getOperators());


                    // add equality establishing the unification
                    ArrayList<Pair<Variable,Term>> varEqualities = new ArrayList<Pair<Variable, Term>>();
                    for (Variable variable : unifier.keySet()) {
                        varEqualities.add(new Pair<Variable, Term>(variable, unifier.get(variable)));
                    }
                    //for (Map.Entry<Variable, Variable> entry : freshOldVar2newVar.entrySet()) {
                    //    varEqualities.add(new Pair<Variable, Term>(entry.getKey(), entry.getValue()));
                    //}

                    unfoldedBody.variableEqualities.addAll(varEqualities);
                    unfoldedBody.variableEqualities.addAll(recursiveUnfold.variableEqualities);
                    //unfoldedBody.variableEqualities.putAll(recursiveUnfold.variableEqualities);
                    /*for (Map.Entry<Variable, Term> equality : unifier.entrySet()) {
                        Operator eqOp = new Operator(equality.getKey(), equality.getValue(), OP.EQUAL);
                        unfoldedBody.addOperator(eqOp);
                    }*/
//                    System.out.println("Unfolded rule so far: "+unfoldedBody);
//                    System.out.println("");

                } else {    // at_inst is a fact or from different decision level
                    if (bodyParts == Body.POSITIVE_BODY) {
                        unfoldedBody.addAtomPlus(atom);
                    } else {
                        unfoldedBody.addAtomMinus(atom);
                    }
                }
                
            }

        }

        // add operators to unfolded rule
        for (Operator op : r.getOperators()) {
             unfoldedBody.addOperator(op);           
        }
//        System.out.println("Returning unfoldedBody:\t"+unfoldedBody);
//        System.out.println("unfoldedBody equalities:\t"+unfoldedBody.variableEqualities);
//        debugRecursionDepth--;
        return new Pair<Rule, Integer>(unfoldedBody,conflictCauseAtDL);
    }
    
    public static int debugCounter = 0;
    
    /**
     * Learns a new rule and adds the learned rule to the Rete network; finishes with a thrown ImmediateBacktrackingException to stop propagation.
     * @param r the constraint that was violated.
     * @param var_assignment the variables to ground the constraint r.
     * @param starting the HeadNode that belongs to r.
     * @throws ImmediateBacktrackingException thrown after a successful learning. It must be caught in Context.propagate() and not earlier.
     * @throws LearningException a helper exception for debugging, should be thrown if the learned rule is wrong in some basic aspects.
     */

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
