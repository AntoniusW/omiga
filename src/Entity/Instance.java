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
 * @author Gerald Weidinger 0526105
 * 
 * An instance encapsulates an array of Terms, and offers methods to make instances easier to handle. (HashCode and equals, to use them with HashMaps/Sets)
 * 
 * 
 * @param terms an array of terms, only containing constants or instantiated function terms.
 * @param hash the hashValue of this instance, set at creation.
 * 
 * @param instances a HashMap of instances in order to not create duplicate instances
 */
public class Instance {
    
    public static int lol = 0;
    public static int lolra = 0;
    private static HashMap<Instance,Instance> instances = new HashMap<Instance,Instance>();
        
    Term[] terms;
    int hash;
    
    /**
     * if you want to generate an instance use this method, since the constructor is private in order to control
     * the generation of Instances.
     * 
     * @param terms an array of terms which has to contain only constants or instantiated function terms. This has to be guaranteed by the programmer. Also null values are not valid!
     * @return returns the desired instance based on the terms array
     */
    public static Instance getInstance(Term[] terms){
        lol++;
        return new Instance(terms);
        /*Instance i = new Instance(terms);
        if(instances.containsKey(i)){
            //System.out.println("Asked for: " + s + " returning: " + instances.get(i)); 
            return instances.get(i);
        }else{
            lol++;
            instances.put(i, i);
            return i;
        }*/
    }
    /**
     * 
     * @return the size of this instance
     */
    public int getSize(){
        return this.terms.length;
    }
    /**
     * 
     * @param i the position of the term you want to have
     * @return the term a position i of this instance
     */
    public Term get(int i){
        return terms[i];
    }
    
    /**
     * private constructor. Use getInstance in order to obtain an instance.
     * Sets the term array of this instance and calculates and sets the hashvalue
     * 
     * @param terms 
     */
    private Instance(Term[] terms){
        this.terms = terms;
        String s = "";
        for(int i = 0; i < terms.length;i++){
            s = s + terms[i].toString() + ",";
        }
        this.hash =  s.hashCode();         
    }
    
    /**
     * This method is needed to use Instances in combination with HashMaps/Sets
     * 
     * @return the hashValue that was set on creation
     */
    @Override
    public int hashCode(){
        return hash;
    }
    /**
     * This method is needed to use Instances in combination with HashMaps/Sets
     * 
     * @param o The object we want to compare this instance with. Precondition: o must be an instance
     * @return wether the two instances are equal or not
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        Instance that = (Instance)o;
        if(this.terms.length != that.terms.length) return false;
        for(int i = 0; i < terms.length;i++){
            if(!this.terms[i].equals(that.terms[i])) return false;
        }
        return true;
    }
    /**
     * 
     * @return the string representation of this instance
     */
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
    }

}

