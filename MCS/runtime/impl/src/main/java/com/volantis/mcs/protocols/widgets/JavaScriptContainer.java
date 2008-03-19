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

import java.io.Writer;

/**
 * The container of JavaScript code with widget instantiation.
 * 
 * This container provides a writer to write JavaScript content. It is also
 * responsible for gathering information about created and used widget IDs.
 */
public interface JavaScriptContainer {
    /**
     * Returns the writer used to write JavaScript code.
     * 
     * @return the writer used to write JavaScript code.
     */
    Writer getWriter();
    
    /**
     * Adds an ID of the widget created by JavaScript code written to this
     * container.
     * 
     * @param id The widget ID
     */
    void addCreatedWidgetId(String id);

    /**
     * Adds an ID of the widget used by JavaScript code written to this
     * container.
     * 
     * @param id The widget ID
     */
    void addUsedWidgetId(String id);
}
