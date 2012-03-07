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
/* -----------------------------
 * ClosestFirstIteratorTest.java
 * -----------------------------
 * (C) Copyright 2003-2008, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: ClosestFirstIteratorTest.java 645 2008-09-30 19:44:48Z perfecthash $
 *
 * Changes
 * -------
 * 03-Sep-2003 : Initial revision (JVS);
 * 29-May-2005 : Test radius support (JVS);
 *
 */
package org.jgrapht.traverse;

import org.jgrapht.*;
import org.jgrapht.graph.*;


/**
 * Tests for ClosestFirstIterator.
 *
 * @author John V. Sichi
 * @since Sep 3, 2003
 */
public class ClosestFirstIteratorTest
    extends AbstractGraphIteratorTest
{
    //~ Methods ----------------------------------------------------------------

    /**
     * .
     */
    public void testRadius()
    {
        result = new StringBuffer();

        DirectedGraph<String, DefaultEdge> graph = createDirectedGraph();

        // NOTE:  pick 301 as the radius because it discriminates
        // the boundary case edge between v7 and v9
        AbstractGraphIterator<String, ?> iterator =
            new ClosestFirstIterator<String, DefaultEdge>(graph, "1", 301);

        while (iterator.hasNext()) {
            result.append(iterator.next());

            if (iterator.hasNext()) {
                result.append(',');
            }
        }

        assertEquals("1,2,3,5,6,7", result.toString());
    }

    /**
     * .
     */
    public void testNoStart()
    {
        result = new StringBuffer();

        DirectedGraph<String, DefaultEdge> graph = createDirectedGraph();

        AbstractGraphIterator<String, ?> iterator =
            new ClosestFirstIterator<String, DefaultEdge>(graph);

        while (iterator.hasNext()) {
            result.append(iterator.next());

            if (iterator.hasNext()) {
                result.append(',');
            }
        }

        assertEquals("1,2,3,5,6,7,9,4,8,orphan", result.toString());
    }

    // NOTE:  the edge weights make the result deterministic
    String getExpectedStr1()
    {
        return "1,2,3,5,6,7,9,4,8";
    }

    String getExpectedStr2()
    {
        return getExpectedStr1() + ",orphan";
    }

    AbstractGraphIterator<String, DefaultEdge> createIterator(
        DirectedGraph<String, DefaultEdge> g,
        String vertex)
    {
        AbstractGraphIterator<String, DefaultEdge> i =
            new ClosestFirstIterator<String, DefaultEdge>(g, vertex);
        i.setCrossComponentTraversal(true);

        return i;
    }
}

// End ClosestFirstIteratorTest.java
