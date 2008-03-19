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

package com.volantis.mcs.protocols.widgets;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates property names.
 */
public final class PropertyName extends MemberName {
    /**
     * Enumeration map.
     */
    private static final Map PROPERTY_NAMES = new HashMap();
    
    /**
     * Creates a new instance of PropertyName for given name and adds it into the
     * enumeration map.
     * 
     * @param name The name of the property.
     * @return Creates instance of PropertyName
     */
    private static PropertyName add(String name) {
        if (PROPERTY_NAMES.containsKey(name)) {
            throw new IllegalStateException("PropertyName already exists.");
        }

        PropertyName propertyName = new PropertyName(name);
        
        PROPERTY_NAMES.put(name, propertyName);
        
        return propertyName;
    }
    
    //  PropertyName definitions follows here.
    public static final PropertyName ITEM_NUMBER = add("item-number");
    public static final PropertyName ITEMS_COUNT = add("items-count");
    public static final PropertyName START_ITEM_NUMBER = add("start-item-number");
    public static final PropertyName END_ITEM_NUMBER = add("end-item-number");
    public static final PropertyName CURRENT_PAGE_NUMBER = add("current-page-number");
    public static final PropertyName PAGE_NUMBER = add("page-number");
    public static final PropertyName PAGES_COUNT = add("pages-count");
    public static final PropertyName MONTH = add("month");
    public static final PropertyName YEAR = add("year");
    public static final PropertyName VALUE = add("value");
    public static final PropertyName ACTIVE_VALUE = add("active-value");
    public static final PropertyName IS_MODIFIED = add("is-modified");
    public static final PropertyName IS_HIDDEN = add("is-hidden");
    public static final PropertyName IS_ENABLED = add("is-enabled");
    public static final PropertyName CONTENT = add("content");
    public static final PropertyName CONTENT_STATUS = add("content-status");
    public static final PropertyName DISPLAYED_CONTENT = add("displayed-content");
    public static final PropertyName DISPLAYED_PAGE_NUMBER = add("displayed-page-number");
    public static final PropertyName PARTIAL_VALUE = add("partial-value");
    public static final PropertyName STATUS = add("status");
    public static final PropertyName CONTAINER = add("container");
    public static final PropertyName LOAD = add("load");
    public static final PropertyName FETCH = add("fetch");
    public static final PropertyName REFRESH = add("refresh");
    public static final PropertyName LONGITUDE = add("longitude");
    public static final PropertyName LATITUDE = add("latitude");
    public static final PropertyName ZOOM = add("zoom");
    public static final PropertyName QUERY = add("query");
    public static final PropertyName MIN_ZOOM = add("min-zoom");
    public static final PropertyName MAX_ZOOM = add("max-zoom");
    public static final PropertyName CURRENT = add("current");    
    public static final PropertyName START_TIME = add("start-time");
    public static final PropertyName STOP_TIME = add("stop-time");
    public static final PropertyName IS_BUSY = add("is-busy");
    public static final PropertyName START_ROW_NUMBER = add("start-row-number");
    public static final PropertyName END_ROW_NUMBER = add("end-row-number");
    public static final PropertyName ROWS_COUNT = add("rows-count");
    public static final PropertyName LOAD_ERROR_MESSAGE = add("load-error-message");
    
    /**
     * The name of this action.
     */
    private final String name;
    
    /**
     * Creates new instance of PropertyName.
     * 
     * @param name The name of the action.
     */
    private PropertyName(String name) {
        this.name = name;
    };
    
    /**
     * Returns the name of this action.
     * 
     * @return the name of this action.
     */
    public String getName() {
        return name; 
    }

    /**
     * Returns an instance of this class for given name.
     * 
     * @return an instance of this class for given name.
     * @throws IllegalArgumentException if such property does not exist.
     */
    public static PropertyName forName(String name) {
        PropertyName propertyName = (PropertyName) PROPERTY_NAMES.get(name);
        
        if (propertyName == null) {
            throw new IllegalArgumentException("Unknown property name.");
        }
        
        return propertyName;
    }

    public MemberType getMemberType() {
        return MemberType.PROPERTY;
    }
}
