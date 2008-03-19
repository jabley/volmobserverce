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

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBufferMock;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.protocols.RegionContentMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link RegionInstance}.
 */
public class RegionInstanceTestCase
        extends TestCaseAbstract {
    private RegionContentMock regionContentMock;
    private DeviceLayoutContextMock contextMock;
    private DOMOutputBufferMock bufferMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        regionContentMock = new RegionContentMock("regionContentMock",
                expectations);

        contextMock = new DeviceLayoutContextMock("contextMock",
                expectations);

        bufferMock = new DOMOutputBufferMock("bufferMock", expectations);
    }

    /**
     * Create the region instance.
     *
     * @return The region instance.
     */
    private RegionInstance createRegionInstance() {
        RegionInstance instance = new RegionInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        instance.setDeviceLayoutContext(contextMock);
        return instance;
    }

    /**
     * Test that adding some region content adds an element with the correct
     * name,
     */
    public void testAddingRegionContent()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        final ElementMock elementMock =
                new ElementMock("elementMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================
        contextMock.expects.allocateOutputBuffer().returns(bufferMock);

        bufferMock.expects.addElement(RegionInstance.REGION_CONTENT_ELEMENT)
                .returns(elementMock);
        elementMock.expects.setAnnotation(regionContentMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RegionInstance instance = createRegionInstance();
        assertEquals("Region content count", 0,
                instance.getRegionContentCount());

        instance.addRegionContent(regionContentMock);

        assertEquals("Region content count", 1,
                instance.getRegionContentCount());
    }

    /**
     * Test that when the output buffer is empty that the region instance is
     * empty.
     */
    public void testInstanceEmptyWhenOutputBufferEmpty()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        bufferMock.expects.isEmpty().returns(true).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RegionInstance instance = createRegionInstance();
        assertTrue(instance.isEmptyImpl());
    }

    /**
     * Test that when the output buffer is non empty that the region instance is
     * non empty.
     */
    public void testInstanceNonEmptyWhenOutputBufferNonEmpty()
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.allocateOutputBuffer().returns(bufferMock);

        bufferMock.expects.isEmpty().returns(false).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RegionInstance instance = createRegionInstance();

        // Force the output buffer to be created.
        instance.getCurrentBuffer();
        assertFalse(instance.isEmptyImpl());
    }
}
