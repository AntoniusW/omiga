/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.choice.ChoiceUnit;
import Entity.Atom;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Stack;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The class Rete defines a hole rete network.To actually build a Rete, have a look into the class ReteBuilder.
 * The rete class itself is an entrypoint for new instances. Those instances are then pushed into BasicNodes.
 * Each basicNode is a memory for one predicate. The Basic Nodes again push the instances forward into SelectionNodes.
 * A selection Node represents an atom, and only insatnces of the corresponding predicate that fullfill the atoms schema
 * may enter a selection node. Selection nodes again push the insatnces to other nodes.
 * 
 * JoinNodes: represent joins of instances between 2 nodes.
 * Operator nodes: Represent an Operator. Only instances that satisfy this operator may enter here.
 * 
 * When an instance is finally pushed to a headNode, we know a rules body has been fullfilled, and therefore we can add
 * a ne fact, that is pushed into rete.
 * 
 * Additional nodes:
 * 
 * ChoiceNodes: Used to define guessing. (Not needed for propagation)
 * HeadNodeConstraints: Defining stuff that may not happen anymore. 8Also used because of guessing)
 * 
 * @param ChoiceUnit the choice Unit that is connected to the rete. (Rete itself does not need this, but it's nodes have to register there)
 * @param basicLayerPlus entrypoints for instances into the positive memory, sorted by predicate the belong to
 * @param basicLayerMinus entrypoints for instances into the negative memory, sorted by predicate the belong to
 * @param satisfiable a boolean flag stating if the actual calculation may still be satisfiable or already is unsatisfiable
 * 
 */
public class Rete {
    
    // TODO: Rete does not need a chocie Unit pointer but the nodes do. more elegant solution?
    private ChoiceUnit choiceUnit;
    private HashMap<Predicate, BasicNode> basicLayerPlus;
    private HashMap<Predicate, BasicNodeNegative> basicLayerMinus;
    public boolean satisfiable = true;
    
    

    
    /**
     * 
     * public constructor. generates a new rete network with initialized data structures.
     * @param choiceUnit the choiceUnit you want to connect with this rete
     */
    public Rete(ChoiceUnit choiceUnit){
        this.choiceUnit = choiceUnit;
        this.basicLayerPlus = new HashMap<Predicate,BasicNode>();
        this.basicLayerMinus = new HashMap<Predicate,BasicNodeNegative>();
    }
    
    /**
     * propagates til nothing can be propagated anymore or til unsatisfiability is reached
     */
    public void propagate(){
        //System.out.println("RETE: PROPAGATION!");
        boolean flag = true;
        while(flag && satisfiable){
            flag = false;
            // we propagate all instances from positive and negative basic layer
            for(BasicNode bn: this.basicLayerPlus.values()){
                if(bn.propagate()) flag = true;
            }
            for(BasicNode bn: this.basicLayerMinus.values()){
                if(bn.propagate()) flag = true;
            }
        }
        if(!satisfiable){
            // Since we store facts within the basic Nodes on a stack, we have to empty this stack
            // in case of unsatisfiability, since the stack has to be empty for the next propagation after backtracking
            for(BasicNode bn: basicLayerPlus.values()){
                bn.resetPropagation();
            }
            for(BasicNode bn: basicLayerMinus.values()){
                bn.resetPropagation();
            }
        }
        /*System.out.println("DECISION UNIT OUTPRINT:");
        this.choiceUnit.printDecisionMemory();
        System.out.println("____________________");
        this.choiceUnit.printAllChoiceNodes();
        System.out.println("____________________");*/
    }
    
   
    
     /**
     * 
     * @param p the predicate you want to add an instance for
     * @param instance the instance you want to add
     */ 
    public void addInstancePlus(Predicate p, Instance instance){
        if(this.containsInstance(p, instance, false)) {
            //System.out.println("HAHA UNSAT! via plus: " + p + " : " + instance);// TODO: swap this if into choice true guess, since this is the only way this can happen
            this.satisfiable = false;
        }
        if(this.containsInstance(p, instance, true)) {
            //System.out.println("BLING!: DOUBLE ADD!");
            return;
        } // TODO: Somehow avoid this if.
        //System.out.println("AddingPLUS: " + p.getName() + "(" + instance + ")");
        basicLayerPlus.get(p).addInstance(instance);
    }
    
    /**
     * 
     * @param p the predicate you want to add an instance for
     * @param instance the instance you want to add
     */ 
    public void addInstanceMinus(Predicate p,Instance instance){
        if(this.containsInstance(p, instance, true)) {
            //System.out.println("HAHA UNSAT via minus!: " + p + " : " + instance);// TODO: swap this if into choice true guess, since this is the only way this can happen
            this.satisfiable = false;
        }
        if(this.containsInstance(p, instance, false)) {
            //System.out.println("BLING!: DOUBLE ADD!");
            return;
        } // TODO: Somehow avoid this if.
        //System.err.println("AddingMINUS: " + p.getName() + "(" + instance + ")");
        /*System.err.println(basicLayerMinus.get(p));*/
        basicLayerMinus.get(p).addInstance(instance);
    }
    
    /**
     * 
     * @param p the predicate of the fact
     * @param instance the instance of the fact
     * @param positive negative or positive occurance?
     * @return whether the fact is contained within the neg/pos memory of the retenetwork or not
     */
    public boolean containsInstance(Predicate p, Instance instance, boolean positive){
        if(positive){
            if(this.basicLayerPlus.containsKey(p)){
                return basicLayerPlus.get(p).containsInstance(instance);
            }else{
                return false;
            }
        }else{
            if(this.basicLayerMinus.containsKey(p)){
                return basicLayerMinus.get(p).containsInstance(instance);
            }else{
                return false;
            }
        }
    }
    
    /**
     * 
     * @param p the predicate of the fact
     * @param instance the instance of the fact
     * @param positive negative or positive occurrence?
     * @return whether the fact is contained within the neg/pos memory of the retenetwork or not
     */
    public boolean containsInstance(Atom a, Instance instance, boolean positive){
        if(positive){
            if(this.basicLayerPlus.containsKey(a.getPredicate())){
                if(basicLayerPlus.get(a.getPredicate()).getChildNode(a) != null){
                    return basicLayerPlus.get(a.getPredicate()).getChildNode(a).containsInstance(instance);
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            if(this.basicLayerMinus.containsKey(a.getPredicate())){
                if(((BasicNodeNegative)basicLayerMinus.get(a.getPredicate())).isClosed()) {
                    return !containsInstance(a,instance,true);
                }
                if(basicLayerMinus.get(a.getPredicate()).getChildNode(a) != null){
                    return basicLayerMinus.get(a.getPredicate()).getChildNode(a).containsInstance(instance);
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }
    
    /**
     * 
     * returns the Basic Node that corresponds to the given predicate
     * 
     * @param p the predicate for which you want the corresponding basic Node
     * @return the BasicNode for predicate p
     */
    public BasicNode getBasicNodePlus(Predicate p){
        return basicLayerPlus.get(p);
    }
    
        /**
     * 
     * returns the Basic Node Negative that corresponds to the given predicate
     * 
     * @param p the predicate for which you want the corresponding basic Node
     * @return the BasicNode for predicate p
     */
    public BasicNodeNegative getBasicNodeMinus(Predicate p){
        return basicLayerMinus.get(p);
    }
    
    /**
     * prints the actual Answersets (=All Instances) to standard Out
     * @param String filter the predicates which are to be printed, if filter
     *        is null, all predicates are printed
     */
    public void printAnswerSet(String filter){
        boolean onlyPrintIN = false; // TODO: for testing set to false
        boolean densePrint = false; //true;
        boolean filtering = filter==null? false : true;
        //System.out.println("Printing Answerset: ");
        //System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
        //System.out.println("Positive Facts: ");
        System.out.println("-----------------------------");
        //System.out.println("IN:");
        String filter_use="";
        if(filtering)
            filter_use = "," + filter + ",";
        for (Predicate p : this.basicLayerPlus.keySet()) {
            // skip predicate if it does not occur in the filter and filtering is on
            String filter_pred = "," + p.getName() + ",";
            if(filtering && filter_use.indexOf(filter_pred) == -1 )
                continue;
            // print all instances
            Term[] selectionCriteria = new Term[p.getArity()];
            for (int i = 0; i < p.getArity(); i++) {
                selectionCriteria[i] = Variable.getVariable("X");
            }
            boolean didPrint = basicLayerPlus.get(p).memory.prettyPrintAllInstances(p.getName(), densePrint);
            if (didPrint) {
                System.out.println();
            }
        }
        if( !onlyPrintIN ) {
        //System.out.println("Negative Facts: ");
        System.out.println("OUT:");
        for (Predicate p : this.basicLayerMinus.keySet()) {
            // skip predicate if it does not occur in the filter and filtering is on
            String filter_pred = "," + p.getName() + ",";
            if(filtering && filter_use.indexOf(filter_pred) == -1 )
                continue;
            Term[] selectionCriteria = new Term[p.getArity()];
            for (int i = 0; i < p.getArity(); i++) {
                selectionCriteria[i] = Variable.getVariable("X");
            }
            boolean didPrint = basicLayerMinus.get(p).memory.prettyPrintAllInstances(p.getName(), densePrint);
            if (didPrint) {
                System.out.println();
            }
        }
        System.out.println("Closed: ");
        
        boolean didPrint = false;
        for (Entry<Predicate, BasicNodeNegative> entry : basicLayerMinus.entrySet()) {
            if (entry.getValue().isClosed()) {
                System.out.print(entry.getKey().getName() + "/" + entry.getKey().getArity() + " ");
                didPrint = true;
            }
        }
        
        if (didPrint) System.out.println();
        /*}else{
            String temp = "," + filter + ",";
            for(Predicate p: this.basicLayerPlus.keySet()){
                //if(p.getName().equals(filter)){
                String pred_name = "," + p.getName() + ",";
                if (temp.indexOf(pred_name) != -1) {
                    Term[] selectionCriteria = new Term[p.getArity()];
                    for(int i = 0; i < p.getArity();i++){
                        selectionCriteria[i] = Variable.getVariable("X");
                    }
                    System.out.println("Instances for: " + p + " " + this.basicLayerPlus.get(p).select(selectionCriteria).size());
                    this.basicLayerPlus.get(p).printAllInstances();
                }
            }
            
            System.out.println("Negative Facts: ");
            for(Predicate p: this.basicLayerMinus.keySet()){
                String pred_name = "," + p.getName() + ",";
                if (temp.indexOf(pred_name) != -1)
                {
                    Term[] selectionCriteria = new Term[p.getArity()];
                    for(int i = 0; i < p.getArity();i++){
                        selectionCriteria[i] = Variable.getVariable("X");
                    }
                    System.out.println("Instances for: " + this.basicLayerMinus.get(p));// + " " + this.basicLayerMinus.get(p).select(selectionCriteria).size());
                    this.basicLayerMinus.get(p).printAllInstances();
                }
            }            
        }*/
        } // end if (onlyPrintIN)
        //System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
        System.out.println("-----------------------------");
    }
    
    public void printRete(){
        /*HashSet<BasicNode> basics = new HashSet<BasicNode>();
        HashSet<SelectionNode> sel = new HashSet<SelectionNode>();
        HashSet<JoinNode> join = new HashSet<JoinNode>();
        HashSet<ChoiceNode> choicer = new HashSet<ChoiceNode>();
        HashSet<HeadNode> heads = new HashSet<HeadNode>();
        HashSet<HeadNodeConstraint> cons = new HashSet<HeadNodeConstraint>();
        for(BasicNode bn: this.basicLayerPlus.values()){
            basics.add(bn);
        }
        for(BasicNode bn: this.basicLayerMinus.values()){
            basics.add(bn);
        }
        for(BasicNode bn: basics){
            for(SelectionNode sn: bn.basicChildren){
                sel.add(sn);
            }
        }*/
        for(Node n: Node.nodes){
            System.out.println(n + " :");
            System.out.println("________________________");
            if(n.memory != null) n.memory.printAllInstances();
        }
        
    }
    
    /**
     * 
     * Adds a new BasicNode into the positive BasicLayer by creating a new BasicNode
     * 
     * @param p the predicate you want to add
     */
    public void addPredicatePlus(Predicate p){
        //System.err.println("AddintPredicatePlus: " + p);
        if(!this.basicLayerPlus.containsKey(p)){
            this.basicLayerPlus.put(p, new BasicNode(p.getArity(),this,p));
            addPredicateMinus(p);
        }
            
    }
    
    /**
     * 
     * Adds a new BasicNode into the negative BasicLayer by creating a new BasicNode
     * 
     * @param p the predicate you want to add
     */
    public void addPredicateMinus(Predicate p){
        //System.err.println("AddintPredicateMinus: " + p);
        if(!this.basicLayerMinus.containsKey(p)){
            this.basicLayerMinus.put(p, new BasicNodeNegative(p.getArity(),this,p));
            addPredicatePlus(p);
        }
            
    }
    
    /**
     * 
     * @param p the predicate you want to check
     * @param plus positive or negative occurance of that predicate
     * @return wether the predicate is contained within our rete or not
     */
    public boolean containsPredicate(Predicate p, boolean plus){
        if(plus) return this.basicLayerPlus.containsKey(p);
        return this.basicLayerMinus.containsKey(p);
    }

    /**
     * 
     * @return the HashMap reresenting the negative BasicLayer
     */
    public HashMap<Predicate, BasicNodeNegative> getBasicLayerMinus() {
        return basicLayerMinus;
    }

    /**
     * 
     * @return the HashMap reresenting the positive BasicLayer
     */
    public HashMap<Predicate, BasicNode> getBasicLayerPlus() {
        return basicLayerPlus;
    }
    
    /**
     * 
     * @return the choice Unit that is connected to this rete
     */
    public ChoiceUnit getChoiceUnit(){
        return this.choiceUnit;
    }
}
