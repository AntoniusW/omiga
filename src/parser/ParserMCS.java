/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import Entity.Constant;
import Entity.ContextASP;
import Entity.Atom;
import Entity.ContextASPMCS;
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
public class ParserMCS {
    
    // TODO: 
    // Operators
    // Predicates of arity 0 are read wrong ) is added to the name at the end
    
    public ContextASPMCS readContext(File f)throws FactSizeException,RuleNotSafeException{
        System.out.println("Starting to read input file: " + System.currentTimeMillis());
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
    
    public ContextASPMCS readContext(String s) throws FactSizeException,RuleNotSafeException{
        System.out.println("STARTING to read Context: " + System.currentTimeMillis());
        ContextASPMCS con = new ContextASPMCS();
        String[] arr = s.split("\\.");
        for(String str: arr){
            if(str.contains(":-")){
                con.addRule(this.readRule(str));
            }else{
                this.readFact(str,con);
            }
        }
        System.out.println("=============================");
        System.out.println("FINISHED READING CONTEXT!: " + System.currentTimeMillis());
        System.out.println("=============================");
        return con;
    }
    
    public void readFact(String s, ContextASP con) throws FactSizeException{
        String realString = "";
        s.replace("not ", "!N$T");
        for(int i = 0; i < s.length();i++){
            if(!(s.charAt(i) == ' ' || s.charAt(i) == '\t')) {
                realString = realString + s.charAt(i);
            }
        }
        // We removed all blanks from this line
        String predName = "";
        int i = 0;
        for(; i < realString.length();i++){
            if(realString.charAt(i) != '('){
                predName = predName + realString.charAt(i);
            }else{
                break;
            }
        }
        i++; // we read (
        
        Character c;
        ArrayList<Term> terms = new ArrayList<Term>();
        for(;i < realString.length();i++){
            c = realString.charAt(i);
            //System.out.println("c = " + c);
            if (Character.isUpperCase(c)){
                System.err.println("Error: Fact with Variable!");
                System.exit(0);
            }else{
                if(Character.isLowerCase(c) || Character.isDigit(c)){
                    i = i + readConstant(realString.substring(i), terms);
                }else{
                    if(c == '_'){
                        i = i + this.readFuncTerm(realString.substring(i), terms);
                    }else{
                        if (c == ')'){
                            //System.out.println("Read Fact: " + predName);
                            Term[] termArray = new Term[terms.size()];
                            terms.toArray(termArray);
                            con.addFact2IN(Predicate.getPredicate(predName, terms.size()), Instance.getInstance(termArray) );
                            break;
                        }else{
                            if(c != ',') System.err.println("ERROR404!");
                        }
                    }
                }
            }
        }
        
        
        
        
        
        
        
        
        
        
        /*String terms[] = realString.substring(i,realString.length()).split(",");
        ArrayList<Term> aL = new ArrayList<Term>();
        for(String str: terms){
            if(str.contains("(")){
                this.readFuncTerm(str + ",", aL); 
            }else{
                this.readConstant(str + ",", aL); // + "," is needed such that read Constant knows when to finish
            }
        }
        Term terms2Add[] = new Term[aL.size()];
        aL.toArray(terms2Add);
        con.addFact(Predicate.getPredicate(predName, aL.size()),Instance.getInstance(terms2Add));*/
    }
    
    public Rule readRule(String s){
        Rule r = new Rule();
        String realString = "";
        for(int i = 0; i < s.length();i++){
            if(!(s.charAt(i) == ' ' || s.charAt(i) == '\t')) {
                realString = realString + s.charAt(i);
            }
        }
        // We removed all blanks from this line
        int i = 0;
        if(s.charAt(i) != ':'){
           i = i + readHead(realString.substring(i),r); 
           i = i + 3; // we read :-
        }
        for(;i < realString.length();i++){
            if (Character.isLowerCase(realString.charAt(i))){
                i = i + readPredicate(realString.substring(i),r);
            }else{
                if(Character.isUpperCase(realString.charAt(i))){
                    //readOperator
                    i = i + this.readOperator(realString.substring(i),r);
                    //System.out.println("LOl OPERATOR: " + realString.charAt(i));
                }else{
                    if (realString.charAt(i) != ','){
                        System.err.println("ERROR007");
                    }
                }
                
            }
        }
        return r;
    }
    
    public int readConstant(String s, ArrayList<Term> aL){
        //System.out.println("READ CONSTANT STARTED!: " + s);
        String constantName = "";
        int i = 0;
        char c = s.charAt(i);
        while(c != ',' && c != '(' && c != ')'){
            constantName = constantName + c;
            i++;
            c = s.charAt(i);
        }
        //System.out.println("read constant: " + constantName);
        //System.out.println("ReadConstant terminated with symbol: " + s.charAt(i));
        aL.add(Constant.getConstant(constantName));
        return i-1;
    }
    
    public int readVariable(String s, ArrayList<Term> aL){
        System.out.println("READ VAR STARTED");
        String variableName = "";
        int i = 0;
        char c = s.charAt(i);
        while(c != ',' && c != '(' && c != ')'){
            //System.out.println("varC = " + c);
            variableName = variableName + c;
            i++;
            c = s.charAt(i);
        }
        System.out.println("read variable: " + variableName);
        System.out.println("ReadVariable terminated with symbol: " + s.charAt(i) + " - and returns i = " + (i-1));
        aL.add(Variable.getVariable(variableName));
        return i-1;
    }
    
    public int readFuncTerm(String s, ArrayList<Term> aL){
        System.out.println("READ FUNCTERM STARTED: " + s);
        int i = 0;
        char c = s.charAt(i);
        String funcTermName = "";
        while(c != '('){
            funcTermName = funcTermName + c;
            System.err.println("i++");
            i++;
            c = s.charAt(i);
        }
        System.err.println(c);
        i++;
        c = s.charAt(i);
        System.err.println(c);
        ArrayList<Term> children = new ArrayList<Term>();
        while( c != ')'){
            System.err.println(c);
            if (Character.isLowerCase(c) || Character.isDigit(c)){
                i = i + this.readConstant(s.substring(i), children);
            }else{
                if (Character.isUpperCase(c)){
                    System.out.println("i= " + i);
                    System.out.println("Calling Variable with: " + s.substring(i));
                    i = i + this.readVariable(s.substring(i), children);
                }else{
                    System.out.println("C= " + c);
                    if (c == '_'){
                        System.out.println("s = " + s);
                        System.out.println("i=" + i);
                        System.out.println("CALLING FUNCTERM !: " + s.substring(i));
                        i = i + this.readFuncTerm(s.substring(i), children);
                    }else{
                        if(c == ','){
                            //DO NOthing
                        }else{
                                System.err.println("ERROR!");
                                System.exit(i);
                        }
                    }
                }
            }
            i++;
            c = s.charAt(i);
        }
        System.err.println("FUNCTERM TERMINATED!: " + funcTermName + " - " + children);
        FuncTerm ft = FuncTerm.getFuncTerm(funcTermName, children);
        aL.add(ft);
        return i;
    }
    
    public int readOperator(String s, Rule r){
        System.out.println("READING OPERATOR!: " + s);
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<OP> ops = new ArrayList<OP>();
        int i = 0;
        String temp;
        while(i < s.length() && s.charAt(i) !=',' && s.charAt(i) !='.'){
            temp = "";
            while(i < s.length() && !isOperator(s.charAt(i)) && s.charAt(i) != '.'){
                System.out.println(s.charAt(i));
                temp = temp + s.charAt(i);
                i++;
            }
            strings.add(temp);
            if(i >= s.length()) break;
            ops.add(OP.valueOf(s.charAt(i)));
            i++;
        }
        //We now have the hole Operator on the string and ops ArrayList.
        OperandI tempOpi = null;
        if(Character.isLowerCase(s.charAt(0))){
            tempOpi = Constant.getConstant(strings.get(0));
        }else{
            tempOpi = Variable.getVariable(strings.get(0));
        }
        OperandI tempOpiLeft = null;
        OP fin = null;
        for(int j = 0; j < ops.size();j++){
            if(ops.get(j).equals(OP.PLUS) || ops.get(j).equals(OP.MINUS)){
                if(Character.isLowerCase(s.charAt(0))){
                    tempOpi = new Operator(tempOpi, Constant.getConstant(strings.get(j+1)), ops.get(j));
                }else{
                    System.out.println(strings.get(j+1));
                    tempOpi = new Operator(tempOpi, Variable.getVariable(strings.get(j+1)), ops.get(j));
                }
            }else{
                tempOpiLeft = tempOpi;
                fin = ops.get(j);
                if(Character.isLowerCase(s.charAt(0))){
                    tempOpi = Constant.getConstant(strings.get(j+1));
                }else{
                    tempOpi = Variable.getVariable(strings.get(j+1));
                }
                j++;
            }
        }
        Operator ret;
        if(tempOpiLeft != null){
            ret = new Operator(tempOpiLeft,tempOpi,fin);
        }else{
            ret = (Operator)tempOpi;
        }
        System.out.println("Read operator: " + ret);
        r.addOperator(ret);
        return i;
    }
    
    /*
     * Used for readOperators
     */
    public Constant getConstant(String s){
        //System.out.println("READ CONSTANT STARTED!: " + s);
        String constantName = "";
        int i = 0;
        char c = s.charAt(i);
        while(c != ',' && c != '(' && c != ')'){
            constantName = constantName + c;
            i++;
            c = s.charAt(i);
        }
        //System.out.println("read constant: " + constantName);
        //System.out.println("ReadConstant terminated with symbol: " + s.charAt(i));
        return Constant.getConstant(constantName);
    }
    
        
    /*
     * Used for readOperators
     */
    public Variable getVariable(String s){
        System.out.println("READ VAR STARTED: " + s);
        String variableName = "";
        int i = 0;
        char c = s.charAt(i);
        while(c != ',' && c != '(' && c != ')'){
            //System.out.println("varC = " + c);
            variableName = variableName + c;
            i++;
            c = s.charAt(i);
        }
        System.out.println("read variable: " + variableName);
        System.out.println("ReadVariable terminated with symbol: " + s.charAt(i) + " - and returns i = " + (i-1));
        return Variable.getVariable(variableName);
    }
    
    private boolean isOperator(char c){
        if(c == '+' || c == '-' || c == '=' || c == '>' || c == '<' || c == '~'){
            return true;
        }
        return false;
    }
    
    public int readPredicate(String s, Rule r){
        boolean neg =false;
        Atom pir = null;
        //System.out.println("READ Pred STATEd");
        String predName = "";
        int i = 0;
        if(s.substring(0,3).equalsIgnoreCase("not")){
            i = 3;
            neg = true;
        }
        char c = s.charAt(i);
        while(c != '('){
            predName = predName + c;
            i++;
            c = s.charAt(i);
        }
        i++; // read the '('
        ArrayList<Term> terms = new ArrayList<Term>();
        for(;i < s.length();i++){
            c = s.charAt(i);
            //System.out.println("c = " + c);
            if (Character.isUpperCase(c)){
                i = i + readVariable(s.substring(i), terms);
            }else{
                if(Character.isLowerCase(c) || Character.isDigit(c)){
                    i = i + readConstant(s.substring(i), terms);
                }else{
                    if(c == '_'){
                        i = i + this.readFuncTerm(s.substring(i), terms);
                    }else{
                        if (c == ')'){
                            System.out.println("Read Predicate: " + predName);
                            Term[] termArray = new Term[terms.size()];
                            terms.toArray(termArray);
                            pir = Atom.getAtom(predName, terms.size(), terms.toArray(termArray));
                            if(neg){
                                r.addAtomMinus(pir);
                            }else{
                                r.addAtomPlus(pir);
                            }
                        
                            break;
                        }else{
                            if(c != ',') System.err.println("ERROR404!");
                        }
                    }
                }
            }
        }
        System.out.println("ReadPredicate terminated with symbol: " + s.charAt(i));
        return i;
    }
    
    
    
    public int readHead(String s, Rule r){
        Atom pir = null;
        //System.out.println("READ Pred STATEd");
        String predName = "";
        int i = 0;
        char c = s.charAt(i);
        while(c != '('){
            predName = predName + c;
            i++;
            c = s.charAt(i);
        }
        i++; // read the '('
        ArrayList<Term> terms = new ArrayList<Term>();
        for(;i < s.length();i++){
            c = s.charAt(i);
            //System.out.println("c = " + c);
            if (Character.isUpperCase(c)){
                i = i + readVariable(s.substring(i), terms);
            }else{
                if(Character.isLowerCase(c) || Character.isDigit(c)){
                    i = i + readConstant(s.substring(i), terms);
                }else{
                    if(c == '_'){
                        i = i + this.readFuncTerm(s.substring(i), terms);
                    }else{
                        if (c == ')'){
                            System.out.println("Read Predicate: " + predName);
                            Term[] termArray = new Term[terms.size()];
                            terms.toArray(termArray);
                            pir = Atom.getAtom(predName, terms.size(), terms.toArray(termArray));
                            r.setHead(pir);
                            break;
                        }else{
                            if(c != ',') System.err.println("ERROR404!");
                        }
                    }
                }
            }
        }
        System.out.println("ReadPredicate terminated with symbol: " + s.charAt(i));
        return i;
    }
    
    
    
}
