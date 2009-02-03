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

package com.volantis.mcs.layouts.spatial;

import com.volantis.testtools.mock.ExpectationBuilder;

/**
 * Provides helper methods for creating {@link EndlessStringArray} mocks.
 */
public class EndlessStringArrayTestHelper {

    /**
     * Create a mock endless array.
     *
     * @param identifier The identifier of the array.
     * @param expectations The expectations to which the mock should be added.
     * @param array The array of strings that should be wrapped.
     * @param maxIndex The max index that will be used on the returned array.
     * @return
     */
    public static EndlessStringArrayMock createMock(
            String identifier, ExpectationBuilder expectations,
            String[] array, int maxIndex) {

        EndlessStringArrayMock mock = new EndlessStringArrayMock(
                identifier, expectations, array);
        for (int i = 0; i < maxIndex; i += 1) {
            mock.expects.get(i).returns(array[i % array.length]).any();
        }

        return mock;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/