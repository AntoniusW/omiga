/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Interfaces.Term;
import java.util.HashMap;

/**
 *
 * @author g.weidinger
 * ClassName: Instance
 * Purpose: Just a static class that stores instances and provides methodes to look them up and convert them to String
 */
public class Instance {
        /*private static HashMap<String,int[]> instances = new HashMap<String,int[]>();
    
    public static void addInstance(int[] instance){
        instances.put(getInstanceID(instance), instance);
    }
    
    public static String getInstanceID(int[] instance){
        String s = "";
        for(int i: instance){
            s = s + i +",";
        }
        return s;
    }*/
    
    public static int hash(Term[] instance){
        return getInstanceAsString(instance).hashCode();
    }
    
    public static String getInstanceAsString(Term[] instance){
        String s = "[";
        for(int i = 0; i < instance.length;i++){
            s = s + instance[i] + ",";
        }
        return s.substring(0,s.length()-1) + "]";
        //return "a";
    }
    
    /*public static String getInstanceAsString2(PredAtom[] instance){
        String s = "[";
        for(int i = 0; i < instance.length;i++){
            s = s + instance[i].toString() + ",";
        }
        return s.substring(0,s.length()-1) + "]";
        //return "a";
    }*/

}

