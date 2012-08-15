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
import network.Pair;
import org.jgrapht.graph.DirectedSubgraph;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


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
    

    public ContextASPMCSRewriting c;
    public Stack<Pair<Integer,Predicate>> closedfromoutside;
    
    public ChoiceUnitMCSRewrite(){
        //super();
        //System.out.println("ChoiceUnitMCSRewrite is created!");
    }
    
    /**
     * 
     * public constructor. Creates a new ChoiceUnit with initialized data structures.
     * 
     * @param c The ASP Context you want to use this choice unit for
     */
    public ChoiceUnitMCSRewrite(ContextASPMCSRewriting c){
        super(c);
        this.memory = new DecisionMemory();
        this.choiceNodes = new ArrayList<ChoiceNode>();
        this.c = c;
        this.stackyNode = new Stack<ChoiceNode>();
        this.stackybool = new Stack<Boolean>();
        this.stackyInstance = new Stack<Instance>();
        this.choiceNodesDecisionLayer = new ArrayList<HashMap<ChoiceNode,HashSet<Instance>>>();
        this.choiceNodesDecisionLayer.add(new HashMap<ChoiceNode,HashSet<Instance>>());
        this.closedfromoutside = new Stack<Pair<Integer,Predicate>> ();
    }
    
    /**
     * @return if there was a guess that was made, or if there are no more guesses left
     */
    @Override
    public boolean choice(){
        //System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
        //System.out.println("CHOICE IS CALLED! : ChoiceUNIT MCS REWRITE");
        /*System.out.println("SCCMAXSize= " + SCC.size());
        System.out.println("Current SCC = "+actualSCC);*/
        
        if(actualSCC >= SCC.size()) {
            //System.out.println("ChoiceUnitMCSRewrite.choice: actualSCC >= SCC.size().");
            return false;
        }
        
        //We need to do a positive guess
        for(ChoiceNode cN: SCC.get(actualSCC)){
            if(!cN.getAllInstances().isEmpty()){
                //System.out.println("POSITIVE GUESS possible!");
                this.addChoicePoint();
                //System.out.println("ChoiceUnitMCSRewrite.choice: choice point added.");
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
        if(this.closeActualSCCWithReturnValue()){
            this.closeProcedure();
            //The SCC (and maybe follwing SCCs) could be closed --> start guessing with the next SCC
            return choice();
        }
        //System.out.println("Going into higher SCC!");
        //We have nothing to guess within our current SCC, therefore we have to do a positive guess in a higher SCC so we go through all SCC if nessacary.
        //System.out.println("Reached the next level guess!");
        int x = actualSCC+1;
        while(x < SCC.size()){
            for(ChoiceNode cN: SCC.get(x)){
                if(!cN.getAllInstances().isEmpty()){
                    this.addChoicePoint();
                    Instance inz = cN.getAllInstances().get(0);
                    //System.out.println("LvL: " + this.memory.getDecisonLevel() + ". Guesing on: " + cN.getRule() + " - with VarAsign: " + inz + " to be true!\n" + i);
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
    public boolean nextBranch(){
        //System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
        //System.out.println("nextBranch called!");
        if(this.nextNode != null){
            //There is a next node. This means we have to make a negative guess since we returned at this point because of backtracking
            //We add a choicepoint since we are doing a guess
            this.addChoicePoint();
            //we activate a constraint for this rule. Since this rule must not be true anymore, as we guessed it to be false
            //nextNode.getConstraintNode().saveConstraintInstance(nextInstance);
            //We do not have to have constraints node anymore but then we have to kill the instance of the choice node here.
            nextNode.removeInstance(nextInstance);
            //This is the code that should work properly
            nextNode.getConstraintNode().addInstance(nextInstance, true);
            if(nextNode.getRule().isHeadFixed()) {
                //this is an optimisation: We put the rules head out iff there is no other way of it beeing derived by the rwritten program
                Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
                this.c.getRete().addInstanceMinus(nextNode.getRule().getHead().getPredicate(), toAdd);
            }
            //This is the code when adding head on negative guesses in the buggy version
            //Instance toAdd = Unifyer.unifyAtom(nextNode.getRule().getHead(), nextInstance, nextNode.getVarPositions());
            //this.c.getRete().addInstanceMinus(nextNode.getRule().getHead().getPredicate(), toAdd);
            
            
            //System.out.println("LvL: " + this.memory.getDecisonLevel() + ". Guesing on: " + nextNode.getRule() + " - with VarAsign: " + nextInstance + " to be false!");
            //we push false,nextNode,nextInstance to our stacks, to later, on backtracking, know that we did a negative guess on this instance for this node
            this.stackybool.push(false);
            this.stackyNode.push(nextNode);
            this.stackyInstance.push(nextInstance);
            //We set nextNode=null. So the next guess will be a positive one if no backtracking is apllied in between
            nextNode = null;
            //We return true, since we guessed
            return true;
        }
        return false; // there is no next alternative
    }
    
    /*@Override
    public boolean choice(){

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
    }*/
    
    
    @Override
    protected void closeActualSCC(){
        // TODO AW fix/delete
        if(true) throw new NotImplementedException();
        c.getRete().propagate(); //think this is not really needed anymore
        for(Predicate p: SCCPreds.get(actualSCC)){
            if(!c.getClosureStatusForOutside(p)){
                return;
            }
        }
        for(Predicate p: SCCPreds.get(actualSCC)){
           //if(c.getRete().containsPredicate(p, false)) {
               //System.out.println("Closing Predicate: " + p);
                   c.getRete().getBasicNodeMinus(p).close();
           //}
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());
        c.getRete().propagate();
       /* c.getRete().propagate();
        for(Predicate p: SCCPreds.get(actualSCC)){
           if(c.getRete().containsPredicate(p, false)) {
               //System.out.println("Closing Predicate: " + p);
               if(c.getClosureStatusForOutside(p)){
                   c.getRete().getBasicNodeMinus(p).close();
               }
           }
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());*/
    }
    
    private boolean closeActualSCCFromOutside(){
        //System.out.println("Closing SCC! lv: " + this.actualSCC + " : " + this.SCCPreds.get(this.actualSCC));
        for(ChoiceNode cN: SCC.get(actualSCC)){
            if(!cN.getAllInstances().isEmpty()){
                //System.out.println("Returning false because we still have choicepoints!; " + cN);
                return false;
            }
        }
        c.getRete().propagate(); //think this is not really needed anymore
        for(Predicate p: SCCPreds.get(actualSCC)){
            if(!c.getClosureStatusForOutside(p)){
                //System.out.println("Returning false because: " + !c.getClosureStatusForOutside(p) + " - " + p);
                //System.out.println("Returning false because its not closed from outside; " + p);
                return false;
            }
        }
        for(Predicate p: SCCPreds.get(actualSCC)){
           //if(c.getRete().containsPredicate(p, false)) {
               //System.out.println("Closing Predicate: " + p);
                //System.out.println("Closing: " + p);
                   c.getRete().getBasicNodeMinus(p).close();
           //}
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());
        c.getRete().propagate();
        return true;
    }
    
    private boolean closeActualSCCWithReturnValue(){
        //System.out.println("Closing SCC! lv: " + this.actualSCC + " : " + this.SCCPreds.get(this.actualSCC));
        c.getRete().propagate(); //think this is not really needed anymore
        for(Predicate p: SCCPreds.get(actualSCC)){
            if(!c.getClosureStatusForOutside(p)){
                //System.out.println("Returning false because: " + !c.getClosureStatusForOutside(p) + " - " + p);
                return false;
            }
            //else{
            //    System.out.println(p +" :  "+ c.getClosureStatusForOutside(p));
            //}
        }
        for(Predicate p: SCCPreds.get(actualSCC)){
           //if(c.getRete().containsPredicate(p, false)) {
               //System.out.println("Closing Predicate: " + p);
                   c.getRete().getBasicNodeMinus(p).close();
           //}
        }
        this.actualSCC++;
        this.closedAt.add(this.memory.getDecisonLevel());
        c.getRete().propagate();
        return true;
    }
    
    @Override
    public boolean killSoloSCC(){
        //System.out.println("Killing: Calling close SCC!");
        if(SCCSize.isEmpty()) {
            return false;
        }
        while(SCCSize.get(actualSCC)<=1){
            if(SCCSize.get(actualSCC)==1 && SCCPreds.get(actualSCC).size() > 1) break;
            if(this.closeActualSCCWithReturnValue()) break;
            //System.out.println("Calling close SCC!");
            if(actualSCC >= SCC.size()) return false;
        }
        return true;
    }
    
    @Override
        public void DeriveSCC(){
        //System.out.println("DeriveSCC called.");
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
        
        /*
        System.out.println("DGraph initialized. SCCSize: " + this.SCC.size());
        for(int i = 0; i < this.SCC.size();i++){
            System.out.println("SCC" + i + " is of size: " + this.SCCSize.get(i));
        }
        for(int i = 0; i < this.SCC.size();i++){
            System.out.println("SCC" + i + " is of size: " + this.SCCSize.get(i) + " :::: " + this.SCC.get(i));
        }
        int i = 0;
        for(DirectedSubgraph gsg: g.getSCCs()){
            i++;
            System.out.println("SCC: " + i);
            System.out.println(gsg.vertexSet());
        }*/
        
    }
    
    private boolean closeProcedure(){
        boolean flag = false;
        //System.out.println("CloseProcedure: " + this.SCCSize.get(actualSCC) + " - " + this.closeActualSCCWithReturnValue());
        while(actualSCC < this.SCCSize.size() && this.SCCSize.get(actualSCC) <= 1 ){
            if(this.SCCPreds.size() > 1) break;
            if(this.closeActualSCCWithReturnValue()) flag = true;
        }
        return flag;
    }
    
    public void pushClosureFromOutside(Predicate p){
       
            this.closedfromoutside.push(new Pair<Integer,Predicate>(this.getDecisionLevel(),p));
        
            //System.out.println("SCCSIZE: " + this.SCCPreds.size() + " vs. " + this.actualSCC);
        if (this.actualSCC < this.SCCPreds.size()){
            
            this.closeActualSCCFromOutside();
            //System.out.println("CloseProcedure: " + this.SCCSize.get(actualSCC) + " - " + this.closeActualSCCWithReturnValue());
            while(actualSCC < this.SCCSize.size() && this.closeActualSCCFromOutside()){
            }
        }
    }
    
    @Override
    public void backtrack(){
        //System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
        //System.out.println("BACKTRACK CALLED!: " + this.getDecisionLevel());
        if(this.getMemory().getDecisonLevel() == 0) return; // there is nothing to backtrack since there was no guess.
        this.backtrackchoiceNodesDecisionLayer();
        this.memory.backtrack();
        
        //backtrack closure from outside
        if(!this.closedfromoutside.isEmpty() && this.closedfromoutside.peek().getArg1() >= this.getDecisionLevel()){
            Pair<Integer,Predicate> pa =this.closedfromoutside.pop();
            this.c.openFactFromOutside(pa.getArg2());
        }
        
        //TODO: backtracking on SCC
        while(!this.closedAt.isEmpty() && this.closedAt.peek() >= this.memory.getDecisonLevel()){
                this.openActualSCC();
        }
        
        if(this.stackybool.pop()){
            //Positive Guess
            //System.out.println("Last was a POSITVE Guess");
            //We take the node and instacne from the last guess and set them as nextNode/nextInstance.
            //This will lead the enxt guess to guess negative on this node and insatnce when it is called the nxt time.
            nextNode = stackyNode.pop();
            nextInstance = stackyInstance.pop();
            //nextNode.simpleRemoveInstance(nextInstance);
        }else{
            //Negative Guess
            //System.out.println("Last was a NEGATIVE Guess");
            if(memory.getDecisonLevel() > 0) {
                stackyNode.pop();
                stackyInstance.pop();
                //This was not the last guess. Add the last guess back tino the choicenode (since it can be reguessed in the other branch of the guess before)
                //this.stackyNode.pop().simpleAddInstance(this.stackyInstance.pop()); // TODO: Is this needed? We add the choice back into the choice node
                //backtrack(); // TODO: if this is a standalone calculation we can imideatly backtrack once more, since nothing else is possible.
            }else{
                //else we are finished with guessing and at decisoonlevel 0 therefore no more backtracking is needed

                //We set rete.unsatisfiable since such that this is not seen as an answerset
                // We empty all choicenodes such that the guessing is over
                //System.out.println("BACKTRACKING setzt unsat!");
                //this.c.getRete().satisfiable = false;
                for(ChoiceNode cN: this.choiceNodes){
                    Stack<Instance> stacky = new Stack<Instance>();
                    for(Instance inz: cN.getAllInstances()){
                        stacky.push(inz);
                    }
                    while(!stacky.empty()){
                        cN.simpleRemoveInstance(stacky.pop());
                    }
                    
                }
            }
        }
    }
    
    public void addExternalNode(){
        this.addChoicePoint();
        this.stackyInstance.push(null);
        this.stackyNode.push(null);
        this.stackybool.push(false);
    }
    
}
