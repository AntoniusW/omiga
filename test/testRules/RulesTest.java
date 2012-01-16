/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRules;

import Entity.Operator;
import Interfaces.Operand;
import Interfaces.BodyAtom;
import java.util.ArrayList;
import Interfaces.PredAtom;
import Entity.PredInRule;
import Entity.Rule;
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
public class RulesTest {
    
    public RulesTest() {
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
    
    @Test
    /*
     * This is how you generate a rule the hard way
     */
    public void TestRuleCreation(){
        Variable x = Variable.getVariable("X");
        Variable y = Variable.getVariable("Y");
        PredAtom[] patoms = new PredAtom[2];
        patoms[0] = x;
        patoms[1] = y;
        PredInRule head = new PredInRule("p",2,patoms);
        PredAtom[] patoms1 = new PredAtom[1];
        patoms1[0] = x;
        PredInRule q = new PredInRule("q",1,patoms1);
        PredAtom[] patoms2 = new PredAtom[1];
        patoms2[0] = y;
        PredInRule r = new PredInRule("r",1,patoms2);
        ArrayList<PredInRule> aL = new ArrayList<PredInRule>();
        aL.add(q);
        aL.add(r);
        
        Rule r1 = new Rule(head,aL,new ArrayList<PredInRule>(), new ArrayList<Operator>());
        
        //System.out.println(r1);
        
        assertFalse(r1.isConstraint());
        assertTrue(r1.getBodyPlus().size() == 2);
        assertTrue(r1.getHead().equals(head));
        
    }
    
    @Test
    /*
     * This is how you generate a rule more easily
     */
    public void TestRuleCreationNew(){
        Rule r1 = new Rule();
        PredInRule head = new PredInRule("p",2);
        head.setAtomAt(0, Variable.getVariable("X"));
        head.setAtomAt(1, Variable.getVariable("Y"));
        r1.setHead(head);
        PredInRule q = new PredInRule("q",1);
        q.setAtomAt(0, Variable.getVariable("X"));
        PredInRule r = new PredInRule("r",1);
        r.setAtomAt(0, Variable.getVariable("Y"));
        r1.addAtomPlus(q);
        r1.addAtomPlus(r);
        
        assertFalse(r1.isConstraint());
        assertTrue(r1.getBodyPlus().size() == 2);
        assertTrue(r1.getHead().equals(head));
    }
}
