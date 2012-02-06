/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.Term;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author g.weidinger
 * ClassName: Instance
 * Purpose: Just a static class that stores instances and provides methodes to look them up and convert them to String
 */
public class Instance {
    
    public static int lol = 0;
    private static HashMap<Instance,Instance> instances = new HashMap<Instance,Instance>();
        
    Term[] terms;
    int hash;
    
    public static Instance getInstance(Term[] terms){
        /*String s = "[";
        for(int i = 0; i < terms.length;i++){
            s = s + terms[i] + ",";
        }
        s = s.substring(0, s.length()-1) + "]";*/
        lol++;
        /*Instance i = new Instance(terms);
        System.out.println("Instance required for: " + i);*/
        return new Instance(terms);
        /*Instance i = new Instance(terms);
        if(instances.containsKey(i)){
            //System.out.println("Asked for: " + s + " returning: " + instances.get(i)); 
            return instances.get(i);
        }else{
            instances.put(i, i);
            return i;
        }*/
    }
    
    public int getSize(){
        return this.terms.length;
    }
    
    public Term get(int i){
        return terms[i];
    }
    
    private Instance(Term[] terms){
        this.terms = terms;
        String s = "";
        for(int i = 0; i < terms.length;i++){
            s = s + terms[i].toString() + ",";
        }
        this.hash =  s.hashCode();
    }
    
    
    @Override
    public int hashCode(){
        return hash;
    }
    
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        Instance that = (Instance)o;
        //lol++;
        //System.err.println("Comparing: " + this + " : " + that + " - " + lol);
        if(this.terms.length != that.terms.length) return false;
        for(int i = 0; i < terms.length;i++){
            if(!this.terms[i].equals(that.terms[i])) return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        String s = "[";
        for(int i = 0; i < this.terms.length;i++){
            s = s + terms[i] + ",";
        }
        return s.substring(0, s.length()-1) + "]";
    }
    
    
    
    
    /*
     * Old Version. Only needed for printouts for testing
     */
    public static String getInstanceAsString(Term[] instance){
        String s = "[";
        for(int i = 0; i < instance.length;i++){
            s = s + instance[i] + ",";
        }
        return s.substring(0,s.length()-1) + "]";
        //return "a";
    }

}

