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

import com.volantis.mcs.protocols.widgets.WidgetModuleMock;
import com.volantis.mcs.protocols.widgets.attributes.TabAttributes;
import com.volantis.mcs.protocols.widgets.renderers.TabsRendererMock;

/**
 * Test the widget:autocomplete element
 */
public class TabTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        

        Class attrClass = TabAttributes.class;
        
        moduleMock = new WidgetModuleMock(
                "moduleMock", expectations);
        
        protocolMock
            .expects.getWidgetModule()
            .returns(moduleMock).any();  

        rendererMock = new TabsRendererMock(
                "rendererMock", expectations);
            
        moduleMock
            .expects.getTabsRenderer()
            .returns(rendererMock).any();
        
        ((TabsRendererMock)rendererMock)
            .fuzzy.renderTabOpen(
                    protocolMock, 
                    mockFactory.expectsInstanceOf(attrClass));
        
        ((TabsRendererMock)rendererMock)
            .fuzzy.renderTabClose(
                protocolMock, 
                mockFactory.expectsInstanceOf(attrClass));        
    }

    protected String getElementName() {
        return WidgetElements.TAB.getLocalName();
    }

    public void testWidget() throws Exception {
        executeTest();
    }
}
