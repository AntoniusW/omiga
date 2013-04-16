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
    
    public boolean debugHelper = false;
    
    public static int decisionCounter = 0;
    public static int maxDecisionLevel = 0;
    
    public static boolean didLearn = false;
    public static boolean initialReteBuilding = true;   // indicates whether Rete nodes are added from learning new rules.
    
    static GlobalSettings instance = null;
    
    // below string is replaced by build-script automatically,
    // do not change line breaking
    public static String version="97ccdc4d15899518be057cf0b0c81dc5c98c93dc";
    
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
