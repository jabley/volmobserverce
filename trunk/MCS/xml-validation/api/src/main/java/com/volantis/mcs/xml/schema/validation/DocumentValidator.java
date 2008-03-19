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

package com.volantis.mcs.xml.schema.validation;

import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * A validator for an XML document.
 */
public interface DocumentValidator {

    /**
     * Get the element type for the element that is currently being validated.
     *
     * <p>The returned value will be the element type of the first element in
     * the ancestor elements that is not a transparent element.</p>
     *
     * @return The current element type, will be null if not inside the root
     *         element, or all containing elements are transparent.
     */
    ElementType getValidatingElement();

    /**
     * Invoked for an XML element before invoking any methods of its children.
     * @param element The element for which this is invoked.
     * @throws ValidationException If it was invalid for 
     */
    void open(ElementType element)
            throws ValidationException;

    boolean pcdata(boolean isWhitespace)
            throws ValidationException;

    void close(ElementType element)
            throws ValidationException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
