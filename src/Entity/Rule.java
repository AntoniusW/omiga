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
 * A rule represents a logical ASP rule and consists of a head, a positive body, a negative body, and operators (f.e.: X + Y = Z)
 * Rules are used to represent an ASP Context, and are generated when reading in the context.
 * After the creation of our Rete Network they are never touched again.
 * 
 * @param head An Atom being the head of the rule, may be null, then this rule is a constraint
 * @param bodyPlus the positive body of this rule, must at least contain one Atom. (Since facts are treated special)
 * @param bodyMinus the negative body of this rule, may be empty.
 * @param operators the operators of this rule, may be empty.
 * 
 */
public class Rule {
    
    protected Atom head;
    protected ArrayList<Atom> bodyPlus;
    protected ArrayList<Atom> bodyMinus;
    protected ArrayList<Operator> operators;
    
    /**
     * 
     * Constructor for creating a rule with all parameters.
     * 
     * @param head the head atom you want your rule to have. May be null, then you create a constraint.
     * @param bodyPlus a list of Atoms, beeing treated as positive Atoms. [a(X,Y)]
     * @param bodyMinus a list of Atoms, beeing treated as negative Atoms. [not a(X,Y)]
     * @param operators a list of Operators. [X + Y = Z]
     */
    public Rule(Atom head, ArrayList<Atom> bodyPlus, ArrayList<Atom> bodyMinus, ArrayList<Operator> operators){
        this.head = head;
        this.bodyPlus = bodyPlus;
        this.bodyMinus = bodyMinus;
        this.operators = operators;
    }
    
    /**
     * Constructor for creating an empty rule. You must not work with empty rules during calculation,
     * but using this constructor and then the addPostive/Negative Atom methods, has proven more be more comfortable.
     */
    public Rule(){
        this.head = null;
        this.bodyPlus = new ArrayList<Atom>();
        this.bodyMinus = new ArrayList<Atom>();
        this.operators = new ArrayList<Operator>();
    }
    
    /**
     * This method is only needed when working with the empty constructor.
     * 
     * @param head the headAtom you want to set.
     */
    public void setHead(Atom head){
        this.head = head;
    }
    /**
     * This method is only needed when working with the empty constructor.
     * 
     * @param p an atom you want to add to your rules positive body.
     */
    public void addAtomPlus(Atom p){
        this.bodyPlus.add(p);
    }
    /**
     * This method is only needed when working with the empty constructor.
     * 
     * @param p an atom you want to add to your rules negative body.
     */
    public void addAtomMinus(Atom p){
        this.bodyMinus.add(p);
    }
     /**
     * This method is only needed when working with the empty constructor.
     * 
     * @param o the operator you want to add
     */
    public void addOperator(Operator o){
        this.operators.add(o);
    }
    
    /**
     * a rule is safe if no variable occurring in the head or negative body are free (= not occurring within the positive body and not fixed through operators)
     * 
     * 
     * @return wether this rule is safe or not
     */
    public boolean isSafe(){
        HashSet<Variable> hs = new HashSet<Variable>();
        // We create a HashSet containing all Variables occuring in the positive body.
        for(Atom ba: bodyPlus){
            Atom pir = (Atom)ba;
            if(ba.getClass().equals(Atom.class)){
                for(Term t: pir.getTerms()){
                    for(Variable v: t.getUsedVariables()){
                        hs.add(v);
                    }
                }
            }
        }
        
        for(Operator op: this.operators){
            for(Operator op2: this.operators){
                int i = 0;
                for(Variable v: op2.getUsedVariables()){
                    if(hs.contains(v)) i++;
                }
                if(i >= op2.getUsedVariables().size()-1){ // All but one Variables of this operator are fixed --> Add the new one
                    hs.addAll(op2.getUsedVariables());
                }
            }
        }
        
        // we check for each Operator if it is well defined (all Variables in HS)
        for(Operator op: this.operators){
            for(Variable v: op.getUsedVariables()){
                if(!hs.contains(v)) {
                    System.err.println("Operator not Safe: " + op);
                    return false;
                }
            }
        }
        
        // we check for each Variable of the negative body if it also occurs in the positive body
        for(Atom ba: bodyMinus){
            if(ba.getClass().equals(Atom.class)){
                Atom pir = (Atom)ba;
                for(Term t: pir.getTerms()){
                    for(Variable v: t.getUsedVariables()){
                        if(!hs.contains(v)) {
                            System.err.println("Atom not Safe: not " + ba);
                            return false;
                        }
                    }
                }
            }
        }
        // we check if the variables of the head atom are contained in the positive body
        if(head != null){
            for(Term t: this.head.getTerms()){
                for(Variable v: t.getUsedVariables()){
                    if(!hs.contains(v)) {
                        System.err.println("Head not Safe: " + head);
                        return false;
                    }
                }
            }
        }
        
        // TODO: A Rule is also safe if we have an operator
        
        return true;
    }
    
    /**
     * returns the string representation of this rule.
     */
    @Override
    public String toString(){
        String s = "";
        if (head != null) s = s + head.toString();
        s = s + " :- ";
        for(Atom ba: bodyPlus){
            s = s + ba + ",";
        }
        for(Atom ba: bodyMinus){
            s = s + "not " + ba + ",";
        }
        for(Operator op: this.operators){
            s = s + op + ",";
        }
        return s.substring(0, s.length()-1) + ".";
    }

    /**
     * 
     * @return the positive body of this rule
     */
    public ArrayList<Atom> getBodyMinus() {
        return bodyMinus;
    }
    /**
     * 
     * @return the negative body of this rule
     */
    public ArrayList<Atom> getBodyPlus() {
        return bodyPlus;
    }
    /**
     * 
     * @return the operators of this rule 
     */
    public ArrayList<Operator> getOperators(){
        return operators;
    }
    /**
     * 
     * @return the head of this rule
     */
    public Atom getHead() {
        return head;
    }
    
    public boolean isConstraint(){
        return this.head == null;
    }

}