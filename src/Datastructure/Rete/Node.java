/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author User
 */
public abstract class Node {
    
    protected Variable[] varOrdering;
    protected Storage memory;
    protected ArrayList<Node> children;
    protected Rete rete;
    HashMap<Variable,Integer> tempVarPosition;
    
    public HashMap<Variable,Integer> getVarPositions(){
        return this.tempVarPosition;
    }
    
    public void resetVarPosition(Atom atom){
        tempVarPosition.clear();
        Term[] terms = atom.getTerms();
        for(int i = 0; i < terms.length;i++){
            Term t = terms[i];
            for(int j = 0;j < t.getUsedVariables().size();j++){
                Variable v = t.getUsedVariables().get(j);
                if(!tempVarPosition.containsKey(v)){
                    tempVarPosition.put(v, tempVarPosition.size());
                }
            }
        }
    }
    
    public Node(Rete rete){
        this.rete = rete;
        this.children = new ArrayList<Node>();
        tempVarPosition = new HashMap<Variable,Integer>();
        this.rete.getChoiceUnit().addNode(this);
    }
    
    public Variable[] getVarOrdering(){
        return varOrdering;
    }
    
    public Collection<Instance> select(Term[] selectionCriteria){
        return memory.select(selectionCriteria);
    }
    
    public void addInstance(Instance instance, Node from){
        this.rete.getChoiceUnit().addInstance(this, instance);
    }
    
    public void removeInstance(Instance instance){
        //has to be implemented by each NodeType
    }
    public boolean containsInstance(Instance instance){
        return memory.containsInstance(instance);
    }
    
    public Storage testMethod_getMemory(){
        return this.memory;
    }
    
    public ArrayList<Node> getChildren(){
        return this.children;
    }
    
    public void addChild(Node n){
        if(n.getClass().equals(ChoiceNode.class)){
            System.out.println("NODE: Choice node is added to: " + this);
        }
            
        if (!this.children.contains(n)) this.children.add(n);
    }
    

    
    
}

/*public interface Node {
    
    
    public Variable[] getVarOrdering();
    public Collection<Term[]> select(Term[] selectionCriteria);
    public void addInstance(Term[] instance);
    public void removeInstance(Term[] instance);
    public boolean containsInstance(Term[] instance);
    
    
}*/
