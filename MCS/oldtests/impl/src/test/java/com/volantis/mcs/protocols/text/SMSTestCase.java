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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.text;

import com.volantis.mcs.protocols.*;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.layouts.*;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.accessors.LayoutBuilder;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.debug.DebugCharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.styling.StylesBuilder;

import java.util.HashMap;
import java.io.StringWriter;
import java.io.IOException;

/**
 * Minimal test case for the SMS protocol.
 */
public class SMSTestCase extends DOMProtocolTestAbstract {

    private SMS protocol;
    private DOMProtocolTestable testable;

    protected static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    private DOMOutputBuffer buffer;


    private TestMarinerPageContext context = new TestMarinerPageContext();


    public SMSTestCase(String name) {
        super(name);
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (SMS) protocol;
        this.testable = (DOMProtocolTestable)testable;
    }

    private void privateSetUp() {
        buffer = new SMS_DOMOutputBuffer();

        testable.setPageHead(new MyPageHead());

        DOMOutputBuffer pageBuffer = new DOMOutputBuffer();
        pageBuffer.initialise();
        testable.setPageBuffer(pageBuffer);

        
        buffer.initialise();
        HashMap policies = new HashMap();
        policies.put("pixelsx", "120");
        policies.put("pixelsy", "165");
        context.setDevice(INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice("mms_smil", policies, null)));
        context.setDevicePolicyValue(
            DevicePolicyConstants.CSS_MULTICLASS_SUPPORT,
            DevicePolicyConstants.CSS_MULTICLASS_SUPPORT_NONE);


        try {
            context.setDeviceLayout(createDeviceLayout(false));
        } catch (LayoutException e) {
            e.printStackTrace();
        }
        protocol.initialise();
        protocol.setMarinerPageContext(context);
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

    public void testRenderAltText() {
        privateSetUp();

        final SMS_DOMOutputBuffer rootBuffer = new SMS_DOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);        

        protocol.renderAltText("alt text",new ImageAttributes());
        String result = null;

        result = getMarkup((DOMOutputBuffer)context.getCurrentOutputBuffer());
        assertEquals(result,"alt text");
    }


    public void testWriteHorizontalRule(){
        privateSetUp();
        String result = null;

        final SMS_DOMOutputBuffer rootBuffer = new SMS_DOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);

        protocol.doHorizontalRule(new DOMOutputBuffer(),new HorizontalRuleAttributes());
        result = getMarkup((DOMOutputBuffer)context.getCurrentOutputBuffer());

        assertEquals("---",result);
    }


    public void testWriteRowIteratorPaneElementContents(){
        privateSetUp();
        String expectedResult = "output buffer content";

        String result = null;
        OutputBuffer outputBuffer = new SMS_DOMOutputBuffer();
        outputBuffer.writeText(expectedResult);

        final SMS_DOMOutputBuffer rootBuffer = new SMS_DOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);


        protocol.writeRowIteratorPaneElementContents(outputBuffer);
        result = getMarkup((DOMOutputBuffer)context.getCurrentOutputBuffer());
        
        assertEquals(expectedResult, result);
    }


    public void testWriteOpenPane(){
        privateSetUp();

        String result = null;
        String expectedResult = "output buffer content";

        final SMS_DOMOutputBuffer rootBuffer = new SMS_DOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);

        try {
            OutputBuffer outputBuffer = new SMS_DOMOutputBuffer();
            outputBuffer.writeText(expectedResult);
            protocol.writePaneContents(outputBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        result = getMarkup((DOMOutputBuffer)context.getCurrentOutputBuffer());
        assertEquals(expectedResult, result);
    }


    public void testSpatialFormatIterator(){
        
    }

    /**
     * Tests if writing out a pre element keeps white-spaces.
     */
    public void testPre() throws Exception {
        final DOMOutputBuffer rootBuffer = new SMS_DOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);
        final PreAttributes attributes = new PreAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        protocol.writeOpenPre(attributes);
        final DOMOutputBuffer buffer =
            (DOMOutputBuffer) context.getCurrentOutputBuffer();
        assertNotEquals(rootBuffer, buffer);

        buffer.writeText("     before     ");
        context.getCurrentOutputBuffer().writeText("     child     text     ");
        buffer.writeText("     after     ");
        protocol.writeClosePre(attributes);

        // check the result
        checkResultForPre(rootBuffer);
    }

    public void testOrderedList(){
        privateSetUp();
        String result = null;

        SMS_DOMOutputBuffer emptyBuffer = new SMS_DOMOutputBuffer();        

        final SMS_DOMOutputBuffer rootBuffer = new SMS_DOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);

        ListItemAttributes listItemAttributes = new ListItemAttributes();

        protocol.openOrderedList(emptyBuffer, new OrderedListAttributes());
        protocol.openListItem( emptyBuffer, listItemAttributes);
        protocol.openListItem( emptyBuffer, listItemAttributes);
        protocol.openListItem( emptyBuffer, listItemAttributes);
        protocol.openListItem( emptyBuffer, listItemAttributes);
        protocol.closeOrderedList(emptyBuffer, new OrderedListAttributes());

        result = getMarkup((DOMOutputBuffer)context.getCurrentOutputBuffer());
        assertEquals("1 2 3 4",result);
    }

    public void testWriteClosePhoneNumber(){
        privateSetUp();
        String result = null;

        final SMS_DOMOutputBuffer rootBuffer = new SMS_DOMOutputBuffer();
        final MarinerPageContext context = protocol.getMarinerPageContext();
        context.pushOutputBuffer(rootBuffer);

        PhoneNumberAttributes phoneNumberAttributes = new PhoneNumberAttributes();
        phoneNumberAttributes.setDefaultContents("+4444444444");
        protocol.writeOpenPhoneNumber(phoneNumberAttributes);

        protocol.writeClosePhoneNumber(phoneNumberAttributes);

        result = getMarkup((DOMOutputBuffer)context.getCurrentOutputBuffer());
        assertEquals(result,"(+4444444444)");
    }

    public void testInitialiseAccesskeySupportTrue() throws Exception {
        // This test is not relevant to the SMS protocol: this protocol has no
        // support for accesskeys whatsoever
    }

    public void testInitialisePreferredLocationFor() {
        // This test is not relevant to the SMS protocol: this protocol has no
        // support for stylesheets whatsoever
    }

    public void testInitialiseDiallingLinkWithPrefix() {
        // This test is not relevant to the SMS protocol: this protocol has no
        // support for dialling links whatsoever
    }


    protected String getMenuElementName() {
        return null;
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestSMSFactory(),
                internalDevice);
        return protocol;
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

    /**
     * Checks the result of the pre test.
     * @param buffer the buffer that contains the result
     */
    protected void checkResultForPre(final DOMOutputBuffer buffer) {
        final String expectedResult = "     before     "+
                "     child     text     "+
                "     after     ";
        final String beforeText = getMarkup(buffer);
        assertEquals(expectedResult, beforeText);
//        final Element emElement = (Element) beforeText.getNext();
//        final Text emText = (Text) emElement.getHead();
//        assertEquals("     child     text     ",
//            new String(emText.getContents(), 0, emText.getLength()));
//        assertNull(emText.getNext());
//        final Text afterText = (Text) emElement.getNext();
//        assertEquals("     after     ",
//            new String(afterText.getContents(), 0, afterText.getLength()));
    }

}
