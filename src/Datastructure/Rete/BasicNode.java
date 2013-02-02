/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
import Entity.GlobalSettings;
import Entity.Instance;
import Entity.Predicate;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A BasicNode is the entry point of each Instance that is added to the retenetwork.
 * 
 * @param basicChildren the ChildNodes of this BasicNodes can only be SelectionNodes
 * @param toPropagate a stack storing all the instances that have already been added into the basic nodes memory, but wait for propagation
 * @param pred the predicate this BasicNode stands for. (Only needed to make the Stringrepresentation look nicer)
 * 
 */
public class BasicNode extends Node{
    
    //private Storage memory;
    protected ArrayList<SelectionNode> basicChildren;
    //private Rete rete;
    protected Stack<Instance> toPropagate;
    protected Predicate pred;
    
    
    
    /**
     * 
     * public constructor. Creates a new BasicNode with initialized data structures.
     * 
     * @param arity the arity of the predicate
     * @param rete the rete network this basicNode is in
     * @param pred the predicate this basicNode stands for
     */
    public BasicNode(int arity, Rete rete, Predicate pred){
        super(rete);
        memory.initStorage(arity);
        basicChildren = new ArrayList<SelectionNode>();
        toPropagate = new Stack<Instance>();
        this.pred = pred;
        //this.rete.getChoiceUnit().addNode(this);
    }
    
    /**
     * 
     * adds an instance to the memory and pushes it onto the stack for further propagation.
     * super.addInstance is called to register all instances added here within the DecisionMemory
     * 
     * @param instance The instance you want to add
     */
    public void addInstance(Instance instance){
        memory.addInstance(instance);
        toPropagate.push(instance);
    }
    
    /**
     * 
     * tells the basicNode to propagate. The basic Node will then go through all
     * the instances on the toPropagatestack and push them to it's children.
     * 
     * @return wether propagation could be done or not (because the stack may have been empty)
     */
    public boolean propagate(){
        boolean ret = this.toPropagate.size() > 0;
        while(!this.toPropagate.isEmpty() && rete.satisfiable){
            Instance ins = toPropagate.pop();
            //System.err.println("BasicNode Sending: " + ins);
            for(SelectionNode sN: basicChildren){
                sendInstanceToChild(ins, sN);
            }
            /*for(int i = 0; i < basicChildren.size();i++){
                basicChildren.get(i).addInstance(ins, true);
            }*/
        }
        return ret;
    }
      

    

   
    /**
     * 
     * this registers an Atom to this basicNode. The basicNode creates a selectionNode for that atom and adds it to it's children
     * 
     * @param atom the atom you want to register to this basicNode. (Has to be of same predicate as the BasicNodes predicate)
     */
    public void AddAtom(Atom atom){
        if(this.getChildNode(atom.getAtomAsReteKey()) == null){
            //System.out.println("Adding new SelectionNode!: " + atom);
            SelectionNode sel = new SelectionNode(atom.getAtomAsReteKey(), this.rete);
            //System.err.println("I am: " + this + "of size:  " + this.pred.getArity() + " - I'm adding positive selectionNode: " + sel);
            this.basicChildren.add(sel);
        }
    }
    
    /*public Collection<Instance> select(Term[] selectionCriteria){
        return memory.select(selectionCriteria);
    }
    public boolean containsInstance(Instance instance){
        return memory.containsInstance(instance);
    }
    */
    public void printAllInstances(){
        memory.printAllInstances();
    }
    
    /**
     * 
     * Overrides Nodes getChildren, because we have to work with selectionNodes as children while normal Nodes
     * can have any type of node as children. Therefore we here have a conversion from SelectionNode to Node.
     * This method is probably never used during caluclation.
     * 
     * @return a List of Nodes that are the children of this basicNode
     */
    @Override
    public ArrayList<Node> getChildren(){
        //TODO: Check if this method is ever called
        ArrayList<Node> aL = new ArrayList<Node>();
        for(SelectionNode sn: this.basicChildren){
            aL.add(sn);
        }
        return aL;
    }
    
    /**
     * 
     * returns the SelectionNode that is registered in this BasicNode with the corresponding Atom a
     * 
     * @param a the Atom for which you want the SelectionNode
     * @return The SelectionNode for Atom a
     */
    public SelectionNode getChildNode(Atom a){
        for(int i = 0; i < this.basicChildren.size();i++){
            if(this.basicChildren.get(i).getAtom().equals(a)){
                return this.basicChildren.get(i);
            }
        }
        return null;
    }
    
    /**
     * 
     * Adds a child to this BasicNode
     * 
     * @param n The Node you want to add as a child (has to be a selectionNode)
     */
    @Override
    public void addChild(Node n){
        if (!basicChildren.contains((SelectionNode)n)) {
            basicChildren.add((SelectionNode)n);
            ReteModificationHelper.getReteModificationHelper().recordNewChild(this, n);
        }
    }
    
    /**
     * 
     * @return the string representation of this basic node
     */
    @Override
    public String toString(){
        return "BasicNode: " + pred + " - Childrensize: " + this.basicChildren.size();
    }
    
    /**
     * clears the stack toPropagate. (This is needed if we run into unsatisfiability during propagation, then we want to backtrack and clear this stack).
     */
    public void resetPropagation(){
        this.toPropagate.clear();
    }
    
    /**
     * 
     * @return the corresponding predicate of this basic Node
     */
    public Predicate getPred(){
        return this.pred;
    }
    
}
