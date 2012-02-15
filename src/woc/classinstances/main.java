/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Datastructure.Rete.HeadNode;
import Datastructure.Rete.Rete;
import Entity.ContextASP;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Predicate;
import Entity.Variable;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.Parser;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The main main class of this project. Use it to read in a context via the File specified by input.
 * 
 */
public class main {
    
    
    
    public static void main(String arg[]){
        
        //File input = new File("C:\\Users\\User\\Desktop\\Context.txt");
        File input = new File("context_broken.txt");
        
        Parser pars = new Parser();
        try {
            ContextASP c = pars.readContext(input);
            c.printContext();
            
            //c.initializeRete();

            c.propagate();
            
            c.printAnswerSet();
            
            System.out.println("Instances created = " + Instance.lol);
            System.out.println("Instances added to the Network = " + Rete.omg);
            System.out.println("Instances added through the fullfillment of a rule = " + HeadNode.arg);
            System.out.println("SATISFIABLE: " + c.getRete().satisfiable);
            
        } catch (FactSizeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleNotSafeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
}
