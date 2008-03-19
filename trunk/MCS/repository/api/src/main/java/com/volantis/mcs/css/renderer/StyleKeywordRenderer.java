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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/StyleKeywordRenderer.java,v 1.6 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - The renderer for style
 *                              keyword values.
 * 13-May-02    Allan           VBM:2002042404 - Added check for existing
 *                              mapper in render().
 * 28-Jun-02    Paul            VBM:2002051302 - Made a singleton.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleException;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;

/**
 * Render a style value representing a keyword.
 */
public class StyleKeywordRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(StyleKeywordRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    StyleKeywordRenderer.class);
    /**
     * The reference to the single allowable instance of this class.
     */
    private static StyleKeywordRenderer singleton;

    // Initialise the static fields.
    static {
        // Always initialise to prevent a synchronization problem if we do it
        // lazily.
        singleton = new StyleKeywordRenderer();
    }

    /**
     * Get the single allowable instance of this class.
     *
     * @return The single allowable instance of this class.
     */
    public static StyleKeywordRenderer getSingleton() {
        return singleton;
    }

    /**
     * Protect the constructor to prevent any other instances being created.
     */
    protected StyleKeywordRenderer() {
    }

    /**
     * Render a StyleKeyword.
     *
     * @param value   the StyleKeyword to render
     * @param context the RendererContext within which to render the StyleKeyword
     */
    public void render(StyleKeyword value, RendererContext context)
            throws IOException {

        KeywordMapper mapper = context.getKeywordMapper();

        String css;
        if (mapper == null) {
            css = value.getStandardCSS();
        } else {
            css = mapper.getExternalString(value);
            if (css == null) {
                throw new StyleException(exceptionLocalizer.format(
                        "keyword-invalid",
                        value));
            }
        }
        Writer writer = context.getWriter();
        writer.write(css);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
