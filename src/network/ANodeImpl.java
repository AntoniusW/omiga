/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import Datastructure.Rete.BasicNode;
import Datastructure.Rete.Node;
import Datastructure.Rewriting.Rewriter_easyMCS;
import Entity.*;
import Exceptions.FactSizeException;
import Exceptions.RuleNotSafeException;
import java.io.IOException;
import java.rmi.RMISecurityManager;
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
import parser.antlr.OmigaLexer;
import parser.antlr.OmigaParser;

/**
 *
 * @author Minh Dao-Tran, Antonius Weinzierl
 */
public class ANodeImpl implements ANodeInterface {
    
    private Map<String,ANodeInterface> other_nodes;
    
    // for serialization/deserialization
    public static String serializingFrom = null;
    // mappings for deserialization
    // integer to function symbols/constants
    public static Map<Integer, Object> deser_mapping =
            new HashMap<Integer, Object>();
    
    // separate map to localize predicate names
    public static Map<String,HashMap<Integer,Predicate>> predicate_mapping =
            new HashMap<String,HashMap<Integer, Predicate>>();
    private static HashSet< Predicate > local_predicates =
            new HashSet<Predicate>();
    
    // mapping for serialization:
    // predicates/function symbols/constants to integers
    public static Map<Object, Integer> out_mapping =
            new HashMap<Object, Integer>();
    // as an integer uniquely identifies the instance of
    // the specific class, we only need one map
    // TODO AW something more specific than Object would be nice

    // list of predicates required at other nodes used for out-projection of predicates
    private static Map<Predicate, ArrayList<ANodeInterface>> required_predicates =
            new HashMap<Predicate, ArrayList<ANodeInterface>>();
    //private static Map<String, ArrayList<Predicate>> required_predicates =
     //       new HashMap<String, ArrayList<Predicate>>();
    
    // the local import interface, i.e., which predicates are imported from where
    private static Map<String, ArrayList<Pair<String,Integer>>> import_predicates =
            new HashMap<String, ArrayList<Pair<String,Integer>>>();
    
    // mapping from global to local decision levels
    private Map<Integer, Integer> global_to_local_dc =
            new HashMap<Integer, Integer>();
    
    private HashSet<Predicate> closed_predicates;
    
    private static ContextASPMCSRewriting ctx;
    //private static int decision_level_before_push;
    public static String local_name;
    private String filter;
    private String filename;
    private long solving_time;
    

    public ANodeImpl(String local_name, String filename, String filter) {
        super();
        
        solving_time = 0;
        
        this.local_name = local_name;
        this.filename = filename;
        this.filter = filter;
        
        closed_predicates = new HashSet<Predicate>();
        other_nodes = new HashMap<String, ANodeInterface>();
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager() );
        }
        
        try {
            ANodeInterface stub =
                (ANodeInterface) UnicastRemoteObject.exportObject(this,0);  // use anonymous/no port
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(local_name, stub);
            System.out.println("Node[" + local_name +"]: NodeImpl bound.");
        } catch (Exception e) {
            System.err.println("Node[" + local_name +"]: ANodeImpl exception:");
            e.printStackTrace();
        }
        
        //global_to_local_dc.put(0, 0);
    }
    
    public String getName() {
        return local_name;
    }    
    
    @Override
    public long getSolvingTime() throws RemoteException
    {
        return solving_time;
    }

    @Override
    public ReplyMessage init(ArrayList<Pair<String, ANodeInterface>> other_nodes) throws RemoteException {
        for (Pair<String, ANodeInterface> pair : other_nodes) {
            this.other_nodes.put(pair.getArg1(), pair.getArg2());
        }
        //this.other_nodes = other_nodes;
        
        //System.out.println("Node[" + local_name +"]: received " + other_nodes.size() +" other nodes.");
       
        // create context
        ctx = new ContextASPMCSRewriting();
        
        // parsing with ANTLR
        try {
            // setting up lexer and parser
            OmigaLexer lex = new OmigaLexer(new ANTLRFileStream(filename));
            OmigaParser parser = new OmigaParser(new CommonTokenStream(lex));
        
            // set context
            parser.setContext(ctx);
            
            // parse input
            parser.woc_program();
            
            //System.out.println("Node[" + local_name +"]: Read in program is: ");
            //ctx.printContext();
            
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
        
        long start_computing = System.currentTimeMillis();
        ctx.propagate();
        //System.out.println("First Propagation finished: " + System.currentTimeMillis());
        ctx.getChoiceUnit().DeriveSCC();
        
        //System.out.println("Call killSoloSCC");
        ctx.getChoiceUnit().killSoloSCC();
        solving_time = solving_time + System.currentTimeMillis() - start_computing;
        
        //System.out.println("After killSoloSCC");
        //ctx.printAnswerSet(filter);
        
        // collecting local predicates
        //System.out.println("Node[" + local_name +"]: Collecting local predicates.");
        for (Iterator<Predicate> it = Predicate.getPredicatesIterator(); it.hasNext();) {
            Predicate pred = it.next();
            local_predicates.add(pred);
            
             // record if this predicate is external
            if(pred.getNodeId()!=null) {
            
                // collect import interface
                String from_node = pred.getNodeId();
                ArrayList<Pair<String,Integer>> preds_from_node;
                
                // check if other node is already listed
                if(import_predicates.containsKey(from_node)) {
                    preds_from_node = import_predicates.get(from_node);
                } else {
                    preds_from_node = new ArrayList<Pair<String, Integer>>();
                    import_predicates.put(from_node, preds_from_node);
                }
                
                //System.out.println("Node[" + local_name +"]: Predicate is from outside: "+pred.toString());
                
                // add import predicate
                preds_from_node.add(new Pair<String, Integer>(pred.getName(),pred.getArity()));
                //System.out.println("Node[" + node_name +"]: Currently listed predicates of node ["+from_node+"]: "+preds_from_node);
                
                // register predicate at local solver to come from outside
                // is done in parser already
                //ANodeImpl.ctx.registerFactFromOutside(pred);
            }
        }
        //System.out.println("Node[" + local_name +"]: import predicates: "+import_predicates);
      
        //System.out.println("Node[" + local_name +"]: Initialized node.");

        return ReplyMessage.SUCCEEDED;
        
    }

    @Override
    public int exchange_active_domain(int serialize_start) throws RemoteException {
        
        // get list of predicate/functions/constants        
        int counter=serialize_start;  // this will contain the id that a certain instance
                        // is mapped to, we make it unique over all types.
        
        // collect predicate symbols
        Map<Pair<String,Integer>,Integer> predicates = new HashMap<Pair<String,Integer>,Integer>();
        predicate_mapping.put(local_name, new HashMap<Integer, Predicate>());   // mapping to de-serialize local predicates (for receiving import domain)
        for (Iterator<Predicate> it = local_predicates.iterator(); it.hasNext();) {
            Predicate pred = it.next();
            
            // if this is an external predicate, assume it is already there at the other context, i.e., ignore it
            if(pred.getNodeId()!=null)
                continue; // TODO AW this can trigger some bug in the following case: if we locally import n1:q(X) but the program of n1 does not contain q/1.
            
            String name = pred.getName();
            Integer arity=pred.getArity();
            Integer ser_id = counter++;
            predicates.put(new Pair<String, Integer>(name,arity), ser_id);
            //predicate_mapping.get(local_name).put(ser_id, pred);
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

        for (Entry<String, ANodeInterface> other : other_nodes.entrySet()) {
            // tell active domain to other nodes except self
            if(!other.getKey().equals(local_name)) {
                other.getValue().receive_active_domain(local_name, predicates,
                        functions, constants);
            }  
        }
        
        return counter;
    }

    @Override
    public ReplyMessage exchange_import_domain() throws RemoteException {
        //System.out.println("Node[" + local_name +"]: Exchanging import domain.");
        // tell import interface to all nodes
        for(Entry<String,ANodeInterface> other : other_nodes.entrySet()) {
            //System.out.println("Node[" + local_name +"]: Import predicates from Node["+other.getKey()+"]: " + import_predicates.get(other.getKey()));
            // only exchange import if it is non-empty
            if(import_predicates.get(other.getKey())!=null) {   
                other.getValue().receiveNextFactsFrom(local_name);     // needed to de-serialize predicates at other node
                other.getValue().receive_import_domain(local_name,import_predicates.get(other.getKey()));
            }
        }
        return ReplyMessage.SUCCEEDED;
    }
    
    

    @Override
    public ReplyMessage receive_active_domain(String other_node, Map<Pair<String, Integer>, Integer> predicates,
                    Map<String, Integer> functions,
                    Map<String, Integer> constants) throws RemoteException {
       
        //System.out.println("Node[" + local_name +"]: Receiving active domain from " + other_node);        
        //System.out.println("Node[" + local_name +"]: Adding predicates: "+predicates);
        
        
        // fill mapping: predicates
        HashMap<Integer,Predicate> pred_map = new HashMap<Integer, Predicate>();
        for (Entry<Pair<String,Integer>,Integer> pred_desc: predicates.entrySet()) {
            
            // TODO AW hack to normalize predicate names
            // n2:q occuring in context n1 locally is q
            String local_pred_name;
            if(pred_desc.getKey().getArg1().contains(":")) {
                throw new RemoteException("Node[" + local_name +"]: Received external predicate from Node["+other_node+"]");
             } else {
                // localize predicate name
                local_pred_name = other_node+":"+pred_desc.getKey().getArg1();
            }
            Predicate pred = Predicate.getPredicate(local_pred_name, pred_desc.getKey().getArg2());
            //ctx.getRete().addPredicatePlus(pred);
            pred_map.put( pred_desc.getValue(), pred);
            //System.out.println("Node[" + local_name +"]: Added to mapping: "+pred+" with value "+pred_desc.getValue()+" from "+other_node);
        }
        predicate_mapping.put(other_node, pred_map);
        
        //System.out.println("Adding function symbols: "+functions);
        
        // fill mapping: function symbols
        for(Entry<String, Integer> func_desc : functions.entrySet()) {
            FunctionSymbol fun = FunctionSymbol.getFunctionSymbol(func_desc.getKey());
            deser_mapping.put(func_desc.getValue(), fun);
            out_mapping.put(fun, func_desc.getValue());
        }
        
        //System.out.println("Adding constants: "+constants);
        
        // fill mapping: constants
        for(Entry<String, Integer> con_desc : constants.entrySet()) {
            Constant con = Constant.getConstant(con_desc.getKey());
            deser_mapping.put(con_desc.getValue(), con); 
            out_mapping.put(con, con_desc.getValue());
            
        }
        
        //System.out.println("Node[" + local_name +"]: Active domain mapping wrt. node "+other_node+" created.");
        
        return ReplyMessage.SUCCEEDED;
    }
    


    @Override
    public ReplyMessage receiveNextFactsFrom(String from_node) throws RemoteException {
        serializingFrom = from_node;
        
        //System.out.println("Node[" + local_name +"]: The next facts will come from Node[" + from_node+"]");
        //ctx.printAnswerSet(filter);
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage receive_import_domain(String from, List<Pair<String,Integer>> required_predicates) throws RemoteException {
        //System.out.println("Node[" + local_name + "]: got import domain from Node[" + from+"]");
        //System.out.println("Node[" + local_name + "]: required predicates are: "+required_predicates);

        ArrayList<Predicate> required_preds = new ArrayList<Predicate>();
        for (Iterator<Pair<String, Integer>> it = required_predicates.iterator(); it.hasNext();) {
            Pair<String, Integer> pair = it.next();
            if(!pair.getArg1().startsWith(local_name+":"))
                    throw new RemoteException("Node[" + local_name +"]: Node identifier of received import predicate ["+pair.getArg1()+"] differs from local name ["+local_name+"]");
            String local_pred_name = pair.getArg1().replaceFirst(".*:", "");
            //System.out.println("Node[" + local_name +"]: Localized predicate "+ pair.getArg1() + " to "+local_pred_name);
            // check that the imported predicate exists locally
            if(!local_predicates.contains(Predicate.getPredicate(local_pred_name, pair.getArg2()))) {
                throw new RemoteException("Node["+local_name+"]: requested non-existent predicate "+pair.getArg1()+"/"+pair.getArg2()+" from Node "+from);
            }
            required_preds.add(Predicate.getPredicate(local_pred_name, pair.getArg2()));
            
        }
        // build list of other nodes that import local predicates
        for (Predicate predicate : required_preds) {
            if( !ANodeImpl.required_predicates.containsKey(predicate))
                ANodeImpl.required_predicates.put(predicate, new ArrayList<ANodeInterface>());
            ANodeInterface from_node = other_nodes.get(from);
            ANodeImpl.required_predicates.get(predicate).add(from_node);
        }
        //ANodeImpl.required_predicates.put(from, (ArrayList<Predicate>)required_preds);
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage firstPropagate() throws RemoteException {        
        //System.out.println("Node[" + local_name + "]: firstPropagate.");
        long start_propagate = System.currentTimeMillis();
        ctx.propagate();
        solving_time = solving_time + System.currentTimeMillis() - start_propagate;
        
        //System.out.println("Node[" + local_name + "]: after firstPropagate. interpretation is ");
        //ctx.printAnswerSet(filter);
        
        if (ctx.isSatisfiable())
        {
            //System.out.println("Node[" + local_name + "]: firstPropagate. Pushing.");
            
            ReplyMessage reply = pushDerivedFacts(0, 0);
            
            if (reply == ReplyMessage.SUCCEEDED)
            {
                //System.out.println("Node[" + local_name + "]: All neighbors propagated successfully. Return SUCCEEDED");
                return ReplyMessage.SUCCEEDED;
            }
            else
            {
                //System.out.println("Node[" + local_name + "]: A neighbor got inconsistent. Return INCONSISTENT");
                return ReplyMessage.INCONSISTENT;
            }
        }
        else
        {
            //System.out.println("Node[" + local_name + "]: firstPropagate. Return INCONSISTENT.");
            return ReplyMessage.INCONSISTENT;
        }           
    }
  
    @Override
    public ReplyMessage localBacktrack(int global_level) throws RemoteException {
        //System.out.println("Node[" + local_name + "]: start in backtrack. local_dc = " + ctx.getDecisionLevel());
        
        Integer local_dc = global_to_local_dc.get(global_level);
        
        //System.out.println("Node[" + local_name + "]: backtrack to global_level = " + global_level);
        
        if (local_dc != null)
        {
            //System.out.println("Node[" + local_name + "]: corresponding local level = " + local_dc);
        
            long start_backtrack = System.currentTimeMillis();
            ctx.backtrackTo(local_dc.intValue());
            
            
            if (global_to_local_dc.remove(global_level+1) == null)
            {
                global_to_local_dc.remove(global_level);
            }

            // remove all re-opened predicates from the list of closed predicates
            for (Iterator<Predicate> it = closed_predicates.iterator(); it.hasNext();) {
                Predicate predicate = it.next();
                if( !ctx.getRete().getBasicNodeMinus(predicate).isClosed() )
                    it.remove();
            }
            solving_time = solving_time + System.currentTimeMillis() - start_backtrack;
            
            //int gl1 = global_level+1;            
            //System.out.println("Node[" + local_name + "]: makeBranch. remove(" + gl1 + ").");
            //System.out.println("After backtracking. Current local decision level = " + ctx.getDecisionLevel());
        }
        //else
        //{
        //    System.out.println("Node[" + local_name + "]: no corresponding local level found.");
        //}
        
        return ReplyMessage.SUCCEEDED;
    }

    @Override
    public ReplyMessage printAnswer() throws RemoteException {
        System.out.println("Node[" + local_name + "]: print interpretation:");
        ctx.printAnswerSet(filter);
        
        return ReplyMessage.SUCCEEDED;
    }


    
    @Override
    public ReplyMessage finalClosing(int global_level) throws RemoteException {
        // increase the outside global_level by one before calling this method
        //System.out.println("Node[" + local_name + "]: finalClosing. Store (global_level,local_dc) = (" + global_level + ", " + ctx.getDecisionLevel() +")");
        
        long start_final_closing = System.currentTimeMillis();
        global_to_local_dc.put(global_level, ctx.getDecisionLevel());
        
        
        int dec = ctx.getDecisionLevel()+1;
        for (Predicate predicate : local_predicates) {
            if( predicate.getNodeId()!= null) {
                closed_predicates.add(predicate);
                ctx.closeFactFromOutside(predicate);
            }
        }        
        
        ArrayList<LinkedList<Pair<Node, Instance>>> new_facts = ctx.deriveNewFacts();
        for (int dl = dec; dl < new_facts.size(); dl++) {
            for (Pair<Node, Instance> pair : new_facts.get(dl)) {
                if(pair.getArg1().getClass().equals(BasicNode.class)) {
                    //System.out.println("Node["+local_name+"]: finalClosing derived new fact: "+((BasicNode)pair.getArg1()).getPred()+" "+ pair.getArg2());
                    solving_time = solving_time + System.currentTimeMillis() - start_final_closing;
                    return ReplyMessage.INCONSISTENT;  
                }
            }
        }
        
        /*HashMap<Predicate, HashSet<Instance>> new_facts = ctx.deriveNewFacts(dec);
        System.out.println("Node["+local_name+"]: finalClosing, new_facts: "+new_facts);

        for (HashSet<Instance> hashSet : new_facts.values()) {
            if( !hashSet.isEmpty() )
                return ReplyMessage.INCONSISTENT;  
        }*/
        
        if (!ctx.isSatisfiable())
        {
            solving_time = solving_time + System.currentTimeMillis() - start_final_closing;
            return ReplyMessage.INCONSISTENT;
        }
        
        solving_time = solving_time + System.currentTimeMillis() - start_final_closing;
        return ReplyMessage.SUCCEEDED;
    }    
    

    
    //***************************************************************************************************************************************************************
    
    public ReplyMessage propagateAfterBeingPushed(int global_level, int decision_level_before_push) throws RemoteException {
        long start_propagation = System.currentTimeMillis();
        ctx.propagate();
        solving_time = solving_time + System.currentTimeMillis() - start_propagation;
        
        //System.out.println("Node[" + local_name + "]: after propagate. interpretation is ");
        //ctx.printAnswerSet(filter);
        
        
        if (ctx.isSatisfiable())
        {
            //System.out.println("Node[" + local_name + "]: propagate. Pushing.");
            
            ReplyMessage reply = pushDerivedFacts(global_level, decision_level_before_push);
            
            if (reply == ReplyMessage.SUCCEEDED)
            {
                //System.out.println("Node[" + local_name + "]: All neighbors propagated successfully. Return SUCCEEDED");
                return ReplyMessage.SUCCEEDED;
            }
            else
            {
                //System.out.println("Node[" + local_name + "]: A neighbor got inconsistent. Return INCONSISTENT");
                return ReplyMessage.INCONSISTENT;
            }
        }
        else
        {
            //System.out.println("Node[" + local_name + "]: propagate. Return INCONSISTENT. local_dc = " + ctx.getDecisionLevel());
            return ReplyMessage.INCONSISTENT;
        }        
    }
        
    
    @Override
    public ReplyMessage handleAddingFacts(int global_level, Map<Predicate, ArrayList<Instance>> in_facts, List<Predicate> closed_predicates) throws RemoteException {
        //System.out.println("Node[" + local_name +"]: Received facts from " + serializingFrom + ":");
        //System.out.println("Node[" + local_name +"]: decision_level_before_push = " + ctx.getDecisionLevel());
       
        int decision_level_before_push = ctx.getDecisionLevel();
                
        if (global_to_local_dc.get(global_level) == null)
        {
            //System.out.println("Node[" + local_name + "]: handleAddingFacts. Store (global_level,local_dc) = (" + global_level + ", " + decision_level_before_push +")");
        
            global_to_local_dc.put(global_level, decision_level_before_push);
        }
        
        
        HashMap<Predicate,ArrayList<Instance>> in_facts_map = (HashMap<Predicate,ArrayList<Instance>>) in_facts;
        long start_adding_facts = System.currentTimeMillis();
        ctx.addFactsFromOutside(in_facts_map);
        solving_time = solving_time + System.currentTimeMillis() - start_adding_facts;
        
        /*for(Entry<Predicate, ArrayList<Instance>> pred : in_facts.entrySet()) {
            
            // print out what was received
            System.out.println("Node[" + local_name +"]: Predicate "+pred.getKey().getName()+"/"+
                                pred.getKey().getArity()+ ", "+
                                pred.getValue().size()+" entries.");
            for (Iterator it = pred.getValue().iterator(); it.hasNext();) {
                Instance inst = (Instance)it.next();
                System.out.println("Node[" + local_name +"]: Instance: "+inst.toString());
                
                // actual adding of the facts
                System.out.println("Node[" + local_name +"]: Adding Predicate / Instance = "+pred.getKey()+"/"+inst);
                ANodeImpl.ctx.addFactFromOutside(pred.getKey(), inst);
            }
        }*/
        
        if(closed_predicates != null ) {
            //System.out.println("Node[" + local_name + "]: Closed predicates are: " + closed_predicates);
            long start_adding_closed_preds = System.currentTimeMillis();
            for (Predicate predicate : closed_predicates) {
                ctx.closeFactFromOutside(predicate);
            }
            solving_time = solving_time + System.currentTimeMillis() - start_adding_closed_preds;
        }
        
        //System.out.println("Node[" + local_name + "]: Received facts end.");
        //System.out.println("Node[" + local_name + "]: HAF: decision level after push = " + ctx.getDecisionLevel());
        
        return propagateAfterBeingPushed(global_level, decision_level_before_push);
    }
    
    private ReplyMessage pushDerivedFacts(int global_level, int from_decision_level) {
        //System.out.println("Node[" + local_name +"]: pushDerivedFacts. from decision level = " + from_decision_level);
        long start_derive_new_facts = System.currentTimeMillis();
        ArrayList<LinkedList<Pair<Node,Instance>>> new_facts = ctx.deriveNewFacts();
        
        
        //System.out.println("Node[" + local_name +"]: PushDerivedFacts: required_predicates ="+required_predicates);
        //System.out.println("Node[" + local_name +"]: PushDerivedFacts: new_facts ="+new_facts);
        
        // collect newly derived instances for required facts
        HashMap<ANodeInterface,HashMap<Predicate,ArrayList<Instance>>> to_push =
                new HashMap<ANodeInterface, HashMap<Predicate, ArrayList<Instance>>>();

        for (int dl= from_decision_level; dl < new_facts.size(); dl++) 
        {
            LinkedList<Pair<Node,Instance>> added_in_dl = new_facts.get(dl);
            
            for (Pair<Node, Instance> pair : added_in_dl) {
                if( pair.getArg1().getClass().equals(BasicNode.class)) {
                    Predicate pred = ((BasicNode)pair.getArg1()).getPred();
                    if( required_predicates.containsKey(pred)) {
                        for (ANodeInterface other : required_predicates.get(pred)) {
                            if(!to_push.containsKey(other))
                                to_push.put(other, new HashMap<Predicate, ArrayList<Instance>>());
                            if(!to_push.get(other).containsKey(pred))
                                to_push.get(other).put(pred, new ArrayList<Instance>());
                            to_push.get(other).get(pred).add(pair.getArg2());
                        }
                    }
                }
            }   
        }
            
        // collect all newly closed predicates
        HashMap<ANodeInterface,ArrayList<Predicate>> closed_preds =new HashMap<ANodeInterface, ArrayList<Predicate>>();
        for (Entry<Predicate, ArrayList<ANodeInterface>> entry : required_predicates.entrySet()) {
            if( ctx.getRete().getBasicNodeMinus(entry.getKey()).isClosed() && !closed_predicates.contains(entry.getKey())) {
                closed_predicates.add(entry.getKey());
                for (ANodeInterface aNodeInterface : entry.getValue()) {
                    if(!closed_preds.containsKey(aNodeInterface) )
                        closed_preds.put(aNodeInterface, new ArrayList<Predicate>());
                    closed_preds.get(aNodeInterface).add(entry.getKey());
                }
            }
        }
         
        solving_time = solving_time + System.currentTimeMillis() - start_derive_new_facts;
         
         for (ANodeInterface aNodeInterface : other_nodes.values()) {
            Map<Predicate,ArrayList<Instance>> in_facts = to_push.get(aNodeInterface);
            List<Predicate> closed_predicates = closed_preds.get(aNodeInterface);
            
            if( in_facts == null && closed_predicates == null ) {
                //try {
                //    System.out.println("Node["+local_name+"]: Nothing to push for Node[" + aNodeInterface.getName() + "].");
                //} catch (RemoteException ex) {
                //    Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
                //}
                continue;
            }
            
            try {
                //System.out.println("Node[" + local_name +"]: PushDerivedFacts: to Node[" + aNodeInterface.getName() + "]");
                //System.out.println("Node[" + local_name +"]: PushDerivedFacts: closed_predicates = " + closed_predicates);
                //System.out.println("Node[" + local_name +"]: PushDerivedFacts: to_push = " + to_push);
                aNodeInterface.receiveNextFactsFrom(local_name);
                ReplyMessage reply = aNodeInterface.handleAddingFacts(global_level, in_facts, closed_predicates);
                if (reply == ReplyMessage.INCONSISTENT)
                {
                    return ReplyMessage.INCONSISTENT;
                }                
            } catch (RemoteException ex) {
                //try {
                //    System.out.println("Node[" + local_name +"]: Exception in pushing derived facts to:"+aNodeInterface.getName());
                //} catch (RemoteException ex1) {
                    Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                //    Logger.getLogger(ANodeImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         return ReplyMessage.SUCCEEDED;
    }
    
    // only called by makeChoice or makeBranch
    private ReplyMessage makePropagation(int global_level)
    {
        long start_propagate = System.currentTimeMillis();
        ctx.propagate();
        solving_time = solving_time + System.currentTimeMillis() - start_propagate;
        
        if (ctx.isSatisfiable())
        {
            return pushDerivedFacts(global_level, ctx.getDecisionLevel());
        }
        else
        {
            return ReplyMessage.INCONSISTENT;
        }
    }
    
    @Override
    public ReplyMessage makeChoice(int global_level) throws RemoteException {
        long start_choice = System.currentTimeMillis();
        int dc_before_choice = ctx.getDecisionLevel();
        boolean has_choice = ctx.choice();
        solving_time = solving_time + System.currentTimeMillis() - start_choice;
        
        if (has_choice)
        {
            //System.out.println("Node[" + local_name +"]: makeChoice. store(gl,dc_before_make_choice) = (" + global_level + "," + dc_before_choice +")");
            global_to_local_dc.put(global_level, dc_before_choice);
            //System.out.println("Node[" + local_name +"]: makeChoice. now call makePropagation(" + global_level + ")");            
            return makePropagation(global_level);
        }
        else
        { 
            //System.out.println("Node[" + local_name +"]: makeChoice. return NO_MORE_CHOICE");
            return ReplyMessage.NO_MORE_CHOICE;
        }
    }
    
    @Override
    public ReplyMessage makeBranch(int global_level) throws RemoteException {        
        long start_branch = System.currentTimeMillis();
        int dc_before_branch = ctx.getDecisionLevel();
        ReplyMessage has_branch = ctx.nextBranch();        
        solving_time = solving_time + System.currentTimeMillis() - start_branch;
        
        if (has_branch == ReplyMessage.HAS_BRANCH)
        {
            //System.out.println("Node[" + local_name +"]: makeBranch. store(gl,dc_before_make_branch) = (" + global_level + "," + dc_before_branch +")");
            global_to_local_dc.put(global_level, dc_before_branch);
            //System.out.println("Node[" + local_name +"]: makeBranch. now call makePropagation(" + global_level + ")");
            return makePropagation(global_level);
        }
        else
        {
            //System.out.println("Node[" + local_name +"]: makeBranch. return NO_MORE_BRANCH");
            return ReplyMessage.NO_MORE_BRANCH;
        }
    }
        
    public static void main(String[] args) {
        String local_name = args[0];
        String filename = args[1];
        
        String filter;
        if (args.length == 3)
        {
            filter = args[2];
        }
        else
        {
            filter = null;//"";
        }
        
        //System.out.println("Node[" + local_name +"]: Starting NodeImpl.main(). args[0] = " + local_name);
        
        //System.out.println("Node[" + local_name +"]: Input file is: " + filename);
        
        // create and export the local node
        ANodeImpl local_node= new ANodeImpl(local_name, filename, filter);
    }
    
    @Override
    public ReplyMessage initGlobalLevelZero() throws RemoteException {
        //System.out.println("Node[" + local_name +"]: init zero global level to: " + ctx.getDecisionLevel());
        
        global_to_local_dc.put(0, ctx.getDecisionLevel());
        return ReplyMessage.SUCCEEDED;
    }
}
