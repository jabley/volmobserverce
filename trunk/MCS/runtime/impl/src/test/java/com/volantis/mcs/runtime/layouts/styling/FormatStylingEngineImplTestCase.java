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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.CanvasLayoutMock;
import com.volantis.mcs.layouts.PaneMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMock;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngineMock;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link FormatStylingEngineImpl}.
 */
public class FormatStylingEngineImplTestCase
        extends TestCaseAbstract {
    private static final String STYLE_CLASS = "CLASS";
    private static final int INT_IDENTIFIER = 12;
    private static final String STRING_IDENTIFIER = String.valueOf(INT_IDENTIFIER);

    public void test() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final StylingEngineMock stylingEngineMock =
                new StylingEngineMock("stylingEngineMock", expectations);

        final CanvasLayoutMock canvasLayoutMock =
                new CanvasLayoutMock("canvasLayoutMock", expectations);

        final PaneMock paneMock = LayoutTestHelper.createPaneMock(
                "paneMock", expectations, canvasLayoutMock);

        final StylesMock stylesMock =
                new StylesMock("stylesMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        final int INT_IDENTIFIER = System.identityHashCode(paneMock);
        final String STRING_IDENTIFIER = String.valueOf(INT_IDENTIFIER);

        // NOTE: this tests Pane which is has different namespace and element
        // name rules to the other formats.
        expectations.add(new OrderedExpectations() {
            public void add() {

                stylingEngineMock.fuzzy
                        .startElement(XDIMESchemata.CDM_NAMESPACE, "pane",
                                      mockFactory.expectsInstanceOf(Attributes.class))
                        .does(new MethodAction() {
                            public Object perform(MethodActionEvent event)
                                    throws Throwable {

                                Attributes attributes = (Attributes)
                                        event.getArgument(Attributes.class);
                                assertEquals("id", STRING_IDENTIFIER,
                                             attributes.getAttributeValue(
                                                     null, FormatStylingConstants.FORMAT_IDENTIFIER));
                                assertEquals("class", STYLE_CLASS,
                                             attributes.getAttributeValue(
                                                     null, FormatStylingConstants.FORMAT_CLASS));

                                return null;
                            }
                        });

                stylingEngineMock.expects.getStyles().returns(stylesMock);

                stylingEngineMock.expects
                        .endElement(XDIMESchemata.CDM_NAMESPACE, "pane");

            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        FormatStylingEngine formatStylingEngine = new FormatStylingEngineImpl(
                stylingEngineMock);

        Styles styles = formatStylingEngine.startStyleable(
                paneMock, STYLE_CLASS);

        formatStylingEngine.endStyleable(paneMock);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
