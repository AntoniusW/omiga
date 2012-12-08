/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Entity.Atom;
import Entity.Constant;
import Entity.ContextASP;
import Entity.Instance;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import Manager.Manager;

/**
 *
 * @author User
 */
public class Birds {
    
    /*p(X) :- sp(X).
    b(X) :- p(X).
    b(X) :- o(X).

    f(X) :- b(X), not p(X), not o(X).
    f(X) :- sp(X).
    nf(X) :- p(X), not sp(X).
    nf(X) :- o(X).*/
    
    public static void main(String args[]) throws RuleNotSafeException, FactSizeException{
        System.out.println("STARTING THE BirdsExample: " + System.currentTimeMillis());
        int nbb = 200000;
        
        Term[] termX = {Variable.getVariable("X")};
        Atom p = Atom.getAtom("p",1,termX);
        Atom sp = Atom.getAtom("sp",1,termX);
        Atom b = Atom.getAtom("b",1,termX);
        Atom o = Atom.getAtom("o",1,termX);
        Atom f = Atom.getAtom("f",1,termX);
        Atom nf = Atom.getAtom("nf",1,termX);
        
        
        
        Rule r1 = new Rule();     Rule r2 = new Rule();        Rule r3 = new Rule();        Rule r4 = new Rule();        Rule r5 = new Rule();        Rule r6 = new Rule();Rule r7 = new Rule();
        r1.setHead(p);
        r1.addAtomPlus(sp);
        r2.setHead(b);
        r2.addAtomPlus(p);
        r3.setHead(b);
        r3.addAtomPlus(o);
        r4.setHead(f);
        r4.addAtomPlus(b);
        r4.addAtomMinus(p);
        r4.addAtomMinus(o);
        r5.setHead(f);
        r5.addAtomPlus(sp);
        r6.setHead(nf); r6.addAtomPlus(p);r6.addAtomMinus(sp);
        r7.setHead(nf); r7.addAtomPlus(o);
        
        Rete rete = new Rete(new ChoiceUnit(new ContextASP()));
        ReteBuilder rb = new ReteBuilder(rete);
        rb.addRule(r1);
        rb.addRule(r2);
        rb.addRule(r3);
        rb.addRule(r4);
        rb.addRule(r5);
        rb.addRule(r6);
        rb.addRule(r7);
        /*ContextASP c = new ContextASP();
        c.addRule(r1);
        c.addRule(r2);
        c.addRule(r3);
        c.addRule(r4);
        c.addRule(r5);
        c.addRule(r6);
        c.addRule(r7);*/
        
        /*b(1..N) :- nbb(N).
        o(1..K) :- nbb(N), K = N / 10.
        p(K..N) :- nbb(N), K = N - (N / 5).
        sp(K..N) :- nbb(N), K = N - (N / 10).
        */
        System.err.println("Starting to create facts!");
        for(int i = 1; i <= nbb;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms,0);
            //c.addFact2IN(p.getPredicate(), instance);
            rete.addInstancePlus(p.getPredicate(),instance);
        }
        System.err.println("1GESCHAFFT");
        for(int i = 1; i <= nbb/10;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms,0);
            //c.addFact2IN(p.getPredicate(), instance);
            rete.addInstancePlus(p.getPredicate(),instance);
        }
        System.err.println("2GESCHAFFT");
        for(int i = nbb-(nbb/5); i <= nbb;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms,0);
            //c.addFact2IN(p.getPredicate(), instance);
            rete.addInstancePlus(p.getPredicate(),instance);
        }
        System.err.println("3GESCHAFFT");
        for(int i = nbb-(nbb/10); i <= nbb;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms,0);
            //c.addFact2IN(p.getPredicate(), instance);
            rete.addInstancePlus(p.getPredicate(),instance);
        }
        System.err.println("4GESCHAFFT: Starting to propagate: " + System.currentTimeMillis());
        
        rete.propagate();
        System.out.println("Instances created = " + Instance.lol);
        System.out.println("SATISFIABLE: " +rete.satisfiable);;
        System.out.println(System.currentTimeMillis());
        
        /*Manager m = new Manager(c);
        System.out.println("Starting calculation: " + System.currentTimeMillis());
        m.calculate();
        System.out.println("Program finished: " + System.currentTimeMillis());*/
        
    }
    
}
