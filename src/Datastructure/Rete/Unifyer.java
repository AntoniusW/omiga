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
 * This is a static helper class that provides methods for unification
 * 
 */
public class Unifyer {
    
    /**
     * 
     * unifies an Atom with the given variable assignment
     * 
     * @param a the atom you to unify
     * @param variableAssignment the variable assignment you want the atom to unify with
     * @param varPos a HashMap mapping Variables of the atom to the position of the variable assignment instance
     * @return the instance resulting from this unification
     */
    public static Instance unifyAtom(Atom a, Instance variableAssignment,HashMap<Variable,Integer> varPositionMapping){
        Term[] retTerms = new Term[a.getArity()];
        for(int i = 0; i < a.getArity();i++){
            //We go through all the terms of the atom
            Term t = a.getTerms()[i];
            if(t.getClass().equals(Variable.class)){
                //if the actual term is a variable we set the corresponding position of the instance we want to return to the the value that is within the variabel assignment at the position
                //of that variable within the mapping
                /*System.err.println("VARAS: " + variableAssignment);
                System.err.println("Atom: " + a);
                System.err.println("Variable: " + t);
                System.err.println("Mapping: " + varPositionMapping);
                System.err.println("WARPOS: " + varPositionMapping.get((Variable)t));*/
                retTerms[i] = variableAssignment.get(varPositionMapping.get((Variable)t));
            }else{
                if (t.getClass().equals(Constant.class)){
                    //if the actual term is a constant we simply put that constant into our instance we want to return.
                    retTerms[i] = t;
                }else{
                    //if the actual term is a functerm we have to unify that functerm and put that result at the corresponding position of our insatnce to return
                    retTerms[i] = unifyFuncTerm((FuncTerm)t, variableAssignment,varPositionMapping);
                }
            }  
        }
        Instance ret = Instance.getInstance(retTerms,variableAssignment.propagationLevel,variableAssignment.decisionLevel);
        return ret;
    }
    
    /**
     * 
     * unifies a function term with the given variable assignment
     * 
     * @param f the function term you want to unify
     * @param variableAssignment the variable assignment you want the atom to unify with
     * @param varPos a HashMap mapping Variables of the atom to the position of the variable assignment instance
     * @return the unified function term resulting from this unification
     */
    private static FuncTerm unifyFuncTerm(FuncTerm f, Instance variableAssignment,HashMap<Variable,Integer> varPositionMapping){
        ArrayList<Term> fchildren = new ArrayList<Term>();        
        for(int i = 0; i < f.getChildren().size();i++){
            //we go through all the children(=terms) of the given function term
            Term t = f.getChildren().get(i);
            if(t.getClass().equals(Variable.class)){
                //if the actual term is a variable we add the corresponding constant, defined by the mapping and the variable assignment, to the children
                //of the function term we want to return.
                fchildren.add(variableAssignment.get(varPositionMapping.get((Variable)t)));
            }else{
                if (t.getClass().equals(Constant.class)){
                    //if the actual term is a constant we simply add that constant to the children of the functerm we will return
                    fchildren.add(t);
                }else{
                    //if the actual term again is a function term we have to unify it as well and then add him to the actual fucntion terms children
                    fchildren.add(unifyFuncTerm((FuncTerm)t, variableAssignment, varPositionMapping));
                }
            }
        }
        FuncTerm ret = FuncTerm.getFuncTerm(f.getName(), fchildren);
        return ret;
    }
    
    
}
