/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import Entity.ContextASP;

/**
 *
 * @author g.weidinger
 */
public class testParser {
    
    
    public static void main(String arg[]){
        Parser par = new Parser();
        
        String s = "head(X) :- body(X). head(X,Y) :- body1(X), body(Y).hEAD_Of_IT(Xanten) :- b(Xylophon). body(a).body(b).b(babababa)";
        ContextASP c;
        
        try{
            c = par.readContext(s);
            c.printContext();
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
        
        
        
        
    }
    
}
