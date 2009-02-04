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

package com.volantis.mcs.protocols.highlight;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMProtocolMock;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactoryMock;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.mcs.runtime.scriptlibrarymanager.RequiredScriptModulesMock;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.SAXException;

/**
 * Test case for Highlight Navigation widget, 
 * implemented as DOM transformer
 */
public class HighlightTransformerTestCase extends TestCaseAbstract {
    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * Test that Highlight Navigation produces the widget markup
     * if mcs-effect-style: highlight is specified for 
     * focusable element
     */
    public void _testHighlighNavigationForFocusable() throws  Exception {
        
        final String input = 
            "<html>" +
                "<head/>" +
                "<body>" +
                    "<a style=\"mcs-effect-style: highlight\" href=\"whatever\">text</a>" +
                 "</body>" +
            "</html>";

        final String expected = 
            "<html>" +
                "<head/>" +
                "<body>" +
                    "<a id=\"widgetId\" href=\"whatever\" style='mcs-effect-style: highlight'>text</a>" +
                    "<script type=\"text/javascript\">" +
                        "Widget.register('widgetId',new Widget.HighlightingNavigation('widgetId',  { time: 1.0,  startColor: '#ffffff',  endColor: '#ffffff'}));" +
                    "</script>" +
                "</body>" +
             "</html>";
        
        DOMProtocol protocol = buildExpectations(createDOMProtocol());
        doTransform(input, expected, protocol);
    }

    /**
     * Test that Highlight Navigation does not produce the widget markup
     * if mcs-effect-style: highlight is specified for 
     * non-focusable element
     */
    public void testHighlighNavigationForNonFocusable() throws  Exception {
        
        final String input = 
            "<html>" +
                "<head/>" +
                "<body>" +
                    "<div style=\"mcs-effect-style: highlight\">text</div>" +
                 "</body>" +
            "</html>";

        final String expected = 
            "<html>" +
                "<head/>" +
                "<body>" +
                    "<div style=\'mcs-effect-style: highlight\'>text</div>" +
                 "</body>" +
            "</html>";
                
        doTransform(input, expected, createDOMProtocol());
    }

    /**
     * Do the transform using the input XML and check the results against the
     * expected XML.
     */
    protected void doTransform(String inputXML,
                               String expectedXML,
                               DOMProtocol protocol)
            throws IOException,
                   SAXException,
                   ParserConfigurationException {

        StrictStyledDOMHelper styledDOMHelper = new StrictStyledDOMHelper(null);
        Document document = styledDOMHelper.parse(inputXML);

        DOMTransformer transformer = new HighlightTransformer();
        transformer.transform(protocol, document);

        String actualXML = styledDOMHelper.render(document);

        Document styledDom = styledDOMHelper.parse(expectedXML);
        String canonicalExpectedXML = styledDOMHelper.render(styledDom);

        System.out.println("Expected: " + expectedXML);
        System.out.println("Actual  : " + actualXML);
        assertXMLEquals("Actual XML does not match expected XML",
                        canonicalExpectedXML,
                        actualXML);
    }
    
    /**
     * Create protocol to test against.
     */    
    protected DOMProtocol createDOMProtocol() {
        
        ProtocolBuilder builder = new ProtocolBuilder();

        final DefaultDevice defaultDevice = new DefaultDevice("device", null, null);
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.SUPPORTS_JAVASCRIPT, "true");
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.SUPPORTS_VFC, "true");
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.SUPPORTS_VFC_SINCE, "4.0.0");

        InternalDevice device =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(defaultDevice);
        return (DOMProtocol)builder.build(
            new ProtocolRegistry.XHTMLFullFactory(), device);
    }
    
    /**
     * Build expectations about interations between the widget code
     * and its environment, accessible by way of protocol.  
     */    
    protected DOMProtocol buildExpectations(DOMProtocol protocol) {

         MarinerPageContextMock pageCtxMock = new MarinerPageContextMock(
                "pageCtx", expectations);        
        EnvironmentContextMock envCtxMock = new EnvironmentContextMock(
                "envCtx", expectations);
        VolantisMock volantisMock = new VolantisMock(
                "volantis", expectations);
        ProjectManagerMock projMgrMock = new ProjectManagerMock(
                "projMgr", expectations);
        RuntimeProjectMock projectMock = new RuntimeProjectMock(
                "project", expectations);
        PolicyReferenceFactoryMock policyRefFactoryMock = new PolicyReferenceFactoryMock(
                "policyRefFactory", expectations);

        final DOMProtocolMock protocolMock =
            new DOMProtocolMock("DOMProtocolMock", expectations,
                new DefaultProtocolSupportFactory(), null);

        pageCtxMock
            .expects.getProtocol()
            .returns(protocolMock).any();

        RequiredScriptModulesMock rsmMock = new RequiredScriptModulesMock(
                "rsmMock", expectations, pageCtxMock);

        pageCtxMock
            .expects.getRequiredScriptModules()
            .returns(rsmMock).any();

        rsmMock
            .expects.require(HighlightInsertRule.MODULE, null, false);

        pageCtxMock
            .expects.generateUniqueFCID()
            .returns("widgetId");

        protocol.setMarinerPageContext(pageCtxMock);

        return protocol;
    }
}
