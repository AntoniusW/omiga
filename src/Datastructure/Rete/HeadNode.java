/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.FuncTerm;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;

/**
 *
 * @author Mika
 * 
 * Maybe we have to execute HeadNodes as soon as an instance reaches a node... but i dont think so
 * if so we have to use a childlist of headnodes
 */
public class HeadNode extends Node{
    
    Atom a;
    
    public HeadNode(Atom atom, Rete rete){
        super(rete);
        this.a = atom;
    }
    
    /*
     * If an instance is added to a headNode then this means the body of a rule has been
     * satisfied. We therefore create a new instance of the head via the variable assignment
     * that is still left from the last node.
     * 
     * from doesnt matter
     */
    @Override
    public void addInstance(Term[] instance, Node from){
        ArrayList<Term> toAdd = new ArrayList<Term>();
        for(int i = 0; i < a.getArity();i++){
            Term t = a.getTerms()[i];
            if(t.isVariable()){
                    toAdd.add(((Variable)t).getValue());
                }else{
                    if (t.isConstant()){
                        toAdd.add(t);
                    }else{
                        toAdd.add(this.unifyFuncTerm((FuncTerm)t));
                    }
            }  
        }
        Term[] headInstance = new Term[toAdd.size()];
        toAdd.toArray(headInstance);
        rete.addInstancePlus(a.getPredicate(),headInstance);
    }
    
    private FuncTerm unifyFuncTerm(FuncTerm f){
        ArrayList<Term> fchildren = new ArrayList<Term>();
        FuncTerm ret = FuncTerm.getFuncTerm(f.getName(), fchildren);
        
        for(int i = 0; i < f.getChildren().size();i++){
            Term t = f.getChildren().get(i);
            if(t.isVariable()){
                    fchildren.add(((Variable)t).getValue());
                }else{
                    if (t.isConstant()){
                        fchildren.add(t);
                    }else{
                        fchildren.add(unifyFuncTerm((FuncTerm)t));
                    }
            }
        }
        return ret;
    }
    
}
