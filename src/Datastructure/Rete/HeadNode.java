/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
import Entity.GlobalSettings;
import Entity.Instance;
import Entity.Rule;
import Entity.TrackingInstance;
import Entity.Variable;
import Learning.GraphLearner;
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
public class HeadNode extends Node{
    
    protected Atom a;
    //protected int[] instanceOrdering; // AW: unused member?
    public Node from;
    public Rule r; // tracking the origin of derived instances, set in retebuilder
    
    /**
     * 
     * public constructor. Creates a new Headnode with initialized data structures.
     * 
     * @param atom the atom that is head of the corresponding rule
     * @param rete the rete network this node is in
     * @param n The parent node of this headNode
     */
    @SuppressWarnings("unchecked") // AW: workaround for array conversion
    public HeadNode(Atom atom, Rete rete, HashMap<Variable,Integer> varPos, Node from){
        super(rete);
        this.from = from;
        this.a = atom;
        this.tempVarPosition = varPos;
        //memory = new Storage(atom.getArity());
    }
    
    /**
     * If an instance is added to a headNode then this means the body of a rule has been
     * satisfied. We therefore create a new instance of the head via the variable assignment
     * that is still left from the last node.
     * 
     * @param instance the instance that is added
     */
    @Override
    public void addInstance(Instance instance){
        if( GlobalSettings.debugOutput ) {
            System.out.print("Rule fires: "+instance);
            if(instance.isMustBeTrue) System.out.print("MBT");
            if(r.getHeadType() == Rule.HEAD_TYPE.must_be_true) System.out.print("rMBT");
            if(r.getHeadType() == Rule.HEAD_TYPE.negative) System.out.print("-");
            System.out.println(" "+r);
        }
//        System.out.println("HeadNode "+this.toString()+" adding instance "+instance.toString()+" @DL="+instance.decisionLevel);
       //System.err.println("HeadNode instance reached: " + instance + " FROM: " + from + " Atom: " + this.a);
        //System.out.println(this.tempVarPosition);
        for(int i=0; i < this.children.size();i++){
        // the only children of HeadNodes are choice Nodes --> We remove the actual Instance from the choice Node
        // The instance should match the instance of the choice Node, since after the positive Part + Operators have been apllied no more Variables are added to the assignment
            //this.children.get(i).removeInstance(instance);
            ((ChoiceNode)children.get(i)).disableInstance(instance, /*instance.decisionLevel*/rete.getChoiceUnit().getDecisionLevel());
        }
        if(a == null){
            // This head Node is a constraint Node. If something arrives here the context is unsatsifiable!
            System.out.println("Constraint violated: "+r+" "+instance.toString());
            GraphLearner gl = new GraphLearner();
            gl.learnRuleFromConflict(r, instance, this, rete);
//            gl.learnRuleAndAddToRete(r, instance, this);
            //rete.satisfiable = false;
            return;
        }
        //We unify the headAtom of the corresponding rule
       //System.err.println(this + "Atom: " + a + " - instance: " + instance + "tempVarPos: " + tempVarPosition);
        Instance instance2Add = Unifyer.unifyAtom(a, instance, tempVarPosition);
        
        if (GlobalSettings.debugOutput) {
            if (!instance2Add.isMustBeTrue
                    && r.getHeadType() != Rule.HEAD_TYPE.must_be_true
                    && rete.getBasicLayerPlus().get(a.getPredicate()).memory.containsMustBeTrueInstance(instance2Add)) {
                System.out.println("NonMBT instance overwrites MBT: " + instance2Add + " " + r);
            }
        }
        
        // if instance is not contained or only contained as MustBeTrue and we add as non-mbt
        if(!rete.containsInstance(a.getPredicate(), instance2Add, true)
                || (!instance2Add.isMustBeTrue && r.getHeadType() != Rule.HEAD_TYPE.must_be_true
                && rete.getBasicLayerPlus().get(a.getPredicate()).memory.containsMustBeTrueInstance(instance2Add)) 
          ){
            //if(!rete.containsInstance(a.getPredicate(), instance2Add, false)){
                //if the resolved instance is not contained within our rete we add it
                //int currentDL = GlobalSettings.getGlobalSettings().
                //        getManager().getContext().getChoiceUnit().getDecisionLevel();
                TrackingInstance inst = new TrackingInstance(instance2Add.getTerms(),
                                                /*0, rete.getChoiceUnit().getDecisionLevel()*/
                                                instance.propagationLevel+1, instance.decisionLevel); // track rule origin
                inst.setCreatedByRule(r);
                inst.setCreatedByHeadNode(this);
                inst.setFullInstance(instance);
                inst.isMustBeTrue = instance.isMustBeTrue;
                // if this HeadNode is for a MBT rule, then created instances are mbt
                if( r.getHeadType() == Rule.HEAD_TYPE.must_be_true ){
                    inst.isMustBeTrue = true;
                }
                rete.addInstancePlus(a.getPredicate(),inst);
            //}else{
                //if the resolved instance is contained in the outset of our rete, we derive UNSATISFIABLE.
//                GraphLearner gl = new GraphLearner();
//                gl.learnRuleAndAddToRete(r, instance, this);
                //this.rete.satisfiable = false;
            //}
        }
    }

    /**
     * HeadNodes have no memory, hence nothing to do for backtracking.
     * @param decisionLevel 
     */
    @Override
    public void backtrackTo(int decisionLevel) {
    }
    
    
    
    @Override
    public String toString(){
        return "HeadNode: " + a + " - " + this.tempVarPosition;
    }
    
}
