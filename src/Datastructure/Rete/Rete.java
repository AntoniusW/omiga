/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 *
 * @author User
 */
public class Rete {
    
    HashMap<Predicate, BasicNode> basicLayerPlus;
    HashMap<Predicate, BasicNode> basicLayerMinus;
    
    /*HashMap<Predicate, Stack<Instance>> stackyPlus;
    HashMap<Predicate, Stack<Instance>> stackyMinus;*/
    
    public boolean satisfiable = true;
    
    public Rete(){
        this.basicLayerPlus = new HashMap<Predicate,BasicNode>();
        this.basicLayerMinus = new HashMap<Predicate,BasicNode>();
        /*this.stackyPlus = new HashMap<Predicate, Stack<Instance>>();
        this.stackyMinus = new HashMap<Predicate, Stack<Instance>>();*/
    }
    
    /*
     * we push facts into our network til nothing more can be reached
     */
    public static int kok;
    
    
    public void propagate(){
        boolean flag = true;
        while(flag && satisfiable){
            flag = false;
            for(BasicNode bn: this.basicLayerPlus.values()){
                if(bn.propagate()) flag = true;
            }
            for(BasicNode bn: this.basicLayerMinus.values()){
                if(bn.propagate()) flag = true;
            }
        }
    }
    
    /*public void propagate(){
        boolean stacksNotEmpty = true;
        while(stacksNotEmpty){
            stacksNotEmpty = false;
            for(Predicate p: stackyPlus.keySet()){
                if(!stackyPlus.get(p).isEmpty()){
                    Instance instance = stackyPlus.get(p).pop();
                    //System.out.println("Adding Instance: " + instance);
                    if(!this.containsInstance(p, instance, false)){
                        if (!this.containsInstance(p, instance, true)) {
                            kok++;
                            //System.out.println("Adding to: " + p + " follwoing instance: " + instance);
                            basicLayerPlus.get(p).addInstance(instance);
                        }else{
                            //System.out.println("This instance is already contained!");
                        }
                    }else{
                        satisfiable = false;
                        System.err.println("Unsatisfiable!");
                        return;
                    }
                    stacksNotEmpty = true;
                    //System.out.println("Added Something!");
                }
            }
            for(Predicate p: stackyMinus.keySet()){
                if(!stackyMinus.get(p).isEmpty()){
                    Instance instance = stackyPlus.get(p).pop();
                    if(!this.containsInstance(p, instance, true)){
                        if (!this.containsInstance(p, instance, false)){
                            kok++;
                            basicLayerMinus.get(p).addInstance(instance);
                        }
                    }else{
                        satisfiable = false;
                        System.err.println("Unsatisfiable!");
                        return;
                    }
                    stacksNotEmpty = true;
                    //System.out.println("Added Something!");
                }
            }
        }
    }*/
    
    
    
    public static int omg = 0;
    public void addInstancePlus(Predicate p, Instance instance){
        omg++;
        basicLayerPlus.get(p).addInstance(instance);
        //this.stackyPlus.get(p).push(instance);
    }
    
     
    public void addInstanceMinus(Predicate p,Instance instance){
        omg++;
        basicLayerMinus.get(p).addInstance(instance);
        //this.stackyMinus.get(p).push(instance);
    }
    
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
    
    /*
     * Only used for testing?
     */
    public BasicNode getBasicNodePlus(Predicate p){
        return basicLayerPlus.get(p);
    }
    
    public void printAnswerSet(){
        for(Predicate p: this.basicLayerPlus.keySet()){
            Term[] selectionCriteria = new Term[p.getArity()];
            for(int i = 0; i < p.getArity();i++){
                selectionCriteria[i] = Variable.getVariable("X");
            }
            System.out.println("Instances for: " + p + " " + this.basicLayerPlus.get(p).select(selectionCriteria).size());
            //this.basicLayerPlus.get(p).printAllInstances();
        }
    }
    
    /*
     * Only needed to add Storage for facts that are of predicates that are not used within rules.
     */
    public void addPredicatePlus(Predicate p){
        this.basicLayerPlus.put(p, new BasicNode(p.getArity(),this));
    }
    public void addPredicateMinus(Predicate p){
        this.basicLayerMinus.put(p, new BasicNode(p.getArity(),this));
    }
    public boolean containsPredicate(Predicate p, boolean plus){
        if(plus) return this.basicLayerPlus.containsKey(p);
        return this.basicLayerMinus.containsKey(p);
    }

    public HashMap<Predicate, BasicNode> getBasicLayerMinus() {
        return basicLayerMinus;
    }

    public HashMap<Predicate, BasicNode> getBasicLayerPlus() {
        return basicLayerPlus;
    }
    
    
    
    
    
    
    
    
    
    
    
}
