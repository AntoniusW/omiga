/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Interfaces.Term;
import Entity.Variable;
import Entity.Atom;
import Entity.Constant;
import Entity.Instance;
import Entity.Rule;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import Interfaces.Term;
import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Entity.ContextASP;
import Entity.Predicate;

/**
 *
 * @author User
 */
public class testPropagation {
    
    public testPropagation() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testChaining1(){
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
        
        Term[] r_terms = {Variable.getVariable("X"), Variable.getVariable("X")};
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
        
        int nbb = 10;
        // Facts for p
        for(int i = 0; i < nbb;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms,0,0);
            rete.addInstancePlus(p_Z.getPredicate(), instance);
        }
        //facts for q
        for(int i = nbb; i < 2*nbb;i++){
            Term[] terms = {Constant.getConstant(String.valueOf(i))};
            Instance instance = Instance.getInstance(terms,0,0);
            rete.addInstancePlus(q_Z.getPredicate(), instance);
        }
        //facts for s
        for(int i = 0; i < nbb; i++){
            for(int j = 0; j < nbb; j++){
                Term[] terms = {Constant.getConstant(String.valueOf(i)),Constant.getConstant(String.valueOf(j))};
                Instance instance = Instance.getInstance(terms,0,0);
                rete.addInstancePlus(s.getPredicate(), instance);
            }
        }
        
        
        rete.propagate();

        
        
        //rete.printAnswerSet();
        
    }
}
