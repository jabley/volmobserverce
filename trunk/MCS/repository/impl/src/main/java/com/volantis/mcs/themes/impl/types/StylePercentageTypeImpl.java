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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.impl.types;

import com.volantis.mcs.themes.types.StylePercentageType;
import com.volantis.mcs.themes.types.StyleTypeVisitor;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 *
 */

public class StylePercentageTypeImpl
        extends AbstractSingleStyleType
    implements StylePercentageType {

    public StylePercentageTypeImpl() {
        super(StyleValueType.PERCENTAGE);
    }

    public void accept(StyleTypeVisitor visitor) {
        visitor.visitStylePercentageType(this);
    }

    protected void validateSupportedValue(
            ValidationContext context, StyleValue value) {

        StylePercentage percentage = (StylePercentage) value;
        // Nothing else to check.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 28-Oct-05	9965/6	ianw	VBM:2005101811 Fix file locations

 27-Oct-05	9965/3	ianw	VBM:2005101811 interim commit

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 ===========================================================================
*/
