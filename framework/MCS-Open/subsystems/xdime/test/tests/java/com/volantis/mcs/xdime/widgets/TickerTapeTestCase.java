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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.TickerTapeAttributes;
import com.volantis.mcs.xdime.xhtml2.XHTML2Elements;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.Arrays;
import java.util.List;

/**
 * Test the widget:ticker-tape element
 */
public class TickerTapeTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        

        addDefaultElementExpectations(TickerTapeAttributes.class);
    }

    protected String getElementName() {
        return WidgetElements.TICKER_TAPE.getLocalName();
    }

    protected List /*of ElementType*/ getChildElements() {
        ElementType[] myContent = {
            WidgetElements.REFRESH,
            XHTML2Elements.SPAN
        };
        
        return Arrays.asList(myContent);
    }

    public void testWidget() throws Exception {
        executeTest();
    }
}
