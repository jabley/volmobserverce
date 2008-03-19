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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/FontRenderer.java,v 1.8 2002/05/23 09:17:01 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - Created. A renderer for 
 *                              css font style properties.
 * 13-May-02    Allan           VBM:2002042404 - Modified render() to ensure
 *                              that the shorthand is only used when all
 *                              values are available and to include 
 *                              font-size-adjust and font-stretch. 
 * 13-May-02    Allan           VBM:2002042404 - Modified render() to bring
 *                              priorities into the equation and set 
 *                              line-height when available with font-size.
 * 13-May-02    Allan           VBM:2002051305 - Added support for 
 *                              mariner-system-font property. Modified 
 *                              render() for this change and added
 *                              getMarinerSystemKeywordMapper().
 * 14-May-02    Allan           VBM:2002042404 - Modified render() to render
 *                              font-size separately from the other properties
 *                              to ensure that it is rendered before 
 *                              font-family in the shorthand version.
 * 14-May-02    Allan           VBM:2002042404 - Appended a ; after the
 *                              inherit keyword and priority are rendered in
 *                              render(). Added full support for line-height
 *                              as a genuine font property. Simplified render()
 *                              and added error handling.
 * 15-May-02    Allan           VBM:2002042404 - Pretty much a complete 
 *                              re-write to provide more flexibility over
 *                              the rendering of the component parts of the
 *                              shorthand renderer. This renderer now uses
 *                              specific renderers for rendering the properties
 *                              that comprise the font shorthand.
 * 22-May-02    Doug            VBM:2002051701 - Modified the method
 *                              getKeywordMapper() to take a RendererContext
 *                              argument and ensured that the returned 
 *                              KeyordMapper is obtained via a 
 *                              KeywordMapperFactory object.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer.shorthand;

import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.mappers.CSSPropertyNameMapper;
import com.volantis.mcs.themes.values.FontShorthandValue;
import com.volantis.styling.properties.StyleProperty;

import java.io.IOException;
import java.io.Writer;

/**
 * The css2 renderer for the "font" property.
 */
public class FontRenderer
        extends ShorthandPropertyRenderer {

    public FontRenderer() {
        super(StyleShorthands.FONT);
    }

    // Javadoc inherited.
    protected boolean renderShorthand(
            RendererContext context, StyleShorthand shorthand,
            StyleProperties properties) throws IOException {

        FontShorthandValue shorthandValue = (FontShorthandValue)
                properties.getShorthandValue(shorthand);
        if (shorthandValue == null) {
            return false;
        }

        CSSPropertyNameMapper mapper =
                CSSPropertyNameMapper.getDefaultInstance();

        Writer writer = context.getWriter();

        Priority priority = shorthandValue.getPriority();
        StyleValue systemFont = shorthandValue.getSystemFont();
        if (systemFont != null) {

            writer.write(mapper.getExternalString(shorthand));
            writer.write(':');
            renderValue(systemFont, context);
            context.renderPriority(priority);
            writer.write(";");

            int count = shorthandValue.getCount();
            for (int i = 0; i < count; i += 1) {
                StyleValue value = shorthandValue.getValue(i);
                if (value != null) {
                    StyleProperty property = shorthandValue.getProperty(i);
                    writer.write(mapper.getExternalString(property));
                    writer.write(":");
                    renderValue(value, context);
                    context.renderPriority(priority);
                    writer.write(";");
                }
            }
        } else {
            super.renderShorthand(context, shorthand, properties);
        }

        return true;
    }

    protected String selectSeparator(String separator, int index) {
        if (index == FontShorthandValue.LINE_HEIGHT_INDEX) {
            return "/";
        } else {
            return separator;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/8	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
