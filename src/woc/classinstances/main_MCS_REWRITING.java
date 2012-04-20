/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Datastructure.Rete.HeadNode;
import Datastructure.Rete.Rete;
import Datastructure.Rewriting.Rewriter_easy;
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
        File input = new File("Z.txt");
        //File input = new File("3Col\\3Col6AS.txt");
        
        ParserMCSRewrite pars = new ParserMCSRewrite();
        try {
            ContextASP c = pars.readContext(input);
            Rewriter_easy rewriter = new Rewriter_easy();
            c = rewriter.rewrite(c);
            c.printContext();
            
            Manager m = new Manager(c);
            System.out.println("Starting calculation: " + System.currentTimeMillis());
            m.calculate(null, false, null);
            System.out.println("Program finished: " + System.currentTimeMillis());
            
        } catch (FactSizeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleNotSafeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("TERMINATED MCS CALC!");
        
        
    }
    
}
