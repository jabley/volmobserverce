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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

/**
 * An instance of this interface can be used to walk the dissectable
 * document structure visting each node in document order.
 * <p>
 * Leaf nodes (text and empty elements) are visited once by the walker.
 * <p>
 * Non leaf nodes (elements with children) are visited twice, once before
 * the children are visited and once after they are visited.
 * <p>
 * Instances of this interface must maintain a current position within the
 * tree. This is updated by the next methods.
 * todo: This interface is not used at the moment.
 */
public interface DissectableWalker {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * This indicates that the current node is being visited before its
     * children.
     */
    public static final int BEFORE_CHILDREN = 0;

    /**
     * This indicates that the current node is being visited after its
     * children.
     */
    public static final int AFTER_CHILDREN = 1;

    /**
     * Get the current node.
     */
    public DissectableNode current();

    /**
     * Go to the next node in document order.
     * <p>
     * If the current node is a leaf node then the next node is determined
     * by trying for the following nodes in order until one is found, or all
     * of them have been checked. If no node was found then there is no next
     * node and the walk is ended.
     * <ol>
     *   <li>The following sibling.
     *   <li>The parent.
     * </ol>
     * <p>
     * Note: The above applies to empty elements as well as text nodes.
     * <p>
     * If the current node is not a leaf node (i.e. a non empty element)
     * then the next node is dependent on whether the children have been
     * visited yet.
     * @param skipChildren If this is true then child nodes are removed from
     * the list of possible next nodes.
     * @return The next node, or null if there is no next node.
     */
    //public Node moveToNext(boolean skipChildren);
    public int moveToNext(boolean skipChildren);

    public void moveToParent();

    /**
     * Move the walker
     * @param index
     * @return the dissectable node
     */
    public DissectableNode moveToChild(int index);

    public void moveToSibling();

    /**
     * Get the order in which the current node was visited.
     * <p>
     * This method always returns {@link #BEFORE_CHILDREN} unless the
     * current node was moved to
     * @return the node order
     */
    public int getOrder();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 ===========================================================================
*/
