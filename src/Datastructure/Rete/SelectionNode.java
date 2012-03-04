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
 * @author Gerald Weidinger 0526105
 * 
 * A SelectionNode represents the memory for an atom. It's child of a basicnode, and saves all instances
 * that match it's atom.
 * 
 * @param atom the Atom this selectionNode stands for
 * 
 * 
 * 
 */
public class SelectionNode extends Node{
    
    private Atom atom;
    
    public Atom getAtom(){
        return atom;
    }
    
    /**
     * 
     * public constructor. Creates a new SelectionNode with initialized data structures.
     * 
     * @param atom The atom for which you want to create a selectionNode. Please be aware that the selectionNode will actually be created for the atoms reteKey,
     *        such that we do not get different nodes for p(X,Y) and p(A,B).
     * @param rete the rete network this selectionNode is in
     */
    public SelectionNode(Atom atom, Rete rete){
        super(rete); // register within the choiceUnit
        
        this.atom = atom.getAtomAsReteKey(); 
        super.resetVarPosition(atom);
        
        // we create the variableOrdering
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
        //System.err.println("SelectionNode Created!: " + atom + " memory: " + this.memory);
    }
    
    /**
     * 
     * SelectionNodes are only children of basicNodes. SO only basicNodes should call this method.
     * The Basic node pushes it's instance (which actuall is an instance of a predicate) to the selectionNode.
     * The selectionNode takes this instance, and unifys it with it's atom. It only adds that unified
     * Variable assignment to it's memory, and pushes this variable assignment to all it's children,.
     * 
     * Please be ware that this unification is not the same as used within the Unifyer class, as we here
     * unify an atom with an actual instance, while the Unifyer unifys an atom with a variable assignment.
     * This kind of unification is not used anywhere else. Therefore it's implemented within the selectionNode.
     * 
     * @param instance The instance you want to add
     * @param from not used, only needed for extending SelectionNode
     */
    @Override
    public void addInstance(Instance instance, Node from){
        super.addInstance(instance, this); // registering the adding of an instance within the choiceUnit

        for(Variable v: varOrdering){
            // All Variable values used in this nodes atom are set to null
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
    
    /**
     * 
     * unifies a Term with another Term, and return wether those two are equal.
     * To do this this methods needs the values of class variable, which are set during unification.
     * 
     * @param schema The term of the atom
     * @param instance the term of the instance
     * @return wether both terms are equal
     */
    private boolean unifyTerm(Term schema, Term instance){
        if (schema.getClass().equals(Variable.class)){
            Variable v = (Variable)schema;
            if(v.getValue() == null ){
                v.setValue(instance);
            }else{
                // if the value of the variable is already assigned we return true if it is the actual term of the instance, false otherwise
                return v.getValue().equals(instance);
            }
        }else{
            if(schema.getClass().equals(Constant.class)){
                //if the instance's constant at that position equals that of the atom return true, otherwise false
                return schema.equals(instance);
            }else{
                // schema is FuncTerm
                if(schema.getName().equals(instance.getName())){ // TOCHECK: Stringvergleich
                    //if the schema's term at this posiion is a function term we have to call UnifyTerm for all it's children
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
    
    /**
     * 
     * @return the string representation of this selection Node.
     */
    @Override
    public String toString(){
        return "SelectionNode " + this.atom;
    }
    
}
