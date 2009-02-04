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

public class MultipleTypeValidator
        extends AbstractContentTypeValidator {

    private final Set validContent;

    public MultipleTypeValidator(Set validTypes) {
        this.validContent = validTypes;
    }

    public ValidationEffect check(Content content, ValidationState state) {
        return (validContent.contains(content)
                ? ValidationEffect.CONSUMED_SATISFIED
                : ValidationEffect.WOULD_FAIL);
    }

    public boolean determineNextExpectedContent(
            Set nextExpectedContent, ValidationState state) {

        nextExpectedContent.addAll(validContent);

        return false;
    }

    public void addTo(Set validContent) {
        validContent.addAll(this.validContent);
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
