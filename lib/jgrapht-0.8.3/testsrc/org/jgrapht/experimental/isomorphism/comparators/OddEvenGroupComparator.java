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
 * OddEvenGroupComparator.java
 * -----------------
 * (C) Copyright 2005-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * $Id: OddEvenGroupComparator.java 645 2008-09-30 19:44:48Z perfecthash $
 *
 * Changes
 * -------
 */
package org.jgrapht.experimental.isomorphism.comparators;

import org.jgrapht.experimental.equivalence.*;


/**
 * Comparator which defines two groups of integers. Odds (mod2=1) and
 * Evens(mod2=0). Works only on Integers.
 *
 * @author Assaf
 * @since Jul 22, 2005
 */
public class OddEvenGroupComparator
    implements EquivalenceComparator<Integer, Object>
{
    //~ Methods ----------------------------------------------------------------

    public boolean equivalenceCompare(
        Integer arg1,
        Integer arg2,
        Object context1,
        Object context2)
    {
        int int1 = arg1.intValue();
        int int2 = arg2.intValue();

        boolean result = ((int1 % 2) == (int2 % 2));
        return result;
    }

    /* Odd and even must have unique values.
     * @see
     *
     *
     *
     *
     *
     * org.jgrapht.experimental.equivalence.EquivalenceComparator#equivalenceHashcode(java.lang.Object)
     */
    public int equivalenceHashcode(Integer arg1, Object context)
    {
        int int1 = arg1.intValue();
        return int1 % 2;
    }
}

// End OddEvenGroupComparator.java
