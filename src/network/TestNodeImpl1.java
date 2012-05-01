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
 * @author minhdt
 */
public class TestNodeImpl1 implements NodeInterface {

    private int count = 0;
    
    public TestNodeImpl1()
    {
        super();
        count = 0;
    }
    
    @Override
    public ReplyMessage handleAddingFacts() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasMoreChoice() throws RemoteException {
        count++;
        
        switch (count)
        {
            case 1:
            case 3:
            case 20:                        
                System.out.println("TestNodeImpl_1::hasMoreChoice()? count = " + count + ". return TRUE.");
                return true;
            case 8:
            case 9:
            case 10:
            case 22:
            case 24:
            case 29:
                System.out.println("TestNodeImpl_1::hasMoreChoice()? count = " + count + ". return FALSE.");
                return false;
            default:
                throw new UnsupportedOperationException("TestNodeImpl_1::hasMoreChoice()? ERROR with count = " + count);
        }
    }

    @Override
    public ReplyMessage makeChoice(int global_level) throws RemoteException {
        count++;
        
        switch (count)
        {
            case 2:
            case 21:
                System.out.println("TestNodeImpl_1::makechoice(" + global_level + "). count = " + count + ". return SUCCEEDED.");
                return ReplyMessage.SUCCEEDED;
            case 4:
                System.out.println("TestNodeImpl_1::makechoice(" + global_level + "). count = " + count + ". return INCONSISTENT.");              
                return ReplyMessage.INCONSISTENT;
            default:
                throw new UnsupportedOperationException("TestNodeImpl_1::makeChoice(" + global_level + "). ERROR with count = " + count);                
        }        
    }

    @Override
    public ReplyMessage hasMoreBranch() throws RemoteException {
        count++;
        
        switch (count)
        {
            case 6:            
            case 18:
            case 27:
                System.out.println("TestNodeImpl_1::hasMoreBranch()? count = " + count + ". return HAS_BRANCH.");
                return ReplyMessage.HAS_BRANCH;
            case 16:
            case 31:
                System.out.println("TestNodeImpl_1::hasMoreBranch()? count = " + count + ". return NO_MORE_BRANCH.");
                return ReplyMessage.NO_MORE_BRANCH;
            case 33:
                System.out.println("TestNodeImpl_1::hasMoreBranch()? count = " + count + ". return NO_MORE_ALTERNATIVE.");
                return ReplyMessage.NO_MORE_ALTERNATIVE;
            default:
                throw new UnsupportedOperationException("TestNodeImpl::hasMoreBranch()? ERROR with count = " + count);                
        }
    }

    @Override
    public ReplyMessage makeBranch() throws RemoteException {
        count++;
        switch (count)
        {
            case 7:
            case 19:
            case 28:
                System.out.println("TestNodeImpl_1::makeBranch. count = " + count + ". return SUCCEEDED.");               
                return ReplyMessage.SUCCEEDED;
            default:
                throw new UnsupportedOperationException("TestNodeImpl_1::makeBranch. ERROR with count = " + count);                                
        }
    }

    @Override
    public ReplyMessage localBacktrack(int global_level) throws RemoteException {
        count++;
        System.out.println("TestNodeImpl_1::localBacktrack(" + global_level + "). count = " + count + ". return SUCCEEDED.");
        return ReplyMessage.SUCCEEDED;
    }
    
    public static void main(String[] args) {
        System.out.println("Starting TestNodeImpl_1.main(). args[0] = " + args[0]);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        try {
            System.out.println("Starting context ");
            // args[0] is the context id
            String name = "Context_" + args[0];
            System.out.println("name = " + name);
            NodeInterface local_node = new TestNodeImpl1();
            NodeInterface stub =
                (NodeInterface) UnicastRemoteObject.exportObject(local_node, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("TestNodeImpl_1 bound");
        } catch (Exception e) {
            System.err.println("TestNodeImpl_1 exception:");
            e.printStackTrace();
        }
    }    
}
