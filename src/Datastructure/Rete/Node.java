/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Interfaces.PredAtom;

/**
 *
 * @author User
 */
public interface Node {
    
    
    public void addInstance(PredAtom[] instance);
    public void removeInstance(PredAtom[] instance);
    public boolean containsInstance(PredAtom[] instance);
    
    
}
