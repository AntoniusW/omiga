/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
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
    public void tell_active_domain(String node_name, Map<Pair<String,Integer>,Integer> predicates,
                       Map<String,Integer> functions, Map<String,Integer> constants) throws RemoteException;
    
    public ReplyMessage handleAddingFacts() throws RemoteException;
    
    public ReplyMessage makeChoice(int global_level) throws RemoteException;
    
    public ReplyMessage makeAlternative() throws RemoteException;

    public ReplyMessage localBacktrack(int global_level) throws RemoteException;
}
