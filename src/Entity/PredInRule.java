/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Predicate;
import Interfaces.BodyAtom;
import Interfaces.PredAtom;
import java.util.ArrayList;

/**
 *
 * @author xir
 * 
 * PredInRule instances resemble occurances of predicates within Rules. In comparism to the class predicates
 * they have PredAtoms (Constants, Variables or Functerms), and there can be more PredInRule Instances with same name.
 */
public class PredInRule implements BodyAtom {
    
    private Predicate p;
    private PredAtom[] atoms;
    private ArrayList<Variable> vars;
    
    public PredInRule(String name, int arity, PredAtom[] atoms){
        p = Predicate.getPredicate(name, arity);
        this.atoms = atoms;
        vars = new ArrayList<Variable>();
        for(int i = 0; i < atoms.length;i++){
            for(Variable v: atoms[i].getUsedVariables()){
                if(!vars.contains(v)) vars.add(v);
            }
        }
    }
    
    public PredInRule(String name, int arity){
        p = Predicate.getPredicate(name, arity);
        this.atoms = new PredAtom[arity];
    }
    
    public void setAtomAt(int position, PredAtom pa){
        this.atoms[position] = pa;
        //Set the variables for this PredInRule
        vars = new ArrayList<Variable>();
        for(int i = 0; i < atoms.length;i++){
           if(atoms[i] != null){
            for(Variable v: atoms[i].getUsedVariables()){
              if(!vars.contains(v)) vars.add(v);
            }
          }  
        }
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
    
    public PredAtom[] getAtoms(){
        return this.atoms;
    }
    
    @Override
    public String toString(){
        String s = p.getName() + "(";
        for(PredAtom pa: atoms){
            s = s + pa + ",";
        }
        return s.substring(0, s.length()-1) + ")";
    }
    
    @Override
    public int hashCode(){
        return (this.p.toString() + this.atoms.toString()).hashCode();
    }
    @Override
    public boolean equals(Object o){
        PredInRule that = (PredInRule)o;
        if(this.getPredicate() != that.getPredicate()) return false;
        for(int i = 0; i < this.getArity(); i++){
            if(this.atoms[i] != that.atoms[i]) return false;
        }
        return true;
    }
    
    public ArrayList<Variable> getVariables(){
        return vars;
    }
    
    
}
