/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.OperandI;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A Variable represents a logical variabele. It can take any constant or FuncTerm as value.
 * 
 * @param value the value this variable actually has. This is used within rete selection nodes.
 * @param vars a List of Variables, used to ensure uniqueness of Variables
 * 
 * 
 */
public class Variable extends Term implements OperandI{
    
    private Term value;
    
    //private static ArrayList<Variable> vars = new ArrayList<Variable>();
    private static HashMap<String,Variable> vars = new HashMap<String,Variable>();
    
    
    /**
     * if you want to generate a Variable use this method, since the constructor is prvate, in order
     * to ensure uniqueness of Variables.
     * 
     * @param name the name of the Variable
     * @return  the desired Variable
     */
    public static Variable getVariable(String name){
        if(vars.containsKey(name)){
            return vars.get(name);
        }else{
            Variable v = new Variable(name);
            vars.put(name, v);
            return v;
        }
        /*for(Variable var: vars){
            if(var.getName().equals(name)) return var;
        }
        Variable v = new Variable(name);
        vars.add(v);
        return v;*/
    }
    
    /**
     * private constructor. In order to obtain Variables use the static getVariable method.
     * Generates a new Variable and sets it's name, usedVariables, as well as calculating it's hashValue.
     * 
     * @param name 
     */
    private Variable(String name){
        super(name);
        value = null;
        usedVariables.add(this);
    }
    
    @Override
    /**
     * Since this is a variable, and for variables we implement the factory pattern, and since
     * a variable never equals a functerm or a constant, we can just use the == operator to
     * determine equality.
     * 
     * @param o The object we want this variable to compare with
     * @return wether o equals this object
     */
    public boolean equals(Object o) {
        return (this == o);
    }
    
    /**
     * 
     * @return the string representation of this variable
     */
    @Override
    public String toString(){
        return this.name;
    }
    
    /**
     * This method is needed within the rete network's selection nodes. There we assign values to variables, til we can build a hole variable assignment
     * for the corresponding Atom. This is actually not very pritty but it's faster than having a temp value storage within our selection nodes.
     * 
     * @return the value of this variable
     */
    public Term getValue(){
        return value;
    }
    
    /**
     * 
     * Works only for constants as values!
     * 
     * @return The integer representation of this variables value.
     */
    @Override
    public int getIntValue(){
        return ((Constant)this.value).getIntValue();
    }
    /**
     * This method is needed within the rete network's selection nodes. There we assign values to variables, til we can build a hole variable assignment
     * for the corresponding Atom. This is actually not very pritty but it's faster than having a temp value storage within our selection nodes.
     * 
     * @param the term you want to assign to this variable
     */
    public void setValue(Term pa){
        this.value=pa;
    }
    
    @Override
    public boolean fatherOf(Term t){
        return true;
    }
    
}
