/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import Entity.Instance;
import Entity.Predicate;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Minh Dao-Tran
 */
public interface ANodeInterface extends Remote {
    public ReplyMessage finalClosing(int global_level) throws RemoteException;
    
    /*
     * Initialize local node and read in local program.
     * node_name: the name assigned to this node
     * other_nodes: all the other nodes in the system as (name,remote) pairs
     */
    public ReplyMessage init(ArrayList<Pair<String,ANodeInterface>> other_nodes) throws RemoteException;
    
    /*
     * Send active domain and import domain to other nodes.
     * serialize_start: lower boundary for serialization
     *  returns: upper boundary of serialization
     */
    public int exchange_active_domain(int serialize_start) throws RemoteException;
    
    /*
     * Send import domains to all other nodes.
     */
    public ReplyMessage exchange_import_domain() throws RemoteException;
    
    /*
     * node_name: from where this domain is
     * predicates: (Name,Arity) -> SerializeInt
     * functions: Name -> SerializeInt
     * constants: Name -> SerializeInt
     */
    public ReplyMessage receive_active_domain(String node_name,
            Map<Pair<String,Integer>,Integer> predicates,
            Map<String,Integer> functions,
            Map<String,Integer> constants ) throws RemoteException;
    
    /*
     * Node gets informed about the predicates required from another node
     * required_predicates:  List of Name/Arity of the required predicates
     */
    public ReplyMessage receive_import_domain(String from, List<Pair<String,Integer>> required_predicates) throws RemoteException;
    
    public ReplyMessage receiveNextFactsFrom(String node_name) throws RemoteException;
    
    public ReplyMessage handleAddingFacts(int global_level, Map<Predicate, ArrayList<Instance>> in_facts, List<Predicate> closed_predicates) throws RemoteException;
    
    public boolean hasMoreChoice(int global_level) throws RemoteException;

    public ReplyMessage firstPropagate() throws RemoteException;

    
    public ReplyMessage propagate(int global_level) throws RemoteException;

    public ReplyMessage hasMoreBranch(int global_level) throws RemoteException;

    public ReplyMessage localBacktrack(int global_level) throws RemoteException;
    
    public ReplyMessage printAnswer() throws RemoteException;
    
    public String getName() throws RemoteException;
    
    public ReplyMessage makeChoice(int global_level) throws RemoteException;
    
    public ReplyMessage makeBranch(int global_level) throws RemoteException;
    
    public ReplyMessage initGlobalLevelZero() throws RemoteException;
}
