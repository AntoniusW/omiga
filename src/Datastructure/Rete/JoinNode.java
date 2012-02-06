/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Atom;
import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author User
 */
public class JoinNode extends Node{
    
    Node a;
    Node b;
    int[] instanceOrdering; // which position of which node goes where. 1 means position 0 of Node a, -2 means position 1 of Node b
    Integer[] selectionCriterion1; // store the positions for a lookup
    Integer[] selectionCriterion2;
    Term[] selCrit1; // Temporaere Vars fuer das selectionCriterion der anderen Node by INstance add
    Term[] selCrit2;
    
    Variable tempVar; // used as a Variable for the lookup
    
    
    
    public void resetVarPosition(Node a, Node b){
        tempVarPosition.clear();
        HashMap<Variable,Integer> termsA = a.getVarPositions();
        HashMap<Variable, Integer> termsB = b.getVarPositions();
        // first we add all Variables of Node a (These will reach from 0 to a.arity)
        for(Variable v: termsA.keySet()){
            this.tempVarPosition.put(v,a.getVarPositions().get(v));
            instanceOrdering[a.getVarPositions().get(v)] = a.getVarPositions().get(v)+1; // +1  because we have to distinguish between Node a,b. and +0 = -0. SO we map the positions to 1..n, -1..-n
            //System.err.println("Got: " + v)
        }
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        for(Variable v: termsB.keySet()){
            if(!this.tempVarPosition.containsKey(v)){
                this.tempVarPosition.put(v,this.tempVarPosition.size());
                instanceOrdering[this.tempVarPosition.size()-1] = (b.getVarPositions().get(v)+1)*-1;
            }
            
        }
        String testTemp = "[";
        for(int i: instanceOrdering){
            testTemp = testTemp + i + ",";
        }
        testTemp = testTemp + "]";
        System.err.println("LOLRAGARIAGA: " + testTemp);
    }
    
    
    
    /*
     * Select: Wir gehen einfach das Variable Ordering der anderen Node durch und setzen alle Variablen nach der Instanz davon
     */
    
    public JoinNode(Node a, Node b, Rete rete){
        super(rete);
        //System.out.println("JoinNode Created!");
        this.a = a;
        this.b = b;

        System.err.println(a);
        System.err.println(b);

        HashMap<Variable,Integer> temp = new HashMap<Variable, Integer>();
        temp.putAll(a.getVarPositions());
        temp.putAll(b.getVarPositions());
        this.instanceOrdering = new int[temp.size()];
        
        this.memory = new Storage(instanceOrdering.length);
        
        this.a.addChild(this);
        this.b.addChild(this);
        
        // We set the selectionCriterias by putting the number of the other predicates position for that position into the array
        this.selectionCriterion1 = new Integer[a.getVarPositions().size()];
        for(Variable v: b.getVarPositions().keySet()){
            if(a.getVarPositions().containsKey(v)){
                selectionCriterion1[a.getVarPositions().get(v)] = b.getVarPositions().get(v);
            }
        }
        this.selectionCriterion2 = new Integer[b.getVarPositions().size()];
        for(Variable v: a.getVarPositions().keySet()){
            if(b.getVarPositions().containsKey(v)){
                selectionCriterion1[b.getVarPositions().get(v)] = a.getVarPositions().get(v);
            }
        }
        selCrit1 = new Term[selectionCriterion1.length];
        selCrit2 = new Term[selectionCriterion2.length];
        
        this.tempVar = Variable.getVariable("temp:Var");
        System.err.println("LOLRAGA:  creating JoinNode for: " + a.getVarPositions() + " - " + b.getVarPositions());
        this.resetVarPosition(a, b);
    }
    
    @Override
    public void addInstance(Instance instance, Node n){
        // We determine the which of the two join partners the instance comes from
        
        //System.out.println(this + " addInstance is called with: " + instance);
        Term[] selectionCriteria;
        
        Node selectFromHere;
        if(n.equals(a)){
            selectFromHere = b;
            for(int i = 0; i < selectionCriterion2.length;i++){
                if (selectionCriterion2[i] == null){
                    selCrit2[i] = tempVar;
                }else{
                    selCrit2[i] = instance.get( selectionCriterion2[i]);
                }
            }
            selectionCriteria = selCrit2;;
        }else{
            selectFromHere = a;
            for(int i = 0; i < selectionCriterion1.length;i++){
                if (selectionCriterion1[i] == null){
                    selCrit1[i] = tempVar;
                }else{
                    selCrit1[i] = instance.get( selectionCriterion1[i]);
                }
            }
            selectionCriteria = selCrit1;
        }
        
        
        // We select from the other node via our selectionCriterion
        // and add to our memory the combination of Variablevalues for each joinpartner
        
        
        Collection<Instance> joinPartners = selectFromHere.select(selectionCriteria);
        for(Instance varAssignment: joinPartners){
            Term[] toAdd = new Term[this.instanceOrdering.length];
            for(int i = 0; i < this.instanceOrdering.length;i++){
                if(n.equals(a)){
                    if(instanceOrdering[i] < 0){
                        toAdd[i] = varAssignment.get((instanceOrdering[i]*-1)-1);
                    }else{
                        toAdd[i] = instance.get(instanceOrdering[i] - 1);
                    }
                }else{
                    if(instanceOrdering[i] >= 0){
                        toAdd[i] = varAssignment.get(instanceOrdering[i] - 1);
                    }else{
                        toAdd[i] = instance.get((instanceOrdering[i]*-1)-1);
                    }
                }
            }
            Instance instance2Add = Instance.getInstance(toAdd);
            this.memory.addInstance(instance2Add);
            // We inform all children of this node that a new instance has arrived
            for(Node child: this.children){
                child.addInstance(instance2Add,this);
            }
        }
        
        /*Term[] selectAll = new Term[this.tempVarPosition.size()];
        for(int i = 0; i < selectAll.length; i++){
            selectAll[i] = Variable.getVariable("X");
        }
        System.out.println(this + " contains instances: " + this.memory.select(selectAll).size());
        System.out.println(this.memory.select(selectAll));*/
    }
    
    @Override
    public String toString(){
        return "JoinNode: " + this.a.toString() + this.b.toString();
    }
    
    
    
    
}
