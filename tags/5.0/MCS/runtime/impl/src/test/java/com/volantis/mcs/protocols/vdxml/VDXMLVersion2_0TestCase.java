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
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.papi.NativeMarkupAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIElementFactory;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.PaneAttributes;
import com.volantis.mcs.papi.SelectAttributes;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.css.emulator.CSSEmulatorTestAbstract;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.layouts.FormatConstants;

/**
 * TestCase for a full VDXML Page
 */
public class VDXMLVersion2_0TestCase extends CSSEmulatorTestAbstract {
                                   
    private String expected =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<!DOCTYPE VDXML SYSTEM \"vdxml.dtd\">" +
            "<!--Test Page Heading Message-->" +
            "<VDXML>" +
            "<TEXTE DX=\"1\" DY=\"12\" X=\"1\" Y=\"1\">" +
            "native markup contents" +
            "pane contents&nbsp;</TEXTE>" +
            "<RACCOURCI FNCT=\"REPETITION\" URL=\"http://localhost/volantis/test.jsp\"/>" +
            "</VDXML>";


    /**
     * create a VDXMLVersion2_0 protocol instance
     * @return
     */
    protected DOMProtocol createProtocol() {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new ProtocolRegistry.VDXMLVersion2_0Factory(), device);
        return (DOMProtocol) protocol;
    }

    //javadoc inherited
    protected String getProtocolName() {
        return "VDXMLVersion2_0";
    }

    /**
     * Test that a full page can be processed through the VDXML Protocol
     * @throws PAPIException
     */
    public void testPage() throws PAPIException {
        open("canvas",
            new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt",
                         null});

        openPane();

        openSelect();

        openNativeMarkup();

        close("nativemarkup");

        close("select");

        writeMessage("pane contents");
        
        writeMessage("\u00a0");
        
        close("pane");

        close("canvas");


        String result = writer.toString();

        assertEquals("Result incorrect for VDXML protocol",
                expected, result);
    }

    /**
     * open a pane element
     * @throws PAPIException
     */
    private void openPane() throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory("pane");

        assertNotNull("Unknown element name, pane check testcase.",
            factory);

        PAPIElement element = factory.createPAPIElement();
        PaneAttributes attributes = (PaneAttributes)
                factory.createElementSpecificAttributes();

        attributes.setName("pane");

        // Process start element, this is the thing which does
        // the work.
        int result = element.elementStart(getCurrentRequestContext(), attributes);

        assertEquals("Open did not return PROCESS_ELEMENT_BODY as expected",
            result, PROCESS_ELEMENT_BODY);

        // Push the element on the stack so that we can close it later.
        elements.push(element);
    }


    /**
     * open select markup
     * @throws PAPIException
     */
    private void openSelect() throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory("select");

        assertNotNull("Unknown element name, select check testcase.",
            factory);

        PAPIElement element = factory.createPAPIElement();
        SelectAttributes attributes = (SelectAttributes)
                factory.createElementSpecificAttributes();

        // Process start element, this is the thing which does
        // the work.
        int result = element.elementStart(getCurrentRequestContext(), attributes);

        assertEquals("Open did not return PROCESS_ELEMENT_BODY as expected",
            result, PROCESS_ELEMENT_BODY);

        // Push the element on the stack so that we can close it later.
        elements.push(element);
    }

    /**
     * open native markup
     * @throws PAPIException
     */
    private void openNativeMarkup() throws PAPIException {
        PAPIElementFactory factory =
            papiFactory.getPAPIElementFactory("nativemarkup");

        assertNotNull("Unknown element name, nativemarkup check testcase.",
            factory);

        PAPIElement element = factory.createPAPIElement();
        NativeMarkupAttributes attributes = (NativeMarkupAttributes)
                factory.createElementSpecificAttributes();

        attributes.setPane("pane");
        attributes.setTargetLocation("pane");

        // Process start element, this is the thing which does
        // the work.
        int result = element.elementStart(getCurrentRequestContext(), attributes);

        assertEquals("Open did not return PROCESS_ELEMENT_BODY as expected",
            result, PROCESS_ELEMENT_BODY);

        // Push the element on the stack so that we can close it later.
        elements.push(element);

        writeMessage("native markup contents");
    }

    /**
     * add the height of the pane - other javadoc inherited 
     * @return
     * @throws RepositoryException
     */
    protected RuntimeDeviceLayout createDeviceLayout()
            throws RepositoryException {

        RuntimeDeviceLayout result = null;

        LayoutBuilder builder = new LayoutBuilder(new RuntimeLayoutFactory());

        builder.createLayout(LayoutType.CANVAS);

        builder.pushFormat("Pane", 0);

        builder.setAttribute("Name", "pane");

        builder.setAttribute(FormatConstants.HEIGHT_ATTRIBUTE, "100");
        builder.setAttribute(FormatConstants.WIDTH_ATTRIBUTE, "8");
        builder.setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                             FormatConstants.WIDTH_UNITS_VALUE_PIXELS);

        builder.popFormat();

        Layout layout = builder.getLayout();
        result = RuntimeDeviceLayoutTestHelper.activate(layout);

        return result;
    }
}
