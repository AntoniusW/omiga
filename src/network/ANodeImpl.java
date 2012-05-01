/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import Entity.Constant;
import Entity.ContextASP;
import Entity.ContextASPRewriting;
import Entity.FunctionSymbol;
import Entity.Instance;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    // mapping for deserialization
    public static Map<String, Map<Integer, Object>> ser_mapping =
            new HashMap<String, Map<Integer, Object>>();
    
    public static String serializingFrom = null;
    
    // mapping for serialization
    public static Map<Object, Integer> out_mapping =
            new HashMap<Object, Integer>();
    // as an integer from a given node uniquely identifies the instance of
    // the specific class, we only need one map
    // TODO AW something more specific than Object would be nice

    // list of predicates required at other nodes used for out-projection of predicates
    private static Map<String, ArrayList<Predicate>> required_predicates =
            new HashMap<String, ArrayList<Predicate>>();
    
    // the local import interface, i.e., which predicates are imported from where
    private static Map<String, ArrayList<Predicate>> import_predicates =
            new HashMap<String, ArrayList<Predicate>>();
    
    private static String node_name;
    private static ContextASP ctx;
    private static String filter;
    private static int rewriting;
    private static String filename;
    private static Integer answersets;
    private static boolean outprint;

    public ANodeImpl() {
        super();
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
        ctx = new ContextASPRewriting();
        
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
        // also init required predicates (import interface)
        for (Iterator<Predicate> it = Predicate.getPredicatesIterator(); it.hasNext();) {
            Predicate pred = it.next();
            String name = pred.getName();
            Integer arity=pred.getArity();
            Integer ser_id = counter++;
            
            predicates.put(new Pair<String, Integer>(name,arity), ser_id);
            out_mapping.put(pred,ser_id);
            
            // collect import interface
            if(pred.getNodeId()!=null) {
                String from_node = pred.getNodeId();
                ArrayList<Predicate> preds_from_node;
                
                // check if other node is already listed
                if(import_predicates.containsKey(from_node)) {
                    preds_from_node = import_predicates.get(from_node);
                } else {
                    preds_from_node = new ArrayList<Predicate>();
                    import_predicates.put(from_node, preds_from_node);
                }
                
                // add import predicate
                preds_from_node.add(pred);
            }
                
                
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
            // tell active domain
            ((ANodeInterface) node.getValue()).tell_active_domain(node_name,
                    predicates, functions, constants);
            
            // tell import interface
            ((ANodeInterface)node.getValue()).tell_import_domain(node_name,import_predicates.get(node.getKey()));
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

    @Override
    public ReplyMessage testInstanceExchange() throws RemoteException {
        
        System.out.println("Testing instance exchange now.");
        
        // simply send all known facts to all other nodes
        HashMap<Predicate, ArrayList<Instance>> in_facts = ctx.getAllINFacts();
        for(Remote other : other_nodes.values()) {
            ((ANodeInterface)other).receiveNextFactsFrom(node_name);
            ((ANodeInterface)other).handleAddingFacts(in_facts);
        }
        return ReplyMessage.SUCCEEDED;
    }
    
    

    @Override
    public ReplyMessage handleAddingFacts(Map<Predicate, ArrayList<Instance>> in_facts) throws RemoteException {
        
        // we simply print out what we received
        System.out.println("Received facts from "+serializingFrom +":");
        for(Entry<Predicate, ArrayList<Instance>> pred : in_facts.entrySet()) {
            System.out.println("Predicate "+pred.getKey().getName()+"/"+
                                pred.getKey().getArity()+ ", "+
                                pred.getValue().size()+" entries.");
            for (Iterator it = pred.getValue().iterator(); it.hasNext();) {
                Instance inst = (Instance)it.next();
                System.out.println("Instance: "+inst.toString());
                
            }
        }
        
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage receiveNextFactsFrom(String from_node) throws RemoteException {
        serializingFrom = from_node;
        
        System.out.println("The next facts will come from node "+from_node);
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public void tell_import_domain(String from, List<Predicate> required_predicates) throws RemoteException {
        this.required_predicates.put(from, (ArrayList<Predicate>)required_predicates);
    }

    
}
