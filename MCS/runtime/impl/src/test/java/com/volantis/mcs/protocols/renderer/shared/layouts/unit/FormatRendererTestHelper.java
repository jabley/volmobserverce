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

package com.volantis.mcs.protocols.renderer.shared.layouts.unit;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.protocols.layouts.FormatInstanceMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContextMock;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.testtools.mock.ExpectationBuilder;

/**
 * Provides static methods for helping test {@link FormatRenderer}s.
 */
public class FormatRendererTestHelper {

    /**
     * Set the expectations that connect the context, instance and formats
     * together.
     *
     * @param formatRendererContextMock The context.
     * @param formatInstanceMockExpects The instance.
     * @param format The format that should be associated with the instance.
     * @param index The index of the instance within the context.
     */
    public static void connectFormatInstanceToFormat(
            FormatRendererContextMock formatRendererContextMock,
            final FormatInstanceMock.Expects formatInstanceMockExpects,
            final Format format,
            final NDimensionalIndex index) {

        formatRendererContextMock.expects.getFormatInstance(format, index)
                .returns(formatInstanceMockExpects._getMock())
                .any();

        // Initialise the child instance.
        formatInstanceMockExpects.getFormat().returns(format).any();
        formatInstanceMockExpects.getIndex().returns(index).any();
    }

    /**
     * Create an array of instance mocks for the children of a format.
     *
     * @param formatRendererContextMock The context mock.
     * @param parent The parent format for whose children the instances will
     * be created.
     * @param expectations The expectations.
     * @param index The index of the instances.
     *
     * @return An array of instances.
     */
    public static FormatInstanceMock[] createInstancesForChildren(
            FormatRendererContextMock formatRendererContextMock,
            Format parent, ExpectationBuilder expectations,
            NDimensionalIndex index) {

        int numChildren = parent.getNumChildren();
        FormatInstanceMock[] formatInstanceMocks =
                new FormatInstanceMock[numChildren];
        for (int i = 0; i < numChildren; i++) {
            formatInstanceMocks[i] = new FormatInstanceMock(
                    "formatInstanceMocks[" + i + "]", expectations,
                    index);
        }

        // Initialise the format context.
        for (int i = 0; i < numChildren; i++) {
            FormatMock formatMock = (FormatMock) parent.getChildAt(i);
            FormatInstanceMock instanceMock = formatInstanceMocks[i];

            // Create an association between the child instance and its format.
            connectFormatInstanceToFormat(
                    formatRendererContextMock, instanceMock.expects,
                    formatMock, index);
        }

        return formatInstanceMocks;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
