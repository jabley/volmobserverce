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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web;

/**
 * Represents the content element.
 */
public class Content {

    /**
     * The type property.
     */
    private final String type;

    /**
     * The action property.
     */
    private final ContentAction action;

    /**
     * Construct a new Content object with the specified properties.
     * @param type The type of content this Content object referes to.
     * @param action The action to take when content of the specified type is
     * encountered.
     */
    public Content(String type, ContentAction action) {
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (action == null) {
            throw new IllegalArgumentException("action cannot be null");
        }
        this.type = type;
        this.action = action;
    }

    /**
     * Get the type property.
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get the action property.
     * @return The action.
     */
    public ContentAction getAction() {
        return action;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Content)) return false;

        final Content content = (Content) o;

        if (!action.equals(content.action)) return false;
        if (!type.equals(content.type)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = type.hashCode();
        result = 29 * result + action.hashCode();
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 24-Jul-03	217/1	allan	VBM:2003071702 WebDriver implementation. Intermediate commit

 ===========================================================================
*/
