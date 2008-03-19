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
 * (c) Volantis Systems Ltd 2006. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.integration.optimizer;

import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerCheckerMock;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyStatus;
import com.volantis.mcs.dom2theme.impl.optimizer.StatusUsage;
import com.volantis.mcs.themes.ShorthandSetMock;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.device.DeviceStylingTestHelper;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.device.DeviceValuesMock;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Set up common stuff for Optimizer tests.
 */

public abstract class OptimizerTestCaseAbstract extends TestCaseAbstract {

    protected static final StylingFactory STYLING_FACTORY =
            StylingFactory.getDefaultInstance();

    protected PropertyClearerCheckerMock checkerMock;
    protected ShorthandSetMock shorthandSetMock;
    protected MutablePropertyValues inputValues;
    protected DeviceValuesMock deviceValuesMock;

    protected void setUp() throws Exception {
        // Sets up log4j with a Threshold of info - we need debug ...
        super.setUp();

        // Turn up log4j *Threshold* to debug.
        // Each test still needs to set a *Level* for it's Category
        enableLog4jDebug();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        checkerMock = new PropertyClearerCheckerMock("checkerMock",
                                expectations);

        shorthandSetMock = new ShorthandSetMock("shorthandSetMock",
                expectations);

        inputValues = STYLING_FACTORY.createPropertyValues();

        deviceValuesMock = DeviceStylingTestHelper.createDeviceValuesMock(
                mockFactory, expectations, DeviceValues.DEFAULT);
    }

    protected void setPropertyValue(MutablePropertyValues inputValues,
            StyleProperty property, final StyleValue value,
            final PropertyStatus status) {

        checkerMock.expects
                .checkStatus(property, value, StatusUsage.SHORTHAND,
                        inputValues, DeviceValues.DEFAULT)
                .returns(status).any();
        inputValues.setComputedValue(property, value);
    }
}
