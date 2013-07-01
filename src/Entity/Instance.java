package Entity;

import Interfaces.Term;
import java.io.Serializable;

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
public class Instance implements Serializable {
    
    public int propagationLevel;
    public int decisionLevel;
    
    public boolean isMustBeTrue;
    
    public static int instance_count = 0;
        
    Term[] terms;

    public Term[] getTerms() {
        return terms;
    }
    int hashcode;
    
    /**
     * if you want to generate an instance use this method, since the constructor is private in order to control
     * the generation of Instances.
     * 
     * @param terms an array of terms which has to contain only constants or instantiated function terms. This has to be guaranteed by the programmer. Also null values are not valid!
     * @return returns the desired instance based on the terms array
     */
    public static Instance getInstance(Term[] terms, int propagationLevel, int decisionLevel){
        instance_count++;
        return new Instance(terms, propagationLevel, decisionLevel);
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
        if ( terms.length != 0) {
            return terms[i];
        } else {
            return null;
        }
        /*try {
        return terms[i];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }*/
    }
    
    /**
     * private constructor. Use getInstance in order to obtain an instance.
     * Sets the term array of this instance and calculates and sets the hashvalue
     * 
     * @param terms 
     */
    protected Instance(Term[] terms, int propagationLevel, int decisionLevel){
        this.terms = terms;    
        this.hashcode = Term.hashCode(terms);
            // Note: hashcode ignores decision/propagation levels on purpose (selection methods need terms only)
        this.propagationLevel = propagationLevel;
        this.decisionLevel = decisionLevel;
    }
    
    /**
     * Copy constructor, makes shallow copy.
     * @param copy the instance to copy.
     */
    public Instance(Instance copy) {
        terms = copy.terms;
        hashcode = copy.hashcode;
        propagationLevel = copy.propagationLevel;
        decisionLevel = copy.decisionLevel;
    }
    
    /**
     * This method is needed to use Instances in combination with HashMaps/Sets
     * 
     * @return the hashValue that was set on creation
     */
    @Override
    public int hashCode(){
        return hashcode;
    }
    /**
     * This method is needed to use Instances in combination with HashMaps/Sets.
     * Note that the propagationLevel is ignored, since it is important only
     * for learning, but not whether two instances are equal. Do _not_ compare
     * propagationLevels (this would break code for checking containment).
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
        boolean isFirst = true;
        for(int i = 0; i < this.terms.length;i++){
            if( ! isFirst ) {
                s+= ",";
            }
            s += terms[i];
            isFirst = false;
        }
        return s+"]";
        //return s.substring(0, s.length()-1) + "]";
    }
}

