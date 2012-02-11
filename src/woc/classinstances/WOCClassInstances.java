/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.Rete;
import Entity.Atom;
import Entity.Constant;
import Entity.Instance;
import Entity.Rule;
import Entity.Variable;
import Interfaces.Term;

/**
 *
 * @author User
 */
public class WOCClassInstances {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("LOL");
        
        Rule r = new Rule();
        Term[] terms = {Variable.getVariable("X"),Variable.getVariable("Y")};
        Atom head = new Atom("p",2,terms);
        Term[] terms1 = {Variable.getVariable("X")};
        Term[] terms2 = {Variable.getVariable("Y")};
        Atom body1 = new Atom("s",1,terms1);
        Atom body2 = new Atom("t",1,terms2);  
        
        r.setHead(head);
        r.addAtomPlus(body1);
        r.addAtomPlus(body2);
        
        Rete rete = new Rete();
        rete.addRule(r);
        // p(X,Y) :- s(X),t(Y).
        
        Rule r2 = new Rule();
        Atom headR2 = new Atom("k",2,terms);
        Atom bodyR21 = new Atom("s",1,terms1);
        Atom bodyR22 = new Atom("q",1,terms2); 
        r2.setHead(headR2);
        r2.addAtomPlus(bodyR21);
        r2.addAtomPlus(bodyR22);
        rete.addRule(r2);
        
        BasicNode bn = rete.getBasicNodePlus(body1.getPredicate());
        System.out.println("BN = " + bn);
        System.out.println("BN.getChildren = " + bn.getChildren().get(body1));
        System.out.println("BN.getChildren.get(body1).getChildren: " + bn.getChildren().get(body1.getAtomAsReteKey()).getChildren().size());
    
        int nbb = 3000;
        for(int i = 0; i < nbb; i++){
            Term[] instance = {Constant.getConstant(String.valueOf(i))};
            rete.addInstancePlus(body1.getPredicate(),Instance.getInstance(instance));
            rete.addInstancePlus(body2.getPredicate(), Instance.getInstance(instance));
        }
        
        /*Term[] instance1 = {Constant.getConstant("a")};
        Term[] instance2 = {Constant.getConstant("b")};
        
        rete.addInstancePlus(body1.getPredicate(), instance1);
        rete.addInstancePlus(body2.getPredicate(), instance2);*/
        
        rete.propagate();
        rete.printAnswerSet();
        
        
        System.out.println("LOL = " + Instance.lol);
        
        
    }
}
