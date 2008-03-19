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
package com.volantis.synergetics.reporting;

/**
 * This specialist report provides a means of setting arbitrarily named
 * "metrics" in addition to permitting the binding between the report and its
 * handler to be on an arbitrary key rather than the interface's class name.
 * The arbitrarily named "parameter" metrics may have values which themselves
 * may be instances of:
 *
 * <ul>
 *
 * <li>java.lang.Boolean</li>
 *
 * <li>java.lang.Byte</li>
 *
 * <li>java.lang.Character</li>
 *
 * <li>java.lang.Double</li>
 *
 * <li>java.lang.Float</li>
 *
 * <li>java.lang.Integer</li>
 *
 * <li>java.lang.Long</li>
 *
 * <li>java.lang.Short</li>
 *
 * <li>java.lang.String</li>
 *
 * <li>java.util.Date</li>
 *
 * </ul>
 *
 * <p>The parameter metric "names" must conform with the pattern:</p>
 *
 * <p>[a-zA-Z_][a-zA-Z0-9_]*</p>
 *
 * <p><strong>Note</strong>: this interface must never be implemented in or
 * extended by user code. The reporting runtime will automatically provide an
 * appropriate implementation of this report interface.</p>
 * 
 * @volantis-api-include-in PublicAPI
 */
public interface DynamicReport extends Report {

    /**
     * Permits a single named parameter to be set. Note that the supplied value
     * must conform to the constraints defined in the class javadoc.
     * (Re)setting a parameter to null causes that parameter to be removed from
     * the report.
     *
     * @param name  the name of the parameter to be set (or cleared if value is
     *              null)
     * @param value the value to be set for the parameter. The class of this
     *              value must be one of those described in this class's
     *              javadoc
     * @throws IllegalArgumentException if the given value doesn't conform to
     *                                  the limitations documented in the class
     *                                  javadoc
     */
    void setParameter(final String name, final Object value)
        throws IllegalArgumentException;
}
