/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testEntity;

import Entity.Variable;
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
    public void testOperandNOTEQUAL(){
        Constant c1 =  Constant.getConstant("4");
        Constant c2 =  Constant.getConstant("3");
        Operator opi1 = new Operator(c1,c2,OP.PLUS);
        Constant c3 =  Constant.getConstant("15");
        Constant c4 =  Constant.getConstant("8");
        Operator opi2 = new Operator(c3,c4,OP.MINUS);
        Operator opi3 = new Operator(opi1,opi2,OP.NOTEQUAL);
        assertTrue(opi3.getIntValue(0,0,null) == 0);
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
        assertTrue(opi3.getIntValue(0,0,null) == 1);
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
        assertTrue(opi3.getIntValue(0,0,null) == 0);
    }
       
    @Test
    public void testComplexOperand(){
        
        // X = 4 + 3 * 4 / 6 - 10 / 5 + 1
        Constant c1 =  Constant.getConstant("1");
        Constant c5 =  Constant.getConstant("5");
        Constant c10 =  Constant.getConstant("10");
        Constant c4 =  Constant.getConstant("4");
        Constant c3 =  Constant.getConstant("3");
        Constant c6 =  Constant.getConstant("6");
        Operator opi7 = new Operator(c5,c1,OP.PLUS);
        Operator opi6 = new Operator(c10,opi7,OP.DIVIDE);
        Operator opi5 = new Operator(c6,opi6,OP.MINUS);
        Operator opi4 = new Operator(c4,opi5,OP.DIVIDE);
        Operator opi3 = new Operator(c3,opi4,OP.TIMES);
        Operator opi2 = new Operator(c4,opi3,OP.PLUS);
        Operator opi1 = new Operator(Variable.getVariable("X"),opi2,OP.ASSIGN);
        /*System.out.println(opi1);
        System.out.println(opi1.getIntValue(0,0,null));*/
        assertTrue(opi1.getIntValue(0,0,null) == 5);
    }
}
