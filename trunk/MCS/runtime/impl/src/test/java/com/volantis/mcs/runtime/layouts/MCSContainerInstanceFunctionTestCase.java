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

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.context.FormatReferenceFinder;
import com.volantis.mcs.context.FormatReferenceFinderMock;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.FormatReferenceMock;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.StyleString;
import com.volantis.styling.expressions.EvaluationContextMock;
import com.volantis.styling.expressions.StylingFunction;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for {@link MCSContainerInstanceFunction}.
 */
public class MCSContainerInstanceFunctionTestCase
        extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY
            = StyleValueFactory.getDefaultInstance();

    public void test() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final EvaluationContextMock evaluationContextMock =
                new EvaluationContextMock("evaluationContextMock",
                                          expectations);

        final FormatReferenceFinderMock formatReferenceFinderMock =
                new FormatReferenceFinderMock(
                        "formatReferenceFinderMock", expectations);

        final FormatReferenceMock formatReferenceMock =
                new FormatReferenceMock("formatReferenceMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        evaluationContextMock.expects.getProperty(FormatReferenceFinder.class)
                .returns(formatReferenceFinderMock)
                .any();

        formatReferenceFinderMock.expects
                .getFormatReference("container", new int[] {2})
                .returns(formatReferenceMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        List arguments = Arrays.asList(new Object[] {
            STYLE_VALUE_FACTORY.getString(null, "container"),
            STYLE_VALUE_FACTORY.getInteger(null, 2),
        });

        StylingFunction function = new MCSContainerInstanceFunction();
        StyleFormatReference referenceValue = (StyleFormatReference)
                function.evaluate(evaluationContextMock, "foo", arguments);
        FormatReference reference = referenceValue.getReference();
        assertEquals("Reference", formatReferenceMock, reference);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 ===========================================================================
*/
