/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

import Entity.ContextASP;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Manager.Manager;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.Parser;

/**
 *
 * @author User
 */
public class mainJar {
    
    public static void main(String args[]){
        File input = new File(args[0]);
        //File input = new File("stratProg\\strat500.txt");
        
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
