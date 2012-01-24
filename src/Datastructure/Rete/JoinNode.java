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
public class JoinNode {
    
    Node a;
    Node b;
    
    Variable[] varOrdering;
    Storage memory;
    ArrayList<Node> children;
    
    /*
     * Select: Wir gehen einfach das Variable Ordering der anderen Node durch und setzen alle Variablen nach der Instanz davon
     */
    
    public JoinNode(Node a, Node b){
        this.a = a;
        this.b = b;
    }
    
    
    public void addInstance(Term[] instance, Node n){
        // We determine the which of the two join partners the instance comes from
        Node selectFromHere;
        if(n.equals(a)){
            selectFromHere = b;
        }else{
            selectFromHere = a;
        }
        
        // we determine the selectionCriteria by first resetting all variable values of the node where we want to look up
        // then we assign the variables of the node where the isntance comes from with the instanciated values of that instance
        // our selection Criteria is then the array consisting of the values of the VarOrdering of the other node
        for(Variable v: selectFromHere.getVarOrdering()){
            v.setValue(null);
        }
        for(int i = 0; i < n.getVarOrdering().length;i++){
            n.getVarOrdering()[i].setValue(instance[i]);
        }
        Term[] selectionCriteria = new Term[selectFromHere.getVarOrdering().length];
        
        for(int i = 0; i < selectFromHere.getVarOrdering().length; i++){
            selectionCriteria[i] = selectFromHere.getVarOrdering()[i].getValue();
        }
        
        // We select from the other node via our selectionCriterion
        // and add to our memory the combination of Variablevalues for each joinpartner
        
        Collection<Term[]> joinPartners = selectFromHere.select(selectionCriteria);
        for(Term[] varAssignment: joinPartners){
            for(int i = 0; i < selectFromHere.getVarOrdering().length;i++){
                selectFromHere.getVarOrdering()[i].setValue(varAssignment[i]);
            }
            Term[] toAdd = new Term[this.varOrdering.length];
            for(int i = 0; i < this.varOrdering.length;i++){
                toAdd[i] = this.varOrdering[i].getValue();
            }
            this.memory.addInstance(toAdd);
            // We inform all children of this node that a new instance has arrived
            for(Node child: this.children){
                child.addInstance(toAdd);
            }
        }
        
        
        
        
    }
    
    
    
    
    
}
