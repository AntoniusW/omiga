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
public interface NodeInterface extends Remote {
    public ReplyMessage handleAddingFacts() throws RemoteException;
    
    public boolean hasMoreChoice() throws RemoteException;
    
    public ReplyMessage makeChoice(int global_level) throws RemoteException;

    public ReplyMessage hasMoreBranch() throws RemoteException;
    
    public ReplyMessage makeBranch() throws RemoteException;

    public ReplyMessage localBacktrack(int global_level) throws RemoteException;
}
