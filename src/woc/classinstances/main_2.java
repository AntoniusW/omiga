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
import Manager.Manager;
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
public class main_2 {
    
    
    
    public static void main(String arg[]){
        System.out.println("STARTING THE PROGRAM: " + System.currentTimeMillis());
        //File input = new File("C:\\Users\\User\\Desktop\\Context.txt");
        File input = new File("birds_ASPERIX_nbb=100.txt");
        
        Parser pars = new Parser();
        try {
            ContextASP c = pars.readContext(input);
            //c.printContext();
            
            Manager m = new Manager(c);
            System.out.println("Starting calculation: " + System.currentTimeMillis());
            m.calculate();
            System.out.println("Program finished: " + System.currentTimeMillis());
            
        } catch (FactSizeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RuleNotSafeException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    
}
