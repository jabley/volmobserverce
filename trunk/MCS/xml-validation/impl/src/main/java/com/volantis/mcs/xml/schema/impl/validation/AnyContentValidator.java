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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.schema.impl.validation;

import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.Set;

/**
 * Validator that accepts any element content (but not PCDATA).
 *
 * <p>This validator is stateless and so only a single instance of this needs
 * to be created.</p>
 */
public class AnyContentValidator
        extends StatelessValidator {

    public static final Content ANY = new Content() {
        public String toString() {
            return "(ANY)";
        }
    };

    public ValidationEffect check(Content content, ValidationState state) {

        if (content instanceof ElementType) {
            return ValidationEffect.CONSUMED_SATISFIED;
        } else {
            return ValidationEffect.WOULD_FAIL;
        }
    }

    public boolean determineNextExpectedContent(
            Set nextExpectedContent, ValidationState state) {

        nextExpectedContent.add(ANY);

        return false;
    }

    public SatisfactionLevel checkSatisfactionLevel() {
        return SatisfactionLevel.COMPLETE;
    }
}
