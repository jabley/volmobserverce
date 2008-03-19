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
package com.volantis.mcs.protocols;


import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base Test case for horizontal rule emulation
 */
public abstract class HrEmulationTestAbstract extends TestCaseAbstract {

    protected StrictStyledDOMHelper helper;

    public HrEmulationTestAbstract() {
        helper = new StrictStyledDOMHelper(null);
    }

    /**
     * Create HorizontalRule Attributes with given styling
     * @param style attribute value string
     * @return
     */
    protected HorizontalRuleAttributes createHorizontalRuleAttributes(String style) {
        HorizontalRuleAttributes attributes = new HorizontalRuleAttributes();
        Styles styles = StylesBuilder.getCompleteStyles(style, true);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.DISPLAY, DisplayKeywords.BLOCK);
        attributes.setStyles(styles);

        return attributes;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 31-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 27-Oct-05	9565/9	ibush	VBM:2005081219 Horizontal Rule Emulation

 27-Oct-05	9565/7	ibush	VBM:2005081219 Horizontal Rule Emulation

 25-Oct-05	9565/5	ibush	VBM:2005081219 Horizontal Rule Emulation

 25-Oct-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
