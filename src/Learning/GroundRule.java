package Learning;

import Datastructure.Rete.HeadNode;
import Entity.Atom;
import Entity.Instance;
import Entity.Operator;
import Entity.Rule;
import java.util.ArrayList;

/**
 * Helper Class to represent ground rules used for building the implication graph.
 * 
 * @author Antonius Weinzierl
 */
public class GroundRule {
    
    protected ArrayList<Atom> bodyPlus;
    protected ArrayList<Atom> bodyMinus;
    protected ArrayList<Operator> operators;
    
    private GroundRule() {
        bodyPlus = new ArrayList<Atom>();
        bodyMinus = new ArrayList<Atom>();
        operators = new ArrayList<Operator>();
    }
    
    /**
     * Creates a ground rule out of a non-ground rule and variable assignment.
     * @param r
     * @param varAssignment
     * @return 
     */
    public static GroundRule ground(Rule r, Instance var_assignment, HeadNode node) {
        GroundRule gr = new GroundRule();
        
        for(Atom at : r.getBodyPlus()) {
            
        }
        
        for(Atom at : r.getBodyPlus()) {
            
        }
        
        for(Operator op : r.getOperators()) {
            
        }
        
        return gr;
    }
    
    
}
