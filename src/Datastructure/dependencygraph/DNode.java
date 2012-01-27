/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.dependencygraph;

import Entity.Atom;
import java.util.HashMap;

/**
 *
 * @author Mika
 */
public class DNode {
    
    Atom a;
    HashMap<Atom, DNode> incoming;
    HashMap<Atom, DNode> outgoing;
    
    public DNode(Atom a){
        this.a = a;
    }
    
    
    
    @Override
    public int hashCode(){
        return a.hashCode();
    }
    
    @Override
    public boolean equals(Object o){
        DNode that = (DNode)o;
        return this.a.equals(that.a);
    }
    
}
