/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testStorage;

import Interfaces.PredAtom;
import Datastructures.storage.Storage;
import Entity.Constant;
import Entity.Instance;
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
public class testStorage {
    
    Storage memory1 = new Storage(1);
    Storage memory2 = new Storage(2);
    Storage memory3 = new Storage(2);
    
    public testStorage() {
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
    public void TestAddMethod1(){
        PredAtom[] instance1 = {Constant.getConstant("a")};
        PredAtom[] instance2 = {Constant.getConstant("a")};
        PredAtom[] instance3 = {Constant.getConstant("b")};
        PredAtom[] instance4 = {Constant.getConstant("c")};

        memory1.addInstance(instance1);
        memory1.addInstance(instance2);
        memory1.addInstance(instance3);
        memory1.addInstance(instance4);
        
        assertTrue(memory1.containsInstance(instance1));
        assertTrue(memory1.containsInstance(instance2));
        assertTrue(memory1.containsInstance(instance3));
        assertTrue(memory1.containsInstance(instance4));
        
        PredAtom[] selCrit1 = {Constant.getConstant("a")};
        assertTrue(memory1.select(selCrit1).size() == 1);
        PredAtom[] selCrit2 = {Constant.getConstant("b")};
        assertTrue(memory1.select(selCrit2).size() == 1);
        PredAtom[] selCrit3 = {Constant.getConstant("c")};
        assertTrue(memory1.select(selCrit3).size() == 1);
        
    }
    
    @Test
    public void TestAddMethod2(){
        PredAtom[] instance1 = {Constant.getConstant("a"),Constant.getConstant("a")};
        PredAtom[] instance2 = {Constant.getConstant("a"),Constant.getConstant("b")};
        PredAtom[] instance3 = {Constant.getConstant("a"),Constant.getConstant("c")};
        PredAtom[] instance4 = {Constant.getConstant("b"),Constant.getConstant("c")};
        PredAtom[] instance5 = {Constant.getConstant("a"),Constant.getConstant("a")};

        memory2.addInstance(instance1);
        memory2.addInstance(instance2);
        memory2.addInstance(instance3);
        memory2.addInstance(instance4);
        memory2.addInstance(instance5);
        
        assertTrue(memory2.containsInstance(instance1));
        assertTrue(memory2.containsInstance(instance2));
        assertTrue(memory2.containsInstance(instance3));
        assertTrue(memory2.containsInstance(instance4));
        assertTrue(memory2.containsInstance(instance5));
        

        PredAtom[] selCrit1 = {Constant.getConstant("a"),Constant.getConstant("a")};
        assertTrue(memory2.select(selCrit1).size() == 1);
        PredAtom[] selCrit2 = {Constant.getConstant("a"),Constant.getConstant("b")};
        assertTrue(memory2.select(selCrit2).size() == 1);
        PredAtom[] selCrit3 = {Constant.getConstant("a"),Constant.getConstant("c")};
        assertTrue(memory2.select(selCrit3).size() == 1);
        PredAtom[] selCrit4 = {Constant.getConstant("b"),Constant.getConstant("c")};
        assertTrue(memory2.select(selCrit4).size() == 1);
        
        PredAtom[] selVarCrit1 = {Variable.getVariable("X"),Constant.getConstant("a")};
        assertTrue(memory2.select(selVarCrit1).size() == 1);
        PredAtom[] selVarCrit2 = {Variable.getVariable("X"),Constant.getConstant("b")};
        assertTrue(memory2.select(selVarCrit2).size() == 1);
        PredAtom[] selVarCrit3 = {Variable.getVariable("X"),Constant.getConstant("c")};
        assertTrue(memory2.select(selVarCrit3).size() == 2);
        
        PredAtom[] selVarCrit4 = {Constant.getConstant("a"),Variable.getVariable("X")};
        assertTrue(memory2.select(selVarCrit4).size() == 3);
        PredAtom[] selVarCrit5 = {Constant.getConstant("b"),Variable.getVariable("Y")};
        assertTrue(memory2.select(selVarCrit5).size() == 1);
        PredAtom[] selVarCrit6 = {Constant.getConstant("c"),Variable.getVariable("X")};
        assertTrue(memory2.select(selVarCrit6).size() == 0);
        
        PredAtom[] selVarCrit7 = {Variable.getVariable("X"), Variable.getVariable("X")};
        assertTrue(memory2.select(selVarCrit7).size() == 4);
        PredAtom[] selVarCrit8 = {Variable.getVariable("X"), Variable.getVariable("Y")};
        assertTrue(memory2.select(selVarCrit8).size() == 4);
        
    }
}
