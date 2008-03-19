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

package com.volantis.xml.pipeline.sax.drivers.web.simple;

/**
 * A setter that will convert its string parameter to a boolean.
 */
public abstract class BooleanSetter
        implements Setter {

    // Javadoc inherited.
    public void setPropertyAsString(Object object, String string) {
        setAsBoolean(object, "true".equals(string));
    }

    /**
     * Set a boolean property.
     *
     * @param object The object upon which the property will be set.
     * @param b      The value to set.
     */
    protected abstract void setAsBoolean(Object object, boolean b);
}
