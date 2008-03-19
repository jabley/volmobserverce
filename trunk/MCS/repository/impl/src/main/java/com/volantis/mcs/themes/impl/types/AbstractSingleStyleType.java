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

package com.volantis.mcs.themes.impl.types;

import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;

import java.util.Set;

public abstract class AbstractSingleStyleType
        extends StyleTypeImpl {

    private final StyleValueType supportedStyleValueType;

    protected AbstractSingleStyleType(StyleValueType supportedStyleValueType) {
        this.supportedStyleValueType = supportedStyleValueType;
    }

    protected void validateValue(ValidationContext context, StyleValue value) {
        StyleValueType styleValueType = value.getStyleValueType();
        if (styleValueType == supportedStyleValueType) {
            validateSupportedValue(context, value);
        } else {
            context.addDiagnostic(value, DiagnosticLevel.ERROR,
                    context.createMessage("unexpected-style-type",
                            supportedStyleValueType,
                            styleValueType));
        }
    }

    protected abstract void validateSupportedValue(
            ValidationContext context, StyleValue value);

    public StyleType getMatchingStyleType(StyleValueType valueType) {
        return valueType == supportedStyleValueType ? this : null;
    }

    public void addDescription(Set set) {
        set.add(supportedStyleValueType.getType());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 ===========================================================================
*/
