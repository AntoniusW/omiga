/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Entity.Predicate;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * An atom is the occurance of a predicate within a rule. It encapsulates the corresponding predicate as well as an
 * array of terms.
 * 
 * @param p the predicate this atom belongs to
 * @param Term[] an array of Terms, representing the Terms that may stand at this position. Has to be of size = arity
 * @param hash the hashvalue of this atom
 * 
 * @param atoms A List of atoms. Used to ensure uniqueness of created atom instances
 * 
 */

public class Atom {
    
    private static ArrayList<Atom> atoms = new ArrayList<Atom>();
    
    private Predicate p;
    private Term[] terms;
    //protected int hash;
    protected int hashcode;
    
    /**
     * if you want to generate an atom use this method, since the constructor is private in order to prevent 
     * generation of double atoms. This method is ment to be called when reading in the context and creating it's rules.
     * 
     * @param name the name of the predicate corresponding to this atom
     * @param arity the arity of the corresponding predicate
     * @param terms An Array of Terms which has to be of size = arity
     * @return a unique atom
     */
    public static Atom getAtom(String name, int arity, Term[] terms){
        boolean flag;
        for(Atom a: atoms){
            if(a.getPredicate().getName().equals(name) && a.getPredicate().getArity() == arity && a.getTerms().length == terms.length){
                flag = true;
                for(int i = 0; i < a.getTerms().length;i++){
                    if(!a.getTerms()[i].equals(terms[i])){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    return a;
                }
            }
        }
        Atom ret = new Atom(name, arity,terms);
        atoms.add(ret);
        return ret;
    }
    
    /**
     * private constructor. In order to obtain atoms use the static getAtom method.
     * gets the corresponding predicate and sets it. Furthermore the hashValue of
     * this atom is set on creation. (since this is faster than calculating it a new all the time)
     * 
     * @param name the name of the predicate corresponding to this atom
     * @param arity the arity of the corresponding predicate
     * @param terms An Array of Terms which has to be of size = arity
     */
    private Atom(String name, int arity, Term[] terms){
        p = Predicate.getPredicate(name, arity);
        this.terms = terms;
        this.hashcode = 17;
        this.hashcode = this.hashcode*37 + p.hashCode();
        this.hashcode = this.hashcode*37 + Term.hashCode(terms);
        //System.out.println(this + " - ATOMHASHCODE: " + this.hashCode() + " - " + name);
    }
    
    /*public Atom(String name, int arity){
        p = Predicate.getPredicate(name, arity);
        this.terms = new Term[arity];
    }*/
    
    /*public void setAtomAt(int position, Term pa){
        this.terms[position] = pa;
    }*/
    
    /**
     * 
     * @return the name of the corresponding predicate
     */
    public String getName(){
        return this.p.getName();
    }
    /**
     * 
     * @return the arity of the corresponding predicate
     */
    public int getArity(){
        return this.p.getArity();
    }
    /**
     * 
     * @return the corresponding predicate 
     */
    public Predicate getPredicate(){
        return this.p;
    }
    /**
     * 
     * @return the array of terms, assigned to this atom
     */
    public Term[] getTerms(){
        return this.terms;
    }
    
    /**
     * 
     * @return the string representation of this atom
     */
    @Override
    public String toString(){
        String s = p.getName() + "(";
        for(Term pa: terms){
            s = s + pa + ",";
        }
        return s.substring(0, s.length()-1) + ")";
    }
    
    /**
     * This method is needed in order to use Atoms as Keys within HashMaps
     * 
     * 
     * @return the hashValue initialized at creation
     */
    @Override
    public int hashCode(){
        return hashcode;
    }
    /**
     * This method is needed in order to use Atoms as Keys within HashMaps
     * Since we use a factory pattern when creating atoms, atoms are unqiue,
     * and therefore we can use == operator to confirm equality 
     * (which is faster than comparing a predicates name and arity all the time)
     * 
     * @param the object we want to compare this to
     * @return weither the two objects represent the same atom
     */
    @Override
    public boolean equals(Object o){
        return (this == o);
        /*System.err.println("ATOM EQUALS STARTED: ");
        Atom that = (Atom)o;
        System.err.println("Equals Atom: " + this + " vs. " + that);
        //if(this.getPredicate() != that.getPredicate()){
        if(!this.getPredicate().equals(that.getPredicate())){
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
        return true;*/
    }
    
    /**
     * This method returns a unique representation of this atom, by replacing all Variable occurances with standardized Variables.
     * Such that p(X,Y) will return the same atom as p(Y,Z). These standardized Atoms then can be used as Keys in our Rete (as we
     * do not want to differ between a storage for p(X,Y) and p(Y,Z), as their instances are the same.
     * 
     * @return a standardized representation of this atom
     */
    public Atom getAtomAsReteKey(){
        //Atom a = new Atom(this.p.getName(), this.p.getArity());
        HashMap<Variable,Variable> tempVars = new HashMap<Variable,Variable>();
        Term[] keyTerms = new Term[this.terms.length];
        for(int i = 0; i < this.terms.length;i++){
            if(this.terms[i].getClass().equals(Variable.class)){
                if(tempVars.containsKey((Variable)terms[i])){
                    //a.setAtomAt(i,tempVars.get((Variable)terms[i]));
                    keyTerms[i] = tempVars.get((Variable)terms[i]);
                }else{
                    Variable unit_Var = Variable.getVariable("special_Var: " + tempVars.size());
                    tempVars.put((Variable)terms[i], unit_Var);
                    //a.setAtomAt(i,unit_Var);
                    keyTerms[i] = unit_Var;
                }
            }else{
                if(this.terms[i].getClass().equals(Constant.class)){
                    //a.setAtomAt(i, this.terms[i]);
                    keyTerms[i] = this.terms[i];
                }else{
                    //FuncTerm
                    //a.setAtomAt(i,getFuncTermVarReplacement((FuncTerm)this.terms[i], tempVars, a));
                    keyTerms[i] = getFuncTermVarReplacement((FuncTerm)this.terms[i], tempVars);
                }
            }
        }
        //System.out.println("WUSAH: " + a + " - "+  a.hashCode() + " - " + a.p.toString() + a.terms.toString() + " - " + (a.p.toString() + Instance.getInstanceAsString(a.terms)).hashCode());
        return Atom.getAtom(this.getName(), this.getArity(), keyTerms);
    }
    
    /**
     * This is a helper method for getAtomAsReteKey. It returns a unqiue representation of a FuncTerm,
     * by returning an equal functerm as t, but replacing all variable occurances with standardized variables.
     * 
     * @param t the term of which we want to get a unique representation
     * @param tempVars a HashMap of Variables, mapping already used variables to standardised variables
     * @return the standardized functionterm
     */
    private FuncTerm getFuncTermVarReplacement(FuncTerm t, HashMap<Variable,Variable> tempVars){
        FuncTerm ret;
        ArrayList<Term> children = new ArrayList<Term>();
        for(int i = 0; i < t.getChildren().size();i++){
            if(t.getChildren().get(i).getClass().equals(Variable.class)){
                if(tempVars.containsKey((Variable)t.getChildren().get(i))){
                    children.add(tempVars.get((Variable)t.getChildren().get(i)));
                }else{
                    Variable unit_Var = Variable.getVariable("special_Var: " + tempVars.size());
                    tempVars.put((Variable)t.getChildren().get(i), unit_Var);
                    children.add(unit_Var);
                }
            }else{
                if(t.getChildren().get(i).getClass().equals(Constant.class)){
                    children.add(t.getChildren().get(i));
                }else{
                    //FuncTerm
                    children.add(getFuncTermVarReplacement((FuncTerm)t.getChildren().get(i), tempVars));
                }
            }
            
        }
        ret = FuncTerm.getFuncTerm(t.getName(), children);
        return ret;
    }
    
    public boolean fatherOf(Atom a){
        if(!this.getPredicate().equals(a.getPredicate())) return false;
        for(int i = 0; i < this.terms.length;i++){
            if(!this.terms[i].fatherOf(a.terms[i])) return false;
        }
        return true;
    }
     
}
