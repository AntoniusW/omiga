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
    GREATER,
    LESS,
    GREATER_EQ,
    LESS_EQ,
    TIMES,
    DIVIDE;
    
    public String toString(){
        switch(this){
            case PLUS: return "+";
            case MINUS: return "-";
            case EQUAL: return "=";
            case NOTEQUAL: return "!=";
            case GREATER: return ">";
            case LESS: return "<";
            case GREATER_EQ: return ">=";
            case LESS_EQ: return "<=";
            case ASSIGN: return "is";
            case TIMES: return "*";
            case DIVIDE: return "/";
            default: return null;
        }
    }
    
}
