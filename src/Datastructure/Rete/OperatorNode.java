/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Constant;
import Entity.Instance;
import Entity.Operator;
import Entity.Variable;
import Enumeration.OP;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class OperatorNode extends Node{
    
    private Operator op; // note that this can only be equals, notequals, bigger or smaller if allSet is true, and only PLUS or MINUS if allSet is false
    //private boolean allSet;
    //Variable badOne = null;
    
    public OperatorNode(Rete rete, Operator op, Node from){
        super(rete);
        //System.out.println("CREATIGN OP NODE!");
        this.op = op;
        
        if(op.getOP().equals(Enumeration.OP.ASSIGN)){
            // There is one Variable that has to be calculated by the Operator therefore we add one further Variable into this VarPositions
            this.tempVarPosition = (HashMap<Variable, Integer>) from.getVarPositions().clone();
            this.tempVarPosition.put((Variable)op.getLeft(), tempVarPosition.size());
            this.memory = new Storage(tempVarPosition.size());
        }else{
            // All Variables of the Operator are set
            this.tempVarPosition = from.getVarPositions();
            this.memory = new Storage(tempVarPosition.size());
        }
        
        /*allSet = true;
        for(Variable v: op.getUsedVariables()){
            //System.out.println("v= " + v);
            if(!from.getVarPositions().containsKey(v)){
                //System.out.println("NOT CONTAINED!: " + from.getVarPositions());
                allSet = false;
                badOne = v;
                break;
            }
        }
        if(allSet){
            // All Variables of the Operator are set
            this.tempVarPosition = from.getVarPositions();
            this.memory = new Storage(tempVarPosition.size());
            // We do not have to do anything. just check when adding an Instance that the Operator has toi be fullfilled
        }else{
            // There is one Variable that has to be calculated by the Operator therefore we add one further Variable into this VarPositions
            this.tempVarPosition = (HashMap<Variable, Integer>) from.getVarPositions().clone();
            this.tempVarPosition.put(badOne, tempVarPosition.size());
            this.memory = new Storage(tempVarPosition.size());
        }*/
        
    }
    
    public void addInstance(Instance instance, boolean from){
        
        //System.out.println("AddInstance in OPNode called!: " + instance);
        //System.out.println("AllSet = " + allSet);
        
        if(op.getOP().equals(Enumeration.OP.ASSIGN)){
            ArrayList<Variable> temp = op.getUsedVariables();
            temp.remove((Variable)op.getLeft());
            for(Variable v: temp){
                //we initialize all the avriable values by the insatnce values such that the operator can calculate its value
                v.setValue(instance.get(this.getVarPositions().get(v)));
            }
            ((Variable)op.getLeft()).setValue(Constant.getConstant(String.valueOf(op.getIntValue(0, 0, null))));
            Term instanceArray[] = new Term[this.tempVarPosition.size()];
            for(Variable v: this.getVarPositions().keySet()){
                instanceArray[this.getVarPositions().get(v)] = v.getValue();
            }
            Instance instance2Add = Instance.getInstance(instanceArray);
            this.memory.addInstance(instance);
            super.addInstance(instance2Add, from); // Register this instance in our backtracking structure.
            for(int i = 0; i < this.children.size();i++){
                this.children.get(i).addInstance(instance2Add, false);
            }
            for(int i = 0; i < this.childrenR.size();i++){
                this.childrenR.get(i).addInstance(instance2Add, true);
            }
        }else{
            for(Variable v: op.getUsedVariables()){
                //we initialize all the avriable values by the insatnce values such that the operator can calculate its value
                v.setValue(instance.get(this.getVarPositions().get(v)));
            }
            if(op.getIntValue(0,0,null) == 1){
                // The instance fullfills the operator
                this.memory.addInstance(instance);
                super.addInstance(instance, from); // Register this instance in our backtracking structure.
                for(int i = 0; i < this.children.size();i++){
                    this.children.get(i).addInstance(instance, false);
                }
                for(int i = 0; i < this.childrenR.size();i++){
                    this.childrenR.get(i).addInstance(instance, true);
                }
            }else{
                //else we do not add the instance
                //System.out.println("Operatorcheck not passed!");
            }
        }
            
        
        /*if(this.allSet){
            for(Variable v: op.getUsedVariables()){
                //we initialize all the avriable values by the insatnce values such that the operator can calculate its value
                v.setValue(instance.get(this.getVarPositions().get(v)));
            }
            if(op.getIntValue(null) == 1){
                // The instance fullfills the operator
                this.memory.addInstance(instance);
                super.addInstance(instance, from); // Register this instance in our backtracking structure.
                for(int i = 0; i < this.children.size();i++){
                    this.children.get(i).addInstance(instance, false);
                }
                for(int i = 0; i < this.childrenR.size();i++){
                    this.childrenR.get(i).addInstance(instance, true);
                }
            }else{
                //else we do not add the instance
                //System.out.println("Operatorcheck not passed!");
            }
            
        }else{
            //TODO
            ArrayList<Variable> temp = op.getUsedVariables();
            temp.remove(this.badOne);
            for(Variable v: temp){
                //we initialize all the avriable values by the insatnce values such that the operator can calculate its value
                v.setValue(instance.get(this.getVarPositions().get(v)));
            }
            badOne.setValue(Constant.getConstant(String.valueOf(op.calculate(badOne))));
            Term instanceArray[] = new Term[this.tempVarPosition.size()];
            for(Variable v: this.getVarPositions().keySet()){
                instanceArray[this.getVarPositions().get(v)] = v.getValue();
            }
            Instance instance2Add = Instance.getInstance(instanceArray);
            this.memory.addInstance(instance);
                super.addInstance(instance2Add, from); // Register this instance in our backtracking structure.
                for(int i = 0; i < this.children.size();i++){
                    this.children.get(i).addInstance(instance2Add, false);
                }
                for(int i = 0; i < this.childrenR.size();i++){
                    this.childrenR.get(i).addInstance(instance2Add, true);
                }
        }*/
        
        
        //this.memory.addInstance(instance);
    }
    
}
