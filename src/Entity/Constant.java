/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.OperandI;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A Constant represents a logical constant.
 * 
 * @param constants a static List of Constants, used to ensure uniqueness of Constants
 */
public class Constant extends Term implements OperandI{
    
    private Integer intValue;
    
    //private static ArrayList<Constant> constants = new ArrayList<Constant>();
    private static HashMap<String,Constant> constants = new HashMap<String,Constant>();
    
    /**
     * 
     * if you want to generate a Constant use this method, since the constructor is private, in order
     * to guarantee uniqueness of Constants.
     * 
     * @param name the name of the Constant you ant to create
     * @return the desired constant of name=name
     */
    public static Constant getConstant(String name){
        if(constants.containsKey(name)){
            return constants.get(name);
        }else{
            Constant c = new Constant(name);
            constants.put(name,c);
            return c;
        }
    }
    
    /**
     * private constructor. if you want to generate a constant, use the static getConstant method.
     * Creates a new Constant with given name. Initializes usedVariables, and hashValue;
     * 
     * @param name The name of the Constant you want to create
     */
    private Constant(String name){
        super(name);
        //System.out.println("CREATED CONSTANT: " + name);
        try{
            this.intValue = Integer.parseInt(name);
        }catch(Exception e){
            this.intValue = null;
        }
    }
    
    @Override
    /**
     * Since this is a constant, and for constants we implement the factory pattern, and since
     * a variable never equals a functerm or a variable, we can just use the == operator to
     * determine equality.
     * 
     * @param o The object we want this variable to compare with
     * @return wether o equals this object
     */
    public boolean equals(Object o) {
        return (this == o);
        /*if(o.getClass().equals(this.getClass())){
            if(this.name.equals(((Term)o).getName())) return true;
        }
        return false;*/
    }


    
    @Override
    /**
     * @return the String representation of this Constant
     */
    public String toString(){
        return this.name;
    }
    
    //TODO: Check if this is correct for numbers!
    @Override
    public int getIntValue(){
        return this.intValue;
    }
    
}
