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
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.Styles;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;

/**
 * This class should be subclassed by all implementations of elements that
 * should be styled. It ensures that the styling engine start and end element
 * methods are always called symmetrically, and that the MCSAttributes
 * corresponding to the element are annotated with the Styles that should be
 * applied to this element.
 */
public abstract class AbstractStyledElementImpl
        extends AbstractElementImpl {


    // Styles to apply to this element
    Styles styles;

    /**
     * Flag which indicates whether the styling should be
     * performed or not.
     */
    final boolean isStylingEnabled;

    /**
     * @param isStylingEnabled Flag which indicates whether the styling
     *                         should be performed or not.
     */
    AbstractStyledElementImpl(boolean isStylingEnabled) {
        this.isStylingEnabled = isStylingEnabled;
    }

    AbstractStyledElementImpl() {
        this(true);
    }

    /**
     * This is called at the start of a styled PAPI element. It calls
     * StylingEngine#startElement and then sets the Styles member of the
     * protocol attributes (if they're non null) to those calculated by the
     * call to the styling engine.
     *
     * @param context        The MarinerRequestContext within which this
     *                       element is being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the
     *                       implementation of PAPIElement.
     * @return CONTINUE_PROCESSING or ABORT_PROCESSING.
     * @throws PAPIException If there was a problem processing the element.
     */
    public abstract int styleElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException;

    /**
     * Called at the end of a styled PAPI element and notifies the
     * StylingEngine that the end of the element has been reached.
     * <p/>
     * If the styleElementStart method was called then this method will also be
     * called unless an Exception occurred during the processing of the body.
     * </p>
     *
     * @param context        The MarinerRequestContext within which this
     *                       element is being processed.
     * @param papiAttributes The implementation of PAPIAttributes which
     *                       contains the attributes specific to the
     *                       implementation of PAPIElement.
     * @return CONTINUE_PROCESSING or ABORT_PROCESSING.
     * @throws PAPIException If there was a problem processing the element.
     */
    public abstract int styleElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException;

    // javadoc inherited
    public int elementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        // Get the page context from the request
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        if (pageContext.insideXDIMECPElement()) {
            throw new PAPIException(EXCEPTION_LOCALIZER.format(
                    "element-not-allowed-inside-xdimecp", papiAttributes.getElementName()));
        }

        if (this.isStylingEnabled) {
            StylingEngine stylingEngine = pageContext.getStylingEngine();
            stylingEngine.startElement(XDIMESchemata.CDM_NAMESPACE,
                    papiAttributes.getElementName(),
                    (Attributes) papiAttributes.getGenericAttributes());

            setStyles(stylingEngine.getStyles());

            // annotate the MCSAttributes with the styles for this element
            MCSAttributes attributes = getMCSAttributes();
            if (attributes != null) {
                attributes.setStyles(stylingEngine.getStyles());
            }
        }

        return styleElementStart(context, papiAttributes);
    }

    // javadoc inherited
    public int elementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {

        int returnCode = styleElementEnd(context, papiAttributes);

        // Get the page context from the request
        MarinerPageContext pageContext
                = ContextInternals.getMarinerPageContext(context);

        if (this.isStylingEnabled) {
            StylingEngine stylingEngine = pageContext.getStylingEngine();
            // Notify the styling engine that the element end has been reached
            stylingEngine.endElement(XDIMESchemata.CDM_NAMESPACE,
                    papiAttributes.getElementName());
        }

        return returnCode;
    }

    /**
     * Gets the protocol attributes for this element. Used when annotating the
     * MCSAttributes with their appropriate Styles.
     *
     * @return the <code>MCSAttributes</code> belonging to this element.
     */
    MCSAttributes getMCSAttributes() {
        return null;
    }

    /**
     * Set the styles object to newStyles.
     *
     * @param newStyles
     */
    protected void setStyles(Styles newStyles) {
        styles = newStyles;
    }

    /**
     * Return current styles object.
     *
     * @return styles object, may be null.
     */
    protected Styles getStyles() {
        return styles;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 28-Jun-05	8878/5	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 ===========================================================================
*/
