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
import com.volantis.shared.iteration.IterationAction;

/**
 * Implementation of {@link PrivateNodeSequence}.
 */
public class NodeSequenceImpl
        implements PrivateNodeSequence {

    /**
     * The first node in the sequence, or null if the sequence is empty.
     */
    private final Node first;

    /**
     * The last node in the sequence, or null if the sequence is empty.
     */
    private final Node last;

    /**
     * The end of the sequence, i.e. the node after the last one, or null if
     * there is no such node.
     */
    private final Node end;

    /**
     * Initialise.
     *
     * @param first The first node in the sequence, or null if the sequence is
     *              empty.
     * @param last  The last node in the sequence, or null if the sequence is
     *              empty.
     */
    public NodeSequenceImpl(Node first, Node last) {

        // If either first or last is null but the other is not then it is an
        // error.
        if ((first == null) != (last == null)) {
            throw new IllegalStateException(
                    "Either first and last must both be null, " +
                    "or neither must be null");
        }

        // If specified then both first and last must have the same
        // parent.
        if (first != null && first.getParent() != last.getParent()) {
            throw new IllegalStateException(
                    "Nodes in sequence must all have same parent");
        }

        this.first = first;
        this.last = last;
        if (last == null) {
            this.end = null;
        } else {
            this.end = last.getNext();
        }
    }

    // Javadoc inherited.
    public void forEach(NodeIteratee nodeIteratee) {
        if (first != null) {

            Node end;
            if (last == null) {
                end = null;
            } else {
                end = last.getNext();
            }
            Node node = first;
            while (node != end) {
                // Get the next node before calling the iteratee just in case
                // it does anything to the current node, i.e. removes it.
                Node next = node.getNext();
                IterationAction action = nodeIteratee.next(node);
                if (action == IterationAction.BREAK) {
                    break;
                } else if (action != IterationAction.CONTINUE) {
                    throw new IllegalStateException(
                            "Unknown iteration action: " + action);
                }
                node = next;
            }
        }
    }

    // Javadoc inherited.
    public Node getFirst() {
        return first;
    }

    // Javadoc inherited.
    public Node getLast() {
        return last;
    }

    // Javadoc inherited.
    public Node getEnd() {
        return end;
    }

}
