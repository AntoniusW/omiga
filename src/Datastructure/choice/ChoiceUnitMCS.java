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
import Entity.ContextASPMCS;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Exceptions.FactSizeException;
import Interfaces.Context;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import org.jgrapht.graph.DirectedSubgraph;


public class ChoiceUnitMCS extends ChoiceUnit{
    
    ContextASPMCS c;
    
    public ChoiceUnitMCS(ContextASPMCS c){
        super();
        this.memory = new DecisionMemory();
        this.choiceNodes = new ArrayList<ChoiceNode>();
        this.c = c;
        this.stackyNode = new Stack<ChoiceNode>();
        this.stackybool = new Stack<Boolean>();
        this.stackyInstance = new Stack<Instance>();
        this.choiceNodesDecisionLayer = new ArrayList<HashMap<ChoiceNode,HashSet<Instance>>>();
        this.choiceNodesDecisionLayer.add(new HashMap<ChoiceNode,HashSet<Instance>>());
    }
    

    private boolean closeActualSCC(){
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
    
    /**
     * 
     * 
     * 
     * @return if there was a guess that was made, or if there are no more guesses left
     */
    int i = 0; //TODO: Remove this counter
    public boolean choice(){
        //System.out.println("CHOICE IS CALLED!");
        //System.out.println("Choice is called!");
        //TODO: replace foreach loops with iterator loops.
        i++;
        //System.out.println("Choice is called! " + this.memory.getDecisonLevel());
        //this.printAllChoiceNodes();
        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //System.out.println("LvL: " + this.memory.getDecisonLevel() + "Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!\n" + i);
            //we push false,nextNode,nextInstance to our stacks, to later on backtracking know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            
            //We return true, since we guessed
            return true;
        }
        
        
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
                    return true;
                }
            }
        //try to close SCC
        if(this.closeProcedure()){
            return choice();
        }
        
        //We need to do a positive guess
        int x = actualSCC+1;
        while(x < SCC.size()){
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
                    return true;
                }
            }
        x++;
        }
        return false; // because no more guess is possible within this context!
    }
    
    private boolean closeProcedure(){
        boolean flag = false;
        while(this.SCCSize.get(actualSCC) <= 1 && this.closeActualSCC()){
            flag = true;
        }
        return flag;
    }
    
    
}
