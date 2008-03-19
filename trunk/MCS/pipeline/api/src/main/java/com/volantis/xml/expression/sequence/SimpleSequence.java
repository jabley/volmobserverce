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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.sequence;

import com.volantis.xml.expression.SequenceIndexOutOfBoundsException;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.atomic.StringValue;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This is a simple, generic implementation of the Sequence interface. It also
 * implements the collection interface to allow the sequence to be manipulated
 * via the standard java collection API.
 *
 * @see com.volantis.xml.expression.ExpressionFactory
 */
public class SimpleSequence implements Sequence, Collection {
    /**
     * Used for outputting whitespace in string and stream conversions
     */
    private static final char[] WHITESPACE = {' '};

    /**
     * Used to handle a sequence created with a null Item array
     */
    private static final Item[] NO_ITEMS = new Item[0];

    /**
     * The factory by which this sequence was created.
     */
    protected ExpressionFactory factory;

    /**
     * The items in this sequence
     */
    private Item[] items;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param factory the factory by which this sequence was created
     * @param items   the items that the sequence is to contain
     */
    public SimpleSequence(ExpressionFactory factory,
                          Item[] items) {
        this.factory = factory;

        if (items != null) {
            this.items = new Item[items.length];

            System.arraycopy(items, 0, this.items, 0, items.length);
        } else {
            this.items = NO_ITEMS;
        }
    }

    //-------------------------------------------------------------------------
    // Sequence Interface Implementation
    //-------------------------------------------------------------------------

    // javadoc inherited
    public int getLength() {
        return items.length;
    }

    // javadoc inherited
    public Item getItem(int index)
            throws SequenceIndexOutOfBoundsException {
        if ((index < 1) || (index > items.length)) {
            throw new SequenceIndexOutOfBoundsException(
                    "index " + index + " is out of bounds" +
                    ((items.length > 0) ?
                     " (1.." + items.length + ")" :
                     ""));
        } else {
            return items[index - 1];
        }
    }

    // javadoc inherited
    public StringValue stringValue() throws ExpressionException {
        // Create the buffer with a guestimate size based on the number of
        // items (this would easily handle a sequence of integer or double
        // values)
        StringBuffer result = new StringBuffer(items.length * 16);

        // Generate a value based on all contained values separated by simple
        // whitespace delimiters
        for (int i = 0;
             i < items.length;
             i++) {
            result.append(items[i].stringValue().asJavaString());

            // Only output whitespace between items in the sequence and
            // specifically not after the last item
            if (i < items.length - 1) {
                result.append(WHITESPACE);
            }
        }

        return factory.createStringValue(result.toString());
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws ExpressionException, SAXException {
        for (int i = 0;
             i < items.length;
             i++) {
            items[i].streamContents(contentHandler);

            // Only output whitespace between items in the sequence and
            // specifically not after the last item
            if (i < items.length - 1) {
                contentHandler.characters(WHITESPACE, 0, WHITESPACE.length);
            }
        }
    }

    // javadoc inherited
    public Sequence getSequence() throws ExpressionException {
        return this;
    }

    //-------------------------------------------------------------------------
    // Collection Interface Implementation
    //-------------------------------------------------------------------------

    // javadoc inherited
    public int size() {
        return items.length;
    }

    // javadoc inherited
    public boolean isEmpty() {
        return items.length == 0;
    }

    // javadoc inherited
    public boolean contains(Object o) {
        boolean result = false;

        for (int i = 0;
             !result && (i < items.length);
             i++) {
            result = (items[i] == o);
        }

        return result;
    }

    // javadoc inherited
    public Iterator iterator() {
        // Provide an iterator that can iterate over but not modify the
        // items array
        return new Iterator() {
            /**
             * The next index within the sequence (based on java indices, not
             * XPath indices) to be returned.
             */
            private int index = 0;

            // javadoc inherited
            public boolean hasNext() {
                return index < items.length;
            }

            // javadoc inherited
            public Object next() {
                if (index < items.length) {
                    return items[index++];
                } else {
                    throw new NoSuchElementException(
                            "The sequence only contains " + items.length +
                            " items");
                }
            }

            /**
             * Item removal is not permitted by this iterator.
             *
             * @throws UnsupportedOperationException since the <tt>remove</tt>
             *         operation is not supported by this Iterator.
             */
            public void remove() {
                throw new UnsupportedOperationException(
                        "Cannot remove items from a sequence");
            }
        };
    }

    // javadoc inherited
    public Object[] toArray() {
        Object[] array = new Object[items.length];

        System.arraycopy(items, 0, array, 0, items.length);

        return array;
    }

    // javadoc inherited
    public Object[] toArray(Object a[]) {
        Object[] array = a;

        if (array.length < items.length) {
            array = (Object[])java.lang.reflect.Array.newInstance(
                    a.getClass().getComponentType(), items.length);
        }

        System.arraycopy(items, 0, array, 0, items.length);

        if (array.length > items.length) {
            array[items.length] = null;
        }

        return array;
    }

    // javadoc inherited
    public boolean add(Object o) {
        throw new UnsupportedOperationException(
                "Cannot add items to a sequence");
    }

    // javadoc inherited
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(
                "Cannot remove items from a sequence");
    }

    // javadoc inherited
    public boolean containsAll(Collection c) {
        Iterator iterator = c.iterator();
        boolean result = true;

        while (result &&
                iterator.hasNext()) {
            result = contains(iterator.next());
        }

        return result;
    }

    // javadoc inherited
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException(
                "Cannot add items to a sequence");
    }

    // javadoc inherited
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException(
                "Cannot remove items from a sequence");
    }

    // javadoc inherited
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException(
                "Cannot modify a sequence");
    }

    // javadoc inherited
    public void clear() {
        throw new UnsupportedOperationException(
                "Cannot clear a sequence");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
