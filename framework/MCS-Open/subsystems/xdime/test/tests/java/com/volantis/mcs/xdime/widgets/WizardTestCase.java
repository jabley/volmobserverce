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

import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBufferMock;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.protocols.OutputBufferFactoryMock;
import com.volantis.mcs.protocols.layouts.RegionInstanceMock;
import com.volantis.mcs.protocols.widgets.WidgetModuleMock;
import com.volantis.mcs.protocols.widgets.attributes.WizardAttributes;
import com.volantis.mcs.protocols.widgets.renderers.WizardRendererMock;
import com.volantis.mcs.xdime.xhtml2.BodyElement;

/**
 * Test the widget:wizard element 
 */
public class WizardTestCase extends WidgetElementTestCaseAbstract {

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create basic mocks 
        super.setUp();        

        
        // Called during doElementStart
        OutputBufferFactoryMock outputBufferFactoryMock 
            = new OutputBufferFactoryMock(
                "outputBufferFactoryMock", expectations);

        DOMFactoryMock domFactoryMock = new DOMFactoryMock(
                "domFactory", expectations);
        
        domFactoryMock.expects.createElement().returns(null);

        DOMOutputBufferMock outputBuffer = new DOMOutputBufferMock(
                "outputBuffer", expectations, domFactoryMock);
        
        protocolMock
            .expects.getOutputBufferFactory()
            .returns(outputBufferFactoryMock);
        
        pageCtxMock
            .fuzzy.pushDeviceLayoutContext(
                mockFactory.expectsInstanceOf(DeviceLayoutContext.class));

        RegionInstanceMock regionMock 
            = new RegionInstanceMock("regionMock", expectations,
                    NDimensionalIndex.ZERO_DIMENSIONS);

        pageCtxMock
            .fuzzy.getFormatInstance(
                mockFactory.expectsInstanceOf(Format.class),
                NDimensionalIndex.ZERO_DIMENSIONS)
             .returns(regionMock);

        pageCtxMock
            .expects.pushContainerInstance(regionMock);

        regionMock
        	.expects.getCurrentBuffer()
        	.returns(outputBuffer).any();
        
        pageCtxMock
            .expects.pushOutputBuffer(outputBuffer);
        
        
        
        // Called during callOpenOnProtocol
        moduleMock = new WidgetModuleMock(
                "moduleMock", expectations);
        protocolMock
            .expects.getWidgetModule()
            .returns(moduleMock).fixed(2);        

        rendererMock = new WizardRendererMock(
                "rendererMock", expectations);
        
        moduleMock
            .expects.getWizardRenderer()
            .returns(rendererMock);
        
        rendererMock
            .fuzzy.renderOpen(
                protocolMock, 
                mockFactory.expectsInstanceOf(WizardAttributes.class));
        
        // Called during callCloseOnProtocol
        
        moduleMock
            .fuzzy.getWidgetRenderer(
                    mockFactory.expectsInstanceOf(WizardAttributes.class))
            .returns(rendererMock);

        rendererMock
            .fuzzy.renderClose(
                protocolMock, 
                mockFactory.expectsInstanceOf(WizardAttributes.class));

        
        // Called during doElementEnd
        
        pageCtxMock.expects.popContainerInstance(regionMock);                                         

        pageCtxMock
        	.expects.popOutputBuffer(outputBuffer);
                
        DeviceLayoutContextMock deviceLayoutCtxMock =
            new DeviceLayoutContextMock("deviceLayoutCtxMock", expectations);

        pageCtxMock
            .expects.getDeviceLayoutContext()
            .returns(deviceLayoutCtxMock);

        pageCtxMock
            .expects.popDeviceLayoutContext()
            .returns(null);
        
              
        domFactoryMock.expects.createElement().returns(null);
        DOMOutputBufferMock currentBuffer = new DOMOutputBufferMock(
                "currentBuffer", expectations, domFactoryMock);

         
        containerInstanceMock
        	.expects.getCurrentBuffer()
        	.returns(currentBuffer).any();
        
        currentBuffer
    		.expects.transferContentsFrom(outputBuffer);
    }
    
    protected String getElementName() {
        return "wizard";
    }

    public void testWidget() throws Exception {
        pushElement(new BodyElement(xdimeContext));
        executeTest();
    }
}
