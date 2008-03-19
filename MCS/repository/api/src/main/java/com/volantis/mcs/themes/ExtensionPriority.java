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

package com.volantis.mcs.themes;

/**
 * Extension priorities.
 */
public class ExtensionPriority {

    /**
     * The medium priority.
     *
     * <p>This is not part of the CSS standard, it is an extension that is
     * used to indicate that MCS needs to output a value with an important
     * priority because otherwise the device ignores it because it treats its
     * own value as if it had a medium priority.</p>
     */
    public static final Priority MEDIUM = new Priority(
            "MEDIUM", 50, " !-medium");
}
