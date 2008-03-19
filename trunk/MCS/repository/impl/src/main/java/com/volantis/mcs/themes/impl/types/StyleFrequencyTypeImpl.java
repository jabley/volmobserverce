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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.impl.types;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.themes.StyleFrequency;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.types.StyleFrequencyType;
import com.volantis.mcs.themes.types.StyleTypeVisitor;

public class StyleFrequencyTypeImpl extends AbstractSingleStyleType
    implements StyleFrequencyType {

    public StyleFrequencyTypeImpl() {
        super(StyleValueType.FREQUENCY);
    }

    public void accept(StyleTypeVisitor visitor) {
        visitor.visitStyleFrequencyType(this);
    }

    protected void validateSupportedValue(
            ValidationContext context, StyleValue value) {

        StyleFrequency frequency = (StyleFrequency) value;
        // Nothing else to check.
    }
}
