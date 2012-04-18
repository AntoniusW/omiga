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
    
}
