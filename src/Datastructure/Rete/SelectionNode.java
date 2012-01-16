/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructures.storage.Storage;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.PredInRule;
import Entity.Variable;
import Interfaces.PredAtom;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class SelectionNode {
    
    private PredInRule pir;
    private ArrayList<Node> children;
    private Storage memory;
    
    
    public SelectionNode(PredInRule pir){
        this.pir = pir;
        memory = new Storage(pir.getVariables().size());
        children = new ArrayList<Node>();
    }
    
    public void addInstance(PredAtom[] varAssignment){
        System.out.println("SelectionNodeAddInstance: " + Instance.getInstanceAsString(varAssignment));
        // TODO: Here only Vars are assigned: So only let real var assignments come form basic node here
        // or filter the vars here!
        
        ArrayList<Variable> vars = pir.getVariables();
        for(Variable v: vars){
            v.setValue(null);
        }
        for(int i = 0; i < pir.getAtoms().length;i++){
            assignVariable4PredAtom(varAssignment[i],pir.getAtoms()[i]);
        }
        PredAtom[] varsAsInstance = new PredAtom[vars.size()];
        for(int i = 0; i < vars.size();i++){
            varsAsInstance[i] = vars.get(i).getValue();
            System.out.println("BLUBB: " + i + " : " + vars.get(i).getValue());
        }
        System.out.println("VARS AS INSTANCE: " + Instance.getInstanceAsString(varsAsInstance));
        memory.addInstance(varsAsInstance);
        for(Node n: children){
            n.addInstance(varsAsInstance);
        }
        PredAtom[] sel = {Variable.getVariable("X"),Variable.getVariable("Y"),Variable.getVariable("Z")};
        System.out.println("Size of all instanceselect: " + memory.select(sel).size());
    }
    
    private boolean assignVariable4PredAtom(PredAtom pa, PredAtom crit){
        if(crit.isConstant()){
            System.err.println("DOING NOTHING!");
            // Do NOthing. This criteria is already fullfilled when an instances fits this node
        }
        if(crit.isVariable()) {
            System.out.println("Crit = " + crit );
            if(((Variable)crit).getValue() != null){
                if(!((Variable)crit).getValue().equals(pa)){
                    System.err.println("Returning false!");
                    return false;
                }else{
                    ((Variable)crit).setValue(pa);
                    System.err.println("Set value to: " + pa);
                } 
            }else{
                ((Variable)crit).setValue(pa);
                System.err.println("Set value to: " + pa); 
            } 
        }
        if(crit.isFuncTerm()){
            for(int i = 0; i < ((FuncTerm)crit).getChildren().size();i++){
                assignVariable4PredAtom(pa.getChildren().get(i), crit.getChildren().get(i));
            }
        }
        return true;
    }
    
    public boolean fits(PredAtom[] instance){
        for(int i = 0; i < pir.getArity();i++){
            System.out.println(pir.getAtoms()[i]);
            if(!pir.getAtoms()[i].isParentOf(instance[i])) return false;
        }
        return true;
    }
    
    public Storage getMemory(){
        return this.memory;
    }
    
    
}
