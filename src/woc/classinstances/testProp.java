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

/**
 *
 * @author User
 */
public class testProp {
    
    
    public static void main(String args[]){
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
        Atom r = new Atom("r",2,r_terms);
        Atom p_X = new Atom("p",1,p_terms_X);
        Atom q_Y = new Atom("q",1,q_terms_Y);
        r1.setHead(r);
        r1.addAtomPlus(p_X);
        r1.addAtomPlus(q_Y);
        Atom t = new Atom("t",2,t_terms);
        Atom s = new Atom("s",2,s_terms);
        r2.setHead(t);
        r2.addAtomPlus(r);
        r2.addAtomPlus(s);
        Atom p_Z = new Atom("p",1,p_terms_Z);
        r3.setHead(p_Z);
        r3.addAtomPlus(s);
        r3.addAtomPlus(t);
        Atom q_Z = new Atom("q",1,q_terms_Z);
        r4.setHead(q_Z);
        r4.addAtomPlus(s);
        r4.addAtomPlus(t);
        
        Rete rete = new Rete();
        rete.addRule(r1);
        rete.addRule(r2);
        rete.addRule(r3);
        rete.addRule(r4);
        
        int nbb = 5;
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
        for(int i = 0; i < 2*nbb; i++){
            for(int j = 0; j < 2*nbb; j++){
                Term[] terms = {Constant.getConstant(String.valueOf(i)),Constant.getConstant(String.valueOf(j))};
                Instance instance = Instance.getInstance(terms);
                rete.addInstancePlus(s.getPredicate(), instance);
            }
        }
        
        
        rete.propagate();
        
        rete.printAnswerSet();
        
        System.out.println("LOL = " + Instance.lol);
        System.out.println("OMG = " + Rete.omg);
        System.out.println("ARG = " + HeadNode.arg);
    }
    
}
