/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Manager.Manager;

/**
 *  Store global settings here
 * @author Antonius
 */
public class GlobalSettings {
    
    static GlobalSettings instance = null;
    
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
