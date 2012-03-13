/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.Instance;
import Entity.Predicate;

/**
 *
 * @author g.weidinger
 */
public class BasicNodeNegative extends BasicNode{
    
    private boolean closed;
    
    public BasicNodeNegative(int arity, Rete rete, Predicate pred){
        super(arity,rete,pred);
        this.closed = false;
        System.err.println("BasicNodeNegative created!: " + pred);
    }
    
    /**
     * 
     * @param instance the instance you want to check for
     * @return wether the instance is saved within the memory or not
     * 
     * if the node is closed it this method asks the corresponding positive BasicNode
     * and returns true if that node does not contain this instance, false otherwise.
     */
    @Override
    public boolean containsInstance(Instance instance){
        if(closed){
            return !rete.containsInstance(this.pred, instance, true);
        }
        return memory.containsInstance(instance);
    }
    
    /**
     * closes this node and informs all children of this closure (can lead to many instances at once)
     * Be aware that you have to propagate after a closure to derive all facts that arise from this closure,
     * since closing a node only leads to the calculation of facts directly following this node,
     * but all derived facts are put on the propagation stack!
     */
    public void close(){
        System.err.println(this + " --> closed");
        this.closed = true;
        //for(SelectionNode sN: this.basicChildren){
        SelectionNode sN;
        for(int j = 0; j < this.basicChildren.size();j++){
            sN = this.basicChildren.get(j);
            /*Closure only comes from right
            //
            for(int i = 0; i < sN.getChildren().size();i++){
                if(sN.getChildren().get(i).getClass().equals(JoinNode.class)){
                    System.err.println("Closed --> Inform JoinNode left");
                    JoinNode jn = (JoinNode)sN.getChildren().get(i);
                    jn.informOfClosure(sN,false);
                }else{
                //cant be a headnode since only negative nodes are closed, which must result in a joinNode. If it results in a headnode the rule would not be save
                    // If it is not a jonnode it is an Operator Node or an ReductionNode (These nodes are planned for optimisation/nicer handling but not implemented yet!)
                }
            }*/
            for(int i = 0; i < sN.getChildrenR().size();i++){
                if(sN.getChildrenR().get(i).getClass().equals(JoinNode.class)){
                    //System.err.println("Closed --> Inform JoinNode right");
                    JoinNode jn = (JoinNode)sN.getChildrenR().get(i);
                    jn.informOfClosure(sN,true);
                }else{
                //cant be a headnode since only negative nodes are closed, which must result in a joinNode. If it results in a headnode the rule would not be save
                    // If it is not a jonnode it is an Operator Node or an ReductionNode (These nodes are planned for optimisation/nicer handling but not implemented yet!)
                }
            }
        }
    }
    
    /**
     * uncloses a node. Since factretracting is done by the backtracking of the choiceUnit we just have to mark this node as not closed
     */
    public void unclose(){
        this.closed=false;
    }
    
    /**
     * 
     * @return the string representation of this basic node
     */
    @Override
    public String toString(){
        if(closed){
            return "BasicNodeNeg: " + pred + " - Childrensize: " + this.basicChildren.size() + " - closed";
        }
        return "BasicNodeNeg: " + pred + " - Childrensize: " + this.basicChildren.size() + " - open";
    }
    
        /**
     * 
     * this registers an Atom to this basicNode. The basicNode creates a selectionNode for that atom and adds it to it's children
     * 
     * @param atom the atom you want to register to this basicNode. (Has to be of same predicate as the BasicNodes predicate)
     */
    @Override
    public void AddAtom(Atom atom){
        if(this.getChildNode(atom.getAtomAsReteKey()) == null){
            System.out.println("Adding new SelectionNode!: " + atom);
            SelectionNode sN = new SelectionNode(atom.getAtomAsReteKey(), this.rete);
            this.basicChildren.add(sN);
            sN.setNeg();
            //this.basicChildren.add(new SelectionNode(atom.getAtomAsReteKey(), this.rete));
        }
    }
    
    public boolean isClosed(){
        return closed;
    }
    
}
