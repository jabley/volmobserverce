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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.StyleableFormat;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.Styles;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.synergetics.log.LogDispatcher;

public class FormatStylingEngineImpl
        implements FormatStylingEngine {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
             LocalizationFactory.createLogger(FormatStylingEngineImpl.class);

    private final StylingEngine stylingEngine;

    private final SingleNamespaceAttributes attributes;

    public FormatStylingEngineImpl(StylingEngine stylingEngine) {
        this.stylingEngine = stylingEngine;
        this.attributes = new SingleNamespaceAttributes(null);
    }

    // Javadoc inherited.
    public Styles startStyleable(StyleableFormat styleable, String styleClass) {

        int identifier = System.identityHashCode(styleable);

        attributes.setAttributeValue(
                FormatStylingConstants.FORMAT_IDENTIFIER,
                String.valueOf(identifier));

        attributes.setAttributeValue(
                FormatStylingConstants.FORMAT_CLASS, styleClass);

        stylingEngine.startElement(
                getNamespace(styleable),
                getLocalName(styleable),
                attributes);

        return stylingEngine.getStyles();
    }

    // Javadoc inherited.
    public void endStyleable(StyleableFormat styleable) {
        stylingEngine.endElement(
                getNamespace(styleable),
                getLocalName(styleable));
    }

    private String getNamespace(StyleableFormat styleable) {
        if (styleable instanceof Pane) {
            // For backwards compatibility, Pane must use the default namespace
            // so that CSS rules written using a type selector of "pane" are
            // picked up and applied for Panes.
            return XDIMESchemata.CDM_NAMESPACE;
        } else {
            // Other formats use a format specific namespace to ensure that
            // users cannot write CSS rules which match them.
            return FormatStylingConstants.FORMAT_NAMESPACE;
        }
    }

    private String getLocalName(StyleableFormat styleable) {
        if (styleable instanceof Pane) {
            // For backwards compatibility, Pane must use the name "pane"
            // so that CSS rules written using a type selector of "pane" are
            // picked up and applied for Panes.
            return "pane";
        } else {
            // Other formats use a local name which is derived from the format
            // name. These will never match any CSS rules since they are used
            // with a format specific namespace.
            return styleable.getTypeName();
        }
    }

    // Javadoc inherited.
    public void pushStyleSheet(CompiledStyleSheet styleSheet) {
        stylingEngine.pushStyleSheet(styleSheet);

        if(logger.isDebugEnabled()) {
            logger.debug("IBDEBUG:: pushStyleSheet :: "+this);
        }
    }

    // Javadoc inherited.
    public void popStyleSheet(CompiledStyleSheet styleSheet) {
        stylingEngine.popStyleSheet(styleSheet);
    }

    public void pushPropertyValues(ImmutablePropertyValues propertyValues) {
        stylingEngine.pushPropertyValues(propertyValues);
    }

    public void popPropertyValues(ImmutablePropertyValues propertyValues) {
        stylingEngine.popPropertyValues(propertyValues);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 01-Nov-05	10046/3	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 31-Oct-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 01-Nov-05	10046/3	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 31-Oct-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
