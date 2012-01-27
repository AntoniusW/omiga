/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testStorage;

import Datastructure.storage.HashInstances;
import Entity.Constant;
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
public class testHashInstances {
    
    public testHashInstances() {
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
    public void hello() {
        HashInstances hI = new HashInstances();
        Constant c = Constant.getConstant("a");
        Constant c2 = Constant.getConstant("a");
    }
}
