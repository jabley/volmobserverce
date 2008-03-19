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

package com.volantis.cache.provider;

import com.volantis.cache.group.Group;

/**
 * Contains result from a {@link CacheableObjectProvider}.
 */
public class ProviderResult {

    /**
     * The value that is to be cached.
     */
    private final Object value;

    /**
     * The group to which the object will belong, may not be null.
     */
    private final Group group;

    /**
     * True if the object can be cached, false otherwise.
     */
    private final boolean cacheable;

    /**
     * An object that will be associated with the entry within which the
     * value will be stored.
     */
    private final Object extensionObject;
    private final Throwable throwable;

    /**
     * Initialise.
     *
     * @param value           The value that is to be cached.
     * @param group           The group to which the object will belong, may
     *                        not be null.
     * @param cacheable       True if the object can be cached, false otherwise.
     * @param extensionObject An object that will be associated with the entry
     *                        within which the
     */
    public ProviderResult(
            Object value, Group group, boolean cacheable,
            Object extensionObject) {

        if (group == null) {
            throw new IllegalArgumentException("group cannot be null");
        }

        this.value = value;
        this.cacheable = cacheable;
        this.extensionObject = extensionObject;
        this.group = group;
        this.throwable = null;
    }

    /**
     * Initialise.
     *
     * @param throwable       The throwable that was caught by the cache, if
     * @param group           The group to which the object will belong, may
     *                        not be null.
     * @param cacheable       True if the object can be cached, false otherwise.
     * @param extensionObject An object that will be associated with the entry
     *                        within which the
     */
    public ProviderResult(
            Throwable throwable, Group group, boolean cacheable,
            Object extensionObject) {

        if (group == null) {
            throw new IllegalArgumentException("group cannot be null");
        }

        this.value = null;
        this.cacheable = cacheable;
        this.extensionObject = extensionObject;
        this.group = group;
        this.throwable = throwable;
    }

    /**
     * Get the value.
     *
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Get the group.
     *
     * @return The group.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Check whether the object is cacheable.
     *
     * @return True if it is cacheable, false otherwise.
     */
    public boolean isCacheable() {
        return cacheable;
    }

    /**
     * Get the extension object.
     *
     * @return The extension object.
     */
    public Object getExtensionObject() {
        return extensionObject;
    }

    /**
     * Get the throwable object.
     * @return
     */
    public Throwable getThrowable() {
        return throwable;
    }
}
