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
    
    /*
     * node_name: the name assigned to this node
     * other_nodes: all the other nodes in the system as (name,remote) pairs
     * serialize_start: lower boundary for serialization
     * returns: upper boundary of serialization
     */
    public int init(String node_name, ArrayList<Pair<String,ANodeInterface>> other_nodes, int serlialize_start) throws RemoteException;
    
    /*
     * node_name: from where this domain is
     * predicates: (Name,Arity) -> SerializeInt
     * functions: Name -> SerializeInt
     * constants: Name -> SerializeInt
     */
    public ReplyMessage tell_active_domain(String node_name,
            Map<Pair<String,Integer>,Integer> predicates,
            Map<String,Integer> functions,
            Map<String,Integer> constants ) throws RemoteException;
    
    /*
     * Node gets informed about the predicates required from another node
     * required_predicates: SerializeInt of the required predicates
     */
    public ReplyMessage tell_import_domain(String from, List<Predicate> required_predicates) throws RemoteException;
    
    public ReplyMessage receiveNextFactsFrom(String node_name) throws RemoteException;
    
    public ReplyMessage handleAddingFacts(int global_level, Map<Predicate, ArrayList<Instance>> in_facts) throws RemoteException;
    
    public boolean hasMoreChoice() throws RemoteException;
    
    public ReplyMessage propagate(int global_level) throws RemoteException;

    public ReplyMessage hasMoreBranch() throws RemoteException;

    public ReplyMessage localBacktrack(int global_level) throws RemoteException;
    
    public ReplyMessage printAnswer() throws RemoteException;    
}
