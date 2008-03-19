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

import java.io.Serializable;

/**
 * Container for multiple sets of {@link org.xml.sax.Attributes}.
 */
public class AttributesContainerImpl
        implements AttributesContainer, Serializable, AttributesConstants {

    /**
     * The attribute data. Each attribute consists of a contiguous sequence
     * of {@link AttributesConstants#SLOTS_PER_ATTRIBUTE} slots within the
     * array with the 0th attribute starting at the 0th index of the array.
     * Different characteristics of the attributes are stored at specific
     * offsets within the attributes slots as defined by the OFFSET_ constants
     * defined in {@link AttributesConstants}.
     *
     * <p><strong>This MUST not be modified in anyway.</p></strong>
     */
    private final String[] data;

    /**
     * The length of conceptual list of attributes. This is the number of
     * attributes stored within the data.
     */
    private final int length;

    /**
     * Initialise.
     *
     * @param length      The number of attributes in the array.
     * @param data        The indeces into the string array for each
     */
    public AttributesContainerImpl(int length, String[] data) {
        this.length = length;
        if (data == null) {
            this.data = null;
        } else {
            this.data = new String[length * SLOTS_PER_ATTRIBUTE];
            System.arraycopy(data, 0, this.data, 0, this.data.length);
        }
    }

    // Javadoc inherited.
    public AttributesWindow createWindow() {
        return new AttributesWindowImpl(this);
    }

    /**
     * Get the string for the specified slot of the specified attribute.
     *
     * @param attributeIndex The index of the attribute.
     * @param slot           The slot for the attribute.
     * @return The string value.
     */
    private final String getString(int attributeIndex, final int slot) {
        return data[attributeIndex * SLOTS_PER_ATTRIBUTE + slot];
    }

    /**
     * Return an attribute's Namespace URI.
     *
     * @param index The attribute's index (zero-based).
     * @return The Namespace URI, the empty string if none is
     *         available, or null if the index is out of range.
     * @see org.xml.sax.Attributes#getURI
     */
    public String getURI(int index) {
        if (index >= 0 && index < length) {
            return getString(index, OFFSET_URI);
        } else {
            return null;
        }
    }

    /**
     * Return an attribute's local name.
     *
     * @param index The attribute's index (zero-based).
     * @return The attribute's local name, the empty string if
     *         none is available, or null if the index if out of range.
     * @see org.xml.sax.Attributes#getLocalName
     */
    public String getLocalName(int index) {
        if (index >= 0 && index < length) {
            return getString(index, OFFSET_LOCAL);
        } else {
            return null;
        }
    }

    /**
     * Return an attribute's qualified (prefixed) name.
     *
     * @param index The attribute's index (zero-based).
     * @return The attribute's qualified name, the empty string if
     *         none is available, or null if the index is out of bounds.
     * @see org.xml.sax.Attributes#getQName
     */
    public String getQName(int index) {
        if (index >= 0 && index < length) {
            return getString(index, OFFSET_QNAME);
        } else {
            return null;
        }
    }

    /**
     * Return an attribute's type by index.
     *
     * @param index The attribute's index (zero-based).
     * @return The attribute's type, "CDATA" if the type is unknown, or null
     *         if the index is out of bounds.
     * @see org.xml.sax.Attributes#getType(int)
     */
    public String getType(int index) {
        if (index >= 0 && index < length) {
            return getString(index, OFFSET_TYPE);
        } else {
            return null;
        }
    }

    /**
     * Return an attribute's value by index.
     *
     * @param index The attribute's index (zero-based).
     * @return The attribute's value or null if the index is out of bounds.
     * @see org.xml.sax.Attributes#getValue(int)
     */
    public String getValue(int index) {
        if (index >= 0 && index < length) {
            return getString(index, OFFSET_VALUE);
        } else {
            return null;
        }
    }

    /**
     * Look up an attribute's index by Namespace name in a sub range of the
     * set of attributes.
     *
     * <p>In many cases, it will be more efficient to look up the name once and
     * use the index query methods rather than using the name query methods
     * repeatedly.</p>
     *
     * @param offset    The start of the range within which the search is
     *                  constrained.
     * @param length    The length of the range within which the search is
     *                  constrained.
     * @param uri       The attribute's Namespace URI, or the empty
     *                  string if none is available.
     * @param localName The attribute's local name.
     * @return The attribute's index, or -1 if none matches.
     * @see org.xml.sax.Attributes#getIndex(java.lang.String,java.lang.String)
     */
    public int getIndex(
            int offset, int length,
            String uri, String localName) {

        int start = offset * SLOTS_PER_ATTRIBUTE;
        int end = start + length * SLOTS_PER_ATTRIBUTE;
        int index = offset;
        for (int i = start; i < end; i += SLOTS_PER_ATTRIBUTE, index += 1) {
            if (data[i + OFFSET_URI].equals(uri) &&
                    data[i + OFFSET_LOCAL].equals(localName)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Look up an attribute's index by qualified (prefixed) name in a sub range
     * of the set of attributes.
     *
     * @param offset The start of the range within which the search is
     *               constrained.
     * @param length The length of the range within which the search is
     *               constrained.
     * @param qName  The qualified name.
     * @return The attribute's index, or -1 if none matches.
     * @see org.xml.sax.Attributes#getIndex(java.lang.String)
     */
    public int getIndex(int offset, int length, String qName) {

        int start = offset * SLOTS_PER_ATTRIBUTE;
        int end = start + length * SLOTS_PER_ATTRIBUTE;
        int index = offset;
        for (int i = start; i < end; i += SLOTS_PER_ATTRIBUTE, index += 1) {
            if (data[i + OFFSET_QNAME].equals(qName)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Look up an attribute's type by Namespace-qualified name in a sub range
     * of the set of attributes.
     *
     * @param offset    The start of the range within which the search is
     *                  constrained.
     * @param length    The length of the range within which the search is
     *                  constrained.
     * @param uri       The Namespace URI, or the empty string for a name
     *                  with no explicit Namespace URI.
     * @param localName The local name.
     * @return The attribute's type, or null if there is no
     *         matching attribute.
     * @see org.xml.sax.Attributes#getType(java.lang.String,java.lang.String)
     */
    public String getType(
            int offset, int length,
            String uri, String localName) {

        int index = getIndex(offset, length, uri, localName);
        if (index == -1) {
            return null;
        } else {
            return getString(index, OFFSET_TYPE);
        }
    }

    /**
     * Look up an attribute's type by qualified (prefixed) name in a sub range
     * of the set of attributes.
     *
     * @param offset The start of the range within which the search is
     *               constrained.
     * @param length The length of the range within which the search is
     *               constrained.
     * @param qName  The qualified name.
     * @return The attribute's type, or null if there is no
     *         matching attribute.
     * @see org.xml.sax.Attributes#getType(java.lang.String)
     */
    public String getType(int offset, int length, String qName) {

        int index = getIndex(offset, length, qName);
        if (index == -1) {
            return null;
        } else {
            return getString(index, OFFSET_TYPE);
        }
    }

    /**
     * Look up an attribute's value by Namespace-qualified name in a sub range
     * of the set of attributes.
     *
     * @param offset    The start of the range within which the search is
     *                  constrained.
     * @param length    The length of the range within which the search is
     *                  constrained.
     * @param uri       The Namespace URI, or the empty string for a name
     *                  with no explicit Namespace URI.
     * @param localName The local name.
     * @return The attribute's value, or null if there is no
     *         matching attribute.
     * @see org.xml.sax.Attributes#getValue(java.lang.String,java.lang.String)
     */
    public String getValue(
            int offset, int length,
            String uri, String localName) {

        int index = getIndex(offset, length, uri, localName);
        if (index == -1) {
            return null;
        } else {
            return getString(index, OFFSET_VALUE);
        }
    }

    /**
     * Look up an attribute's value by qualified (prefixed) name in a sub range
     * of the set of attributes.
     *
     * @param offset The start of the range within which the search is
     *               constrained.
     * @param length The length of the range within which the search is
     *               constrained.
     * @param qName  The qualified name.
     * @return The attribute's value, or null if there is no
     *         matching attribute.
     * @see org.xml.sax.Attributes#getValue(java.lang.String)
     */
    public String getValue(
            int offset, int length,
            String qName) {

        int index = getIndex(offset, length, qName);
        if (index == -1) {
            return null;
        } else {
            return getString(index, OFFSET_VALUE);
        }
    }
}
