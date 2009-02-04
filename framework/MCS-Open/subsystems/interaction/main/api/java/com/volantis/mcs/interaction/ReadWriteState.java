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
package com.volantis.mcs.interaction;

/**
 * A typesafe enumeration of possible readable/writeable states for interaction
 * layer objects.
 */
public class ReadWriteState {
    /**
     * The proxy is read only.
     */
    public static final ReadWriteState READ_ONLY =
            new ReadWriteState("read-only");

    /**
     * The proxy is readable and writeable.
     */
    public static final ReadWriteState READ_WRITE =
            new ReadWriteState("read-write");

    /**
     * Inherit read/write state from parent proxy. If there is no parent, the
     * default is read/write.
     */
    public static final ReadWriteState INHERIT =
            new ReadWriteState("inherit");

    /**
     * The name of the read/write state.
     */
    private String name;

    /**
     * Private constructor to prevent instantiation by other classes.
     *
     * @param name The name of the read/write state (for debugging purposes
     *             only).
     */
    private ReadWriteState(String name) {
        this.name = name;
    }
}
