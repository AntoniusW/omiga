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
/* -------------
 * AllTests.java
 * -------------
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: AllTests.java 706 2010-02-28 05:09:24Z perfecthash $
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 *
 */
package org.jgrapht;

import java.util.*;

import junit.framework.*;

import org.jgrapht.alg.*;
import org.jgrapht.alg.util.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.jgrapht.util.*;


/**
 * Runs all unit tests of the JGraphT library.
 *
 * @author Barak Naveh
 */
public final class AllTests
{
    //~ Constructors -----------------------------------------------------------

    private AllTests()
    {
    } // ensure non-instantiability.

    //~ Methods ----------------------------------------------------------------

    /**
     * Creates a test suite that includes all JGraphT tests.
     *
     * @return a test suite that includes all JGraphT tests.
     */
    public static Test suite()
    {
        ExpandableTestSuite suite =
            new ExpandableTestSuite("All tests of JGraphT");

        suite.addTestSuit((TestSuite) AllAlgTests.suite());
        suite.addTestSuit((TestSuite) AllAlgUtilTests.suite());
        suite.addTestSuit((TestSuite) AllGenerateTests.suite());
        suite.addTestSuit((TestSuite) AllGraphTests.suite());
        suite.addTestSuit((TestSuite) AllTraverseTests.suite());
        suite.addTestSuit((TestSuite) AllUtilTests.suite());

        return suite;
    }

    //~ Inner Classes ----------------------------------------------------------

    private static class ExpandableTestSuite
        extends TestSuite
    {
        /**
         * @see TestSuite#TestSuite()
         */
        public ExpandableTestSuite()
        {
            super();
        }

        /**
         * @see TestSuite#TestSuite(java.lang.String)
         */
        public ExpandableTestSuite(String name)
        {
            super(name);
        }

        /**
         * Adds all the test from the specified suite into this suite.
         *
         * @param suite
         */
        public void addTestSuit(TestSuite suite)
        {
            for (Enumeration e = suite.tests(); e.hasMoreElements();) {
                Test t = (Test) e.nextElement();
                this.addTest(t);
            }
        }
    }
}

// End AllTests.java
