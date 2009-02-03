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
import java.util.AbstractList;
import java.util.List;

/**
 * A list which only accepts objects of a particular type which is defined during the
 * construction of this object.
 */
public class TypedList
        extends AbstractList
        implements Serializable {

    /**
     * The Serial Version UID.
     */
    //static final long serialVersionUID = -1728557817014813748L;

    /**
     * The list which this object wraps.
     */
    private List backingList;

    /**
     * The allowable <code>Class</code> of objects which can be put into this list.
     */
    private Class allowableClass;

    /**
     * Hold the class name of the allowable class. This works around the lack
     * of jpox support for the Class object. This should never be null
     */
    private String allowableClassName;

    /**
     * Constructor which takes a <code>List</code> and wraps it to restrict the objects
     * inserted in it to only be of the specified class.
     * @param backingList The <code>List</code> which this object wraps.
     * @param allowableClass The <code>Class</code> of objects which can be put
     * into this. Must not be null.
     */
    public TypedList(List backingList, Class allowableClass) {
        this.backingList = backingList;
        this.allowableClass = allowableClass;
        this.allowableClassName = allowableClass.getName();
    }

    // Javadoc inherited.
    public Object get(int index) {
        return backingList.get(index);
    }

    // Javadoc inherited.
    public int size() {
        return backingList.size();
    }


    public boolean equals(Object o) {
        return super.equals(o) && ((TypedList)o).allowableClassName.equals(allowableClassName);
    }

    /**
     * Return the allowable class. This is the only method that should be used
     * to access the allowableClass member
     *
     * @return the allowable class or null
     */
    private Class getAllowableClass() {
        synchronized (this) {
            if(allowableClass == null) {
                try {
                    allowableClass = Class.forName(allowableClassName);
                } catch (ClassNotFoundException e) {
                    // just rethrow as a runtime exception
                    throw new RuntimeException(e);
                }
            }
        }
        return allowableClass;
    }

    /**
     * Helper method which ensures that an object is of the allowable class.
     * @param element The object which is being tested.
     * @return the element if the specified object is allowed in this list.
     */
    protected Object checkElement(Object element) {
        if (!getAllowableClass().isInstance(element)) {
            throw new IllegalArgumentException("o is Not " + allowableClassName);
        }

        return element;
    }

    // Javadoc inherited.
    public void add(int index, Object element) {
        backingList.add(index, checkElement(element));
    }

    // Javadoc inherited.
    public Object set(int index, Object element) {
        return backingList.set(index, checkElement(element));
    }

    // Javadoc inherited.
    public Object remove(int index) {
        return backingList.remove(index);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 13-Jan-05	6560/5	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
