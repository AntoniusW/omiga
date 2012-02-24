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
 * @author Mika
 * 
 * Maybe we have to execute HeadNodes as soon as an instance reaches a node... but i dont think so
 * if so we have to use a childlist of headnodes
 */
public class HeadNode extends Node{
    
    Atom a;
    int[] instanceOrdering;
    public static int arg = 0;
    
    @SuppressWarnings("unchecked") // AW: workaround for array conversion
    public HeadNode(Atom atom, Rete rete, Node n){
        super(rete);
        this.a = atom;
        this.tempVarPosition = (HashMap<Variable, Integer>) n.getVarPositions().clone();
        System.err.println("HeadNode Created!: " + this);
    }
    
    /*
     * If an instance is added to a headNode then this means the body of a rule has been
     * satisfied. We therefore create a new instance of the head via the variable assignment
     * that is still left from the last node.
     * 
     * from doesnt matter
     */
    @Override
    public void addInstance(Instance instance, Node from){
        //System.err.println("Adding something to headNode: " + instance);
        // the only children of HeadNodes are choice Nodes --> We remove the actual Instance from the choice Node
        // The instance should match the instance of the choice Node, since after the positive Part + Operators have been apllied no mor eVariables are added to the assignment
        //System.err.println(this.children);
        for(int i=0; i < this.children.size();i++){
            this.children.get(i).removeInstance(instance);
        }
        if(a == null){
            // This head Node is a constraint Node. If something arrives here the context is unsatsifiable!
            rete.satisfiable = false;
            System.out.println("UNSATISFIABLE!: huhu?");
            rete.printAnswerSet();
            return;
        }
        //System.err.println("ADD Intsnace to HEADNODE: " + instance);
        arg++;
        Term[] headInstance = new Term[a.getArity()];
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
        
        Instance instance2Add = Instance.getInstance(headInstance);
        //System.out.println("HEAD: " + instance2Add);
        if(!rete.containsInstance(a.getPredicate(), instance2Add, true)){
            if(!rete.containsInstance(a.getPredicate(), instance2Add, false)){
                rete.addInstancePlus(a.getPredicate(),instance2Add);
                //System.err.println("HEADNODE ADDING: " + instance2Add + "because this was added: " + instance + " and Atom of head is: " + a);
            }else{
                System.out.println("UNSATISFIABLE!: muhu: " + rete.getChoiceUnit().getDecisionLevel());
                this.rete.satisfiable = false;
                rete.printAnswerSet();
            }
        }
    }
    
    private FuncTerm unifyFuncTerm(FuncTerm f, Instance instance){
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
    }
    
}
