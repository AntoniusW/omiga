/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class Constant extends Term {
    
    
        /*
     * This method shall only be used when parsing a context. not within the calculation
     * as classinstances are created.
     * 
     * if a constant of that name already exists we return that constant instance, otherwise we
     * save the constant and return it. If it's not saved garbage collection will delete
     * it when nessasary.
     */
    public static Constant getConstant(String name){
        Constant c = new Constant(name);
        if(Term.containsPredAtom(c)){
            return (Constant)Term.getPredAtom(c);
        }else{
            Term.addPredAtom(c);
            return c;
        }
    }
    
    private Constant(String name){
        super(name, null);
    }

    /*@Override
    public boolean isParentOf(Term pa) {
        if (this.equals(pa)) return true;
        return false;
    }*/
    
    @Override
    /*
     * A constant only equals a Pred Atom that is a constant with same name, namly itself
     */
    public boolean equals(Object o) {
        if(o.getClass().equals(this.getClass())){
            if(this.name.equals(((Term)o).getName())) return true;
        }
        return false;
    }

    @Override
    /*
     * A constant is always instanciated
     */
    public boolean isInstanciated() {
        return true;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
    @Override
    public boolean isConstant(){
        return true;
    }
    @Override
    public boolean isFuncTerm(){
        return false;
    }
    @Override
    public boolean isVariable(){
        return false;
    }
    
    @Override
    public ArrayList<Variable> getUsedVariables(){
        //TODO: new ArrayList is bad!
        return new ArrayList<Variable>();
    }
    
    @Override
    public boolean equalsType(Term t){
        if(this.getClass() != t.getClass()){
            Constant that = (Constant) t;
            return this.name.equals(that.name);
        }else{
            return false;
        }
    }
    
    
    
}
