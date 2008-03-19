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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/AttributeSelectorRenderer.java,v 1.2 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Allan           VBM:2002042404 - Created. A renderer for
 *                              css2 attribute selectors.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.StyleException;
import com.volantis.mcs.themes.constraints.Constraint;
import com.volantis.mcs.themes.constraints.Contains;
import com.volantis.mcs.themes.constraints.ContainsWord;
import com.volantis.mcs.themes.constraints.ContainsWord;
import com.volantis.mcs.themes.constraints.EndsWith;
import com.volantis.mcs.themes.constraints.EndsWith;
import com.volantis.mcs.themes.constraints.Equals;
import com.volantis.mcs.themes.constraints.Equals;
import com.volantis.mcs.themes.constraints.MatchesLanguage;
import com.volantis.mcs.themes.constraints.Set;
import com.volantis.mcs.themes.constraints.StartsWith;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;

/**
 * A renderer for CSS Attribute selectors.
 */
public class AttributeSelectorRenderer implements SelectorRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AttributeSelectorRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            AttributeSelectorRenderer.class);

    /**
     * Render an AttributeSelector
     * @param selector the AttributeSelector to render
     * @param context the RendererContext used to render the selector
     */
    public void render(Selector selector,
                       RendererContext context)
        throws IOException {

        AttributeSelector attributeSelector = (AttributeSelector) selector;
        String attribute = attributeSelector.getName();
        String namespacePrefix = attributeSelector.getNamespacePrefix();
        Constraint constraint = attributeSelector.getConstraint();
        String value = constraint.getValue();

        if (attribute == null) {
            throw new StyleException(exceptionLocalizer.format(
                        "attribute-selector-null-attribute"));
        }

        Writer writer = context.getWriter();
        writer.write('[');
        if (namespacePrefix != null && namespacePrefix.length() > 0) {
            writer.write(namespacePrefix);
            writer.write('|');
        }
        writer.write(attribute);

        // If the constraint doesn't need a value then we can ignore it,
        // otherwise add the appropriate operator and value.
        if (constraint.requiresValue()) {
            if (value == null) {
                throw new StyleException(exceptionLocalizer.format(
                            "null-attribute-selector"));
            }

            if (constraint instanceof MatchesLanguage) {
                writer.write("|=");
            } else if (constraint instanceof ContainsWord) {
                writer.write("~=");
            } else if (constraint instanceof Contains) {
                writer.write("*=");
            } else if (constraint instanceof EndsWith) {
                writer.write("$=");
            } else if (constraint instanceof StartsWith) {
                writer.write("^=");
            } else if (constraint instanceof Equals) {
                writer.write("=");
            } else {
                // The action was null or invalid
                if (logger.isDebugEnabled()){
                     logger.debug("Invalid attribute selector constraint:"
                             + constraint);
                }
                throw new StyleException(exceptionLocalizer.format(
                            "invalid-attribute-selector-action"));
            }
            writer.write("\"");
            writer.write(value);
            writer.write("\"");
        }
        writer.write(']');
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/1	adrianj	VBM:2005083007 CSS renderer using new model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 21-Jan-04	2592/1	doug	VBM:2003112712 Implementation of the ThemeElementRenderer and ThemeElementParser interfaces

 ===========================================================================
*/
