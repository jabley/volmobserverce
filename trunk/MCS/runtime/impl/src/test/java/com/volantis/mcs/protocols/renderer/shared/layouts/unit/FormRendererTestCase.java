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

package com.volantis.mcs.protocols.renderer.shared.layouts.unit;

import com.volantis.mcs.layouts.FormMock;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.protocols.AttributesTestHelper;
import com.volantis.mcs.protocols.FormAttributesMock;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.layouts.FormInstanceMock;
import com.volantis.mcs.protocols.layouts.FormatInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.shared.layouts.unit.AbstractFormatRendererTestAbstract;
import com.volantis.mcs.protocols.renderer.shared.layouts.FormRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Tests rendering functionality of default form renderer.
 */
public class FormRendererTestCase
        extends AbstractFormatRendererTestAbstract {

    /**
     * Test DeviceLayoutRenderer.render(form) to ensure that if the form
     * context only has a preamble buffer, we still render the form.
     * <p>
     * This is the test case for VBM:2003012802.
     * @throws java.io.IOException
     * @throws com.volantis.mcs.layouts.LayoutException
     */
    public void testRenderFormWithPreambleOnly()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final FormInstanceMock formInstanceMock =
                new FormInstanceMock("formInstanceMock", expectations,
                                     NDimensionalIndex.ZERO_DIMENSIONS);

        final FormMock formMock = LayoutTestHelper.createFormMock(
                "formMock", expectations, canvasLayoutMock);

        final FormatInstanceMock childInstanceMock =
                new FormatInstanceMock("childInstanceMock", expectations,
                                       NDimensionalIndex.ZERO_DIMENSIONS);

        final FormatMock childMock = LayoutTestHelper.createFormatMock(
                "childMock", expectations, canvasLayoutMock);

        LayoutTestHelper.addChildren(formMock.expects, new FormatMock.Expects[] {
            childMock.expects
        });

        final OutputBufferMock preambleBufferMock =
                new OutputBufferMock("preambleBufferMock", expectations);

        final FormAttributesMock formAttributesMock = (FormAttributesMock)
                AttributesTestHelper.createMockAttributes(
                        FormAttributesMock.class, "xfform",
                        "formAttributesMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the instance.
        formInstanceMock.expects.isEmpty().returns(false).any();
        formInstanceMock.expects.getFormat().returns(formMock).any();
        formInstanceMock.expects.getIndex()
                .returns(NDimensionalIndex.ZERO_DIMENSIONS)
                .any();
        formInstanceMock.expects.getPreambleBuffer(false)
                .returns(preambleBufferMock)
                .any();
        formInstanceMock.expects.getPostambleBuffer(false)
                .returns(null)
                .any();

        // Create an association between the child instance and its format.

        FormatRendererTestHelper.connectFormatInstanceToFormat(
                formatRendererContextMock,
                childInstanceMock.expects, childMock, NDimensionalIndex.ZERO_DIMENSIONS);

        // Initialise the layout attributes factory.
        layoutAttributesFactoryMock.expects.createFormAttributes()
                .returns(formAttributesMock);

        // Make sure that the attributes are initialised properly.
        formAttributesMock.expects.setForm(formMock).atLeast(1);

        // Make sure that the layout module is invoked properly.
        expectations.add(new OrderedExpectations() {
            public void add() {

                layoutModuleMock.expects
                        .writeOpenForm(formAttributesMock);

                layoutModuleMock.expects.writeFormPreamble(preambleBufferMock);

                formatRendererContextMock.expects
                        .renderFormat(childInstanceMock);

                layoutModuleMock.expects
                        .writeCloseForm(formAttributesMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        FormatRenderer renderer = new FormRenderer(layoutAttributesFactoryMock);
        renderer.render(formatRendererContextMock, formInstanceMock);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/7	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
