/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testMCS;

import Datastructure.Rewriting.Rewriter_easyMCS;
import Entity.ContextASPMCSRewriting;
import java.util.logging.Logger;
import java.util.logging.Level;
import Exceptions.RuleNotSafeException;
import Exceptions.FactSizeException;
import Manager.Manager;
import Datastructure.Rewriting.Rewriter_easy;
import Entity.ContextASP;
import Entity.Predicate;
import parser.ParserMCSRewrite;
import java.io.File;
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
public class testMCS {
    
    public testMCS() {
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
        File input = new File("MCS1.txt");
        ParserMCSRewrite pars = new ParserMCSRewrite();
        try {
            ContextASP c = pars.readContext(input);
            Rewriter_easyMCS rewriter = new Rewriter_easyMCS();
             //c = rewriter.rewrite(c);
            ((ContextASPMCSRewriting) c).registerFactFromOutside(Predicate.getPredicate("s", 1));
            c.printContext();
            
            Manager m = new Manager(c);
            System.out.println("Starting calculation: " + System.currentTimeMillis());
            m.calculate(null, true, null);
            System.out.println("Program finished: " + System.currentTimeMillis());
            
        } catch (FactSizeException ex) {
           ex.printStackTrace();
        } catch (RuleNotSafeException ex) {
           ex.printStackTrace();
        }
    
    }
}
