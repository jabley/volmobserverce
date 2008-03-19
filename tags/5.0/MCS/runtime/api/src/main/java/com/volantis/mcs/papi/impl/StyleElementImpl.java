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
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.StyleAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.runtime.styling.CSSCompilerBuilder;
import com.volantis.mcs.runtime.themes.ThemeStyleSheetCompilerFactory;
import com.volantis.mcs.themes.CSSParserMode;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.StringReader;

/**
 * The style element.
 */
public class StyleElementImpl
        extends AbstractExprElementImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(StyleElementImpl.class);

    /**
     * Buffer to hold the content seen inside this object.
     */
    private DOMOutputBuffer bodyContentBuffer;

    public StyleElementImpl() {

        //styling shouldn't be performed in style element.
        super(false);
    }

    // javadoc inherited
    protected int exprElementStart(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        StyleAttributes attributes = (StyleAttributes) papiAttributes;
        String typeAttribute = attributes.getType();
        if (!"text/css".equals(typeAttribute)) {
            throw new IllegalArgumentException(
                    "Style element must have type=\"text/css\"");
        }

        // Create an output buffer for the content of the object element.
        // This will enable us to grab it easily when the element ends and
        // either throw it away if the src attribute can be used or use it
        // instead if not.
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        bodyContentBuffer = (DOMOutputBuffer) pageContext.getProtocol().
                getOutputBufferFactory().createOutputBuffer();
        pageContext.pushOutputBuffer(bodyContentBuffer);

        return PROCESS_ELEMENT_BODY;
    }

    // javadoc inherited
    protected int exprElementEnd(
            MarinerRequestContext context,
            PAPIAttributes papiAttributes) throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        if (bodyContentBuffer == null) {
            throw new IllegalStateException("no content for style");
        }

        // Remove the output buffer pushed on in {@link #exprElementStart}
        pageContext.popOutputBuffer(bodyContentBuffer);

        // Get the textual CSS. Should be easy as <style> is PCDATA.
        String css = bodyContentBuffer.getPCDATAValue();
        if (css != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Inline css: " + css);
            }

            // Create a style sheet compiler.
            CSSCompilerBuilder builder = new CSSCompilerBuilder();
            builder.setProject(pageContext.getCurrentProject());
            builder.setBaseURL(pageContext.getBaseURL());
            builder.setStyleSheetCompilerFactory(
                    ThemeStyleSheetCompilerFactory.getDefaultInstance());
            builder.setParserMode(CSSParserMode.LAX);
            CSSCompiler cssCompiler = builder.getCSSCompiler();

            // Parse and compile the style sheet.
            CompiledStyleSheet compiledStyleSheet =
                    cssCompiler.compile(new StringReader(css), null);

            // adding style sheet to the list of style sheets 
            // that should be stored for current element 
            StylingEngine stylingEngine = pageContext.getStylingEngine();
            stylingEngine.addNestedStyleSheet(compiledStyleSheet);
        }

        return CONTINUE_PROCESSING;
    }

    // Javadoc inherited from super class.
    boolean hasMixedContent() {
        return true;
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
