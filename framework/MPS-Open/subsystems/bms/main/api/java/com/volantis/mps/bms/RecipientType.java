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

package com.volantis.mps.bms;

/**
 * Enumerated type defining the possible types of recipient.
 *
 * @volantis-api-include-in InternalAPI
 */
public enum RecipientType {
    TO, CC, BCC;

    /**
     * Return a RecipientType based on the specified type name. If the type
     * is null, then it defaults to RecipientType.TO.
     *
     * @param type name of the RecipientType
     * @return a RecipientType
     */
    public static RecipientType getRecipientType(String type) {
        RecipientType result = RecipientType.TO;
        if (null != type) {
	    result = RecipientType.valueOf(type);
	}
        return result;
    }

    /**
     * Return a String representation of the specified RecipientType.
     *
     * @param type a RecipientType - not null.
     * @return the string representation.
     */
    public static String getValue(RecipientType type) {
        if (null == type) {
            throw new IllegalArgumentException("type cannot be null");
        }
        return type.name();
    }

}
