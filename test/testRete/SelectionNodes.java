/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRete;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.Rete;
import Interfaces.PredAtom;
import java.util.ArrayList;
import Entity.Constant;
import Entity.FuncTerm;
import Entity.PredInRule;
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
        ArrayList<PredAtom> children1 = new ArrayList<PredAtom>();
        children1.add(X);
        f1 = FuncTerm.getFuncTerm("f1", children1);
        ArrayList<PredAtom> children2 = new ArrayList<PredAtom>();
        children2.add(a);
        f2 = FuncTerm.getFuncTerm("f2", children2);
        ArrayList<PredAtom> children3 = new ArrayList<PredAtom>();
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
        PredInRule pir = new PredInRule("p", 3);
        pir.setAtomAt(0, X);
        pir.setAtomAt(1, Y);
        pir.setAtomAt(2, Z);
        
        PredAtom[] instance1 = {a,a,a};
        PredAtom[] instance2 = {a,a,b};
        PredAtom[] instance3 = {a,a,c};
        PredAtom[] instance4 = {a,b,a};
        PredAtom[] instance5 = {a,b,b};
        PredAtom[] instance6 = {a,b,c};
        PredAtom[] instance7 = {a,c,a};
        PredAtom[] instance8 = {a,c,b};
        PredAtom[] instance9 = {a,c,c};
        
        Rete r = new Rete();
        r.addPredInRule(pir);
        
        r.addInstance(p, instance1);
        r.addInstance(p, instance2);
        r.addInstance(p, instance3);
        r.addInstance(p, instance4);
        r.addInstance(p, instance5);
        r.addInstance(p, instance6);
        r.addInstance(p, instance7);
        r.addInstance(p, instance8);
        r.addInstance(p, instance9);
        
        
        BasicNode bn = r.getBasicNode(p);
        PredAtom[] selectionCriterion = {X,Y,Z};
        System.out.println("CHILDREN: " + bn.getChildren());
        System.out.println("CHILDREN SIZE: " + bn.getChildren().size());
        System.out.println("SELECTED: " + bn.getChildren().get(0).getMemory().select(selectionCriterion));
        System.out.println("SELECTED SIZE: " + bn.getChildren().get(0).getMemory().select(selectionCriterion).size());
        assertTrue(bn.getChildren().get(0).getMemory().select(selectionCriterion).size() == 9);
    }
}
