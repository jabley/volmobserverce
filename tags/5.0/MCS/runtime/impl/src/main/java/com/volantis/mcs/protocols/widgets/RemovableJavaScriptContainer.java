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
package com.volantis.mcs.protocols.widgets;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaScriptContainer that expose reads method for created and used widget id lists.
 */     
public class RemovableJavaScriptContainer implements JavaScriptContainer,AccessibleJavaScriptContainer {

    /**
     * A list of created widget IDs. 
     */
    private List createdWidgetIds = new ArrayList();
    
    /**
     * A list of used widget IDs. 
     */
    private List usedWidgetIds = new ArrayList();
    
    /**
     * writer
     */
    private Writer writer;
    
    /**
     * Initializes this container.
     */
    protected RemovableJavaScriptContainer() {
        writer = new StringWriter();
    }    
    
    // javadoc inherited
    public void addCreatedWidgetId(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        createdWidgetIds.add(id);        
    }

    // javadoc inherited
    public void addUsedWidgetId(String id) {
        if (id == null) {
            throw new NullPointerException();
        }
        
        usedWidgetIds.add(id);
    }

    // javadoc inherited
    public Writer getWriter() {
        return writer;
    }

    // javadoc inherited
    public List getCreatedWidgetsIdList() {
        return this.createdWidgetIds;
    }

    // javadoc inherited
    public List getUsedWidgetsIdList() {
        return this.usedWidgetIds;
    }
    
    // javadoc inherited
    public String getJavaScriptContent() {
        return writer.toString();
    }    
    

}
