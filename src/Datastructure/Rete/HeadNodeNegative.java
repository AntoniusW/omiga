/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.GlobalSettings;
import Entity.Instance;
import Entity.Rule;
import Entity.TrackingInstance;
import Entity.Variable;
import Interfaces.Term;
import Learning.GraphLearner;
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
    public void addInstance(Instance instance){
        if( GlobalSettings.debugOutput) {
        System.out.print("Rule fires: "+instance);
        //if(instance.isMustBeTrue) System.out.print("MBT");
        //if(r.getHeadType() == Rule.HEAD_TYPE.must_be_true) System.out.print("rMBT");
        if(r.getHeadType() == Rule.HEAD_TYPE.negative) System.out.print("-");
        System.out.println(" "+r);
        }
        Instance instance2Add = Unifyer.unifyAtom(a, instance, tempVarPosition);
        
        
        if(!rete.containsInstance(a.getPredicate(), instance2Add, true)){
            if(!rete.containsInstance(a.getPredicate(), instance2Add, false)){
    
                TrackingInstance inst = new TrackingInstance(instance2Add.getTerms(),
                          /*0, rete.getChoiceUnit().getDecisionLevel()*/
                        instance2Add.propagationLevel + 1, instance.decisionLevel); // track rule origin
                inst.setCreatedByRule(r);
                inst.setCreatedByHeadNode(this);
                inst.setFullInstance(instance);
                //inst.setDecisionLevel(GlobalSettings.getGlobalSettings().
                //        getManager().getContext().getChoiceUnit().getDecisionLevel());
                rete.addInstanceMinus(a.getPredicate(), inst);

                //rete.addInstanceMinus(a.getPredicate(),instance2Add);
                //System.out.println("HeadNodeNegative adds: " + instance2Add + " because of: " + instance);
            }
        }else{
//            System.out.println("HeadNodeNegative makes inconsistent: "+r+" "+instance2Add);
            //this.rete.satisfiable = false;
            // let constraint handle this inconsistency, add instance
            TrackingInstance inst = new TrackingInstance(instance2Add.getTerms(),
                        /*0, rete.getChoiceUnit().getDecisionLevel()*/
                        instance2Add.propagationLevel + 1, instance.decisionLevel); // track rule origin
                inst.setCreatedByRule(r);
                inst.setCreatedByHeadNode(this);
                inst.setFullInstance(instance);
                //inst.setDecisionLevel(GlobalSettings.getGlobalSettings().
                //        getManager().getContext().getChoiceUnit().getDecisionLevel());
                rete.addInstanceMinus(a.getPredicate(), inst);
            
/*          // TODO: avoid below generation of constraint, add these directly in the rewriting!
            // create a new constraint of the form:
            // :- hd_r(X,Y,Z), -hd_r(X,Y,Z).
            Rule constraint = new Rule();
            int arity = r.getHead().getArity();
            Term[] vars = new Term[arity];
            for (int i = 0; i < vars.length; i++) {
                vars[i] = Variable.getVariable("cVar:"+i);
            }
            Atom head_allvars = Atom.getAtom(r.getHead().getName(), arity, vars);
            constraint.addAtomMinus(head_allvars);
            constraint.addAtomPlus(head_allvars);
            System.out.println("Creating new constraint: "+constraint);
            GraphLearner gl = new GraphLearner();
            gl.learnRuleAndAddToRete(constraint, instance2Add, this);
*/            
        }
    }
    
    @Override
    public String toString(){
        return "HeadNodeNegative: " + a + " - " + this.tempVarPosition;
    }
    
}
