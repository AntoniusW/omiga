/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;


import Datastructure.Rete.HeadNode;
import Datastructure.Rete.Rete;
import Datastructure.Rewriting.Rewriter_easy;
import Datastructure.Rewriting.Rewriter_easyMCS;
import Datastructure.choice.ChoiceUnitRewrite;
import Entity.*;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import Manager.Manager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import parser.Parser;
import parser.ParserRewrite;
import parser.antlr.*;


/**
 *
 * @author User
 */
public class main_FINAL_NormalASP {

    
    private static String filter;
    private static int rewriting;
    private static String filename;
    private static Integer answersets;
    private static boolean outprint;
    
    public static void main(String args[]) /*throws FactSizeException, RuleNotSafeException*/{
        long start = System.currentTimeMillis();
        /*filename = args[0];
        for(String s: args){
            System.out.println(s);
        }*/
        
        //filename = "birds_ASPERIX_nbb=100.txt";
        //filename = "StableMarriage/01-stable_marriage-0-0.txt";
        //filename = "funcTermTest.txt";
        //filename = "Hanoi\\00-hanoi.txt";
        //filename = "maze generation\\00-maze_genearation.txt";
        //filename = "maze generation\\01-maze_generation-0-0.txt";
        //filename = "Partner Units Polynomial/00-partner.txt";
        //filename = "Partner Units Polynomial/07-partner_units_polynomial-28-0.txt";
        //filename = "graph colouring/00-graph-colouring.txt";
        //filename ="single_import_pos.txt";
        filename ="single_import_neg.txt";
        //filename = "Xir2.txt";
        rewriting = 1;
        answersets = 600;
        filter = null;
        outprint =true;
        
        //GlobalSettings.getGlobalSettings().setStringbasedHashCode(true); // try non-string hash codes
        
        // create context
        ContextASPRewriting ctx = new ContextASPRewriting();
        // parsing with ANTLR
        try {
            // setting up lexer and parser
            wocLexer lex = new wocLexer(new ANTLRFileStream(filename));
            CommonTokenStream tokens = new CommonTokenStream(lex);
            wocParser parser = new wocParser(tokens);
        
            // set context
            parser.setContext(ctx);
            
            try {
                try {
                    // parse input
                    parser.woc_program();
                    
                    System.out.println("Parsed program, starting rewriting...");
                    
                    Rewriter_easy rewriter = new Rewriter_easy();
                    ctx = rewriter.rewrite(ctx);
                    
                    System.out.println("Read in program is: ");
                    ctx.printContext();
                } catch (RuleNotSafeException ex) {
                    Logger.getLogger(main_FINAL_NormalASP.class.getName()).log(Level.SEVERE, null, ex);
                } catch (FactSizeException ex) {
                    Logger.getLogger(main_FINAL_NormalASP.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            } catch (RecognitionException ex) {
                Logger.getLogger(main_FINAL_NormalASP.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(main_FINAL_NormalASP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //File input = new File(filename);


        
        System.out.println("STARTING: " + filename + "Answersets2Derive: " + answersets + "rewriting="+rewriting + "-filter= " + filter + " StartingTime: " + System.currentTimeMillis());
        
        
        Manager m = new Manager(ctx);
        long beforeCalc = System.currentTimeMillis();
        m.calculate(answersets,outprint,filter);

        System.out.println("Termianted final Calculation");
        System.out.println("Time needed overAll: " + (1.0F*(System.currentTimeMillis()-start)/1000));
        System.out.println("Time needed for calculation: " +(1.0F*(System.currentTimeMillis()-beforeCalc)/1000));
    }
    
    private static void help(){
        System.out.println("java -jar woc <filename> [-answersets=X, -filter=predicateName -rewriting=0-2 | 0=No rewriting, 1=Rewriting, 2=AlreadyRewritten]");
    }
    
}
