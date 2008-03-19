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

package com.volantis.mcs.css.impl.parser;


/**
 * Test property values.
 *
 * @todo Use mocks instead of round tripping.
 */
public class PropertyParsingTestCase
    extends ParsingTestCaseAbstract {

    /**
     * Test an angle property.
     */
    public void testAngleProperty()
        throws Exception {

        doDeclarationsRoundTrip("mcs-chart-x-axis-angle: 10deg",
                                "mcs-chart-x-axis-angle:10deg");
    }

    /**
     * Test an rgb() color property.
     */
    public void testColorRGBProperty()
        throws Exception {

        doDeclarationsRoundTrip("color: rgb(10%,20%,30%)",
                                "color:rgb(10%,20%,30%)");
    }

    /**
     * Test a triple color property.
     */
    public void testColorTripleProperty()
        throws Exception {

        doDeclarationsRoundTrip("color: #f00",
                                "color:#f00");
    }

    /**
     * Test a hex color property.
     */
    public void testColorHexProperty()
        throws Exception {

        doDeclarationsRoundTrip("color: #123456",
                                "color:#123456");
    }

    /**
     * Test a length property.
     */
    public void testLengthProperty()
        throws Exception {

        doDeclarationsRoundTrip("width: 10px", "width:10px");
    }

    /**
     * Test a percentage property.
     */
    public void testPercentageProperty()
        throws Exception {

        doDeclarationsRoundTrip("width: 10%", "width:10%");
    }

    /**
     * Test a URL property.
     */
    public void testURLProperty()
        throws Exception {

        doDeclarationsRoundTrip("background-image: url(/image.png)",
                                "background-image:url(/image.png)");
    }

    /**
     * Test a time property.
     */
    public void testTimeProperty()
        throws Exception {

        doDeclarationsRoundTrip("mcs-image-frame-interval: 10.0s",
                                "mcs-image-frame-interval:10s");
    }

    /**
     * Test an important property.
     */
    public void testPriority()
        throws Exception {

        doDeclarationsRoundTrip("color: blue !important",
                                "color:blue !important");

    }

    /**
     * Test that additional ; do not matter.
     */
    public void testAdditionalSemiColon()
        throws Exception {

        doDeclarationsRoundTrip("color: blue;", "color:blue");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
