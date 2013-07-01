package Datastructure.Rete;

import Datastructure.choice.ChoiceUnit;
import Entity.Atom;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Enumeration.OP;
import Exceptions.LearningException;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author user
 */
public class ReteBuilder {
    
    protected Rete rete;
    protected static Rule stemRule = null;  // hack for easier debugging
    
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
    
    public void addRuleNeg(Rule r) {
        stemRule = r;
//We first add all Atoms of the rule to out retenetwork, so we then can work with the selectionnodes that are already there
        VarPosNodes = new HashMap<Node, HashMap<Variable, Integer>>();
        HashMap<Atom, HashMap<Variable, Integer>> varPositions = new HashMap<Atom, HashMap<Variable, Integer>>();

        if (r.getHead() != null) {
            this.addAtomPlus(r.getHead());
            varPositions.put(r.getHead(), SelectionNode.getVarPosition(r.getHead()));
        }

        for (Atom a : r.getBodyPlus()) {
            this.addAtomPlus(a);
            this.addAtomMinus(a);
            varPositions.put(a, SelectionNode.getVarPosition(a));
        }
        for (Atom a : r.getBodyMinus()) {
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
        if (atomsPlus.isEmpty()) {
            actual = getBestNextAtom(atomsMinus);
            actualNode = this.rete.getBasicLayerMinus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        } else {
            actual = getBestNextAtom(atomsPlus);
            actualNode = this.rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        }
        VarPosNodes.put(actualNode, varPositions.get(actual));
        //We cast to selectionNode, since this is always a selection Node
        ((SelectionNode) actualNode).resetVarPosition(actual);

        //We create variables for the partner, the ChoiceNode and HeadNodeConstraint for this rule
        //Please note that they are not used in the current implementation, as our rewriting only leads to
        // very simple negative rules. If you want to fully feature negative rules, adept the follwoing code
        // to the positive rules by adding the choiceNode and constraint Node to your liking.
        Atom partner;
        ChoiceNode cN = null;
        HeadNodeConstraint constraintNode = null;
        while (!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()) {
            //While there is still seomthing in the rules body
            if (!atomsPlus.isEmpty()) {
                //There is still something within the positive body of the rule --> take it --> it's the new partner
                partner = getBestPartner(atomsPlus, actualNode);
                //Create a joinNode from the actualNode and the partner
                //System.out.println("RULE: " + r);
                if (actualNode.getClass().equals(SelectionNode.class)) {
                    actualNode = this.createJoin(actual, partner, true, varPositions);
                    //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                } else {
                    actualNode = this.createJoin(actualNode, partner, true, varPositions);
                    //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                }
            } else {
                if (!atomsMinus.isEmpty()) {    // we build a propagation-rule, so negative body works like positive one
                    //There is still something within the negative body of the rule --> take it --> it's the new partner
                    partner = getBestPartner(atomsMinus, actualNode);
                    //Create a joinNode from the actualNode and the partner
                    //System.out.println("RULE: " + r);
                    if (actualNode.getClass().equals(SelectionNode.class)) {
                        actualNode = this.createJoinNegative(actual, partner, false, varPositions); // TODO createJoinNegative
                        //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                    } else {
                        actualNode = this.createJoinNegative(actualNode, partner, false, varPositions); // TODO createJoinNegative
                        //System.err.println("Created JoinNode 4 negative Rule: " + actualNode);
                    }
                } else {
                    if (!operators.isEmpty()) {
                        for (Operator op : operators) {
                            if (op.getOP().equals(Enumeration.OP.ASSIGN) && op.isInstanciatedButOne(actualNode.tempVarPosition.keySet())) {
                                OperatorNode opN = new OperatorNode(rete, op, actualNode);
                                opN.stemRule = stemRule;
                                actualNode.addChild(opN);
                                actualNode = opN;
                                VarPosNodes.put(opN, opN.getVarPositions());
                                operators.remove(op);
                                break;
                            } else {
                                if (op.isInstanciated(actualNode.tempVarPosition.keySet())) {
                                    OperatorNode opN = new OperatorNode(rete, op, actualNode);
                                    opN.stemRule = stemRule;
                                    actualNode.addChild(opN);
                                    actualNode = opN;
                                    VarPosNodes.put(opN, opN.getVarPositions());
                                    operators.remove(op);
                                    break;
                                }
                            }
                        }
                    } else {
                    }
                }

            }
        }
        HeadNode hN = null;
        if (r.getHeadType() == Rule.HEAD_TYPE.negative) {
            //We define a headNode and add it to the actualNode (which is the last within this rules joinorder, since we are finsihed now)
            hN = new HeadNodeNegative(r.getHead(), rete, this.VarPosNodes.get(actualNode), actualNode);
            hN.stemRule = stemRule;
        } else if (r.getHeadType() == Rule.HEAD_TYPE.must_be_true) {
            hN = new HeadNode(r.getHead(), rete, this.VarPosNodes.get(actualNode), actualNode);
            hN.stemRule = stemRule;
        }
        hN.r = r;
        actualNode.addChild(hN);

    }
    
    /**
     * 
     * adds a rule into the rete, by creating all needed nodes and connecting them into the right join order.
     * 
     * @param r the rule that should be added
     */
    public void addRule(Rule r){
        stemRule = r;
        
        if( r.getBodyPlus().isEmpty() && r.getBodyMinus().isEmpty()) {
            throw new RuntimeException("Rule "+r+" neither has positive literals nor negative literals.");
        }
        
        //We first add all Atoms of the rule to out retenetwork, so we then can work with the selectionnodes that are already there
        VarPosNodes = new HashMap<Node,HashMap<Variable,Integer>>();
        HashMap<Atom,HashMap<Variable,Integer>> varPositions = new HashMap<Atom, HashMap<Variable,Integer>>();
        
        if(r.getHead()!=null) {
            //System.err.println("Adding head: " + r.getHead() + " - Pred= " + r.getHead().getPredicate() + " - PredArity: " + r.getHead().getPredicate().getArity() + " - Atomarity: " + r.getHead().getArity());
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
        
        Atom actual;
        Node actualNode;
        boolean hasNoPositiveLiteral = false;
        //we choose an atom with which we want to start
        if(!atomsPlus.isEmpty()){
            actual = getBestNextAtom(atomsPlus);
            actualNode = rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        }else{
            actual = getBestNextAtom(atomsMinus);
            actualNode = rete.getBasicLayerMinus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
            hasNoPositiveLiteral = true;
        }
        
         
        // From the actual Atom we easily derive the corresponding node
        //Node actualNode = this.rete.getBasicLayerPlus().get(actual.getPredicate()).getChildNode(actual.getAtomAsReteKey());
        VarPosNodes.put(actualNode, varPositions.get(actual));
        //We cast to selectionNode, since this is always a selection Node
        ((SelectionNode)actualNode).resetVarPosition(actual);

        //We create variables for the partner, the ChoiceNode and HeadNodeConstraint for this rule
        Atom partner;
        ChoiceNode cN = null;
        HeadNodeConstraint constraintNode = null;
        
        
        // immediately create ChoiceNode if rule has no positive body
        boolean doneChoiceNode = false;
        if( hasNoPositiveLiteral && operators.isEmpty() && !r.isConstraint()){
            // if the rule consisted only of one positive atom no operators and has a negative body we have to create the choice and constraint Node here.
            constraintNode = new HeadNodeConstraint(rete, varPositions.get(actual).size());
            cN = new ChoiceNode(rete, varPositions.get(actual).size(),r,varPositions.get(actual), constraintNode);
            cN.stemRule = stemRule;
            actualNode.addChild(cN);
            
            // rule's positive body is always fulfilled, hence add empty instance
            Term [] zero_term = {};
            Instance zero_inst = Instance.getInstance(zero_term, 0, 0);
            cN.addInstance(zero_inst);
            
            doneChoiceNode = true;
        }
        
        
        while(!atomsPlus.isEmpty() || !atomsMinus.isEmpty() || !operators.isEmpty()){
            //While there is still seomthing in the rules body

            if (atomsPlus.isEmpty() && operators.isEmpty() && !r.isConstraint() && !doneChoiceNode) {
                // if atomPlus is now empty  we removed the last atom from here.
                // If there is a negative part and no operators are within this Rule then we now add the ChoiceNode and constraintNode
                // since the positive part of the rule is satisfied now
                constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
                cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(), r, actualNode.tempVarPosition, constraintNode);
                cN.stemRule = stemRule;
                actualNode.addChild(cN);
                doneChoiceNode = true;
            }
            
            if(!atomsPlus.isEmpty()){
                //There is still something within the positive body of the rule --> take it --> it's the new partner
                partner = getBestPartner(atomsPlus, actualNode);
                //Create a joinNode from the actualNode and the partner
                if(actualNode.getClass().equals(SelectionNode.class)){
                    actualNode = createJoin(actual, partner, true,varPositions);
                }else{
                    actualNode = createJoin(actualNode, partner, true,varPositions);
                }
                
            }else{
                if(!operators.isEmpty()){
                    for(Operator op: operators){
                        if(op.getOP().equals(Enumeration.OP.ASSIGN) && op.isInstanciatedButOne(actualNode.tempVarPosition.keySet())){
                            OperatorNode opN = new OperatorNode(rete, op, actualNode);
                            opN.stemRule = stemRule;
                            actualNode.addChild(opN);
                            actualNode = opN;
                            VarPosNodes.put(opN, opN.getVarPositions());
                            operators.remove(op);
                            break;
                        }else{
                            if(op.isInstanciated(actualNode.tempVarPosition.keySet())){
                                OperatorNode opN = new OperatorNode(rete, op, actualNode);
                                opN.stemRule = stemRule;
                                actualNode.addChild(opN);
                                actualNode = opN;
                                VarPosNodes.put(opN, opN.getVarPositions());
                                operators.remove(op);
                                break;
                            }
                        }
                    }
                    if(atomsPlus.isEmpty() && operators.isEmpty() && !atomsMinus.isEmpty() && !r.isConstraint()){
                        // if atomPlus is now empty  we removed the last atom from here.
                        // If there is a negative part and no operators are within this Rule then we now add the ChoiceNode and constraintNode
                        // since the positive part of the rule is satisfied now
                        constraintNode = new HeadNodeConstraint(rete, actualNode.tempVarPosition.size());
                        constraintNode.stemRule = stemRule;
                        cN = new ChoiceNode(rete, actualNode.tempVarPosition.size(),r,actualNode.tempVarPosition, constraintNode);
                        cN.stemRule = stemRule;
                        actualNode.addChild(cN);
                    }
                }else{
                    if(!atomsMinus.isEmpty()){
                        //There is still something within the negative body of the rule --> take it --> it's the new partner
                        partner = getBestPartner(atomsMinus, actualNode);
                        //Create a joinNode from the actualNode and the partner
                        //System.out.println("RULE: " + r);
                        if(actualNode.getClass().equals(SelectionNode.class)){
                            actualNode = createJoinNegative(actual, partner, false,varPositions); // TODO createJoinNegative
                        }else{
                            actualNode = createJoinNegative(actualNode, partner, false,varPositions); // TODO createJoinNegative
                        }
                    }
                }
            }
        }
        //We define a headNode and add it to the actualNode (which is the last within this rules joinorder, since we are finsihed now)
        HeadNode hN = new HeadNode(r.getHead(),rete, this.VarPosNodes.get(actualNode),actualNode);
        hN.r = r;
        hN.stemRule = stemRule;

        actualNode.addChild(hN);
        //If we did contruct a constraintNode we add it to the actual Node as well
        if(constraintNode != null) actualNode.addChild(constraintNode);
        //if we did construct a ChoiceNode we add it to the headNode
        if(cN!=null) hN.addChild(cN);
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
        jn.stemRule = stemRule;
        //JoinNode jn = new JoinNode(aNode,bNode,rete, aNode.getVarPositions(), varPositions.get(b));
        //VarPosNodes.put(jn,jn.getVarPosition(VarPosNodes.get(aNode), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
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
        jn.stemRule = stemRule;
        //JoinNode jn = new JoinNode(aNode,bNode,rete, varPositions.get(a), varPositions.get(b));
        //VarPosNodes.put(jn,jn.getVarPosition(varPositions.get(a), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
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
        jn.stemRule = stemRule;
        //VarPosNodes.put(jn,jn.getVarPosition(VarPosNodes.get(aNode), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
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
        jn.stemRule = stemRule;
        //VarPosNodes.put(jn,jn.getVarPosition(varPositions.get(a), varPositions.get(b)));
        VarPosNodes.put(jn,jn.getVarPositions());
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
        rete.addPredicatePlus(atom.getPredicate());
        if(!rete.getBasicLayerPlus().containsKey(atom.getPredicate())){
            BasicNode bn = new BasicNode(atom.getArity(),rete, atom.getPredicate());
            bn.stemRule = stemRule;
            rete.getBasicLayerPlus().put(atom.getPredicate(), bn);
        }    
        rete.getBasicLayerPlus().get(atom.getPredicate()).AddAtom(atom);
        
    }
    
    /**
     * Adds an atom to the negative memory of the rete network, by creating a new SelectionNode for that atom and adding it as a child to the right basicNode
     * 
     * @param atom the atom you want to add
     */
    public void addAtomMinus(Atom atom){
        rete.addPredicateMinus(atom.getPredicate());
        if(!rete.getBasicLayerMinus().containsKey(atom.getPredicate())){
            BasicNodeNegative bn = new BasicNodeNegative(atom.getArity(),rete, atom.getPredicate());
            bn.stemRule = stemRule;
            rete.getBasicLayerMinus().put(atom.getPredicate(), bn);
        }   
        rete.getBasicLayerMinus().get(atom.getPredicate()).AddAtom(atom);  
    }
}
