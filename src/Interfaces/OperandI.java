/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.Variable;
import Enumeration.OP;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author xir
 * 
 * An Operand is either a Variable or an Operator
 */
public interface OperandI {
    
    public int getIntValue();
    public ArrayList<Variable> getUsedVariables();
    
}
