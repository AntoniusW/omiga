package Datastructure.Rete;

import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

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
    
    //@Override
    public void addInstanceFoo(Instance instance, Node fromNode){
        boolean from;
        if( a == fromNode) {
            from = true;
        } else if ( b == fromNode ) {
            from = false;
        } else {
            throw new RuntimeException("JoinNode received input from unknown parent node");
        }
        //System.out.println("Instance: " + instance + " reaches JoinNode: " + this + " from: " + fromNode);
        
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
        } else {
            //the instance came from node b
            //selectFromHere = a;
            for(int i = 0; i < selectionCriterion1.length;i++){
                if (selectionCriterion1[i] == null){
                    selCrit1[i] = tempVar;
                }else{
                    selCrit1[i] = instance.get( selectionCriterion1[i]);
                }
            }
        }
            
        //selectionCriteria = selCrit2;
        Collection<Instance> joinPartners;
        if (from) {
            // The new instance arived from right, therefore we to push all instances of the select from left to the children
            joinPartners = b.select(selCrit2);
        } else {
            joinPartners = a.select(selCrit1);
        }
        //System.out.println("selCrit: " + Instance.getInstanceAsString(selCrit2));
        //System.out.println("JoinNodeNegative has this many children: " + this.children);
        for (Instance inz : joinPartners) {
            
            Term[] toAdd = new Term[this.instanceOrdering.length];
            for (int i = 0; i < this.instanceOrdering.length; i++) {
                if (from) {
                    if (instanceOrdering[i] < 0) {
                        toAdd[i] = inz.get((instanceOrdering[i] * -1) - 1);
                    } else {
                        toAdd[i] = instance.get(instanceOrdering[i] - 1);
                    }
                } else {
                    if (instanceOrdering[i] >= 0) {
                        toAdd[i] = inz.get(instanceOrdering[i] - 1);
                    } else {
                        toAdd[i] = instance.get((instanceOrdering[i] * -1) - 1);
                    }
                }
            }
            Instance tempInst = Instance.getInstance(toAdd, 0, 0);
            
            if( !from ) {
                Instance inst2add = newInstanceFromJoin(instance, inz, tempInst/*instance*/);
                //Instance inst2add = newInstanceFromJoin(instance, selInst, instance);
                this.memory.addInstance(inst2add);
                //System.out.println(this + " instance added: " + instance + " because of: " + a + " saying: " + a.containsInstance(Instance.getInstance(selCrit1)) + " to: " + Instance.getInstance(selCrit1));
                //this.rete.getChoiceUnit().addInstance(this, instance);
                for (Node child : children) {
                    sendInstanceToChild(inst2add, child);
                }
                break;
            }

            Instance inst2add = newInstanceFromJoin(instance, inz, tempInst);

            this.memory.addInstance(inst2add);

            for (Node child : children) {
                sendInstanceToChild(inst2add, child);
            }
        }
    }
    
    @Override
    public void addInstance(Instance instance, Node fromNode){
        if( true ) {
            addInstanceFoo(instance, fromNode);
            return;
        }
        boolean from; // TODO: check boolean value
        if( a == fromNode) {
            from = true;
        } else if ( b == fromNode ) {
            from = false;
        } else {
            throw new RuntimeException("JoinNode received input from unknown parent node");
        }
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
            //System.out.println("selCrit: " + Instance.getInstanceAsString(selCrit2));
            //System.out.println("JoinNodeNegative has this many children: " + this.children);
            for(Instance inz: joinPartners){       
                
                Instance inst2add = newInstanceFromJoin(instance, inz, inz/*tempInst*/);
                
                this.memory.addInstance(inst2add);
                //this.rete.getChoiceUnit().addInstance(this, inz);
                //System.out.println("TEMPVARPOSITION: " + this.tempVarPosition);
                //System.out.println(this + " instance added: " + inz + " JOINPARTNERS: " + joinPartners + " because of: " + instance + " from: " + from);
                
                for (Node child : children) {
                    sendInstanceToChild(inst2add, child);
                }
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
            //Instance selInst = Instance.getInstance(selCrit1,0,0);
            
            // we need to select a join partner to get correct decision level
            Collection<Instance> joinPartners = a.select(selCrit1);
            if(//a.containsInstance(selInst)
                    !joinPartners.isEmpty()){
                Iterator<Instance> it = joinPartners.iterator();
                Instance inst2add = newInstanceFromJoin(instance, it.next(), instance);
                //Instance inst2add = newInstanceFromJoin(instance, selInst, instance);
                this.memory.addInstance(inst2add);
                //System.out.println(this + " instance added: " + instance + " because of: " + a + " saying: " + a.containsInstance(Instance.getInstance(selCrit1)) + " to: " + Instance.getInstance(selCrit1));
                //this.rete.getChoiceUnit().addInstance(this, instance);
                for (Node child : children) {
                    sendInstanceToChild(inst2add, child);
                }
            }
        }
        
        
    }
    
    public void informOfClosure(SelectionNodeNegative sN) {
        boolean from;
        if (a == sN) {
            from = true;
        } else if (b == sN) {
            from = false;
        } else {
            throw new RuntimeException("JoinNode received input from unknown parent node");
        }
        ArrayList<Instance> temp;
        //System.out.println("Closure of: " + sN);
        //TODO: A closed notification always come from the right side! just kick this if statement. No need for bool nor sN
        Node actual; // The node that is not closed
        if (from) {
            // the right partner is closed
            actual = b;
        } else {
            //the left partner is closed
            actual = a;
        }
        temp = actual.memory.getAllInstances();

        for (int i = 0; i < temp.size(); i++) {
            
            // return if join should create new variable bindings
            // TODO: fix this to create bindings, if it makes any sense
            if( temp.get(0).getTerms().length != tempVarPosition.size()) {
                return;
            }

            for (int j = 0; j < selectionCriterion1.length; j++) {
                if (selectionCriterion1[j] == null) {
                    selCrit1[j] = tempVar;
                } else {
                    selCrit1[j] = temp.get(i).get(selectionCriterion1[j]);
                }
            }

            if (rete.getBasicNodePlus(sN.getAtom().getPredicate()) == null
                    || rete.getBasicNodePlus(sN.getAtom().getPredicate()).getChildNode(sN.getAtom()) == null
                    || sN.containsInstance((Instance.getInstance(selCrit1, 0, 0)))) {

                if (!this.memory.containsInstance(temp.get(i))) {
                    //Instance Inst = new Instance(temp.get(i));
                    // create instance with DL and PL set correctly
                    Instance tempInst = Instance.getInstance(temp.get(i).getTerms(), 0, rete.getChoiceUnit().getDecisionLevel());
                    Instance inst2Add = newInstanceFromJoin(tempInst, temp.get(i), tempInst);
                    memory.addInstance(inst2Add);
                    //memory.addInstance(temp.get(i));
                    //super.addInstance(temp.get(i)); // register the adding of this variableassignment within the choiceUnit
                    //System.out.println("Dervied newly through Closure: " + temp.get(i) + " to " + this);
                    for (Node child : children) {
                        sendInstanceToChild(inst2Add, child);
                        //sendInstanceToChild(temp.get(i), child);
                    }
                }
            }
        }
    }
    
    
}
