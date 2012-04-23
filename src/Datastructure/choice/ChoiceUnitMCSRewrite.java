/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.choice;

import Datastructure.DependencyGraph.DGraph;
import Datastructure.Rete.ChoiceNode;
import Datastructure.Rete.HeadNode;
import Datastructure.Rete.HeadNodeConstraint;
import Datastructure.Rete.Node;
import Datastructure.Rete.Unifyer;
import Entity.Atom;
import Entity.Constant;
import Entity.ContextASP;
import Entity.ContextASPMCSRewriting;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Exceptions.FactSizeException;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import org.jgrapht.graph.DirectedSubgraph;


/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The choiceUnit controls all the choices that are made.
 * 
 * @param memory The DecisionMemory used to store the instances per node per decision level
 * @param choiceNodes a List of ChoiceNodes, such that the choice Unit knows on what to make a choice on
 * @param c The context this choiceUnit should work on
 * 
 * @param stackyNode a stack were we push nodes on which's instance we guessed
 * @param stackybool a stack were we push if the last guess was a positive or negative one
 * @param stackyInstance a stack were we push the instance guessed on
 * 
 * @param nextNode when null a positive guess has to be done on the next possible choice. Otherwise we have to guess negativly on this node with the nextInstance
 * @param nextInstance the instance we have to guess negativly on nextNode if nextNode is not null.
 * 
 * 
 * @param choiceNodesDecisionLayer A Datastructure for storing instances fo choiceNodes per decisionlevel. Theses insatnces have to be added after backtracking
 */
public class ChoiceUnitMCSRewrite extends ChoiceUnitRewrite {
    

    protected ContextASPMCSRewriting c;
    
    public ChoiceUnitMCSRewrite(){
        //System.out.println("ChoiceUnitMCSRewrite is created!");
    }
    
    /**
     * 
     * public constructor. Creates a new ChoiceUnit with initialized data structures.
     * 
     * @param c The ASP Context you want to use this choice unit for
     */
    public ChoiceUnitMCSRewrite(ContextASPMCSRewriting c){
        this.memory = new DecisionMemory();
        this.choiceNodes = new ArrayList<ChoiceNode>();
        this.c = c;
        this.stackyNode = new Stack<ChoiceNode>();
        this.stackybool = new Stack<Boolean>();
        this.stackyInstance = new Stack<Instance>();
        this.choiceNodesDecisionLayer = new ArrayList<HashMap<ChoiceNode,HashSet<Instance>>>();
        this.choiceNodesDecisionLayer.add(new HashMap<ChoiceNode,HashSet<Instance>>());
    }
    
    /**
     * @return if there was a guess that was made, or if there are no more guesses left
     */
    @Override
    public boolean choice(){
        /*System.out.println("CHOICE IS CALLED! : ChoiceUNIT MCS REWRITE");
        System.out.println("SCCMAXSize= " + SCC.size());*/

        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            //nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //We do not have to have constraints node anymore but then we have to kill the instance of the choice node here.
            nextNode.removeInstance(nextInstance);
            Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
            //System.out.println("OLD: Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + nextInstance + " to OUT!");
            //System.out.println("Adding head: " + nextNode.getRule().getHead() + " nextInstance: " + toAdd + " to OUT!");
            this.c.getRete().addInstanceMinus(nextNode.getRule().getHead().getPredicate(), toAdd);
            //System.out.println("LvL: " + this.memory.getDecisonLevel() + "Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!");
            //we push false,nextNode,nextInstance to our stacks, to later, on backtracking, know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            //We return true, since we guessed
            return true;
        }
        
        //We need to do a positive guess
        for(ChoiceNode cN: SCC.get(actualSCC)){
            if(!cN.getAllInstances().isEmpty()){
                //System.out.println("POSITIVE GUESS possible!");
                this.addChoicePoint();
                Instance inz = cN.getAllInstances().get(0);
                //System.out.println("LvL: " + this.memory.getDecisonLevel() + "Guesing on: " + cN.getRule() + " - with VarAsign: " + inz + " to be true!\n" + i);
                for(Atom a: cN.getRule().getBodyMinus()){
                    Instance toAdd = Unifyer.unifyAtom(a,inz, cN.getVarPositions());
                    //System.out.println("Adding: " + toAdd + " to: " + a.getPredicate());
                    c.getRete().addInstanceMinus(a.getPredicate(), toAdd);
                }
                cN.simpleRemoveInstance(inz) ;
                this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).add(inz);
                this.stackyNode.push(cN);
                this.stackybool.push(true); 
                this.stackyInstance.push(inz); 
                //c.printAnswerSet(null);
                return true;
            }
        }
        //try to close SCC
        if(this.closeProcedure()){
            //The SCC could be closed --> start guessing with the next SCC
            return choice();
        }
        
        //We have nothing to guess within our current SCC, therefore we have to do a positive guess in a higher SCC so we go through all SCC if nessacary.
        System.out.println("Reached the next level guess!");
        int x = actualSCC+1;
        while(x < SCC.size()){
            for(ChoiceNode cN: SCC.get(x)){
                if(!cN.getAllInstances().isEmpty()){
                    this.addChoicePoint();
                    Instance inz = cN.getAllInstances().get(0);
                    //System.out.println("LvL: " + this.memory.getDecisonLevel() + "Guesing on: " + cN.getRule() + " - with VarAsign: " + inz + " to be true!\n" + i);
                    for(Atom a: cN.getRule().getBodyMinus()){
                        Instance toAdd = Unifyer.unifyAtom(a,inz, cN.getVarPositions());
                        c.getRete().addInstanceMinus(a.getPredicate(), toAdd);
                    }
                    cN.simpleRemoveInstance(inz) ;
                    this.choiceNodesDecisionLayer.get(memory.getDecisonLevel()).get(cN).add(inz);
                    this.stackyNode.push(cN);
                    this.stackybool.push(true); 
                    this.stackyInstance.push(inz); 
                    return true;
                }
            }
        x++;
        }
        return false; // because no more guess is possible within this context!
    }
    
    
    
    @Override
    protected void closeActualSCC(){
        c.getRete().propagate();
        for(Predicate p: SCCPreds.get(actualSCC)){
           if(c.getRete().containsPredicate(p, false)) {
               //System.out.println("Closing Predicate: " + p);
               if(c.getClosureStatusForOutside(p)){
                   c.getRete().getBasicNodeMinus(p).close();
               }
           }
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());
    }
    
    private boolean closeActualSCCWithReturnValue(){
        c.getRete().propagate();
        for(Predicate p: SCCPreds.get(actualSCC)){
           if(c.getRete().containsPredicate(p, false)) {
               //System.out.println("Closing Predicate: " + p);
               if(c.getClosureStatusForOutside(p)){
                   c.getRete().getBasicNodeMinus(p).close();
               }else{
                   return false;
               }
               
           }
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());
        return true;
    }
    
    @Override
        public void DeriveSCC(){
        SCC = new ArrayList<ArrayList<ChoiceNode>>();
        SCCPreds = new ArrayList<ArrayList<Predicate>>();
        SCCSize = new ArrayList<Integer>();
        actualSCC = 0;
        this.closedAt = new Stack<Integer>();
        DGraph g = new DGraph();
        for(Rule r: c.getAllRules()){
            g.addRule(r);
        }
        //g.gd.addEdge(Predicate.getPredicate("p",2), Predicate.getPredicate("s",1));
        for(int i = 0; i < g.getSortedSCCs().size();i++){
            SCC.add(new ArrayList<ChoiceNode>());
            SCCPreds.add(g.getSortedSCCs().get(i));
            //SCCSize.add(g.getSortedSCCs().get(i).size());
            for(int j = 0; j < g.getSortedSCCs().get(i).size();j++){
               for(ChoiceNode cN: this.choiceNodes){
                   if(cN.getRule().getHead().getPredicate().equals(g.getSortedSCCs().get(i).get(j))){
                       SCC.get(i).add(cN);
                   }
               }
            }
             SCCSize.add(SCC.get(i).size());
        }
        
    }
    
    private boolean closeProcedure(){
        boolean flag = false;
        while(this.SCCSize.get(actualSCC) <= 1 && this.closeActualSCCWithReturnValue()){
            flag = true;
        }
        return flag;
    }
    
}
