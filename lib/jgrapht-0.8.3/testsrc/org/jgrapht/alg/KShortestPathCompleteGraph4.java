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
 * KShortestPathCompleteGraph4.java
 * -------------------------
 * (C) Copyright 2007-2008, by France Telecom
 *
 * Original Author:  Guillaume Boulmier and Contributors.
 *
 * $Id: KShortestPathCompleteGraph4.java 645 2008-09-30 19:44:48Z perfecthash $
 *
 * Changes
 * -------
 * 05-Jun-2007 : Initial revision (GB);
 *
 */
package org.jgrapht.alg;

import org.jgrapht.graph.*;


/**
 * @author Guillaume Boulmier
 * @since July 5, 2007
 */
@SuppressWarnings("unchecked")
public class KShortestPathCompleteGraph4
    extends SimpleWeightedGraph
{
    //~ Static fields/initializers ---------------------------------------------

    /**
     */
    private static final long serialVersionUID = -4091707260999013100L;

    //~ Instance fields --------------------------------------------------------

    public Object e12;

    public Object e13;

    public Object e23;

    public Object eS1;

    public Object eS2;

    public Object eS3;

    //~ Constructors -----------------------------------------------------------

    public KShortestPathCompleteGraph4()
    {
        super(DefaultWeightedEdge.class);
        addVertices();
        addEdges();
    }

    //~ Methods ----------------------------------------------------------------

    private void addEdges()
    {
        this.eS1 = addEdge("vS", "v1");
        this.eS2 = addEdge("vS", "v2");
        this.eS3 = addEdge("vS", "v3");
        this.e12 = addEdge("v1", "v2");
        this.e13 = addEdge("v1", "v3");
        this.e23 = addEdge("v2", "v3");

        setEdgeWeight(this.eS1, 1.0);
        setEdgeWeight(this.eS2, 1.0);
        setEdgeWeight(this.eS3, 1000.0);
        setEdgeWeight(this.e12, 1.0);
        setEdgeWeight(this.e13, 1.0);
        setEdgeWeight(this.e23, 1.0);
    }

    private void addVertices()
    {
        addVertex("vS");
        addVertex("v1");
        addVertex("v2");
        addVertex("v3");
    }
}

// End KShortestPathCompleteGraph4.java
