/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Enumeration.OP;
import Interfaces.OperandI;
import java.util.ArrayList;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * An Operator represents a mathematical operator in order to connect Variables. Only works for integerConstants as variable values. f.e.: X + Y = Z
 */
public class Operator implements OperandI{
    
    OperandI left;
    OperandI right;
    OP op;

    public Operator(OperandI left, OperandI right, OP op){
        this.left = left;
        this.right = right;
        this.op = op;
    }
    
    @Override
    public int getIntValue() {
        switch(op){
            case PLUS: return left.getIntValue() + right.getIntValue();
            case MINUS: return left.getIntValue() - right.getIntValue();
            case EQUAL: 
                if(left.getIntValue() == right.getIntValue()) return 1;
                return 0;
            case NOTEQUAL:
                if(left.getIntValue() != right.getIntValue()) return 1;
                return 0;
            case BIGGER:
                if(left.getIntValue() > right.getIntValue()) return 1;
                return 0;
            case SMALLER:
                if(left.getIntValue() < right.getIntValue()) return 1;
                return 0;
            default: return 0;
        }
    }
    
    
    
}
