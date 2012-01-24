/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRete;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.Rete;
import Interfaces.Term;
import java.util.ArrayList;
import Entity.Constant;
import Entity.FuncTerm;
import Entity.Atom;
import Entity.Predicate;
import Entity.Variable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class SelectionNodes {
    
    Variable X = Variable.getVariable("X");
    Variable Y = Variable.getVariable("Y");
    Variable Z = Variable.getVariable("Z");
    Constant a = Constant.getConstant("a");
    Constant b = Constant.getConstant("b");
    Constant c = Constant.getConstant("c");
    
    FuncTerm f1,f2,f3;
    
    public SelectionNodes() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        ArrayList<Term> children1 = new ArrayList<Term>();
        children1.add(X);
        f1 = FuncTerm.getFuncTerm("f1", children1);
        ArrayList<Term> children2 = new ArrayList<Term>();
        children2.add(a);
        f2 = FuncTerm.getFuncTerm("f2", children2);
        ArrayList<Term> children3 = new ArrayList<Term>();
        children3.add(f2);
        children3.add(b);
        f3 = FuncTerm.getFuncTerm("f3", children3);
        
        
    }
    
    @After
    public void tearDown() {
    }
    
    
    
    @Test
    public void testSelectionNodeAddInstance(){
        Predicate p = Predicate.getPredicate("p",3);
        Atom pir = new Atom("p", 3);
        pir.setAtomAt(0, X);
        pir.setAtomAt(1, Y);
        pir.setAtomAt(2, Z);
        
        Term[] instance1 = {a,a,a};
        Term[] instance2 = {a,a,b};
        Term[] instance3 = {a,a,c};
        Term[] instance4 = {a,b,a};
        Term[] instance5 = {a,b,b};
        Term[] instance6 = {a,b,c};
        Term[] instance7 = {a,c,a};
        Term[] instance8 = {a,c,b};
        Term[] instance9 = {a,c,c};
        
        Rete r = new Rete();
        r.addAtomPlus(pir);
        
        r.addInstancePlus(p, instance1);
        r.addInstancePlus(p, instance2);
        r.addInstancePlus(p, instance3);
        r.addInstancePlus(p, instance4);
        r.addInstancePlus(p, instance5);
        r.addInstancePlus(p, instance6);
        r.addInstancePlus(p, instance7);
        r.addInstancePlus(p, instance8);
        r.addInstancePlus(p, instance9);
        
        
        BasicNode bn = r.getBasicNodePlus(p);
        Term[] selectionCriterion = {X,Y,Z};
        System.out.println("CHILDREN: " + bn.getChildren());
        System.out.println("CHILDREN SIZE: " + bn.getChildren().size());
        System.out.println("SELECTED PIR: " + bn.getChildren().get(pir));
        System.out.println("SELECTED PIR Memory: " + bn.getChildren().get(pir).testMethod_getMemory());
        System.out.println("SELECTED: " + bn.getChildren().get(pir).testMethod_getMemory().select(selectionCriterion));
        System.out.println("SELECTED SIZE: " + bn.getChildren().get(pir).testMethod_getMemory().select(selectionCriterion).size());
        assertTrue(bn.getChildren().get(pir).testMethod_getMemory().select(selectionCriterion).size() == 9);
        
        bn.getChildren().get(pir).testMethod_getMemory().printAllInstances();
    }
}
