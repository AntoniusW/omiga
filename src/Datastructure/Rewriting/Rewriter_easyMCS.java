/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rewriting;

import Entity.Atom;
import Entity.ContextASPMCSRewriting;
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

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The Rewriting does the following:
 * For each rule head(X,Y) :- body(X,Y)... .
 * We create another rule: rule1_head(X,Y) :- body(X,Y)... .
 * This we we obtain that rules where choices are made on have unique heads.
 * Then another rule is added for:
 * head(X,Y) :- ruleI_head(X,Y)... . // for all I rules with the same head
 * 
 * Furtheremore we may add rules like this:
 * If positive body, and head completely fullfilled by atom: -rule1_head(X,Y) :- -body(X,Y).
 * and for negative ones: -rule1_head(X,Y) :- body(X,Y).
 * furtheremore we add: -head(X,Y) :- ruleI_head(X,Y)... . // for all I rules with the same head
 * 
 */
public class Rewriter_easyMCS {
    
    /**
     * 
     * @param c The context you want to rewrite
     * @return the rewritten context
     */
    public ContextASPMCSRewriting rewrite(ContextASPMCSRewriting c) throws RuleNotSafeException, FactSizeException{
        int counter = 0;
        ContextASPMCSRewriting ret = new ContextASPMCSRewriting();
        
        
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
                        ret.addNegRule(rule2Add);
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
                        ret.addNegRule(rule2Add);
                    }
                }
                
            }
            
             // Now we generate one negative rule for all rules of same original head for each such head
            // The head of this rule is the original head and the body consists only of body minus containing 
            // all heads of the new positive rules we allready created in our rewriting
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
                ret.addNegRule(rule2Add);
            }
        }
        
        // We preserve all facts of the old context
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
        // We preserve all states for facts that can come from outside
        for (Predicate predicate : c.getFromOutside().keySet()) {
            ret.registerFactFromOutside(predicate);
        }
        
        return ret;
    }
    
    
}
