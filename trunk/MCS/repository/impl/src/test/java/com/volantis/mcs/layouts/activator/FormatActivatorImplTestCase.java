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

package com.volantis.mcs.layouts.activator;

import com.volantis.mcs.layouts.CanvasLayoutMock;
import com.volantis.mcs.layouts.SpatialFormatIteratorMock;
import com.volantis.mcs.layouts.TemporalFormatIteratorMock;
import com.volantis.mcs.unit.layouts.LayoutTestHelper;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the {@link RecursingFormatActivatorImpl}.
 */
public class FormatActivatorImplTestCase
        extends TestCaseAbstract {

    protected FormatActivatorFactoryMock formatActivatorFactoryMock;
    protected FormatActivatorMock spatialFormatActivatorMock;
    protected FormatActivatorMock temporalFormatActivatorMock;
    protected CanvasLayoutMock canvasLayoutMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        formatActivatorFactoryMock = new FormatActivatorFactoryMock(
                "formatActivatorFactoryMock", expectations);

        spatialFormatActivatorMock = new FormatActivatorMock(
                "spatialFormatActivatorMock", expectations);

        temporalFormatActivatorMock = new FormatActivatorMock(
                "temporalFormatActivatorMock", expectations);

        canvasLayoutMock = new CanvasLayoutMock("canvasLayoutMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        formatActivatorFactoryMock.expects.createSpatialIteratorActivator()
                .returns(spatialFormatActivatorMock).any();

        formatActivatorFactoryMock.expects.createTemporalIteratorActivator()
                .returns(temporalFormatActivatorMock).any();
    }

    /**
     * Create the object to test.
     *
     * @return The object being tested.
     */
    private RecursingFormatActivatorImpl createActivator() {
        return new RecursingFormatActivatorImpl(formatActivatorFactoryMock);
    }

    /**
     * Test that it works correctly if the format is null.
     */
    public void testActivateNull()
            throws Exception {

        FormatActivator activator = createActivator();
        activator.activate(null);
    }

    /**
     * Test that it invokes the spatial format activator appropriately.
     */
    public void testActivateSpatial()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final SpatialFormatIteratorMock spatialFormatIteratorMock =
                LayoutTestHelper.createSpatialFormatIteratorMock(
                        "spatialFormatIteratorMock", expectations,
                        canvasLayoutMock);

        // =====================================================================
        //   Create object to be tested.
        // =====================================================================

        final RecursingFormatActivatorImpl activator = createActivator();

        // =====================================================================
        //   Set Additional Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                spatialFormatActivatorMock.expects
                        .activate(spatialFormatIteratorMock);

                spatialFormatIteratorMock.expects.visitChildren(activator, null)
                        .returns(false);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        activator.activate(spatialFormatIteratorMock);
    }

    /**
     * Test that it invokes the temporal format activator appropriately.
     */
    public void testActivateTemporal()
            throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final TemporalFormatIteratorMock temporalFormatIteratorMock =
                LayoutTestHelper.createTemporalFormatIteratorMock(
                        "temporalFormatIteratorMock", expectations,
                        canvasLayoutMock);

        // =====================================================================
        //   Create object to be tested.
        // =====================================================================

        final RecursingFormatActivatorImpl activator = createActivator();

        // =====================================================================
        //   Set Additional Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                temporalFormatActivatorMock.expects
                        .activate(temporalFormatIteratorMock);

                temporalFormatIteratorMock.expects.visitChildren(activator, null)
                        .returns(false);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        activator.activate(temporalFormatIteratorMock);
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
