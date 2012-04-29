/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A FuncTerm represents a logical function term. 
 * 
 * @param children a list of Terms representing those terms that are contained within this functerm. (in the ordering of the list) 
 * 
 * 
 */
public class FuncTerm extends Term{
        
    private ArrayList<Term> children;
    private FunctionSymbol function_symbol;
    
    
    /**
     * If you want to generate a FuncTerm use this method, since the constructor is private in order to prevent generation of
     * double FuncTerms instances. (Please note that this factory pattern is not as powerfull like the one we use for 
     * variables and constants, since we have to create a funcTerm, before we are able to check it's existence.
     * Anyway this may save some space, as the functerm created for lookUp can be destroyed by garbage collction as soon
     * as this method terminates. (While if we simple create Functerms when needed, the functerms are stored within our rete,
     * and therefore double functerms could not be destroyed by garbage collection, as they are in use.
     * 
     * @param name the name of your desired FuncTerm
     * @param children a list of Terms representing those terms that are contained within this functerm. (in the ordering of the list)
     * @return the desired Functerm with given name and children
     */
    public static FuncTerm getFuncTerm(String name, ArrayList<Term> children){
        FuncTerm t = new FuncTerm(name, children);
        // TODO AW: check if below code can be removed!
        if(Term.containsTerm(t)){
            return (FuncTerm)Term.getTerm(t);
        }else{
            Term.addTerm(t);
            return t;
        }
    }
    
    /**
     * 
     * @return the List of Terms this function term contains in correct ordering
     */
    public ArrayList<Term> getChildren(){
        return children;
    }
    
    /**
     * private constructor. In order to obtain a FuncTerm use the static method getFuncTerm.
     * generates a new functerm by setting it's name, usedVariables, children and hashValue.
     * 
     * @param name the name of the FuncTerm you want to create
     * @param children a list of Terms representing those terms that are contained within this functerm. (in the ordering of the list)
     */
    private FuncTerm(String name, ArrayList<Term> children){
        this.function_symbol = FunctionSymbol.getFunctionSymbol(name);
        this.name = name;   // TODO AW use function_symbol instead of name everywhere, remove this line then
        this.children = children;
        this.hashcode = 17;
        this.hashcode = this.hashcode*37 + name.hashCode();
        this.hashcode = this.hashcode*37 + Term.hashCode(children);
        this.usedVariables = new ArrayList<Variable>();
        for(int i = 0; i < children.size();i++){
            for(int j = 0; j < children.get(i).getUsedVariables().size();j++){
                Variable v = children.get(i).getUsedVariables().get(j);
                if(!usedVariables.contains(v)) usedVariables.add(v);
            }
        }
    }    

    /**
     * Since function terms have to be created during calculation we cannot apply the factory patterns
     * in such a way that we can use == for equals, because creating a function Term like we do with 
     * variables or constants would take to long (since there can be unlimited many functerms).
     * 
     * @param o the object we want to compare with this function term
     * @return wether this fucntion term equals o
     */
    @Override
    public boolean equals(Object o) {
        // functerms only equal functerms
        if(o.getClass().equals(FuncTerm.class)){
            FuncTerm t = (FuncTerm)o;
            // Functerms of different arity or different name are not equal
            if(
                    //this.name.equals(t.name) 
                    function_symbol == t.function_symbol
                    && t.children.size() == this.children.size()) {
                for(int i = 0; i < this.children.size(); i++){
                    //if the children of both functerms are not equal, the functerms are not equal
                    if(!this.children.get(i).equals(t.children.get(i))) return false;
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @return the string representation of this function term.
     */
    @Override
    public String toString(){
        String s = function_symbol.toString() + "(";
        for(int i = 0; i < children.size(); i++){
             s = s + this.children.get(i).toString() + ",";
        } 
        return s.substring(0,s.length()-1) + ")";
    }    
    
    public boolean fatherOf(Term t){
        if(!this.getClass().equals(t.getClass())) return false;
        FuncTerm that = (FuncTerm) t;
        if(this.function_symbol != that.function_symbol) return false;
        //if(!this.name.equals(that.name)) return false;
        for(int i = 0; i < this.children.size();i++){
            if(!this.children.get(i).fatherOf(that.children.get(i))) return false;
        }
        return true;
    }
    
}
