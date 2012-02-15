/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Atom;
import Entity.Constant;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class SelectionNode extends Node{
    
    private Atom atom;
    
    HashMap<Variable,Integer> tempVarPosition;
    int[] instanceOrdering;
    
    public Atom getAtom(){
        return atom;
    }
    
    
    public SelectionNode(Atom atom, Rete rete){
        super(rete);
        //System.out.println("SelectionNode Created!: " + atom);
        this.atom = atom.getAtomAsReteKey();
        children = new ArrayList<Node>();
        tempVarPosition = new HashMap<Variable,Integer>();
        System.err.println(atom);
        resetVarPosition(atom);
        
        ArrayList<Variable> vars = new ArrayList<Variable>();
        for(int i = 0; i < atom.getAtomAsReteKey().getTerms().length;i++){
            for(Variable v: atom.getAtomAsReteKey().getTerms()[i].getUsedVariables()){
                if(!vars.contains(v)) vars.add(v);
            }
        }
        varOrdering = new Variable[vars.size()];
        vars.toArray(varOrdering);
        
        // memory is initialized with the size of the var ordering (as we only need to save variableassignments
        memory = new Storage(atom.getArity());
        
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
    public void addInstance(Instance instance, Node from){
        //System.err.println(this + " " + this.children.size() +  " addInstance is called with: " + instance);
        for(Variable v: varOrdering){
            // All Variable values used in this nodes pir are set to null
            v.setValue(null);
        }
        for(int i = 0; i < instance.getSize(); i++){
            // unifyTerm assigns values to our variables
            if(!unifyTerm(atom.getTerms()[i],instance.get(i))) {
                return;
            }
        }
        
        Term[] varAssignment2Add = new Term[varOrdering.length];
        for(int i = 0; i < varOrdering.length;i++){
            // we create our variable assignment by taking all the values of the variables of our varOrdering.
            varAssignment2Add[i] = varOrdering[i].getValue();
        }
        Instance instance2Add = Instance.getInstance(varAssignment2Add);
        this.memory.addInstance(instance2Add);
        
        
        for(Node n: this.children){
            // we transfer the inserted varAssignment to all childnodes
            n.addInstance(instance2Add, this);
        }
        
        
    }
    
    /*
     * 
     */
    private boolean unifyTerm(Term schema, Term instance){
        //System.err.println("Unify!");
        if (schema.getClass().equals(Variable.class)){
            //System.err.println("SCHWEMA-VARIABLE: " + schema + " vs instance: " + instance);
            Variable v = (Variable)schema;
            if(v.getValue() == null ){
                v.setValue(instance);
            }else{
                // if the value of the variable is already assigned we return true if it is the actual term of the instance, false otherwise
                return v.getValue().equals(instance);
            }
        }else{
            if(schema.getClass().equals(Constant.class)){
                //System.err.println("SCHWEMA-Constant: " + schema + " vs instance: " + instance);
                return schema.equals(instance);
            }else{
                // schema is FuncTerm
                if(schema.getName().equals(instance.getName())){ // TOCHECK: Stringvergleich
                    for(int i = 0; i < ((FuncTerm)schema).getChildren().size();i++){
                        if(!unifyTerm(((FuncTerm)schema).getChildren().get(i), ((FuncTerm)instance).getChildren().get(i))) return false;
                    }
                }else{
                    return false;
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
