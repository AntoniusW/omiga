/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.FuncTerm;
import Entity.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author xir
 */
public abstract class Term {
    
    /*
     * A PredAtom is either a constant a variable or a funcTerm.
     * Equals and hashCode Methodes are used for HashMapLookUps in the datastructure
     * Father/Child relation is needed to check if we have to update more schema
     */
    
    private static HashMap<Term,Term> predAtoms = new HashMap<Term,Term>();
    
    public static void addPredAtom(Term pa){
        predAtoms.put(pa, pa);
    }
    
    public static Term getPredAtom(Term pa){
        return predAtoms.get(pa);
    }
    public static boolean containsPredAtom(Term pa){
        return predAtoms.containsKey(pa);
    }
    
    
    protected String name;
    protected int hash;
    protected ArrayList<Term> children;
    
    public Term(String name, ArrayList<Term> children){
        this.name = name;
        if(children != null){
            this.children = new ArrayList<Term>(children);
        }else{
            this.children = new ArrayList<Term>(); // is this neccassary?
        }   
        this.hash = this.toString().hashCode();
    }
    
    public String getName(){
        return name;
    }
    
    public ArrayList<Term> getChildren(){
        return children;
    }
    /*
     * Implemented over the isParentOf method which changes in the generalisations
     */
    /*public boolean isChildOf(Term pa){
        return pa.isParentOf(this);
    }
    
    
    public boolean isParentOf(Term pa){
        //This Code depends on the child class
        return false;
    }*/
    @Override
    public boolean equals(Object o){
        //This Code depends on the child class
        return false;
    }
    @Override
    public int hashCode(){
        return this.hash;
    }
    public boolean isInstanciated(){
        //This Code depends on the child class
        return false;
    }
    
    @Override
    public String toString(){
        return "ABSTRACT CLASS";
    }
    
    public boolean isConstant(){
        return false;
    }
    public boolean isFuncTerm(){
        return false;
    }
    public boolean isVariable(){
        return false;
    }
    
    public ArrayList<Variable> getUsedVariables(){
        return new ArrayList<Variable>();
    }
    
    /*
     * This method is used to define equalaty over atoms, such that p(X,Y) == p(Z,A).
     */
    public boolean equalsType(Term t){
        // must beinplemented by each subclass!
        return false;
    }
    
}
