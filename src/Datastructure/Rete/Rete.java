/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Entity.Atom;
import Entity.Predicate;
import Entity.Rule;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author User
 */
public class Rete {
    
    HashMap<Predicate, BasicNode> basicLayerPlus;
    HashMap<Predicate, BasicNode> basicLayerMinus;
    
    public Rete(){
        this.basicLayerPlus = new HashMap<Predicate,BasicNode>();
        this.basicLayerMinus = new HashMap<Predicate,BasicNode>();
    }
    
    public void addRule(Rule r){
        //We first add all Atoms of the rule to out retenetwork, so we then can work with the selectionnodes that are already there
        for(Atom a: r.getBodyPlus()){
            this.addAtomPlus(a);
        }
        for(Atom a: r.getBodyMinus()){
            this.addAtomMinus(a);
        }
        
        ArrayList<Atom> atoms = (ArrayList<Atom>) r.getBodyPlus().clone();
        Atom actual = getBestNextAtom(atoms);
        Atom partner = getBestPartner(atoms, actual);
        while(!atoms.isEmpty()){
            
        }
        
        
        
    }
    
    private void createJoin(Node aNode, Atom b, boolean bPositive){
        SelectionNode bNode;
        if(bPositive){
            bNode = this.basicLayerPlus.get(b.getPredicate()).getChildren().get(b);
        }else{
            bNode = this.basicLayerMinus.get(b.getPredicate()).getChildren().get(b);
        }
        // TOCHECK: Keep track of all JoinNodes such that we create each joinNode only once!
        JoinNode jn = new JoinNode(aNode,bNode);  
    }
    
    private Atom getBestPartner(ArrayList<Atom> atoms, Atom atom){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    private Atom getBestNextAtom(ArrayList<Atom> atoms){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    public void addAtomPlus(Atom atom){
        if(!basicLayerPlus.containsKey(atom.getPredicate()))
            this.basicLayerPlus.put(atom.getPredicate(), new BasicNode(atom.getArity()));
        basicLayerPlus.get(atom.getPredicate()).AddPredInRule(atom);
        
    }
    
    public void addAtomMinus(Atom atom){
        if(!basicLayerMinus.containsKey(atom.getPredicate()))
            this.basicLayerMinus.put(atom.getPredicate(), new BasicNode(atom.getArity()));
        basicLayerMinus.get(atom.getPredicate()).AddPredInRule(atom);
        
    }
    
    public void addInstancePlus(Predicate p, Term[] instance){
        basicLayerPlus.get(p).addInstance(instance);
    }
    
     
    public void addInstanceMinus(Predicate p, Term[] instance){
        basicLayerMinus.get(p).addInstance(instance);
    }
    
    /*
     * Only used for testing?
     */
    public BasicNode getBasicNodePlus(Predicate p){
        return basicLayerPlus.get(p);
    }
    
    
}
