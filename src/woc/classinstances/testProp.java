/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Datastructure.Rete.HeadNode;
import Entity.Atom;
import Entity.Constant;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;
import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Entity.ContextASP;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * a test class with following rules:
 * 
         * r(X,Y) :- p(X), q(Y).
         * t(X,Z) :- r(X,Y), s(Y,Z).
         * p(Z) :- s(Y,Z), t(X,Z).
         * p(Z) :- s(Y,Z), t(X,Z).
 * 
 * this class shows how bad joinnodes of bigger size than the head are
 */
public class testProp {
    
    
    public static void main(String args[]){
        // Many instances are created because we have joinnodes of size 3 which lead to heads with only size 2 --> many ins
        /*
         * r(X,Y) :- p(X), q(Y).
         * t(X,Z) :- r(X,Y), s(Y,Z).
         * p(Z) :- s(Y,Z), t(X,Z).
         * p(Z) :- s(Y,Z), t(X,Z).
         */
        
        Rule r1 = new Rule();
        Rule r2 = new Rule();
        Rule r3 = new Rule();
        Rule r4 = new Rule();
        
        Term[] r_terms = {Variable.getVariable("X"), Variable.getVariable("Y")};
        Term[] t_terms = {Variable.getVariable("X"), Variable.getVariable("Z")};
        Term[] s_terms = {Variable.getVariable("Y"), Variable.getVariable("Z")};
        Term[] p_terms_X = {Variable.getVariable("X")};
        Term[] p_terms_Z = {Variable.getVariable("Z")};
        Term[] q_terms_Y = {Variable.getVariable("Y")};
        Term[] q_terms_Z = {Variable.getVariable("Z")};
        Atom r = Atom.getAtom("r",2,r_terms);
        Atom p_X = Atom.getAtom("p",1,p_terms_X);
        Atom q_Y = Atom.getAtom("q",1,q_terms_Y);
        r1.setHead(r);
        r1.addAtomPlus(p_X);
        r1.addAtomPlus(q_Y);
        Atom t = Atom.getAtom("t",2,t_terms);
        Atom s = Atom.getAtom("s",2,s_terms);
        r2.setHead(t);
        r2.addAtomPlus(r);
        r2.addAtomPlus(s);
        Atom p_Z = Atom.getAtom("p",1,p_terms_Z);
        r3.setHead(p_Z);
        r3.addAtomPlus(s);
        r3.addAtomPlus(t);
        Atom q_Z = Atom.getAtom("q",1,q_terms_Z);
        r4.setHead(q_Z);
        r4.addAtomPlus(s);
        r4.addAtomPlus(t);
        
        Rete rete = new Rete(new ChoiceUnit(new ContextASP()));
        ReteBuilder rb = new ReteBuilder(rete);
        rb.addRule(r1);
        rb.addRule(r2);
        rb.addRule(r3);
        rb.addRule(r4);
        
        int nbb = 2;
        // Facts for p
        for(int i = 0; i < nbb;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms);
            rete.addInstancePlus(p_Z.getPredicate(), instance);
        }
        //facts for q
        for(int i = nbb; i < 2*nbb;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms);
            rete.addInstancePlus(q_Z.getPredicate(), instance);
        }
        //facts for s
        for(int i = 0; i < 2*nbb; i++){
            for(int j = 0; j < 2*nbb; j++){
                Term[] terms = {Constant.getConstant(String.valueOf(i)),Constant.getConstant(String.valueOf(j))};
                Instance instance = Instance.getInstance(terms);
                rete.addInstancePlus(s.getPredicate(), instance);
            }
        }
        
        
        rete.propagate();
        
        rete.printAnswerSet();
        
        System.out.println("Instances created = " + Instance.lol);
        System.out.println("Instances added to the Network = " + Rete.omg);
        System.out.println("Instances added through the fullfillment of a rule = " + HeadNode.arg);
        System.out.println("SATISFIABLE: " +rete.satisfiable);;
    }
    
}
