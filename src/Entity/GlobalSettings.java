/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Manager.Manager;

/**
 *  Store global settings (eg, version number) here.
 * Note: renaming/moving this class requires adaptions to the build script.
 * @author Antonius
 */
public class GlobalSettings {
    public static boolean debugOutput = false;
    
    public boolean debugHelper = false;
    
    public static int decisionCounter = 0;
    public static int maxDecisionLevel = 0;
    
    public static boolean noLearning = false;
    public static boolean didLearn = false;
    public static boolean initialReteBuilding = true;   // indicates whether Rete nodes are added from learning new rules.
    
    static GlobalSettings instance = null;
    
    // below string is replaced by build-script automatically,
    // do not change line breaking
    public static String version="d5b0a3ad4c83728905223462a364a7e4c8ffdb5c";
    
    private Manager manager;

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
    
    //boolean stringbasedHashCode;
    
    public static GlobalSettings getGlobalSettings() {
       if(instance != null) 
           return instance;
       else {
           instance = new GlobalSettings();
           return instance;
       }
    }
    
    private GlobalSettings() {  
        debugHelper = false;
    }
    
}
