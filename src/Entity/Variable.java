/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.Operand;
import Interfaces.PredAtom;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author xir
 */
public class Variable extends PredAtom implements Operand{
    
    
    private PredAtom value;
    
    

    public static Variable getVariable(String name){
        Variable v = new Variable(name);
        if(PredAtom.containsPredAtom(v)){
            return (Variable)PredAtom.getPredAtom(v);
        }else{
            PredAtom.addPredAtom(v);
            return v;
        }
    }
    
    private Variable(String name){
        super(name, null);
        value = null;
    }

    @Override
    public boolean isParentOf(PredAtom pa) {
        return true;
    }
    
    @Override
    /*
     * A Variable only euqal another PredAtom of type Variable iff both names are equal
     */
    public boolean equals(Object o) {
        //TODO: Is it faster to compare o.getClass().equals(Variable)?
        if(o.getClass().equals(this.getClass())){
            if(this.name.equals(((PredAtom)o).getName())) return true;
        }
        return false;
    }

    @Override
    /*
     * A Variable is not instanciated.
     * Please be aware that this is in respect to the comparism of mapping instances and schemata
     * We do not take the value of the variable into account
     */
    public boolean isInstanciated() {
        return false;
    }
    
    @Override
    public String toString(){
        return this.name;
    }

    @Override
    public Integer getOperandValue(ArrayList<Variable> vars) {
        if (vars.contains(this)) return Integer.parseInt(this.value.getName());
        return null;
    }

    @Override
    public boolean isInstanciated(ArrayList<Variable> vars) {
        if(vars.contains(this)) return true;
        return false;
    }
    
    @Override
    public boolean isConstant(){
        return false;
    }
    @Override
    public boolean isFuncTerm(){
        return false;
    }
    @Override
    public boolean isVariable(){
        return true;
    }
    @Override
    public ArrayList<Variable> getUsedVariables(){
        ArrayList<Variable> ret = new ArrayList<Variable>();
        ret.add(this);
        return ret;
    }
    
    public PredAtom getValue(){
        return value;
    }
    public void setValue(PredAtom pa){
        this.value=pa;
    }
    
}
