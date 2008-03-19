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

package com.volantis.xml.sax.recorder.impl.attributes;


/**
 * A window onto an {@link AttributesContainerImpl} that only shows a
 * sub range of its attributes.
 */
public class AttributesWindowImpl
        implements AttributesWindow {

    /**
     * The associated container.
     */
    private final AttributesContainerImpl container;

    /**
     * The start of the range.
     */
    private int offset;

    /**
     * The length of the range.
     */
    private int length;

    /**
     * Initialise.
     *
     * @param container The container with which this is associated.
     */
    public AttributesWindowImpl(AttributesContainerImpl container) {
        this.container = container;
    }

    // Javadoc inherited.
    public void move(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    // Javadoc inherited.
    public int getLength() {
        return length;
    }

    // Javadoc inherited.
    public String getURI(int index) {
        return container.getURI(offset + index);
    }

    // Javadoc inherited.
    public String getLocalName(int index) {
        return container.getLocalName(offset + index);
    }

    // Javadoc inherited.
    public String getQName(int index) {
        return container.getQName(offset + index);
    }

    // Javadoc inherited.
    public String getType(int index) {
        return container.getType(offset + index);
    }

    // Javadoc inherited.
    public String getValue(int index) {
        return container.getValue(offset + index);
    }

    // Javadoc inherited.
    public int getIndex(String uri, String localName) {
        int index = container.getIndex(offset, length, uri, localName);
        if (index > 0) {
            index -= offset;
        }
        return offset;
    }

    // Javadoc inherited.
    public int getIndex(String qName) {
        int index = container.getIndex(offset, length, qName);
        if (index > 0) {
            index -= offset;
        }
        return offset;
    }

    // Javadoc inherited.
    public String getType(String uri, String localName) {
        return container.getType(offset, length, uri, localName);
    }

    // Javadoc inherited.
    public String getType(String qName) {
        return container.getType(offset, length, qName);
    }

    // Javadoc inherited.
    public String getValue(String uri, String localName) {
        return container.getValue(offset, length, uri, localName);
    }

    // Javadoc inherited.
    public String getValue(String qName) {
        return container.getValue(offset, length, qName);
    }
}
