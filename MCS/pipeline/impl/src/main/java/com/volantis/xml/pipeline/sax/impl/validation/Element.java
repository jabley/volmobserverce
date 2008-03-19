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

package com.volantis.xml.pipeline.sax.impl.validation;



/**
 * An enumeration of the different template elements.
 *
 * @todo should use facilities provided by xml-validation subsystem.
 */
public class Element {

    /**
     * The name of the element.
     */
    private final String name;

    /**
     * The event to use for the start of the element.
     */
    private final Event startEvent;

    /**
     * The event to use for the end of the element.
     */
    private final Event endEvent;

    /**
     * True if allows any content, false otherwise.
     */
    private final boolean allowsAnyContent;

    /**
     * Initialise.
     *
     * @param name             The name of the element.
     * @param startEvent       The event to use for the start of the element.
     * @param endEvent         The event to use for the end of the element.
     * @param allowsAnyContent True if allows any content, false otherwise.
     */
    public Element(
            String name, Event startEvent, Event endEvent,
            boolean allowsAnyContent) {
        this.name = name;
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.allowsAnyContent = allowsAnyContent;
    }

    /**
     * Get the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the start event.
     *
     * @return The start event.
     */
    public Event getStartEvent() {
        return startEvent;
    }

    /**
     * Get the end event.
     *
     * @return The end event.
     */
    public Event getEndEvent() {
        return endEvent;
    }

    /**
     * Get the allows any content.
     *
     * @return The allows any context.
     */
    public boolean allowsAnyContent() {
        return allowsAnyContent;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
