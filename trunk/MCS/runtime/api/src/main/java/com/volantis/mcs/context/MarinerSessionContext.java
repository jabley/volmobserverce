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
 * $Header: /src/voyager/com/volantis/mcs/context/MarinerSessionContext.java,v 1.15 2003/03/10 14:22:22 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Oct-01    Paul            VBM:2001102901 - Added this change history,
 *                              and Device has moved from utilities package to
 *                              devices package.
 * 19-Nov-01    Paul            VBM:2001110202 - Added user and fragmentation
 *                              specific methods. User was being created
 *                              for every page when it could have been shared.
 * 22-Nov-01    Paul            VBM:2001110202 - Added support for caching
 *                              changes to the FragmentationState object and
 *                              added a method to parse a string and create
 *                              FragmentationState object based on an existing
 *                              state (or null) and a change.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 13-Feb-02    Paul            VBM:2002021203 - Moved fragmentation state
 *                              cache from session into PageGenerationCache.
 * 19-Feb-02    Paul            VBM:2001100102 - Added cache of FormDescriptor
 *                              objects.
 * 20-Feb-02    Steve           VBM:2001101803 - Added attribute support
 *                              methods so that attributes can be read from a
 *                              servlet or JSP. These are used by form
 *                              fragments to pass the current values of fields
 *                              around.
 * 04-Mar-02    Paul            VBM:2001101803 - Added a comment about possible
 *                              improvements to the code.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 07-May-02    Jason           VBM:2002050802 - Added a constructor with no
 *                              parameters (for use in cluster migration), the
 *                              readExternal() and writeExternal() methods, also
 *                              changed class to implement Externalizable.
 * 13-Feb-03    Phil W-S        VBM:2003021302 - Add deviceAssetCache member
 *                              and deviceAssetCacheMaxSize property. Add
 *                              isAssetCached method.
 * 17-Feb-03    Phil W-S        VBM:2003021701 - Remove erroneous ";" which
 *                              causes problems in the production build (which
 *                              removes debug statements)
 * 03-Mar-03    Steve           VBM:2003021101 - Cookie support for a session.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.forms.FormDataManager;
import com.volantis.mcs.runtime.plugin.markup.MarkupFactory;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds information that is valid across an entire session with a
 * device. For example, it holds a reference to the class that represents the
 * device in use. Objects of this class are referenced via the attribute list
 * in the HttpSession class under which the request is being processed.
 * HttpSession maintains a hash table of named objects which remain valid for
 * the session.
 *
 * @mock.generate
 */
public class MarinerSessionContext
        implements Externalizable {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MarinerSessionContext.class);

    /**
     * The InternalDevice which is in use in this session.
     */
    private InternalDevice device;
    private final MarkupFactory markupFactory;

    /**
     * Manages the data stored for fragmented forms in this session context.
     */
    private FormDataManager formDataManager = new FormDataManager();

    /**
     * This is a model of the device's cache of assets. The list will be held
     * in MRU (most recently used) order, such that the MRU asset has the
     * earliest entry in the list.
     */
    private List deviceAssetCache = null;

    /**
     * This indicates the maximum size of the asset cache on the device. If
     * this value is zero the asset cache model is not enabled.
     */
    private int deviceAssetCacheMaxSize = 0;

    /**
     * The {@link MarkupPluginContainer} to which we will delegate our
     * {@link MarkupPluginContainer} methods.
     */
    private MarkupPluginContainer markupPluginContainer;

    /**
     * Create a new <code>MarinerSessionContext</code>, this is only
     * to be used when the object is Externalized and thereforce expects
     * readExternal() to be run to populate the device and user objects.
     * As a device object contains references to otehr devices, at the
     * moment rather than serialize the device and user objects we leave
     * them set to null. initialSession() will check if device or user
     * are null and populate them with the current requests details.
     */
    public MarinerSessionContext () {
        this (null, MarkupFactory.getDefaultInstance());
    }

    /**
     * Create a new <code>MarinerSessionContext</code> for the specified InternalDevice
     * and User.
     *
     * @param device        The InternalDevice which this session is for.
     * @param markupFactory
     */
    public MarinerSessionContext(InternalDevice device,
            MarkupFactory markupFactory) {
        this.device = device;
        this.markupFactory = markupFactory;
    }

    /**
     * Get the InternalDevice which is in use in this session.
     *
     * @return The InternalDevice which is in use in this session.
     */
    public InternalDevice getDevice() {
        return device;
    }

    /**
     * Set the InternalDevice which is in use in this session.
     *
     * @param device The InternalDevice which is to be in this session.
     */
    public void setDevice(InternalDevice device) {
        this.device = device;
    }

    /**
     * Return the class which is responsible for managing the form data stored
     * for fragmented forms in this session context.
     *
     * @return FormDataManager which is responsible for managing the form data
     * stored for fragmented forms in this session context.
     */
    public FormDataManager getFormDataManager() {
        return formDataManager;
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
     * Returns the maximum size for the device cache model. Note that a value
     * of zero will be returned if a cache size has not previously been set
     * (and will also be returned if a value of zero has been passed into
     * the equivalent set method). A negative or zero value indicates that
     * the cache model is disabled.
     *
     * @return the maximum size for the device cache model
     */
    public int getDeviceAssetCacheMaxSize() {
        return deviceAssetCacheMaxSize;
    }

    /**
     * Any change of cache size will reset the cache to empty. A negative or
     * zero value will disable the device cache model.
     *
     * @param deviceAssetCacheMaxSize the new size for the device cache model
     * @todo later this method should be synchronized if this mechanism is
     * to be used on devices which can have concurrent requests
     */
    public void setDeviceAssetCacheMaxSize(int deviceAssetCacheMaxSize) {
        if (this.deviceAssetCacheMaxSize != deviceAssetCacheMaxSize) {
            this.deviceAssetCacheMaxSize = deviceAssetCacheMaxSize;

            if (deviceAssetCacheMaxSize > 0) {
                if (deviceAssetCache == null) {
                    deviceAssetCache =
                            new ArrayList(deviceAssetCacheMaxSize);
                } else {
                    deviceAssetCache.clear();
                }
            } else {
                deviceAssetCache = null;
            }
        }
    }

    /**
     * Updates the model of the cache, returning true if the asset is already
     * in the cache model, false otherwise. The model of the device cache is
     * updated, either moving the asset to the MRU (Most Recently Used)
     * position, or adding it at the MRU position. If an asset is added which
     * will make the cache model larger than the maximum size of the cache, the
     * cache is truncated. If the cache model is not enabled this always
     * returns false.
     *
     * @param assetURL the asset URL to be queried and updated in the cache
     * @return true if the asset URL is already cached
     * @todo later this method should be synchronized if this mechanism is to
     * be used on devices which can have concurrent requests
     */
    public boolean isAssetCached(String assetURL) {
        boolean isCached = false;

        if (deviceAssetCacheMaxSize > 0) {
            isCached = deviceAssetCache.contains(assetURL);

            if (isCached) {
                // Move the entry to MRU position if needed
                if (deviceAssetCache.indexOf(assetURL) != 0) {
                    deviceAssetCache.remove(assetURL);
                    deviceAssetCache.add(0, assetURL);

                    if (logger.isDebugEnabled()) {
                        logger.debug("Pushed \"" + assetURL + "\" to MRU");
                    }
                }
            } else {
                // Make room in the cache if needed
                if (deviceAssetCache.size() == deviceAssetCacheMaxSize) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Dropping \"" +
                                     (String)deviceAssetCache.get(
                                         deviceAssetCache.size() - 1) +
                                     "\" from the cache");
                    }

                    deviceAssetCache.remove(deviceAssetCacheMaxSize - 1);
                }

                // Add the asset at MRU position
                deviceAssetCache.add(0, assetURL);

                if (logger.isDebugEnabled()) {
                    logger.debug("Adding \"" + assetURL + "\" at MRU");
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Dump of updated device cache (max size " +
                        deviceAssetCacheMaxSize + ", current size " +
                        deviceAssetCache.size() + "):");
                for (int i = 0;
                        i < deviceAssetCache.size();
                        i++) {
                    logger.debug("  cache[" + i + "] = \"" +
                            (String) deviceAssetCache.get(i) + "\"");
                }
            }
        }

        return isCached;
    }

    /**
     * Release the resources associated with this MarinerSessionContext
     */
    public void release() {
        if (markupPluginContainer != null) {
            markupPluginContainer.releasePlugins();
        }
    }

    public MarkupPluginContainer getMarkupPluginContainer() {
        synchronized (this) {
            if (markupPluginContainer == null) {
                markupPluginContainer
                        = markupFactory.createMarkupPluginContainer();
            }

            return markupPluginContainer;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes

 25-Jan-05	6712/2	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 28-Oct-04	6010/1	claire	VBM:2004102701 mergevbm: External stylesheet handling with different projects in portals

 28-Oct-04	5995/1	claire	VBM:2004102701 External stylesheet handling with different projects in portals

 27-May-04	4075/1	ianw	VBM:2004041408 Ported forward ATG changes and merged

 27-Apr-04	3843/1	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 19-Jul-03	812/3	adrian	VBM:2003071609 Support session scope markup plugins

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 ===========================================================================
*/
