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

package com.volantis.mcs.xml.schema.impl.validation;

import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.ValidationException;

/**
 * Implementations are responsible for validating the content of elements.
 *
 * @mock.generate
 */
public interface ElementValidator {

    /**
     * Get the type of element to which this validator applies.
     *
     * @return The element type.
     */
    ElementType getElementType();

    /**
     * Get the type of element against which any content will be validated.
     *
     * <p>The returned element type will be different to the value returned
     * from {@link #getElementType()} if that element type is transparent.</p>
     *
     * @return The element type.
     */
    ElementType getValidatingElementType();

    /**
     * Prepare any information needed for validating the current element.
     * @param containingValidator
     */
    void open(ElementValidator containingValidator);

    /**
     * Make sure that the specified child is valid content for the element.
     *
     * @param content  The content that was found within the element.
     * @param required If the content is required
     * @return An indication as to whether the content was valid. True if it
     *         was, false if it wasn't and required was false, otherwise an
     *         exception is thrown.
     */
    boolean content(Content content, boolean required)
            throws ValidationException;

    /**
     * Make sure that all required elements have been seen.
     */
    void close() throws ValidationException;

    /**
     * Check to see whether this validator excludes the specified element.
     *
     * @param element The element to check to see if it is excluded.
     * @return True if the element is excluded, false otherwise.
     */
    boolean excludes(ElementType element);

    /**
     * Check whether this validator has any excludes.
     *
     * @return True if it has excludes false otherwise.
     */
    boolean hasExcludes();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
