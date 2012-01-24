/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testRules;

import Entity.Operator;
import Interfaces.Operand;
import Interfaces.BodyAtom;
import java.util.ArrayList;
import Interfaces.Term;
import Entity.Atom;
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
        Term[] patoms = new Term[2];
        patoms[0] = x;
        patoms[1] = y;
        Atom head = new Atom("p",2,patoms);
        Term[] patoms1 = new Term[1];
        patoms1[0] = x;
        Atom q = new Atom("q",1,patoms1);
        Term[] patoms2 = new Term[1];
        patoms2[0] = y;
        Atom r = new Atom("r",1,patoms2);
        ArrayList<Atom> aL = new ArrayList<Atom>();
        aL.add(q);
        aL.add(r);
        
        Rule r1 = new Rule(head,aL,new ArrayList<Atom>(), new ArrayList<Operator>());
        
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
        Atom head = new Atom("p",2);
        head.setAtomAt(0, Variable.getVariable("X"));
        head.setAtomAt(1, Variable.getVariable("Y"));
        r1.setHead(head);
        Atom q = new Atom("q",1);
        q.setAtomAt(0, Variable.getVariable("X"));
        Atom r = new Atom("r",1);
        r.setAtomAt(0, Variable.getVariable("Y"));
        r1.addAtomPlus(q);
        r1.addAtomPlus(r);
        
        assertFalse(r1.isConstraint());
        assertTrue(r1.getBodyPlus().size() == 2);
        assertTrue(r1.getHead().equals(head));
    }
}
