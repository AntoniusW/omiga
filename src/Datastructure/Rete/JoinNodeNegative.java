/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A JoinNodeNegative is a JoinNode that has a negative SelectionNode as right partner. This is a special case of JoinNodes where we can
 * piush instances form left to the children, rather than calculating new instances we then have to push.
 * 
 */
public class JoinNodeNegative extends JoinNode{
    
    
    //a is always a selectionNodeNegative for a JoinNodeNegative!
    
    //TODO: Integreate These new JoinNodesNegative in the reteBuilder
    
    public JoinNodeNegative(Node a, Node b, Rete rete, HashMap<Variable,Integer> varPosA, HashMap<Variable,Integer> varPosB){
        super(a,b,rete,varPosA,varPosB);
        /*if(!a.getClass().equals(SelectionNodeNegative.class)) {
            System.err.println("DANGER: NO neg SelectionNode in Negative JoinNode: " + a.getClass());
            System.err.println(this);
        }*/
    }
    
    @Override
    public void addInstance(Instance instance, boolean from){
        //System.out.println("Instance: " + instance + " reaches JoinNode: " + this + " from: " + from);
        //TODO: selectFromHere and selectioNCriteria can be omitted here!
        Term[] selectionCriteria; // TODO: check if putting thise two variable outside this method decreases runtime
        Node selectFromHere; // TODO: check if putting thise two variable outside this method decreases runtime
        
        // we determine from which joinpartner the instance arrived, and build our selectionCriteria accordingly
        // by setting the position to be a variable if selectionCriterionX is null, and to the term of the insatnce's position
        // otherwise. This way we'll obtain all joinable instances from the other joinpartner.
        if(from){
            //The instance came from node a
            //selectFromHere = b;
            for(int i = 0; i < selectionCriterion2.length;i++){
                if (selectionCriterion2[i] == null){
                    selCrit2[i] = tempVar;
                }else{
                    selCrit2[i] = instance.get( selectionCriterion2[i]);
                }
            }
            //selectionCriteria = selCrit2;
            // The new instance arived from right, therefore we to push all instances of the select from left to the children
            Collection<Instance> joinPartners = b.select(selCrit2);
            //System.out.println("JoinNodeNegative has this many children: " + this.children);
            for(Instance inz: joinPartners){
                this.memory.addInstance(inz);
                this.rete.getChoiceUnit().addInstance(this, inz);
                //System.out.println("TEMPVARPOSITION: " + this.tempVarPosition);
                //System.out.println(this + " instance added: " + inz + " JOINPARTNERS: " + joinPartners + " because of: " + instance + " from: " + from);
                for(int i = 0; i < this.children.size();i++){
                    this.children.get(i).addInstance(inz, false);
                }
                for(int i = 0; i < this.childrenR.size();i++){
                    this.childrenR.get(i).addInstance(inz, true);
                }
                /*for(Node n: this.children){
                    n.addInstance(inz,false);
                }
                for(Node n: this.childrenR){
                    n.addInstance(inz,true);
                }*/
            }
        }else{
            //the instance came from node b
            //selectFromHere = a;
            for(int i = 0; i < selectionCriterion1.length;i++){
                if (selectionCriterion1[i] == null){
                    selCrit1[i] = tempVar;
                }else{
                    selCrit1[i] = instance.get( selectionCriterion1[i]);
                }
            }
            /*System.out.println("TEMPVARPOSITION: " + this.tempVarPosition);
            System.out.println("SelCrit1[0] = so because selCriterion1= " + selectionCriterion1[0]);
            for(Variable v: this.tempVarPosition.keySet()){
                if(this.tempVarPosition.get(v) == selectionCriterion1[0]){
                    System.out.println("And the variable equal to that Pos =  "+v);
                }
                if(v.equals(Variable.getVariable("X4"))) System.out.println("X4 would be: " + this.tempVarPosition.get(v));
            }*/
            //selectionCriteria = selCrit1;
            if(a.containsInstance(Instance.getInstance(selCrit1))){
                this.memory.addInstance(instance);
                //System.out.println(this + " instance added: " + instance + " because of: " + a + " saying: " + a.containsInstance(Instance.getInstance(selCrit1)) + " to: " + Instance.getInstance(selCrit1));
                this.rete.getChoiceUnit().addInstance(this, instance);
                for(int i = 0; i < this.children.size();i++){
                    this.children.get(i).addInstance(instance, false);
                }
                for(int i = 0; i < this.childrenR.size();i++){
                    this.childrenR.get(i).addInstance(instance, true);
                }
                 /*for(Node n: this.children){
                    n.addInstance(instance2Add,false);
                }
                for(Node n: this.childrenR){
                    n.addInstance(instance2Add,true);
                }*/
            }
        }
        
        
    }
    
    
}
