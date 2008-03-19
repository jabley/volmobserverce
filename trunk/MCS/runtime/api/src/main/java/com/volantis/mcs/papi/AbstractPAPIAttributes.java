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
package com.volantis.mcs.papi;

import java.util.Iterator;

/**
 * This class is responsible for the management and manipulation of attributes
 * in a generic way.
 * <p/>
 * It is subclassed by all the generated, element specific PAPIAttributes
 * classes, which provide specific mutator methods for each of the attributes
 * that are particular to them. This is required for backwards compatibility.
 * <p/>
 * However, this class stores an instance of PAPIAttributes (retrieved from
 * {@link PAPIElementFactory#createGenericAttributes()}) to which all specific
 * attribute mutator methods will delegate.
 *
 *
 */
public abstract class AbstractPAPIAttributes implements PAPIAttributes {

    /**
     * The generic PAPIAttributes instance to delegate to. It is generic
     * because it only provides access to the attribute values via the
     * generic attribute mutators.
     */
    final PAPIAttributes papiAttributes;

    protected AbstractPAPIAttributes(PAPIElementFactory elementFactory) {
        papiAttributes = elementFactory.createGenericAttributes();
        // This constructor delegates all its work to the reinitialise method
        // no extra initialisation should be added here, instead it should be
        // added to the reinitialise method.
        reinitialise();
    }

    /**
     * Resets the internal state so it is equivalent (not necessarily
     * identical) to a new instance.
     */
    public void reset() {

        // Call this after calling super.reset to allow reinitialise to
        // override any inherited attributes.
        reinitialise();
    }

    /**
     * Reinitialise all the data members. This is called from the constructor
     * and also from reset.
     */
    protected void reinitialise() {
        papiAttributes.reset();
    }

    // javadoc inherited
    public PAPIAttributes getGenericAttributes() {
        return papiAttributes.getGenericAttributes();
    }

    // javadoc inherited
    public String getAttributeValue(String namespace, String localName) {
        return papiAttributes.getAttributeValue(namespace, localName);
    }

    // javadoc inherited
    public void setAttributeValue(String namespace, String localName, String value) {
        papiAttributes.setAttributeValue(namespace, localName, value);
    }

    public Iterator getAttributeNames(String namespace) {
        return papiAttributes.getAttributeNames(namespace);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 ===========================================================================
*/
