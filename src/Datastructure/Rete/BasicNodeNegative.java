/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Instance;
import Entity.Predicate;

/**
 *
 * @author g.weidinger
 */
public class BasicNodeNegative extends BasicNode{
    
    private boolean closed;
    
    public BasicNodeNegative(int arity, Rete rete, Predicate pred){
        super(arity,rete,pred);
        this.closed = false;
    }
    
    /**
     * 
     * @param instance the instance you want to check for
     * @return wether the instance is saved within the memory or not
     * 
     * if the node is closed it this method asks the corresponding positive BasicNode
     * and returns true if that node does not contain this instance, false otherwise.
     */
    @Override
    public boolean containsInstance(Instance instance){
        if(closed){
            return !rete.containsInstance(this.pred, instance, true);
        }
        return memory.containsInstance(instance);
    }
    
}
