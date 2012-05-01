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
    public void init(Map<String,Remote> other_nodes) throws RemoteException;
    
    /*
     * node_name: name to which the mapping belongs to
     * predicates: (Name,Arity) -> SerializeInt
     * functions: Name -> SerializeInt
     * constants: Name -> SerializeInt
     */
    public void tell_active_domain(String node_name,
            Map<Pair<String,Integer>,Integer> predicates,
            Map<String,Integer> functions,
            Map<String,Integer> constants ) throws RemoteException;
    
    /*
     * Node gets informed about the predicates required from another node
     * required_predicates: SerializeInt of the required predicates
     */
    public void tell_import_domain(String from, List<Predicate> required_predicates) throws RemoteException;
    
    /*
     * This is ugly, but needed to tune the de-serialization of the later
     * handleAddingFacts, it needs to know where the facts come from, before it
     * actually starts to deserialize them.
     */
    public ReplyMessage receiveNextFactsFrom(String from_node) throws RemoteException;
    
    public ReplyMessage handleAddingFacts(Map<Predicate, ArrayList<Instance>> in_facts) throws RemoteException;
    
    public ReplyMessage makeChoice(int global_level) throws RemoteException;
    
    public ReplyMessage makeAlternative() throws RemoteException;

    public ReplyMessage localBacktrack(int global_level) throws RemoteException;
    
    public ReplyMessage testInstanceExchange() throws RemoteException;
}
