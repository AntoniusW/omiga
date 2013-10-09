package Datastructure.Rete;

import Exceptions.ImmediateBacktrackingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import Datastructure.storage.Storage;

/**
 * Helper class for running the propagation in Rete after a new rule was
 * learned.
 *
 * @author Antonius Weinzierl
 */
public class ReteModificationHelper {
    // stores which child nodes were added to which node

    private HashMap<Node, ArrayList<Node>> newChildren;
    private boolean isInitPhase;    // true while initial Rete building
    private static ReteModificationHelper singleton = null;
    
    private ReteModificationHelper() {
        isInitPhase = true;
        newChildren = new HashMap<Node, ArrayList<Node>>();
    }
    
    public static ReteModificationHelper getReteModificationHelper() {
        if (singleton == null) {
            singleton = new ReteModificationHelper();
        }
        return singleton;
    }

    /**
     * Signals that the initial Rete building is finished.
     */
    public void setInitPhaseFinished() {
        isInitPhase = false;
    }

    /**
     * Triggers a propagation round in Rete after new children have been added.
     * Nodes and children must have been registered before by a call to
     * recordNewChild.
     */
    public void triggerPropagationAfterModification() {
        try {
        if (!isInitPhase) {
            for (Map.Entry<Node, ArrayList<Node>> entryNodeChildren : newChildren.entrySet()) {
                entryNodeChildren.getKey().propagateToChildren(entryNodeChildren.getValue());
            }
        }
        newChildren = new HashMap<Node, ArrayList<Node>>();
        } catch (ImmediateBacktrackingException e) {
            throw e;
        }
    }

    /**
     * Records that a node got new children.
     *
     * @param n
     * @param child
     */
    public void recordNewChild(Node n, Node child) {
        if (isInitPhase) {
            return;
        }
        ArrayList<Node> children;
        if (newChildren.containsKey(n)) {
            children = newChildren.get(n);
        } else {
            children = new ArrayList<Node>();
            newChildren.put(n, children);
        }
        children.add(child);
    }
}
