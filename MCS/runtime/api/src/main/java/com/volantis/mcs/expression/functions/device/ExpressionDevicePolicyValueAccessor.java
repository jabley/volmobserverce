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

package com.volantis.mcs.expression.functions.device;

import com.volantis.mcs.context.DevicePolicyAccessor;
import com.volantis.mcs.context.DeviceAncestorRelationship;
import com.volantis.mcs.expression.functions.device.DevicePolicyDependency;
import com.volantis.mcs.expression.functions.device.DevicePolicyDependency;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.shared.dependency.DependencyContext;

public class ExpressionDevicePolicyValueAccessor
        implements DevicePolicyValueAccessor {

    private final ExpressionContext expressionContext;
    private final DevicePolicyAccessor accessor;

    public ExpressionDevicePolicyValueAccessor(
            ExpressionContext expressionContext, DevicePolicyAccessor accessor) {
        this.expressionContext = expressionContext;
        this.accessor = accessor;
    }

    public DeviceAncestorRelationship getRelationshipTo(String deviceName) {
        return accessor.getRelationshipTo(deviceName);
    }

    public String getDeviceName() {
        return accessor.getDeviceName();
    }

    public String getDevicePolicyValue(String policyName) {
        return accessor.getDevicePolicyValue(policyName);
    }

    public String getDependentPolicyValue(String policyName) {
        String value = accessor.getDevicePolicyValue(policyName);
        DependencyContext context = expressionContext.getDependencyContext();
        if (context.isTrackingDependencies()) {
            DevicePolicyDependency dependency = new DevicePolicyDependency(
                    policyName, value);
            context.addDependency(dependency);
        }

        return value;
    }
}
