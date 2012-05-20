/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.choice.ChoiceUnit;
import Entity.Atom;
import Entity.ContextASP;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class ReteBuilder {
    
    protected Rete rete;
    
        private HashMap<Predicate,ArrayList<ChoiceNode>> choiceNodes;
        private HashMap<Predicate,ArrayList<HeadNode>> headNodes;
    
    
    /**
     * 
     * public constrcutor. Creates a new ReteBuilder.
     * 
     * @param choiceUnit the ChoiceUnit you want your rete to connect with.
     */
    public ReteBuilder(ChoiceUnit choiceUnit){
        this.rete = new Rete(choiceUnit);
        this.choiceNodes = new HashMap<Predicate,ArrayList<ChoiceNode>>();
        this.headNodes = new HashMap<Predicate,ArrayList<HeadNode>>();
    }
    
    /**
     * 
     * public constrcutor. Creates a new ReteBuilder.
     * 
     * @param rete the rete network you wanna work with
     */
    public ReteBuilder(Rete rete){
        this.rete = rete;
        this.choiceNodes = new HashMap<Predicate,ArrayList<ChoiceNode>>();
        this.headNodes = new HashMap<Predicate,ArrayList<HeadNode>>();
    }
    
    public void addNegRule(Rule r){
        VarPosNodes = new HashMap<Node,HashMap<Variable,Integer>>();
        HashMap<Atom,HashMap<Variable,Integer>> varPositions = new HashMap<Atom, HashMap<Variable,Integer>>();
        
        if(r.getHead()!=null) {
            this.addAtomMinus(r.getHead());
            varPositions.put(r.getHead(), SelectionNode.getVarPosition(r.getHead()));
        }
        
        for(Atom a: r.getBodyPlus()){
            this.addAtomPlus(a);
            this.addAtomMinus(a);
            varPositions.put(a, SelectionNode.getVarPosition(a));
        }
        for(Atom a: r.getBodyMinus()){
            this.addAtomMinus(a);
            this.addAtomPlus(a); // We also create apositive SelectionNode for each negative one, since then it is easier to look them up for closed nodes.
            varPositions.put(a, SelectionNode.getVarPosition(a));
        }
        
        if(r.getBodyPlus().isEmpty()){
            Atom actual = r.getBodyMinus().get(0);
            SelectionNode actualNode = this.rete.getBasicLayerMinus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
            actualNode.resetVarPosition(actual);
            HeadNode hN = new HeadNodeNegative(r.getHead(),rete, SelectionNode.getVarPosition(actual),actualNode);
            hN.r = r;
            actualNode.addChild(hN);
            //this.addHeadNode(actual.getPredicate(), hN);
        }else{
            Atom actual = r.getBodyPlus().get(0);
            SelectionNode actualNode = this.rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
            actualNode.resetVarPosition(actual);
            HeadNode hN = new HeadNodeNegative(r.getHead(),rete, SelectionNode.getVarPosition(actual),actualNode);
            hN.r=r;
            actualNode.addChild(hN);
            //this.addHeadNode(actual.getPredicate(), hN);
        }
    }
    
    public void addRuleNeg(Rule r){
//We first add all Atoms of the rule to out retenetwork, so we then can work with the selectionnodes that are already there
        VarPosNodes = new HashMap<Node,HashMap<Variable,Integer>>();
        HashMap<Atom,HashMap<Variable,Integer>> varPositions = new HashMap<Atom, HashMap<Variable,Integer>>();
        
        if(r.getHead()!=null) {
            this.addAtomPlus(r.getHead());
            varPositions.put(r.getHead(), SelectionNode.getVarPosition(r.getHead()));
        }
        
        for(Atom a: r.getBodyPlus()){
            this.addAtomPlus(a);
            this.addAtomMinus(a);
            varPositions.put(a, SelectionNode.getVarPosition(a));
        }
        for(Atom a: r.getBodyMinus()){
            this.addAtomMinus(a);
            this.addAtomPlus(a); // We also create apositive SelectionNode for each negative one, since then it is easier to look them up for closed nodes.
            varPositions.put(a, SelectionNode.getVarPosition(a));
        }
        
        // We clone the different parts of the rules body, so we can change them without changing the rule
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsPlus = (ArrayList<Atom>) r.getBodyPlus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsMinus = (ArrayList<Atom>) r.getBodyMinus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Operator> operators = (ArrayList<Operator>) r.getOperators().clone();
        
        //we choose an atom with which we want to start
        Atom actual;
        Node actualNode;
        if(atomsPlus.isEmpty()){
            actual = getBestNextAtom(atomsMinus);
            actualNode = this.rete.getBasicLayerMinus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        }else{
            actual = getBestNextAtom(atomsPlus);
            actualNode = this.rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        }
        this.VarPosNodes.put(actualNode, varPositions.get(actual));
        //We cast to selectionNode, since this is always a selection Node
        ((SelectionNode)actualNode).resetVarPosition(actual);

        //We create variables for the partner, the ChoiceNode and HeadNodeConstraint for this rule
        Atom partner;
        ChoiceNode cN = null;
        //HeadNodeConstraint constraintNode = null; 
        while(!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()){
            //While there is still seomthing in the rules body
            if(!atomsPlus.isEmpty()){
                //There is still something within the positive body of the rule --> take it --> it's the new partner
                partner = getBestPartner(atomsPlus, actualNode);
                //Create a joinNode from the actualNode and the partner
                //System.out.println("RULE: " + r);
                if(actualNode.getClass().equals(SelectionNode.class)){
                    actualNode = this.createJoin(actual, partner, true,varPositions);
                    //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                }else{
                    actualNode = this.createJoin(actualNode, partner, true,varPositions);
                    //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                }
            }else{
                if(!atomsMinus.isEmpty()){
                    //There is still something within the negative body of the rule --> take it --> it's the new partner
                    partner = getBestPartner(atomsMinus, actualNode);
                    //Create a joinNode from the actualNode and the partner
                    //System.out.println("RULE: " + r);
                    if(actualNode.getClass().equals(SelectionNode.class)){
                        actualNode = this.createJoinNegative(actual, partner, false,varPositions); // TODO createJoinNegative
                        //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                    }else{
                        actualNode = this.createJoinNegative(actualNode, partner, false,varPositions); // TODO createJoinNegative
                        //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                    }
                }else{
                    // Negative Rules do not contain operators. At least for our easy rewriting.
                }
            }
        }
        //We define a headNode and add it to the actualNode (which is the last within this rules joinorder, since we are finsihed now)
        HeadNode hN = new HeadNodeNegative(r.getHead(),rete, this.VarPosNodes.get(actualNode),actualNode);
        hN.r=r;
        actualNode.addChild(hN);
    }
    
    /*private void addHeadNode(Predicate p, HeadNode hn){
        if (!this.headNodes.containsKey(p)) headNodes.put(p, new ArrayList<HeadNode>());
        this.headNodes.get(p).add(hn);
        if (!this.choiceNodes.containsKey(p)) return;
        for(ChoiceNode cN: this.choiceNodes.get(p)){
            hn.addChild(cN);
        }
    }
    
    private void addChoiceNode(Predicate p, ChoiceNode cn){
        if (!this.choiceNodes.containsKey(p)) choiceNodes.put(p, new ArrayList<ChoiceNode>());
        this.choiceNodes.get(p).add(cn);
        if (!this.headNodes.containsKey(p)) return;
        for(HeadNode hN: this.headNodes.get(p)){
            cn.addChild(hN);
        }
    }*/
    
    /**
     * 
     * adds a rule into the rete, by creating all needed nodes and connecting them into the right join order.
     * 
     * @param r the rule that should be added
     */
    public void addRule(Rule r){
        //We first add all Atoms of the rule to out retenetwork, so we then can work with the selectionnodes that are already there
        VarPosNodes = new HashMap<Node,HashMap<Variable,Integer>>();
        HashMap<Atom,HashMap<Variable,Integer>> varPositions = new HashMap<Atom, HashMap<Variable,Integer>>();
        
        if(r.getHead()!=null) {
            this.addAtomPlus(r.getHead());
            varPositions.put(r.getHead(), SelectionNode.getVarPosition(r.getHead()));
        }
        
        for(Atom a: r.getBodyPlus()){
            this.addAtomPlus(a);
            this.addAtomMinus(a);
            varPositions.put(a, SelectionNode.getVarPosition(a));
        }
        for(Atom a: r.getBodyMinus()){
            this.addAtomMinus(a);
            this.addAtomPlus(a); // We also create apositive SelectionNode for each negative one, since then it is easier to look them up for closed nodes.
            varPositions.put(a, SelectionNode.getVarPosition(a));
        }
        
        // We clone the different parts of the rules body, so we can change them without changing the rule
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsPlus = (ArrayList<Atom>) r.getBodyPlus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Atom> atomsMinus = (ArrayList<Atom>) r.getBodyMinus().clone();
        @SuppressWarnings("unchecked") // AW: workaround for array conversion
        ArrayList<Operator> operators = (ArrayList<Operator>) r.getOperators().clone();
        
        //we choose an atom with which we want to start
        Atom actual = getBestNextAtom(atomsPlus);
        
        // From the actual Atom we easily derive the corresponding node
        Node actualNode = this.rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        this.VarPosNodes.put(actualNode, varPositions.get(actual));
        //We cast to selectionNode, since this is always a selection Node
        ((SelectionNode)actualNode).resetVarPosition(actual);

        //We create variables for the partner, the ChoiceNode and HeadNodeConstraint for this rule
        Atom partner;
        ChoiceNode cN = null;
        //HeadNodeConstraint constraintNode = null;
        
        
        if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty() && !r.isConstraint()){
            // if the rule consisted only of one positive atom no operators and has a negative body we have to create the choice and constraint Node here.
            //constraintNode = new HeadNodeConstraint(rete, varPositions.get(actual).size());
            cN = new ChoiceNode(rete, varPositions.get(actual).size(),r,varPositions.get(actual), null/*constraintNode*/);
            actualNode.addChild(cN);
        }
        
        while(!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()){
            //While there is still seomthing in the rules body
            if(!atomsPlus.isEmpty()){
                //There is still something within the positive body of the rule --> take it --> it's the new partner
                partner = getBestPartner(atomsPlus, actualNode);
                //Create a joinNode from the actualNode and the partner
                //System.out.println("RULE: " + r);
                if(actualNode.getClass().equals(SelectionNode.class)){
                    actualNode = this.createJoin(actual, partner, true,varPositions);
                }else{
                    actualNode = this.createJoin(actualNode, partner, true,varPositions);
                }
                
                
                if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty() && !r.isConstraint()){
                    // if atomPlus is now empty  we removed the last atom from here.
                    // If there is a negative part and no operators are within this Rule then we now add the ChoiceNode and constraintNode
                    // since the positive part of the rule is satisfied now
                    //constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
                    cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(),r,actualNode.tempVarPosition, null/*constraintNode*/);
                    actualNode.addChild(cN);
                }
            }else{
                if(!operators.isEmpty()){
                    for(Operator op: operators){
                        if(op.getOP().equals(Enumeration.OP.ASSIGN) && op.isInstanciatedButOne(actualNode.tempVarPosition.keySet())){
                            OperatorNode opN = new OperatorNode(this.rete, op, actualNode);
                            actualNode.addChild(opN);
                            actualNode = opN;
                            this.VarPosNodes.put(opN, opN.getVarPositions());
                            operators.remove(op);
                            break;
                        }else{
                            if(op.isInstanciated(actualNode.tempVarPosition.keySet())){
                                OperatorNode opN = new OperatorNode(this.rete, op, actualNode);
                                actualNode.addChild(opN);
                                actualNode = opN;
                                this.VarPosNodes.put(opN, opN.getVarPositions());
                                operators.remove(op);
                                break;
                            }
                        }
                    }
                    if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty() && !r.isConstraint()){
                        // if atomPlus is now empty  we removed the last atom from here.
                        // If there is a negative part and no operators are within this Rule then we now add the ChoiceNode and constraintNode
                        // since the positive part of the rule is satisfied now
                        //constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
                        cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(),r,actualNode.tempVarPosition, null/*constraintNode*/);
                        actualNode.addChild(cN);
                    }
                }else{
                    if(!atomsMinus.isEmpty()){
                        //There is still something within the negative body of the rule --> take it --> it's the new partner
                        partner = getBestPartner(atomsMinus, actualNode);
                        //Create a joinNode from the actualNode and the partner
                        //System.out.println("RULE: " + r);
                        if(actualNode.getClass().equals(SelectionNode.class)){
                            actualNode = this.createJoinNegative(actual, partner, false,varPositions); // TODO createJoinNegative
                        }else{
                            actualNode = this.createJoinNegative(actualNode, partner, false,varPositions); // TODO createJoinNegative
                        }
                    }
                }
            }
        }
        //We define a headNode and add it to the actualNode (which is the last within this rules joinorder, since we are finsihed now)
        HeadNode hN = new HeadNode(r.getHead(),rete, this.VarPosNodes.get(actualNode),actualNode);
        hN.r = r;
        //this.addHeadNode(r.getHead().getPredicate(), hN);
        /*System.out.println("BLING!: " + r + " derives head: " + hN);
        JoinNode jn = (JoinNode)actualNode;
        System.out.println("SelCrit1: " + Instance.getInstanceAsString(jn.selectionCriterion1));
        System.out.println("SelCrit2: " + Instance.getInstanceAsString(jn.selectionCriterion2));*/
        actualNode.addChild(hN);
        //If we did contruct a constraintNode we add it to the actual Node as well
        //if(constraintNode != null) actualNode.addChild(constraintNode);
        //if we did construct a ChoiceNode we add it to the headNode
        if(cN!=null) hN.addChild(cN);
        /*if(cN != null){
            this.addChoiceNode(r.getHead().getPredicate(), cN);
        }*/
    }
    
    /**
     * 
     * Creates and returns a JoinNode
     * 
     * @param aNode the actual Node (must not be a selectionNode since the variablePositions would not match!)
     * @param b the Atom you want to combine the node with
     * @param bPositive wether b is from the positive or negative memory of the rete
     * @param varPositions a HAshMap containing for each Atom of the actual Rule the correspondin
     * @return the resulting joinnode with a and b as children.
     */
    protected Node createJoin(Node aNode, Atom b, boolean bPositive,HashMap<Atom,HashMap<Variable,Integer>> varPositions){
        SelectionNode bNode;
        if(bPositive){
            bNode = this.rete.getBasicLayerPlus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }else{
            bNode = this.rete.getBasicLayerMinus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }
        bNode.resetVarPosition(b);
        //TODO: Vertausche Nodea mit Nodeb
        JoinNode jn = new JoinNode(bNode,aNode,rete, varPositions.get(b),this.VarPosNodes.get(aNode));
        //JoinNode jn = new JoinNode(aNode,bNode,rete, aNode.getVarPositions(), varPositions.get(b));
        //VarPosNodes.put(jn,jn.getVarPosition(VarPosNodes.get(aNode), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
        /*System.out.println("LOL JOIN1: " + jn);
        System.out.println("TempVars: " + jn.tempVarPosition);
        System.out.println("SelCrit1: " + Instance.getInstanceAsString(jn.selectionCriterion1));
        System.out.println("SelCrit2: " + Instance.getInstanceAsString(jn.selectionCriterion2));*/
        return jn;
    }
    
    protected HashMap<Node,HashMap<Variable,Integer>> VarPosNodes = new HashMap<Node,HashMap<Variable,Integer>>();
    
    protected Node createJoin(Atom a, Atom b, boolean bPositive,HashMap<Atom,HashMap<Variable,Integer>> varPositions){
        SelectionNode aNode = this.rete.getBasicLayerPlus().get(a.getPredicate()).getChildNode(a.getAtomAsReteKey());
        SelectionNode bNode;
        if(bPositive){
            bNode = this.rete.getBasicLayerPlus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }else{
            bNode = this.rete.getBasicLayerMinus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }
        JoinNode jn = new JoinNode(bNode,aNode,rete, varPositions.get(b), varPositions.get(a));
        //JoinNode jn = new JoinNode(aNode,bNode,rete, varPositions.get(a), varPositions.get(b));
        //VarPosNodes.put(jn,jn.getVarPosition(varPositions.get(a), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
        /*System.out.println("LOL JOIN2: " + jn);
        System.out.println("TempVars: " + jn.tempVarPosition);
        System.out.println("SelCrit1: " + Instance.getInstanceAsString(jn.selectionCriterion1));
        System.out.println("SelCrit2: " + Instance.getInstanceAsString(jn.selectionCriterion2));*/
        return jn;
    }
    
    protected Node createJoinNegative(Node aNode, Atom b, boolean bPositive,HashMap<Atom,HashMap<Variable,Integer>> varPositions){
        SelectionNode bNode;
        if(bPositive){
            bNode = this.rete.getBasicLayerPlus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }else{
            bNode = this.rete.getBasicLayerMinus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }
        bNode.resetVarPosition(b);
        JoinNodeNegative jn = new JoinNodeNegative(bNode,aNode,rete, varPositions.get(b),this.VarPosNodes.get(aNode));
        //VarPosNodes.put(jn,jn.getVarPosition(VarPosNodes.get(aNode), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
        /*System.out.println("LOL JOIN3: " + jn);
        System.out.println("TempVars: " + jn.tempVarPosition);
        System.out.println("SelCrit1: " + Instance.getInstanceAsString(jn.selectionCriterion1));
        System.out.println("SelCrit2: " + Instance.getInstanceAsString(jn.selectionCriterion2));
        System.out.println("Atom b: " + b);*/
        return jn;
    }
    
    protected Node createJoinNegative(Atom a, Atom b, boolean bPositive,HashMap<Atom,HashMap<Variable,Integer>> varPositions){
        SelectionNode aNode = this.rete.getBasicLayerPlus().get(a.getPredicate()).getChildNode(a.getAtomAsReteKey());
        SelectionNode bNode;
        if(bPositive){
            bNode = this.rete.getBasicLayerPlus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }else{
            bNode = this.rete.getBasicLayerMinus().get(b.getPredicate()).getChildNode(b.getAtomAsReteKey());
        }
        JoinNodeNegative jn = new JoinNodeNegative(bNode,aNode,rete, varPositions.get(b), varPositions.get(a));
        //VarPosNodes.put(jn,jn.getVarPosition(varPositions.get(a), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
        /*System.out.println("LOL JOIN4: " + jn);
        System.out.println("TempVars: " + jn.tempVarPosition);
        System.out.println("SelCrit1: " + Instance.getInstanceAsString(jn.selectionCriterion1));
        System.out.println("SelCrit2: " + Instance.getInstanceAsString(jn.selectionCriterion2));
        System.out.println("Atom b: " + b);*/
        return jn;
    }
    
    
    /**
     * Finds an atom to combine the node with. Maybe something cool can be implemented here.
     * At the moment we just take the first.
     * 
     * @param atoms a list of atoms you want to choose from
     * @param node a node representing a memory for variable assignments
     * @return the best partner atom for the node
     */
    protected Atom getBestPartner(ArrayList<Atom> atoms, Node node){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    /**
     * 
     * Finds an atom to start with. Maybe something cool can be implemented here.
     * At the moment we just take the first.
     * 
     * @param atoms a list of atoms you want to choose from
     * @return the best atom to start with
     */
    protected Atom getBestNextAtom(ArrayList<Atom> atoms){
        Atom a = atoms.get(0);
        atoms.remove(a);
        return a;
    }
    
    /**
     * 
     * Adds an atom to the positive memory of the rete network, by creating a new SelectionNode for that atom and adding it as a child to the right basicNode
     * 
     * @param atom the atom you want to add
     */
    public void addAtomPlus(Atom atom){   
        //System.err.println("AddAtomPlus:");
        this.rete.addPredicatePlus(atom.getPredicate());
        if(!this.rete.getBasicLayerPlus().containsKey(atom.getPredicate())){
            //System.err.println("AddAtomPlus: Creating BasicNode: " + atom.getPredicate());
            this.rete.getBasicLayerPlus().put(atom.getPredicate(), new BasicNode(atom.getArity(),rete, atom.getPredicate()));
            //this.stackyPlus.put(atom.getPredicate(), new Stack<Instance>());
        }    
        //System.out.println("Adding Atom Plus");
        this.rete.getBasicLayerPlus().get(atom.getPredicate()).AddAtom(atom);
        
    }
    
    /**
     * Adds an atom to the negative memory of the rete network, by creating a new SelectionNode for that atom and adding it as a child to the right basicNode
     * 
     * @param atom the atom you want to add
     */
    public void addAtomMinus(Atom atom){
        this.rete.addPredicateMinus(atom.getPredicate());
        if(!this.rete.getBasicLayerMinus().containsKey(atom.getPredicate())){
            this.rete.getBasicLayerMinus().put(atom.getPredicate(), new BasicNodeNegative(atom.getArity(),rete, atom.getPredicate()));
            //this.stackyMinus.put(atom.getPredicate(), new Stack<Instance>());
        }   
        //System.out.println("Adding Atom Minus");
        this.rete.getBasicLayerMinus().get(atom.getPredicate()).AddAtom(atom);  
    }
    
}
