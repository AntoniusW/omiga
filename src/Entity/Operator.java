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
    
   private int punktrechnung = 0;
   private OP lastPunktOP = null;
   private int summe = 0;
   private Operator actual = null;
   
   private void doIt(){
       if(lastPunktOP == OP.PLUS){
           summe = summe + punktrechnung;
       }else{
           summe = summe - punktrechnung;
       }
       punktrechnung = 0;
   }
   
   public boolean isPunktRechnung(){
       if(this.op.equals(OP.DIVIDE) || this.op.equals(OP.TIMES)) return true;
       return false;
   }

    /**
    * 
    * Please be aware that the operator class was intended to support a treestructure to easily 
    * represent infix/postfix notation. But in favor for easy parsing we use it now just as a
    * chain of Operators, where the leftside of a PLUS/MINUS/DIVIDE/TIMES Operator can only be
    * a constant or a variable, so we can calculate from left to right.
    * 
    * @param ergebnis the value of the previous finsihed calculation
    * @param punktrechnung the value of the actual DIVIDE/TIMES calculation
    * @param lastPunktOP the value before the actual DIVIDE/TIMES calculation
    * @return the integer value of this Operator (0,1 for comparsim operators)
    */
    @Override
    public int getIntValue(int ergebnis, int punktrechnung, OP lastPunktOP) {
        switch(op){
            case PLUS: 
                //System.out.println("ARRIVED: Ergebnis= " + ergebnis + " _ punktrechnung = " + punktrechnung + " _ lastOP = " + lastPunktOP);
                if(lastPunktOP.equals(OP.PLUS)){
                    ergebnis = ergebnis + punktrechnung;
                }else{
                    ergebnis = ergebnis - punktrechnung;
                }
                punktrechnung = 0;
                if(right.getClass().equals(Operator.class)){
                    Operator opi = (Operator) right;
                    if(opi.isPunktRechnung()){
                        return opi.getIntValue(ergebnis, opi.left.getIntValue(0, 0, lastPunktOP), OP.PLUS);
                    }else{
                        ergebnis = ergebnis + opi.left.getIntValue(0, 0, OP.PLUS);
                        return opi.getIntValue(ergebnis, punktrechnung, OP.PLUS);
                    }
                }else{
                    ergebnis = ergebnis + right.getIntValue(ergebnis, punktrechnung, lastPunktOP);
                    return ergebnis;
                }
            case MINUS:
                //System.out.println("ARRIVED: Ergebnis= " + ergebnis + " _ punktrechnung = " + punktrechnung + " _ lastOP = " + lastPunktOP);
                if(lastPunktOP.equals(OP.PLUS)){
                    ergebnis = ergebnis + punktrechnung;
                }else{
                    ergebnis = ergebnis - punktrechnung;
                }
                punktrechnung = 0;
                if(right.getClass().equals(Operator.class)){
                    Operator opi = (Operator) right;
                    if(opi.isPunktRechnung()){
                        return opi.getIntValue(ergebnis, opi.left.getIntValue(0, 0, lastPunktOP), OP.MINUS);
                    }else{
                        ergebnis = ergebnis + opi.left.getIntValue(0, 0, OP.MINUS);
                        return opi.getIntValue(ergebnis, punktrechnung, OP.MINUS);
                    }
                }else{
                    ergebnis = ergebnis - right.getIntValue(ergebnis, punktrechnung, lastPunktOP);
                    return ergebnis;
                }
            case TIMES: 
                if(right.getClass().equals(Operator.class)){
                    Operator opi = (Operator)right;
                    punktrechnung = punktrechnung * opi.left.getIntValue(0, 0, lastPunktOP);
                    return opi.getIntValue(ergebnis, punktrechnung, lastPunktOP);
                }else{
                    if(lastPunktOP.equals(OP.PLUS)){
                        ergebnis = ergebnis + punktrechnung * right.getIntValue(0, 0, lastPunktOP);
                    }else{
                        ergebnis = ergebnis - punktrechnung * right.getIntValue(0, 0, lastPunktOP);
                    }
                    return ergebnis;
                }     
            case DIVIDE: 
                if(right.getClass().equals(Operator.class)){
                    Operator opi = (Operator)right;
                    punktrechnung = punktrechnung / opi.left.getIntValue(0, 0, lastPunktOP);
                    return opi.getIntValue(ergebnis, punktrechnung, lastPunktOP);
                }else{
                    if(lastPunktOP.equals(OP.PLUS)){
                        ergebnis = ergebnis + punktrechnung / right.getIntValue(0, 0, lastPunktOP);
                    }else{
                        ergebnis = ergebnis - punktrechnung / right.getIntValue(0, 0, lastPunktOP);
                    }
                    return ergebnis;
                }     
            case ASSIGN: return this.calc(this.right);
            case EQUAL: 
                if(calc(left) == calc(right)) return 1;
                return 0;
            case NOTEQUAL:
                if(calc(left) != calc(right)) return 1;
                return 0;
            case GREATER:
                if(calc(left) > calc(right)) return 1;
                return 0;
            case LESS:
                if(calc(left) < calc(right)) return 1;
                return 0;
            case GREATER_EQ:
                if(calc(left) >= calc(right)) return 1;
                return 0;
            case LESS_EQ:
                if(calc(left) <= calc(right)) return 1;
                return 0;
            default: return 0;
        }
    }
    
    private int calc(OperandI oper){
        if(oper.getClass().equals(Operator.class)){
            Operator opi = (Operator)oper;
            if(opi.isPunktRechnung()){
                return opi.getIntValue(0, opi.left.getIntValue(0, 0, null), OP.PLUS);
            }else{
                return opi.getIntValue(opi.left.getIntValue(0, 0, null),0, OP.PLUS);
            }
        }else{
            return oper.getIntValue(0, 0, null);
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
        return (left.getIntValue(0,0,null) - right.getIntValue(0,0,null))/(countRight-countLeft);

        
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
