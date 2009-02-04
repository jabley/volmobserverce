/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.impl.engine.sheet;

import com.volantis.styling.impl.sheet.Styler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Merges one styler list into another by append normal priority stylers
 * after the last normal before the first normal priority stylers and
 *
 * An object that will merge styles into the styling engine by appending
 * the styles from the style sheet being merged from to the end of the
 * styles of the style sheet being merged to.
 *
 * <p>Styles with different priority are treated separately so given style
 * sheets A and B that have styles, <code>A1, A2, A3, !A4, !A5, !A6</code>
 * and <code>B1, B2, B3, !B4, !B5, !B6</code> (where ! indicates an
 * important style) then the result of using the returned merger to merge
 * B into A will be <code>A1, A2, A3, B1, B2, B3, !A4, !A5, !A6, !B4, !B5,
 * !B6</code>.</p>
 */
public class AppendMerger
        implements StylerListMerger {

    // Javadoc inherited.
    public void merge(MutableStylerList list, StylerList delta) {

        // HACK: quickly implement sorting by source and then priority.
        // todo: reimplement using manual merge sort when we have time.

        // Extract both lists into a single list.
        ArrayList merged = new ArrayList();
        Iterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            Object o = (Object) listIterator.next();
            // remove it from the original list, we will add it back in later.
            listIterator.remove();
            merged.add(o);
        }
        Iterator deltaIterator = delta.iterator();
        while (deltaIterator.hasNext()) {
            Object o = (Object) deltaIterator.next();
            merged.add(o);
        }

        // Sort by source and then priority.
        Collections.sort(merged, new Comparator() {
            public int compare(Object o1, Object o2) {
                int result;

                if (o1 instanceof Styler && o2 instanceof Styler) {
                    Styler styler1 = (Styler) o1;
                    Styler styler2 = (Styler) o2;

                    result = styler1.getSource().compareTo(styler2.getSource());
                    if (result == 0) {
                        // source is equal, try comparing by priority
                        result = styler1.getPriority().compareTo(styler2.getPriority());
                    }

                } else {
                    throw new IllegalArgumentException();
                }

                return result;
            }
        });

        // Stuff the merged styler list back into the original mutable list.
        Iterator mergedIterator = merged.iterator();
        while (mergedIterator.hasNext()) {
            Styler styler = (Styler) mergedIterator.next();
            list.append(styler);
        }
    }
//    // Javadoc inherited.
//    public void merge(MutableStylerList list, StylerList delta) {
//        Iterator iterator = delta.iterator();
//        ListIterator listIterator = list.listIterator();
//        Priority currentPriority = null;
//
//        while(iterator.hasNext()) {
//            Styler deltaStyler = (Styler) iterator.next();
//            Priority deltaPriority = deltaStyler.getPriority();
//            if (currentPriority != deltaPriority) {
//                moveBeforeFirstStylerOfHigherPriority(
//                        listIterator, deltaPriority);
//                currentPriority = deltaPriority;
//            }
//            listIterator.add(deltaStyler);
//        }
//    }
//
//    /**
//     * Move the list iterator so that it immediately precedes the first styler
//     * with a higher priority than the specified one.
//     *
//     * @param iterator
//     * @param priority
//     */
//    private void moveBeforeFirstStylerOfHigherPriority(
//            ListIterator iterator, Priority priority) {
//
//        // The iterator could either be at the very start of the list, or at
//        // the very end of the list, or immediately before something of a
//        // different priority.
//        while(iterator.hasNext()) {
//            Styler styler = (Styler) iterator.next();
//            int result = styler.getPriority().compareTo(priority);
//            if (result > 0) {
//                iterator.previous();
//                break;
//            }
//        }
//    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
