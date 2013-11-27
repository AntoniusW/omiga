package Datastructure.Rete;

import Entity.Atom;
import Entity.Instance;

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
                return !rete.containsInstance(atom, Instance.getInstance(atom.getTerms(),0,0), true);
            }
            
            //System.out.println(this + " returning: " + !rete.containsInstance(atom, instance, true) + " when asked for: " + instance + " BECAUSE OF CLOSURE!");
            return !rete.containsInstance(atom, instance, true);
        }else{
            if(instance.getSize() == 0){
                return this.memory.containsInstance(Instance.getInstance(atom.getTerms(),0,0));
            }
            //System.out.println(this + " returning: " + this.memory.containsInstance(instance) + "when asked for: " + instance);
            return this.memory.containsInstance(instance);
        }
    }
    
    
    @Override
    public String toString(){
        return "SelectionNodeNegative " + this.atom;
    }
    
 
    public void close(){
        this.closed=true;
    }
    
    public void unClose(){
        this.closed=false;
    }
    
}
