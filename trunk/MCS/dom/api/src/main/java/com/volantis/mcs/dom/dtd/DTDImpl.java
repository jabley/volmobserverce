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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.dtd;

import java.util.Set;

/**
 * Implementation of {@link DTD}.
 */
public abstract class DTDImpl
        implements DTD {

    /**
     * The maximum line length.
     */
    private final int maximumLineLength;

    /**
     * Indicates whether an empty tag requires a space between the tag name
     * and the />.
     */
    private final boolean emptyTagRequiresSpace;

    /**
     * The set of elements that should be ignored.
     */
    private final Set ignoreableElements;

    /**
     * Initialise.
     *
     * @param builder The builder from which this is built.
     */
    public DTDImpl(DTDBuilder builder) {
        this.maximumLineLength = builder.getMaximumLineLength();
        this.emptyTagRequiresSpace = builder.getEmptyTagRequiresSpace();
        this.ignoreableElements = builder.getIgnoreableElements();
    }

    // Javadoc inherited.
    public int getMaximumLineLength() {
        return maximumLineLength;
    }

    // Javadoc inherited.
    public boolean getEmptyTagRequiresSpace() {
        return emptyTagRequiresSpace;
    }

    // Javadoc inherited.
    public boolean isElementIgnoreable(String name) {
        return ignoreableElements.contains(name);
    }
}
