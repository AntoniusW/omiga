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
 * @author Gerald Weidinger 0526105
 * 
 * JoinNodes are store the joined variable assignments of two other nodes.
 * 
 * @param a the node that is the first join partner
 * @param b the node that is the second join partner
 * @param instanceOrdering defines how the variable assignment that is saved is calculated from to instance (one form a one from b). 2 means: take position 1 of node a. -1 means take position 0 of node b
 * @param selectionCriterion1 which position of an instance coming from node b resembles this position of node a, null means no one
 * @param selectionCriterion2 which position of an instance coming from node a resembles this position of node b, null means no one
 * 
 * @param selCrit1 temporary memory, for the selectionCriterion of Node a. (used within method addInstance)
 * @param selCrit2 temporary memory, for the selectionCriterion of Node b. (used within method addInstance)
 * @param tempVar A Variable that is used for the lookUp within addInstance. (It's a global variable so we do not have to create it all the time add instance is called)
 * 
 */
public class JoinNode extends Node{
    
    protected Node a;
    protected Node b;
    protected int[] instanceOrdering; // which position of which node goes where. 1 means position 0 of Node a, -2 means position 1 of Node b
    public Integer[] selectionCriterion1; // store the positions for a lookup
    public Integer[] selectionCriterion2;
    protected Term[] selCrit1; // Temporaere Vars fuer das selectionCriterion der anderen Node by INstance add
    protected Term[] selCrit2;
    
    protected Variable tempVar; // used as a Variable for the lookup
    
    //TODO: Remove ID when found bug!
        static int ID = 0;
    int id;
    
    public static int getNextID(){
        ID++;
        return ID;
    }
    
    /**
     * 
     * resets the tempVarPosition by clearing it and then adding all variables from both parentNodes.
     * This method also initializes the instanceOrdering, where the variables of node a are set to
     * the first n positions, and the variables from b, that are not contained in a, make up the rest.
     * 
     * @param a the node that is the first join partner
     * @param b the node that is the second join partner
     */
    /*public void resetVarPosition(Node a, Node b){
        System.err.println("RESETVARPOSITION of joinNOdes is used!");
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
                System.err.println("LOL: " + (this.tempVarPosition.size()-1) + " v = " + v);
                instanceOrdering[this.tempVarPosition.size()-1] = (b.getVarPositions().get(v)+1)*-1;
            }
            
        }
        //String testTemp = "[";
        //for(int i: instanceOrdering){
        //    testTemp = testTemp + i + ",";
        //}
        //testTemp = testTemp + "]";
        //System.err.println("LOLRAGARIAGA: " + testTemp);
    }*/
    
    
    
    
    
    /*public HashMap<Variable,Integer> getVarPosition(HashMap<Variable,Integer> varPosLeft, HashMap<Variable,Integer> varPosRight){
        HashMap<Variable,Integer> ret = new HashMap<Variable,Integer>();
        // first we add all Variables of Node a (These will reach from 0 to a.arity)
        for(Variable v: varPosLeft.keySet()){
            ret.put(v,varPosLeft.get(v));
        }
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        for(Variable v: varPosRight.keySet()){
            if(!ret.containsKey(v)){
                ret.put(v,ret.size());
            }
            
        }
        return ret;
    }*/
    
    
    
    
    /*
     * Select: Wir gehen einfach das Variable Ordering der anderen Node durch und setzen alle Variablen nach der Instanz davon
     */
    
    /**
     * 
     * public constructor. Creates a new joinNode with initialized data structures.
     * 
     * @param a the node that is the first join partner
     * @param bthe node that is the second join partner
     * @param rete the rete this join node is in
     */
    public JoinNode(Node a, Node b, Rete rete, HashMap<Variable,Integer> varPosA, HashMap<Variable,Integer> varPosB){
        super(rete); // registering this node within the ChoiceUnit

        this.id = getNextID();
        
        this.a = a;
        this.b = b;

        //We initialize the instance Ordering array
        HashMap<Variable,Integer> temp = new HashMap<Variable, Integer>();
        temp.putAll(varPosA);
        temp.putAll(varPosB);
        this.instanceOrdering = new int[temp.size()];
        
        //We initialize this nodes memory
        this.memory = new Storage(instanceOrdering.length);
        
        //We register this joinNode within it's two partners as child
        this.a.addChildR(this);
        this.b.addChild(this);
        
        // We set the selectionCriterias by putting the number of the other predicates position for that position into the array
        this.selectionCriterion1 = new Integer[varPosA.size()];
        for(Variable v: varPosB.keySet()){
            if(varPosA.containsKey(v)){
                //System.out.println(v);
                selectionCriterion1[varPosA.get(v)] = varPosB.get(v);
                //System.out.println("Setting Var position selCrit1: " + varPosA.get(v) +  " to: " + varPosB.get(v));
                //System.out.println("Because I select from: " + varPosA);
            }
        }
        this.selectionCriterion2 = new Integer[varPosB.size()];
        for(Variable v: varPosA.keySet()){
            if(varPosB.containsKey(v)){
                //System.out.println(v);
                selectionCriterion2[varPosB.get(v)] = varPosA.get(v);
                //System.out.println("Setting Var position selCrit2: " + varPosB.get(v) +  " to: " + varPosA.get(v));
                //System.out.println("Because I select from: " + varPosB);
            }
        }
        selCrit1 = new Term[selectionCriterion1.length];
        selCrit2 = new Term[selectionCriterion2.length];
        
        //We initialize a temp variable. that is laters used for lookups
        this.tempVar = Variable.getVariable("temp:Var");
        
        //this.resetVarPosition(a,b);
        
        //We initialize the instanceOrdering: Variables of the Left Partner get numbers from 1 to arity
        //while Variables of the right partner get negative numbers from -1 to - arity
        HashMap<Variable,Integer> temp2 = new HashMap<Variable,Integer>();
        
        
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        //TODO: Hier muss der bug sein!
        // In the JoinNodeNegative we ush instances from theleft to the actual join node
        // but they are of different position if we do not add the left partners variables here first!
        //TODO. Remove the TODO above. Add text: This must be like this because we push instaces in neg joinnodes
        
        // We first add the left Partner. This is important since we push instances from left to right
        // in negative join nodes. If we did not do it like this, the var ordering would not match anymore.
        // A recalculation on adding would cost time. So stay with this.
        for(Variable v: varPosB.keySet()){
            temp2.put(v, varPosB.get(v));
            instanceOrdering[varPosB.get(v)] = (varPosB.get(v)+1)*-1;// +1  because we have to distinguish between Node a,b. and +0 = -0. SO we map the positions to 1..n, -1..-n
            //instanceOrdering[temp2.size()-1] = (varPosB.get(v)+1)*-1; // +1  because we have to distinguish between Node a,b. and +0 = -0. SO we map the positions to 1..n, -1..-n
        }
        //System.out.println("VERDAMMT1: " + temp2);
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        for(Variable v: varPosA.keySet()){
            if(!temp2.containsKey(v)){
                temp2.put(v,temp2.size());
                instanceOrdering[temp2.size()-1] = (varPosA.get(v)+1);
            }
            
        }
        //System.out.println("INSTANCEORDERING: " + Instance.getInstanceAsString(instanceOrdering));
        //HashMap<Variable,Integer> temp2 = new HashMap<Variable,Integer>();
        /*for(Variable v: varPosA.keySet()){
            temp2.put(v,varPosA.get(v)); //temp2.size()
            instanceOrdering[varPosA.get(v)] = varPosA.get(v)+1; // +1  because we have to distinguish between Node a,b. and +0 = -0. SO we map the positions to 1..n, -1..-n
        }
        System.out.println("VERDAMMT1: " + temp2);
        // then we add all Nodes of B, without the Ordering of B. They take the last positions of our instances
        for(Variable v: varPosB.keySet()){
            if(!temp2.containsKey(v)){
                temp2.put(v,temp2.size());
                instanceOrdering[temp2.size()-1] = (varPosB.get(v)+1)*-1;
            }
            
        }
        System.out.println("VERDAMMT2: " + temp2);*/
        this.tempVarPosition = temp2;
        
        //System.err.println("Creating JoinNode for: A= " + a + " _ B= " + b + " MemorySIze= " + this.memory.arity);
        //this.rete.getChoiceUnit().addNode(this);
    }
    
    /**
     * 
     * Informs the joinNode of a new instance of it's parent nodes. The join node then 
     * calculates all joined variable assignments for this new instance, and adds them
     * to it's memory.
     * 
     * @param instance the new instance that has arrived
     * @param n the node where the instance arrived (a or b)
     */
    @Override
    public void addInstance(Instance instance, boolean from){
        //System.err.println("ADD Instance called: " + instance + " -" + from + " - " + this);
        Term[] selectionCriteria; // TODO: check if putting thise two variable outside this method decreases runtime
        Node selectFromHere; // TODO: check if putting thise two variable outside this method decreases runtime
        
        // we determine from which joinpartner the instance arrived, and build our selectionCriteria accordingly
        // by setting the position to be a variable if selectionCriterionX is null, and to the term of the insatnce's position
        // otherwise. This way we'll obtain all joinable instances from the other joinpartner.
        if(from){
            //The instance came from node a
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
            //the instance came from node b
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
        
        // We select all instances that match the selectionCriterion, and therefore are joinable with the new instance, from the other node
        //System.out.println("selectionCriteria: " + Instance.getInstanceAsString(selectionCriteria));
        //System.out.println("Node " + selectFromHere);
        Collection<Instance> joinPartners = selectFromHere.select(selectionCriteria);
        //System.out.println("All Join PArtners: " + joinPartners);
        //System.out.println("JoinNode instance added: " + instance + " JOINPARTNERS: " + joinPartners);
        // for each joinpartner we build a variable assignment we then add to this joinnodes memory
        //System.err.println("Join Partners for: " + from + " -:-: " + selectFromHere + "satis: " + rete.satisfiable + " joinpartnerSize: " + joinPartners.size());
        for(Instance varAssignment: joinPartners){
            Term[] toAdd = new Term[this.instanceOrdering.length];
            for(int i = 0; i < this.instanceOrdering.length;i++){
                if(from){
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
            /*System.out.println(this + "Adding " + instance2Add + " because of " + instance + " from " + from);
            System.out.println("Found JoindPartner: " + varAssignment);
            System.out.println(this.tempVarPosition);
            System.out.println(Instance.getInstanceAsString(this.instanceOrdering));*/
            this.memory.addInstance(instance2Add);
            super.addInstance(instance2Add, true); // register the adding of this variableassignment within the choiceUnit
            // We inform all children of this node that a new instance has arrived
            /*for(Node n: this.children){
                n.addInstance(instance2Add,false);
            }
            for(Node n: this.childrenR){
                n.addInstance(instance2Add,true);
            }*/
        
            for(int i = 0; i < this.children.size();i++){
                this.children.get(i).addInstance(instance2Add, false);
            }
            for(int i = 0; i < this.childrenR.size();i++){
                this.childrenR.get(i).addInstance(instance2Add, true);
            }
        }
    }
    
    /**
     * 
     * @return the string representation of this join node
     */
    @Override
    public String toString(){
        return "JoinNode" + this.id + ": " + "[" + this.b.toString() + "] [" + this.a.toString() + "] ";
    }
    
    //Actually this method is only called for JoinNodeNegative
    ArrayList<Instance> temp;
    public void informOfClosure(SelectionNode sN, boolean from){
        //System.out.println("Closure of: " + sN);
        //TODO: A closed notification always come from the right side! just kick this if statement. No need for bool nor sN
        Node actual; // The node that is not closed
        if(from){
            // the right partner is closed
            actual = b;
        }else{
            //the left partner is closed
            actual = a;
        }
        temp = actual.memory.getAllInstances();
        //System.out.println("ACTUAL = " + actual);
        //System.err.println("informed of closure: " + this + " #instances: " + temp.size() + " time: " + System.currentTimeMillis());
        //System.err.println("EXAMPLE INSTANCE: " + temp.get(0));
        for(int i = 0; i < temp.size();i++){
            
            for(int j = 0; j < selectionCriterion1.length;j++){
                if (selectionCriterion1[j] == null){
                    selCrit1[j] = tempVar;
                }else{
                    selCrit1[j] = temp.get(i).get( selectionCriterion1[j]);
                }
            }
            
            //rete.getBasicNodePlus(sN.getAtom().getPredicate()).getChildNode(sN.getAtom()).containsInstance(temp.get(i));
            //if(!rete.containsInstance(sN.getAtom().getPredicate(), temp.get(i), true)){
            //if(!rete.getBasicNodePlus(sN.getAtom().getPredicate()).getChildNode(sN.getAtom()).containsInstance(temp.get(i))){
            if(rete.getBasicNodePlus(sN.getAtom().getPredicate()) == null || rete.getBasicNodePlus(sN.getAtom().getPredicate()).getChildNode(sN.getAtom()) == null || !rete.getBasicNodePlus(sN.getAtom().getPredicate()).getChildNode(sN.getAtom()).containsInstance((Instance.getInstance(selCrit1))) ){

            //if(!rete.containsInstance(sN.getAtom().getPredicate(), temp.get(i), true)){
                //System.out.println("Rete contains: " + sN.getAtom().getPredicate() + "(" + temp.get(i) + ")" + "SN = " + sN.getAtom());
                this.memory.addInstance(temp.get(i));
                super.addInstance(temp.get(i), true); // register the adding of this variableassignment within the choiceUnit
                //System.out.println("Dervied through Closure: " + temp.get(i));
                for(int j = 0; j < this.children.size();j++){
                    this.children.get(j).addInstance(temp.get(i), false);
                }
                for(int j = 0; j < this.childrenR.size();j++){
                    this.childrenR.get(j).addInstance(temp.get(i), true);
                }
            }
        }
        //System.err.println("Finished closing: " + System.currentTimeMillis());
        
        /*for(Instance inz: actual.memory.getAllInstances()){
            if(!rete.containsInstance(sN.getAtom().getPredicate(), inz, true)){
                this.memory.addInstance(inz);
                for(Node n: this.children){
                    n.addInstance(inz, false);
                }
                for(Node n: this.childrenR){
                    n.addInstance(inz, true);
                }
            }
        }*/
    }
    
    
    
    
}
