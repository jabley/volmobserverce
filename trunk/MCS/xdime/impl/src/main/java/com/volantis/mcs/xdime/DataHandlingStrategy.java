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
 * Interface which defines how data encountered during processing should be
 * handled.
 * @mock.generate
 */
public interface DataHandlingStrategy {

    /**
     * Indicates that any data (character data or markup) encountered from now
     * on should be handled by this strategy.
     *
     * @param context   in which the data is being handled
     */
    void handleData(XDIMEContextInternal context);

    /**
     * Returns the character data that has been encountered while processing as
     * a string.
     *
     * @return the collected character data as a string
     */
    String getCharacterData();

    /**
     * Indicates that any data (character data or markup) encountered from now
     * on should not be handled by this strategy.
     *
     * @param context   in which the data is being handled.
     */
    void stopHandlingData(XDIMEContextInternal context);
}
