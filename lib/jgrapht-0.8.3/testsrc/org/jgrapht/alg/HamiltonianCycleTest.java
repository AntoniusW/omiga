/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
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
/* ----------------
 * HamiltonianCycleTest.java
 * ----------------
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Andrew Newell
 * Contributor(s):   -
 *
 * $Id: HamiltonianCycleTest.java 722 2010-11-24 06:22:07Z perfecthash $
 *
 * Changes
 * -------
 * 17-Feb-2008 : Initial revision (AN);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import junit.framework.*;

import org.jgrapht.generate.*;
import org.jgrapht.graph.*;


/**
 * .
 *
 * @author Andrew Newell
 */
public class HamiltonianCycleTest
    extends TestCase
{
    //~ Methods ----------------------------------------------------------------

    // ~ Methods
    // ----------------------------------------------------------------

    /**
     * .
     */
    public void testHamiltonianCycle()
    {
        SimpleWeightedGraph<Object, DefaultWeightedEdge> completeGraph =
            new SimpleWeightedGraph<Object, DefaultWeightedEdge>(
                DefaultWeightedEdge.class);
        CompleteGraphGenerator<Object, DefaultWeightedEdge> completeGraphGenerator =
            new CompleteGraphGenerator<Object, DefaultWeightedEdge>(
                6);
        completeGraphGenerator.generateGraph(
            completeGraph,
            new ClassBasedVertexFactory<Object>(Object.class),
            null);

        assertTrue(
            HamiltonianCycle.getApproximateOptimalForCompleteGraph(
                completeGraph).size() == 6);

        List<Object> vertices =
            new LinkedList<Object>(completeGraph.vertexSet());
        completeGraph.removeEdge(
            completeGraph.getEdge(vertices.get(0),
                vertices.get(1)));

        assertTrue(
            HamiltonianCycle.getApproximateOptimalForCompleteGraph(
                completeGraph) == null);
    }
}

// End HamiltonianCycleTest.java
