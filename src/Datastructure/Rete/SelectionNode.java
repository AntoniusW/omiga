/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Atom;
import Entity.Variable;
import Interfaces.Term;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class SelectionNode extends Node{
    
    private Atom atom;
    
    
    
    public SelectionNode(Atom atom, Rete rete){
        super(rete);
        this.atom = atom;
        children = new ArrayList<Node>();
        
        // we calculate the variable ordering by parsing the atom for variables
        ArrayList<Variable> vars = new ArrayList<Variable>();
        for(int i = 0; i < atom.getTerms().length;i++){
            for(Variable v: atom.getTerms()[i].getUsedVariables()){
                if(!vars.contains(v)) vars.add(v);
            }
        }
        varOrdering = new Variable[vars.size()];
        vars.toArray(varOrdering);
        
        // memory is initialized with the size of the var ordering (as we only need to save variableassignments
        memory = new Storage(varOrdering.length);
        
        /*
         * This is the old code where we had the caluclation within the atom itself
        varOrdering = new Variable[atom.getVariables().size()];
        atom.getVariables().toArray(varOrdering);*/
    }
    
    /*
     * Gets an instance of a predicate as input and unifys it with the schema of the atom. The corresponding variable assignment
     * then is added to this nodes memory, and all child nodes are informed of the new instance (varAssignment)
     *
     * from == null
     */
    @Override
    public void addInstance(Term[] instance, Node from){
        //System.out.println(this + " addInstance is called with: " + Instance.getInstanceAsString(instance));
        for(Variable v: varOrdering){
            // All Variable values used in this nodes pir are set to null
            v.setValue(null);
        }
        for(int i = 0; i < instance.length; i++){
            // unifyTerm assigns values to our variables
            if(!unifyTerm(atom.getTerms()[i],instance[i])) return;
        }
        
        Term[] varAssignment2Add = new Term[varOrdering.length];
        for(int i = 0; i < varOrdering.length;i++){
            // we create our variable assignment by taking all the values of the variables of our varOrdering.
            varAssignment2Add[i] = varOrdering[i].getValue();
        }
        this.memory.addInstance(varAssignment2Add);
        
        for(Node n: this.children){
            // we transfer the inserted varAssignment to all childnodes
            n.addInstance(varAssignment2Add, this);
        }
        
        
    }
    
    /*
     * 
     */
    private boolean unifyTerm(Term schema, Term instance){
        if (schema.isVariable()){
            Variable v = (Variable)schema;
            if(v.getValue() == null ){
                v.setValue(instance);
            }else{
                // if the value of the variable is already assigned we return true if it is the actual term of the instance, false otherwise
                return v.getValue().equals(instance);
            }
        }else{
            if(schema.isConstant()){
                return schema.equals(instance);
            }else{
                // schema is FuncTerm
                if(schema.getName().equals(instance.getName())){ // TOCHECK: Stringvergleich
                    for(int i = 0; i < (schema).getChildren().size();i++){
                        unifyTerm(schema.getChildren().get(i), instance.getChildren().get(i));
                    }
                }
                
            }
        }
        return true;
    } 
    
    @Override
    public String toString(){
        return "SelectionNode " + this.atom;
    }
    
}
