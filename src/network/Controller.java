/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import network.NodeInterface;

/**
 *
 * @author Minh Dao-Tran
 */
public class Controller {
    private Registry registry;
    private int system_size;
    private ArrayList<NodeInterface> nodes = new ArrayList<NodeInterface>();
    
    public Controller(int n) {
        try {
            system_size = n;
            registry = LocateRegistry.getRegistry("127.0.0.1");
            
            /* Get remote interface to all nodes */
            for (int i = 0; i < system_size; i++)
            {
                String name = "Context_n" + i;
                System.out.println("Looking for node with name = " + name);
                NodeInterface node = (NodeInterface) registry.lookup(name);
                nodes.add(node);
            }
        }
        catch (Exception e) {
            System.err.println("Controller ctor ERROR.");
            e.printStackTrace();
        }
    }
    
    private void backTrack(int global_level)
    {
        try
        {
            for (int i = 0; i < system_size; i++)
            {
                System.out.println("Controller::backTrack(). Requesting backTrack(" + global_level + ") to node[" + i + "]");                  
                ReplyMessage reply = nodes.get(i).localBacktrack(global_level);
                assert (reply == ReplyMessage.SUCCEEDED);
            }
        }
        catch (Exception e)
        {
            System.err.println("Controller global backtrack ERROR.");
            e.printStackTrace();            
        }
    }
    
    public void mainLoop() {
        try 
        {            
            Action action = Action.MAKE_CHOICE;
            int global_level = 0;
            int current_node = 0;
            
            while (action != Action.FINISH)
            {                
                if (action == Action.MAKE_CHOICE)
                {
                    global_level++;
                    System.out.println("Controller::mainLoop(). Requesting makeChoice(" + global_level + ") to node[" + current_node + "]");
                    ReplyMessage reply = nodes.get(current_node).makeChoice(global_level);
                    System.out.println("Controller::mainLoop(). Got reply = " + reply);
                    if (reply == ReplyMessage.NO_MORE_CHOICE)
                    {
                        if (current_node < system_size - 1) current_node++;
                        else
                        {
                            /* An answer set found */
                            global_level--;
                            System.out.println("An answer set found. global_level = " + global_level);
                            action = Action.MAKE_ALTERNATIVE;
                        }
                    }
                    else if (reply == ReplyMessage.INCONSISTENT)
                    {
                        //global_level--;
                        //backTrack(global_level);
                        action = Action.MAKE_ALTERNATIVE;
                    }
                }
                else if (action == Action.MAKE_ALTERNATIVE)
                {                    
                    System.out.println("Will make alternative. global_level = " + global_level);
                    backTrack(global_level-1);
                    System.out.println("Controller::mainLoop(). Requesting makeAlternative(" + global_level + ") to node[" + current_node + "]");
                    ReplyMessage reply = nodes.get(current_node).makeAlternative();
                    switch (reply)
                    {
                        case SUCCEEDED:
                            action = Action.MAKE_CHOICE;
                            break;
                        case NO_MORE_BRANCH:
                            global_level--;
                            //backTrack(global_level);                           
                            break;
                        case NO_MORE_ALTERNATIVE:
                            if (current_node == 0) action = Action.FINISH;
                            else current_node--;
                            break;
                    }
                }
            }
        }        
        catch (Exception e) {
            System.err.println("Controller mainLoop ERROR.");
            e.printStackTrace();
        }        
    }
    
    public static void main(String[] args) {
        System.out.println("Starting Controller.main()");
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        Controller controller = new Controller(1);
        controller.mainLoop();
    }
}
