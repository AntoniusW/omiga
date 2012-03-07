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
/* -------------------------
 * KSPExampleTest.java
 * -------------------------
 * (C) Copyright 2007-2008, by France Telecom
 *
 * Original Author:  Guillaume Boulmier and Contributors.
 *
 * $Id: KSPExampleTest.java 645 2008-09-30 19:44:48Z perfecthash $
 *
 * Changes
 * -------
 * 23-Sep-2007 : Initial revision (GB);
 *
 */
package org.jgrapht.alg;

import junit.framework.*;

import org.jgrapht.graph.*;


@SuppressWarnings("unchecked")
public class KSPExampleTest
    extends TestCase
{
    //~ Methods ----------------------------------------------------------------

    public void testFourReturnedPathsJGraphT()
    {
        SimpleWeightedGraph graph = new KSPExampleGraph();

        Object sourceVertex = "S";
        KShortestPaths ksp = new KShortestPaths(graph, sourceVertex, 4);

        Object targetVertex = "T";
        assertEquals(3, ksp.getPaths(targetVertex).size());
    }

    public void testThreeReturnedPathsJGraphT()
    {
        SimpleWeightedGraph graph = new KSPExampleGraph();

        Object sourceVertex = "S";
        int nbPaths = 3;
        KShortestPaths ksp = new KShortestPaths(graph, sourceVertex, nbPaths);

        Object targetVertex = "T";
        assertEquals(nbPaths, ksp.getPaths(targetVertex).size());
    }

    public void testTwoReturnedPathsJGraphT()
    {
        SimpleWeightedGraph graph = new KSPExampleGraph();

        Object sourceVertex = "S";
        int nbPaths = 2;
        KShortestPaths ksp = new KShortestPaths(graph, sourceVertex, nbPaths);

        Object targetVertex = "T";
        assertEquals(nbPaths, ksp.getPaths(targetVertex).size());
    }
}

// End $file.name$
