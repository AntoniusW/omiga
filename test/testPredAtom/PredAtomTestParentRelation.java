/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testPredAtom;

import Entity.FuncTerm;
import java.util.ArrayList;
import Entity.Constant;
import Interfaces.Term;
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
public class PredAtomTestParentRelation {
    
    String s;
    
    
    public PredAtomTestParentRelation() {
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
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    
    /*@Test
    public void TestParentChildRelationForVariables(){
        Term c1 = Constant.getConstant("x");
        Term v1 = Variable.getVariable("Var1");
        Term v2 = Variable.getVariable("Var2");
        ArrayList<Term> children = new ArrayList<Term>();
        children.add(c1);
        Term f1 = FuncTerm.getFuncTerm("f", children);
    
        assertTrue(v1.isParentOf(c1)); // A variable is parent of any constant
        assertFalse(v1.isChildOf(c1)); // A variable is never child of a constant
        assertTrue(v1.isParentOf(f1)); // A variable is always parent of a functerm
        assertFalse(v1.isChildOf(f1)); // A variable is never child of a funcTerm
        assertTrue(v1.isParentOf(v2)); // A variable is always Father of another Variable
        assertTrue(v1.isChildOf(v2));  // A variable is always child of another Variable
        assertTrue(v1.isParentOf(v1)); // A variable is always Father of itself
        assertTrue(v1.isChildOf(v1));  // A variable is always child of itself
        
        
    }
    
    @Test
    public void TestParentChildRelationForConstants(){
        Term c1 = Constant.getConstant("x");
        Term c2 = Constant.getConstant("y");
        Term v1 = Variable.getVariable("Var1");
        ArrayList<Term> children = new ArrayList<Term>();
        children.add(c1);
        Term f1 = FuncTerm.getFuncTerm("f", children);
    
        assertTrue(c1.isParentOf(c1));   // a constant is always parent of itself
        assertTrue(c1.isChildOf(c1));    // a constant is always child of itself
        assertFalse(c1.isParentOf(c2));  // a constant is never parent of another constant
        assertFalse(c1.isChildOf(c2));   // a constant is never child of another constant
        assertFalse(c1.isParentOf(f1));  // a constant is never parent of a functerm
        assertFalse(c1.isChildOf(f1));    // a constant is never child of a functerm
        assertFalse(c1.isParentOf(v1));  // a constant is never parent of a variable
        assertTrue(c1.isChildOf(v1));    // a constant is always child of a variable
        
    }
    
    @Test
    public void TestParentChildRelationForFuncTerms(){
        Term c1 = Constant.getConstant("x");
        Term c2 = Constant.getConstant("y");
        Term v1 = Variable.getVariable("Var1");
        ArrayList<Term> children = new ArrayList<Term>();
        children.add(c1);
        Term f1 = FuncTerm.getFuncTerm("f1", children);
        System.out.println(f1);
        ArrayList<Term> children2 = new ArrayList<Term>();
        children2.add(c2);
        Term f2 = FuncTerm.getFuncTerm("f2", children2);
        ArrayList<Term> children3 = new ArrayList<Term>();
        children3.add(v1);
        Term f3 = FuncTerm.getFuncTerm("f1", children3);
        Term f4 = FuncTerm.getFuncTerm("f4", children3);
        System.out.println("_______________");
        System.out.println(f1);
        System.out.println(f2);
        System.out.println(f3);
    
        assertFalse(f1.isParentOf(c1));   // a functerm is never parent of a constant
        assertFalse(f1.isChildOf(c1));    // a functerm is never child of a constant
        assertTrue(f1.isParentOf(f1));  // a functerm is always parent of itself
        assertTrue(f1.isChildOf(f1));    // a functerm is always child of itself
        assertFalse(f1.isParentOf(f2));  // a functerm is not parent of another functerm which children are not parent
        assertFalse(f1.isChildOf(f2));    // a functerm is not child of another functerm which children are not child
        assertTrue(f1.isChildOf(f3));    // a functerm is child of another functerm which's children are parent of the first one's children iff the name of both functerms is equal
        assertFalse(f1.isChildOf(f4));    // a functerm is not child of another functerm which's children are parent of the first one's children iff the name of both functerms is NOT equal
        assertFalse(f3.isChildOf(f1));    // but not vica verca
        assertTrue(f3.isParentOf(f1));      // a functerm is parent of another functerm if it's children are parent of the other functerms children
        assertFalse(f1.isParentOf(v1));  // a functerm is never parent of a variable
        assertTrue(f1.isChildOf(v1));   // a functerm is always child of a variable
        
    }*/
    
    @Test
    public void TestingTheFunctermConstructer(){
        Term c1 = Constant.getConstant("c1");
        Term v1 = Variable.getVariable("v1");
        ArrayList<Term> children = new ArrayList<Term>();
        children.add(c1);
        FuncTerm f1 = FuncTerm.getFuncTerm("f1", children);
        FuncTerm f2 = FuncTerm.getFuncTerm("f2", children);
        children.add(v1);
        children.add(f2);
        FuncTerm f3 = FuncTerm.getFuncTerm("f3", children);
        FuncTerm f4 = FuncTerm.getFuncTerm("f1", children);
        FuncTerm f5 = FuncTerm.getFuncTerm("f1", children);
        
        
        /*System.out.println("F1 = " + f1);
        System.out.println(f1.getChildren().get(0));
        System.out.println(c1.toString());
        System.out.println("F2 = " + f2);
        System.out.println("F3 = " + f3);
        System.out.println("F4 = " + f4);*/
        
        assertTrue(f1.getChildren().size() == 1);
        assertTrue(f2.getChildren().size() == 1);
        assertTrue(f3.getChildren().size() == 3);
        assertTrue(f1.getName().equals("f1"));
        assertTrue(f2.getName().equals("f2"));
        assertTrue(f3.getName().equals("f3"));
        assertTrue(f3.equals(f3));
        assertFalse(f4.equals(f1));
        assertFalse(f4.equals(f3));
        assertTrue(f4.equals(f5));
        assertTrue(f4 == f5);

    }
    
    @Test
    public void TestEqualityAMontPredAtoms(){
        Term c1 = Constant.getConstant("x");
        Term c2 = Constant.getConstant("x");
        Term c3 = Constant.getConstant("y");
        Term v1 = Variable.getVariable("Var1");
        Term v2 = Variable.getVariable("Var1");
        Term v3 = Variable.getVariable("Var2");
        ArrayList<Term> children = new ArrayList<Term>();
        children.add(c1);
        Term f1 = FuncTerm.getFuncTerm("f", children);
        Term f2 = FuncTerm.getFuncTerm("f", children);
        Term f3 = FuncTerm.getFuncTerm("f3", children);
        children.add(f1);
        Term f4 = FuncTerm.getFuncTerm("f", children);
        
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertFalse(c1.equals(c3));
        
        assertFalse(c1.equals(v1));
        assertFalse(c1.equals(f1));
        assertFalse(v1.equals(c1));
        assertFalse(v1.equals(v3));
        assertTrue(v1.equals(v1));
        assertTrue(v1.equals(v2));
        
        assertTrue(f1.equals(f2));
        assertTrue(f2.equals(f1));
        assertFalse(f1.equals(f3));
        assertFalse(f1.equals(f4));
        assertFalse(f2.equals(f3));
        assertFalse(f2.equals(f4));
        assertTrue(f1 == f2);
        assertTrue(c1 == c2);
        assertTrue(v1 == v2);
    }
    
    
    
}
