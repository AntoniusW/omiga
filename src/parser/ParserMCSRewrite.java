/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import Entity.Constant;
import Entity.ContextASP;
import Entity.Atom;
import Entity.ContextASPMCS;
import Entity.ContextASPMCSRewriting;
import Entity.ContextASPRewriting;
import Entity.FuncTerm;
import Entity.Instance;
import Entity.Operator;
import Entity.Predicate;
import Entity.Rule;
import Entity.Variable;
import Enumeration.OP;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.OperandI;
import Interfaces.Term;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * The Parser class is used to read in a context from a file.
 * This class is not documented since my hard core java parser gets only discriminated anyway!
 * 
 */
public class ParserMCSRewrite extends Parser{
    
    // TODO: 
    // Operators
    // Predicates of arity 0 are read wrong ) is added to the name at the end
    
    public ContextASPMCSRewriting readContext(File f)throws FactSizeException,RuleNotSafeException{
        //System.out.println("Starting to read input file: " + System.currentTimeMillis());
        StringBuffer s = new StringBuffer("");
        try{
            FileInputStream fstream = new FileInputStream(f);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
            String strLine;
            while ((strLine = br.readLine()) != null)   {
                //System.out.println("Reading Line: " + strLine);
                // Print the content on the console
                s = s.append(strLine);
            }
            //System.out.println("s: " + s);
            return this.readContext(s.toString());
        
        
        
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    
    public ContextASPMCSRewriting readContext(String s) throws FactSizeException,RuleNotSafeException{
        //System.out.println("STARTING to read Context: " + System.currentTimeMillis());
        ContextASPMCSRewriting con = new ContextASPMCSRewriting();
        String[] arr = s.split("\\.");
        for(String str: arr){
            if(str.contains(":-")){
                con.addRule(this.readRule(str));
            }else{
                this.readFact(str,con);
            }
        }
        /*System.out.println("=============================");
        System.out.println("FINISHED READING CONTEXT!: " + System.currentTimeMillis());
        System.out.println("=============================");*/
        return con;
    }
    
    
    
}
