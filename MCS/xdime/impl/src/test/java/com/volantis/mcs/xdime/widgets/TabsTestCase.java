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

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.OutputBufferFactoryMock;
import com.volantis.mcs.protocols.layouts.RegionInstanceMock;
import com.volantis.mcs.protocols.widgets.WidgetModuleMock;
import com.volantis.mcs.protocols.widgets.attributes.TabsAttributes;
import com.volantis.mcs.protocols.widgets.renderers.TabsRendererMock;
import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.Arrays;
import java.util.List;

/**
 * Test the widget:tabs element
 */
public class TabsTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {

        // Let the base class create basic mocks
        super.setUp();

        Class attrClass = TabsAttributes.class;
                
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
        
        // call before renderTabsOpen

        OutputBufferFactoryMock outputBufferFactoryMock
            = new OutputBufferFactoryMock("outputBufferFactoryMock", expectations);
      
        protocolMock
            .expects.getOutputBufferFactory()
            .returns(outputBufferFactoryMock).fixed(2);
        
        pageCtxMock
            .fuzzy.pushDeviceLayoutContext(
                mockFactory.expectsInstanceOf(DeviceLayoutContext.class)).returns().fixed(2);
    
        RegionInstanceMock regionMock 
            = new RegionInstanceMock("regionMock", expectations,
                    NDimensionalIndex.ZERO_DIMENSIONS);
    
        pageCtxMock
            .fuzzy.getFormatInstance(
                mockFactory.expectsInstanceOf(Format.class),
                NDimensionalIndex.ZERO_DIMENSIONS)
             .returns(regionMock).fixed(2);

        ((TabsRendererMock)rendererMock)
            .expects.setContentsRegionInstance(regionMock);

        ((TabsRendererMock)rendererMock)
            .expects.setLabelsRegionInstance(regionMock);

        ((TabsRendererMock) rendererMock)
            .fuzzy.renderOpen(
                protocolMock,
                mockFactory.expectsInstanceOf(attrClass));
        
        ((TabsRendererMock) rendererMock)
            .fuzzy.renderTabsOpen(
                protocolMock,
                mockFactory.expectsInstanceOf(attrClass));
        
        rendererMock
            .fuzzy.shouldRenderContents(
                protocolMock,
                mockFactory.expectsInstanceOf(attrClass)).returns(true);

        ((TabsRendererMock) rendererMock)
            .fuzzy.renderTabsClose(
                protocolMock,
                mockFactory.expectsInstanceOf(attrClass));                

        ((TabsRendererMock) rendererMock)
            .fuzzy.renderClose(
                protocolMock,
                mockFactory.expectsInstanceOf(attrClass));                

    }

    protected String getElementName() {
        return WidgetElements.TABS.getLocalName();
    }

    protected List /* of ElementType */getChildElements() {
        ElementType[] myContent = { 
                WidgetElements.TAB, 
                WidgetElements.TAB,
                WidgetElements.TAB 
        };
        return Arrays.asList(myContent);
    }

    public void testWidget() throws Exception {
        executeTest();
    }
}
