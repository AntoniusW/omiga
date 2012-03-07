/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2009, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -------------------------
 * FloydWarshallShortestPathsTest.java
 * -------------------------
 * (C) Copyright 2009-2009, by Tom Larkworthy and Contributors
 *
 * Original Author:  Tom Larkworthy
 * Contributors:  Andrea Pagani
 *
 * $Id: FloydWarshallShortestPathsTest.java 715 2010-06-13 01:25:00Z perfecthash $
 *
 * Changes
 * -------
 * 29-Jun-2009 : Initial revision (TL);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import junit.framework.*;

import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;


/**
 * @author Tom Larkworthy
 * @version $Id: FloydWarshallShortestPathsTest.java 715 2010-06-13 01:25:00Z perfecthash $
 */
public class FloydWarshallShortestPathsTest
    extends TestCase
{
    //~ Methods ----------------------------------------------------------------

    public void testCompareWithDijkstra()
    {
        RandomGraphGenerator<Integer, DefaultWeightedEdge> gen =
            new RandomGraphGenerator<Integer, DefaultWeightedEdge>(
                10,
                15);
        VertexFactory<Integer> f =
            new VertexFactory<Integer>() {
                int gid;

                public Integer createVertex()
                {
                    return gid++;
                }
            };

        for (int i = 0; i < 10; i++) {
            SimpleDirectedGraph<Integer, DefaultWeightedEdge> directed =
                new SimpleDirectedGraph<Integer, DefaultWeightedEdge>(
                    DefaultWeightedEdge.class);

            gen.generateGraph(directed, f, new HashMap<String, Integer>());

            // setup our shortest path measurer
            FloydWarshallShortestPaths<Integer, DefaultWeightedEdge> fw =
                new FloydWarshallShortestPaths<Integer, DefaultWeightedEdge>(
                    directed);

            for (Integer v1 : directed.vertexSet()) {
                for (Integer v2 : directed.vertexSet()) {
                    double fwSp = fw.shortestDistance(v1, v2);
                    double dijSp =
                        new DijkstraShortestPath<Integer, DefaultWeightedEdge>(
                            directed,
                            v1,
                            v2).getPathLength();
                    assertTrue(
                        (Math.abs(dijSp - fwSp) < .01)
                        || (Double.isInfinite(fwSp)
                            && Double.isInfinite(dijSp)));
                }
            }

            SimpleGraph<Integer, DefaultWeightedEdge> undirected =
                new SimpleGraph<Integer, DefaultWeightedEdge>(
                    DefaultWeightedEdge.class);

            gen.generateGraph(undirected, f, new HashMap<String, Integer>());

            // setup our shortest path measurer
            fw = new FloydWarshallShortestPaths<Integer, DefaultWeightedEdge>(
                undirected);

            for (Integer v1 : undirected.vertexSet()) {
                for (Integer v2 : undirected.vertexSet()) {
                    double fwSp = fw.shortestDistance(v1, v2);
                    double dijSp =
                        new DijkstraShortestPath<Integer, DefaultWeightedEdge>(
                            undirected,
                            v1,
                            v2).getPathLength();
                    assertTrue(
                        (Math.abs(dijSp - fwSp) < .01)
                        || (Double.isInfinite(fwSp)
                            && Double.isInfinite(dijSp)));
                }
            }
        }
    }

    private static UndirectedGraph<String, DefaultEdge> createStringGraph()
    {
        UndirectedGraph<String, DefaultEdge> g =
            new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v1);
        g.addEdge(v3, v4);

        return g;
    }

    public void testDiameter()
    {
        UndirectedGraph<String, DefaultEdge> stringGraph = createStringGraph();
        FloydWarshallShortestPaths<String, DefaultEdge> testFWPath =
            new FloydWarshallShortestPaths<String, DefaultEdge>(stringGraph);
        double diameter = testFWPath.getDiameter();
        assertEquals(2.0, diameter);
    }
}

// End FloydWarshallShortestPathsTest.java
