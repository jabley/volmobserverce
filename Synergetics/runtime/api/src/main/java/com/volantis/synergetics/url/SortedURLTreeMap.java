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
 * $Header: /src/voyager/com/volantis/mcs/url/SortedURLTreeMap.java,v 1.3 2003/02/07 11:06:43 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Sep-02    Mat             VBM:2002100701 - Created to hold a tree of
 *                              URL tokens and associated objects.
 * 06-Feb-03    Mat             VBM:2002100701 - Changed TreeNode.addChild
 *                              to return a boolean to save garbage creation.
 * 07-Feb-03    Mat             VBM:2002100701 - Updated JAVADOC for addChild
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.url;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * A tree structure for holding URL's and objects associated with them. The
 * implementation requires that for a given URL, the object associated with the
 * longest matching prefix URL is returned. The tree structure makes it easy to
 * perform this matching efficiently.
 */
public class SortedURLTreeMap {

    /**
     * The copyright statement.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2002. ";

    private TreeNode rootNode;

    /**
     * Creates a new instance of SortedURLTreeMap
     */
    public SortedURLTreeMap() {
        rootNode = new TreeNode();

    }

    /**
     * Add a new url and object to the tree.
     *
     * @param url    The url to add
     * @param object The associated object.
     */
    public void put(String url, Object object) {
        StringTokenizer st = new StringTokenizer(url, "/");
        rootNode.addChild(st, object);
    }

    /**
     * Search the tree for the given url.
     *
     * @param url The url to search for
     * @return The object found. or null if no match found.
     */
    public Object get(String url) {
        StringTokenizer st = new StringTokenizer(url, "/");
        TreeNode node = rootNode.getMatchingNode(st);
        if (node != null) {
            return node.getObject();
        } else {
            return node;
        }
    }


    /**
     * Class to represent a node on the tree.  It holds a list of peer nodes
     * and optionally an object to be returned if this node is matched in a
     * search.
     */
    class TreeNode {

        private Object object = null;

        private HashMap peers;

        /**
         * Create a new instance of a TreeNode.
         */
        public TreeNode() {
            peers = new HashMap();
        }

        /**
         * Add a child to this tree node.  If the token doesn't match any peer
         * nodes a new peer will be created.  If a matching peer is found, it
         * will be used. If there are no more tokens, return true.
         *
         * @param tokens The remaining tokens from the tokenizer.
         * @param object The object to associate with the last node.
         * @return True if their were no more nodes added.
         */
        public boolean addChild(StringTokenizer tokens, Object object) {
            TreeNode node = null;
            if (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                node = (TreeNode) peers.get(token);
                if (node != null) {
                    // Found a node for this token.  Don't check the
                    // return from the call to addChild as if we happen
                    // to be adding a duplicate URL, the addChild will not
                    // do anything as there are no more tokens and we want
                    // to keep the object first added to this node instead
                    // of using the new one.
                    node.addChild(tokens, object);
                } else {
                    // There was no match for the token at this level, so
                    // add a new one to the list and create a new level for it.
                    node = new TreeNode();
                    peers.put(token, node);
                    // If no more nodes were added, set the object in the last
                    // one.
                    if (node.addChild(tokens, object)) {
                        node.setObject(object);
                    }
                }
            }
            return (node == null);
        }

        /**
         * Find a node matching the token.  This will in effect recurse down
         * the tree to find matching nodes.  It will return when either:
         *
         * A matching node is not found There are no more tokens.
         *
         * @param tokens The remaining tokens from the tokenizer
         * @return The matching node, or null if none found.
         */
        public TreeNode getMatchingNode(StringTokenizer tokens) {
            TreeNode node = null;
            if (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                TreeNode tokenNode = (TreeNode) peers.get(token);
                if (tokenNode != null) {
                    node = tokenNode.getMatchingNode(tokens);
                    // If no child node was found, return the node found
                    // at this level.
                    if (node == null) {
                        node = tokenNode;
                    }
                }
            }
            return node;
        }

        /**
         * Getter for property object.
         *
         * @return Url of property object.
         */
        public Object getObject() {
            return object;
        }

        /**
         * Setter for property object.
         *
         * @param object New url of property object.
         */
        public void setObject(Object object) {
            this.object = object;
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	343/3	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	343/1	doug	VBM:2004111702 Refactoring of logging framework

 24-May-04	219/1	allan	VBM:2004052101 Add URLIntrospector and SortedURLTreeMap

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
