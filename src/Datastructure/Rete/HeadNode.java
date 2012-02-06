/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
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
    
    public HeadNode(Atom atom, Rete rete, Node n){
        super(rete);
        //System.out.println("HeadNode Created!");
        this.a = atom;
        this.tempVarPosition = (HashMap<Variable, Integer>) n.getVarPositions().clone();
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
        Term[] headInstance = new Term[a.getArity()];
        for(int i = 0; i < a.getArity();i++){
            Term t = a.getTerms()[i];
            if(t.isVariable()){
                    headInstance[i] = instance.get(this.tempVarPosition.get((Variable)t));
                }else{
                    if (t.isConstant()){
                        headInstance[i] = t;
                    }else{
                        headInstance[i] = this.unifyFuncTerm((FuncTerm)t, instance);
                    }
            }  
        }
        rete.addInstancePlus(a.getPredicate(),Instance.getInstance(headInstance));
    }
    
    private FuncTerm unifyFuncTerm(FuncTerm f, Instance instance){
        ArrayList<Term> fchildren = new ArrayList<Term>();
        FuncTerm ret = FuncTerm.getFuncTerm(f.getName(), fchildren);
        
        for(int i = 0; i < f.getChildren().size();i++){
            Term t = f.getChildren().get(i);
            if(t.isVariable()){
                    fchildren.add(instance.get(this.tempVarPosition.get((Variable)t)));
                }else{
                    if (t.isConstant()){
                        fchildren.add(t);
                    }else{
                        fchildren.add(unifyFuncTerm((FuncTerm)t, instance));
                    }
            }
        }
        return ret;
    }
    
}
