/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Minh Dao-Tran
 */
public class NodeImpl implements NodeInterface {

    public NodeImpl() {
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
        System.out.println("Starting NodeImpl.main(). args[0] = " + args[0]);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        try {
            System.out.println("Starting context ");
            // args[0] is the context id
            String name = "Context" + args[0];
            System.out.println("name = " + name);
            NodeInterface local_node = new NodeImpl();
            NodeInterface stub =
                (NodeInterface) UnicastRemoteObject.exportObject(local_node, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("NodeImpl bound");
        } catch (Exception e) {
            System.err.println("NodeImpl exception:");
            e.printStackTrace();
        }
    }
    
}
