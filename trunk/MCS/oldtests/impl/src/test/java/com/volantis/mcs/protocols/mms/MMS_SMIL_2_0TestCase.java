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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/mms/MMS_SMIL_2_0TestCase.java,v 1.12 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Dec-02    Sumit           VBM:2002102403 - Created to test MMS SMIL
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 14-Jan-01    Mat             VBM:2002112603 - Added test for doAudio.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and so it
 *                              uses the new TestMariner...Context classes
 *                              rather than a "cut & paste" inner classes
 *                              which extend Mariner...Context.
 * 06-Mar-03    Sumit           VBM:2003022605 - Moved static constants up to
 *                              DOMProtocolTestCase
 * 24-Mar-03    Phil W-S        VBM:2003031910 - Update the call for
 *                              storing device policy values as required by
 *                              this VBM update.
 * 28-Mar-03    Geoff           VBM:2003031711 - Add test for renderAltText.
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Override tests for
 *                              addPhoneNumberContents.
 * 10-Apr-03    Sumit           VBM:2003032713 - Override menu separator tests
 * 14-Apr-03    Phil W-S        VBM:2002111502 - Minor update to correct test
 *                              failure.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add ProtocolException
 *                              declarations where necessary.
 * 20-May-03    Steve           VBM:2003041606 - Removed whitespace from test
 *                              outputs as the protocol now removes it.
 * 27-May-03    Byron           VBM:2003051904 - Added testDoMenu and
 *                              testDoMenuHorizontal.
 * 23-May-03    Mat             VBM:2003042907 - Changed instantiation of
 *                              XMLOutputter as it is no longer a singleton.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.mms;

import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.debug.DebugCharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.RuntimeLayoutFactory;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.protocols.AudioAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocolTestAbstract;
import com.volantis.mcs.protocols.DOMProtocolTestable;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

/**
 *
 */
public class MMS_SMIL_2_0TestCase extends DOMProtocolTestAbstract {


    private MMS_SMIL_2_0 protocol;
    private DOMProtocolTestable testable;

    private DOMOutputBuffer buffer;
    private TestMarinerPageContext context = new TestMarinerPageContext();

    public MMS_SMIL_2_0TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestMMS_SMIL_2_0Factory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (MMS_SMIL_2_0) protocol;
        this.testable = (DOMProtocolTestable) testable;
    }

    private void privateSetUp() {
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        HashMap policies = new HashMap();
        policies.put("pixelsx", "120");
        policies.put("pixelsy", "165");
        context.setDevice(INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice("mms_smil", policies, null)));
        context.setDevicePolicyValue(
            DevicePolicyConstants.CSS_MULTICLASS_SUPPORT,
            DevicePolicyConstants.CSS_MULTICLASS_SUPPORT_NONE);
    }

    public void testWritePageHead() throws Exception {
        // overide for now since MMS_SMIL doesn't have a page head really?
        // @todo need to investigate what is going on here...
    }

    /**
     * Get the name of the menu element for the protocol under test.
     * @return The name of the menu element for the protocol under test.
     */
    protected String getMenuElementName() {
        return "";
    }

    public void testCanvas() throws LayoutException, ProtocolException {
        privateSetUp();

        context.setDeviceLayout(createDeviceLayout(false));
        protocol.setMarinerPageContext(context);
        CanvasAttributes attrib = new CanvasAttributes();
        attrib.setPageTitle("PageTitle");
        protocol.openCanvas(buffer, attrib);
        protocol.closeCanvas(buffer,attrib);
        String compare = "<smil><head><meta content=\"PageTitle\" name=\"title\"/>" +
        "<layout><root-layout/><region height=\"165\" id=\"Text\" " +
        "left=\"0\" top=\"0\" width=\"120\"/><region height=\"165\" " +
        "id=\"Image\" left=\"0\" top=\"165\" width=\"120\"/></layout>" +
        "</head></smil>";
        assertTrue("Markup is incorrect: "+getMarkup(buffer),
                    compare.equals(getMarkup(buffer)));
    }

    public void testDoAudio() {
        privateSetUp();

        AudioAttributes attr = new AudioAttributes();
        attr.setSrc("testSource");
        protocol.doAudio(buffer, attr);
        String compare = "<audio src=\"testSource\"/>";
        assertTrue("Markup is incorrect: " + getMarkup(buffer),
                compare.equals(getMarkup(buffer)));
    }

    public void testBodyOutsideTemporal()
            throws LayoutException, ProtocolException {
        privateSetUp();

        context.setDeviceLayout(createDeviceLayout(false));
        protocol.setMarinerPageContext(context);
        BodyAttributes body = new BodyAttributes();
        DOMOutputBuffer scratch = new DOMOutputBuffer();
        scratch.initialise();
        CanvasAttributes attrib = new CanvasAttributes();
        protocol.openCanvas(scratch,attrib);
        protocol.openBody(buffer, body);
        protocol.closeBody(buffer, body);
        String compare = "<body><par/></body>";
        assertTrue("Markup is incorrect: "+getMarkup(buffer),
                    compare.equals(getMarkup(buffer)));
    }

    public void testBodyInsideTemporal()
            throws LayoutException, ProtocolException {
        privateSetUp();

        context.setDeviceLayout(createDeviceLayout(true));
        protocol.setMarinerPageContext(context);
        DOMOutputBuffer scratch = new DOMOutputBuffer();
        scratch.initialise();
        CanvasAttributes attrib = new CanvasAttributes();
        protocol.openCanvas(scratch,attrib);
        BodyAttributes body = new BodyAttributes();
        protocol.openBody(buffer, body);
        protocol.closeBody(buffer, body);
        String compare = "<body/>";
        assertTrue("Markup is incorrect: "+getMarkup(buffer),
                            compare.equals(getMarkup(buffer)));
    }

    private String getMarkup(DOMOutputBuffer buffer){
        Element top = buffer.getRoot();
        StringWriter writer = new StringWriter();
    
        
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
                                    new XMLDocumentWriter(writer),
                                    new DebugCharacterEncoder());
        try {
            outputter.output(top);
        } catch (IOException e){
            fail("Failed with "+e.toString());
        }
        return writer.toString();
    }

    public RuntimeDeviceLayout createDeviceLayout(boolean inTemporal)
            throws LayoutException {
        LayoutBuilder builder = new LayoutBuilder(new RuntimeLayoutFactory());
        builder.createLayout(LayoutType.CANVAS);
        if(inTemporal){
            builder.pushFormat(FormatType.TEMPORAL_FORMAT_ITERATOR.getTypeName(),0);
            builder.setAttribute(FormatConstants.NAME_ATTRIBUTE, "temporal");
            builder.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS,
                    "variable");
            builder.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT,
                                "5");
            builder.setAttribute(TemporalFormatIterator.TEMPORAL_ITERATOR_CLOCK_VALUES,
                                "1,2,3,4,5");
        }
        builder.pushFormat(FormatType.GRID.getTypeName(),0);
        builder.setAttribute(Grid.COLUMNS_ATTRIBUTE,"1");
        builder.setAttribute(Grid.ROWS_ATTRIBUTE,"2");
        builder.attributesRead();
        builder.createSubComponent(Grid.COLUMN_FORMAT_TYPE,0);
        builder.attributesRead();
        builder.createSubComponent(Grid.ROW_FORMAT_TYPE,0);
        builder.attributesRead();
        builder.pushFormat(FormatType.PANE.getTypeName(),0);
        builder.setAttribute(FormatConstants.NAME_ATTRIBUTE,"one");
        builder.setAttribute(FormatConstants.DESTINATION_AREA_ATTRIBUTE,"Text");
        builder.attributesRead();
        //pop pane one
         builder.popFormat();

        builder.pushFormat(FormatType.PANE.getTypeName(),1);
        builder.setAttribute(FormatConstants.NAME_ATTRIBUTE,"two");
        builder.setAttribute(FormatConstants.DESTINATION_AREA_ATTRIBUTE,"Image");
        builder.attributesRead();
        //pop pane two
        builder.popFormat();
        // pop grid
        builder.popFormat();
        //if required pop temporal
        if(inTemporal){
            builder.popFormat();
        }

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(builder.getLayout());

        return runtimeDeviceLayout;
    }

    protected void checkRenderAltText(String altText, DOMOutputBuffer buffer) {
        // MMS SMIL doesn't render alt text fallbacks at all for now.
        // This will supposedly be fixed later.
        Element root = buffer.getCurrentElement();
        assertNull(root.getHead());
    }

    public void testAddPhoneNumberContentsString() throws Exception {
        doPhoneNumberContentTest("example with text",
                                 "example with text (" + phoneNumber + ")",
                                 "string");

    }

    public void testAddPhoneNumberContentsEmptyDom() throws Exception {
        MMS_DOMOutputBuffer contentDom = new MMS_DOMOutputBuffer();
        contentDom.initialise();

        doPhoneNumberContentTest(contentDom,
                                 "(" + phoneNumber + ")",
                                 "empty MMS DOM");
    }

    public void testAddPhoneNumberContentsNull() throws Exception {
        doPhoneNumberContentTest(null,
                                 "(" + phoneNumber + ")",
                                 "null");
    }

    public void testAddPhoneNumberContentsPopulatedDom() throws Exception {
        MMS_DOMOutputBuffer contentDom = new MMS_DOMOutputBuffer();
        contentDom.initialise();
        contentDom.openElement("content");
        contentDom.addElement("example");
        contentDom.appendEncoded("with text");
        contentDom.closeElement("content");

        doPhoneNumberContentTest(contentDom,
                                 "<content>" +
                                     "<example/>" +
                                     "with text" +
                                 "</content>" +
                                 "(" + phoneNumber + ")",
                                 "MMS DOM");
    }

    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "application/smil";
    }

    // javadoc inherited
    protected void checkResultForPre(final DOMOutputBuffer buffer) {
        final Text text = (Text) buffer.getRoot().getHead();
        assertEquals("     before          child     text          after     ",
            new String(text.getContents(), 0, text.getLength()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 07-Jun-05	8637/1	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 17-Feb-04	2974/3	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling
 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 17-Aug-03	1052/1	allan	VBM:2003073101 Support styles on menu and menuitems

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 ===========================================================================
*/
