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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl.devices.policy.values;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.PolicyDescriptorAccessor;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.policy.types.BooleanPolicyType;
import com.volantis.mcs.devices.policy.types.CompositePolicyType;
import com.volantis.mcs.devices.policy.types.IntPolicyType;
import com.volantis.mcs.devices.policy.types.OrderedSetPolicyType;
import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.RangePolicyType;
import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.mcs.devices.policy.types.SimplePolicyType;
import com.volantis.mcs.devices.policy.types.StructurePolicyType;
import com.volantis.mcs.devices.policy.types.TextPolicyType;
import com.volantis.mcs.devices.policy.types.UnorderedSetPolicyType;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.mcs.devices.policy.values.SimplePolicyValue;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * A factory that can generate {@link PolicyValue}s using device policy value
 * information from {@link DefaultDevice} and the device policy meta data from
 * {@link com.volantis.mcs.devices.policy.PolicyDescriptor}.
 */
public class DefaultPolicyValueFactory extends PolicyValueFactory {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DefaultPolicyValueFactory.class);

    /**
     * The object to use to retrieve {@link PolicyDescriptor} objects.
     */
    private PolicyDescriptorAccessor policyDescriptorAccessor;

    /**
     * Initialize.
     *
     * @param policyDescriptorAccessor The object to use to access
     * {@link PolicyDescriptor}s. May not be null.
     */
    public DefaultPolicyValueFactory(PolicyDescriptorAccessor policyDescriptorAccessor) {
        if (policyDescriptorAccessor == null) {
            throw new IllegalArgumentException("Cannot be null: policyDescriptorAccessor");
        }

        this.policyDescriptorAccessor = policyDescriptorAccessor;
    }

    // javadoc inherited
    public PolicyValue createPolicyValue(DefaultDevice device, String policyName) {
        if (device == null) {
            throw new IllegalArgumentException("Cannot be null: device");
        }
        if (policyName == null) {
            throw new IllegalArgumentException("Cannot be null: policyName");
        }

        PolicyValue value = null;

        try {
            PolicyDescriptor descriptor =
                    policyDescriptorAccessor.getPolicyDescriptor(
                            policyName, Locale.getDefault());
            if (descriptor != null) {
                PolicyType type = descriptor.getPolicyType();
                if (type instanceof StructurePolicyType) {
                    value = createStructuredPolicyValue(device, policyName,
                            (StructurePolicyType) type);
                } else {
                    try {
                        // Extract the policy value as a string.
                        String valueString =
                            device.getComputedPolicyValue(policyName);
                        // If we found a string value...
                        if (valueString != null) {
                            // Create a policy value object for the string
                            // value.
                            value = createPolicyValue(valueString, type);
                        }
                        // Else, there was no value for that name, so return
                        // null.
                    } catch (DeviceRepositoryException e) {
                        // There was an invalid policy value for the policy
                        // type.
                        logger.error("unexpected-exception", e.getCause());
                    }
                }
            } else {
                logger.warn("cannot-retrieve-policy-info",
                        new Object[]{policyName});
            }
        } catch (DeviceRepositoryException dre) {
            logger.warn("cannot-retrieve-policy-info",
                    new Object[]{policyName}, dre);
        }

        return value;
    }

    /**
     * Given a policy value, and the known type of that value, create a
     * <code>PolicyValue</code> instance of the correct type and return it.
     *
     * <strong>
     * It should be noted that the <code>PolicyValue</code> returned should
     * be an immutable object.
     * </strong>
     *
     * @param policyValue The value to create a correctly typed instance of
     *                    <code>PolicyValue</code> around.
     * @param type        The type of the value.
     *
     * @return An initialized PolicyValue containing the value provided
     *         expressed as the correct type.
     * @throws DeviceRepositoryException if the given value is not of the
     * specified policy type.
     */
    private PolicyValue createPolicyValue(String policyValue, PolicyType type)
            throws DeviceRepositoryException {
        PolicyValue value = null;

        if (type instanceof SimplePolicyType) {
            value = createSimplePolicyValue(policyValue, (SimplePolicyType) type);
        } else if (type instanceof CompositePolicyType) {
            if (type instanceof OrderedSetPolicyType) {
                OrderedSetPolicyType orderedSetType =
                        (OrderedSetPolicyType) type;
                value = new DefaultOrderedSetPolicyValue(
                        orderedSetType.getMemberPolicyType(), policyValue);
            } else if (type instanceof UnorderedSetPolicyType) {
                UnorderedSetPolicyType unorderedSetType =
                        (UnorderedSetPolicyType) type;
                value = new DefaultUnorderedSetPolicyValue(
                        unorderedSetType.getMemberPolicyType(), policyValue);
            }
        } else {
            logger.warn("policy-type-unknown", new Object[]{type});
        }
        return value;
    }

    /**
     * Given a policy value, and the known simple type of that value, create a
     * <code>SimplePolicyValue</code> instance of the correct type and
     * return it.
     *
     * <strong>
     * It should be noted that the <code>PolicyValue</code> returned should
     * be an immutable object.
     * </strong>
     *
     * @param policyValue The value to create a correctly typed instance of
     *                    <code>SimplePolicyValue</code> around. Must not be
     *                    null.
     * @param type        The type of the value.
     *
     * @return An initialized SimplePolicyValue containing the value provided
     *         expressed as the correct type.
     * @throws DeviceRepositoryException if the given value is not of the
     * specified policy type.
     */
    private SimplePolicyValue createSimplePolicyValue(String policyValue,
                                                      SimplePolicyType type)
            throws DeviceRepositoryException {
        SimplePolicyValue value = null;
        if (type instanceof BooleanPolicyType) {
            value = new DefaultBooleanPolicyValue(policyValue);
        } else if (type instanceof IntPolicyType) {
            if (policyValue.length() > 0) {
                value = new DefaultIntPolicyValue(policyValue);
            }
        } else if (type instanceof RangePolicyType) {
            if (policyValue.length() > 0) {
                value = new DefaultRangePolicyValue(policyValue);
            }
        } else if (type instanceof SelectionPolicyType) {
            value = new DefaultSelectionPolicyValue(policyValue);
        } else if (type instanceof TextPolicyType) {
            value = new DefaultTextPolicyValue(policyValue);
        } else {
            logger.warn("policy-type-unknown-simple", new Object[]{type});
        }
        return value;
    }

    /**
     * Given a policy name and associated device for a structured type, create
     * a <code>StructuredPolicyValue</code> instance of the correct type and
     * return it.
     *
     * <strong>
     * It should be noted that the <code>PolicyValue</code> returned should
     * be an immutable object.
     * </strong>
     *
     * @param device     The device on which the policies of interested are
     *                   registered.
     * @param policyName The policy for which the value should be retrieved
     * @param type       The type of the value, which has to be a structured
     *                   type.  This is necesary because of the extra
     *                   information it contains.
     *
     * @return An initialized PolicyValue containing the value provided
     *         expressed as the correct type.
     */
    private PolicyValue createStructuredPolicyValue(DefaultDevice device,
                                                    String policyName,
                                                    StructurePolicyType type) {
        DefaultStructurePolicyValue value = new DefaultStructurePolicyValue();

        Map fieldTypes = type.getFieldTypes();

        Set keys = fieldTypes.keySet();

        // Handle each of the field types for the structured type
        for (Iterator i = keys.iterator(); i.hasNext();) {
            try {
                String fieldName = (String) i.next();
                String fullName = policyName + "." + fieldName;

                SimplePolicyValue fieldValue = null;
                PolicyType fieldType = (PolicyType) fieldTypes.get(fieldName);
                if (!(fieldType instanceof SimplePolicyType)) {
                    throw new IllegalStateException(
                            "Can only have simple types inside a structured type");
                }
                // Extract the policy value as a string.
                String valueString = device.getComputedPolicyValue(fullName);
                // If we found a string value...
                if (valueString != null) {
                    // Create a policy value object for the string value.
                    fieldValue = createSimplePolicyValue(valueString,
                            (SimplePolicyType) fieldType);
                }
                // Else, there was no value for that name, so use null.

                // Add the value object as a child of the structure value.
                value.addField(fieldName, fieldValue);

            } catch (DeviceRepositoryException dre) {
                logger.warn("structured-fields-no-info", new Object[]{policyName});
            }
        }

        value.complete();
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Apr-05	7679/1	allan	VBM:2005041320 SmartClient Packager - minimal testing

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/9	doug	VBM:2004111702 refactored logging framework

 07-Dec-04	6232/7	doug	VBM:2004111702 Refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 06-Dec-04	6377/1	geoff	VBM:2004112307 Device.getRealPolicyValue(fallback) fails with NullPointerException

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 08-Oct-04	5755/3	geoff	VBM:2004092209 NullPointerException thrown when Accessing DeviceRepository API

 08-Oct-04	5755/1	geoff	VBM:2004092209 NullPointerException thrown when Accessing DeviceRepository API

 21-Sep-04	5569/1	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/3	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 03-Sep-04	5408/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 03-Sep-04	5387/1	byron	VBM:2004090109 ClassCastException when calling getRealPolicyValue()

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4940/1	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 28-Jul-04	4952/3	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
