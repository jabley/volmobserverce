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

import java.util.Set;

/**
 * Checks to see whether some content matches the content model represented
 * by this validator.
 * <p/>
 * <p>Some validators require state per element while some do not. If it does
 * not require state then a validator can be reused across many elements which
 * makes it much more efficient, otherwise a new instance is needed for each
 * element. This optional construction of a new instance is supported by
 * making use of the Prototype pattern. Each validator created from the
 * schema is a prototype that can be used to construct any number of real
 * instances that can be used to validate an element's content. Those
 * validators that do not require any per element state can simply return the
 * same instance.</p>
 *
 * @mock.generate
 */
public interface ContentValidator {

    /**
     * Create a new content validator that can be used to validate an element
     * instance.
     *
     * @return A new content validator.
     */
    ContentValidator createValidator();

    /**
     * Indicates whether this validator requires state per element or not.
     *
     * @return True if it does, false otherwise.
     */
    boolean requiresPerElementState();

    /**
     * Check to see what effect the content would have on validation within
     * the specified state.
     *
     * todo lots more documentation.
     *
     * @param content The content that is being checked.
     * @param state The current state.
     * @return The effect.
     */
    ValidationEffect check(Content content, ValidationState state);

    /**
     * Determine the set of the next allowable content types.
     * <p/>
     * <p>Populates the set with all content types that could be consumed by
     * this validator in its current state.
     *
     * @param nextExpectedContent The set of content to be updated.
     * @param state
     */
    boolean determineNextExpectedContent(
            Set nextExpectedContent, ValidationState state);

    /**
     * Reset any element state.
     */
    void reset();

    SatisfactionLevel checkSatisfactionLevel();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
