/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author xir
 */
public abstract class PredAtom {
    
    /*
     * A PredAtom is either a constant a variable or a funcTerm.
     * Equals and hashCode Methodes are used for HashMapLookUps in the datastructure
     * Father/Child relation is needed to check if we have to update more schema
     */
    
    private static HashMap<PredAtom,PredAtom> predAtoms = new HashMap<PredAtom,PredAtom>();
    
    public static void addPredAtom(PredAtom pa){
        predAtoms.put(pa, pa);
    }
    
    public static PredAtom getPredAtom(PredAtom pa){
        return predAtoms.get(pa);
    }
    public static boolean containsPredAtom(PredAtom pa){
        return predAtoms.containsKey(pa);
    }
    
    
    protected String name;
    protected int hash;
    protected ArrayList<PredAtom> children;
    
    public PredAtom(String name, ArrayList<PredAtom> children){
        this.name = name;
        if(children != null){
            this.children = new ArrayList<PredAtom>(children);
        }else{
            this.children = new ArrayList<PredAtom>(); // is this neccassary?
        }   
        this.hash = this.toString().hashCode();
    }
    
    public String getName(){
        return name;
    }
    
    public ArrayList<PredAtom> getChildren(){
        return children;
    }
    /*
     * Implemented over the isParentOf method which changes in the generalisations
     */
    public boolean isChildOf(PredAtom pa){
        return pa.isParentOf(this);
    }
    
    
    public boolean isParentOf(PredAtom pa){
        //This Code depends on the child class
        return false;
    }
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
    
}
