package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Gerald Weidinger, Antonius Weinzierl
 * 
 * Abstract Class Node, is the father class of all other nodes of which the rete network is built.
 * 
 * @param memory the memory where this node saves it's instances
 * @param varOrdering defines which position of the stored variable assignments belongs to which variable
 * @param children the child-nodes of this node
 * @param rete the retenetwork the node is in
 * @param tempVarPosition used to build the rete. It's a mapping which position of an instance belongs to which variable
 * 
 */
public abstract class Node {
    
    protected Variable[] varOrdering;
    public Storage memory;
    protected ArrayList<Node> children;
    public Rete rete;
    protected HashMap<Variable,Integer> tempVarPosition;
    
    public static HashSet<Node> nodes = new HashSet<Node>();
    
    public Rule stemRule;
    
    /**
     * 
     * @return the tempVarPosition Mapping of this node.
     */
    public HashMap<Variable,Integer> getVarPositions(){
        //TODO remove this method --> Extra treatment for choiceNOdes needed then, but tempVars should not be used anymore!
        //KEEP removal requires changes to GraphLearner/learning
        return this.tempVarPosition;
    }
    
    /**
     * 
     * resets the tempVarPosition mapping by removing everything and adding those variables of the current atom.
     * 
     * @param atom 
     */
    public void resetVarPosition(Atom atom){
        //System.err.println("resetVarPosition: " + this);
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
    
    
    /**
     * 
     * public constructor. Creates a new Node with initialized data structures.
     * Also adds this node into the ChoiceUnits DecisionMemory, such that,
     * when backtracking, added instances of higher decision levels are removed again.
     * 
     * @param rete the rete network this node is in
     */
    public Node(Rete rete){
        this.rete = rete;
        this.children = new ArrayList<Node>();
        tempVarPosition = new HashMap<Variable,Integer>();
        this.rete.getChoiceUnit().addNode(this);
        nodes.add(this);
        memory = new Storage();
        this.stemRule = ReteBuilder.stemRule;   // hack for easier debugging
    }
    
    /**
     * 
     * @return the variable ordering of this node
     */
    public Variable[] getVarOrdering(){
        return varOrdering;
    }
    
    /**
     * 
     * selects from the memory the desired instances. (See Storage Class for details on how the selectionCriteria works)
     * 
     * @param selectionCriteria the selectionCriteria for this selection
     * @return all instances that satisfy the selectionCriteria and are contained within this memory
     */
    public Collection<Instance> select(Term[] selectionCriteria){
        //System.out.println("Selecting from node: " + this + " - " + Instance.getInstanceAsString(selectionCriteria));
        return memory.select(selectionCriteria);
    }
    
    
    /**
     * 
     * Adds an instance into the memory, without registering this instance in the Decision Memory
     * 
     * @param instance the instance you want to add to this nodes memory
     */
    public void simpleAddInstance(Instance instance){
        this.memory.addInstance(instance);
    }
        
    /**
     * 
     * @param instance the instance you want to check for
     * @return wether the instance is saved within the memory or not
     */
    public boolean containsInstance(Instance instance){
        return memory.containsInstance(instance);
    }
    
    /**
     * 
     * @return the children of this node
     */
    protected ArrayList<Node> getChildren(){
        return children;
    }

    /**
     * 
     * Adds a child to this node
     * 
     * @param n the node you want to add as a child for this node
     */
    public void addChild(Node n){
        if (!children.contains(n)) {
            children.add(n);
            ReteModificationHelper.getReteModificationHelper().recordNewChild(this, n);
        }
    }
    
    public Storage getMemory(){
        return memory;
    }
    
    /**
     * Propagates each stored instance to the indicated children.
     * @param children the list of children to send the instances to
     */
    protected void propagateToChildren(ArrayList<Node> children) {
        ArrayList<Instance> all_inst = memory.getAllInstances();
        for (Node child : children) {
            for (Instance instance : all_inst) {
                sendInstanceToChild(instance, child);
            }
        }
    }
    
    /**
     * Tests whether child is a JoinNode and calls addInstance with respective
     * parameters.
     *
     * @param inst
     * @param child
     */
    protected void sendInstanceToChild(Instance inst, Node child) {
        if (child instanceof JoinNode) {
            ((JoinNode) child).addInstance(inst, this);
        } else {
            child.addInstance(inst);
        }
    }

    /**
     * Adds an instance to this node; Node only stores the instance in memory.
     *
     * @param instance
     */
    public abstract void addInstance(Instance instance);

    /**
     * Backtracks its local memory (removes all instances greater than
     * decisionLevel).
     *
     * @param decisionLevel
     */
    public void backtrackTo(int decisionLevel) {
        memory.backtrackTo(decisionLevel);
    }
}
