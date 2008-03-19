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

import com.volantis.shared.inhibitor.Inhibitor;

import java.io.Serializable;
import java.util.List;

/**
 * This is an {@link TypedList} with the
 */
public class ImmutableGeneratingTypedList
        extends TypedList implements Serializable {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 1248738718653049483L;

    /**
     * Constructor.
     * @param backingList the backing list
     * @param allowableClass the allowable class of objects for this list
     */
    public ImmutableGeneratingTypedList(List backingList, Class allowableClass) {
        super(backingList, allowableClass);
    }

    /**
     * Helper method which ensures that an object is of the allowable class and if
     * necessary creates an Immutable version of it.
     * @param element The object which is being tested.
     * @return an immutable instance of the provided element if the specified object is
     *         allowed in this list.
     */
    protected Object checkElement(Object element) {
        Inhibitor object = (Inhibitor) super.checkElement(element);
        return object.createImmutable();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6560/5	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
