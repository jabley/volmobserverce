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
package com.volantis.mcs.protocols.widgets;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.volantis.mcs.utilities.TopologicalSorter;
import com.volantis.mcs.utilities.TopologicalSorter.SortException;

/**
 * A factory of JavaScriptContainer, used within the WidgetDefaultModule.
 */
public class DefaultJavaScriptContainerFactory {
    private List containers = new ArrayList();
    
    
    /**
     * Set containing all created widget Ids.
     */
    private Set createdWidgetIds = new HashSet();
        
    /**
     * Creates and returns new instance of JavaScriptContainer.
     * 
     * @return The new instance of JavaScriptContainer.
     */
    public JavaScriptContainer createJavaScriptContainer() {
        JavaScriptContainer container = new DefaultJavaScriptContainer(this);
        
        containers.add(container);
        
        return container;
    }
    
    /**
     * Check if containers are empty. 
     * @return true if empty
     */
    public boolean isJavaScriptEmpty() {
        return containers.isEmpty();
    }
    
    /**
     * Writes the script containing all the scripts written to all containers, 
     * in the correct order.
     * 
     * @param writer The writer.
     * @throws IOException when IO error occurs during writing.
     * @throws OrderException when scripts can not be ordered.
     */
    public void writeOrderedJavaScript(Writer writer) throws IOException, OrderException {
        // Get the list with ordered JavaScriptContainers.
        // An OrderException is thrown, in case containers could not be ordered.
        List containers = getOrderedContainers();
        
        // Iterate over all containers, and write contained JavaScript code
        // into specified writer, separating scripts with semicolon.
        Iterator iterator = containers.iterator();
        
        while (iterator.hasNext()) {
            DefaultJavaScriptContainer container = (DefaultJavaScriptContainer) iterator.next();
            
            String javaScriptContent = container.getJavaScriptContent();
            
            if (javaScriptContent.length() != 0) {
                writer.write(javaScriptContent);
            
                writer.write(";");
                
                // TODO: Why this line separator is not rendered in output page?
                writer.write(System.getProperty("line.separator"));
            }
        }
    }
    
    /**
     * An exception thrown, when some containers could not be ordered.
     */
    public class OrderException extends Exception {
        private Collection unorderedContainers;
        
        private OrderException(String message, Collection unorderedContainers) {
            super(message);
            
            this.unorderedContainers = unorderedContainers;
        }
    
        /**
         * Returns the collection of JavaScriptContainers, which could not be ordered.
         * 
         * @return the collection of JavaScriptContainers, which could not be ordered.
         */
        protected Collection getUnorderedContainers() {
            return unorderedContainers;
        }
    }

	
    /**
     * Returns an ordered list of JavaScriptContainers, in the order or rendering.
     *  
     * @return An ordered list of JavaScriptContainers.
     * @throws OrderException If some of the containers could not be ordered.
     */
    private List getOrderedContainers() throws OrderException {
        // Create an instance of topological sorter.
        TopologicalSorter sorter = new TopologicalSorter();

        // Build a map of producers: widgetId -> JavaScriptContainer
        // and add single node for each producer to the graph.
        Map producersMap = new HashMap();
        
        for (int containerIndex = 0; containerIndex < containers.size(); containerIndex++) {
            DefaultJavaScriptContainer container = 
                (DefaultJavaScriptContainer) containers.get(containerIndex);
            
            List producedIds = container.getCreatedWidgetIds();
            
            for (int producedIdIndex = 0; producedIdIndex < producedIds.size(); producedIdIndex++) {
                String producedId = (String) producedIds.get(producedIdIndex);
                
                if (producersMap.containsKey(producedId)) {
                    throw new IllegalStateException("Widget ID produced twice."); 
                }
                
                producersMap.put(producedId, container);
            }
            
            sorter.addNode(container);
        }
        
        // Fill graph with producer-consumer edges.
        for (int containerIndex = 0; containerIndex < containers.size(); containerIndex++) {
            DefaultJavaScriptContainer consumer = 
                (DefaultJavaScriptContainer) containers.get(containerIndex);
            
            List consumedIds = consumer.getUsedWidgetIds();
            
            for (int consumedIdIndex = 0; consumedIdIndex < consumedIds.size(); consumedIdIndex++) {
                String consumedId = (String) consumedIds.get(consumedIdIndex);
                
                Object producer = producersMap.get(consumedId);
                
                if (producer == null) {
                    throw new IllegalStateException("Unspecified widget ID: " + consumedId);
                }
                
                sorter.addEdge(consumer, producer);
            }
        }
        
        // Sort the graph topologically.
        List orderedContainers;
        
        try {
            orderedContainers = sorter.sort();
        } catch (SortException e) {
            throw new OrderException("Could not order some of the containers.", e.getUnsortedNodes());
        }
        
        // Reverse the list of containers, so that all dependencies are placed
        // before its dependents.
        Collections.reverse(orderedContainers);
        
        return orderedContainers;
    }
    
    protected void addCreatedWidgetId(String widgetId) {
        boolean added = createdWidgetIds.add(widgetId);
            
        if (!added) {
            // TODO: Make a checked exception here.
            throw new IllegalArgumentException("Widget ID '" + widgetId + "' already created.");
        }
    }
}
