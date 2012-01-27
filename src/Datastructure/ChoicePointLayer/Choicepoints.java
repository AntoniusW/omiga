/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.ChoicePointLayer;

import Datastructure.storage.HashInstances;
import Entity.Predicate;
import Interfaces.Term;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Mika
 */
public class Choicepoints {
    
    int actualChoiceLayer = 0;
    
    // We do not need to use hashInstances here because we never want to look up if
    // an instance is in the list of a certain choicepoint.
    // furthermore we garantee no instance is added twice
    ArrayList<HashMap<Predicate, ArrayList<Term[]>>> choices;
    
    public Choicepoints(){
        choices = new ArrayList<HashMap<Predicate, ArrayList<Term[]>>>();
    }
    
    public void nextChoice(){
        this.actualChoiceLayer++;
        choices.add(new HashMap<Predicate, ArrayList<Term[]>>());
    }
    
    public void revertToChoiceLevel(int i){
        this.actualChoiceLayer = i;
        // TODO: Removal of instances from rete
        choices = (ArrayList)choices.subList(0, i);
    }
    
    public void addInstance(Predicate p, Term[] instance){
        if(!choices.get(this.actualChoiceLayer).containsKey(p)){
            choices.get(this.actualChoiceLayer).put(p, new ArrayList<Term[]>());
        }
        choices.get(this.actualChoiceLayer).get(p).add(instance);
    }
    
    
}
