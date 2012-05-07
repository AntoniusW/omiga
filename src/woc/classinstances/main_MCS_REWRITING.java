/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Datastructure.Rete.HeadNode;
import Datastructure.Rete.Rete;
import Datastructure.Rewriting.Rewriter_easy;
import Datastructure.Rewriting.Rewriter_easyMCS;
import Entity.Constant;
import Entity.ContextASP;
import Entity.ContextASPMCS;
import Entity.ContextASPMCSRewriting;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Predicate;
import Entity.Variable;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import Manager.Manager;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.Parser;
import parser.ParserMCS;
import parser.ParserMCSRewrite;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The main main class of this project. Use it to read in a context via the File specified by input.
 * 
 */
public class main_MCS_REWRITING {
    
    
    
    public static void main(String arg[]){
        System.out.println("STARTING THE PROGRAM: " + System.currentTimeMillis());
        //File input = new File("Z:\\DLV\\3col.txt");
        File input = new File("MCSTest.txt");
        //File input = new File("3Col\\3Col6AS.txt");
        
        ParserMCSRewrite pars = new ParserMCSRewrite();
        try {
            ContextASPMCSRewriting c = pars.readContext(input);
            Rewriter_easyMCS rewriter = new Rewriter_easyMCS();
            c = rewriter.rewrite(c);
            //((ContextASPMCSRewriting) c).registerFactFromOutside(Predicate.getPredicate("s", 1));
            ((ContextASPMCSRewriting) c).registerFactFromOutside(Predicate.getPredicate("p", 1));
            c.printContext();
            
            c.propagate();
            c.getChoiceUnit().DeriveSCC();
            
            System.out.println("Before any choice");
            System.out.println(c.deriveNewFacts(0));
            
            c.choice();
            c.propagate();
            System.out.println("After choice1 with decisionlevel 0");
            System.out.println(c.deriveNewFacts(0));
            System.out.println("After choice1 with decisionlevel 1");
            System.out.println(c.deriveNewFacts(1));
            
            /*Term[] terms = new Term[1];
            terms[0] = Constant.getConstant("0");
            Instance inz = Instance.getInstance(terms);
            c.addFactFromOutside(Predicate.getPredicate("p", 1), inz);
            c.printAnswerSet(null);
            c.propagate();*/
            
            
            
            /*System.out.println("Decisonlevel @ STartUp: " + c.getDecisionLevel());
            c.choice();
            c.propagate();
            c.printAnswerSet(null);
            Term[] terms = new Term[1];
            terms[0] = Constant.getConstant("2");
            Instance inz = Instance.getInstance(terms);
            //c.addFact2IN(Predicate.getPredicate("t", 1), inz);
            c.addFactFromOutside(Predicate.getPredicate("t", 1), inz);
            c.printAnswerSet(null);
            System.out.println("C is sat: " + c.isSatisfiable());
            System.out.println("Decisonlevel: " + c.getDecisionLevel());
            c.backtrack();
            c.backtrack();
            System.out.println("NEXT ALTERNATIVE: " + c.nextBranch());
            c.printAnswerSet(null);
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            System.out.println(c.choice());
            c.printAnswerSet(null);
            System.out.println("Decisonlevel: " + c.getDecisionLevel());
            System.out.println("C is sat: " + c.isSatisfiable());
            
            Manager m = new Manager(c);
            System.out.println("Starting calculation: " + System.currentTimeMillis());
            //m.calculate(null, false, null);
            System.out.println("Program finished: " + System.currentTimeMillis());*/
            
        } catch (FactSizeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleNotSafeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("TERMINATED MCS CALC!");
        
        
    }
    
}
