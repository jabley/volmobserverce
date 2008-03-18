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

package com.volantis.shared.metadata.impl;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A {@link Set} which only accepts objects of a type which is specified during
 * construction of this object.
 */
public class TypedSet
        extends AbstractSet
        implements Serializable {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = -1083178323311179370L;

    /**
     * The <code>Set</code> which is wrapped by this object.
     */
    private Set set;

    /**
     * The allowable <code>Class</code> of objects which can be put into this set.
     */
    private Class allowableClass;

    /**
     * The name of the allowable class (for persistence only)
     */
    private final String allowableClassName;

    /**
     * Constructor which takes a <code>Set</code> and wraps it to restrict it to only
     * allow objects of the specified class to be put in it.
     * @param set The <code>Set</code> to wrap.
     * @param allowableClass The <code>Class</code> of objects which can be put into this
     *        <code>Set</code>.
     */
    public TypedSet(Set set, Class allowableClass) {
        this.set = set;
        this.allowableClass = allowableClass;
        this.allowableClassName = allowableClass.getName();
    }

    /**
     * This method should be the only mechanism used to access the allowable
     * class
     * @return the allowable class
     */
    private Class getAllowableClass() {
        synchronized(this) {
            if (null == allowableClass) {
                try {
                    allowableClass = Class.forName(allowableClassName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return allowableClass;
    }

    // Javadoc inherited
    public Iterator iterator() {
        return set.iterator();
    }

    // Javadoc inherited
    public int size() {
        return set.size();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.  More
     * formally, returns <tt>true</tt> if and only if this set contains an
     * element <code>e</code> such that <code>(o==null ? e==null :
     * o.equals(e))</code>.
     *
     * @param o element whose presence in this set is to be tested.
     * @return <tt>true</tt> if this set contains the specified element.
     * @throws ClassCastException if the type of the specified element
     * 	       is incompatible with this set (optional).
     * @throws NullPointerException if the specified element is null and this
     *         set does not support null elements (optional).
     * @throws IllegalArgumentException if the object provided is not of the
     *         allowableClasss associated with this set.
     */
    public boolean contains(Object o) {
        return set.contains(checkElement(o));
    }

    /**
     * Adds the specified element to this set if it is not already present
     * (optional operation).  More formally, adds the specified element,
     * <code>o</code>, to this set if this set contains no element
     * <code>e</code> such that <code>(o==null ? e==null :
     * o.equals(e))</code>.  If this set already contains the specified
     * element, the call leaves this set unchanged and returns <tt>false</tt>.
     * In combination with the restriction on constructors, this ensures that
     * sets never contain duplicate elements.<p>
     *
     * The stipulation above does not imply that sets must accept all
     * elements; sets may refuse to add any particular element, including
     * <tt>null</tt>, and throwing an exception, as described in the
     * specification for <tt>Collection.add</tt>.  Individual set
     * implementations should clearly document any restrictions on the the
     * elements that they may contain.
     *
     * @param o element to be added to this set.
     * @return <tt>true</tt> if this set did not already contain the specified
     *         element.
     *
     * @throws UnsupportedOperationException if the <tt>add</tt> method is not
     * 	       supported by this set.
     * @throws ClassCastException if the class of the specified element
     * 	       prevents it from being added to this set.
     * @throws NullPointerException if the specified element is null and this
     *         set does not support null elements.
     * @throws IllegalArgumentException if some aspect of the specified element
     *         prevents it from being added to this set.
     * @throws IllegalArgumentException if the object provided is not of the
     *         allowableClasss associated with this set.
     */
    public boolean add(Object o) {
        return set.add(checkElement(o));
    }


    /**
     * Helper method which ensures that an object is of the allowable class.
     * @param element The object which is being tested.
     * @return the element if the specified object is allowed in this list.
     */
    protected Object checkElement(Object element) {
        if (!getAllowableClass().isInstance(element)) {
            throw new IllegalArgumentException(
                "o is Not " + getAllowableClass().getName());
        }

        return element;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 13-Jan-05	6560/5	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
