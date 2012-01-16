/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRules;

import Interfaces.PredAtom;
import Entity.Variable;
import Entity.PredInRule;
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
public class PredInRuleTest {
    
    public PredInRuleTest() {
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
    public void Test(){
        PredAtom[] atoms = {Variable.getVariable("X"),Variable.getVariable("Y")} ;
        PredInRule q = new PredInRule("q",2,atoms);
        PredInRule p = new PredInRule("q",2);
        p.setAtomAt(0, Variable.getVariable("X"));
        p.setAtomAt(1, Variable.getVariable("Y"));
        System.out.println(q.getVariables());
        assertTrue(q.getVariables().size() == 2);
        System.out.println(p.getVariables());
        assertTrue(p.getVariables().size() == 2);
    }
}
