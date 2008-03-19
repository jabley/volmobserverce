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

import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.Set;

/**
 * The basic element validator, used for all normal elements.
 */
public class BasicElementValidator
        extends AbstractElementValidator {

    /**
     * Indicates whether the underlying validators are stateful, if they are
     * then when this prototype is asked to create a validator it creates a
     * new instance of itself along with a new instance of the content
     * validator. Otherwise it just returns itself. 
     */
    private final boolean stateful;

    /**
     * Initialise.
     *
     * @param elementType The type of element to which this validator applies.
     * @param validator   The validator for the content, may be null.
     * @param exclusions  The exclusions, may be null.
     * @param useAnywhere True if this element can be used anywhere but is not
     * @param canContainPCDATA
     */
    public BasicElementValidator(
            ElementType elementType, ContentValidator validator, Set exclusions,
            boolean useAnywhere, boolean canContainPCDATA) {
        super(elementType, validator, exclusions, useAnywhere, canContainPCDATA);

        stateful = validator.requiresPerElementState();
    }

    // Javadoc inherited.
    public ElementValidator createValidator() {
        if (stateful) {
            return new BasicElementValidator(
                    elementType, validator.createValidator(), exclusions,
                    useAnywhere, canContainPCDATA);
        } else {
            return this;
        }
    }

    // Javadoc inherited.
    public ElementType getValidatingElementType() {
        return getElementType();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
