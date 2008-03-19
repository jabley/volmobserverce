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
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.styling.Styles;

public class PaneRuleBuilderTestCase
        extends FormatRuleBuilderTestAbstract {

    public void testStyling()
        throws Exception {

        enableLog4jDebug();

        Pane pane = new Pane(canvasLayout);
        pane.setName("PANE");
        pane.setBackgroundColour("red");
        pane.setBorderWidth("2");
        pane.setCellPadding("4");
        pane.setCellSpacing("6");
        pane.setWidth("100");
        pane.setWidthUnits("percent");
        pane.setHeight("50");
        pane.setHorizontalAlignment(
                FormatConstants.HORIZONTAL_ALIGNMENT_VALUE_CENTER);
        pane.setVerticalAlignment(FormatConstants.VERTICAL_ALIGNMENT_VALUE_TOP);

        canvasLayout.setRootFormat(pane);

        FormatStylingEngine formatStylingEngine =
                createFormatStylingEngine(canvasLayout);

        Styles actualStyles;

        actualStyles = formatStylingEngine.startStyleable(pane, null);

        assertEquals("Pane Styles",
                     "background-color: red; " +
                     "background-image: none; " +
                     "border: solid 2px; " +
                     "border-spacing: 6px; " +
                     "height: 50px; " +
                     "padding: 4px; " +
                     "vertical-align: middle; " +
                     "width: 100%; " +
                     "text-align: center; " +
                     "vertical-align: top",
                     actualStyles);

        formatStylingEngine.endStyleable(pane);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
