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
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Minh Dao-Tran, Antonius Weinzierl
 */
public class DisControllerImpl implements DisControllerInterface {
    private int myid;
    private int system_size;
    private ANodeInterface local_node;        
    private ArrayList<Pair<String, DisControllerInterface>> all_controllers;
    
    Stack<Pair<Integer, Integer> > stack = new Stack<Pair<Integer,Integer> >();
    
    public DisControllerImpl(int id, int size, String filename)
    {
        try {
            myid = id;
            system_size = size;
            String local_name = "n" + (myid+1);
            
            //registry = LocateRegistry.getRegistry("127.0.0.1");
            //local_node = (ANodeInterface) registry.lookup(local_name);
            local_node = new ANodeImpl(local_name, filename);
        }
        catch (Exception e) {
            System.err.println("Controller ctor ERROR.");
            e.printStackTrace();
        }
    }
    
    // called by Client
    public ReplyMessage init(ArrayList<Pair<String, DisControllerInterface>> controllers) throws RemoteException
    {
        all_controllers = controllers;
        
        return ReplyMessage.SUCCEEDED;
    }
    
    @Override
    public ReplyMessage localBacktrack(int global_level) throws RemoteException {
        return local_node.localBacktrack(global_level);
    }
    
    private void backTrack(int global_level)
    {
        try
        {
            for (int i = 0; i < myid; i++)
            {
                System.out.println("DisController. backTrack(). "
                        + "Requesting localBackTrack(" + global_level + ") to node[" + i + "]");                  
                ReplyMessage reply = all_controllers.get(i).getArg2().localBacktrack(global_level);
                assert (reply == ReplyMessage.SUCCEEDED);
            }
            
            localBacktrack(global_level);
            
            for (int i = myid+1; i < system_size; i++)
            {
                System.out.println("DisController. backTrack(). "
                        + "Requesting localBackTrack(" + global_level + ") to node[" + i + "]");                  
                ReplyMessage reply = all_controllers.get(i).getArg2().localBacktrack(global_level);
                assert (reply == ReplyMessage.SUCCEEDED);
            }            
        }
        catch (Exception e)
        {
            System.err.println("Controller. backtrack ERROR.");
            e.printStackTrace();            
        }
    }
    
    @Override
    public ReplyMessage localPrintAnswer() throws RemoteException {
        local_node.printAnswer();
        
        return ReplyMessage.SUCCEEDED;
    }    
    
    private void printAnswer()
    {
        try {
            for (int i = 0; i < myid; i++) {
                all_controllers.get(i).getArg2().localPrintAnswer();
            }
            
            localPrintAnswer();
            
            for (int i = myid+1; i < system_size; i++) {
                all_controllers.get(i).getArg2().localPrintAnswer();
            }
        }
        catch (Exception e)
        {
            System.err.println("DisController. printAnswer ERROR.");
            e.printStackTrace();
        }        
    }    

    @Override
    public void makeChoice(int global_level, int last_guy) throws RemoteException {
        try {
            if (local_node.hasMoreChoice())
            {
                if (myid != last_guy)
                {
                    stack.push(new Pair(global_level, last_guy));
                    System.out.println("DisController[" + myid + "]. stack.push("+ global_level + "," + last_guy + "). Because last_guy = " + last_guy + " is not me = " + myid);
                }
                
                global_level++;
                stack.push(new Pair(global_level, myid));
                System.out.println("DisController[" + myid + "]. stack.push("+ global_level + "," + myid + ").");
                
                ReplyMessage reply = local_node.propagate(global_level);
                System.out.println("DisController[" + myid + "]. makeChoice done.");
                
                if (reply == ReplyMessage.INCONSISTENT)
                {
                    Pair<Integer, Integer> p = stack.pop();
                    global_level = p.getArg1();
                    assert (myid == p.getArg2());
                    makeBranch(global_level);
                }
                else
                {
                    assert (reply == ReplyMessage.SUCCEEDED);
                    System.out.println("Ask first controller to do the next choice.");
                    all_controllers.get(0).getArg2().makeChoice(global_level, myid);
                }
            }
            else
            {
                if (myid == system_size - 1)
                {
                    System.out.println("DisController[" + myid + "]. AN ANSWER FOUND");
                    printAnswer();
                    System.out.println("DisController[" + myid + "]. Request last guy = " + last_guy + " to make a branch");
                    all_controllers.get(last_guy).getArg2().makeBranch(global_level);
                }
                else
                {
                    all_controllers.get(myid+1).getArg2().makeChoice(global_level, last_guy);
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("DisController. makeChoice ERROR.");
            e.printStackTrace();
        }
    }

    @Override
    public void makeBranch(int global_level) throws RemoteException {
        try {
            System.out.println("DisController[" + myid + "]. Backtrack the whole system to global level = " + global_level);
            backTrack(global_level-1);
            
            Pair<Integer, Integer> p = stack.pop();
            assert (p.getArg2() == myid);
            assert (p.getArg1() == global_level-1);
            
            global_level--;
            
            ReplyMessage reply = local_node.hasMoreBranch();
            switch (reply)
            {
                case HAS_BRANCH:
                    reply = local_node.propagate(global_level);
                    if (reply == ReplyMessage.SUCCEEDED)
                    {
                        stack.push(new Pair(global_level, myid));
                        all_controllers.get(0).getArg2().makeChoice(global_level, myid);
                    }
                    else
                    {
                        makeBranch(global_level);
                    }
                    break;
                case NO_MORE_BRANCH:
                    if (global_level > 1)
                    {
                        p = stack.pop();
                        assert (p.getArg1() == global_level-1);
                        int last_guy = p.getArg2();
                        all_controllers.get(last_guy).getArg2().makeBranch(global_level);
                    }
                    // otherwise global_level == 1 ~~> FINISH
                    break;
                case NO_MORE_ALTERNATIVE:
                    if (!stack.empty())
                    {
                        p = stack.pop();
                        assert (p.getArg1() == global_level-1);
                        int last_guy = p.getArg2();
                        all_controllers.get(last_guy).getArg2().makeBranch(global_level);
                    }
                    // FINISH
                    break;
            }
        }
        catch (Exception e)
        {
            System.err.println("DisController. makeChoice ERROR.");
            e.printStackTrace();
        }        
    }

    public static void main(String[] args) {
        System.out.println("DisController. Starting DisController.main()");
        
        int myid = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        String filename = args[2];
        String controller_name = "d" + myid;
        
        System.out.println("DisController. MyID = " + myid);
        System.out.println("DisController. name = " + controller_name);
        System.out.println("DisController. System size = " + size);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager() );
        }
        
        try {
            DisControllerInterface local_controller = new DisControllerImpl(myid, size, filename);
            DisControllerInterface stub =
                (DisControllerInterface) UnicastRemoteObject.exportObject(local_controller,0);  // use anonymous/no port
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(controller_name, stub);
            System.out.println("DisController[" + controller_name +"]: DisControllerImpl bound.");
        } catch (Exception e) {
            System.err.println("DisController[" + controller_name +"]: DisControllerImpl exception:");
            e.printStackTrace();
        }        
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    }
}
