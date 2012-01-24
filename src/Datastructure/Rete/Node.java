/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructures.storage.Storage;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author User
 */
public abstract class Node {
    
    protected Variable[] varOrdering;
    protected Storage memory;
    protected ArrayList<Node> children;
    
    public Variable[] getVarOrdering(){
        return varOrdering;
    }
    
    public Collection<Term[]> select(Term[] selectionCriteria){
        return memory.select(selectionCriteria);
    }
    
    public void addInstance(Term[] instance){
        // has to be implemented by each NodeTye
    }
    
    public void removeInstance(Term[] instance){
        //has to be implemented by each NodeType
    }
    public boolean containsInstance(Term[] instance){
        return memory.containsInstance(instance);
    }
    
    public Storage testMethod_getMemory(){
        return this.memory;
    }
    
    
}

/*public interface Node {
    
    
    public Variable[] getVarOrdering();
    public Collection<Term[]> select(Term[] selectionCriteria);
    public void addInstance(Term[] instance);
    public void removeInstance(Term[] instance);
    public boolean containsInstance(Term[] instance);
    
    
}*/
