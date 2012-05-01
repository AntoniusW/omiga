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
public class TestNodeImpl2 implements NodeInterface {

    private int count;
    
    public TestNodeImpl2()
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
            case 2:
            case 4:
            case 19:
                System.out.println("TestNodeImpl_2::hasMoreChoice()? count = " + count + ". return TRUE.");
                return true;
            case 6:
            case 24:
            case 28:
                System.out.println("TestNodeImpl_2::hasMoreChoice()? count = " + count + ". return FALSE.");
                return false;
            default:
                throw new UnsupportedOperationException("TestNodeImpl_2::hasMoreChoice()? ERROR with count = " + count);
        }
    }

    @Override
    public ReplyMessage makeChoice(int global_level) throws RemoteException {
        count++;
        
        switch (count)
        {
            case 3:
            case 5:
                System.out.println("TestNodeImpl_2::makechoice(" + global_level + "). count = " + count + ". return SUCCEEDED.");
                return ReplyMessage.SUCCEEDED;
            case 20:
                System.out.println("TestNodeImpl_2::makechoice(" + global_level + "). count = " + count + ". return INCONSISTENT.");              
                return ReplyMessage.INCONSISTENT;
            default:
                throw new UnsupportedOperationException("TestNodeImpl_2::makeChoice(" + global_level + "). ERROR with count = " + count);                
        } 
    }

    @Override
    public ReplyMessage hasMoreBranch() throws RemoteException {
        count++;
        
        switch (count)
        {
            case 8:            
            case 13:
            case 22:
                System.out.println("TestNodeImpl_2::hasMoreBranch()? count = " + count + ". return HAS_BRANCH.");
                return ReplyMessage.HAS_BRANCH;
            case 11:
                System.out.println("TestNodeImpl_2::hasMoreBranch()? count = " + count + ". return NO_MORE_BRANCH.");
                return ReplyMessage.NO_MORE_BRANCH;
            case 16:
            case 26:
                System.out.println("TestNodeImpl_2::hasMoreBranch()? count = " + count + ". return NO_MORE_ALTERNATIVE.");
                return ReplyMessage.NO_MORE_ALTERNATIVE;
            default:
                throw new UnsupportedOperationException("TestNodeImp2::hasMoreBranch()? ERROR with count = " + count);                
        }
    }

    @Override
    public ReplyMessage makeBranch() throws RemoteException {
        count++;
        switch (count)
        {
            case 23:
                System.out.println("TestNodeImpl_2::makeBranch. count = " + count + ". return SUCCEEDED.");               
                return ReplyMessage.SUCCEEDED;
            case 9:
            case 14:
                System.out.println("TestNodeImpl_2::makeBranch. count = " + count + ". return INCONSISTENT.");
                return ReplyMessage.INCONSISTENT;
            default:
                throw new UnsupportedOperationException("TestNodeImpl_2::makeBranch. ERROR with count = " + count);                                
        }
    }

    @Override
    public ReplyMessage localBacktrack(int global_level) throws RemoteException {
        count++;
        System.out.println("TestNodeImpl_2::localBacktrack(" + global_level + "). count = " + count + ". return SUCCEEDED.");
        return ReplyMessage.SUCCEEDED;
    }
    
    public static void main(String[] args) {
        System.out.println("Starting TestNodeImpl_2.main(). args[0] = " + args[0]);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        try {
            System.out.println("Starting context ");
            // args[0] is the context id
            String name = "Context_" + args[0];
            System.out.println("name = " + name);
            NodeInterface local_node = new TestNodeImpl2();
            NodeInterface stub =
                (NodeInterface) UnicastRemoteObject.exportObject(local_node, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("TestNodeImpl_2 bound");
        } catch (Exception e) {
            System.err.println("TestNodeImpl_2 exception:");
            e.printStackTrace();
        }
    }    
}
