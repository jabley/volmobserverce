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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.NodeSequence;

/**
 * An empty node sequence.
 */
public class EmptyNodeSequence
        implements PrivateNodeSequence {

    /**
     * The default instance of the empty node sequence.
     */
    public static final NodeSequence INSTANCE = new EmptyNodeSequence();

    // Javadoc inherited.
    public Node getFirst() {
        return null;
    }

    // Javadoc inherited.
    public Node getLast() {
        return null;
    }

    // Javadoc inherited.
    public Node getEnd() {
        return null;
    }

    // Javadoc inherited.
    public void forEach(NodeIteratee nodeIteratee) {
    }
}
