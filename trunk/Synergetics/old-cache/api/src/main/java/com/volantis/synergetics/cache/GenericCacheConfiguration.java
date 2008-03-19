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

package com.volantis.synergetics.cache;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * A class that contains the various options for configuring a cache.  This is
 * used by {@link GenericCacheFactory} to ascertain the type of cache to create
 * and return.  Default values are provided for all properties within the cache
 * configuration so that if these are the values required, there is no need to
 * set them each time. <p> <strong>NOTE:</strong> Currently this class does not
 * include a property such as <code>private String name;</code>.  However the
 * cache element that is used in the config files contains this as a mandatory
 * attribute.  It could be in future that this property should be added into
 * this class and that it would be used when creating caches. </p>
 *
 * @deprecated Use {@link com.volantis.cache.Cache} instead.
 */
public class GenericCacheConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(GenericCacheConfiguration.class);

    /**
     * The strategy required for the cache - -1 is no strategy and is the
     * default value.
     */
    private String strategy = "-1";

    /**
     * The maximum number of entries allowed in the cache - -1 is no limit and
     * is the default value.
     */
    private int maxEntries = FOREVER;

    /**
     * The timeout required for the cache - -1 is no timeout and is the default
     * value.
     */
    private int timeout = FOREVER;

    /**
     * Request that the cache use soft references to store the cache entries if
     * the underlying JVM supports them.  This is not checked for JVM
     * capability or compatability in this class.  It just stores the value
     * provided.  It is important that the cache generation factory or creation
     * code does this if necessary.  Currently {@link GenericCacheFactory#canUseReferenceCaching}
     * does this check and is called from the appropriate methods within that
     * class. <p> This defaults to false because it is not supported in the
     * Volantis products under the Sun JVM and because other implementations
     * have unpredictable behaviour. </p>
     */
    private boolean requestReferenceCaching = false;

    public static final String STRATEGY_NAME = "strategy";

    public static final String MAXENTRIES_NAME = "max-entries";

    public static final String TIMEOUT_NAME = "timeout";

    public static final String REQUEST_REFERENCE_NAME = "use-reference";

    /**
     * Constant in a string form representing a time-to-live for cache objects
     * of forever.  This is needed in addition to {@link #FOREVER} because it
     * is possible to specify this string in the config file to indicate
     * forever.
     */
    public static final String UNLIMITED = "unlimited";

    /**
     * Constant representing a time-to-live for cache objects of forever.  A
     * string representation of this can be found at {@link #UNLIMITED}.
     */
    static final int FOREVER = -1;

    /**
     * Used to indicate no strategy is to be used when removing objects from a
     * cache.
     */
    static final String NO_STRATEGY = "-1";

    /**
     * Strategy for removing objects from a cache that has reached its maximum
     * number of elements. LEAST_USED means that the least used objects in the
     * cache will be removed first.
     */
    public static final String LEAST_USED = "least-used";

    /**
     * Strategy for removing objects from a cache that has reached its maximum
     * number of elements.  LEAST_RECENTLY_USED means that the object in the
     * cache that was accessed the longest time ago will be removed first.
     */
    public static final String LEAST_RECENTLY_USED = "least-recently-used";

    /**
     * Returns the current value for the maximum number of entries that the
     * cache represented by this class currently has set.  If this is set to -1
     * there is no limit on the number of entries allowed.
     *
     * @return The maximum number of entries for the cache.
     */
    public int getMaxEntries() {
        return maxEntries;
    }

    /**
     * Set the maximum number of entries that the cache this configuration
     * represents should contain.  If the value provided is -1 then that
     * indicates that there is no upper bound on the number of entries. <p>
     * This method handles a value for max entries that are already in numeric
     * form.  If the value is represented as a string then use {@link
     * #setMaxEntries(String)} rather than performing the conversion elsewhere
     * in the code. </p>
     *
     * @param maxEntries The maximum number of entries the cache should
     *                   contain.
     */
    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    /**
     * Set the maximum number of entries that the cache this configuration
     * represents should contain.  If the value provided is equal to {@link
     * #UNLIMITED} then that indicates that there is no upper bound on the
     * number of entries. <p> This method provides a convenience for handling
     * String versions of max entries that may contain {@link #UNLIMITED}.  The
     * value is set correctly for this case and for the case where the string
     * needs to be parsed into an Integer.  If there is already an int value
     * available then use {@link #setMaxEntries(int)}. </p>
     *
     * @param maxEntriesString A string representation of the maximum number of
     *                         entries the cache should contain.
     */
    public void setMaxEntries(String maxEntriesString) {
        if (maxEntriesString.equals(UNLIMITED)) {
            maxEntries = FOREVER;
        } else {
            maxEntries = Integer.parseInt(maxEntriesString);
        }
    }

    /**
     * Checks whether the cache that this configuration represents is
     * requesting to use reference caching or not.  There is no guarantee that
     * even if this is true that the cache implementation will do so but it
     * will check this property and apply it if necessary.
     *
     * @return True if the cache is requesting to use reference caching, false
     *         otherwise.
     */
    public boolean isRequestReferenceCaching() {
        return requestReferenceCaching;
    }

    /**
     * Set whether the cache that this configuration represents should request
     * reference caching.  There is no guarantee that if this is true it will
     * be honoured but if false, it will not even try to use reference
     * caching.
     *
     * @param requestReferenceCaching Whether reference caching should be used
     *                                or not.  True if it should be attempted,
     *                                false otherwise.
     */
    public void setRequestReferenceCaching(boolean requestReferenceCaching) {
        this.requestReferenceCaching = requestReferenceCaching;
    }

    /**
     * Returns the current value for the strategy that the cache represented by
     * this class currently has set.  If this is set to -1 there is no strategy
     * specified.
     *
     * @return The strategy for the cache.
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Set the strategy that the cache this configuration represents should
     * contain.  If the value provided is -1 then that indicates that there is
     * no strategy specified.
     *
     * @param strategy The strategy to employ within the cache.
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * Returns the current value for the timeout that the cache represented by
     * this class currently has set.  If this is set to -1 there is no timeout
     * specified.
     *
     * @return The strategy for the cache.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the timeout that the cache this configuration represents should
     * contain.  If the value provided is -1 then that indicates that there is
     * no timeout specified. <p> This method handles a value for timeouts that
     * are already in numeric form.  If the value is represented as a string
     * then use {@link #setTimeout(String)} rather than performing the
     * conversion elsewhere in the code. </p>
     *
     * @param timeout The timeout to employ within the cache.
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Set the timeout that the cache this configuration represents should
     * contain.  If the value provided is equal to {@link #UNLIMITED} then that
     * indicates that there is no upper bound on timeout. <p> This method
     * provides a convenience for handling String versions of timeouts that may
     * contain {@link #UNLIMITED}.  The value is set correctly for this case
     * and for the case where the string needs to be parsed into an Integer.
     * If there is already an int value available then use {@link
     * #setTimeout(int)}. </p>
     *
     * @param timeoutString A string representation of the timeout to employ
     *                      within the cache.
     */
    public void setTimeout(String timeoutString) {
        if (timeoutString.equals(UNLIMITED)) {
            timeout = FOREVER;
        } else {
            timeout = Integer.parseInt(timeoutString);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	343/1	doug	VBM:2004111702 Refactored Logging framework

 25-Jun-04	259/10	claire	VBM:2004060803 Refactored location of cache config related constants

 24-Jun-04	259/8	claire	VBM:2004060803 Renamed CacheConfiguration to GenericCacheConfiguration to avoid name clashes with a runtime cache configuration object

 24-Jun-04	259/5	claire	VBM:2004060803 Updated cache configuration to handle unlimited strings from the config due to xsd change

 22-Jun-04	259/3	claire	VBM:2004060803 CacheConfiguration - update JavaDoc

 21-Jun-04	259/1	claire	VBM:2004060803 CacheConfiguration implementation and integration

 ===========================================================================
*/
