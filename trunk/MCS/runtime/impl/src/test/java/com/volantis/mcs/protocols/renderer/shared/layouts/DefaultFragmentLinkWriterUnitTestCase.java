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
package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.layouts.CanvasLayoutMock;
import com.volantis.mcs.layouts.FragmentMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.layouts.FragmentInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutMock;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverTestHelper;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 */
public class DefaultFragmentLinkWriterUnitTestCase extends TestCaseAbstract {
    private DefaultFragmentLinkWriter writer;
    private FragmentMock fragmentMock;
    private FragmentInstance fragmentInstance;

    protected void setUp() throws Exception {
        super.setUp();

        final MarinerPageContextMock pageContextMock =
            new MarinerPageContextMock("pageContextMock", expectations);
        final MarinerRequestContextMock requestContextMock =
            new MarinerRequestContextMock("requestContextMock", expectations);
        final CanvasLayoutMock canvasLayoutMock = new CanvasLayoutMock(
                "canvasLayoutMock", expectations);
        fragmentMock = LayoutTestHelper.createFragmentMock(
            "fragmentMock", expectations, canvasLayoutMock);
        final DeviceLayoutContextMock deviceLayoutContextMock =
            new DeviceLayoutContextMock("deviceLayoutContextMock", expectations);
        fragmentInstance =
            new FragmentInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        final RuntimeDeviceLayoutMock runtimeDeviceLayoutMock =
            new RuntimeDeviceLayoutMock("runtimeDeviceLayoutMock", expectations);

        pageContextMock.expects.getDeviceLayoutContext().
            returns(deviceLayoutContextMock).any();
        pageContextMock.expects.getRequestContext().
            returns(requestContextMock).any();
        pageContextMock.expects.getDeviceLayout().
            returns(runtimeDeviceLayoutMock).any();

        pageContextMock.expects.getPolicyReferenceResolver()
                .returns(PolicyReferenceResolverTestHelper
                .getCommonExpectations(expectations, mockFactory))
                .any();

        deviceLayoutContextMock.expects.allocateOutputBuffer().
            does(
                new MethodAction() {
                    public Object perform(MethodActionEvent event)
                        throws Throwable {
                        return new DOMOutputBuffer();
                    }
                }).any();
        deviceLayoutContextMock.expects.getCurrentFormatInstance(fragmentMock).
            returns(fragmentInstance).any();
        requestContextMock.expects.getEnvironmentContext().returns(null).any();

        writer = new DefaultFragmentLinkWriter(pageContextMock);
    }

    public void testFragmentLinkTextNothing() throws Exception {

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "fragmentMock");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "fragmentMock");
    }

    public void testFragmentLinkTextForwardLink() throws Exception {

        fragmentInstance.setLinkToText("forwardLink");

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLink");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLink");
    }

    public void testFragmentLinkTextBackwardLink() throws Exception {

        fragmentInstance.setLinkFromText("backwardLink");

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "fragmentMock");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardLink");
    }

    public void testFragmentLinkTextBothLinks() throws Exception {

        fragmentInstance.setLinkToText("forwardLink");
        fragmentInstance.setLinkFromText("backwardLink");

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLink");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardLink");
    }

    public void testFragmentLinkTextFromFormat() throws Exception {

        fragmentMock.expects.getLinkText().returns("forwardFormatLink").any();
        fragmentMock.expects.getBackLinkText().
            returns("backwardFormatLink").any();

        fragmentInstance.setFormat(fragmentMock);
        fragmentInstance.initialise();

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardFormatLink");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardFormatLink");
    }

    public void testFragmentLinkTextForwardLinkBuffer() throws Exception {

        final OutputBuffer buffer = new DOMOutputBuffer();
        buffer.writeText("forwardLinkBuffer");
        fragmentInstance.setLinkToBuffer(buffer, 0);

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLinkBuffer");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLinkBuffer");
    }

    public void testFragmentLinkTextBackwardLinkBuffer() throws Exception {

        final OutputBuffer buffer = new DOMOutputBuffer();
        buffer.writeText("backwardLinkBuffer");
        fragmentInstance.setLinkFromBuffer(buffer, 0);

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "fragmentMock");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardLinkBuffer");
    }

    public void testFragmentLinkTextBothLinksBuffer() throws Exception {

        OutputBuffer buffer = new DOMOutputBuffer();
        buffer.writeText("forwardLinkBuffer");
        fragmentInstance.setLinkToBuffer(buffer, 0);
        buffer = new DOMOutputBuffer();
        buffer.writeText("backwardLinkBuffer");
        fragmentInstance.setLinkFromBuffer(buffer, 0);

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLinkBuffer");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardLinkBuffer");
    }

    public void testFragmentLinkTextBothLinksBufferAndText() throws Exception {

        OutputBuffer buffer = new DOMOutputBuffer();
        buffer.writeText("forwardLinkBuffer");
        fragmentInstance.setLinkToBuffer(buffer, 0);
        fragmentInstance.setLinkToText("forwardLinkText");
        buffer = new DOMOutputBuffer();
        buffer.writeText("backwardLinkBuffer");
        fragmentInstance.setLinkFromBuffer(buffer, 0);
        fragmentInstance.setLinkFromText("backwardLinkText");

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLinkBuffer");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardLinkBuffer");
    }

    public void testFragmentLinkTextPriority1() throws Exception {

        OutputBuffer buffer = new DOMOutputBuffer();
        buffer.writeText("forwardLinkBuffer0");
        fragmentInstance.setLinkToBuffer(buffer, 0);
        buffer = new DOMOutputBuffer();
        buffer.writeText("forwardLinkBuffer1");
        fragmentInstance.setLinkToBuffer(buffer, 1);
        fragmentInstance.setLinkToText("forwardLinkText");

        buffer = new DOMOutputBuffer();
        buffer.writeText("backwardLinkBuffer0");
        fragmentInstance.setLinkFromBuffer(buffer, 0);
        buffer = new DOMOutputBuffer();
        buffer.writeText("backwardLinkBuffer1");
        fragmentInstance.setLinkFromBuffer(buffer, 1);
        fragmentInstance.setLinkFromText("backwardLinkText");

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLinkBuffer0");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardLinkBuffer0");
    }

    public void testFragmentLinkTextPriority2() throws Exception {

        OutputBuffer buffer = new DOMOutputBuffer();
        buffer.writeText("forwardLinkBuffer1");
        fragmentInstance.setLinkToBuffer(buffer, 1);
        buffer = new DOMOutputBuffer();
        buffer.writeText("forwardLinkBuffer0");
        fragmentInstance.setLinkToBuffer(buffer, 0);
        fragmentInstance.setLinkToText("forwardLinkText");

        buffer = new DOMOutputBuffer();
        buffer.writeText("backwardLinkBuffer1");
        fragmentInstance.setLinkFromBuffer(buffer, 1);
        buffer = new DOMOutputBuffer();
        buffer.writeText("backwardLinkBuffer0");
        fragmentInstance.setLinkFromBuffer(buffer, 0);
        fragmentInstance.setLinkFromText("backwardLinkText");

        // forward check
        DOMOutputBuffer linkBuffer =
            (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, false);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "forwardLinkBuffer0");

        // backward check
        linkBuffer = (DOMOutputBuffer) writer.getLinkBuffer(fragmentMock, true);

        assertNotNull(linkBuffer);
        checkTextValue(linkBuffer, "backwardLinkBuffer0");
    }

    private void checkTextValue(
            final DOMOutputBuffer linkBuffer, final String value) {

        final Element root = linkBuffer.getRoot();
        final Text text = (Text) root.getHead();
        assertEquals(value, new String(text.getContents(), 0, text.getLength()));
        assertNull(text.getNext());
    }
}
