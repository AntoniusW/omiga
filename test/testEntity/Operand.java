/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testEntity;

import Enumeration.OP;
import Entity.Constant;
import Entity.Operator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author g.weidinger
 */
public class Operand {
    
    public Operand() {
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
    public void testOperandPlus(){
        Constant c1 =  Constant.getConstant("4");
        Constant c2 =  Constant.getConstant("3");
        Operator opi1 = new Operator(c1,c2,OP.PLUS);
        assertTrue(opi1.getIntValue() == 7);
    }
    
    @Test
    public void testOperandMinus(){
        Constant c1 =  Constant.getConstant("3");
        Constant c2 =  Constant.getConstant("4");
        Operator opi1 = new Operator(c1,c2,OP.MINUS);
        assertTrue(opi1.getIntValue() == -1);
    }
    
    @Test
    public void testOperandNOTEQUAL(){
        Constant c1 =  Constant.getConstant("4");
        Constant c2 =  Constant.getConstant("3");
        Operator opi1 = new Operator(c1,c2,OP.PLUS);
        Constant c3 =  Constant.getConstant("15");
        Constant c4 =  Constant.getConstant("8");
        Operator opi2 = new Operator(c3,c4,OP.MINUS);
        Operator opi3 = new Operator(opi1,opi2,OP.NOTEQUAL);
        assertTrue(opi3.getIntValue() == 0);
    }
    
    @Test
    public void testOperandBIGGER(){
        Constant c1 =  Constant.getConstant("4");
        Constant c2 =  Constant.getConstant("3");
        Operator opi1 = new Operator(c1,c2,OP.PLUS);
        Constant c3 =  Constant.getConstant("15");
        Constant c4 =  Constant.getConstant("9");
        Operator opi2 = new Operator(c3,c4,OP.MINUS);
        Operator opi3 = new Operator(opi1,opi2,OP.GREATER);
        assertTrue(opi3.getIntValue() == 1);
    }
    
        @Test
    public void testOperandSMALLER(){
        Constant c1 =  Constant.getConstant("4");
        Constant c2 =  Constant.getConstant("3");
        Operator opi1 = new Operator(c1,c2,OP.PLUS);
        Constant c3 =  Constant.getConstant("15");
        Constant c4 =  Constant.getConstant("9");
        Operator opi2 = new Operator(c3,c4,OP.MINUS);
        Operator opi3 = new Operator(opi1,opi2,OP.LESS);
        assertTrue(opi3.getIntValue() == 0);
    }
}
