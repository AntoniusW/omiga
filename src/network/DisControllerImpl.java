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
    private int count_answer;
    private int count_potential_answer;
    private int answer_to_find;
    
    Stack<Pair<Integer, Integer> > stack = new Stack<Pair<Integer,Integer> >();
    
    public DisControllerImpl(int id, int size, String filename, String filter)
    {
        try {
            myid = id;
            system_size = size;
            String local_name = "n" + (myid+1);
            
            count_answer = 0;
            count_potential_answer = 0;
            
            answer_to_find = 1;
            
            //registry = LocateRegistry.getRegistry("127.0.0.1");
            //local_node = (ANodeInterface) registry.lookup(local_name);
            local_node = new ANodeImpl(local_name, filename, filter);
        }
        catch (Exception e) {
            System.err.println("Controller ctor ERROR.");
            e.printStackTrace();
        }
    }
    
    public ReplyMessage setAnswerToFind(int atf) throws RemoteException
    {
        answer_to_find = atf;
        return ReplyMessage.SUCCEEDED;
    }
    
    public int getCountAnswers() throws RemoteException 
    {
        return count_answer;
    }
    
    public int getCountPotentialAnswers() throws RemoteException 
    {
        return count_potential_answer;
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
                //System.out.println("DisController. backTrack(). " + "Requesting localBackTrack(" + global_level + ") to node[" + i + "]");                  
                ReplyMessage reply = all_controllers.get(i).getArg2().localBacktrack(global_level);
                assert (reply == ReplyMessage.SUCCEEDED);
            }
            
            localBacktrack(global_level);
            
            for (int i = myid+1; i < system_size; i++)
            {
                //System.out.println("DisController. backTrack(). " + "Requesting localBackTrack(" + global_level + ") to node[" + i + "]");                  
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
            System.out.println("******************************************************************************************");
        }
        catch (Exception e)
        {
            System.err.println("DisController. printAnswer ERROR.");
            e.printStackTrace();
        }        
    }
    
    @Override
    public ReplyMessage finalClosing(int global_level) throws RemoteException
    {
        return local_node.finalClosing(global_level);
    }
    
    private ReplyMessage doClosing(int global_level)
    {
        try
        {
            assert (myid == system_size - 1);
            
            for (int i = 0; i < system_size - 1; i++)
            {
                //System.out.println("Controller. Final closing at node[" + i + "]");
                if (all_controllers.get(i).getArg2().finalClosing(global_level) == ReplyMessage.INCONSISTENT)
                {
                    //System.out.println("Controller. Killing answer set after finalClosing.");
                    return ReplyMessage.INCONSISTENT;
                }
            }
            
            return local_node.finalClosing(global_level);
        }
        catch (Exception e)
        {
            System.err.println("DisController. finalClosing ERROR.");
            e.printStackTrace();
        }
        
        return ReplyMessage.SUCCEEDED;
    }
    
    private void print_stack()
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

    @Override
    public void makeChoice(int global_level, int last_guy) throws RemoteException {
        try {
            ReplyMessage reply = local_node.makeChoice(global_level);
            switch (reply)
            {
                case SUCCEEDED:
                    if (last_guy != myid) 
                    {
                        //System.out.println("DisController[" + myid + "]. makeChoice: stack.push("+ global_level + "," + last_guy + "). Because last_guy = " + last_guy + " is not me = " + myid);
                        stack.push(new Pair(global_level, last_guy));
                    }
                    //int glpo = global_level+1;
                    //System.out.println("DisController[" + myid + "]. makeChoice: succeeded. stack.push("+ glpo + "," + myid + "). Interpretation is");
                    stack.push(new Pair(global_level+1, myid));
                    //print_stack();
                    //printAnswer();
                    
                    //System.out.println("DisController[" + myid + "]. makeChoice: Ask first controller to do the next choice. global_level = " + glpo + ". myid = " + myid);
                    all_controllers.get(0).getArg2().makeChoice(global_level+1, myid);
                    break;
                case INCONSISTENT:
                    if (last_guy != myid) 
                    {
                        //System.out.println("DisController[" + myid + "]. makeChoice. stack.push("+ global_level + "," + last_guy + "). Because last_guy = " + last_guy + " is not me = " + myid);
                        stack.push(new Pair(global_level, last_guy));
                    }
                    //glpo = global_level+1;
                    //System.out.println("DisController[" + myid + "]. makeChoice: stack.push("+ glpo + "," + myid + ").");
                    stack.push(new Pair(global_level+1, myid));
                    //print_stack();
                    
                    //System.out.println("DisController[" + myid + "]. makeChoice got INCONSISTENT from propagation. Now call makeBranch.");
                    makeBranch();
                    break;
                case NO_MORE_CHOICE:
                    if (myid == system_size - 1)
                    {
                        count_potential_answer++;
                        //System.out.println("DisController[" + myid + "]. A POTENTIAL ANSWER FOUND No.[" + count_potential_answer + "] Now do final closing.");                    
                        
                        global_level++;
                        if (doClosing(global_level) == ReplyMessage.SUCCEEDED)
                        {
                            //System.out.println("DisController[" + myid + "]. AN ANSWER FOUND");
                            count_answer++;
                            printAnswer();
                            
                            if (answer_to_find != 0 && count_answer == answer_to_find)
                            {
                                printSolvingTime();
                                break;
                            }
                        }
                        //else
                        //{
                            //System.out.println("DisController[" + myid + "]. NOT AN ANSWER!!!");
                         //   printAnswer();
                        //}
                        
                        backTrack(global_level);
                        //System.out.println("DisController[" + myid + "]. Backtrack after final closing. Interpretation is");
                        //printAnswer();
                        global_level--;
                        
                        //System.out.println("DisController[" + myid + "]. Request last guy = " + last_guy + " to make a branch");
                        all_controllers.get(last_guy).getArg2().makeBranch();                        
                    }
                    else
                    {
                        //System.out.println("DisController[" + myid + "]. Request next node to make choice. passing on (global_level,last_guy = (" + global_level +"," + last_guy + ")");
                        all_controllers.get(myid+1).getArg2().makeChoice(global_level, last_guy);
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            System.err.println("DisController. makeChoice ERROR.");
            e.printStackTrace();
        }
    }

    @Override
    public void makeBranch() throws RemoteException {
        try {
            Pair<Integer, Integer> p = stack.pop();
            int global_level = p.getArg1();
            
            //System.out.println("DisController[" + myid + "]. makeBranch: pop out (" + p.getArg1() + "," + p.getArg2() + ")");
            //print_stack();
            //int glmo = global_level-1;
            //System.out.println("DisController[" + myid + "]. makeBranch: Backtrack the whole system to global level = " + glmo);
            
            if (global_level == 0)
            {
                System.out.println("DisController[" + myid + "]. Ask for branch at global_level = 0. Get out now");
                return;
            }
            
            backTrack(global_level-1);
            
            //System.out.println("DisController[" + myid + "]. After backtracking to global_level = " + glmo + ". Interpretation is");
            //printAnswer();
            
            ReplyMessage reply = local_node.makeBranch(global_level-1);
            
            switch (reply)
            {
                case SUCCEEDED:
                    //System.out.println("DisController[" + myid + "]. makeBranch: succeeded. stack.push("+ global_level + "," + myid + "). Interpretation is");
                    stack.push(new Pair(global_level, myid));
                    //print_stack();
                    //printAnswer();                    
                    
                    //System.out.println("DisController[" + myid + "]. makeBranch: Ask first controller to do the next choice. global_level = " + global_level + ". myid = " + myid);
                    all_controllers.get(0).getArg2().makeChoice(global_level, myid);
                    break;
                case INCONSISTENT:
                    //System.out.println("DisController[" + myid + "]. makeChoice: stack.push("+ global_level + "," + myid + ").");
                    stack.push(new Pair(global_level, myid));
                    //print_stack();
                    //System.out.println("DisController[" + myid + "]. makeChoice got INCONSISTENT from propagation. Now call makeBranch.");
                    makeBranch();
                    break;
                case NO_MORE_BRANCH:
                    if (global_level > 0)
                    {
                        p = stack.peek();                        
                        int last_guy = p.getArg2();
                        
                        if (last_guy != myid)
                        {
                            stack.pop();
                        }
                        
                        //System.out.println("DisController[" + myid +"]:. makBranch: NO MORE BRANCH. peek into stack, (global_level, last_guy) = ("+ p.getArg1() + "," + p.getArg2() + "). ");                        
                        //print_stack();
                        
                        if (last_guy == -1)
                        {
                            System.out.println("DisController[" + myid +"]: FINISHED. number of potential answers = " + all_controllers.get(system_size-1).getArg2().getCountPotentialAnswers());
                            System.out.println("DisController[" + myid +"]: FINISHED. number of answers = " + all_controllers.get(system_size-1).getArg2().getCountAnswers());                        
                            printSolvingTime();
                        }
                        else
                        {
                            //System.out.println("DisController[" + myid +"]:. makBranch: Ask last_guy = " + last_guy + " to make branch");
                            all_controllers.get(last_guy).getArg2().makeBranch();
                        }
                    }
                    else
                    {
                        System.out.println("DisController[" + myid +"]: FINISHED. number of potential answers = " + all_controllers.get(system_size-1).getArg2().getCountPotentialAnswers());
                        System.out.println("DisController[" + myid +"]: FINISHED. number of answers = " + all_controllers.get(system_size-1).getArg2().getCountAnswers());
                        printSolvingTime();
                    }
                    break;
            }
        }

        catch (Exception e)
        {
            System.err.println("DisController. makeBranch ERROR.");
            e.printStackTrace();
        }        
    }
    
    @Override
    public long getSolvingTime() throws RemoteException {
        return local_node.getSolvingTime();
    }

    private void printSolvingTime()
    {
        try
        {
            for (int i = 0; i < system_size-1; i++) {
                System.out.println("INFO: DisController[" + i + "]: Solving time = " + all_controllers.get(i).getArg2().getSolvingTime());
            }
            
            int ssmo = system_size - 1;
            System.out.println("INFO: DisController[" + ssmo + "]: Solving time = " + local_node.getSolvingTime());            
        }
        catch (Exception e)
        {
            System.err.println("DisController. printSolvingTime ERROR.");
            e.printStackTrace();
        }           
    }
    
    public static void main(String[] args) {
        //System.out.println("DisController. Starting DisController.main()");
        
        int myid = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        String filename = args[2];
        
        String filter;
        if (args.length == 4)
        {
            filter = args[3];
        }
        else
        {
            filter = "";
        }
        
        String controller_name = "d" + myid;
        
        //System.out.println("DisController. MyID = " + myid);
        //System.out.println("DisController. name = " + controller_name);
        //System.out.println("DisController. System size = " + size);
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager() );
        }
        
        try {
            DisControllerInterface local_controller = new DisControllerImpl(myid, size, filename, filter);
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
