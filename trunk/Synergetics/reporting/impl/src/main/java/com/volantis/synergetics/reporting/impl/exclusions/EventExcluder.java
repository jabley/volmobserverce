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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl.exclusions;

import java.util.Map;

/**
 * Report event exclude interface
 *
 */
public interface EventExcluder {

    /**
     * Is event excluded from being reported
     * @param metrics Map of metrics
     * @return true if excluded, false otherwise
     */
    public boolean isExcluded(Map metrics);

    /**
     * Equals object with object field by field.
     * Name was changed to enforce implementation in every excluder.
     * Used to create unique list of report excluders and not implement hashCode method
     * @param object Object to equal
     * @return true if object is equal field by field
     */
    public boolean isEqual(Object object);
}
