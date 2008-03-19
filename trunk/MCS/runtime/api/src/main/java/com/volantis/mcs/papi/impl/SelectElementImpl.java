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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.Precept;
import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.SelectAttributes;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;

/**
 * The select element.
 */
public class SelectElementImpl
        extends AbstractStyledElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    SelectElementImpl.class);

    // javadoc inherited
    public int styleElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        SelectAttributes attributes = (SelectAttributes) papiAttributes;
        Precept precept = Precept.MATCH_FIRST;
        Value expr = BooleanValue.TRUE;

        if (attributes.getPrecept() != null) {
            precept = Precept.literal(attributes.getPrecept());

            if (precept == null) {
                throw new PAPIException(exceptionLocalizer.format(
                        "unrecognized-precept-value",
                        attributes.getPrecept()));
            }
        }

        if (attributes.getExpr() != null) {
            ExpressionContext expressionContext =
                    ContextInternals.getEnvironmentContext(context).
                            getExpressionContext();

            try {
                expr = expressionContext.getFactory().
                        createExpressionParser().
                        parse(attributes.getExpr()).evaluate(expressionContext);
            } catch (ExpressionException e) {
                throw new PAPIException(e);
            }
        }

        pageContext.pushSelectState(new SelectState(precept, expr));

        return PROCESS_ELEMENT_BODY;
    }

    // javadoc inherited
    public int styleElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        pageContext.popSelectState();

        return CONTINUE_PROCESSING;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10562/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 01-Dec-05	10517/1	ibush	VBM:2005113001 Fix extra whitespace characters appearing

 28-Jun-05	8878/5	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 12-Aug-03	1008/5	philws	VBM:2003080805 Provide implementation of the select, when and otherwise PAPI elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 ===========================================================================
*/
