/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.Instance;
import Interfaces.Term;
import java.util.Collection;

/**
 *
 * @author g.weidinger
 */
public class SelectionNodeNegative extends SelectionNode{
    
    private boolean closed = false;
    
    public SelectionNodeNegative(Atom atom, Rete rete){
        super(atom,rete);
    }
    
    /**
     * ContainsMethod. If not closed we return if this nodes memory contains the given isnatnce.
     * Otherwise we say true if the instance is contained in the corresponding positive memory, otherwise false.
     * 
     * @param instance The instance you want to lookUp
     * @return wether the given instance is OUT or not
     */
    @Override
    public boolean containsInstance(Instance instance){
        if(closed){
            
            if(instance.getSize() == 0){
                return !rete.containsInstance(atom, Instance.getInstance(atom.getTerms(),0), true);
            }
            
            //System.out.println(this + " returning: " + !rete.containsInstance(atom, instance, true) + "when asked for: " + instance + " BECAUSE OF CLOSURE!");
            return !rete.containsInstance(atom, instance, true);
        }else{
            if(instance.getSize() == 0){
                return this.memory.containsInstance(Instance.getInstance(atom.getTerms(),0));
            }
            //System.out.println(this + " returning: " + this.memory.containsInstance(instance) + "when asked for: " + instance);
            return this.memory.containsInstance(instance);
        }
    }
    
    /**
     * Do not use this method in negative selectionNodes! Since all lookUps have to be
     * completly instanciated use the containsmethod instead!
     * 
     * @param selectionCriteria
     * @return 
     */
    /*@Override
    public Collection<Instance> select(Term[] selectionCriteria){
        //DO NOT USE THIS METHOD!
        return null;
    }*/
    
    @Override
    public String toString(){
        return "SelectionNodeNegative " + this.atom;
    }
    
    /*@Override
    public void addInstance(Instance instance, boolean from){
        System.out.println("FUCK!");
        for(int i = 0; i < varOrdering.length;i++){
            // All Variable values used in this nodes atom are set to null
            varOrdering[i].setValue(null);
        }
        
        for(int i = 0; i < instance.getSize(); i++){
            // unifyTerm assigns values to our variables
            if(!unifyTerm(atom.getTerms()[i],instance.get(i))) {
                System.out.println("Returning because this sucks!");
                return;
            }
        }
        
        
        
        Term[] varAssignment2Add = new Term[varOrdering.length];
        for(int i = 0; i < varOrdering.length;i++){
            // we create our variable assignment by taking all the values of the variables of our varOrdering.
            varAssignment2Add[i] = varOrdering[i].getValue();
        }
        Instance instance2Add = Instance.getInstance(varAssignment2Add);
        this.rete.getChoiceUnit().addInstance(this, instance2Add);
        System.out.println(this + " Adding Instance: " + instance2Add);
        this.memory.addInstance(instance2Add);
        
        for(int i = 0; i < this.children.size();i++){
            System.out.println("informing: " + children.get(i) + "of: " + instance2Add);
            children.get(i).addInstance(instance2Add, false);
        }
        for(int i = 0; i < this.childrenR.size();i++){
            System.out.println("informing: " + childrenR.get(i) + "of: " + instance2Add);
            childrenR.get(i).addInstance(instance2Add, true);
        } 
    }*/
    
    public void close(){
        this.closed=true;
    }
    
    public void unClose(){
        this.closed=false;
    }
    
}
