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
    
    public static int decisionCounter = 0;
    
    public static boolean didLearn = false;
    
    static GlobalSettings instance = null;
    
    // below string is replaced by build-script automatically,
    // do not change line breaking
    public static String version="6fb255ede28db511d4fec48b5aa5d8d81ec5dedc";
    
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
        //stringbasedHashCode=true;
    }

    /*
    public boolean isStringbasedHashCode() {
        return stringbasedHashCode;
    }

    public void setStringbasedHashCode(boolean stringbasedHashCode) {
        this.stringbasedHashCode = stringbasedHashCode;
    }
     */

    
}
