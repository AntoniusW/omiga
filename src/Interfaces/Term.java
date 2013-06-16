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
 * @author Gerald Weidinger 0526105
 * 
 * A Term is an abstract class extended by Variable, Constant and FuncTerm. It's essential for creating instances
 * since an instance is nothing more than an array of terms.
 * 
 * @param name the name of the term
 * @param hash the hasValue of the term (calculated on creation)
 * @param usedVariables a List of Variables used by this term
 * @param terms a static HashMap of terms, which is used for uniqueness of terms
 * 
 * 
 */
public abstract class Term  implements OperandI {
    
    
    protected String name;
    protected int hash;
    protected int hashcode;
    protected ArrayList<Variable> usedVariables;
    
    /**
     * empty constructor
     */
    public Term(){
        
    }
    
    /**
     * public constructor
     * creates a term with desired name calculates and sets the hashValue and initializes the usedVariables list
     * 
     * @param name name of the Term
     */
    public Term(String name){
        this.name = name;  
        this.hashcode = name.hashCode();
        this.usedVariables = new ArrayList<Variable>();
    }
    
    /**
     * 
     * @return the name of this term
     */
    public String getName(){
        return name;
    }
    /*
     * returns the hashValue of this term, which was set during creation
     */
    @Override
    public int hashCode(){
        return this.hashcode;
    }
    
    public static int hashCode(Term[] terms) {
        if(terms.length==0) {
            return 0;
        }
        int result = 17;
        for(int i=0; i<terms.length;i++) {
            result = result*37+terms[i].hashCode();
        }
        return result;
    }
    
     public static int hashCode(ArrayList<Term> terms) {
        if(terms.isEmpty()) {
             return 0;
         }
        int result = 17;
         for (Term term : terms) {
             result = result*37+term.hashCode();             
         }
         return result;
    }
     
     public static String prettyPrint(Term[] termlist, boolean densePrint) {
         String ret;
         if(densePrint)
             ret="[";
         else 
            ret="(";  
         for (int i = 0; i < termlist.length; i++) {
             ret+=termlist[i].toString();
             if(i+1 < termlist.length)
                 ret+=",";
         }
         if(densePrint)
             ret+="]";
         else
            ret+=") ";
         return ret;
     }
    
    
    /**
     * 
     * @return the list of used variables of this term
     */
    public ArrayList<Variable> getUsedVariables(){
        return this.usedVariables;
    }
    
    
    public boolean fatherOf(Term t){
        return true;
    }

    public abstract HashSet<Variable> getVariables();
    
}
