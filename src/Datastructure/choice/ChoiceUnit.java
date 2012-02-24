/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.Node;
import Entity.Atom;
import Entity.Constant;
import Entity.ContextASP;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import Exceptions.FactSizeException;
import Interfaces.Context;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


/**
 *
 * @author User
 */
public class ChoiceUnit {
    
    //TODO: A select everything Method in Storage and all nodes!
    //TODO: Store choiceNodes within the memory, as we need to store removement instances for them. This is now done her. But this is ugly.
    
    private ArrayList<ChoiceNode> choiceNodes;
    private ArrayList<HashMap<ChoiceNode,ArrayList<Instance>>> choiceNodesDecisionLayer;
    
    private DecisionMemory memory;
    
    private Stack<ChoiceNode> stackyNode;
    private Stack<Boolean> stackybool;
    private Stack<Instance> stackyInstance;
    
    ContextASP c;
    
    public ChoiceUnit(ContextASP c){
        this.memory = new DecisionMemory();
        this.choiceNodes = new ArrayList<ChoiceNode>();
        this.c = c;
        this.stackyNode = new Stack<ChoiceNode>();
        this.stackybool = new Stack<Boolean>();
        this.stackyInstance = new Stack<Instance>();
        this.choiceNodesDecisionLayer = new ArrayList<HashMap<ChoiceNode,ArrayList<Instance>>>();
        this.choiceNodesDecisionLayer.add(new HashMap<ChoiceNode,ArrayList<Instance>>());
    }
    
    public boolean choice(){
        System.out.println("Choice is called! " + this.memory.getDecisonLevel());
        if(this.nextNode != null){
            this.memory.addChoicePoint();
            nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            nextNode = null;
            return true;
        }
        
        this.printAllChoiceNodes();
        int i = 0;
        for(;i < choiceNodes.size();i++){
            Term[] selectionCriteria = new Term[choiceNodes.get(i).getVarPositions().size()];
            for(int j = 0; j < choiceNodes.get(i).getVarPositions().size();j++){
                selectionCriteria[j] = Variable.getVariable("X");
            }
            if(!choiceNodes.get(i).select(selectionCriteria).isEmpty()){
                memory.addChoicePoint();
                // We add a new layer to our choiceNodeLayer
                HashMap<ChoiceNode,ArrayList<Instance>> newHM = new HashMap<ChoiceNode, ArrayList<Instance>>();
                for(ChoiceNode cN: this.choiceNodes){
                    newHM.put(cN, new ArrayList<Instance>());
                }
                this.choiceNodesDecisionLayer.add(newHM);
                // End DecisionNode Layer update
                Instance inz = ((Instance)choiceNodes.get(i).select(selectionCriteria).toArray()[0]);
                String varPos = "[";
                for(Variable v: choiceNodes.get(i).getVarPositions().keySet()){
                    varPos = varPos + v + "=" + choiceNodes.get(i).getVarPositions().get(v) + ",";
                }
                varPos = varPos + "]";
                System.out.println("VarPos: ");
                System.out.println(varPos);
                
                for(Atom a: choiceNodes.get(i).getRule().getBodyMinus()){
                    Instance toAdd = unifyAtom(a,inz, choiceNodes.get(i).getVarPositions());
                    c.getRete().addInstanceMinus(a.getPredicate(), toAdd);
                }
                // be aware that used instances get deleted from choice nodes via propagation
                
                this.stackyNode.push(choiceNodes.get(i));
                this.stackybool.push(true); 
                this.stackyInstance.push(inz); 
                System.out.println("GUESSED: " + choiceNodes.get(i).getRule() + " with: " + inz);
                // We made a chocie so we have to store the information on which we guessed
                
                return true;
                
            }
        }
        return false;
    }
    
    public Instance unifyAtom(Atom a, Instance instance,HashMap<Variable,Integer> varPos){
        Term[] headInstance = new Term[a.getArity()];
        for(int i = 0; i < a.getArity();i++){
            Term t = a.getTerms()[i];
            if(t.getClass().equals(Variable.class)){
                    headInstance[i] = instance.get(varPos.get((Variable)t));
                }else{
                    if (t.getClass().equals(Constant.class)){
                        headInstance[i] = t;
                    }else{
                        //System.err.println("unifyTerm from head");
                        headInstance[i] = this.unifyFuncTerm((FuncTerm)t, instance,varPos);
                    }
            }  
        }
        
        Instance instance2Add = Instance.getInstance(headInstance);
        return instance2Add;
    }
    
    private FuncTerm unifyFuncTerm(FuncTerm f, Instance instance,HashMap<Variable,Integer> varPos){
        //System.err.println("Unifying Head Instance: " + f + " vs. " + instance);
        ArrayList<Term> fchildren = new ArrayList<Term>();        
        for(int i = 0; i < f.getChildren().size();i++){
            Term t = f.getChildren().get(i);
            if(t.getClass().equals(Variable.class)){
                    fchildren.add(instance.get(varPos.get((Variable)t)));
            }else{
                if (t.getClass().equals(Constant.class)){
                    fchildren.add(t);
                }else{
                    fchildren.add(unifyFuncTerm((FuncTerm)t, instance, varPos));
                }
            }
        }
        FuncTerm ret = FuncTerm.getFuncTerm(f.getName(), fchildren);
        //System.err.println("Tehrefore returning: " + ret + " as children are: " + ret.getChildren());
        return ret;
    }
    
    public void addChoiceNode(ChoiceNode cN){
        this.choiceNodesDecisionLayer.get(0).put(cN, new ArrayList<Instance>());
        this.choiceNodes.add(cN);
    }
    
    public void printAllChoiceNodes(){
        System.out.println("Printing choice nodes");
        System.out.println("_________________________");
        for(ChoiceNode cN: choiceNodes){
            System.out.println("ChoiceNode: " + cN);
            Term[] selectionCriteria = new Term[cN.getVarPositions().size()];
            for(int i = 0; i < cN.getVarPositions().size();i++){
                selectionCriteria[i] = Variable.getVariable("X");
            }
            for(Instance i: cN.select(selectionCriteria)){
                System.out.println("Instance: " + i);
            }
        }
        System.out.println("_________________________");
    }
    
    
    public void addInstance(Node n, Instance instance){
        this.memory.addInstance(n, instance);
    }
    
    public void AddInstanceRemovement(ChoiceNode cn, Instance instance){
        this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cn).add(instance);
    }
    
    public void printDecisionMemory(){
        memory.printDecisionMemory();
    }
    
    public void addNode(Node n){
        this.memory.addNode(n);
    }
    
    private ChoiceNode nextNode = null;
    private Instance nextInstance = null;
    public void backtrack(){
        //if(this.memory.getDecisonLevel()< 0) return;
        System.out.println("BACKTRACK: " + this.memory.getDecisonLevel());
        this.memory.backtrack();
        System.out.println("STACKSIZE: " + stackybool.size() + " - " + stackyInstance.size() + " - " + stackyNode.size());
        if(this.stackybool.isEmpty()) return;
        if(this.stackybool.pop()){
            System.out.println("Last was a POSITVE Guess");
            nextNode = stackyNode.pop();
            nextInstance = stackyInstance.pop();
        }else{
            System.out.println("Last was a NEGATIVE Guess");
            if(memory.getDecisonLevel() > 0) {
                //Terminate the Guessing here. We are back at lvl one and no more guessing is possible since we arrived via a negative guess!
                this.stackyNode.pop().addInstance(this.stackyInstance.pop(), null);
            } // We add the choice back into the choice node
            backtrack();
        }
    }
    
    
    public void backtrack2(){
        System.out.println("BACKTRACKING!: " + memory.getDecisonLevel());
        this.printAllChoiceNodes();
        // add Instances that have been removed from choicenodes on a certain decisionlevel
        /*for(ChoiceNode cN: this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).keySet()){
            for(int i = 0; i < this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).size();i++){
                cN.addInstance(this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).get(i),null);
            }
        }*/
        //this.choiceNodesDecisionLayer.remove(this.choiceNodesDecisionLayer.size()-1);
        if(this.stackyNode.isEmpty()) return;
        ChoiceNode n = this.stackyNode.pop();
        Instance inz = stackyInstance.pop();
        //n.removeInstance(inz);
        System.out.println("STACKSIZE: " + stackybool.size() + " - " + stackyInstance.size() + " - " + stackyNode.size());
        if(this.stackybool.empty()){
            return;
        }
        boolean temp = this.stackybool.peek();
        if(temp){
            System.out.println("ASA+");
            this.memory.backtrack();
            n.getConstraintNode().saveConstraintInstance(inz); //TODO: Remove Constraint instance?!
            this.stackybool.push(false);
            this.stackyNode.push(n);
            this.stackyInstance.push(inz);
        }else{
            System.out.println("Asa-");
            System.out.println("This Never happens! ^^: " + memory.getDecisonLevel());
            if(memory.getDecisonLevel() > 0 ){
                //this.stackyInstance.pop();
                System.out.println("Adding: " + inz + " to Node: " + n);
                n.addInstance(inz, null);
                System.out.println("PEEK: " + stackybool.peek());
                System.out.println("PEEK: " + stackybool.empty());
                if(!temp || stackybool.isEmpty()){
                    System.out.println("SPECIAL BACK TRACK!");
                    backtrack();
                }else{
                    System.out.println("FUCK!");
                }
            }else{
                return;
            }
        }
    }
    
    /*public void backtrack(Instance instance){
        System.out.println("BACKTRACKING!");
        this.printAllChoiceNodes();
        this.memory.backtrack();
        // add Instances that have been removed from choicenodes on a certain decisionlevel
        for(ChoiceNode cN: this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).keySet()){
            System.err.println("Einraga");
            for(int i = 0; i < this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).size();i++){
                System.err.println("Zweiraga");
                cN.addInstance(this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).get(i),null);
                System.out.println("AFTERPARTY: ");
                this.printAllChoiceNodes();
            }
        }
        this.choiceNodesDecisionLayer.remove(this.choiceNodesDecisionLayer.size()-1);
        ChoiceNode n = this.stackyNode.pop();
        n.removeInstance(instance);
        if(this.stackybool.pop()){
            Instance inz = stackyInstance.pop();
            n.getConstraintNode().saveConstraintInstance(inz);
            this.stackybool.push(false);
            this.stackyNode.push(n);
            this.stackyInstance.push(inz);
        }else{
            System.out.println("This Never happens! ^^");
            if(memory.getDecisonLevel() > 0){
                //this.stackyInstance.pop();
                backtrack(this.stackyInstance.pop());
            }else{
                return;
            }
        }
        System.out.println("AFTER BACK: ");
        this.printAllChoiceNodes();
    }*/
    
    public int getDecisionLevel(){
        return this.memory.getDecisonLevel();
    }
    
    public void printDecisionNodes(){
        for(ChoiceNode cN: this.choiceNodes){
            System.out.println("ChoiceNode: " + cN);
            Term[] selectionCriteria = new Term[cN.getVarPositions().size()];
            for(int j = 0; j < cN.getVarPositions().size();j++){
                selectionCriteria[j] = Variable.getVariable("X");
            }
            for(Instance inz: cN.select(selectionCriteria)){
                System.out.println("Instance: " + inz);
            }
        }
    }
    
    
}
