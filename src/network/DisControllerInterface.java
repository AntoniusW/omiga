/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Minh Dao-Tran, Antonius Weinzierl
 */
public interface DisControllerInterface extends Remote {
    
    public int getCountAnswers()  throws RemoteException;
    
    public int getCountPotentialAnswers()  throws RemoteException;
    
    public ReplyMessage finalClosing() throws RemoteException;
    
    public ReplyMessage init(ArrayList<Pair<String, DisControllerInterface>> controller) throws RemoteException;
    
    public void makeChoice(int global_level, int last_guy) throws RemoteException;
    
    public void makeBranch(int global_level) throws RemoteException;
    
    public ReplyMessage localPrintAnswer() throws RemoteException;
    
    public ReplyMessage localBacktrack(int global_level) throws RemoteException;
}
