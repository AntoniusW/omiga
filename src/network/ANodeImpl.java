/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import Entity.Constant;
import Entity.ContextASP;
import Entity.ContextASPRewriting;
import Entity.FunctionSymbol;
import Entity.Predicate;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import Interfaces.Term;
import Manager.Manager;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
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
    
    private Map<String,Remote> other_nodes;
    
    // mappings of other_node_name to predicate/function symbols/constants
    public static Map<String, Map<Integer, Object>> ser_mapping =
            new HashMap<String, Map<Integer, Object>>();    // mapping for deserialization
    public static Map<Object, Integer> out_mapping =
            new HashMap<Object, Integer>();  // mapping for serialization
    // as an integer from a given node uniquely identifies the instance of
    // the specific class, we only need one map
    // TODO AW something more specific than Object would be nice
    //private Map<String, Map<Integer, Predicate>> predicates;
    //private Map<String, Map<Integer, FunctionSymbol>> functions;
    //private Map<String, Map<Integer, Constant>> constants;
    
    
    private static String node_name;
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
        node_name = args[0]; //"ctx1";
        filename = args[1]; //"../../examples/birds_ASPERIX_nbb=100.txt";
        System.out.println("Starting NodeImpl.main(). args[0] = " + node_name);
        
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
            String name = "Context_" + node_name;
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
        
        System.out.println("Node "+ node_name + " received " + other_nodes.size() +" other nodes.");
        
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
        int counter=0;  // this will contain the id that a certain instance
                        // is mapped to, we make it unique over all types.
        
        // collect predicate symbols
        Map<Pair<String,Integer>,Integer> predicates = new HashMap<Pair<String,Integer>,Integer>();
        for (Iterator<Predicate> it = Predicate.getPredicatesIterator(); it.hasNext();) {
            Predicate pred = it.next();
            String name = pred.getName();
            Integer arity=pred.getArity();
            Integer ser_id = counter++;
            
            predicates.put(new Pair<String, Integer>(name,arity), ser_id);
            out_mapping.put(pred,ser_id);
        }
        
        // collect constants
        Map<String, Integer> constants = new HashMap<String,Integer>();
        for (Iterator<Constant> it = Constant.getConstantsIterator(); it.hasNext();) {
             Constant con = it.next();
             String name = con.getName();
             Integer ser_id = counter++;
             
             constants.put(name, ser_id);
             out_mapping.put(con, ser_id);
        }
        
        // collect function symbols
        Map<String,Integer> functions = new HashMap<String, Integer>();
        for (Iterator<FunctionSymbol> it = FunctionSymbol.getFunctionSymbolsIterator(); it.hasNext();) {
            FunctionSymbol func = it.next();
            String name = func.getName();
            Integer ser_id = counter++;
            
            functions.put(name, ser_id);
            out_mapping.put(func, ser_id);
        }

        for (Entry<String, Remote> node : other_nodes.entrySet()) {
            ((ANodeInterface) node.getValue()).tell_active_domain(node_name,predicates, functions, constants);
        }
        
    }

    @Override
    public void tell_active_domain(String node_name, Map<Pair<String, Integer>, Integer> predicates,
                    Map<String, Integer> functions,
                    Map<String, Integer> constants) throws RemoteException {
        
        System.out.println("Got informed by node " + node_name);
        
        Map<Integer, Object> mapping = new HashMap<Integer, Object>();
        
        // fill mapping: predicates
        for (Entry<Pair<String,Integer>,Integer> pred_desc: predicates.entrySet()) {
            // localize predicate name
            String local_pred_name = node_name+":"+pred_desc.getKey().getArg1();
            Predicate pred = Predicate.getPredicate(local_pred_name, pred_desc.getKey().getArg2());
            mapping.put( pred_desc.getValue(), pred);
        }
        
        // fill mapping: function symbols
        for(Entry<String, Integer> func_desc : functions.entrySet()) {
            FunctionSymbol fun = FunctionSymbol.getFunctionSymbol(func_desc.getKey());
            mapping.put(func_desc.getValue(), fun);
        }
        
        // fill mapping: constants
        for(Entry<String, Integer> con_desc : constants.entrySet()) {
            Constant con = Constant.getConstant(con_desc.getKey());
            mapping.put(con_desc.getValue(), con);
        }
        
        ser_mapping.put(node_name, mapping);
        
        System.out.println("Mapping created.");

    }

    
}
