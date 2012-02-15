/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package woc.classinstances;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * This class is only for test purpose. You can create facts and then copy it from the command line to your context.
 * (Such that you do not need to write thausends of facts by hand)
 */
public class ContextCreationHelper {
    
    public static void main(String arg[]){
        
       
        int nbb = 3000;
        for(int i = 0; i < nbb; i++){
            System.out.println("p(" + i + ").");
        }
        for(int i = 0; i < nbb; i++){
            System.out.println("q(" + i + ").");
        }
        
        
        
        
    }
    
}
