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
package com.volantis.mcs.xdime;

/**
 * Concrete implementation of {@link DataHandlingStrategy} which does nothing
 * with any data (character data or markup) encountered while processing.
 */
public class IgnoreDataStrategy implements DataHandlingStrategy {

    private static final IgnoreDataStrategy INSTANCE =
            new IgnoreDataStrategy();

    // Javadoc inherited.
    public void handleData(XDIMEContextInternal context) {
    }

    // Javadoc inherited.
    public String getCharacterData() {
        return null;
    }

    // Javadoc inherited.
    public void stopHandlingData(XDIMEContextInternal context) {
    }

    /**
     * Initialize a new instance, using private constructor to enforce
     * singleton pattern.
     */
    private IgnoreDataStrategy(){}

    /**
     * Return the only instance of this class.
     *
     * @return the single instance of this class.
     */
    public static IgnoreDataStrategy getDefaultInstance() {
        return INSTANCE;
    }
}
