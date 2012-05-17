/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.Constant;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A HeadNode represents the fullfillment of a rule. A HeadNode is a node that does not story any instances. 
 * Whenever an instance is added to a headNode the Atom is unified and the resulting instance then added to the rete.
 * 
 * 
 * @param a the atom that is head of the corresponding rule
 * @param 
 */
public class HeadNodeNegative extends HeadNode{
    

    
    /**
     * 
     * public constructor. Creates a new Headnode with initialized data structures.
     * 
     * @param atom the atom that is head of the corresponding rule
     * @param rete the rete network this node is in
     * @param n The parent node of this headNode
     */
    public HeadNodeNegative(Atom atom, Rete rete, HashMap<Variable,Integer> varPos, Node from){
        super(atom,rete,varPos,from);
    }
    
    /**
     * If an instance is added to a headNode then this means the body of a rule has been
     * satisfied. We therefore create a new instance of the head via the variable assignment
     * that is still left from the last node.
     * 
     * @param instance the instance that is added
     * @param from only needed for extending class Node
     */
    @Override
    public void addInstance(Instance instance, boolean from){
        Instance instance2Add = Unifyer.unifyAtom(a, instance, tempVarPosition);
        
        
        if(!rete.containsInstance(a.getPredicate(), instance2Add, true)){
            if(!rete.containsInstance(a.getPredicate(), instance2Add, false)){
                rete.addInstanceMinus(a.getPredicate(),instance2Add);
                //System.out.println("HeadNodeNegative adds: " + instance2Add + " because of: " + instance);
            }
        }else{
            System.out.println("HeadNodeNegative kills Sat!");
            this.rete.satisfiable = false;
        }
    }
    
    @Override
    public String toString(){
        return "HeadNodeNegative: " + a + " - " + this.tempVarPosition;
    }
    
}
