/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Predicate;
import Interfaces.BodyAtom;
import Interfaces.Term;
import java.util.ArrayList;

/**
 *
 * @author xir
 * 
 * PredInRule instances resemble occurances of predicates within Rules. In comparism to the class predicates
 * they have PredAtoms (Constants, Variables or Functerms), and there can be more PredInRule Instances with same name.
 */
public class Atom implements BodyAtom {
    
    private Predicate p;
    private Term[] terms;
    
    public Atom(String name, int arity, Term[] terms){
        p = Predicate.getPredicate(name, arity);
        this.terms = terms;
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
        return (this.p.toString() + this.terms.toString()).hashCode();
    }
    @Override
    public boolean equals(Object o){
        Atom that = (Atom)o;
        if(this.getPredicate() != that.getPredicate()) return false;
        for(int i = 0; i < this.getArity(); i++){
            if(this.terms[i] != that.terms[i]) return false;
        }
        return true;
    }
    
    
}
