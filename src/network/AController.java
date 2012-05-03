/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Minh Dao-Tran, Antonius Weinzierl
 */
public class AController {
    private Registry registry;
    private int system_size;
    private ArrayList<Pair<String,ANodeInterface>> nodes =
            new ArrayList<Pair<String,ANodeInterface>>();
    
    public AController(int size) {
        try {
            system_size = size;
            registry = LocateRegistry.getRegistry("127.0.0.1");
            
            /* Get remote interface to all nodes */
            for (int i = 0; i < system_size; i++)
            {
                String name = "Context_n" + (i+1);
                System.out.println("Looking for node with name = " + name);
                ANodeInterface node = (ANodeInterface) registry.lookup(name);
                nodes.add(new Pair(name, node));
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
                System.out.println("Controller.findNodeWithChoicePoint(): checking, i = " + i);

                if (nodes.get(i).getArg2().hasMoreChoice() == true)
                {
                    System.out.println("Controller.findNodeWithChoicePoint(): return " + i);
                    return i;
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Controller.findNodeWithChoicePoint ERROR.");
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
                System.out.println("Controller.backTrack(). "
                        + "Requesting backTrack(" + global_level + ") to node[" + i + "]");                  
                ReplyMessage reply = nodes.get(i).getArg2().localBacktrack(global_level);
                assert (reply == ReplyMessage.SUCCEEDED);
            }
        }
        catch (Exception e)
        {
            System.err.println("Controller.backtrack ERROR.");
            e.printStackTrace();            
        }
    }

    
    private void printAnswer()
    {
        try
        {
            for (int i = 0; i < system_size; i++)
            {
                System.out.println("Request node[" + i + "] to print answer.");
                nodes.get(i).getArg2().printAnswer();
            }
        }
        catch (Exception e)
        {
            System.err.println("Controller.printAnswer ERROR.");
            e.printStackTrace();
        }
    }
    
    public void mainLoop() {
        try {
            
            // init each node
            System.out.println("Initializing nodes now.");
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().init(pair.getArg1(),nodes);
                
            }
                            
            System.out.println("Start up successful.");
            
            /*
            System.out.println("Calling test exchange on nodes now.");
            for (Entry<String,Remote> node : nodes.entrySet()) {
                ReplyMessage repl=((ANodeInterface)node.getValue()).testInstanceExchange();
                
                System.out.println("Reply was: "+repl);
            }*/
            
            
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
                        
                        ReplyMessage reply = nodes.get(current_node).getArg2().makeChoice(global_level);
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
                        printAnswer();
                        p = stack.pop();
                        global_level = p.getArg1();
                        current_node = p.getArg2();
                        action = Action.MAKE_BRANCH;
                    }
                }
                else if (action == Action.MAKE_BRANCH)
                {                    
                    backTrack(global_level-1);
                    ReplyMessage reply = nodes.get(current_node).getArg2().hasMoreBranch();
                    switch (reply)
                    {
                        case HAS_BRANCH:
                            reply = nodes.get(current_node).getArg2().makeBranch();
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
            System.err.println("Controller mainLoop ERROR.");
            e.printStackTrace();
        }        
    }
    
    public static void main(String[] args) {
        System.out.println("Starting Controller.main()");
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        int size = Integer.parseInt(args[0]);
        System.out.println("System size = " + size);
        
        AController controller = new AController(size);
        controller.mainLoop();
    }
}
