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

import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.NDimensionalIndexMock;
import com.volantis.mcs.protocols.layouts.FormatInstanceMock;
import com.volantis.mcs.protocols.renderer.shared.layouts.AbstractFormatIteratorRenderer;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;

/**
 * Abstract base for extension by tests of
 * {@link AbstractFormatIteratorRenderer} specializations.
 */
public abstract class AbstractFormatIteratorRendererTestAbstract
        extends AbstractFormatRendererTestAbstract {

    protected NDimensionalIndexMock indexMock;
    protected NDimensionalIndexMock[] childMockIndeces;
    protected FormatInstanceMock childInstanceMock;
    protected FormatMock childMock;

    // Javadoc inherited from superclass
    public void setUp() throws Exception {
        super.setUp();

        indexMock = new NDimensionalIndexMock(
                "indexMock", expectations, new int[0]);

        // Create a sequence of indeces.
        childMockIndeces = new NDimensionalIndexMock[10];
        NDimensionalIndexMock next = null;
        for (int i = childMockIndeces.length - 1; i >= 0; i--) {
            NDimensionalIndexMock childMockIndex = new NDimensionalIndexMock(
                    "childIndexMock" + i, expectations, new int[0]);
            childMockIndeces[i] = childMockIndex;

            // If this is not the last in the list then make sure that
            // incrementing this index by 1 returns the next one.
            if (next != null) {
                childMockIndex.expects.incrementCurrentFormatIndex(1)
                        .returns(next).any();
            }

            // Remember this for next time around.
            next = childMockIndex;
        }

        // Iterate over and make sure that they can all be set to another.
        for (int i = 0; i < childMockIndeces.length; i++) {
            NDimensionalIndexMock index1 = childMockIndeces[i];
            for (int j = i; j < childMockIndeces.length; j++) {
                NDimensionalIndexMock index2 = childMockIndeces[j];
                index1.expects.setCurrentFormatIndex(j)
                        .returns(index2).any();
                index2.expects.setCurrentFormatIndex(i)
                        .returns(index1).any();
            }
        }

        childInstanceMock = new FormatInstanceMock(
                "childInstanceMock", expectations,
                NDimensionalIndex.ZERO_DIMENSIONS);

        childMock = LayoutTestHelper.createFormatMock(
                "childMock", expectations, canvasLayoutMock);

        // Create an association between the child instance and its format.
        for (int i = 0; i < childMockIndeces.length; i++) {
            NDimensionalIndexMock childMockIndex = childMockIndeces[i];

            FormatRendererTestHelper.connectFormatInstanceToFormat(
                    formatRendererContextMock,
                    childInstanceMock.expects, childMock, childMockIndex);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/4	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
