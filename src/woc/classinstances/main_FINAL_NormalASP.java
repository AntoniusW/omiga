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
import org.antlr.runtime.UnbufferedTokenStream;
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
        
        System.out.println("OMiGA -- An Open Minded Grounding on-the-fly Answer-Set Solver.");
        
        if(args.length == 0 ) {
            help();
            return;
        }
       
        //filename = "birds_ASPERIX_nbb=100.txt";
        //filename = "StableMarriage/01-stable_marriage-0-0.txt";
        //filename = "scholarship.txt";
       // filename = "Hanoi\\00-hanoi.txt";
        //filename = "maze generation\\00-maze_genearation.txt";
        //filename = "maze generation\\01-maze_generation-0-0.txt";
        //filename = "Partner Units Polynomial/00-partner.txt";
        //filename = "Partner Units Polynomial/07-partner_units_polynomial-28-0.txt";
        //filename = "graph colouring/00-graph-colouring.txt";
        //filename ="single_import_pos.txt";
        //filename ="single_import_neg.txt";
        //filename = "Xir2.txt";
        
        filename = args[0];
        rewriting = 1;
        answersets = 1;
        filter = null;
        outprint =true;
        
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(args[i].startsWith("-filter=")) {
                filter = args[i].substring("-filter=".length());
            }
            if(args[i].startsWith("-answersets=")) {
                answersets = Integer.parseInt(args[i].substring("-answersets=".length()));
            }
            if(args[i].startsWith("-rewriting=")) {
                rewriting = Integer.parseInt(args[i].substring("-rewriting=".length()));
            }
            if(args[i].startsWith("-help") || args[i].startsWith("--help") || args[i].startsWith("-?")) {
                help();
                return;
            }
        }
        
        //GlobalSettings.getGlobalSettings().setStringbasedHashCode(true); // try non-string hash codes
        
        // create context
        ContextASPMCSRewriting ctx = new ContextASPMCSRewriting();
        ContextASPRewriting rewctx = null;
        // parsing with ANTLR
        long parsing_time = 0;
        try {
            // setting up lexer and parser
            OmigaLexer lex = new OmigaLexer(new ANTLRFileStream(filename));
            UnbufferedTokenStream tokens = new UnbufferedTokenStream(lex);
            //CommonTokenStream tokens = new CommonTokenStream(lex);
            OmigaParser parser = new OmigaParser(tokens);
        
            // set context
            parser.setContext(ctx);
            
            try {
                try {
                    // parse input
                    long start_parsing_time = System.currentTimeMillis();
                    parser.woc_program();
                    parsing_time = System.currentTimeMillis() - start_parsing_time;
                    //System.out.println("Parsed program, starting rewriting...");
                    
                    Rewriter_easy rewriter = new Rewriter_easy();
                    rewctx = rewriter.rewrite(ctx);
                    
                    //System.out.println("Read in program is: ");
                    //ctx.printContext();
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


        
        //System.out.println("STARTING: " + filename + " answersets=" + answersets + " rewriting="+rewriting + " filter=" + filter + " StartingTime=" + System.currentTimeMillis());
        
        
        Manager m = new Manager(rewctx);
        long beforeCalc = System.currentTimeMillis();
        m.calculate(answersets,outprint,filter);

        System.out.println("Calculation finished.");
        System.out.println("Time needed for parsing: "+(parsing_time/1000.0f));
        System.out.println("Time needed overall: " + (1.0F*(System.currentTimeMillis()-start)/1000));
        System.out.println("Time needed for calculation: " +(1.0F*(System.currentTimeMillis()-beforeCalc)/1000));
    }
    
    private static void help(){
        System.out.println("Usage is:");
        System.out.println("java -jar omiga.jar <filename> [-answersets=NumAnswerSetsDesired, -filter=predicateNames -rewriting=0-2]"
                + "\n\t rewriting: 0=No rewriting, 1=Normal rewriting, 2=Input is already rewritten"
                + "\n\t filter: a comma-separated list of predicate names to print (no whitespace)\n");
    }
    
}
