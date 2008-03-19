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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.StringWriter;

/**
 * This class is responsible for providing a base class for all
 * {@link StyleEmulationAttributeValueRenderer}'s that 'emulate'
 * a css property by actually writing the property-value pair as
 * actual css as a value of the style element.
 * <p>
 * This is neccessary as some devices that use the HTML 3.2 protocol
 * may support some CSS properties that cannot be emulated using
 * presentational attributes or elements (e.g border styling).  In such cases
 * the actual CSS needs to be inserted into the document using the style
 * attribute on the element requiring styling.
 */
public abstract class AbstractCSSPropertyRenderer
        implements StyleEmulationAttributeValueRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractCSSPropertyRenderer.class);

    /**
     * The name of the css property that is being rendered.
     */
    private final String propertyName;

    /**
     * Initialises a new instance with the supplied parameters.
     *
     * @param propertyName
     */
    protected AbstractCSSPropertyRenderer(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Provides a partial implementation of render.  Sub classes must
     * override {@link #setKeywordMapper} to complete the implementation.
     *
     * @param value the value that is to be renderered in an
     * appropriate manner.
     */
    public String render(StyleValue value) {
        StringWriter writer = new StringWriter();
        StyleSheetRenderer styleSheetRenderer =
                CSSStyleSheetRenderer.getSingleton();
        RendererContext context =
                new RendererContext(writer, styleSheetRenderer);

        // Don't forget to setup the css keyword mapper
        setKeywordMapper(context);

        String styleProperty = "";
        try{
            context.renderValue(value);
            styleProperty = context.getWriter().toString();
        } catch(IOException e) {
            // This should never really happen.  However, log a warning
            // if it does.
            logger.warn("unable-to-write-style-value",new Object[]{value});
        }

        return propertyName + ":" + styleProperty + ";";
    }

    /**
     * Sets the appropriate
     * {@link com.volantis.mcs.themes.mappers.KeywordMapper} with the supplied
     * <code>renderContext</code>
     *
     * @param renderContext the context in which the appropriate
     * {@link com.volantis.mcs.themes.mappers.KeywordMapper} will be set.
     */
    protected abstract void setKeywordMapper(RendererContext renderContext);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
