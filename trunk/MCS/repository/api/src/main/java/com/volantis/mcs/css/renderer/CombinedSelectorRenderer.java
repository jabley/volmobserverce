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
 * $Header: /src/voyager/com/volantis/mcs/css/renderer/css2/CombinedSelectorRenderer.java,v 1.2 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Allan           VBM:2002042404 - Created. A renderer for
 *                              css2 combined selectors.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleException;
import com.volantis.mcs.themes.Subject;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * A renderer for CSS Combined selectors.
 */
public class CombinedSelectorRenderer implements SelectorRenderer {
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(CombinedSelectorRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(CombinedSelectorRenderer.class);

    /**
     * Map associating the various supported combinators with their textual
     * equivalent from the CSS2/3 specs.
     */
    private static final Map COMBINATOR_REPRESENTATIONS = new HashMap();
    static {
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.CHILD, " > ");
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.DESCENDANT, " ");
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.DIRECT_ADJACENT, " + ");
        COMBINATOR_REPRESENTATIONS.put(CombinatorEnum.INDIRECT_ADJACENT, " ~ ");
    }

    /**
     * Render an CombinedSelector
     * @param selector the CombinedSelector to render
     * @param context the RendererContext used to render the selector
     */
    public void render(Selector selector,
                       RendererContext context)
        throws IOException {

        CombinedSelector combined = (CombinedSelector) selector;
        Subject subject = combined.getSubject();
        SelectorSequence contextual = combined.getContext();
        CombinatorEnum combinator = combined.getCombinator();

        context.renderSelector(contextual);

        Writer writer = context.getWriter();

        String combinatorText =
                (String) COMBINATOR_REPRESENTATIONS.get(combinator);

        if (combinatorText == null) {
            if(logger.isDebugEnabled()){
                 logger.debug("Invalid CombinedSelector combinator: " +
                         combinator);
             }
            // MCSTH0006X="Invalid CombinedSelector combinator"
            throw new StyleException(exceptionLocalizer.format(
                        "invalid-combined-selector-combinator"));
        }

        writer.write(combinatorText);

        context.renderSelector(subject);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 01-Sep-05	9412/1	adrianj	VBM:2005083007 CSS renderer using new model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 15-Oct-04	5798/1	adrianj	VBM:2004082515 Device Theme Cascade: Find matching rules

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
