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

package com.volantis.styling.device;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.styling.properties.StyleProperty;

public class DeviceStylingTestHelper {

    public static DeviceValuesMock createDeviceValuesMock(
            MockFactory mockFactory, ExpectationContainer expectations,
            final StyleValue deviceValue) {

        final DeviceValuesMock deviceValuesMock =
                new DeviceValuesMock("deviceValuesMock", expectations);

        deviceValuesMock.fuzzy.getStyleValue(mockFactory.expectsAny())
                .returns(deviceValue).any();
        deviceValuesMock.fuzzy.getPriority(mockFactory.expectsAny())
                .returns(Priority.NORMAL).any();

        return deviceValuesMock;
    }

    public static DeviceValuesMock createDeviceValuesMock(
            MockFactory mockFactory, ExpectationContainer expectations,
            final StyleValue deviceValue, final Priority priority,
            StyleProperty property) {

        final DeviceValuesMock deviceValuesMock =
                new DeviceValuesMock("deviceValuesMock", expectations);

        deviceValuesMock.expects.getPriority(property)
                .returns(priority).any();

        deviceValuesMock.fuzzy.getStyleValue(mockFactory.expectsAny())
                .returns(deviceValue).any();
        deviceValuesMock.fuzzy.getPriority(mockFactory.expectsAny())
                .returns(Priority.NORMAL).any();

        return deviceValuesMock;
    }
}
