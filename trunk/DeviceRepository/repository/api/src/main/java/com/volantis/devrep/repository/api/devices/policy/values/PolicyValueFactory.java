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
package com.volantis.devrep.repository.api.devices.policy.values;

import com.volantis.devrep.repository.api.devices.PolicyDescriptorAccessor;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.mcs.devices.policy.values.PolicyValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public abstract class PolicyValueFactory {

    /**
     * Given a device and a policy name, return the policy value for that
     * policy as a {@link PolicyValue} object. All typing information
     * required will be obtained as necessary.
     * <p>
     * <strong>
     * It should be noted that the <code>PolicyValue</code> returned should
     * be an immutable object.
     * </strong>
     *
     * @param device     The device on which the policies of interested are
     *                   registered.
     * @param policyName The policy for which the value should be retrieved
     * @return An initialized PolicyValue containing the value for the policy
     *         name provided for the given device.
     */
    public abstract PolicyValue createPolicyValue(DefaultDevice device, String policyName);

    public static PolicyValueFactory createInstance(
            final PolicyDescriptorAccessor accessor) {
        
        try {
            final Class clazz = Class.forName(
                "com.volantis.devrep.repository.impl.devices.policy.values.DefaultPolicyValueFactory");
            final Constructor constructor = clazz.getConstructor(
                new Class[]{PolicyDescriptorAccessor.class});
            return (PolicyValueFactory) constructor.newInstance(
                new Object[]{accessor});
        } catch (ClassNotFoundException e) {
            throw new ExtendedRuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new ExtendedRuntimeException(e);
        } catch (InstantiationException e) {
            throw new ExtendedRuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new ExtendedRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new ExtendedRuntimeException(e);
        }
    }
}
