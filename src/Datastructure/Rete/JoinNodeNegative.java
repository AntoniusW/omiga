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
    
    //TODO: Integreate These new JoinNodesNegative in the reteBuilder
    
    public JoinNodeNegative(Node a, Node b, Rete rete, HashMap<Variable,Integer> varPosA, HashMap<Variable,Integer> varPosB){
        super(a,b,rete,varPosA,varPosB);
    }
    
    @Override
    public void addInstance(Instance instance, boolean from){
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
            selectionCriteria = selCrit2;
            // The new instance arived from right, therefore we to push all instances of the select from left to the children
            Collection<Instance> joinPartners = selectFromHere.select(selectionCriteria);
            for(Instance inz: joinPartners){
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
            selectFromHere = a;
            for(int i = 0; i < selectionCriterion1.length;i++){
                if (selectionCriterion1[i] == null){
                    selCrit1[i] = tempVar;
                }else{
                    selCrit1[i] = instance.get( selectionCriterion1[i]);
                }
            }
            selectionCriteria = selCrit1;
            Instance instance2Add = selectFromHere.select(selectionCriteria).iterator().next(); // TODO DOes this work?
            if(instance2Add != null){
                for(int i = 0; i < this.children.size();i++){
                    this.children.get(i).addInstance(instance2Add, false);
                }
                for(int i = 0; i < this.childrenR.size();i++){
                    this.childrenR.get(i).addInstance(instance2Add, true);
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
