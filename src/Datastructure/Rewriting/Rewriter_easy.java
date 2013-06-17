/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rewriting;

import Datastructure.Rete.Node;
import Entity.Atom;
import Entity.ContextASP;
import Entity.ContextASPRewriting;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The Rewriting does the following:
 * For each rule head(X,Y) :- body(X,Y)... .
 * We create another rule: rule1_head(X,Y) :- body(X,Y)... .
 * This way we obtainthe situation that rules where choices are made, have unique heads.
 * Then another rule is added for:
 * head(X,Y) :- ruleI_head(X,Y)... . // for all I rules with the same head
 * 
 * Furtheremore we may add rules like this:
 * If positive body, and head completely fullfilled by atom: -rule1_head(X,Y) :- -body(X,Y).
 * and for negative ones: -rule1_head(X,Y) :- body(X,Y).
 * furtheremore we add: -head(X,Y) :- ruleI_head(X,Y)... . // for all I rules with the same head
 * 
 */
public class Rewriter_easy {
    
    /**
     * 
     * Rewrites the given context in such a way that no two rules have the same head.
     * by preserving the same answersets.
     * 
     * @param c The context you want to rewrite
     * @return the rewritten context
     */
    public ContextASPRewriting rewrite(ContextASP c) throws RuleNotSafeException, FactSizeException{
        int counter = 0;
        ContextASPRewriting ret = new ContextASPRewriting();
        
        // reset global nodes container
        Node.nodes = new HashSet<Node>();
        
        
        HashMap<Predicate,ArrayList<Rule>> sorted = new HashMap<Predicate,ArrayList<Rule>>();
        
        //We sort all the rules by it's head predicate
        for(Rule r: c.getAllRules()){
            if(!r.isConstraint()){
                if(!sorted.containsKey(r.getHead().getPredicate())){
                    sorted.put(r.getHead().getPredicate(), new ArrayList<Rule>());
                }
                sorted.get(r.getHead().getPredicate()).add(r);
            }else{
                ret.addRule(r);
            }
            
        }
        
        for(Predicate p: sorted.keySet()){
            //wir gehen jeweils alle regeln mit gleichem head durch
            ArrayList<Atom> heads = new ArrayList<Atom>();
            for(Rule r: sorted.get(p)){
                counter++;
                HashSet<Variable> temp = new HashSet<Variable>();
                
                for(Atom a: r.getBodyPlus()){
                    for(Term t: a.getTerms()){
                        if(t.getClass().equals(Variable.class)){
                            temp.add((Variable)t);
                        }
                    }
                }
                Variable[] newHeadVars = new Variable[temp.size()];
                int i = 0;
                for(Variable v: temp){
                    newHeadVars[i] = v;
                    i++;
                }
                //the new Head contains all Variables of the rules body, to be unique
                Atom head = Atom.getAtom("rule"+counter+"_"+r.getHead().getName(), newHeadVars.length, newHeadVars);
                ret.addRule(new Rule(head,r.getBodyPlus(),r.getBodyMinus(), r.getOperators())); // newHead :- oldBody.
                ArrayList<Atom> bodyPos = new ArrayList<Atom>();
                bodyPos.add(head);
                ret.addRule(new Rule(r.getHead(),bodyPos, new ArrayList<Atom>(), new ArrayList<Operator>())); // oldHead :- newHead
                
                heads.add(head.getAtomAsReteKey()); // We add the atoms as ReteKeys to obtain unified variables for the rule over rules.
                
                // Now we create negative rules for optimisation that put heads into out if a rule cannot be fullfilled anymore.
                // We can not do this in general, but we do it for Bodyatoms which Variables are equal to those of the head
                // this way it's easy to determine that a rule will never lead to the grounded head.
                // For everything else no optimisation is done. Maybe we can find additional rules here to further optimize?
                //This optimisation could be done for all new rules (newHead :- oldhead.), but this has no sence, since they only trigger
                // derivation of the oldhead, and newHead is not contained in any negative body.
                for(Atom a: r.getBodyPlus()){
                    boolean flag1 = true;
                    //All Variables of the head must occur within the atom
                    for(Term t1: r.getHead().getTerms()){
                        boolean flag2 = false;
                        for(Term t2: a.getTerms()){
                            if(t1.equals(t2)) {
                                flag2 = true;
                            }
                        }
                        if(!flag2){
                            flag1=false;
                            break;
                        }
                    }
                    //All Variables of the atom must occur within the head
                    for(Term t1: a.getTerms()){
                        boolean flag2 = false;
                        for(Term t2: r.getHead().getTerms()){
                            if(t1.equals(t2)) {
                                flag2 = true;
                            }
                        }
                        if(!flag2){
                            flag1=false;
                            break;
                        }
                    }
                    
                    
                    if(flag1){
                        //The Variables of head and atom are equal
                        ArrayList<Atom> negBody = new ArrayList<Atom>();
                        negBody.add(a);
                        Rule rule2Add = new Rule(head, new ArrayList<Atom>(), negBody, new ArrayList<Operator>());
                        checkVariablesAddNegRule(rule2Add, ret);
                        //ret.addNegRule(rule2Add);
                    }
                }
                
                for(Atom a: r.getBodyMinus()){
                    //All Variables of the head must occur within the atom
                    boolean flag1 = true;
                    for(Term t1: r.getHead().getTerms()){
                        boolean flag2 = false;
                        for(Term t2: a.getTerms()){
                            if(t1.equals(t2)) flag2 = true;
                        }
                        if(!flag2){
                            flag1=false;
                            break;
                        }
                    }
                    //All Variables of the atom must occur within the head
                    for(Term t1: a.getTerms()){
                        boolean flag2 = false;
                        for(Term t2: r.getHead().getTerms()){
                            if(t1.equals(t2)) flag2 = true;
                        }
                        if(!flag2){
                            flag1=false;
                            break;
                        }
                    }
                    
                    
                    if(flag1){
                        //The Variables of head and atom are equal
                        ArrayList<Atom> posBody = new ArrayList<Atom>();
                        posBody.add(a);
                        Rule rule2Add= new Rule(head, posBody, new ArrayList<Atom>(), new ArrayList<Operator>());
                        checkVariablesAddNegRule(rule2Add, ret);
                        //ret.addNegRule(rule2Add);
                    }
                }
                
            }
            
            // Now we generate one negative rule for all rules of same original head for each such head
            // The head of this rule is the original head and the body consists only of body minus containing 
            // all heads of the new positive rules we allready created in our rewriting
            // TO OPTIMIZE: Better optimisation for heads with Constants. For those heads seperate rules may be created
            // since there might be a rule that leads to p(X,Y) and one that leads to p(X,a), then we can derive
            // -p(a,b) even if p(a,a) can still be derived, which is not possible in the actual implementeion.
            for(int i = 0; i < sorted.get(p).size();i++){
            Rule r1 = sorted.get(p).get(i);
                ArrayList<Atom> BodyMinus = new ArrayList<Atom>();
                Atom headAtom = Atom.getAtom(r1.getHead().getName(), r1.getHead().getArity(), r1.getHead().getAtomAsReteKey().getTerms());
                for(int j = 0; j < sorted.get(p).size();j++){
                    Rule r2 = sorted.get(p).get(j);
                    //if(!r1.equals(r2)){
                        if(r1.getHead().fatherOf(r2.getHead())){
                            BodyMinus.add(heads.get(j));
                        }
                    //}
                }
                Rule rule2Add = new Rule(headAtom, new ArrayList<Atom>(), BodyMinus, new ArrayList<Operator>());
                checkVariablesAddNegRule(rule2Add,ret);
            }
        }
        
        // for each predicate pred(X,Y,Z), create a constraint
        // :- pred(X,Z,Y), not pred(X,Y,Z).
        for (Iterator it = Predicate.getPredicatesIterator(); it.hasNext();) {
            Predicate predicate = (Predicate)it.next();
            Rule predConstraint = new Rule();
            Term[] terms = new Term[predicate.getArity()];
            for (int i = 0; i < terms.length; i++) {
                terms[i] = Variable.getVariable("T"+i);
            }
            Atom at = Atom.getAtom(predicate.getName(), predicate.getArity(), terms);
            predConstraint.addAtomPlus(at);
            predConstraint.addAtomMinus(at);
            
            ret.addRule(predConstraint);
        }
        
        // We keep all Facts of the input context as they are and put them into the rewritten context as well
        for(Predicate p: c.getAllINFacts().keySet()){
            for(Instance inz: c.getAllINFacts().get(p)){
                ret.addFact2IN(p, inz);
            }
        }
        for(Predicate p: c.getAllOUTFacts().keySet()){
            for(Instance inz: c.getAllINFacts().get(p)){
                ret.addFact2OUT(p, inz);
            }
        }
        // keep all rules with negative heads
        for (Rule negrule : c.getNegRules()) {
            ret.addNegRule(negrule);
        }
        
        return ret;
    }

    private void checkVariablesAddNegRule(Rule rule2Add, ContextASPRewriting ret) {
        // check if all variables in the head are contained in the body
        HashSet<Variable> varsHead = rule2Add.getHead().getVariables();
        HashSet<Variable> varsBody = new HashSet<Variable>();
        varsBody.addAll(rule2Add.getVariablesInAtoms(rule2Add.getBodyPlus()));
        varsBody.addAll(rule2Add.getVariablesInAtoms(rule2Add.getBodyMinus()));
        varsHead.removeAll(varsBody);
        // only add rule if all head variables are contained in the body
        if (varsHead.isEmpty()) {
            rule2Add.setHeadType(Rule.HEAD_TYPE.negative);
            ret.addNegRule(rule2Add);
        }
    }
    
    
}
