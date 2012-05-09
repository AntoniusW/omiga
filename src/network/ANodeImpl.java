/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import Datastructure.Rewriting.Rewriter_easyMCS;
import Entity.Constant;
import Entity.ContextASP;
import Entity.ContextASPMCSRewriting;
import Entity.ContextASPRewriting;
import Entity.FunctionSymbol;
import Entity.Instance;
import Entity.Predicate;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
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
 * @author Minh Dao-Tran, Antonius Weinzierl
 */
public class ANodeImpl implements ANodeInterface {
    
    private ArrayList<Pair<String,ANodeInterface>> other_nodes;
    // TODO AW changed other_nodes to now containing all nodes, including this
    // one. Check if this broke something
    
    // mapping for deserialization:
    // integer to predicate/function symbols/constants
    public static Map<Integer, Object> deser_mapping =
            new HashMap<Integer, Object>();
    
    // separate map to localize predicate names
    public static String serializingFrom = null;
    public static Map<String,HashMap<Integer,Predicate>> predicate_mapping =
            new HashMap<String,HashMap<Integer, Predicate>>();
    
    // mapping for serialization:
    // predicates/function symbols/constants to integers
    public static Map<Object, Integer> out_mapping =
            new HashMap<Object, Integer>();
    // as an integer uniquely identifies the instance of
    // the specific class, we only need one map
    // TODO AW something more specific than Object would be nice

    // list of predicates required at other nodes used for out-projection of predicates
    private static Map<String, ArrayList<Predicate>> required_predicates =
            new HashMap<String, ArrayList<Predicate>>();
    
    // the local import interface, i.e., which predicates are imported from where
    private static Map<String, ArrayList<Predicate>> import_predicates =
            new HashMap<String, ArrayList<Predicate>>();
    
    // mapping from global to local decision levels
    private Map<Integer, Integer> global_to_local_dc =
            new HashMap<Integer, Integer>();
    
    private static ContextASPMCSRewriting ctx;
    private static int decision_level_before_push;
    private static String node_name;
    private static String filter;
    private static int rewriting;
    private static String filename;
    private static Integer answersets;
    private static boolean outprint;

    public ANodeImpl() {
        super();
        
        global_to_local_dc.put(0, 0);
    }

    @Override
    public int init(String node_name, ArrayList<Pair<String, ANodeInterface>> other_nodes, int serialize_start) throws RemoteException {
        this.other_nodes = other_nodes;
        ANodeImpl.node_name = node_name;
        
        System.out.println("Node[" + node_name +"]: received " + other_nodes.size() +" other nodes.");
        
        // startup of local server here (read program, etc.)
        long start = System.currentTimeMillis();
        rewriting = 1;
        answersets = 50000;
        filter = null;
        outprint =true;
        
        // create context
        ctx = new ContextASPMCSRewriting();
        
        // parsing with ANTLR
        try {
            // setting up lexer and parser
            wocLexer lex = new wocLexer(new ANTLRFileStream(filename));
            wocParser parser = new wocParser(new CommonTokenStream(lex));
        
            // set context
            parser.setContext(ctx);
            
            // parse input
            parser.woc_program();
            
            System.out.println("Node[" + node_name +"]: Read in program is: ");
            ctx.printContext();
            
            // rewrite context
            Rewriter_easyMCS rewriter = new Rewriter_easyMCS();
            ctx = rewriter.rewrite(ctx);
            
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
        
        ctx.propagate();
        //System.err.println("First Propagation finsihed: " + System.currentTimeMillis());
        ctx.getChoiceUnit().DeriveSCC();
        ctx.getChoiceUnit().killSoloSCC();
      
        System.out.println("Node[" + node_name +"]: Initialized node, informing other nodes now.");

        // get list of predicate/functions/constants        
        int counter=serialize_start;  // this will contain the id that a certain instance
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
            
            // if this predicate is external one, tell it to others
            if(pred.getNodeId()!=null) {
            
                // collect import interface
                String from_node = pred.getNodeId();
                ArrayList<Predicate> preds_from_node;
                
                // check if other node is already listed
                if(import_predicates.containsKey(from_node)) {
                    //System.out.println("Node "+from_node+" already recorded at import interface.");
                    preds_from_node = import_predicates.get(from_node);
                } else {
                    //System.out.println("Node "+from_node+" not recorded at import interface, creating it.");
                    preds_from_node = new ArrayList<Predicate>();
                    import_predicates.put(from_node, preds_from_node);
                }
                
                System.out.println("Node[" + node_name +"]: Predicate is from outside: "+pred.toString());
                
                // add import predicate
                preds_from_node.add(pred);
               // System.out.println("Currently listed predicates are: "+preds_from_node);
                
                // register predicate at local solver to come from outside
                ANodeImpl.ctx.registerFactFromOutside(pred);                
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
             deser_mapping.put(ser_id, con);
        }
        
        // collect function symbols
        Map<String,Integer> functions = new HashMap<String, Integer>();
        for (Iterator<FunctionSymbol> it = FunctionSymbol.getFunctionSymbolsIterator(); it.hasNext();) {
            FunctionSymbol func = it.next();
            String name = func.getName();
            Integer ser_id = counter++;
            
            functions.put(name, ser_id);
            out_mapping.put(func, ser_id);
            deser_mapping.put(ser_id, func);
        }

        for (Pair<String, ANodeInterface> other : other_nodes) {
            // tell active domain to other nodes except self
            if(!other.getArg1().equals(node_name)) {
                other.getArg2().tell_active_domain(node_name, predicates,
                        functions, constants);
            }
            
            // tell import interface to all nodes
            other.getArg2().receiveNextFactsFrom(node_name);     // needed to de-serialize predicates at other node
            other.getArg2().tell_import_domain(node_name,import_predicates.get(other.getArg1()));
        }
        
        return counter;
        
    }

    @Override
    public ReplyMessage tell_active_domain(String node_name, Map<Pair<String, Integer>, Integer> predicates,
                    Map<String, Integer> functions,
                    Map<String, Integer> constants) throws RemoteException {
       
        System.out.println("Node[" + node_name +"]: Got informed by node " + node_name);
        
        System.out.println("Adding predicates: "+predicates);
        
        
        // fill mapping: predicates
        HashMap<Integer,Predicate> pred_map = new HashMap<Integer, Predicate>();
        for (Entry<Pair<String,Integer>,Integer> pred_desc: predicates.entrySet()) {
            
            // TODO AW hack to normalize predicate names
            // n2:q occuring in context n1 locally is q
            String local_pred_name;
            if(pred_desc.getKey().getArg1().contains(":")) {
                //local_pred_name = "n"+pred_desc.getKey().getArg1();    // this is the global name
                if(!pred_desc.getKey().getArg1().startsWith(ANodeImpl.node_name+":"))
                    throw new RemoteException("Node identifier of received external predicate ["+pred_desc.getKey().getArg1()+"] differs from local name ["+ANodeImpl.node_name+"]");
                local_pred_name = pred_desc.getKey().getArg1().replaceFirst(".*:", "");
                System.out.println("Node[" + node_name +"]: Localized predicate "+ pred_desc.getKey().getArg1() + " to "+local_pred_name);
            } else {
                // localize predicate name
                local_pred_name = node_name+":"+pred_desc.getKey().getArg1();
            }
            Predicate pred = Predicate.getPredicate(local_pred_name, pred_desc.getKey().getArg2());
            pred_map.put( pred_desc.getValue(), pred);
            System.out.println("Added to mapping: "+pred+" with value "+pred_desc.getValue()+" from "+node_name);
            //out_mapping.put(pred, pred_desc.getValue());
        }
        predicate_mapping.put(node_name, pred_map);
        
        System.out.println("Adding function symbols: "+functions);
        
        // fill mapping: function symbols
        for(Entry<String, Integer> func_desc : functions.entrySet()) {
            FunctionSymbol fun = FunctionSymbol.getFunctionSymbol(func_desc.getKey());
            deser_mapping.put(func_desc.getValue(), fun);
            out_mapping.put(fun, func_desc.getValue());
        }
        
        System.out.println("Adding constants: "+constants);
        
        // fill mapping: constants
        for(Entry<String, Integer> con_desc : constants.entrySet()) {
            Constant con = Constant.getConstant(con_desc.getKey());
            deser_mapping.put(con_desc.getValue(), con); 
            out_mapping.put(con, con_desc.getValue());
            
        }
        
        System.out.println("Node[" + node_name +"]: Mapping created.");
        
        return ReplyMessage.SUCCEEDED;
    }

    /*@Override
    public ReplyMessage testInstanceExchange() throws RemoteException {
        
        System.out.println("Testing instance exchange now.");
        
        // simply send all known facts to all other nodes
        HashMap<Predicate, ArrayList<Instance>> in_facts = ctx.getAllINFacts();
        for(Remote other : other_nodes.values()) {
            ((ANodeInterface)other).receiveNextFactsFrom(node_name);
            ((ANodeInterface)other).handleAddingFacts(in_facts);
        }
        return ReplyMessage.SUCCEEDED;
    }*/
    
    

    @Override
    public ReplyMessage handleAddingFacts(int global_level, Map<Predicate, ArrayList<Instance>> in_facts) throws RemoteException {
               
        System.out.println("Node[" + node_name +"]: Received facts from "+serializingFrom +":");
        System.out.println("Node[" + node_name +"]: HAF: ctx.decisionLevel (decision_level_before_push) = "+ctx.getDecisionLevel());
        decision_level_before_push = ctx.getDecisionLevel();
        for(Entry<Predicate, ArrayList<Instance>> pred : in_facts.entrySet()) {
            
            // print out what was received
            System.out.println("Node[" + node_name +"]: Predicate "+pred.getKey().getName()+"/"+
                                pred.getKey().getArity()+ ", "+
                                pred.getValue().size()+" entries.");
            for (Iterator it = pred.getValue().iterator(); it.hasNext();) {
                Instance inst = (Instance)it.next();
                System.out.println("Node[" + node_name +"]: Instance: "+inst.toString());
                
                // actual adding of the facts
                System.out.println("Node[" + node_name +"]: Adding Predicate / Instance = "+pred.getKey()+"/"+inst);
                ANodeImpl.ctx.addFactFromOutside(pred.getKey(), inst);
            }
        }
        
        System.out.println("Node[" + node_name +"]: Received facts end.");
        System.out.println("Node[" + node_name +"]: HAF: ctx.decisionLevel = "+ctx.getDecisionLevel());
        
        // TODO AW find global decision level
        return this.propagate(global_level);
        //return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage receiveNextFactsFrom(String from_node) throws RemoteException {
        serializingFrom = from_node;
        
        System.out.println("Node[" + node_name +"]: The next facts will come from node " + from_node);
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage tell_import_domain(String from, List<Predicate> required_predicates) throws RemoteException {
        System.out.println("Node[" + node_name + "]: got import domain from Node[" + from+"]");
        System.out.println("Node[" + node_name + "]: required predicates are: "+required_predicates);

        ANodeImpl.required_predicates.put(from, (ArrayList<Predicate>)required_predicates);
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public boolean hasMoreChoice() throws RemoteException {
        System.out.println("Node[" + node_name + "]: before checking choice: local_dc = " + ctx.getDecisionLevel());
        
        boolean moreChoice = ctx.choice();

        System.out.println("Node[" + node_name + "]: after checking choice: local_dc = " + ctx.getDecisionLevel());

        
        if (moreChoice)
        {
            System.out.println("Node[" + node_name + "]: hasMoreChoice. Return TRUE.");
        }
        else
        {
            System.out.println("Node[" + node_name + "]: hasMoreChoice. Return FALSE.");
        }
        
        return moreChoice;
    }
    
    @Override
    public ReplyMessage propagate(int global_level) throws RemoteException {
        
        int local_dc = ctx.getDecisionLevel();
        
        System.out.println("Node[" + node_name + "]: before propagate. local_dc = " + local_dc);
        
        global_to_local_dc.put(global_level, local_dc);
        
        System.out.println("Node[" + node_name + "]: propagate. put(" + global_level + ", " + local_dc + ").");
        
        ctx.propagate();
        
        System.out.println("Node[" + node_name + "]: after propagate. local_dc = " + local_dc);
        
        if (ctx.isSatisfiable())
        {
            System.out.println("Node[" + node_name + "]: propagate. Pushing.");
            
            pushDerivedFacts(global_level);
            
            System.out.println("Node[" + node_name + "]: propagate. Return SUCCEEDED.");
            return ReplyMessage.SUCCEEDED;
        }
        else
        {
            System.out.println("Node[" + node_name + "]: propagate. Return INCONSISTENT. local_dc = " + local_dc);
            return ReplyMessage.INCONSISTENT;
        }        
    }
    
    @Override
    public ReplyMessage hasMoreBranch() throws RemoteException {
        System.out.println("Node[" + node_name + "]: before checking branch: local_dc = " + ctx.getDecisionLevel());

        
        ReplyMessage moreBranch = ctx.nextBranch();
        
        System.out.println("Node[" + node_name + "]: after checking branch: local_dc = " + ctx.getDecisionLevel());
        
        switch (moreBranch)
        {
            case HAS_BRANCH:
                System.out.println("Node[" + node_name + "]: hasMoreBranch. Return HAS_BRANCH.");
                break;
            case NO_MORE_BRANCH:
                System.out.println("Node[" + node_name + "]: hasMoreBranch. Return NO_MORE_BRANCH.");
                break;
            case NO_MORE_ALTERNATIVE:
                System.out.println("Node[" + node_name + "]: hasMoreBranch. Return NO_MORE_ALTERNATIVE.");
                break;
        }
        return moreBranch;
    }    

    @Override
    public ReplyMessage localBacktrack(int global_level) throws RemoteException {
        System.out.println("Node[" + node_name + "]: start in backtrack. local_dc = " + ctx.getDecisionLevel());
        
        Integer local_dc = global_to_local_dc.get(global_level);
        
        System.out.println("Node[" + node_name + "]: backtrack to global_level = " + global_level);
        
        if (local_dc != null)
        {
            System.out.println("Node[" + node_name + "]: corresponding local level = " + local_dc);
        
            ctx.backtrackTo(local_dc.intValue());
            global_to_local_dc.remove(global_level+1);
            
            int gl1 = global_level+1;
            
            System.out.println("Node[" + node_name + "]: makeBranch. remove(" + gl1 + ").");
            
            System.out.println("After backtracking. Current local decision level = " + ctx.getDecisionLevel());
        }
        else
        {
            System.out.println("Node[" + node_name + "]: no corresponding local level found.");
        }
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage printAnswer() throws RemoteException {
        ctx.printAnswerSet(null);
        
        return ReplyMessage.SUCCEEDED;
    }

    private void pushDerivedFacts(int global_level) {
        //int current_level = ctx.getDecisionLevel();
        //current_level = (current_level == 0) ? 0 : current_level-1;
        System.out.println("Node[" + node_name +"]: Decision level before push = " + decision_level_before_push);
        HashMap<Predicate, HashSet<Instance>> new_facts = ctx.deriveNewFacts(decision_level_before_push);
        
        System.out.println("Node[" + node_name +"]: PushDerivedFacts: required_predicates ="+required_predicates);
        System.out.println("Node[" + node_name +"]: PushDerivedFacts: new_facts ="+new_facts);
        
        // for all other nodes
        for (Iterator<Pair<String, ANodeInterface>> it = other_nodes.iterator(); it.hasNext();) {
            Pair<String,ANodeInterface>  node = it.next();
            
            // skip other node if it does not require facts from this one
            if(!required_predicates.containsKey(node.getArg1()) || required_predicates.get(node.getArg1())==null) {
                System.out.println("Node[" + node_name +"]: Skipping " + node.getArg1());
                continue;
            }
            
            boolean will_push = false;
            HashMap<Predicate,ArrayList<Instance>> to_push = new HashMap<Predicate, ArrayList<Instance>>();
            // for all predicates having new facts 
            for (Entry<Predicate, HashSet<Instance>> pred : new_facts.entrySet()) {
                

                
                //System.out.println("pushDerivedFacts:  pred: "+pred.toString());
                
                // add facts if this predicate is imported
                if( required_predicates.get(node.getArg1()).contains(pred.getKey())) {
                    if (pred.getValue().size() > 0)
                    {
                        will_push = true;
                        System.out.println("Node[" + node_name +"]: PushDerivedFacts: to " + node.getArg1() + ". HashSet of Instances size " + pred.getValue().size());
                        to_push.put(pred.getKey(), new ArrayList<Instance>(pred.getValue()));
                    }
                }
            }
            
            // send new facts to other node
            try {
                if (will_push)
                {
                    System.out.println("Node[" + node_name +"]: PushDerivedFacts: to_push = "+to_push);
                    node.getArg2().receiveNextFactsFrom(node_name);
                    node.getArg2().handleAddingFacts(global_level, to_push);
                }
                else
                {
                    System.out.println("Node[" + node_name +"]: Nothing to push to " + node.getArg1());
                }
            } catch (RemoteException ex) {
                Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
        
    public static void main(String[] args) {
        node_name = args[0];
        filename = args[1];
        System.out.println("Node[" + node_name +"]: Starting NodeImpl.main(). args[0] = " + node_name);
        
        System.out.println("Node[" + node_name +"]: Input file is: " + filename);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager() );
        }
        
        try {
            String name = node_name;

            ANodeInterface local_node = new ANodeImpl();
            ANodeInterface stub =
                (ANodeInterface) UnicastRemoteObject.exportObject(local_node,0);  // use anonymous/no port
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Node[" + node_name +"]: NodeImpl bound.");
        } catch (Exception e) {
            System.err.println("Node[" + node_name +"]: ANodeImpl exception:");
            e.printStackTrace();
        }
    }
}
