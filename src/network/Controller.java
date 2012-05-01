/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Stack;

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
    
    private int findNodeWithChoicePoint()
    {
        try
        {
            for (int i = 0; i < system_size; i++)
            {
                System.out.println("Controller::findNodeWithChoicePhoit(): checking, i = " + i);

                if (nodes.get(i).hasMoreChoice() == true)
                {
                    System.out.println("Controller::findNodeWithChoicePhoit(): return " + i);
                    return i;
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Controller::findNodeWithChoicePoint ERROR.");
            e.printStackTrace(); 
        }
        
        return -1;
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
            System.err.println("Controller::backtrack ERROR.");
            e.printStackTrace();            
        }
    }
    
    public void mainLoop() {
        try 
        {            
            Action action = Action.MAKE_CHOICE;
            int global_level = 0;
            int current_node = 0;
            Stack<Pair<Integer, Integer> > stack = new Stack<Pair<Integer,Integer> >();
            Pair<Integer, Integer> p;
            
            while (action != Action.FINISH)
            {                
                if (action == Action.MAKE_CHOICE)
                {
                    current_node = findNodeWithChoicePoint();
                    
                    
                    if (current_node != -1)
                    {
                        global_level++;
                        stack.push(new Pair(global_level, current_node));
                        
                        ReplyMessage reply = nodes.get(current_node).makeChoice(global_level);
                        if (reply == ReplyMessage.INCONSISTENT)
                        {
                            p = stack.pop();
                            global_level = p.getArg1();
                            current_node = p.getArg2();
                            action = Action.MAKE_BRANCH;
                        }
                    }
                    else
                    {
                        System.out.println("An answer set found!");
                        p = stack.pop();
                        global_level = p.getArg1();
                        current_node = p.getArg2();
                        action = Action.MAKE_BRANCH;
                    }
                }
                else if (action == Action.MAKE_BRANCH)
                {                    
                    backTrack(global_level-1);
                    ReplyMessage reply = nodes.get(current_node).hasMoreBranch();
                    switch (reply)
                    {
                        case HAS_BRANCH:
                            reply = nodes.get(current_node).makeBranch();
                            if (reply == ReplyMessage.SUCCEEDED)
                            {
                                stack.push(new Pair(global_level, current_node));
                                action = Action.MAKE_CHOICE;
                            }
                            break;
                        case NO_MORE_BRANCH:
                            p = stack.pop();
                            global_level = p.getArg1();
                            current_node = p.getArg2();
                            break;
                        case NO_MORE_ALTERNATIVE:
                            if (stack.empty())
                                action = Action.FINISH;
                            else
                            {
                                p = stack.pop();
                                global_level = p.getArg1();
                                current_node = p.getArg2();
                            }
                            break;
                    }
                }
            }
        }        
        catch (Exception e) {
            System.err.println("Controller::mainLoop ERROR.");
            e.printStackTrace();
        }        
    }
    
    public static void main(String[] args) {
        System.out.println("Starting Controller.main()");
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        int size = Integer.parseInt(args[0]);
        System.out.println("system size = " + size);
        
        Controller controller = new Controller(size);
        controller.mainLoop();
    }
}
