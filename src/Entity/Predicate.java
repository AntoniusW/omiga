/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.HashMap;

/**
 *
 * @author xir
 * 
 * A Predicate represents a fact of our knowledgebase for example: bird(X), or Father(X,Y)
 * A Predicate itself is only an entity encapsulating the name and the arity of a fact.
 * The actual occurances of these predicates in rules, are represented via the Class PredInRule!
 * 
 * 
 */
public class Predicate {
    
    // A datastrucutre to store all created predicates so we later on use only one instance for one predicate
    private static HashMap<Predicate,Predicate> predicates = new HashMap<Predicate,Predicate>();
    
    
    private String name;
    private int arity;
    private int hash;
    
    /*
     * Use this method onl while parsing a context
     */
    public static Predicate getPredicate(String name, int arity){
        Predicate p = new Predicate(name,arity);
        if(predicates.containsKey(p)){
            return predicates.get(p);
        }else{
            predicates.put(p, p);
            return p;
        }
    }
    
    private Predicate(String name, int arity){
        this.name=name;
        this.arity = arity;
        this.hash = (name+arity).hashCode();
    }
    
    @Override
    public int hashCode(){
        return hash;
    }
    
    @Override
    /*
     * Two predicates are equal if they have the same name and the same arity
     */
    public boolean equals(Object o){
        // TODO: Faster if no class comparism. (We probably will never compare it to something else but predicates
        if(this.getClass().equals(o.getClass())){
            Predicate p = (Predicate)o;
            if(p.name.equals(this.name) && this.arity == p.arity) return true;
        }
        return false;
    }

    public int getArity() {
        return arity;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
    
    
    
    
    
}
