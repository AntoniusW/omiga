package Exceptions;

/**
 * This exception serves the purpose of bringing propagation to an immediate
 * halt. It must be caught by Context.propagate and not earlier.
 *
 * @author Antonius Weinzierl
 */
public class ImmediateBacktrackingException extends RuntimeException 
{
}
