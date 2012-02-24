/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.Rete;

import Datastructure.storage.Storage;
import Entity.Instance;
import Entity.Variable;
import Interfaces.Term;

/**
 *
 * @author g.weidinger
 */
public class HeadNodeConstraint extends Node{
    
    //TODO: This nodes does not work. Somehow nothing is added hear!
    
    public HeadNodeConstraint(Rete rete, int arity){
        super(rete);
        this.memory = new Storage(arity);
    }
    
    
    public void saveConstraintInstance(Instance instance){
        // we call super because super only handles the isnatnce storage in the choice layer.
        // so we can use it althaugh this is not the addFunction in common sense
        super.addInstance(instance, this);
        System.out.println("saveConstraintInstance: " + instance + " - " + this);
        this.memory.addInstance(instance);
    }
    
    /*public void removeInstance(Instance inz){
        System.out.println("TRying to remove: " + inz + " from memory of size: " + this.memory.arity);
        this.memory.removeInstance(inz);
    }/*/
    
    public void addInstance(Instance instance){
        Term[] selectionCriteria = new Term[instance.getSize()];
            for(int j = 0; j < instance.getSize();j++){
                selectionCriteria[j] = Variable.getVariable("X");
            }
            System.out.println("Instanzen der HeadConstraint Node!");
            for(Instance inz: this.memory.select(selectionCriteria)){
                System.out.println("Instance: " + inz);
            }
            
            
        if(this.memory.containsInstance(instance)){
            rete.satisfiable = false;
            System.out.println("UNSATISFIABLE because of HeadConstraintNode! Printing AnswerSet");
            rete.printAnswerSet();
        }
    }
    
    
}
