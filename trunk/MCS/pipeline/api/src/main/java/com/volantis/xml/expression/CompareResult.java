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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression;

import java.util.HashSet;
import java.util.Set;

/**
 * Compare result enumeration. Possible instances of this class are:
 * GREATER_THAN, LESS_THAN, EQUAL, INCOMPARABLE.
 * 
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class CompareResult {
    
    /**
     * A set of unique CompareResult names
     */
    private static final Set instanceNames = new HashSet();
    
    /**
     * Greater than result
     */
    public static final CompareResult GREATER_THAN = new CompareResult("GREATER_THAN");
    
    /**
     * Less than result
     */
    public static final CompareResult LESS_THAN = new CompareResult("LESS_THAN");
    
    /**
     * Equal result
     */
    public static final CompareResult EQUAL = new CompareResult("EQUAL");
    
    /**
     * Incomparable result
     */
    public static final CompareResult INCOMPARABLE = new CompareResult("INCOMPARABLE");
    
    /**
     * Instance name. Must be unique among all instances
     */
    private final String name;
    
    /**
     * Default constructor
     * 
     * @param name name of created instance
     */
    private CompareResult(final String name) {
        this.name = name;
        if (instanceNames.contains(name)) {
            throw new IllegalStateException("CompareResult with name '" + name
                    + "' already exists");
        }
        instanceNames.add(name);
    }
    
    /**
     * Get instance name
     * 
     * @return
     */
    public String getName() {
        return this.name;
    }
    
    // Javadoc inherited
    public String toString() {
        return getName();
    }
}
