/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.Variable;
import java.util.ArrayList;

/**
 *
 * @author xir
 * 
 * An Operand is either a Variable or an Operator
 */
public interface Operand {
    
    public Integer getOperandValue(ArrayList<Variable> vars);
    public boolean isInstanciated(ArrayList<Variable> vars);
    
}
