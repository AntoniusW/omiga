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
 * @author xir
 */
public class FuncTerm extends Term{
    
    // TOCHECK: maybe we should store the Variables of a functerm at creation time in the functerm to return it when getUsedVariables is called
    
    public static FuncTerm getFuncTerm(String name, ArrayList<Term> children){
        FuncTerm t = new FuncTerm(name, children);
        if(Term.containsPredAtom(t)){
            return (FuncTerm)Term.getPredAtom(t);
        }else{
            Term.addPredAtom(t);
            return t;
        }
    }
    
    private FuncTerm(String name, ArrayList<Term> children){
        super(name, children);
        this.name = name;
        this.hash = this.toString().hashCode();
    }
    
    /*
     * A func term is no parent of Variables nor constants, it's only parent to functerms
     */
    /*@Override
    public boolean isParentOf(Term pa) {
        if(pa.getClass().equals(this.getClass())){
            FuncTerm t = (FuncTerm)pa;
            // Functerms of different arity or different name are in no parent/child relation
            if(this.name.equals(t.name) && t.children.size() == this.children.size()) {
                for(int i = 0; i < this.children.size(); i++){
                    if(!this.children.get(i).isParentOf(t.children.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }*/
    
    /*
     * A functerm only equals functerms of same name and same children
     */
    @Override
    public boolean equals(Object o) {
        if(o.getClass().equals(this.getClass())){
            FuncTerm t = (FuncTerm)o;
            // Functerms of different arity or different name are in no parent/child relation
            if(this.name.equals(t.name) && t.children.size() == this.children.size()) {
                for(int i = 0; i < this.children.size(); i++){
                    if(!this.children.get(i).equals(t.children.get(i))) return false;
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    /*
     * A Functerm is instanciated iff he contains no variable
     */
    public boolean isInstanciated() {
        for(Term pa: children){
            if (!pa.isInstanciated()) return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        String s = name + "(";
        for(int i = 0; i < children.size(); i++){
             s = s + this.children.get(i).toString() + ",";
        } 
        return s.substring(0,s.length()-1) + ")";
    }
    
    @Override
    public boolean isConstant(){
        return false;
    }
    @Override
    public boolean isFuncTerm(){
        return true;
    }
    @Override
    public boolean isVariable(){
        return false;
    }
    
    @Override
    public ArrayList<Variable> getUsedVariables(){
        ArrayList<Variable> ret = new ArrayList<Variable>();
        for(int i = 0; i < children.size();i++){
            for(int j = 0; j < children.get(i).getUsedVariables().size();j++){
                Variable v = children.get(i).getUsedVariables().get(j);
                if(!ret.contains(v)) ret.add(v);
            }
        }
        /*for(PredAtom child: children){
            ret.addAll(child.getUsedVariables());
        }*/
        return ret;
    }
    
    @Override
    public boolean equalsType(Term t){
        if(this.getClass() != t.getClass()){
            FuncTerm that = (FuncTerm)t;
            if (this.name.equals(that.name)){
                if( this.children.size() == that.children.size()){
                    boolean flag = true;
                    for(int i = 0; i < this.children.size();i++){
                        if (!this.children.get(i).equalsType(that.children.get(i))){
                            flag = false;
                            break;
                        }
                    }
                    if (flag) return true;
                }
            }
        }
        return false;
    }
    
    
}
