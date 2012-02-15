/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testEntity;

import Entity.Predicate;
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
public class testPredicate {
    
    public testPredicate() {
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
    public void testEqualityWithFactoryPattern(){
        Predicate p1 = Predicate.getPredicate("p", 2);
        Predicate p2 = Predicate.getPredicate("p", 2);
        System.out.println("p1.equals(p2): " + p1.equals(p2));
        System.out.println("p1 == p2: " + (p1 == p2));
        assertTrue(p1.equals(p2));
        assertTrue(p1 == p2);
    }
}
