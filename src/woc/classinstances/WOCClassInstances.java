/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.Rete;
import Datastructure.Rete.ReteBuilder;
import Datastructure.choice.ChoiceUnit;
import Entity.Atom;
import Entity.Constant;
import Entity.ContextASP;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A simple test class, containing only one rule: p(X,Y) :- s(X),t(Y).
 * nbb defines the number of starting facts for s and t.
 * This class tests how fast the propagation is.
 */
public class WOCClassInstances {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("LOL");
        
        Rule r = new Rule();
        Term[] terms = {Variable.getVariable("X"),Variable.getVariable("Y")};
        Atom head = Atom.getAtom("p",2,terms);
        Term[] terms1 = {Variable.getVariable("X")};
        Term[] terms2 = {Variable.getVariable("Y")};
        Atom body1 = Atom.getAtom("s",1,terms1);
        Atom body2 = Atom.getAtom("t",1,terms2);  
        
        r.setHead(head);
        r.addAtomPlus(body1);
        r.addAtomPlus(body2);
        
        Rete rete = new Rete(new ChoiceUnit(new ContextASP()));
        ReteBuilder rb = new ReteBuilder(rete);
        rb.addRule(r);
        // p(X,Y) :- s(X),t(Y).
        
        Rule r2 = new Rule();
        Atom headR2 = Atom.getAtom("k",2,terms);
        Atom bodyR21 = Atom.getAtom("s",1,terms1);
        Atom bodyR22 = Atom.getAtom("q",1,terms2); 
        r2.setHead(headR2);
        r2.addAtomPlus(bodyR21);
        r2.addAtomPlus(bodyR22);
        rb.addRule(r2);
        
        BasicNode bn = rete.getBasicNodePlus(body1.getPredicate());
        System.out.println("BN = " + bn);
        System.out.println("BN.getChildren = " + bn.getChildNode(body1));
        System.out.println("BN.getChildren.get(body1).getChildren: " + bn.getChildNode(body1.getAtomAsReteKey()).getChildren().size());
    
        int nbb = 1000;
        for(int i = 0; i < nbb; i++){
            Term[] instance = {Constant.getConstant(String.valueOf(i))};
            rete.addInstancePlus(body1.getPredicate(),Instance.getInstance(instance,0));
            rete.addInstancePlus(body2.getPredicate(), Instance.getInstance(instance,0));
        }
        
        /*Term[] instance1 = {Constant.getConstant("a")};
        Term[] instance2 = {Constant.getConstant("b")};
        
        rete.addInstancePlus(body1.getPredicate(), instance1);
        rete.addInstancePlus(body2.getPredicate(), instance2);*/
        
        rete.propagate();
        rete.printAnswerSet(null);
        
        
        System.out.println("LOL = " + Instance.lol);
        
        
    }
}
