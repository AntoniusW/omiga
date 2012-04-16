/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Enumeration.OP;
import Interfaces.OperandI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * An Operator represents a mathematical operator in order to connect Variables. Only works for integerConstants as variable values. f.e.: X + Y = Z
 */
public class Operator implements OperandI{
    
    private static Constant zero = Constant.getConstant("0");
    
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

    @Override
    public ArrayList<Variable> getUsedVariables() {
        HashSet<Variable> vars = new HashSet<Variable>();
        vars.addAll(this.left.getUsedVariables());
        vars.addAll(this.right.getUsedVariables());
        ArrayList<Variable> ret = new ArrayList<Variable>(vars);
        return ret;
    }
    
    @Override
    public String toString(){
        return left.toString() + " " + op.toString() + " " + right.toString();
    }
    
    public int calculate(Variable v){
        int countLeft = 0;
        int countRight = 0;
        for(Variable v1: this.left.getUsedVariables()){
            if(v.equals(v1)) countLeft++;
        }
        for(Variable v1: this.right.getUsedVariables()){
            if(v.equals(v1)) countLeft++;
        }
        //replace all Variable oocurences of v by 0 then replace this op by MINUS and Divide by countRight-countLeft and return
        v.setValue(zero);
        return (left.getIntValue() - right.getIntValue())/(countRight-countLeft);

        
        //return new Operator(null,null, OP.BIGGER);
    }
    
    public boolean isInstanciated(Collection<Variable> vars){
        for(Variable v: this.getUsedVariables()){
            if(!vars.contains(v)) return false;
        }
        return true;
    }
    
   public boolean isInstanciatedButOne(Collection<Variable> vars){
       int i = 0;
        for(Variable v: this.getUsedVariables()){
            if(!vars.contains(v)) {
                if(i > 0) return false;
                i++;
            }
        }
        return true;
    }
    
    
    
}
