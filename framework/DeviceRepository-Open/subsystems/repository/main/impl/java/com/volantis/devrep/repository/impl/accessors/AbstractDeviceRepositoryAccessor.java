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
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Sep-01    Allan           VBM:2001083118 - Add refreshDevicePatternCache
 *                              and refreshDeviceCache methods. Added this
 *                              change history.
 * 08-Oct-01    Paul            VBM:2001100801 - Closed enumeration used in
 *                              retrieveMatchingDeviceName.
 * 11-Oct-01    Doug            VBM:2001100906 - Modified the method
 *                              retrieveMatchingDeviceName to handle the
 *                              modifications made to the devicePatternCache.
 *                              The devicePatternCache is now a SortedSet as
 *                              opposed to a SortedMap. Also it now stores
 *                              DevicePattern objects.
 * 15-Oct-01    Paul            VBM:2001101202 - Added setPolicyValue
 *                              methods which is needed by the user interface,
 *                              added getDeviceFallbackChain to get a device
 *                              and all its fallback devices, this
 *                              functionality has now been removed from
 *                              retrieveDevice.
 * 24-Oct-01    Paul            VBM:2001092608 - Added enumerateOrphanedDevices
 *                              and enumerateDevicesChildrenImpl.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Removed dependency on rex
 *                              classes.
 * 29-Jan-02    Allan           VBM:2001121703 - Added renameDevice() and
 *                              renameDeviceImpl(). Modified removeDevice() to
 *                              recursively remove all the children of the
 *                              device being removed. Modified setPolicyValue
 *                              method to call removeDeviceImpl() so as to
 *                              avoid removing the child of the device being
 *                              updated.
 * 29-Jan-02    Allan           VBM:2001121703 - Added removePolicy().
 * 20-Feb-02    Doug            VBM:2002011405 - Modified removeDevice() so
 *                              that the method no longer removes the devices
 *                              children. Added another removeDevice() method
 *                              that takes a boolean argument specifying
 *                              whether to remove any children.
 * 27-Feb-02    Doug            VBM:2002011405 - Backed out previous edits
 *                              changes so that removeDevice() again removes
 *                              a devices child devices.
 * 06-Mar-02    Allan           VBM:2002030504 - retrieveMatchingDeviceName()
 *                              modified to only ever look for patterns in the
 *                              pattern cache.
 * 12-Mar-02    Doug            VBM:2002030804 - Fixed bug in removeDevice()
 *                              where a RepositoryEnumeration object was never
 *                              being closed.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 03-Apr-02    Adrian          VBM:2001102414 - Use RepositoryObjectIdentity
 *                              for cache key.
 * 17-May-02    Byron           VBM:2002041502 - Implement a faster algorithm
 *                              for looking up the device names given a pattern
 *                              Added inner class: DevicePatternCache with
 *                              its own inner class KeyInfo. DevicePatternCache
 *                              contains methods: initializeKeyInfo(),
 *                              mapPatternAndDevice(), outputPatternCache(),
 *                              getBinaryKey(),  getPatternMapKey(),
 *                              getIterator(), isValidMatch()
 *                              Modified: initializeDevicePatternCache(),
 *                              retrieveMatchingDeviceName()
 * 20-May-02  Byron             VBM:2002041501 - Updated outputPatternCache()
 *                              to show the contents of the sorted TreeSet
 * 27-May-02  Byron             VBM:2002041502 - Improve the algorithm to cater
 *                              for keys with one char wildcards and modified
 *                              keys correctly containing '\.' characters
 * 11-Jul-02  Byron             VBM:2002041502 - Modified inner class KeyInfo
 *                              methods getValue() and getIndex() to take a
 *                              parameter to use the expanded or default string
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl.accessors;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.PolicyDescriptorAccessor;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.devrep.repository.impl.devices.DefaultPolicyDescriptorAccessor;
import com.volantis.devrep.repository.impl.devices.policy.values.DefaultPolicyValueFactory;
import com.volantis.devrep.repository.impl.accessors.DevicePatternCache;
import com.volantis.devrep.repository.impl.DeviceTACPair;
import com.volantis.devrep.repository.impl.TACValue;
import com.volantis.mcs.accessors.CollectionRepositoryEnumeration;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cache.GenericCache;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * This class implements common behaviour for most DeviceRepositoryAccessor
 * classes.
 */
public abstract class AbstractDeviceRepositoryAccessor
        implements DeviceRepositoryAccessor {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    AbstractDeviceRepositoryAccessor.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    AbstractDeviceRepositoryAccessor.class);


    /**
     * X-WAP-PROFILE special header
     */
    private static String X_WAP_PROFILE = "x-wap-profile";
    /**
     * The Device cache.
     */
    private GenericCache deviceCache;

    /**
     * The Device cache.
     */
    private DevicePatternCache devicePatternCache;

    /**
     * The TAC cache
     */
    private Map tacCache;

    private PolicyValueFactory policyValueFactory;

    private final DefaultPolicyDescriptorAccessor policyDescriptorAccessor;

    protected AbstractDeviceRepositoryAccessor(final LocalRepository repository) {
        /**
         * The factory for creating policy values; uses the policy type meta data
         * and the old textual device policy values.
         */
        policyDescriptorAccessor =
            new DefaultPolicyDescriptorAccessor(repository, this);
        policyValueFactory =
            new DefaultPolicyValueFactory(policyDescriptorAccessor);
    }

    public PolicyDescriptorAccessor getPolicyDescriptorAccessor() {
        return policyDescriptorAccessor;
    }

    public PolicyValueFactory getPolicyValueFactory() {
        return policyValueFactory;
    }

    // Javadoc inherited from super class.
    public void setDeviceCache(GenericCache deviceCache) {
        this.deviceCache = deviceCache;
    }

    /**
     * Refresh the device caches. The refresh mechanism is to simply clear the
     * cache.
     */
    public synchronized void refreshDeviceCache() {
        if (deviceCache != null) {
            deviceCache.clear();
            if (logger.isDebugEnabled()) {
                logger.debug("Refreshed device cache");
            }
        } else {
            logger.warn("cannot-refresh-null-cache");
        }
        if (devicePatternCache != null) {
            devicePatternCache.clear();
            if (logger.isDebugEnabled()) {
                logger.debug("Refreshed device pattern cache");
            }
        } else {
            logger.warn("cannot-refresh-null-cache");
        }
        tacCache = null;
    }

    /**
     * Enumerate the device patterns.
     *
     * RepositoryEnumeration.next () returns an array of two Strings, the first
     * of which is the name of a device and the second is the pattern which
     * matches the user agent strings associated with that device.
     *
     * @param connection The connection to use to access the repository.
     * @return An enumeration of device names and patterns.
     */
    public abstract RepositoryEnumeration enumerateDevicePatterns(RepositoryConnection connection)
            throws RepositoryException;


    /**
     * Pre-load the cache with all device patterns for better performance.
     *
     * @param connection The connection to use to access the repository.
     * @throws RepositoryException If there was a problem accessing the
     * repository.
     */
    public synchronized void initializeDevicePatternCache(RepositoryConnection connection)
            throws RepositoryException {

        if (devicePatternCache == null) {
            devicePatternCache = new DevicePatternCache();
        }

        RepositoryEnumeration enumeration = enumerateDevicePatterns(connection);
        // count the number of items we add to the cache.
        // Remember duplicate items will be discarded
        int count = 0;
        try {
            while (enumeration.hasNext()) {
                String[] pair = (String[]) enumeration.next();
                String deviceName = pair[0];
                String pattern = normalizePattern(pair[1]);

                try {
                    devicePatternCache.mapPatternAndDevice(pattern.trim(),
                                                           deviceName.intern());
                    count++;
                } catch (IllegalArgumentException e) {
                    enumeration.close();
                    throw new RepositoryException(
                            exceptionLocalizer.format(
                                    "unexpected-illegal-argument-exception"),
                            e);
                }
            }
        } finally {
            enumeration.close();
        }

        int cacheSize = devicePatternCache.size();
        if (logger.isDebugEnabled()) {
            logger.debug("Initialized devicePatternCache with " +
                         cacheSize + " elements");
        }

        if (count != cacheSize) {
            logger.info("device-pattern-count-error", new Object[]{
                new Integer(count), new Integer(cacheSize)});
        }
//    devicePatternCache.outputPatternCache();
    }

    /**
     * The given pattern is modified, as necessary, to normalize any header
     * name prefix. The latter is determined by looking for a string of alpha,
     * "-" or "_" characters followed immediately by a colon (":") character.
     * If the string doesn't conform to this pattern then the string is not
     * a header name and should not be normalized. Normalization involves
     * conversion to lower case as specified in Requirement 23.
     *
     * @param pattern the pattern what may need to be normalized
     * @return the possibly normalized pattern
     */
    protected String normalizePattern(String pattern) {
        String result = pattern;
        final int colon = pattern.indexOf(':');

        if (colon != -1) {
            // The pattern contains a colon. Check that all characters before
            // this are alpha/"-"/"_" only.
            boolean header = true;

            for (int i = 0; header && (i < colon); i++) {
                final char ch = pattern.charAt(i);

                header = Character.isLetter(ch) ||
                        (ch == '-') ||
                        (ch == '_');
            }

            if (header) {
                // It is a header prefix, so normalize that part of the pattern

                String headerName = pattern.substring(0,colon).toLowerCase();
                if (X_WAP_PROFILE.equals(headerName)) {
                    headerName="profile";
                }
                String remainder = pattern.substring(colon);

                result = headerName + remainder;
            }
        }

        return result;
    }

    // Javadoc inherited from super class.
    public String retrieveMatchingDeviceName(String userAgent)
            throws RepositoryException {

        String matchingDeviceName = null;

        if (devicePatternCache != null) {
            matchingDeviceName = devicePatternCache.match(userAgent);
        }
        return matchingDeviceName;
    }

    // Javadoc inherited from super class.
    public DefaultDevice getDeviceFallbackChain(RepositoryConnection connection,
                                                 String deviceName)
            throws RepositoryException {

        DefaultDevice device = retrieveDevice(connection, deviceName);
        if (device == null) {
            return null;
        }

        String fallbackDeviceName = device.getFallbackDeviceName();
        if (fallbackDeviceName != null) {
            device.setFallbackDevice(getDeviceFallbackChain(connection,
                                                            fallbackDeviceName));
        }

        return device;
    }

    /**
     * Calls an abstract method to add the device to the repository first
     * and then it adds it to the cache.
     *
     * @see #addDeviceImpl
     */
    // Some javadoc inherited from super class.
    public void addDevice(RepositoryConnection connection,
                          Device device)
            throws RepositoryException {

        // Add it to the repository first.
        addDeviceImpl(connection, (DefaultDevice) device);

        // Then add it to the cache.
        addDeviceToCache((DefaultDevice) device);
    }

    /**
     * Add the specified device to the repository.
     *
     * @param connection The connection to use to access the repository.
     * @param device The device to add.
     * @throws RepositoryException If there was a problem accessing the
     * repository.
     *
     * @see DeviceRepositoryAccessor#addDevice(RepositoryConnection, Device)
     */
    protected abstract
            void addDeviceImpl(RepositoryConnection connection,
                               DefaultDevice device)
            throws RepositoryException;


    /**
     * Calls an abstract method to remove the device from the repository first
     * and then it removes it from the cache.
     * @see #removeDeviceImpl
     */
    public void removeDevice(RepositoryConnection connection,
                             String deviceName,
                             boolean removeChildren)
            throws RepositoryException {

        if (removeChildren) {

            RepositoryEnumeration children = enumerateDevicesChildren(connection,
                                                                      deviceName);
            try {
                while (children.hasNext()) {
                    String childName = (String) children.next();
                    removeDevice(connection, childName, removeChildren);
                }
            } finally {
                children.close();
            }
        }
        // Remove it from the repository first.
        removeDeviceImpl(connection, deviceName);

        // Then remove it from the cache.
        removeDeviceFromCache(deviceName);
    }

    // Some javadoc inherited from super class.
    public void removeDevice(RepositoryConnection connection, String deviceName)
            throws RepositoryException {

        removeDevice(connection, deviceName, true);
    }

    /**
     * Remove the specified device from the repository.
     *
     * @param connection The connection to use to access the repository.
     * @param deviceName The name of the device to remove.
     * @throws RepositoryException If there was a problem accessing the
     * repository.
     *
     * @see DeviceRepositoryAccessor#removeDevice(RepositoryConnection,String)
     */
    protected abstract
            void removeDeviceImpl(RepositoryConnection connection,
                                  String deviceName)
            throws RepositoryException;

    /**
     * Looks in the cache for the device first, if it could not be found then
     * it calls an abstract method to look in the repository. If the device was
     * found in the repository then it is added to the cache.
     *
     * @see #retrieveDeviceImpl
     */
    public DefaultDevice retrieveDevice(RepositoryConnection connection,
                                         String deviceName)
            throws RepositoryException {

        // Look in the cache first.
        DefaultDevice device = getDeviceFromCache(deviceName);
        if (device != null) {
            return device;
        }

        if (deviceCache != null) {
            synchronized (deviceCache) {
                device = getDeviceFromCache(deviceName);
                if (device == null) {
                    try {
                        // Now try looking in the repository.
                        initializeConnection(connection);
                        device = retrieveDeviceImpl(connection, deviceName);
                        // If a device was found the store it in the cache.
                        if (device != null) {
                            addDeviceToCache(device);
                        }
                    } finally {
                        releaseConnection(connection);
                    }
                }
            }
        } else {
            try {
                initializeConnection(connection);
                device = retrieveDeviceImpl(connection, deviceName);
            } finally {
                releaseConnection(connection);
            }
        }

        return device;
    }

    // JavaDoc inherited
    public List enumerateDeviceNames(RepositoryConnection connection,
                                     String deviceNamePattern)
            throws RepositoryException {
        // Create an empty list - this means an emopty list rather than null
        // is returned if no values that match are found
        List deviceNames = new ArrayList();

        // Create a suitable string from which to build the regular expression
        String regexp = null;
        boolean allDevices = false;

        if (deviceNamePattern.equals("*")) {
            allDevices = true;
            regexp = ".*";
        } else if (deviceNamePattern.startsWith("*")) {
            // Pattern was specified as *pattern
            regexp = ".*" + deviceNamePattern.substring(1) + "$";
        } else if (deviceNamePattern.endsWith("*")) {
            // Pattern was specified as pattern*
            regexp = "^"
                    + deviceNamePattern.substring(0,
                                                  deviceNamePattern.length() - 1)
                    + ".*";
        } else {
            // Pattern did not contain any wildcards
            regexp = deviceNamePattern;
        }

        // Build the regular expression to use
        Pattern expression = Pattern.compile(regexp);

        // Retrieve all the device names
        RepositoryEnumeration enumeration = enumerateDeviceNames(connection);

        // Check each device name returned against the expression and
        // store those that match to be returned
        while (enumeration.hasNext()) {
            String name = (String) enumeration.next();
            if (allDevices || expression.matcher(name).matches()) {
                deviceNames.add(name);
            }
        }


        // Return all matching device names
        return deviceNames;
    }

    /**
     * <h6>Should be overriden by specialisations which require that
     * repository connections are initialised before use.</h6>
     *
     * @param connection The {@link RepositoryConnection} which wraps a
     * physical connection the repository.
     * @throws RepositoryException If there was a problem establishing the
     * connection to the repository.
     */
    protected void initializeConnection(RepositoryConnection connection)
            throws RepositoryException {
        // by default we do not need to perform any processing here.
    }

    /**
     * <h6>Should be overriden by specialisations which require that
     * repository connections are released after use.</h6>
     *
     * @param connection The {@link RepositoryConnection} which wraps a
     * physical connection the repository.
     * @throws RepositoryException If there was a problem releasing the
     * connection to the repository.
     */
    protected void releaseConnection(RepositoryConnection connection)
            throws RepositoryException {
        // by default we do not need to perform any processing here.
    }

    /**
     * Retrieve the specified device from the repository.
     *
     * @param connection The connection to use to access the repository.
     * @param deviceName The name of the device to retrieve.
     * @return The device which was retrieved from the repository, or null if it
     * could not be found.
     * @throws RepositoryException If there was a problem accessing the
     * repository.
     *
     * @see DeviceRepositoryAccessor#retrieveDevice
     */
    protected abstract
            DefaultDevice retrieveDeviceImpl(RepositoryConnection connection,
                                              String deviceName)
            throws RepositoryException;

    // Javadoc inherited from super class.
    public RepositoryEnumeration enumerateDevicesChildren(RepositoryConnection connection,
                                                          String deviceName)
            throws RepositoryException {

        // If the device name is null then we need to retrieve the root device
        // and any orphaned devices, they are ones whose fallbacks are not present
        // in the repository.
        if (deviceName == null) {
            return enumerateOrphanedDevices(connection);
        } else {
            return enumerateDevicesChildrenImpl(connection, deviceName);
        }
    }

    /**
     * Return an enumeration of the names of the specified devices children.
     *
     * RepositoryEnumeration.next () returns a String which is the name of a
     * device which fallbacks to the specified device.
     *
     * @param connection The connection to use to access the repository.
     * @param deviceName The name of the device.
     * @return An enumeration of device names.
     */
    protected abstract
            RepositoryEnumeration enumerateDevicesChildrenImpl(
            RepositoryConnection connection,
            String deviceName)
            throws RepositoryException;

    /**
     * Return an enumeration of the names of the orphaned devices, including the
     * root device.
     * <p>
     * RepositoryEnumeration.next () returns a String which is the name of an
     * orphaned device.
     * </p>
     * @param connection The connection to use to access the repository.
     * @return An enumeration of orphaned device names.
     */
    protected RepositoryEnumeration enumerateOrphanedDevices(
            RepositoryConnection connection)
            throws RepositoryException {

        // Create a map of device names to fallbacks.
        Map deviceToFallback = new HashMap();

        String deviceName;
        String fallbackName;

        // Enumerate all the devices and their fallbacks and populate the hash
        // table.
        RepositoryEnumeration e = enumerateDeviceFallbacks(connection);
        try {
            while (e.hasNext()) {
                String[] pair = (String[]) e.next();
                deviceName = pair[0];
                fallbackName = pair[1];
                deviceToFallback.put(deviceName, fallbackName);
            }
        } finally {
            e.close();
        }

        // Now enumerate through the table finding any devices whose fallback is
        // not in the table and add them to the list.
        List orphaned = new ArrayList();
        for (Iterator i = deviceToFallback.entrySet().iterator();
             i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            deviceName = (String) entry.getKey();
            fallbackName = (String) entry.getValue();
            if (!deviceToFallback.containsKey(fallbackName)) {
                orphaned.add(deviceName);
            }
        }

        return new CollectionRepositoryEnumeration(orphaned);
    }

    /**
     * Get the device from the cache.
     *
     * @param deviceName The name of the device.
     * @return The InternalDevice found in the cache, or null if it could not be found.
     */
    protected DefaultDevice getDeviceFromCache(String deviceName) {

        if (deviceCache == null) {
            return null;
        }

        DefaultDevice device = (DefaultDevice) deviceCache.get(deviceName);

        if (device != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Retrieved device named \"" + deviceName
                             + "\" from cache");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not find device named \"" + deviceName
                             + "\" in cache");
            }
        }

        return device;
    }

    /**
     * Add the device to the cache.
     *
     * @param device     The device to add to the cache, the device name is
     */
    protected void addDeviceToCache(Device device) {

        if (deviceCache == null) {
            return;
        }

        if (deviceCache.put(device.getName(), device) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Added device named \"" + device.getName()
                             + "\" to cache");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Updated device named \"" + device.getName()
                             + "\" in cache");
            }
        }
    }

    /**
     * Remove the device from the cache.
     *
     * @param deviceName The name of the device.
     */
    protected void removeDeviceFromCache(String deviceName) {

        if (deviceCache == null) {
            return;
        }

        DefaultDevice device = (DefaultDevice) deviceCache.remove(deviceName);

        if (device == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not find device named \"" + deviceName
                             + "\" in cache");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Removed device named \"" + deviceName
                             + "\" from cache");
            }
        }
    }

    // javadoc inherited
    // todo: rename appears to be unused.
    // DeviceTransferAccessor is the only usage of mutators, and it only uses add/remove
    // todo: remove other unused mutators, eg updatePolicyName
    public void renameDevice(RepositoryConnection connection,
                             String deviceName, String newName)
            throws RepositoryException {

        // Look in the cache first.
        DefaultDevice device = (DefaultDevice) getDeviceFromCache(deviceName);
        if (device != null) {
            removeDeviceFromCache(deviceName);
            device.setName(newName);
            addDeviceToCache(device);
        }

        renameDeviceImpl(connection, deviceName, newName);
    }

    /**
     * Rename the named device.
     * @param connection The connection to use to access the repository.
     * @param deviceName The name of the device.
     * @param newName The new name for the device.
     * @throws RepositoryException If there was a problem accessing the
     * repository.
     */
    protected abstract
            void renameDeviceImpl(RepositoryConnection connection,
                                  String deviceName, String newName)
            throws RepositoryException;

    /**
     * Initialise the cache of TAC numbers to provide fast look-up of TACs.
     *
     * @param connection The connection to use to access the repository.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    private Map initialiseTACCache(RepositoryConnection connection)
            throws RepositoryException {
        Map cache = null;
        synchronized (this) {
            if (tacCache == null) {
                cache = new HashMap();
                RepositoryEnumeration deviceTACPairs =
                        enumerateDeviceTACs(connection);
                while (deviceTACPairs.hasNext()) {
                    DeviceTACPair pair = (DeviceTACPair) deviceTACPairs.next();
                    cache.put(new TACValue(pair.getTAC()),
                              pair.getDeviceName());
                }
                tacCache = cache;
            } else {
                cache = tacCache;
            }
        }
        return cache;
    }

    // Javadoc inherited
    public String retrieveDeviceName(
            RepositoryConnection connection,
            long tac)
            throws RepositoryException {
        Map tacCacheLoc = null;
        synchronized (this) {
            tacCacheLoc = tacCache;
        }
        if (tacCacheLoc == null) {
            tacCacheLoc = initialiseTACCache(connection);
        }

        // The creation of a new TACValue object with each query could be
        // avoided by using a manual implementation of the cache (based on
        // a binary search, perhaps) but this implementation is clearer,
        // and more flexible in terms of expected future changes to the TAC
        // format.
        return (String) tacCacheLoc.get(new TACValue(tac));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10765/1	ibush	VBM:2005120822 Fixed DevicePattern matching for X-WAP-PROFILE special cases in secondary headers

 09-Dec-05	10742/1	ibush	VBM:2005120822 Fixed DevicePattern matching for X-WAP-PROFILE special cases in secondary headers

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 01-Nov-05	10055/1	rgreenall	VBM:2005092902 Fixed fallback identification of DoCoMo devices.

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 02-Mar-05	7130/6	rgreenall	VBM:2005011201 Further modifications post review.

 02-Mar-05	7130/3	rgreenall	VBM:2005011201 Post review corrections

 02-Mar-05	7130/1	rgreenall	VBM:2005011201 Fixed bug where the mapping of a user agent pattern to a device name would fail if the pattern was one character greater than the device name.

 01-Mar-05	6623/1	philws	VBM:2005010602 Port of correction for migration and other handling of secondary ID headers from 3.3

 01-Mar-05	7167/1	philws	VBM:2005010602 Correct migration and other handling of secondary ID headers

 31-Dec-04	6567/1	byron	VBM:2004123001 Externalise the list of common UA strings used in device identification

 14-Dec-04	6472/1	allan	VBM:2004121003 Intern device names and device property names

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 30-Sep-04	4511/2	tom	VBM:2004052005 Added short column support for new table columns and cache accessors

 13-Aug-04	5187/1	adrianj	VBM:2004080302 UAProf URI caching mechanism

 06-Aug-04	5121/3	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC (rework issues)

 06-Aug-04	5121/1	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC

 04-Aug-04	5065/3	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 28-Jul-04	4935/3	claire	VBM:2004072106 Public API for Device Repository: Retrieve devices with pattern matching; supermerge

 22-Jul-04	4935/1	claire	VBM:2004072106 Public API for Device Repository: Retrieve devices with pattern matching

 05-May-04	4070/1	ianw	VBM:2004042602 Search for generic device patterns outside of buckets

 11-Mar-04	3376/3	adrian	VBM:2004030908 Rework to fix javadoc duplication

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jan-04	2626/1	mat	VBM:2004011507 Separate out the refreshDevicePatternCache from refreshDeviceCache

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 ===========================================================================
*/
