/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A predicate encapsulates the name and arity of a fact. This class is used together with an instance
 * to represent a fact. Predicate instances alone are used as keys within hashMaps within our rete datastructure
 * in order to map instances to corresponding predicates.
 * 
 * @param name the name of the predicate
 * @param arity the arity of the predicate
 * @param hash the hashvalue of this predicate
 * 
 * @param predicates Factory pattern: A datastructure to store all created predicates so we later on use only one instance for each predicate
 * 
 */
public class Predicate {
    
    private static ArrayList<Predicate> predicates = new ArrayList<Predicate>();
    
    
    private String name;
    private int arity;
    private int hash;
    
    /**
     * if you want to generate a predicate use this method, since the constructor is private in order to prevent generation of
     * double predicate instances. This method is meant to be called while reading the context and creating it's rules.
     * 
     * @param name name of the predicate
     * @param arity arity of the predicate
     * @return unique predicate with the given name and arity
     */
    public static Predicate getPredicate(String name, int arity){
        for(Predicate p: predicates){
            if(p.name.equals(name) && p.arity == arity){
                return p;
            }
        }
        Predicate p = new Predicate(name, arity);
        predicates.add(p);
        return p;
    }
    
    
    /**
     * private constructor. In order to obtain predicates use the static getPredicate method.
     * generates a new predicate and sets it's name and arity to the given parameters.
     * furthermore the hash is calculated once on creation (which is faster than calculating it a new all the time)
     * 
     * @param name name of the predicate
     * @param arity arity of the predicate
     */
    private Predicate(String name, int arity){
        this.name=name;
        this.arity = arity;
        this.hash = (name+arity).hashCode();
    }
    
    /**
     * This method is needed in order to use Predicates as Keys within HashMaps
     * 
     * @return the hash that was set during creation
     */
    @Override
    public int hashCode(){
        return hash;
    }
    
    /**
     * This method is needed in order to use Predicates as Keys within HashMaps
     * Since we use a factory pattern when creating predicates, predicates are unqiue,
     * therefore we can use == operator to confirm equality 
     * (which is faster than comparing a predicates name and arity all the time)
     * 
     * @param the object we want to compare this
     * @return weither the two objects represent the same predicate
     */
    @Override
    public boolean equals(Object o){
        // System.err.println("Equals for: " + this + " vs: " + o + " returns: " + this == o);
        return (this == o);
        // TODO: Faster if no class comparism. (We probably will never compare it to something else but predicates
        /*if(this.getClass().equals(o.getClass())){
            Predicate p = (Predicate)o;
            if(p.name.equals(this.name) && this.arity == p.arity) return true;
        }
        return false;*/
    }

    /**
     * 
     * @return the arity of this predicate
     */
    public int getArity() {
        return arity;
    }

    /**
     * 
     * @return the name of this predicate
     */
    public String getName() {
        return name;
    }
    
    /**
     * returns the string representation of this predicate
     * @return 
     */
    @Override
    public String toString(){
        return this.name;
    }
    
    
    
    
    
    
}
