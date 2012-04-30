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
    
    public OperandI getLeft(){
        return left;
    }
    
    public OperandI getRight(){
        return right;
    }
    
    public OP getOP(){
        return op;
    }
    
   
    
    @Override
    public int getIntValue(Integer i) {
        switch(op){
            case PLUS: //
                if(i == null) {
                    return left.getIntValue(null) + right.getIntValue(null);
                }else{
                    return i + right.getIntValue(null);
                }
            case MINUS:
                if(i == null) {
                    return left.getIntValue(null) - right.getIntValue(null);
                }else{
                    return i - right.getIntValue(null);
                }
            case TIMES: 
                if(right.getClass().equals(Operator.class)){
                    Operator oper = (Operator)right;
                    if(i == null){
                        int ret = left.getIntValue(null) * oper.getLeft().getIntValue(i);
                        return oper.getIntValue(ret);
                    }else{
                        int ret = i * oper.getLeft().getIntValue(null);
                        return oper.getIntValue(ret);
                    }
                }else{
                    if(i == null){
                        return left.getIntValue(null) * right.getIntValue(null);
                    }else{
                        return i * right.getIntValue(null);
                    }
                }
            case DIVIDE: 
                if(right.getClass().equals(Operator.class)){
                    Operator oper = (Operator)right;
                    if(i == null){
                        int ret = left.getIntValue(null) / oper.getLeft().getIntValue(i);
                        return oper.getIntValue(ret);
                    }else{
                        int ret = i / oper.getLeft().getIntValue(null);
                        return oper.getIntValue(ret);
                    }
                }else{
                    if(i == null){
                        return left.getIntValue(null) / right.getIntValue(null);
                    }else{
                        return i / right.getIntValue(null);
                    }
                }
            case ASSIGN: return this.right.getIntValue(null);
            case EQUAL: 
                if(left.getIntValue(null) == right.getIntValue(null)) return 1;
                return 0;
            case NOTEQUAL:
                if(left.getIntValue(null) != right.getIntValue(null)) return 1;
                return 0;
            case GREATER:
                if(left.getIntValue(null) > right.getIntValue(null)) return 1;
                return 0;
            case LESS:
                if(left.getIntValue(null) < right.getIntValue(null)) return 1;
                return 0;
            case GREATER_EQ:
                if(left.getIntValue(null) >= right.getIntValue(null)) return 1;
                return 0;
            case LESS_EQ:
                if(left.getIntValue(null) <= right.getIntValue(null)) return 1;
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
