/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
            String name = "Context1";
            ANodeInterface node = (ANodeInterface) registry.lookup(name);
        
            ReplyMessage reply = node.makeChoice(0);
            
            assert (reply == ReplyMessage.SUCCEEDED);
            
            System.out.println("Got SUCCEEDED");
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
