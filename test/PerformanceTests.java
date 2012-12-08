/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Interfaces.Term;
import Entity.Constant;
import Entity.Instance;
import java.util.ArrayList;
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
public class PerformanceTests {
    
    public PerformanceTests() {
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
    public void testPerformanceOfCLassCreationOfInstances(){
        double x = System.currentTimeMillis();
        ArrayList<Instance> aL = new ArrayList<Instance>();
        int nbb = 5196;
        for (int i = 0; i < nbb; i++){
            for(int j = 0; j < nbb; j++){
                Term[] term = {Constant.getConstant(String.valueOf(i)),Constant.getConstant(String.valueOf(j))};
                aL.add(Instance.getInstance(term,0));
            }
           
        }
        System.out.println((System.currentTimeMillis()-x)/1000);
        System.out.println("AL-SIZE: " + aL.size());
    }
}
