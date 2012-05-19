/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 *
 * @author Minh Dao-Tran
 */
public class Client {
    private Registry registry;
    private int system_size;
    private ArrayList<Pair<String,DisControllerInterface>> all_controllers =
            new ArrayList<Pair<String,DisControllerInterface>>();
    
    private ArrayList<Pair<String,ANodeInterface>> nodes =
            new ArrayList<Pair<String,ANodeInterface>>();    
    
    public Client(int size)
    {
        try {
            system_size = size;
            registry = LocateRegistry.getRegistry("127.0.0.1");
        
            System.out.println("Client. Get remote interface to all DISTRIBUTED CONTROLLERS");
            for (int i = 0; i < system_size; i++)
            {
                String name = "d" + i;
                System.out.println("Client. Looking for discontroller with name = " + name);
                DisControllerInterface controller = (DisControllerInterface) registry.lookup(name);
                all_controllers.add(new Pair(name, controller));
            }
            
            System.out.println("Client. Get remote interface to all NODES");
            for (int i = 0; i < system_size; i++)
            {
                String name = "n" + (i+1);
                System.out.println("Client. Looking for node with name = " + name);
                ANodeInterface node = (ANodeInterface) registry.lookup(name);
                nodes.add(new Pair(name, node));
            }
            
            System.out.println("Client. Initialize all NODES");
            // startup all nodes
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().init(nodes);
            }
            
            int serialize_lower = 0;
            for (Pair<String, ANodeInterface> pair : nodes) {
                serialize_lower = pair.getArg2().exchange_active_domain(serialize_lower+1);                
            } 
            
                        // let all nodes exchange import domains
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().exchange_import_domain();
            }
            
            System.out.println("Client. Start first propagate");
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().firstPropagate();
            }
            
            System.out.println("Client. Reset global_level zero");
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().initGlobalLevelZero();
            }
            
            System.out.println("Client. Interpretation after first round of all propagation");
            for (Pair<String, ANodeInterface> pair : nodes) {
                pair.getArg2().printAnswer();
            }
            System.out.println("*************************************************************");
        }
        catch (Exception e) {
            System.err.println("Client ctor ERROR.");
            e.printStackTrace();
        }        
    }
    
    public void start()
    {
        try {
            for (int i = 0; i < system_size; i++)
            {
                all_controllers.get(i).getArg2().init(all_controllers);
            }
           
            
            System.out.println("Client. Issue the request to the first node");
            all_controllers.get(0).getArg2().makeChoice(0, 0);
        }
        catch (Exception e) {
            System.err.println("Client start() ERROR.");
            e.printStackTrace();
        }  
    }
    
    public static void main(String[] args) {
        System.out.println("Client. Starting Client.main()");
        
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        int size = Integer.parseInt(args[0]);
        System.out.println("Client. System size = " + size);
        
        Client client = new Client(size);
        client.start();
    }
}
