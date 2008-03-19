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

package com.volantis.mcs.model.validation;

/**
 * Interface that should be implemented by classes within a model that
 * require validation.
 *
 * <p>Typically this will be implemented for those objects that can be the
 * root of the model and there will be some model specific mechanism for
 * traversing over the hierarchy of objects within the model in order to
 * validate them all.</p>
 *
 * @mock.generate
 */
public interface Validatable {

    /**
     * Validate the object within the specified context.
     *
     * <p>Implementations of this should validate all dependent objects.</p>
     *
     * @param context The context within which validation takes place.
     */
    void validate(ValidationContext context);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9992/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
