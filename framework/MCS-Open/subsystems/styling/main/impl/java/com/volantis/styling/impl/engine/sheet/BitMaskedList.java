package com.volantis.styling.impl.engine.sheet;

import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * An immutable list that contains only those elements marked within a bitset
 * of the same length as the list.
 */
public class BitMaskedList implements List {
    /**
     * The underlying list on which this masked list is based.
     */
    private final List underlyingList;

    /**
     * A mask specifying which list items are visible.
     */
    private final BitSet bitSetMask;

    /**
     * Create a new masked list from an underlying list and a mask containing
     * true at the index of items to include in the masked list.
     *
     * <p>The underlying list must have the same size as the bitset. This is
     * not currently checked to avoid a performance hit, but failure to follow
     * this rule may lead to unexpected behaviour.</p>
     *
     * @param underlyingList The underlying list to mask
     * @param bitSetMask The mask to apply
     */
    public BitMaskedList(List underlyingList, BitSet bitSetMask) {
        this.underlyingList = underlyingList;
        this.bitSetMask = bitSetMask;
    }

    // Javadoc inherited
    public int size() {
        return bitSetMask.cardinality();
    }

    // Javadoc inherited
    public boolean isEmpty() {
        return size() == 0;
    }

    // Javadoc inherited
    public boolean contains(Object o) {
        boolean contains = false;
        for (int i = 0; !contains && i < underlyingList.size(); i++) {
            contains =  (bitSetMask.get(i) && underlyingList.get(i).equals(o));
        }
        return contains;
    }

    // Javadoc inherited
    public Object[] toArray() {
        Object[] array = new Object[size()];
        int bitIndex = -1;
        for (int i = 0; i < array.length; i++) {
            bitIndex = bitSetMask.nextSetBit(bitIndex + 1);
            array[i] = underlyingList.get(bitIndex);
        }
        return new Object[0];
    }

    // The following methods are not required by our current usage of this
    // class, and so are not implemented. They should be implemented as
    // required in future.

    // Javadoc inherited
    public Object get(int index) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public Iterator iterator() {
        return new Iterator() {
            private int indexPointer = bitSetMask.nextSetBit(0);

            // Javadoc inherited
            public boolean hasNext() {
                return indexPointer != -1;
            }

            // Javadoc inherited
            public Object next() {
                Object returnObject = null;
                if (indexPointer == -1) {
                    throw new NoSuchElementException();
                } else {
                    try {
                        returnObject = underlyingList.get(indexPointer);
                        indexPointer = bitSetMask.nextSetBit(indexPointer + 1);
                    } catch (Exception e) {
                        throw new IllegalStateException(
                            "List size is " + underlyingList.size() +
                            ", bitset size is " + bitSetMask.size() +
                            ", bitset cardinality is " + bitSetMask.cardinality()
                        );
                    }
                }
                return returnObject;
            }

            // Javadoc inherited
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Javadoc inherited
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public ListIterator listIterator() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public ListIterator listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public List subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public boolean containsAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException();
    }

    // The following operations modify the list, and as such are not
    // supported.

    // Javadoc inherited
    public boolean add(Object o) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public boolean addAll(int index, Collection c) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public void clear() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public Object set(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public void add(int index, Object element) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited
    public Object remove(int index) {
        throw new UnsupportedOperationException();
    }
}