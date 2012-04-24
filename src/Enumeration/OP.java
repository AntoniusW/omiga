/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Enumeration;

/**
 *
 * @author g.weidinger
 */
public enum OP {
    PLUS,
    MINUS,
    EQUAL,
    ASSIGN,
    NOTEQUAL,
    BIGGER,
    SMALLER,
    TIMES,
    DIVIDE;
    
    public static OP valueOf(char c){
        switch(c){
            case '+': return PLUS;
            case '-': return MINUS;
            case '=': return EQUAL;
            case '~': return NOTEQUAL;
            case '>': return BIGGER;
            case '<': return SMALLER;
            default: return null;
        }
    }
    
    public String toString(){
        switch(this){
            case PLUS: return "+";
            case MINUS: return "-";
            case EQUAL: return "=";
            case NOTEQUAL: return "~";
            case BIGGER: return ">";
            case SMALLER: return "<";
            case ASSIGN: return "is";
            case TIMES: return "*";
            case DIVIDE: return "/";
            default: return null;
        }
    }
    
}
