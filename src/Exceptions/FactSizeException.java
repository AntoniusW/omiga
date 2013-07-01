package Exceptions;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * This exception should be thrown when an instance is assigned to a predicate of different arity
 */
public class FactSizeException extends Exception{
    
    public FactSizeException(String s){
        super(s);
    }
    
}
