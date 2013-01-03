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
    
    static GlobalSettings instance = null;
    
    // below string is replaced by build-script automatically,
    // do not change line breaking
    public static String version="7cfa7055a8396136cd57203a2a785530b2b33d6e";
    
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
