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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.LabelAttributes;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.menu.MenuInternals;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The label element.
 */
public class LabelElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    LabelElementImpl.class);

    /**
     * The output buffer to which content will be directed.
     */
    OutputBuffer content = null;

    // javadoc inherited
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {
        LabelAttributes attributes = (LabelAttributes) papiAttributes;

        // A new output buffer is required to capture the content of the label
        // element (the content model indicates the use of "phrase" group
        // elements and text)
        // NB: The allocation mechanism must be compatible with the
        //     de-allocation mechanism used in
        //     {@link MenuElement#releaseLabelOutputBuffers}
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        content = pageContext.getProtocol().
                getOutputBufferFactory().createOutputBuffer();

        pageContext.pushOutputBuffer(content);

        try {
            MenuModelBuilder builder = pageContext.getMenuBuilder();

            String title = attributes.getTitle();

            // Action the builder to construct the label in the menu model
            builder.startLabel();

            // Set the stylistic information in the model for this element.
            MenuInternals.setElementDetails(builder, attributes,
                    pageContext.getStylingEngine().getStyles());

            // The label has events, style and pane, so handle these
            MenuInternals.setEvents(builder, attributes, pageContext);

            // Set the pane attribute
            MenuInternals.setPane(builder, attributes.getPane(), pageContext);

            // Handle the title
            if (title != null) {
                builder.setTitle(title);
            }

            // Set up the text
            builder.startText();
            builder.setText(content);
            builder.endText();

            // Complete the label
            builder.endLabel();
        } catch (BuilderException e) {
            throw new PAPIException(e);
        }

        return PROCESS_ELEMENT_BODY;
    }

    // javadoc inherited
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes)
            throws PAPIException {
        if (content == null) {
            throw new PAPIException(
                    exceptionLocalizer.format("label-requires-content"));
        } else {
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);

            // Remove the output buffer pushed on in {@link #exprElementStart}
            pageContext.popOutputBuffer(content);
        }

        return CONTINUE_PROCESSING;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 11-May-04	4246/3	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 10-May-04	4248/1	geoff	VBM:2004050708 Enhance Menu Support: Integration Bugs: Simple problems with rollover images

 29-Apr-04	4091/5	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 27-Apr-04	4010/2	claire	VBM:2004042208 Pane handling in PAPI for enhanced menus

 02-Apr-04	3429/2	philws	VBM:2004031502 MenuLabelElement implementation

 ===========================================================================
*/
