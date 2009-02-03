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
package com.volantis.devrep.repository.api.devices;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.devices.policy.values.InternalPolicyValue;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.synergetics.ArrayCache;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A default implementation of {@link com.volantis.mcs.devices.Device}.
 * <p>
 * This wraps {@link PolicyValueFactory} to do most of the work of returning
 * policy values.
 * <p>
 * NOTE: this implements {@link Externalizable} so that it may be stored
 * directly in the servlet session.
 * <p>
 * NOTE: The implementation of Externalizable provided with this class is
 * trivial. This means that instances of this class must be checked to see
 * if they are are valid (using {@link #isValid}) before use if they may have
 * been constructed via serialisation. This is typically the case if the
 * instance has been stored in a persistable session (eg when clustering).
 *
 * @mock.generate
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class DefaultDevice implements Device, Externalizable {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER = 
            LocalizationFactory.createLogger(DefaultDevice.class);

    /**
     * Policies for a device.
     */
    private Map policies;

    /**
     * The name of the device.
     */
    private String name;

    /**
     * The device patterns.
     */
    private Map patterns;

    /**
     * A factory to translate string policy values into {@link PolicyValue}
     * objects when necessary, using the metadata provided by
     * {@link com.volantis.mcs.devices.policy.PolicyDescriptor}.
     */
    private PolicyValueFactory valueFactory;

    /**
     * Flag to indicate if this instance is in a valid state.
     * <p>
     * This will be true if constructed normally and false if constructed via
     * the null constructor, which will be the case if constructed via
     * serialisation.
     */
    private boolean valid;

    /**
     * Collection of the names (as Strings) of headers that were used to
     * identify the device.
     */
    private Collection identificationHeaderNames;

    /**
     * Locking object for the secondary ID cache
     */
    private final Object cacheLock;

    /**
     * The TACs associated with this device
     */
    private Set tacValues;

    /**
     * Secondary devices
     */
    private ArrayCache secondaryDevices;

    /**
     * Fallback device.
     */
    private DefaultDevice fallbackDevice;

    /**
     * Map of properties.
     */
    private Map properties;

    /**
     * Create a new <code>DefaultDevice</code>, this is <strong>only</strong>
     * to be used by serialisation when the object is Externalized and
     * thereforce expects readExternal() to be called.
     * <p>
     * Instances constructed this way are not 'valid' and should not be used.
     */
    public DefaultDevice() {
        valid = false;
        cacheLock = new Object();
    }

    /**
     * Initialise.
     */
    public DefaultDevice(final String name, final Map policies,
                         final PolicyValueFactory valueFactory) {
        this.valid = true;
        this.name = name;
        if (policies == null) {
            this.policies = Collections.synchronizedMap(new HashMap());
        } else {
            this.policies = Collections.synchronizedMap(policies);
        }
        this.valueFactory = valueFactory;
        cacheLock = new Object();
        properties = Collections.synchronizedMap(new HashMap());
    }

    // Javadoc inherited.
    public String getName() {
        ensureValid();
        return name;
    }

    /**
     * Set the name of the device.
     *
     * @param name The name of the device.
     */
    public void setName(String name) {
        ensureValid();
        this.name = name;
    }

    // Javadoc inherited.
    public String getPolicyValue(String policyName) {
        ensureValid();
        return (String) policies.get(policyName);
    }

    /**
     * Set the value for the specified policy.
     *
     * @param policy The name of the policy to set.
     * @param value  The new value for the policy.
     */
    public void setPolicyValue(String policy, String value) {
        ensureValid();
        if(value != null) {
            value = value.intern();
        }

        policies.put(policy.intern(), value);
    }

    /**
     * Returns an iterator over the available policy names.
     *
     * @return the iterator
     */
    public Iterator getPolicyNames() {
        ensureValid();
        return policies.keySet().iterator();
    }

    /**
     * Remove the setting of the specified policy.
     *
     * @param policy The name of the policy to remove.
     */
    public void removePolicyValue(String policy) {
        ensureValid();
        policies.remove(policy);
    }

    /**
     * Sets the map of patterns that are used to match this device.
     *
     * @param patterns the map of patterns
     */
    public void setPatterns(final Map patterns) {
        ensureValid();
        this.patterns = patterns;
    }

    /**
     * Get the patterns which are used to match this device.
     *
     * @return A collection of the String patterns, may be null.
     */
    public Map getPatterns() {
        ensureValid();
        return patterns;
    }

    /**
     * Set the TACs that are associated with this device.
     *
     * @param tacs A Set of instances of TACValue.
     */
    public void setTACValues(Set tacs) {
        tacValues = tacs;
    }

    /**
     * Get the TACs that are associated with this device.
     *
     * @return A Set of instances of TACValue, may be null.
     */
    public Set getTACValues() {
        return tacValues;
    }

    /**
     * Return the secondary identification header name if there is one. If there isnt, return
     * null. Note that this method does NOT call getPolicyValue() as that method looks at
     * fallback devices which would be disastrous in this instance as this device may have
     * been loaded as a secondary device of it's fallback. That would cause this device to be
     * re-loaded indefinately.
     *
     * @return the name of the secondary ID header or null
     */
    public String getSecondaryIDHeaderName() {
        ensureValid();
        return (String) policies.get("sec.id.header");
    }
    /**
     * Add a secondary device to the device cache.
     *
     * @param hdr the value of the secondary ID request header
     * @param dev the resolved device
     *
     * todo This is used in a thread unsafe manner.
     */
    public void addSecondaryDevice(String hdr, DefaultDevice dev) {
        synchronized (cacheLock) {
            if (secondaryDevices == null) {
                secondaryDevices = new ArrayCache(5);
            }

            // InternalDevice may have been added in the meantime. If so dont add this one
            // but check that it is consistant.
            Device newDev = (DefaultDevice) secondaryDevices.get(hdr);
            if (newDev != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("addSecondaryDevice() : '" + hdr +
                            "' already mapped to " + newDev.getName());
                }
                // Should always map to same device
                if (!dev.equals(newDev)) {
                    throw new IllegalArgumentException("Attempting to remap " + hdr);
                }
            } else {
                secondaryDevices.put(hdr, dev);
            }
        }
    }

    /**
     * Get a secondary device from the cache
     *
     * @param hdr the value of the secondary ID request header
     * @return the resolved device or null
     */
    public DefaultDevice getSecondaryDevice(String hdr) {
        synchronized (cacheLock) {
            DefaultDevice dev = null;
            if (secondaryDevices != null) {
                dev = (DefaultDevice) secondaryDevices.get(hdr);
            }
            return dev;
        }
    }

    // Javadoc inherited.
    public PolicyValue getRealPolicyValue(String policyName) {
        ensureValid();
        return valueFactory.createPolicyValue(this, policyName);
    }

    // Javadoc inherited.
    public ImmutableMetaDataValue getPolicyMetaDataValue(String policyName) {
        ensureValid();

        InternalPolicyValue policyValue = (InternalPolicyValue)
                getRealPolicyValue(policyName);
        ImmutableMetaDataValue metaDataValue;
        if (policyValue == null) {
            metaDataValue = null;
        } else {
            metaDataValue = policyValue.createMetaDataValue();
        }

        return metaDataValue;
    }

    public String getComputedPolicyValue(String policy) {
        String value = getPolicyValue(policy);
        if (value == null || value.length() == 0) {

            // The policy could not be found in this device so see whether it is
            // available in the fallback device (if available).
            if (fallbackDevice != null) {
                value = fallbackDevice.getComputedPolicyValue(policy);

                if (value == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Requested policy " + policy
                                + " UNKNOWN for device " + getName());
                    }
                }
            }
        }
        return value;
    }

    /**
     * Returns the name of the fallback device or null if no such device exists.
     *
     * @return the name of the fallback device
     */
    public String getFallbackDeviceName() {
        return getPolicyValue(DevicePolicyConstants.FALLBACK_POLICY_NAME);
    }

    /**
     * Sets the fallback device.
     *
     * @param fallbackDevice the new fallback device
     */
    public void setFallbackDevice(final DefaultDevice fallbackDevice) {
        setPolicyValue(DevicePolicyConstants.FALLBACK_POLICY_NAME,
            fallbackDevice.getName());
        this.fallbackDevice = fallbackDevice;
    }

    /**
     * Returns the fallback device or null.
     *
     * @return the fallback device
     */
    public DefaultDevice getFallbackDevice() {
        return fallbackDevice;
    }

    /**
     * Returns true if this device is currently in a valid state.
     * <p/>
     * This will be false if the device was constructed via serialisation.
     *
     * @return true if this device is currently in a valid state.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Throws a runtime exception if we are in an invalid state.
     */
    private void ensureValid() {
        if (!valid) {
            throw new IllegalStateException("attempt to use invalid device");
        }
    }

    /**
     * Write the Externalizable objects out. This is called by the cluster
     * node during transition/replication.
     *
     * @param out The ObjectOutput stream.
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        // This intentionally does nothing.
    }

    /**
     * Read the Externalizable objects in. This is called by the cluster
     * node during transition/replication.
     *
     * @param in The ObjectInput stream.
     */
    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {
        // This intentionally does nothing.
    }

    /**
     * Sets the names of the headers that were used to identify the device.
     *
     * @param headerNames collection of header names (as Strings).
     */
    public void setIdentificationHeaderNames(final Collection headerNames) {
        this.identificationHeaderNames = headerNames;
    }

    /**
     * Returns the collection of the names (as Strings) of headers that were
     * used to identify the device.
     */
    public Collection getIdentificationHeaderNames() {
        return Collections.unmodifiableCollection(identificationHeaderNames);
    }

    /**
     * Stores a property in the device.
     *
     * @param propertyName the name of the property
     * @param propertyValue the value of the property
     */
    public void setProperty(final String propertyName,
                            final Object propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    /**
     * Returns the property value for the specified property name.
     *
     * @param propertyName the name of the property
     * @return the value of the property
     */
    public Object getProperty(final String propertyName) {
        return properties.get(propertyName);
    }
}
