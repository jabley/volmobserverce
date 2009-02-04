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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.runtime.ScriptLibraryManager;
import com.volantis.mcs.runtime.scriptlibrarymanager.RequiredScriptModules;

import java.util.HashMap;

/**
 * Base class for integration testing of XDIME 2 elements
 * with a HTML 4 protocol.
 */
public abstract class HTMLV4TestCaseAbstract extends RootTestCaseAbstract {
    
    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * Return an instance of the Protocol class HTMLVersion4.
     * 
     * @return HTML 4 Protocol class
     */
    protected DOMProtocol createProtocol() {

        ProtocolBuilder builder = new ProtocolBuilder();

        final DefaultDevice defaultDevice =
            new DefaultDevice("Test Device", new HashMap(), null);
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.SUPPORTS_JAVASCRIPT, "true");
        InternalDevice device =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(defaultDevice);

        DOMProtocol protocol = (DOMProtocol) builder.build(
                new ProtocolRegistry.HTMLVersion4_0Factory(), device);
        return protocol;
    }

    protected void setProtocolSpecificExpectations() {
        marinerPageContextMock.expects.getBooleanDevicePolicyValue(
                "dial.link.supported").returns(false);
        marinerPageContextMock.expects.getBooleanDevicePolicyValue(
                "stylesheets").returns(true);

        marinerPageContextMock.expects.getDevicePolicyValue(
                "protocol.stylesheet.location.theme").returns("external");
        marinerPageContextMock.expects.getDevicePolicyValue(
                "protocol.supports.accesskey").returns("true");
        marinerPageContextMock.expects.getDevicePolicyValue(
                "nested.tables.support").returns("true");
        marinerPageContextMock.expects.getDevicePolicyValue("maxhtmlpage")
                .returns(null);
        marinerPageContextMock.expects.getDevicePolicyValue("maxlinechars")
                .returns(null).any();
        marinerPageContextMock.expects.getDevicePolicyValue(
                "x-element.td.supports.attribute.align").returns(null);
        marinerPageContextMock.expects.getDevicePolicyValue(
                "x-element.center.supported").returns(null);
        marinerPageContextMock.expects.getScriptLibraryManager().returns(
                new ScriptLibraryManager(protocol)).any();
        marinerPageContextMock.expects.getRequiredScriptModules().returns(
                        new RequiredScriptModules(marinerPageContextMock)).any();
                 
        marinerPageContextMock.expects.getMediaAgent(false)
                .returns(null).any();
        
        marinerPageContextMock.expects.getDevicePolicyValue(
                "x-element.script.supported").returns("true");        
    }

    /**
     * Add expectations that are required for the canvas tag to run.
     * 
     * todo: move this into parent class and always add them?
     */
    protected void addCanvasExpectations() {

        marinerPageContextMock.expects.getDevicePolicyValue(
                DevicePolicyConstants.FIXED_META_VALUES).returns(null).any();

        marinerPageContextMock.expects.endPhase1BeginPhase2().any();

    }

    protected void addElementOutputStateExpectations() {

        marinerPageContextMock.fuzzy.pushContainerInstance(
                mockFactory.expectsInstanceOf(PaneInstance.class)).returns().any();
        marinerPageContextMock.fuzzy.popContainerInstance(
                mockFactory.expectsInstanceOf(PaneInstance.class)).returns().any();
    }

    /**
     * Add the standard markup to the supplied body text and compare it to the
     * generated markup.
     * 
     * @param expectedBodyText
     */
    protected void assertBodyEqual(String expectedBodyText, String expectedCSS,
            String actualMarkup) {

        String expectedMarkup = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">"
                + "<!--"
                + PAGE_HEADER_COMMENT
                + "-->"
                + "<html>"
                + "<head>"
                + "<title>"
                + PAGE_TITLE
                + "</title>"
                + "<style type=\"text/css\">"
                + expectedCSS
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div>"
                + expectedBodyText
                + "</div>"
                + "</body>" + "</html>";

        assertEquals("Output HTML incorrectly formatted.", expectedMarkup,
                actualMarkup);
    }

    protected void assertBodyEqual(String expectedBodyText, String actualMarkup) {
        assertBodyEqual(expectedBodyText, "", actualMarkup);
    }

}

/*
 * ===========================================================================
 * Change History
 * ===========================================================================
 * $Log$
 * 
 * 12-Dec-05 10791/1 ianw VBM:2005071309 Ported forward meta changes
 * 
 * 12-Dec-05 10785/7 ianw VBM:2005071309 Ported forward meta changes
 * 
 * 12-Dec-05 10785/5 ianw VBM:2005071309 Ported forward meta changes
 * 
 * 12-Dec-05 10785/3 ianw VBM:2005071309 Ported forward meta changes
 * 
 * 12-Dec-05 10785/1 ianw VBM:2005071309 Ported forward meta changes
 * 
 * 07-Dec-05 10679/1 ianw VBM:2005120605 Back out background-color inheritance
 * and make xdimecp body output div
 * 
 * 07-Dec-05 10659/1 ianw VBM:2005120605 Back out background-color inheritance
 * and make xdimecp body output div
 * 
 * 06-Dec-05 10638/1 emma VBM:2005120505 Forward port: Generated XHTML was
 * invalid - had no head tag but had head content
 * 
 * 06-Dec-05 10623/1 emma VBM:2005120505 Generated XHTML was invalid: missing
 * head tag but head content
 * 
 * 01-Dec-05 9839/5 geoff VBM:2005101702 Fix the XDIME2 Object element
 * 
 * 29-Nov-05 10504/1 ianw VBM:2005112312 Fixed pseudoElements in GUI and JIBX
 * 
 * 29-Nov-05 10484/1 ianw VBM:2005112312 Fixed pseudoElements in GUI and JIBX
 * 
 * 28-Nov-05 10467/1 ibush VBM:2005111812 Styling Fixes for Orange Test Page
 * 
 * 28-Nov-05 10394/1 ibush VBM:2005111812 Styling Fixes for Orange Test Page
 * 
 * 25-Nov-05 9708/2 rgreenall VBM:2005092107 Restrict the length of lines
 * written by MCS for devices that have a maximum line limit.
 * 
 * 07-Nov-05 10168/1 ianw VBM:2005102504 port forward web clipping
 * 
 * 07-Nov-05 10170/1 ianw VBM:2005102504 port forward web clipping
 * 
 * 10-Oct-05 9673/1 pduffin VBM:2005092906 Improved validation and fixed layout
 * formatting
 * 
 * 06-Oct-05 9736/1 pabbott VBM:2005100512 Add the XHTML2 object testcase
 * 
 * ===========================================================================
 */
