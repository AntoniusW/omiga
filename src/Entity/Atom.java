/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Predicate;
import Interfaces.BodyAtom;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import sun.nio.cs.ext.ISCII91;

/**
 *
 * @author xir
 * 
 * PredInRule instances resemble occurances of predicates within Rules. In comparism to the class predicates
 * they have PredAtoms (Constants, Variables or Functerms), and there can be more PredInRule Instances with same name.
 */
public class Atom implements BodyAtom {
    
    // To check: put Arity into predAtom, then we do not have to go thorugh the predicate faster?
    
    private Predicate p;
    private Term[] terms;
    
    public Atom(String name, int arity, Term[] terms){
        p = Predicate.getPredicate(name, arity);
        this.terms = terms;
        System.out.println(this + " - ATOMHASHCODE: " + this.hashCode() + " - " + name);
    }
    
    public Atom(String name, int arity){
        p = Predicate.getPredicate(name, arity);
        this.terms = new Term[arity];
    }
    
    public void setAtomAt(int position, Term pa){
        this.terms[position] = pa;
    }
    
    public String getName(){
        return this.p.getName();
    }
    public int getArity(){
        return this.p.getArity();
    }
    public Predicate getPredicate(){
        return this.p;
    }
    
    public Term[] getTerms(){
        return this.terms;
    }
    
    @Override
    public String toString(){
        String s = p.getName() + "(";
        for(Term pa: terms){
            s = s + pa + ",";
        }
        return s.substring(0, s.length()-1) + ")";
    }
    
    @Override
    public int hashCode(){
        // To check: this is bad. generate hash only once!
        //return (this.p.toString() + this.terms.toString()).hashCode();
        return (this.p.toString() + Instance.getInstanceAsString(this.terms)).hashCode();
    }
    @Override
    public boolean equals(Object o){
        Atom that = (Atom)o;
        System.err.println("Equals Atom: " + this + " vs. " + that);
        if(this.getPredicate() != that.getPredicate()){
            System.err.println("returning false");
            return false;
        }
        for(int i = 0; i < this.getArity(); i++){
            if(!this.terms[i].equals(that.terms[i])) {
                System.err.println("returning false");
                return false;
            }
            //if(!this.terms[i].equalsType(that.terms[i])) return false;
        }
        System.err.println("returning true");
        return true;
    }
    
    /*
     * This Method returns a unique representation of an atom. This is then used as a key for the rete selectionLayer.
     * This way we do not worry about p(X,Y) and p(A,Y) beeing the same thing. (because they get the same key)
     */
    public Atom getAtomAsReteKey(){
        Atom a = new Atom(this.p.getName(), this.p.getArity());
        HashMap<Variable,Variable> tempVars = new HashMap<Variable,Variable>();
        for(int i = 0; i < this.terms.length;i++){
            if(this.terms[i].isVariable()){
                if(tempVars.containsKey((Variable)terms[i])){
                    a.setAtomAt(i,tempVars.get((Variable)terms[i]));
                }else{
                    Variable unit_Var = Variable.getVariable("special_Var: " + tempVars.size());
                    tempVars.put((Variable)terms[i], unit_Var);
                    a.setAtomAt(i,unit_Var);
                }
            }else{
                if(this.terms[i].isConstant()){
                    a.setAtomAt(i, this.terms[i]);
                }else{
                    //FuncTerm
                    a.setAtomAt(i,getFuncTermVarReplacement((FuncTerm)this.terms[i], tempVars, a));
                }
            }
        }
        //System.out.println("WUSAH: " + a + " - "+  a.hashCode() + " - " + a.p.toString() + a.terms.toString() + " - " + (a.p.toString() + Instance.getInstanceAsString(a.terms)).hashCode());
        return a;
    }
    
    /*
     * Helper Method for getAtomAsReteKey. Returns a unique representation of a FuncTerm
     */
    private FuncTerm getFuncTermVarReplacement(FuncTerm t, HashMap<Variable,Variable> tempVars, Atom a){
        FuncTerm ret;
        ArrayList<Term> children = new ArrayList<Term>();
        for(int i = 0; i < t.getChildren().size();i++){
            if(t.getChildren().get(i).isVariable()){
                if(tempVars.containsKey((Variable)t.getChildren().get(i))){
                    children.add(tempVars.get((Variable)t.getChildren().get(i)));
                }else{
                    Variable unit_Var = Variable.getVariable("special_Var: " + tempVars.size());
                    tempVars.put((Variable)t.getChildren().get(i), unit_Var);
                    children.add(unit_Var);
                }
            }else{
                if(t.getChildren().get(i).isConstant()){
                    children.add(t.getChildren().get(i));
                }else{
                    //FuncTerm
                    children.add(getFuncTermVarReplacement((FuncTerm)t.getChildren().get(i), tempVars, a));
                }
            }
            
        }
        ret = FuncTerm.getFuncTerm(t.getName(), children);
        return ret;
    }
    
    
}
