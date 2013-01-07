package Exceptions;

/**
 * An exception for unexpected behaviour during learning.
 * Its intention is to be thrown if some buggy behaviour of the learning occurs,
 * hence it is a RuntimeException and usually signals a bug in the algorithm.
 * @author Antonius Weinzierl
 */
public class LearningException extends RuntimeException {

    public LearningException(String message) {
        super(message);
    }
    
}
