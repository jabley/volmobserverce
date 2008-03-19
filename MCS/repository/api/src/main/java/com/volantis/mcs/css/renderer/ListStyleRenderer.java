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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/ListStyleRenderer.java,v 1.5 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - Created. A renderer for 
 *                              css listStyle style properties.
 * 13-May-02    Allan           VBM:2002042404 - Modified render() to ensure
 *                              that the shorthand is only used when all
 *                              values are available - along with the other 
 *                              existing checks.
 * 14-May-02    Allan           VBM:2002042404 - Simplified implementation to
 *                              more easily accomodate error handling during
 *                              the rendering process. render() virtually
 *                              re-written. Removed addToPropertyGroup().
 * 22-May-02    Doug            VBM:2002051701 - Modified the method
 *                              getKeywordMapper() to take a RendererContext
 *                              argument and ensured that the returned 
 *                              KeyordMapper is obtained via a 
 *                              KeywordMapperFactory object.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.themes.StyleException;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 * The css2 renderer for the "listStyle" property.
 */
public class ListStyleRenderer extends PropertyRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ListStyleRenderer.class);

    /**
     * Get the KeywordMapper for list style image properties
     * @param context the RendererContext
     * @return a KeywordMapper
     */
    protected 
        KeywordMapper getListStyleImageKeywordMapper(RendererContext context) {
       
        return 
	  context.getKeywordMapper(StylePropertyDetails.LIST_STYLE_IMAGE);
    }

  
    /**
     * Get the KeywordMapper for list style image properties
     * @param context the RendererContext
     * @return a KeywordMapper
     */
    protected KeywordMapper
        getListStylePositionKeywordMapper(RendererContext context) {

      return 
	context.getKeywordMapper(StylePropertyDetails.LIST_STYLE_POSITION);
    }

  
    /**
     * Get the KeywordMapper for list style image properties
     * @param context the RendererContext
     * @return a KeywordMapper
     */
    protected 
        KeywordMapper getListStyleTypeKeywordMapper(RendererContext context) {
        return 
	  context.getKeywordMapper(StylePropertyDetails.LIST_STYLE_TYPE);
    }

  

    // javadoc inherited
    public String getName() {
        throw new UnsupportedOperationException(); // renderer renders itself
    }

    // javadoc inherited
    public StyleValue getValue(StyleProperties properties) {
        throw new UnsupportedOperationException(); // renderer renders itself
    }

    public PropertyValue getPropertyValue(StyleProperties properties) {
        throw new UnsupportedOperationException(); // renderer renders itself
    }

    // javadoc inherited
    public KeywordMapper getKeywordMapper(RendererContext context) {
        throw new UnsupportedOperationException(); // renderer renders itself
    }

    /**
     * Get the list-style-image property.
     */
    protected PropertyValue getListStyleImage(StyleProperties properties,
                                           RendererContext context) {
        return properties.getPropertyValue(
                StylePropertyDetails.LIST_STYLE_IMAGE);
    }

    /**
     * Get the list-style-position property.
     */
    protected PropertyValue getListStylePosition(StyleProperties properties,
                                              RendererContext context) {
        return properties.getPropertyValue(
                StylePropertyDetails.LIST_STYLE_POSITION);
    }

    /**
     * Get the list-style-type property.
     */
    protected PropertyValue getListStyleType(StyleProperties properties,
                                          RendererContext context) {
        return properties.getPropertyValue(
                StylePropertyDetails.LIST_STYLE_TYPE);
    }

    /**
     * Render list style properties
     * @param properties the StyleProperties containing the list style 
     * properties if any.
     * @param context the RendererContext in which to render the background.
     * @throws IOException if the is a problem writing the rendered property.
     */
    public void render (StyleProperties properties, RendererContext context)
        throws IOException {

        PropertyValue[] propertyValues = new PropertyValue[3];
        propertyValues[0] = getListStyleImage(properties, context);
        propertyValues[1] = getListStyleType(properties, context);
        propertyValues[2] = getListStylePosition(properties, context);

        StyleValue[] values = getStyleValues(propertyValues);

        // Decide if we should use the list-style shortcut.
        // We only use it if the output css version supports it and all the
        // values are available and have equal priority.
        boolean useShortcut = false;
        final CSSVersion cssVersion = context.getCSSVersion();
        if (cssVersion == null ||
                cssVersion.supportsShorthand(StyleShorthands.LIST_STYLE)) {
          boolean allValuesAvailable = values[0] != null &&
                  values[1] != null && values[2] != null;
          boolean allPrioritiesEqual = allValuesAvailable &&
                  propertyValues[0].getPriority() == propertyValues[1].getPriority() &&
                  propertyValues[1].getPriority() == propertyValues[2].getPriority();
          useShortcut = allPrioritiesEqual;
        }

        Writer writer = context.getWriter ();

        if(useShortcut) {
            // If all values are equal then they must be inherit so we can
            // just use list-style:inherit.
            boolean allValuesEqual = values[0].equals(values[1]) &&
                    values[1].equals(values[2]);

            if(allValuesEqual) {
                writer.write("list-style:");
                context.renderValue(values[0]);
                context.renderPriority(propertyValues[0].getPriority());
                writer.write(';');
                return;
            }
        }

        // Provide a means to assoicate the style values with their css 
        //property names.
        HashMap nameMap = new HashMap(3);
        HashMap mapperMap = new HashMap(3);

        if(values[0] !=null) {
            nameMap.put(identityHashCode(values[0]), "list-style-image");
            mapperMap.put(identityHashCode(values[0]),
                    getListStyleImageKeywordMapper(context));
        }
        if(values[1] !=null) {
            nameMap.put(identityHashCode(values[1]), "list-style-type");
            mapperMap.put(identityHashCode(values[1]),
                    getListStyleTypeKeywordMapper(context));
        }
        if(values[2] !=null) {
            nameMap.put(identityHashCode(values[2]), "list-style-position");
            mapperMap.put(identityHashCode(values[2]),
			  getListStylePositionKeywordMapper(context));
        }

        if(useShortcut) {
            writer.write("list-style:");
        }
          
        int renderLength = 0;
        for (int i = 0; i < values.length; i++) {
            renderLength = context.getRenderLength();
            if (values[i] != null) {
                try {
                    if(!useShortcut) {
                        String name = (String)
                                nameMap.get(identityHashCode(values[i]));
                        writer.write(name);
                        writer.write(':');
                    }
                    KeywordMapper mapper = (KeywordMapper)
                            mapperMap.get(identityHashCode(values[i]));
                    context.setKeywordMapper(mapper);
                    context.renderValue(values[i]);
                    if(useShortcut && i < (values.length - 1)) {
                        writer.write(' ');
                    } else {
                        context.renderPriority(propertyValues[i].getPriority());
                        writer.write(';');
                    }
                }
                catch(StyleException e) {
                    if(logger.isDebugEnabled()){
                        logger.debug("Problem rendering a " + values[i]);
                    }
                    logger.info("unexpected-exception", e.getLocalizedMessage());
                    if(renderLength!=context.getRenderLength()) {
                        context.rewindWriter(renderLength);
                    }
                    // continue rendering.
                }
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/2	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10830/1	pduffin	VBM:2005121405 Allowed keyword mapper used by renderer to be overridden by CSSVersion, created keyword mapper for vertical-align

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/9	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/7	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
