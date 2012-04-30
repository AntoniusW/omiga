/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import network.NodeInterface;

/**
 *
 * @author Minh Dao-Tran
 */
public class AController {
    private Registry registry;
    
    public AController() {
        try {
            registry = LocateRegistry.getRegistry("localhost");
        }
        catch (Exception e) {
            System.err.println("Controller ctor ERROR.");
            e.printStackTrace();
        }
    }
    
    public void mainLoop() {
        try {
            Map<String,Remote> nodes = new HashMap<String,Remote>();
            
            // build map of all nodes
            System.out.println("Looking up nodes.");
            for(int i=1; i <=2; i++) {
                String name = "Context_n"+i;
                nodes.put(name, registry.lookup(name));
            }
            
            // init each node
            System.out.println("Initializing nodes now.");
            for (Entry<String,Remote> node : nodes.entrySet()) {
                Map<String,Remote> nodes_minus_current = new HashMap<String,Remote>(nodes);
                nodes_minus_current.remove(node.getKey());
                ((ANodeInterface)node.getValue()).init(nodes_minus_current);
            }
                            
            System.out.println("Start up successful.");
            
            System.out.println("Calling test exchange on nodes now.");
            for (Entry<String,Remote> node : nodes.entrySet()) {
                ReplyMessage repl=((ANodeInterface)node.getValue()).testInstanceExchange();
                
                System.out.println("Reply was: "+repl);
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
        
        AController controller = new AController();
        controller.mainLoop();
    }
}
