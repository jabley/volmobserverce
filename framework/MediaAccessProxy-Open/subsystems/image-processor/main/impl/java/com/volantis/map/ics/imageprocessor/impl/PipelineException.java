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

package com.volantis.map.ics.imageprocessor.impl;

/**
 * Represents an exception generated by Pipeline.
 */
public class PipelineException extends Exception {

    /**
     * Initializes new object instance with given parameters.
     *
     * @param message the detail message.
     */
    public PipelineException(String message) {
        super(message);
    }

    /**
     * Initializes new object instance with given parameters.
     *
     * @param e - source of the exception.
     */
    public PipelineException(Exception e) {
        super(e);
    }
}
