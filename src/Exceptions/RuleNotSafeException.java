package Exceptions;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * This exception should be thrown when you try to add a unsafe rule to a context
 * 
 */
public class RuleNotSafeException extends Exception{
    
    public RuleNotSafeException(String s){
        super(s);
    }
    
}
