/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import Entity.ContextASP;
import Entity.ContextASPRewriting;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Manager.Manager;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import parser.antlr.wocLexer;
import parser.antlr.wocParser;

/**
 *
 * @author Minh Dao-Tran
 */
public class ANodeImpl implements ANodeInterface {
    
    Map<String,Remote> other_nodes;
    
    private static String filter;
    private static int rewriting;
    private static String filename;
    private static Integer answersets;
    private static boolean outprint;

    public ANodeImpl() {
        super();
    }
    
    
    
    
    @Override
    public ReplyMessage handleAddingFacts() throws RemoteException {
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage makeChoice(int global_level) throws RemoteException {
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage makeAlternative() throws RemoteException {
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage localBacktrack(int global_level) throws RemoteException {
        return ReplyMessage.SUCCEEDED;
    }
    
    public static void main(String[] args) {
        String ctx_id = args[0]; //"ctx1";
        filename = args[1]; //"../../examples/birds_ASPERIX_nbb=100.txt";
        System.out.println("Starting NodeImpl.main(). args[0] = " + ctx_id);
        
        System.out.println("Input file is: " +filename);
        
        if (System.getSecurityManager() == null) {
            // create anonymous sub-class of SecurityManager, which accepts everything from everywhere
            System.setSecurityManager(new RMISecurityManager() /*{
                @Override
                    public void checkConnect (String host, int port) {}
                @Override
                    public void checkConnect (String host, int port, Object context) {}
                }*/ );
        }
        
        try {
            // args[0] is the context id
            String name = "Context_" + ctx_id;
            System.out.println("name = " + name);
            ANodeInterface local_node = new ANodeImpl();
            ANodeInterface stub =
                (ANodeInterface) UnicastRemoteObject.exportObject(local_node,0);  // use anonymous/no port
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("NodeImpl bound.");
        } catch (Exception e) {
            System.err.println("ANodeImpl exception:");
            e.printStackTrace();
        }
    }

    @Override
    public void init(Map<String, Remote> other_nodes) throws RemoteException {
        this.other_nodes = other_nodes;
        
        // startup of local server here (read program, etc.)
        long start = System.currentTimeMillis();
        rewriting = 1;
        answersets = 50000;
        filter = null;
        outprint =true;
        
        // create context
        ContextASP ctx = new ContextASPRewriting();
        
        // parsing with ANTLR
        try {
            // setting up lexer and parser
            wocLexer lex = new wocLexer(new ANTLRFileStream(filename));
            wocParser parser = new wocParser(new CommonTokenStream(lex));
        
            // set context
            parser.setContext(ctx);
            
            // parse input
            parser.woc_program();
            
            System.out.println("Read in program is: ");
            ctx.printContext();
        } catch (RuleNotSafeException ex) {
            Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FactSizeException ex) {
            Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
        }        
        catch (RecognitionException ex) {
            Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
         catch (IOException ex) {
            Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* below code is running the node stand-alone
        System.out.println("STARTING: " + filename + "Answersets2Derive: " + answersets + "rewriting="+rewriting + "-filter= " + filter + " StartingTime: " + System.currentTimeMillis());
                
        Manager m = new Manager(ctx);
        long beforeCalc = System.currentTimeMillis();
        m.calculate(answersets,outprint,filter);

        System.out.println("Termianted final Calculation");
        System.out.println("Time needed overAll: " + (1.0F*(System.currentTimeMillis()-start)/1000));
        System.out.println("Time needed for calculation: " +(1.0F*(System.currentTimeMillis()-beforeCalc)/1000));
        */
        
        System.out.println("Initialized node, informing other nodes now.");

        // get list of predicate/functions/constants
        
        for (Entry<String, Remote> node : other_nodes.entrySet()) {
            //((ANodeInterface) node.getValue()).inform(null, null, null);
        }
        
    }

    @Override
    public void inform(Map<Entry<String, Integer>, Integer> predicates,
                    Map<String, Integer> functions,
                    Map<String, Integer> constants) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
