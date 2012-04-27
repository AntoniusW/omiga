/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Minh Dao-Tran
 */
public class ANodeImpl implements ANodeInterface {

    public ANodeImpl() {
        super();
    }            
    
    @Override
    public ReplyMessage handleAddingFacts() throws RemoteException {
        return ReplyMessage.SUCCEEDED;
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
        String ctx_id = "ctx1";
        System.out.println("Starting NodeImpl.main(). args[0] = " + ctx_id);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager() {
                @Override
                    public void checkConnect (String host, int port) {}
                @Override
                    public void checkConnect (String host, int port, Object context) {}
                } );
        }
        
        try {
            // args[0] is the context id
            String name = "Context" + ctx_id;
            System.out.println("name = " + name);
            ANodeInterface local_node = new ANodeImpl();
            ANodeInterface stub =
                (ANodeInterface) UnicastRemoteObject.exportObject(local_node, 9000);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("NodeImpl bound");
        } catch (Exception e) {
            System.err.println("ANodeImpl exception:");
            e.printStackTrace();
        }
    }
    
}
