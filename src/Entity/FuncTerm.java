package Entity;

import Interfaces.Term;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
public class FuncTerm extends Term implements Serializable {
        
    private static HashMap<String, FuncTerm> functerms = new HashMap<String, FuncTerm>();
    
    private ArrayList<Term> children;
    private FunctionSymbol function_symbol;
    
    
    /**
     * If you want to generate a FuncTerm use this method, since the constructor is private in order to prevent generation of
     * double FuncTerms instances. 
     * 
     * @param name the name of your desired FuncTerm
     * @param children a list of Terms representing those terms that are contained within this functerm. (in the ordering of the list)
     * @return the desired Functerm with given name and children
     */
    public static FuncTerm getFuncTerm(String name, ArrayList<Term> children){
        String lookup = name +"(";
        for(Term t: children){
            lookup = lookup + t.toString();
        }
        lookup = lookup+")";
        if(functerms.containsKey(lookup)){
            return functerms.get(lookup);
        }else{
            FuncTerm ft = new FuncTerm(name, children); 
            functerms.put(lookup, ft);
            return ft;
        }
        /*
         * Old code, before guaranteeing functerms to be unique. New code should be much faster.
         * FuncTerm t = new FuncTerm(name, children);
        // TODO AW: check if below code can be removed!
        if(Term.containsTerm(t)){
            return (FuncTerm)Term.getTerm(t);
        }else{
            Term.addTerm(t);
            return t;
        }*/
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
     * Since functionterms are unique we simple use the == operator to verify equality
     * 
     * @param o the object we want to compare with this function term
     * @return wether this fucntion term equals o
     */
    @Override
    public boolean equals(Object o) {
        return (this == o);
        /*// functerms only equal functerms
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
        return false;*/
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
    
    /**
     * 
     * this method is used to determine if a functerm is a super scema to another one.
     * This used within selectionnodes, to determine if an instance matches a scema.
     * 
     * @param t
     * @return 
     */
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

    @Override
    public int getIntValue() {
        throw new UnsupportedOperationException("getIntValue called on FuncTerm, this should not happen."); // TODO: adapt interface to avoid this
    }

    @Override
    public HashSet<Variable> getVariables() {
        HashSet<Variable> vars = new HashSet<Variable>();
        for (Term term : this.getChildren()) {
            vars.addAll(term.getVariables());
        }
        return vars;
    }
}
