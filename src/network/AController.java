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
    
    private int potential_count;
    
    private Registry registry;
    private int system_size;
    private int answer_count;
    private ArrayList<Pair<String,ANodeInterface>> nodes =
            new ArrayList<Pair<String,ANodeInterface>>();
    
    private long startup_time;
    
    public AController(int size) {
        try {
            potential_count = 0;
            system_size = size;
            answer_count = 0;            
            
            long start_registry = System.currentTimeMillis();
            registry = LocateRegistry.getRegistry("127.0.0.1");
            
            /* Get remote interface to all nodes */
            for (int i = 0; i < system_size; i++)
            {
                String name = "n" + (i+1);
                System.out.println("Controller. Looking for node with name = " + name);
                ANodeInterface node = (ANodeInterface) registry.lookup(name);
                nodes.add(new Pair(name, node));
            }
            
            // collect registering time 
            startup_time = System.currentTimeMillis() - start_registry;
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
                //System.out.println("Controller. backTrack(). Requesting backTrack(" + global_level + ") to node[" + i + "]");                  
                ReplyMessage reply = nodes.get(i).getArg2().localBacktrack(global_level);
                assert (reply == ReplyMessage.SUCCEEDED);
            }
        }
        catch (Exception e)
        {
            System.err.println("Controller. backtrack ERROR.");
            e.printStackTrace();            
        }
    }

    
    private void printAnswer()
    {
        try
        {
            System.out.println("***************************************************************************************");
            for (int i = 0; i < system_size; i++)
            {
                System.out.println("Controller. Request node[" + i + "] to print interpretation.");
                nodes.get(i).getArg2().printAnswer();
            }
            System.out.println("***************************************************************************************");            
        }
        catch (Exception e)
        {
            System.err.println("Controller. printAnswer ERROR.");
            e.printStackTrace();
        }
    }
    
    private void print_stack(Stack<Pair<Integer, Integer> > stack)
    {
        System.out.print("stack = ");
        Stack<Pair<Integer, Integer> > s2 = (Stack<Pair<Integer, Integer> >)stack.clone();
        while (!s2.empty())
        {
            Pair<Integer, Integer> p = s2.pop();
            System.out.print("(" + p.getArg1() + "," + p.getArg2() + ")");
        }
        System.out.println("");
    }
    
    public void mainLoop() {
        try {
            
            long start_init_nodes = System.currentTimeMillis();
            // init each node
            System.out.println("Controller. Initializing nodes now.");
            
            // startup all nodes
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().init(nodes);
            }
            
            // let all nodes exchange domains
            int serialize_lower = 0;
            for (Pair<String, ANodeInterface> pair : nodes) {
                serialize_lower = pair.getArg2().exchange_active_domain(serialize_lower+1);
            }
            
            // let all nodes exchange import domains
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().exchange_import_domain();
            }
                            
            
            
            // let all nodes propagate
            for (Pair<String, ANodeInterface> pair : nodes) {
                //System.out.println("Controller. Ask node " + pair.getArg1() + " to do first propagation!");
                pair.getArg2().firstPropagate();   
            }
            
            for (Pair<String, ANodeInterface> pair : nodes) {
                //System.out.println("Controller. Ask node " + pair.getArg1() + " to do first propagation!");
                pair.getArg2().initGlobalLevelZero(); 
            }
            
            /*
            System.out.println("Calling test exchange on nodes now.");
            for (Entry<String,Remote> node : nodes.entrySet()) {
                ReplyMessage repl=((ANodeInterface)node.getValue()).testInstanceExchange();
                
                System.out.println("Reply was: "+repl);
            }*/
            
            //System.out.println("Controller. Interpretation after first round of all propagation:");
            //printAnswer();
            
            System.out.println("Controller. Start up successful.");
            long start_computation = System.currentTimeMillis();
            startup_time = startup_time + start_computation - start_init_nodes;
            
            Action action = Action.MAKE_CHOICE;
            int global_level = 0;
            int current_node = 0;
            Stack<Pair<Integer, Integer> > stack = new Stack<Pair<Integer,Integer> >();
            Pair<Integer, Integer> p;
            
            stack.push(new Pair(0, -1));
            
            ReplyMessage reply = ReplyMessage.INCONSISTENT;
                    
            while (action != Action.FINISH)
            {                
                if (action == Action.MAKE_CHOICE)
                {
                    //System.out.println("Controller. action = makeChoice");
                    current_node = -1;
                    for (int i = 0; i < system_size; i++)
                    {
                        reply = nodes.get(i).getArg2().makeChoice(global_level);
                        if (reply != ReplyMessage.NO_MORE_CHOICE)
                        {
                            current_node = i;
                            break;
                        }
                    }
                        
                    if (current_node != -1)
                    {                        
                        global_level++;
                        stack.push(new Pair(global_level, current_node));
                        
                        //System.out.println("Controller. A choice was made at node = " + current_node + ". stack.push(" + global_level + "," + current_node + ")");
                        
                        if (reply == ReplyMessage.INCONSISTENT)
                        {
                            //System.out.println("Controller. choice returned INCONSISTENT. will next make branch");
                            action = Action.MAKE_BRANCH;
                        }
                        //else
                        //{
                        //   System.out.println("Controller. choice returned SUCCEEDED. will continue to make choice. Interpretation is");
                        //   printAnswer();
                        //}
                    }
                    else
                    {
                        //System.out.println("Controller. A potential answer set  No.["+potential_count+"] found. Now do final closing. global_level = " + global_level);
                        potential_count++;
                        //printAnswer();
                        boolean is_an_answer = true;
                        global_level++;
                        
                        for (int i = 0; i < system_size; i++)
                        {
                            //System.out.println("Controller. Final closing at node[" + i + "]");
                            if (nodes.get(i).getArg2().finalClosing(global_level) == ReplyMessage.INCONSISTENT)
                            {
                                //System.out.println("Controller. Killing answer set after finalClosing.");
                                is_an_answer = false;
                                break;
                            }
                        }
                        //System.out.println("Controller. Undo closing");
                        backTrack(global_level);
                        global_level--;
                        
                        if (is_an_answer)
                        {
                            //System.out.println("Controller. An answer set found!");
                            answer_count++;
                            printAnswer();
                        }
                        action = Action.MAKE_BRANCH;
                    }
       
                }
                else if (action == Action.MAKE_BRANCH)
                {   
                    p = stack.pop();
                    //System.out.println("Controller. makeBranch: pop out (" + p.getArg1() + "," + p.getArg2() + ")");
            
                    global_level = p.getArg1();
                    current_node = p.getArg2();
                    
                    if (global_level == 0)
                    {
                        //System.out.println("Controller. Ask for branch at global_level = 0. Get out now");
                        return;
                    }                    
                    
                    //int glmo = global_level-1;
                    //System.out.println("Controller. makeBranch: Backtrack the whole system to global level = " + glmo);
                    backTrack(global_level-1);
                    //printAnswer();
                    
                    reply = nodes.get(current_node).getArg2().makeBranch(global_level-1);
                    
                    switch (reply)
                    {
                        case SUCCEEDED:
                            stack.push(p);
                            //System.out.println("Controller. makeBranch succeeded. stack.push(" + global_level + "," + current_node + ")");
                            //System.out.println("will next make choice. Interpretation is:");
                            //printAnswer();
                            action = action.MAKE_CHOICE;
                            break;
                        case INCONSISTENT:
                            //System.out.println("Controller. makeBranch went inconsistent. stack.push(" + global_level + "," + current_node + ")");
                            stack.push(p);
                            break;
                        case NO_MORE_BRANCH:
                            if (global_level > 0)
                            {
                                p = stack.peek();
                                global_level = p.getArg1();
                                int last_guy = p.getArg2();
                                
                                //System.out.println("Controller. makeBranch. no more branch. peek(" + global_level + "," + current_node + ")");
                                
                                if (last_guy == -1)
                                {
                                    action = Action.FINISH;                                    
                                }
                                else
                                {
                                    action = Action.MAKE_BRANCH;
                                }
                            }
                            else
                            {
                                action = Action.FINISH;
                            }
                            break;
                    }
                }
            }
            System.out.println("Controller. FINISHED: number of potential answers = " + potential_count);
            System.out.println("Controller. FINISHED: number of answers = " + answer_count);
        }
        catch (Exception e) {
            System.err.println("Controller. Controller mainLoop ERROR.");
            e.printStackTrace();
        }        
    }
    
    public static void main(String[] args) {
        System.out.println("Controller. Starting Controller.main()");
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        int size = Integer.parseInt(args[0]);
        System.out.println("Controller. System size = " + size);
        
        AController controller = new AController(size);
        controller.mainLoop();
    }
}
