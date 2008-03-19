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
 * $Header: /src/voyager/com/volantis/mcs/utilities/NameValuePair.java,v 1.3 2003/02/07 10:39:18 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jul-02    Steve           VBM:2002071604  - Simple class to hold a name
 *                              and value pair for storage in a collection.
 * 06-Feb-03    Steve           VBM:2002071604  - Fixed logger class name
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics;

public class NameValuePair {

    /**
     * Copyright
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2003. ";

    /**
     * The name.
     */
    private String name;

    /**
     * The value
     */
    private String value;

    /**
     * Creates a new instance of NameValuePair.
     */
    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Return the name of the pair
     *
     * @return name as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Return the value of the pair
     *
     * @return value as a String
     */
    public String getValue() {
        return value;
    }

    /**
     * Return a String representation of the pair
     *
     * @return the pair as a String
     */
    public String toString() {
        return "Name=" + name + " Value=" + value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Aug-03	46/1	byron	VBM:2003081203 Port NameValuePair from MCS to synergetics

 ===========================================================================
*/
