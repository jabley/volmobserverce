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

/**
 * This interface describes a factory that will create a {@link PAPIElement} or
 * {@link PAPIAttributes} object.
 *
 * @mock.generate
 */
public interface PAPIElementFactory {
    /**
     * Create a new PAPI Element for this factory.
     * @return The new PAPIElement
     */
    PAPIElement createPAPIElement();

    /**
     * Create a new PAPI Attributes specific to this factory.
     * <p/>
     * Element specific PAPI attributes will define specific mutator methods
     * for each of the attributes particular to them.
     * 
     * @return The new element specific PAPIAttributes
     */
    PAPIAttributes createElementSpecificAttributes();

    /**
     * Create a new generic PAPI Attributes.
     * <p/>
     * A generic PAPI attributes means an implementation which only provides
     * access to the attribute values via the methods defined by PAPIAttributes.
     *
     * @return The new generic PAPIAttributes
     */
    PAPIAttributes createGenericAttributes();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 18-May-05	8196/3	ianw	VBM:2005051203 Fixed up javadoc

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
