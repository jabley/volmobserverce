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

package com.volantis.xml.sax.recorder.impl.attributes;

import com.volantis.xml.sax.recorder.impl.recording.StringTable;
import org.xml.sax.Attributes;

/**
 * Implementation of {@link AttributeContainerBuilder}.
 */
public class AttributeContainerBuilderImpl
        implements AttributeContainerBuilder {

    /**
     * The {@link StringTable} used to keep track of the strings that have been
     * seen so that they can be shared.
     */
    private final StringTable stringTable;

    /**
     * The length of conceptual list of attributes. This is the number of
     * attributes stored within the data.
     */
    private int length;

    /**
     * The attribute data. Each attribute consists of a contiguous sequence
     * of {@link AttributesConstants#SLOTS_PER_ATTRIBUTE} slots within the array with the 0th
     * attribute starting at the 0th index of the array. Different
     * characteristics of the attributes are stored at specific offsets within
     * the attributes slots as defined by the OFFSET_ constants above.
     */
    private String[] data;

    /**
     * Initialise.
     *
     * @param stringTable The string table to use when adding strings.
     */
    public AttributeContainerBuilderImpl(StringTable stringTable) {
        this.stringTable = stringTable;
    }

    public int getOffset() {
        return length;
    }

    // Javadoc inherited.
    public void addAttributes(Attributes attributes) {

        int extraLength = attributes.getLength();
        int newCapacity = length + extraLength;
        ensureCapacity(newCapacity);

        int offset = length;
        length = newCapacity;

        // Copy the attributes.
        for (int i = 0, dataIndex = offset *
                AttributesConstants.SLOTS_PER_ATTRIBUTE;
             i < extraLength;
             dataIndex += AttributesConstants.SLOTS_PER_ATTRIBUTE, i += 1) {

            data[dataIndex + AttributesConstants.OFFSET_URI] =
                    stringTable.intern(attributes.getURI(i));
            data[dataIndex + AttributesConstants.OFFSET_LOCAL] =
                    stringTable.intern(attributes.getLocalName(i));
            data[dataIndex + AttributesConstants.OFFSET_QNAME] =
                    stringTable.intern(attributes.getQName(i));
            data[dataIndex + AttributesConstants.OFFSET_TYPE] =
                    stringTable.intern(attributes.getType(i));
            data[dataIndex + AttributesConstants.OFFSET_VALUE] =
                    stringTable.intern(attributes.getValue(i));
        }
    }

    // Javadoc inherited.
    public AttributesContainer buildContainer() {
        return new AttributesContainerImpl(length, data);
    }

    /**
     * Ensure that this container has sufficient capacity to hold the specified
     * number of attributes.
     *
     * @param n The number of attributes that the container must hold in total.
     */
    private void ensureCapacity(int n) {
        if (n <= 0) {
            return;
        }
        int max;
        int requiredSize = n * AttributesConstants.SLOTS_PER_ATTRIBUTE;
        if (data == null || data.length == 0) {
            max = 5 * AttributesConstants.SLOTS_PER_ATTRIBUTE;
        } else if (data.length >= requiredSize) {
            return;
        } else {
            max = data.length;
        }
        while (max < requiredSize) {
            max *= 2;
        }

        String[] newData = new String[max];
        if (length > 0) {
            System.arraycopy(data, 0, newData, 0,
                    length * AttributesConstants.SLOTS_PER_ATTRIBUTE);
        }
        data = newData;
    }
}
