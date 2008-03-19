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
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.schema.DISelectElements;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.atomic.BooleanValue;

/**
 * Implementation of the &lt;select&gt; element.
 */
public class SelectElementImpl extends DISelectElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    SelectElementImpl.class);

    /**
     * The state of this &lt;select&gt; element.
     */
    private SelectState state;

    public SelectElementImpl(XDIMEContextInternal context) {
        super(DISelectElements.SELECT, context, AttributeUsage.OPTIONAL);
    }

    // Javadoc inherited.
    public XDIMEResult exprElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // store the precept if there is one, defaulting to MATCH_FIRST
        // if there is no precept attribute
        Precept precept = calculatePrecept(attributes);

        // The select element's expr attribute must have evaluated to true
        // in order to get to this point, so create state with expr set to TRUE.
        state = new SelectState(precept, BooleanValue.TRUE);

        // We should process a select element's body if its sel:expr attribute
        // evaluates to true. This must be the case if we've reached this point
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    /**
     * Calculates the precept that should be applied for this &lt;select&gt;
     * element. If no precept attribute has been provided in the attributes,
     * then the default value will be {@link Precept#MATCH_FIRST}.
     *
     * @param attributes        the attributes for this element
     * @return Precept that has been calculated for this element
     */
    private Precept calculatePrecept(XDIMEAttributes attributes)
            throws XDIMEException {

        Precept precept = null;

        String preceptAttributeValue = attributes.getValue(
                XDIMESchemata.DISELECT_NAMESPACE, "precept");

        // default is match first
        precept = Precept.MATCH_FIRST;
        if (preceptAttributeValue != null) {
            Precept preceptAttribute = Precept.literal(preceptAttributeValue);

            // throw an exception if the precept was not an expected one
            if (precept == null) {
                throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                        "unrecognized-precept-value",
                        preceptAttributeValue));
            }

            // we only care if the result is match every since match first is
            // assigned by default
            if (preceptAttribute != null
                    && preceptAttribute.equals(Precept.MATCH_EVERY)) {
                precept = Precept.MATCH_EVERY;
            }
        }
        return precept;
    }

    // Javadoc inherited.
    public XDIMEResult exprElementEnd(XDIMEContextInternal context)
            throws XDIMEException {
        return XDIMEResult.CONTINUE_PROCESSING;
    }

    /**
     * Gets the state of this &lt;select&gt; element.
     *
     * @return the state of the select element
     */
    public SelectState getState() {
        return state;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 09-Sep-05	9415/5	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 28-Jul-05	9129/1	emma	VBM:2005071304 Modifications after review

 27-Jul-05	9060/3	tom	VBM:2005071304 Added Sel Select

 ===========================================================================
*/
