/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.Constant;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Rule;
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
public class HeadNode extends Node{
    
    protected Atom a;
    protected int[] instanceOrdering;
    Node from;
    public Rule r; // debug stuff, set in the retebuilder
    
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
        //System.err.println("Head Node for: " + atom);
        //this.tempVarPosition = (HashMap<Variable, Integer>) n.getVarPositions().clone(); // As we have one headNode per Rule, we can use the tempVarPosition of the parentNode for unification
        this.tempVarPosition = varPos;
        //System.err.println("Tempvars: " + tempVarPosition);
        //System.err.println("HeadNode Created!: " + this);
        //this.rete.getChoiceUnit().addNode(this);
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
       //System.err.println("HeadNode instance reached: " + instance + " FROM: " + from + " Atom: " + this.a);
        //System.out.println(this.tempVarPosition);
        for(int i=0; i < this.children.size();i++){
        // the only children of HeadNodes are choice Nodes --> We remove the actual Instance from the choice Node
        // The instance should match the instance of the choice Node, since after the positive Part + Operators have been apllied no more Variables are added to the assignment
            //System.err.println("normal headNode: " + this);
            //System.out.println("HeadNode: " + this + " removes something becasue of: " + instance);
            this.children.get(i).removeInstance(instance);
        }
        if(a == null){
            // This head Node is a constraint Node. If something arrives here the context is unsatsifiable!
            rete.satisfiable = false;
            //System.out.println("UNSATISFIABLE!: " + instance + " Rule: " + r);
            //rete.printAnswerSet();
            //rete.printAnswerSet();
            return;
        }
        //We unify the headAtom of the corresponding rule
       //System.err.println(this + "Atom: " + a + " - instance: " + instance + "tempVarPos: " + tempVarPosition);
        Instance instance2Add = Unifyer.unifyAtom(a, instance, tempVarPosition);
        
        /*Term[] headInstance = new Term[a.getArity()];
        for(int i = 0; i < a.getArity();i++){
            Term t = a.getTerms()[i];
            if(t.getClass().equals(Variable.class)){
                    headInstance[i] = instance.get(this.tempVarPosition.get((Variable)t));
                }else{
                    if (t.getClass().equals(Constant.class)){
                        headInstance[i] = t;
                    }else{
                        //System.err.println("unifyTerm from head");
                        headInstance[i] = this.unifyFuncTerm((FuncTerm)t, instance);
                    }
            }  
        }
        
        Instance instance2Add = Instance.getInstance(headInstance);*/
        //System.out.println("HEAD: " + instance2Add);
        
        /*if(!rete.containsInstance(a.getPredicate(), instance2Add, true) != !rete.containsInstance(a, instance2Add, true)){
            System.out.println("DANGER1");
            System.out.println("1 says: " + rete.containsInstance(a.getPredicate(), instance2Add, true));
            System.out.println("2 says: " + rete.containsInstance(a, instance2Add, true));
            System.out.println("for: " + a + ": " + instance2Add);
            rete.printAnswerSet();
        }
        if(rete.containsInstance(a.getPredicate(), instance2Add, false) != rete.containsInstance(a, instance2Add, false)){
            System.out.println("DANGER2");
            System.out.println("1 says: " + rete.containsInstance(a.getPredicate(), instance2Add, false));
            System.out.println("2 says: " + rete.containsInstance(a, instance2Add, false));
            System.out.println("for: " + a + ": " + instance2Add);
            rete.printAnswerSet();
        }
        
        if(!rete.containsInstance(a, instance2Add, true)){
            if(!rete.containsInstance(a, instance2Add, false)){
                //if the resolved instance is not contained within our rete we add it
                rete.addInstancePlus(a.getPredicate(),instance2Add);
                //System.out.println("HeadNode added: " + this.a + " : " + instance2Add);
                //System.err.println("HEADNODE ADDING: " + instance2Add + "because this was added: " + instance + " and Atom of head is: " + a);
            }else{
                //if the resolved instance is contained in the outset of our rete, we derive UNSATISFIABLE.
                System.out.println("UNSATISFIABLE!: muhu: " + rete.getChoiceUnit().getDecisionLevel()+ " instance: " + instance + " Atom: " + a);
                System.out.println("HeadNode Of: " + this.from);
                System.out.println("Because Instance: " + instance2Add + " for " + a.getPredicate() + " ha sbeen dervied");
                System.out.println("Rete contains this negativly??: " + rete.containsInstance(a, instance2Add, false));
                rete.printAnswerSet();
                this.rete.satisfiable = false;
            }
        }*/
        if(!rete.containsInstance(a.getPredicate(), instance2Add, true)){
            if(!rete.containsInstance(a.getPredicate(), instance2Add, false)){
                //if the resolved instance is not contained within our rete we add it
                rete.addInstancePlus(a.getPredicate(),instance2Add);
                //System.out.println("HeadNode added: " + this.a + " : " + instance2Add);
                //System.out.println("HEADNODE ADDING: " + instance2Add + "because this was added: " + instance + " and Atom of head is: " + a);
            }else{
                //if the resolved instance is contained in the outset of our rete, we derive UNSATISFIABLE.
                /*System.out.println("UNSATISFIABLE!: muhu: " + rete.getChoiceUnit().getDecisionLevel()+ " instance: " + instance + " Atom: " + a);
                System.out.println("Wanted to add: " + instance2Add + " to " + a);
                System.out.println("TEMPVAR: " + this.from.tempVarPosition);
                System.out.println("SelCrit1: " + Instance.getInstanceAsString(((JoinNode)this.from).selectionCriterion1));
                System.out.println("SelCrit2: " + Instance.getInstanceAsString(((JoinNode)this.from).selectionCriterion2));*/
                //System.out.println(rete.getChoiceUnit().getMemory().getNodes());
                //System.out.println("HeadNode Of: " + this.from);
                //rete.printAnswerSet();
                //System.out.println("UNSATISFIABLE because of HeadNode: " + " instance: " + instance + " Atom: " + a);
                this.rete.satisfiable = false;
            }
        }/*else{
            System.out.println("Instance: " + instance2Add + " already contained in rete: " + this.a);
        }*/
    }
    
    /*private FuncTerm unifyFuncTerm(FuncTerm f, Instance instance){
        //System.err.println("Unifying Head Instance: " + f + " vs. " + instance);
        ArrayList<Term> fchildren = new ArrayList<Term>();        
        for(int i = 0; i < f.getChildren().size();i++){
            Term t = f.getChildren().get(i);
            if(t.getClass().equals(Variable.class)){
                    fchildren.add(instance.get(this.tempVarPosition.get((Variable)t)));
            }else{
                if (t.getClass().equals(Constant.class)){
                    fchildren.add(t);
                }else{
                    fchildren.add(unifyFuncTerm((FuncTerm)t, instance));
                }
            }
        }
        FuncTerm ret = FuncTerm.getFuncTerm(f.getName(), fchildren);
        //System.err.println("Tehrefore returning: " + ret + " as children are: " + ret.getChildren());
        return ret;
    }*/
    
    @Override
    public String toString(){
        return "HeadNode: " + a + " - " + this.tempVarPosition;
    }
    
}
