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
package com.volantis.mcs.xdime.diselect;

import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.schema.DISelectElements;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Implementation of an &lt;otherwise&gt; element.
 */
public class OtherwiseElementImpl extends XDIMESelectBodyElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    OtherwiseElementImpl.class);

    public OtherwiseElementImpl(XDIMEContextInternal context) {
        super(DISelectElements.OTHERWISE, AttributeUsage.ILLEGAL, context);
    }

    // Javadoc inherited.
    protected boolean evaluateExpression(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // retrieve the value of the expr attribute (no namespace)
        String expr = getAttribute(XDIMEAttribute.EXPR, attributes);
        if (expr != null) {
            throw new IllegalStateException(
                    "Expr attribute is illegal on " + getElementType());
        }

        // We should only evaluate the otherwise expression if none of the
        // select's when statements matched (can find this out from SelectState)
        SelectState state = getState(context);

        if (state.isOtherwiseExecuted()) {
            throw new XDIMEException(
                    EXCEPTION_LOCALIZER.format("too-many-otherwises"));
        }

        final boolean matched = !state.isMatched();
        if (matched) {
            // Record the fact that this otherwise has been executed
            state.setOtherwiseExecuted();
        }

        return matched;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 12-Sep-05	9415/6	emma	VBM:2005072710 Fixing element stack mistake

 09-Sep-05	9415/4	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 25-Jul-05	9060/1	tom	VBM:2005071304 Interim Commit so Emma can see the changes we have made

 ===========================================================================
*/
