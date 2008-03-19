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

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * An implementation of JavaScriptContainer provided by the DefaultWidgetModule. 
 */
public class DefaultJavaScriptContainer implements JavaScriptContainer {
    /**
     * The writer for JavaScript code.
     */
    private Writer writer;
    
    /**
     * A list of created widget IDs. 
     */
    private List createdWidgetIds = new ArrayList();
    
    /**
     * A list of used widget IDs. 
     */
    private List usedWidgetIds = new ArrayList();
    
    /**
     * A list of created widget IDs, for external, read-only use.
     */
    private List externalCreatedWidgetIds = Collections.unmodifiableList(createdWidgetIds);
    
    /**
     * A list of used widget IDs, for external, read-only use.
     */
    private List externalUsedWidgetIds = Collections.unmodifiableList(usedWidgetIds);
    
    /**
     * A factory which created this container.
     */
    private final DefaultJavaScriptContainerFactory factory;
    
    /**
     * Initializes this container.
     */
    protected DefaultJavaScriptContainer(DefaultJavaScriptContainerFactory factory) {
        this.factory = factory;
        
        writer = new StringWriter();
    }
    
    /**
     * @inheritDoc
     */
    public Writer getWriter() {
        return writer;
    }

    /**
     * @inheritDoc
     */
    public void addCreatedWidgetId(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        
        factory.addCreatedWidgetId(id);
        
        createdWidgetIds.add(id);
    }

    /**
     * @inheritDoc
     */
    public void addUsedWidgetId(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        
        usedWidgetIds.add(id);
    }
    
    /**
     * Returns the list with used widget IDs.
     * 
     * @return the list with used widget IDs.
     */
    protected List getUsedWidgetIds() {
        return externalUsedWidgetIds;
    }

    /**
     * Returns the list with created widget IDs.
     * 
     * @return the list with created widget IDs.
     */
    protected List getCreatedWidgetIds() {
        return externalCreatedWidgetIds;
    }
    
    /**
     * Returns the current JavaScript content written by the writer.
     * 
     * @return the current JavaScript content written by the writer.
     */
    protected String getJavaScriptContent() {
        return writer.toString();
    }
}
