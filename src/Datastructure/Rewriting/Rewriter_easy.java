/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rewriting;

import Entity.Atom;
import Entity.ContextASP;
import Entity.ContextASPRewriting;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

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
public class Rewriter_easy {
    
    /**
     * 
     * @param c The context you want to rewrite
     * @return the rewritten context
     */
    public ContextASPRewriting rewrite(ContextASP c) throws RuleNotSafeException, FactSizeException{
        int counter = 0;
        ContextASPRewriting ret = new ContextASPRewriting();
        //ret.setINFActs(c.getAllINFacts());
        //ret.setOUTFActs(c.getAllOUTFacts());
        //ret.setChoiceUnit(c.getChoiceUnit());
        
        
        HashMap<Predicate,ArrayList<Rule>> sorted = new HashMap<Predicate,ArrayList<Rule>>();
        
        for(Rule r: c.getAllRules()){
            if(!sorted.containsKey(r.getHead().getPredicate())){
                sorted.put(r.getHead().getPredicate(), new ArrayList<Rule>());
            }
            sorted.get(r.getHead().getPredicate()).add(r);
        }
        
        for(Predicate p: sorted.keySet()){
            ArrayList<Atom> heads = new ArrayList<Atom>();
            for(Rule r: sorted.get(p)){
                counter++;
                Atom head = Atom.getAtom("rule"+counter+"_"+r.getHead().getName(), r.getHead().getArity(), r.getHead().getTerms());
                ret.addRule(new Rule(head,r.getBodyPlus(),r.getBodyMinus(), r.getOperators())); // newHead :- oldBody.
                ArrayList<Atom> bodyPos = new ArrayList<Atom>();
                bodyPos.add(head);
                ret.addRule(new Rule(r.getHead(),bodyPos, new ArrayList<Atom>(), new ArrayList<Operator>())); // oldHead :- newHead
                
                heads.add(head.getAtomAsReteKey()); // We add the atoms as ReteKeys to obtain unified variables for the rule over rules.
                
                for(Atom a: r.getBodyPlus()){
                    boolean flag1 = true;
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
                    if(flag1){
                        ArrayList<Atom> negBody = new ArrayList<Atom>();
                        negBody.add(a);
                        Rule rule2Add = new Rule(head, new ArrayList<Atom>(), negBody, new ArrayList<Operator>());
                        ret.addNegRule(rule2Add);
                    }
                }
                
                for(Atom a: r.getBodyMinus()){
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
                    if(flag1){
                        ArrayList<Atom> posBody = new ArrayList<Atom>();
                        posBody.add(a);
                        Rule rule2Add= new Rule(head, posBody, new ArrayList<Atom>(), new ArrayList<Operator>());
                        ret.addNegRule(rule2Add);
                    }
                }
                
            }

            
            //Since the old head and the new heads are of same size, reteKey will derive same variables for same positions, therefore generating correct rules!
            //TODO: Retebuilder cannot handle negRules with bodysize > 1.
            ret.addNegRule(new Rule(Atom.getAtom(p.getName(),p.getArity(),heads.get(0).getAtomAsReteKey().getTerms()),new ArrayList<Atom>(),heads,new ArrayList<Operator>()));
        }
        
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
        
        return ret;
    }
    
    
}
