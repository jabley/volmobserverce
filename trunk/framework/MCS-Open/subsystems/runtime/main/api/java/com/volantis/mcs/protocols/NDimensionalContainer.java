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
 * $Header: /src/voyager/com/volantis/mcs/protocols/NDimensionalContainer.java,v 1.4 2003/01/29 15:51:07 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Oct-02    Chris W         VBM:2002110404 - Created
 * 29-Oct-02    Sumit           VBM:2002110404 - Added methods to iterator lists
 *                              and get lists at specific positions
 * 13-Nov-02    Sumit           VBM:2002111301 - Added a remove method to 
 *                              delete an object at an FIR in the underlying
 *                              list
 * 14-Oct-02    Chris W         VBM:2002111301 - Fixed remove method. Elements
 *                              are removed by setting the value at the index
 *                              to null rather than called ArrayList.remove()
 *                              which shifts the elements to the left. 
 * 20-Nov-02    Chris W         VBM:2002110404 - Fixed get method to return null
 *                              if we try to access an element after the end of
 *                              a list. Refactored get and remove to use the
 *                              common getListAt code. 
 * 21-Nov-02    Chris W         VBM:2002110404 - Corrections made as a result of
 *                              unit tests. isEmpty renamed to isTopLevelEmpty
 *                              next renamed to nextInCurrentDimension. New
 *                              method names more accurately describe the 
 *                              functionality.
 * 13-Jan-03    Chris W         VBM:2003011311 - Added getNumCellsInDimension.
 *                              This is needed when a FormatIterator specifies
 *                              that it renders a variable no. of cells.
 * 29-Jan-03    Chris W         VBM:2003012203 - Improved performance of
 *                              getNumCellsInDimension by looping through cells
 *                              backwards.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * An NDimensionalContainer stores objects so that they can be retrieved using
 * an n-dimensional index. The number of dimensions is set when the first
 * object is placed in the container.
 * <p/>
 * Things stored in this container are referred to by using
 * NDimensionalIndex objects.
 * <p/>
 * NOTE: In order to make it convenient to use this class in the (only)
 * context which we currently use it, it can also store a single entry at
 * {@link NDimensionalIndex#ZERO_DIMENSIONS}.
 */
public final class NDimensionalContainer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(NDimensionalContainer.class);


    /**
     * The single object which may conveniently be stored at
     * {@link NDimensionalIndex#ZERO_DIMENSIONS}.
     */
    private Object zeroDimensions;

    /**
     * The top level container. To access objects in a n-dimensional way we use
     * a list of list of lists etc...
     */
    private List container;

    public NDimensionalContainer() {
    }

    /**
     * Stores a value in the container at a specified position.
     *
     * @param index The NDimensionalIndex containing the position. It is
     *              assumed that the NDimensionalIndex contains an index with
     *              the correct number of dimensions. This should be checked
     *              when the object is created.
     * @param value The object to be stored.
     */
    public void set(NDimensionalIndex index, Object value) {
        if (index == NDimensionalIndex.ZERO_DIMENSIONS) {
            zeroDimensions = value;
            return;
        }

        if (container == null) {
            // We use an ArrayList as we assume that the indices will be
            // almost consecutive ints (i.e. not credit card numbers etc)
            // In the future associative arrays i.e. indices that are letters
            // may need to be supported.
            // You are meant to add elements to ArrayLists by calling the add
            // method not by randomly accessing locations within the list. We
            // want the latter behaviour. It might be better to provide our
            // own implementation of List.          
            container = new ArrayList();
        }

        List list = container;
        int[] indicies = index.getIndicies();
        int dimensions = index.getDimensions();
        
        // loop around for the number of dimensions in the index.
        // Let x denote the whole index, d the number of dimensions of the
        // index and xi be the ith part of x where i = {0, 1, ... , d-1}
        // e.g. x = (0, 1, 2} then x0 = 0, x1 = 1 etc.        
        for (int i = 0; i < dimensions; i++) {
            if (i < (dimensions - 1)) {
                // To avoid IndexOfBoundsExceptions we need to add some cells
                // before we can set the values.                
                for (int j = list.size(); j <= indicies[i]; j++) {
                    list.add(null);
                }
                Object currentObj = list.get(indicies[i]);
                if (currentObj == null) {
                    // If the list doesn't contain an element at position i
                    // and i < d-1 then we need to create a new list for
                    // this index. Set the current list to be this new one.
                    List list2 = new ArrayList();
                    list.set(indicies[i], list2);
                    list = list2;
                } else {
                    // If the list contains an element at position i and
                    // i < d-1 then we set the current list to be the one
                    // we've just retrieved.
                    list = (List) currentObj;
                }
            } else {
                // If i = d-1 i.e. the last dimension we store the value in
                // the appropriate place in the map.
                // To avoid IndexOfBoundsExceptions we need to add some cells
                // before we can set the values.                
                for (int j = list.size(); j <= indicies[i]; j++) {
                    list.add(null);
                }
                list.set(indicies[i], value);

            }
        }
    }

    /**
     * Returns the number of elements in a particular dimension. This is
     * equal to the index of the last non-null element in the list plus 1.
     * e.g. if the list contains:
     * a, b, c, null we return 3
     * null, a, b, c we return 4
     * a, null, b, c we return 4
     *
     * @param index An index denoting the dimension to inspect
     * @return int The number of elements in this dimension
     */
    public int getNumCellsInDimension(NDimensionalIndex index) {
        // Get the list at the last but one dimension
        List list = getListAt(index);

        if (list == null) {
            return 0;
        }
        
        // loop backwards as we are looking for the last non-null
        // element.
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) != null) {
                return i + 1;
            }
        }
        return 0;
    }

    /**
     * Retrieves the specified value from the container
     *
     * @param index
     * @return Object
     */
    public Object get(NDimensionalIndex index) {
        if (index == NDimensionalIndex.ZERO_DIMENSIONS) {
            return zeroDimensions;
        }
        
        // Get the list at the last but one dimension
        List list = getListAt(index);
        
        // Get the value of the last indice e.g. the last indice of 1.0.2 is 2
        int pos = index.getIndicies()[index.getDimensions() - 1];
        
        // Avoid IndexOutOfBoundsExceptions 
        if (list == null || list.size() <= pos) {
            return null;
        }
        return list.get(pos);
    }

    /**
     * Removes the specified value from the container
     *
     * @param index
     */
    void remove(NDimensionalIndex index) {
        if (index == NDimensionalIndex.ZERO_DIMENSIONS) {
            zeroDimensions = null;
            return;
        }

        // Get the list at the last but one dimension
        List list = getListAt(index);

        // Get the value of the last indice e.g. the last indice of 1.0.2 is 2
        int pos = index.getIndicies()[index.getDimensions() - 1];

        // Avoid IndexOutOfBoundsExceptions
        if (list == null || list.size() <= pos) {
            return;
        }
        // We can't call list.remove(pos) as this shifts any subsequent
        // elements to the left and subtracts one from their indices.
        // Changing the indices will stop the get methods from retrieving
        // the correct contents.
        list.set(pos, null);
    }

    /**
     * Retrieves a List at a specified fir. Ensure that the index refers
     * to the List itself rather than elements of the list i.e. if the list is
     * at 1.0 make sure index is 1.0 and not 1.0.x
     */

    private List getListAt(NDimensionalIndex index) {
        if (container == null) {
            return null;
        }
        List list = container;
        int[] indicies = index.getIndicies();
        int dimensions = index.getDimensions();

        if (logger.isDebugEnabled()) {
            logger.debug("NDimensionalContainer=" + this.toString());
            logger.debug("index=" + index);
        }

        for (int i = 0; i < dimensions - 1; i++) {
            // To avoid IndexOfBoundsExceptions we check that the list has
            // more elements than the required index.
            if (list.size() <= indicies[i]) {
                return null;
            }
            list = (List) list.get(indicies[i]);
            if (list == null) {
                // If at any stage we can't find the list for this part of the
                // index return null.
                return null;
            }
        }
        return list;
    }

    /**
     * Debug method
     *
     * @return String
     */
    public String toString() {
        return container.toString();
    }

    /**
     * Returns an iterator that will return all items stored in this container
     * from the lowest index to the highest.
     * <p>
     * e.g.
     * <p>
     * A Iterator for a 2 dimensional container would return items in the
     * following order:
     * <p>
     * 0.0, 0.1, 0.2, 0.3 ... 1.0, 1.1, 1.2, 1.3 ... 2.0, 2.1, 2.2 etc...
     *
     * @return an Iterator containing all items stored in this container,
     * ordered by index (lowest to highest).
     */
    public Iterator iterator() {
        return new NDimensionalContainerIterator();
    }


    /**
     * This class is responsible for providing an Iterator for
     * NDimensionalContainer which will iterate over all non null elements
     * in the container.
     */
    private final class NDimensionalContainerIterator implements Iterator {

        /**
         * An iterator that allows us to return the
         * non-null elements in the container
         */
        private Iterator currentIterator;

        /**
         * A stack for storing the iterators needed with
         * multi-dimensional containers.
         */
        private final Stack iteratorStack;

        /**
         * Initialises the new instance.
         */
        public NDimensionalContainerIterator() {
            if (container != null) {
                currentIterator = container.iterator();
            } else {
                // Avoid null pointer when container is empty and handle zero
                // dimensions if available
                List emptyContainer = new ArrayList();

                if (zeroDimensions != null) {
                    emptyContainer.add(zeroDimensions);
                }
                currentIterator = emptyContainer.iterator();
            }

            iteratorStack = new Stack();
        }

        // Javadoc inherited.
        public void remove() {
            currentIterator.remove();
        }

        // Javadoc inherited.
        public boolean hasNext() {
            if (currentIterator.hasNext()) {
                return true;
            } else {
                // May have other iterators stored that have only been
                // partially processed.

                // Note that is a valid to assume that a non empty
                // stack idicates we have more items to iterate over
                // because iterators are only added to the stack if they
                // have more elements to be iterated.
                return !iteratorStack.isEmpty();
            }
        }

        // Javadoc inherited.
        // @todo Needs refactoring so we only have a single return statement.
        public Object next() {

            while (currentIterator != null && currentIterator.hasNext()) {
                Object currentObject = currentIterator.next();

                // The current object may be another list which will
                // need to be iterated.
                if (currentObject instanceof List) {

                    // However, we do not want to lose any remaining items
                    // in the current iteration.  We therefore need to
                    // save the current iteration (if it has more items)
                    // so we can return to it later.
                    if (currentIterator.hasNext()) {
                        iteratorStack.push(currentIterator);
                    }

                    // A new iterator is required for this newly
                    // encountered list.
                    currentIterator = ((List) currentObject).iterator();
                    return next();
                }
                if (currentObject != null) {
                    return currentObject;
                }
            }
            // Are there any iterators stored that have only been partially
            // processed?
            if (!iteratorStack.isEmpty()) {
                currentIterator = (Iterator) iteratorStack.pop();
                return next();
            } else {
                throw new NoSuchElementException();
            }
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Jul-05	8927/2	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 30-Jun-05	8734/3	rgreenall	VBM:2005052611 Post review improvements

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
