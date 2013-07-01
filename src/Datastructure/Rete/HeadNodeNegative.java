package Datastructure.Rete;

import Entity.Atom;
import Entity.GlobalSettings;
import Entity.Instance;
import Entity.Rule;
import Entity.TrackingInstance;
import Entity.Variable;
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
            
        }
    }
    
    @Override
    public String toString(){
        return "HeadNodeNegative: " + a + " - " + this.tempVarPosition;
    }
    
}
