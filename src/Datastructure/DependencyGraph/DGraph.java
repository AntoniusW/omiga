/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Datastructure.DependencyGraph;

import Entity.Atom;
import Entity.Predicate;
import Entity.Rule;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.StrongConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedSubgraph;

/**
 *
 * @author User
 */
public class DGraph {
    

    public DirectedGraph<Predicate, DefaultEdge> gd = new DefaultDirectedGraph<Predicate, DefaultEdge>(DefaultEdge.class);
    StrongConnectivityInspector sci = new StrongConnectivityInspector(gd);
    
    
    
    public void addRule(Rule r){
        if(r.getHead() == null) return; // this rule is a constraint dont add it to the Graph
        System.err.println("GD= " + gd);
        gd.addVertex(r.getHead().getPredicate());
        for(Atom a: r.getBodyPlus()){
            gd.addVertex(a.getPredicate());
            System.out.println("Adding Edge: " + a.getPredicate() + " --> " + r.getHead().getPredicate());
            gd.addEdge(a.getPredicate(), r.getHead().getPredicate());
            //gd.addEdge(r.getHead().getPredicate(),a.getPredicate());
        }
        for(Atom a: r.getBodyMinus()){
            gd.addVertex(a.getPredicate());
            System.out.println("Adding Edge: " + a.getPredicate() + " -N-> " + r.getHead().getPredicate());
            gd.addEdge(a.getPredicate(), r.getHead().getPredicate());
            //gd.addEdge(r.getHead().getPredicate(),a.getPredicate());
        }
    }
    
    public List<DirectedSubgraph> getSCCs(){
        //It seems this already returns an ordered set of SCCs!
        return sci.stronglyConnectedSubgraphs();
    }
    
    /**
     * 
     * @return A List of Lists of Predicates, where the first list-indix stands for the corresponding SCC lvl,
     * and the second one for the different predicate nodes within that SCC.
     */
    public ArrayList<ArrayList<Predicate>> getSortedSCCs(){
        //TODO
        ArrayList<ArrayList<Predicate>> ret = new ArrayList<ArrayList<Predicate>>();
        List<DirectedSubgraph> temp = sci.stronglyConnectedSubgraphs();
        
        for(int i = 0; i < temp.size();i++){
            ret.add(new ArrayList<Predicate>());
            for(Object o: temp.get(i).vertexSet()){
                Predicate p = (Predicate)o;
                ret.get(i).add(p);
            }
        }
        
        return ret;
    }
    
    public void test(Predicate p){
        gd.outgoingEdgesOf(p);
        gd.incomingEdgesOf(p);
    }
    
}
