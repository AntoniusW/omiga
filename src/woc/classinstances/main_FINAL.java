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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import parser.Parser;
import parser.ParserRewrite;

/**
 *
 * @author User
 */
public class main_FINAL {

    
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
        
        filename = "Z.txt";
        rewriting = 1;
        answersets = 50000;
        filter = null;
        outprint =true;
        
        
        File input = new File(filename);
        
        
        System.out.println("STARTING: " + filename + "Answersets2Derive: " + answersets + "rewriting="+rewriting + "-filter= " + filter + " StartingTime: " + System.currentTimeMillis());
        
        ContextASP c = null;
        Parser pars;
        try{
        if(rewriting == 0){
            pars = new Parser();
            c = pars.readContext(input);
        }else{
            if(rewriting == 1){
                pars = new ParserRewrite();
                Rewriter_easy rewriter = new Rewriter_easy();
                c = pars.readContext(input);
                c = rewriter.rewrite(c);
                System.out.println("REWRITING=1 (We rewrite the context):");
                c.printContext();
            }else{
                if(rewriting == 2){
                    pars = new ParserRewrite();
                    c = pars.readContext(input);
                    System.out.println("REWRITING=2 (The context is already rewritten):");
                    c.printContext();
                }
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
        System.out.println("java -jar woc <filename> [-answersets=X, -filter=predicateName -rewriting=0-2 | 0=No rewriting, 1=Rewriting, 2=AlreadyRewritten]");
    }
    
}
