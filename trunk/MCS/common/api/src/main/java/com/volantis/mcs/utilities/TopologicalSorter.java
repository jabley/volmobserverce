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
package com.volantis.mcs.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Sorts set of nodes of a directed graph in a topological order.
 * 
 * <p>
 * A list of nodes in a directed graph is topologically sorted if and only if
 * for each directed edge from node A to node B, node A is placed before node B
 * on the list.
 * </p>
 * 
 * <p>
 * The usage of this sorter consists of two phases, which may be repeated any
 * number of times:
 * <li>prepare a graph by adding its nodes and edges</li>
 * <li>perform the sort</li>
 * </p>
 */
public class TopologicalSorter {
    /**
     * A mapping between graph nodes, and Node instances used internally to
     * represent the graph.
     */
    private final HashMap nodesMap = new HashMap(); 
    
    /**
     * Adds specified node, unless it was already added.
     * 
     * @param node The node to add
     */
    public void addNode(Object node) {
        createOrGetNode(node);
    }
    
    /**
     * Adds directional edge between specified nodes.
     * Non-existing nodes are added implicitely.
     * 
     * @param from The starting node.
     * @param to The ending node.
     */
    public void addEdge(Object from, Object to) {
        // Implicitely add two nodes, if they don't exist yet.
        Node fromNode = createOrGetNode(from);
        
        Node toNode = createOrGetNode(to);
        
        // Create and edge between the nodes.
        fromNode.toNodes.add(toNode);
        
        toNode.fromNodesCount++;
    }

    /**
     * Performs topological sort and returns its result as a list of nodes.
     * Implicit reset is performed after sorting.
     * 
     * @return The sorted list of nodes.
     * @throws SortException If some of the nodes could not be sorted.
     */
    public List sort() throws SortException {
        // Create an array, which would contain isolated nodes (nodes without
        // incoming edges).
        ArrayList isolatedNodes = new ArrayList(nodesMap.size());
        
        // Create a set which would contain non-isolated nodes (nodes with at
        // least one incoming edge).
        HashSet nonIsolatedNodes = new HashSet(nodesMap.size());

        // Step 1: Split the set of all nodes into isolated and non-isolated ones.
        Iterator nodesIterator = nodesMap.values().iterator();
        
        while (nodesIterator.hasNext()) {
            Node node = (Node) nodesIterator.next();
            
            if (node.fromNodesCount == 0) {
                isolatedNodes.add(node);
            } else {
                nonIsolatedNodes.add(node);
            }
        }
        
        // Step 2: Perform the sort.
        ArrayList sorted = new ArrayList(nodesMap.size());
        
        while (!isolatedNodes.isEmpty()) {
            // Take any of the isolated nodes, and remove it from the graph,
            // together with all outcoming edges.
            Node isolatedNode = (Node) isolatedNodes.remove(isolatedNodes.size() - 1);
            
            Iterator toNodesIterator = isolatedNode.toNodes.iterator();
            
            while (toNodesIterator.hasNext()) {
                Node toNode = (Node) toNodesIterator.next();
                
                toNode.fromNodesCount--;
                
                if (toNode.fromNodesCount == 0) {
                    isolatedNodes.add(toNode);
                    
                    nonIsolatedNodes.remove(toNode);
                }
            }
            
            // Add removed node to the sorted list. 
            sorted.add(isolatedNode.element);
        }
        
        // Step 3: Check if there are non-isolated nodes left. If they are,
        // throw a SortException.
        if (!nonIsolatedNodes.isEmpty()) {
            // Prepare a collection of unsorted nodes.
            ArrayList unsortedNodes = new ArrayList(nonIsolatedNodes.size());
            
            Iterator leftNodesIterator = nonIsolatedNodes.iterator();
            
            while (leftNodesIterator.hasNext()) {
                Node leftNode = (Node) leftNodesIterator.next();
                
                unsortedNodes.add(leftNode.element);
            }
            
            throw new SortException("Some nodes could not be sorted.", unsortedNodes);
        }
        
        // Step 4: Perform implicit reset.
        reset();
        
        return sorted;
    }
    
    /**
     * Resets (clears) the graph.
     */
    public void reset() {
        nodesMap.clear();
    }
    
    /**
     * Creates an instance of internal Node, containing specified graph node.
     * 
     * @param element Element contained within this node.
     * @return new instance of Node.
     */
    private Node createOrGetNode(Object element) {
        // Check, if there already is an instance of Node created for specified
        // element.
        Node node = (Node) nodesMap.get(element);

        // If no, create it now.
        if (node == null) {
            node = new Node(element);
            
            nodesMap.put(element, node);
        }
        
        return node;
    }
    
    /**
     * An exception thrown when sorting the graph was not possible.
     */
    public class SortException extends Exception {
        /**
         * The nodes, which could not be sorted.
         */
        private Collection unsortedNodes;
        
        /**
         * Initializes this exception with specified message and collection of
         * nodes, which were not sortable.
         * 
         * @param message The exception message.
         * @param unsortedNodes The nodes, which were not sorted.
         */
        private SortException(String message, Collection unsortedNodes) {
            super(message);
            
            this.unsortedNodes = unsortedNodes;
        }

        /**
         * Returns the collection of nodes which were not sortable and caused
         * this exception.
         * 
         * @return the collection of nodes which were not sortable.
         */
        public Collection getUnsortedNodes() {
            return unsortedNodes;
        }
    }
    
    /**
     * A graph node used for internal representation of the graph.
     */
    private class Node {
        final Object element;
        final ArrayList toNodes;
        int fromNodesCount;
        
        Node(Object element) {
            this.element = element;
            this.toNodes = new ArrayList();
            this.fromNodesCount = 0;
        }
    }
}
