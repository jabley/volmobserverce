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

import com.volantis.styling.Styles;

/**
 * Constructs a {@link ElementOutputState} using {@link Styles} and other
 * contextual information with which it has been configured.
 *
 * @mock.generate
 */
public interface ElementOutputStateBuilder {

    /**
     * Indicates whether or not the element that is currently being processed
     * either is, or is contained by, a group element which is currently
     * inactive.
     * <p/>
     * This is significant because an element should not generate output markup
     * if either it is, or is contained by, an inactive group (regardless of
     * whether its styles specify a valid container).
     *
     * @param isInactiveGroup   true if the element currently being processed
     *                          either is, or is contained by, an inactive
     *                          group, false otherwise
     */
    void setInactiveGroup(boolean isInactiveGroup);

    /**
     * Build an {@link ElementOutputState} using the information that has been
     * configured.
     *
     * @return ElementOutputState which will specify if and where the element
     * to which it applies should generate markup.
     */
    ElementOutputState createElementOutputState();
}
