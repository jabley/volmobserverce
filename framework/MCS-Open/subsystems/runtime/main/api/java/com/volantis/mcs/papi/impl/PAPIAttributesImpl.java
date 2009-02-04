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
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.styling.engine.Attributes;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is necessary to allow the element specific PAPI attributes to be
 * passed to the styling engine, which requires generic attributes.
 * <p/>
 * This generic attributes class only allows access to the attribute values
 * via the generic mutator methods defined in PAPIAttributes; whereas the
 * element specific attributes classes provide mutators for each attribute
 * that is particular to them.
 * <p/>
 * Element specific PAPI attribute implementations should delegate to an
 * instance of this class, which should have been created by calling
 * {@link com.volantis.mcs.papi.PAPIElementFactory#createGenericAttributes()}
 *
 * @todo this class currently only supports one namespace (which is an empty string) and will need to be updated when MCS supports multiple namespaces internally.
 * @mock.generate
 */
public class PAPIAttributesImpl
        implements Attributes, PAPIAttributes {

    /**
     * Map which contains attribute values keyed against their names.
     */
    HashMap attributes = new HashMap();

    // javadoc inherited
    public String getAttributeValue(String namespace, String localName) {

        // this exception will be removed when multiple namespaces are
        // supported; it is present to make it obvious that this class does
        // not support multiple namespaces at the moment.
        if (namespace != null) {
            throw new UnsupportedOperationException("Unsupported namespace");
        }
        return (String) attributes.get(localName);
    }

    // javadoc inherited
    public void setAttributeValue(
            String namespace, String localName, String value) {

        // this exception will be removed when multiple namespaces are
        // supported; it is present to make it obvious that this class does
        // not support multiple namespaces at the moment.
        if (namespace != null) {
            throw new UnsupportedOperationException("Unsupported namespace");
        }
        attributes.put(localName, value);
    }

    // javadoc inherited
    public void reset() {
        attributes.clear();
    }

    // javadoc inherited
    public String getElementName() {
        return null;
    }

    // javadoc inherited
    public PAPIAttributes getGenericAttributes() {
        return this;
    }

    // javadoc inherited
    public Iterator getAttributeNames(String namespace) {
        // this exception will be removed when multiple namespaces are
        // supported; it is present to make it obvious that this class does
        // not support multiple namespaces at the moment.
        if (namespace != null) {
            throw new UnsupportedOperationException("Unsupported namespace");
        }
        return attributes.keySet().iterator();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 ===========================================================================
*/
