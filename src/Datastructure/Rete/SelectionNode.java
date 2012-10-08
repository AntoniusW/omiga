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
    
    protected Atom atom;
    //boolean neg = true; // TODO: Remove neg when it works
    
    /*public void setNeg(){
        this.neg = false;
    }*/
    
    public Atom getAtom(){
        return atom;
    }
    
    
    /*public SelectionNode(Rete rete){
        super(rete);
    }*/
    /**
     * 
     * public constructor. Creates a new SelectionNode with initialized data structures.
     * 
     * @param atom The atom for which you want to create a selectionNode. Please be aware that the selectionNode will actually be created for the atoms reteKey,
     *        such that we do not get different nodes for p(X,Y) and p(A,B).
     * @param rete the rete network this selectionNode is in
     */
    public SelectionNode(Atom atom, Rete rete){
        super(rete);
        //System.err.println("Creating SelectionNode: " + atom);
        
        this.atom = atom.getAtomAsReteKey(); 
        this.tempVarPosition = getVarPosition(atom); //super.resetVarPosition(atom); 
        
        // we create the variableOrdering
        ArrayList<Variable> vars = new ArrayList<Variable>();
        for(int i = 0; i < atom.getAtomAsReteKey().getTerms().length;i++){
            for(Variable v: atom.getAtomAsReteKey().getTerms()[i].getUsedVariables()){
                if(!vars.contains(v)) vars.add(v);
            }
        }
        varOrdering = new Variable[vars.size()];
        vars.toArray(varOrdering);
        //System.err.println("VAR ORDERING: " + this);
        /*for(Variable v: varOrdering){
            System.err.println(v);
        }*/
        
        // memory is initialized with the size of the var ordering (as we only need to save variableassignments
        memory = new Storage(atom.getArity());
        //System.err.println("SelectionNode Created!: " + atom + " memory: " + this.memory);
        //this.rete.getChoiceUnit().addNode(this);
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
    public void addInstance(Instance instance, boolean from){
        //System.err.println("Trying to add instance: " + instance);
        
        if(varOrdering.length == 0){
            // if this node encapsulates a fact atom
            Instance instance2Add = Instance.getInstance(this.atom.getTerms());
            this.memory.addInstance(instance2Add);
            super.addInstance(instance2Add, from);
            
            for(int i = 0; i < this.children.size();i++){
                children.get(i).addInstance(instance2Add, false);
            }
            for(int i = 0; i < this.childrenR.size();i++){
                childrenR.get(i).addInstance(instance2Add, true);
            }
            
            return;
        }
        
        for(int i = 0; i < varOrdering.length;i++){
            // All Variable values used in this nodes atom are set to null
            varOrdering[i].setValue(null);
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
        /*System.err.println("I am: " + this);
        System.err.println("SIZE of varOrderong: "  + varOrdering.length);
        System.err.println("SIZE of varAss: " + varAssignment2Add.length);
        System.err.println("varAssighnment2Add: " + Instance.getInstanceAsString(varAssignment2Add));*/
        Instance instance2Add = Instance.getInstance(varAssignment2Add);
        super.addInstance(instance2Add, true); // registering the adding of an instance within the choiceUnit
        //System.err.println(this + " Adding Instance: " + instance2Add);
        this.memory.addInstance(instance2Add);
        
        // we transfer the inserted varAssignment to all childnodes
        /*for(Node n: this.children){
            n.addInstance(instance2Add,false);
        }
        for(Node n: this.childrenR){
            n.addInstance(instance2Add,true);
        }*/
        
        for(int i = 0; i < this.children.size();i++){
            children.get(i).addInstance(instance2Add, false);
        }
        for(int i = 0; i < this.childrenR.size();i++){
            childrenR.get(i).addInstance(instance2Add, true);
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
    protected boolean unifyTerm(Term schema, Term instance){
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
    
    /**
     * 
     * @param atom The atom for which you want to get the VariablePositions
     * @return a HashMap that contains Variables together with their position in the memory in the corresponding SelectionNode of the given atom
     */
    public static HashMap<Variable,Integer> getVarPosition(Atom atom){
        //System.out.println("GetVarPos Atom: " + atom);
        HashMap<Variable,Integer> ret = new HashMap<Variable,Integer>();
        Term[] terms = atom.getTerms();
        for(int i = 0; i < terms.length;i++){
            Term t = terms[i];
            for(int j = 0;j < t.getUsedVariables().size();j++){
                Variable v = t.getUsedVariables().get(j);
                if(!ret.containsKey(v)){
                    ret.put(v, ret.size());
                }
            }
        }
        //System.out.println("Ret: " + ret);
        return ret;
    }
    
    public HashMap<Variable,Integer> getVarPosition(){
        // This method must not be used for selectionNodes since the VariablePositions are useless here since more 
        System.err.println("EROOR: getVarPositions called for a SelectionNode!");
        return null;
    }
    
}
