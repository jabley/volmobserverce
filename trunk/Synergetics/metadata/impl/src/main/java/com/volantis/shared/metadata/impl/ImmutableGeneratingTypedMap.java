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
import java.util.Map;

/**
 * A <code>TypedMap</code> which in addition will create Immutable instances of any of the
 * objects inserted / modified in it.
 */
public class ImmutableGeneratingTypedMap
        extends TypedMap
        implements Serializable {

    /**
     * The Serial Version UID.
     */
    static final long serialVersionUID = 3321575339099934370L;

    /**
     * Indicates whether the value is an {@link Inhibitor} and so this map needs
     * to store an immutable instance.
     */
    private final boolean makeValueImmutable;

    /**
     * Constructor which takes a <code>Map</code> and wraps it to restrict the keys and
     * values of this map to be of the specified type.
     * @param map The <code>Map</code> which this class wraps.
     * @param allowableValueClass The allowable <code>Class</code> of objects which can be
     *        used as values in this <code>Map</code>.
     */
    public ImmutableGeneratingTypedMap(Map map,
                                       Class allowableValueClass,
                                       boolean allowNullValue) {
        super(map, allowableValueClass, allowNullValue);

        // Check to see whether values need to be made immutable.
        if (Inhibitor.class.isAssignableFrom(allowableValueClass)) {
            makeValueImmutable = true;
        } else {
            makeValueImmutable = false;
        }
    }

    /**
     * Helper method which ensures that an object is of the allowable class and if so
     * generates an immutable instance of it.
     * @param value The object which is being tested.
     * @return the element if the specified object is allowed in this list.
     */
    protected Object checkValue(Object value) {
        Object object = super.checkValue(value);
        if (makeValueImmutable && object != null) {
            object = ((Inhibitor) object).createImmutable();
        }
        return object;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6560/5	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
