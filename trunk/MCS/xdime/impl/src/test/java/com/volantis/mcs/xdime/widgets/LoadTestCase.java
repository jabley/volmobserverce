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

import com.volantis.mcs.protocols.widgets.attributes.FoldingItemAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;

/**
 * Test the widget:load element
 */
public class LoadTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();      
    }


    protected String getElementName() {
        return WidgetElements.LOAD.getLocalName();
    }

    public void testWidget() throws Exception {
        FoldingItemElement parent = new FoldingItemElement(xdimeContext);
        pushElement(parent);
        pushElement(new DetailElement(xdimeContext));
        
        executeTest();
        
        FoldingItemAttributes parentAttrs = (FoldingItemAttributes)parent.getProtocolAttributes();
        LoadAttributes loadAttrs = parentAttrs.getLoadAttributes();
        assertNotNull(loadAttrs);
        assertEquals(loadAttrs.getId(), getWidgetId());
    }
}
