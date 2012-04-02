/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;

/**
 *
 * @author Gerald Weidinger 0526105
 * 
 * A negative rule is used for optimising the solver via rewriting. Negative rules are able to add facts into the OUT memory
 * via their head. Please be aware that at the moment we only use bodyOUT and bodyPlus within negative rules, since we have not
 * yet found a rewriting that needsthe other stuff, and definitly will pay off. If you want a better rewriting you have to 
 * support that within the retebuilder for rewriting, such that it also looks into Operator and bodyMinus part of these rules.
 * 
 */
public class NegativeRule extends Rule{
    
    protected ArrayList<Atom> bodyOUT;
    
    public NegativeRule(){
        super();
        this.bodyOUT = new ArrayList<Atom>();
    }
    
    public void addAtomOUT(Atom a){
        this.bodyOUT.add(a);
    }
    
    public ArrayList<Atom> getBodyOUT(){
        return this.bodyOUT;
    }
    
}
