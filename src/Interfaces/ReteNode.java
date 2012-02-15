/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Entity.Predicate;
import Entity.Rule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author User
 */
public abstract class ReteNode {
    
    Predicate p;
    ArrayList<ReteNode> children;
    ArrayList<Rule> completeRules;
    //      []Pos   ConstID         InstanceID
    HashMap<Term,HashSet<String>>[] storage;
    HashMap<Term, HashSet<Term[]>>[] store;
    
    @SuppressWarnings("unchecked") // AW: workaround for array conversion
    public ReteNode(Predicate p){
        this.p = p;
        this.children= new ArrayList<ReteNode>();
        this.completeRules = new ArrayList<Rule>();
        this.storage = new HashMap[p.getArity()];
        for(HashMap hm: storage){
            hm = new HashMap<Term, HashSet<Term[]>>();
        }
    }


    public ArrayList<ReteNode> getChildren() {
        return children;
    }

    public Predicate getPredicate() {
        return p;
    }
    
   
    
    
}
