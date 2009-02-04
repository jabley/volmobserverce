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

package com.volantis.mcs.dom;

/**
 * An abstract sequence of nodes.
 *
 * <p>A node sequence has the following characteristics:</p>
 * <ul>
 * <li>All nodes in a sequence must have the same parent.</li>
 * <li>Node being iterated can be modified in a number of ways.</li>
 * </ul>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate 
 */
public interface NodeSequence {

    /**
     * Iterate over the nodes within the sequence, calling the supplied
     * iteratee for each one.
     *
     * <p>This is an application of the Internal Iterator pattern in the GOF
     * book.</p>
     *
     * <p>The nodes being iterated over can be safely modified in the following
     * ways, all other modifications have undefined behaviour:</p>
     * <ul>
     * <li>Node can be removed.</li>
     * <li>Node can be changed.</li>
     * <li>Nodes can be inserted immediately before and immediately after the
     * node. The inserted nodes will not be iterated over during this
     * iteration.</li>
     * </ul>
     *
     * @param nodeIteratee called for each node within this sequence.
     */
    void forEach(NodeIteratee nodeIteratee);
}
