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
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.themes.*;
import com.volantis.mcs.themes.types.StyleTypeVisitor;
import com.volantis.mcs.themes.types.StyleFractionType;
import com.volantis.mcs.themes.types.StyleType;

public class StyleFractionTypeImpl extends AbstractSingleStyleType
    implements StyleFractionType {

    StyleType numeratorType;
    StyleType denominatorType;

    public StyleFractionTypeImpl() {
        super(StyleValueType.FRACTION);
    }

    public void accept(StyleTypeVisitor visitor) {
        visitor.visitStyleFractionType(this);
    }

    protected void validateSupportedValue(
            ValidationContext context, StyleValue value) {

        StyleFraction fraction = (StyleFraction) value;

        StyleValue numeratorValue = fraction.getNumerator();
        StyleValue denominatorValue = fraction.getDenominator();

        Step step;
        step = context.pushPropertyStep("numerator");
        if (numeratorValue == null) {
            context.addDiagnostic(value, DiagnosticLevel.ERROR,
                    context.createMessage("pair-first-unspecified"));
        } else {
            numeratorType.validate(context, numeratorValue);
        }
        context.popStep(step);

        if (denominatorValue != null) {
            step = context.pushPropertyStep("second");
            denominatorType.validate(context, denominatorValue);
            context.popStep(step);
        }
    }

    public StyleType getNumeratorType() {
        return numeratorType;
    }

    public StyleType getDenominatorType() {
        return denominatorType;
    }
}
