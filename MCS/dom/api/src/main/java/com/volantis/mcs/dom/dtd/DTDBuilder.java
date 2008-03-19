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
import java.util.HashSet;

/**
 * Base class for all builders.
 */
public abstract class DTDBuilder {

    private int maximumLineLength;

    private boolean emptyTagRequiresSpace;

    private Set ignoreableElements;

    protected DTDBuilder() {
        ignoreableElements = new HashSet();
    }

    /**
     * Build the DTD.
     *
     * @return The DTD.
     */
    public abstract DTD buildDTD();

    public void setMaximumLineLength(int maximumLineLength) {
        this.maximumLineLength = maximumLineLength;
    }

    public void setEmptyTagRequiresSpace(boolean emptyTagRequiresSpace) {
        this.emptyTagRequiresSpace = emptyTagRequiresSpace;
    }

    public void addIgnorableElement(String element) {
        ignoreableElements.add(element);
    }

    int getMaximumLineLength() {
        return maximumLineLength;
    }

    public boolean getEmptyTagRequiresSpace() {
        return emptyTagRequiresSpace;
    }

    public Set getIgnoreableElements() {
        return ignoreableElements;
    }
}
