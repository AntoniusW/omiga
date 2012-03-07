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
/* -----------------
 * EquivalenceGroupCreatorTest.java
 * -----------------
 * (C) Copyright 2005-2008, by Assaf Lehr and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * $Id: EquivalenceGroupCreatorTest.java 645 2008-09-30 19:44:48Z perfecthash $
 *
 * Changes
 * -------
 */
package org.jgrapht.experimental.equivalence;

import java.util.*;

import junit.framework.*;

import org.jgrapht.experimental.isomorphism.comparators.*;


/**
 * @author Assaf
 * @since Jul 22, 2005
 */
public class EquivalenceGroupCreatorTest
    extends TestCase
{
    //~ Instance fields --------------------------------------------------------

    // create the groups array as 0 to X (it)
    final int INTEGER_ARRAY_SIZE = 25;

    //~ Methods ----------------------------------------------------------------

    /*
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    public void testUniformGroup()
    {
        // expecting two seperate groups , one with odd , one with even nubmers"
        testOneComparator(new UniformEquivalenceComparator(), 1);

        // " expecting 3 seperate groups , one for each mod3
        testOneComparator(
            new org.jgrapht.experimental.isomorphism.comparators.Mod3GroupComparator(),
            3);
    }

    public void testOddEvenGroup()
    {
        // " expecting two seperate groups , one with odd , one with even
        // nubmers");
        testOneComparator(
            new org.jgrapht.experimental.isomorphism.comparators.OddEvenGroupComparator(),
            2);

        // " expecting 3 seperate groups , one for each mod3");
        testOneComparator(
            new org.jgrapht.experimental.isomorphism.comparators.Mod3GroupComparator(),
            3);
    }

    /**
     * Using a chain of evenOdd(mod2) and mod3 comparator , should yield the 6
     * groups , which are infact mod6 , examples:
     * <li>mod2 = 0 , mod3 = 0 --> mod6=0 , like : 6 , 12
     * <li>mod2 = 1 , mod3 = 0 --> mod6=1 , like : 7 , 13
     * <li>
     */
    public void testComparatorChain()
    {
        EquivalenceComparatorChain<Integer, Object> comparatorChain =
            new EquivalenceComparatorChainBase<Integer, Object>(
                new OddEvenGroupComparator());
        comparatorChain.appendComparator(new Mod3GroupComparator());

        // for (int i=0 ; i<INTEGER_ARRAY_SIZE ; i++)
        // {
        // System.out.println("hash of "+i+" =
        // "+comparatorChain.equivalenceHashcode(integerArray[i], null));
        //
        //
        // }
        // expecting six seperate groups , with the different mod6 values");
        testOneComparator(
            comparatorChain,
            6);
    }

    @SuppressWarnings("unchecked")
    public void testComparatorChainSameComparatorTwice()
    {
        EquivalenceComparatorChain comparatorChain =
            new EquivalenceComparatorChainBase(new OddEvenGroupComparator());
        comparatorChain.appendComparator(new UniformEquivalenceComparator());
        comparatorChain.appendComparator(new OddEvenGroupComparator());

        // still expecting 2 groups "
        testOneComparator(
            comparatorChain,
            2);
    }

    @SuppressWarnings("unchecked")
    private void testOneComparator(
        EquivalenceComparator comparator,
        int expectedNumOfGroups)
    {
        ArrayList<Integer> integerArray =
            new ArrayList<Integer>(INTEGER_ARRAY_SIZE);
        for (int i = 0; i < INTEGER_ARRAY_SIZE; i++) {
            integerArray.add(i);
        }

        EquivalenceSet [] eqGroupArray =
            EquivalenceSetCreator.createEqualityGroupOrderedArray(
                integerArray,
                comparator,
                null);
        assertEquals(expectedNumOfGroups, eqGroupArray.length);

        // assert the group order size is sorted.
        for (int i = 1; i < eqGroupArray.length; i++) {
            EquivalenceSet set = eqGroupArray[i];
            assertTrue(set.size() >= eqGroupArray[i - 1].size());
        }
        // System.out.println("\nTesting the EquivalenceSet[] returned from
        // Integer["
        // +INTEGER_ARRAY_SIZE+"] filled with the integers as the indexes. \n"
        // + expectedResult);
        // System.out.println("result size="+eqGroupArray.length);
        // for (int i = 0; i < eqGroupArray.length; i++) {
        // System.out.println(eqGroupArray[i]);
        // }
    }
}

// End EquivalenceGroupCreatorTest.java
