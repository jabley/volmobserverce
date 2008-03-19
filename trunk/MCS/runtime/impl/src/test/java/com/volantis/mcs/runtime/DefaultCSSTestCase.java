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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Ensure that the default.css rules behave correctly.
 */
public class DefaultCSSTestCase
        extends TestCaseAbstract {

    public void testXFTextMUSelectRows() throws Exception {

        StrictStyledDOMHelper helper = new StrictStyledDOMHelper();
        Document document = helper.parse("<xfmuselect/>");
        Element root = document.getRootElement();
        StyleValue rows = root.getStyles().getPropertyValues()
                .getStyleValue(StylePropertyDetails.MCS_ROWS);
        assertEquals(StyleValueFactory.getDefaultInstance().getInteger(null, 4),
                rows);
    }
}
