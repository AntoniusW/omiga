/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;


import Datastructure.Rete.HeadNode;
import Datastructure.Rete.Rete;
import Datastructure.Rewriting.Rewriter_easy;
import Datastructure.choice.ChoiceUnitRewrite;
import Entity.ContextASP;
import Entity.ContextASPRewriting;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Predicate;
import Entity.Variable;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import Manager.Manager;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.Parser;
import parser.ParserRewrite;

/**
 *
 * @author User
 */
public class main_Command {

    
    private static String filter;
    private static int rewriting;
    private static String filename;
    private static Integer answersets;
    private static boolean outprint;
    
    public static void main(String args[]) /*throws FactSizeException, RuleNotSafeException*/{
        long start = System.currentTimeMillis();
        File input = null;
        try{
            filename = args[0];
            input = new File(filename);
            if(!input.canRead()){
                System.err.println("ERROR when reading input file!");
                main_Command.help();
            }
        }catch(Exception e){
            System.err.println("ERROR when reading input file!");
            main_Command.help();
        }
        
        outprint = true;
        filter = null;
        answersets = null;
        rewriting = 1;
        for(String s: args){
            if(s.equalsIgnoreCase("help") || s.equalsIgnoreCase("-help") || s.equalsIgnoreCase("?") || s.equalsIgnoreCase("-?")){
                main_Command.help();
            }
            System.out.println(s);
            if(s.startsWith("filter=") || s.startsWith("-filter=")){
                if(s.startsWith("-")){
                    filter = s.substring("-filter".length(),s.length());
                }else{
                    filter = s.substring("filter".length(),s.length());
                }
            }
            if(s.equalsIgnoreCase("nooutput") ||s.startsWith("-nooutput")){
                outprint = false;
            }
            try{
            if(s.startsWith("n=") || s.startsWith("N=") ){
                answersets = Integer.parseInt(s.substring("n=".length(),s.length()));
            }
            if(s.startsWith("-n=") || s.startsWith("-N=")){
                answersets = Integer.parseInt(s.substring("-n=".length(),s.length()));
            }
            }catch(Exception e){
                e.printStackTrace();
                System.err.println("Error when reading Parameter: number of Answersets that should be calculated.");
                answersets= null;
                help();
            }
            if(s.startsWith("-rewriting=") || s.startsWith("rewriting=")){
                try{
                if(s.startsWith("-")){
                    rewriting = Integer.parseInt(s.substring("-rewriting=".length(),s.length()));
                }else{
                    rewriting = Integer.parseInt(s.substring("rewriting=".length(),s.length()));
                }
                }catch(Exception e){
                    e.printStackTrace();
                    System.err.println("Error when reading Parameter: rewriting");
                    help();
                }
                if(rewriting != 1 && rewriting != 2){
                    System.err.println("ERROR: Rewriting set to: " + rewriting + " but may only have values: 1 or 2.");
                    help();
                }
            }
        }
        
        
        
        
        
        System.out.println("STARTING: " + filename + "Answersets2Derive: " + answersets + "rewriting="+rewriting + "-filter= " + filter + " StartingTime: " + System.currentTimeMillis());
        
        ContextASP c = null;
        Parser pars;
        try{
            if(rewriting == 1){
                pars = new ParserRewrite();
                Rewriter_easy rewriter = new Rewriter_easy();
                c = pars.readContext(input);
                c = rewriter.rewrite(c);
                //System.out.println("REWRITING=1 (We rewrite the context):");
                //c.printContext();
            }else{
                if(rewriting == 2){
                    pars = new ParserRewrite();
                    c = pars.readContext(input);
                    //System.out.println("REWRITING=2 (The context is already rewritten):");
                    //c.printContext();
                }
            }  
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //c.printContext();
        Manager m = new Manager(c);
        long beforeCalc = System.currentTimeMillis();   
        m.calculate(answersets,outprint,filter);
            
        System.out.println("Termianted final Calculation");
        System.out.println("Time needed overAll: " + (1.0F*(System.currentTimeMillis()-start)/1000));
        System.out.println("Time needed for calculation: " +(1.0F*(System.currentTimeMillis()-beforeCalc)/1000));
    }
    
    private static void help(){
        System.out.println("java -jar woc <filename> [-n=NumberOfAnswersetsToCalculate, -nooutput. -filter=predicateName -rewriting=1 or 2 | 1=Rewriting, 2=AlreadyRewritten]");
        System.out.println("n... number of answersets that should be calculated on this run");
        System.out.println("nooutput ... runs the calculation without outprint");
        System.out.println("filter ... sets a filter such that only predicates of the specified name are printed");
        System.out.println("rewriting: ");
        System.out.println(" 1 ... The Context is internally rewritten by our easy rewriting procedure.");
        System.out.println(" 2 ... The Context is already rewritten. Must satisfy the condition that ruleheads are unique! A good rewriting can save much time.");
        System.exit(1);
    }
    
}
