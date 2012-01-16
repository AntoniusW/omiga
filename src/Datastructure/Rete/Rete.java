/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.PredInRule;
import Entity.Predicate;
import Interfaces.PredAtom;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class Rete {
    
    HashMap<Predicate, BasicNode> basicLayer;
    HashMap<PredInRule, SelectionNode> selectionLayer;
    HashMap<String, JoinNode> joinLayer;
    
    public Rete(){
        this.basicLayer = new HashMap<Predicate,BasicNode>();
        this.selectionLayer = new HashMap<PredInRule, SelectionNode>();
        this.joinLayer = new HashMap<String,JoinNode>();
    }
    
    public void addPredInRule(PredInRule pir){
        if(!basicLayer.containsKey(pir.getPredicate()))
            this.basicLayer.put(pir.getPredicate(), new BasicNode(pir.getArity()));
        basicLayer.get(pir.getPredicate()).AddPredInRule(pir);
        
    }
    
    
    public void addInstance(Predicate p, PredAtom[] instance){
        basicLayer.get(p).addInstance(instance);
    }
    
    public BasicNode getBasicNode(Predicate p){
        return basicLayer.get(p);
    }
    
    
}
