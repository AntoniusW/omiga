/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Minh Dao-Tran
 */
public interface ANodeInterface extends Remote {
    public ReplyMessage handleAddingFacts() throws RemoteException;
    
    public ReplyMessage makeChoice(int global_level) throws RemoteException;
    
    public ReplyMessage makeAlternative() throws RemoteException;

    public ReplyMessage localBacktrack(int global_level) throws RemoteException;
}
