/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *  Store global settings here
 * @author Antonius
 */
public class GlobalSettings {
    
    static GlobalSettings instance = null;
    boolean stringbasedHashCode;
    
    public static GlobalSettings getGlobalSettings() {
       if(instance != null) 
           return instance;
       else {
           instance = new GlobalSettings();
           return instance;
       }
    }
    
    private GlobalSettings() {  
        stringbasedHashCode=true;
    }
    
    public boolean isStringbasedHashCode() {
        return stringbasedHashCode;
    }

    public void setStringbasedHashCode(boolean stringbasedHashCode) {
        this.stringbasedHashCode = stringbasedHashCode;
    }

    
}
