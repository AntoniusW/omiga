/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.PredAtom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class Constant extends PredAtom {
    
    
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
        if(PredAtom.containsPredAtom(c)){
            return (Constant)PredAtom.getPredAtom(c);
        }else{
            PredAtom.addPredAtom(c);
            return c;
        }
    }
    
    private Constant(String name){
        super(name, null);
    }

    @Override
    public boolean isParentOf(PredAtom pa) {
        if (this.equals(pa)) return true;
        return false;
    }
    
    @Override
    /*
     * A constant only equals a Pred Atom that is a constant with same name, namly itself
     */
    public boolean equals(Object o) {
        if(o.getClass().equals(this.getClass())){
            if(this.name.equals(((PredAtom)o).getName())) return true;
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
        return null;
    }
    
    
    
}
