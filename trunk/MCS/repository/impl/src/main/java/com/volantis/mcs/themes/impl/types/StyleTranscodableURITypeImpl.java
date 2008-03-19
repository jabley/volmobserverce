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
package com.volantis.mcs.themes.impl.types;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.types.StyleTranscodableURIType;
import com.volantis.mcs.themes.types.StyleTypeVisitor;

/**
 * Implementation of the transcodable URI type.
 */
public class StyleTranscodableURITypeImpl
    extends AbstractSingleStyleType
    implements StyleTranscodableURIType {

    public StyleTranscodableURITypeImpl() {
        super(StyleValueType.TRANSCODABLE_URI);
    }

    // javadoc inherited
    public void accept(final StyleTypeVisitor visitor) {
        visitor.visitStyleTranscodableURIType(this);
    }

    // javadoc inherited
    protected void validateSupportedValue(
            final ValidationContext context, final StyleValue value) {

        final StyleTranscodableURIType uri = (StyleTranscodableURIType) value;
        // Nothing else to check.
    }
}
