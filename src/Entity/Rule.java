/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.BodyAtom;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Rule {
    private PredInRule head;
    private ArrayList<PredInRule> bodyPlus;
    private ArrayList<PredInRule> bodyMinus;
    private ArrayList<Operator> operators;
    
    public Rule(PredInRule head, ArrayList<PredInRule> bodyPlus, ArrayList<PredInRule> bodyMinus, ArrayList<Operator> operators){
        this.head = head;
        this.bodyPlus = bodyPlus;
        this.bodyMinus = bodyMinus;
        this.operators = operators;
    }
    
    public Rule(){
        this.head = null;
        this.bodyPlus = new ArrayList<PredInRule>();
        this.bodyMinus = new ArrayList<PredInRule>();
    }
    
    public void setHead(PredInRule head){
        this.head = head;
    }
    public void addAtomPlus(PredInRule p){
        this.bodyPlus.add(p);
    }
    public void addAtomMinus(PredInRule p){
        this.bodyMinus.add(p);
    }
    
    public boolean isSafe(){
        //TODO: Is this the definition of safe? or is it also safe when the Variable occurs in the head?
        
        for(BodyAtom ba: bodyMinus){
            if(ba.getClass().equals(PredInRule.class)){
                PredInRule pir = (PredInRule)ba;
                if(!bodyPlus.contains(pir)) return false;
            }
        }
        return true;
    }
    
    public boolean isConstraint(){
        if(head == null) return true;
        return false;
    }
    
    @Override
    public String toString(){
        String s = "";
        if (head != null) s = s + head.toString();
        s = s + " :- ";
        for(BodyAtom ba: bodyPlus){
            s = s + ba + ",";
        }
        for(BodyAtom ba: bodyMinus){
            s = s + ba + ",";
        }
        return s.substring(0, s.length()-1) + ".";
    }

    public ArrayList<PredInRule> getBodyMinus() {
        return bodyMinus;
    }

    public ArrayList<PredInRule> getBodyPlus() {
        return bodyPlus;
    }

    public PredInRule getHead() {
        return head;
    }
    
    
    
    
    
}