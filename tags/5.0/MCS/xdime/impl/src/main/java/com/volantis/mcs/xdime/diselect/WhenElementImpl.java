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

import com.volantis.mcs.expression.Precept;
import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.schema.DISelectElements;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The Implementation of a &lt;when&gt; element.
 */
public class WhenElementImpl extends XDIMESelectBodyElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    WhenElementImpl.class);

    public WhenElementImpl(XDIMEContextInternal context) {
        super(DISelectElements.WHEN, AttributeUsage.MANDATORY, context);
    }

    // Javadoc inherited.
    protected boolean evaluateExpression(XDIMEContextInternal context,
            String expressionString) throws XDIMEException {

        // default value is false
        boolean result = false;

        // We should only evaluate the when's expression if the select state
        // indicates that this when can be evaluated.
        SelectState state = getState(context);

        if (state.isOtherwiseExecuted()) {
            throw new XDIMEException(
                    EXCEPTION_LOCALIZER.format("when-preceed-otherwise"));
        } else if (!state.isMatched() ||
                (state.getPrecept() == Precept.MATCH_EVERY)) {
            // Should evaluate the expressions since either every precept can
            // be matched or no matches have yet been found
            result = super.evaluateExpression(context, expressionString);

            if (result == true) {
                // Ensure that the select state records the fact that a match
                // has been found
                state.setMatched();
            }
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 12-Sep-05	9415/6	emma	VBM:2005072710 Fixing element stack mistake

 09-Sep-05	9415/4	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 25-Jul-05	9060/1	tom	VBM:2005071304 Interim Commit so Emma can see the changes we have made

 ===========================================================================
*/
