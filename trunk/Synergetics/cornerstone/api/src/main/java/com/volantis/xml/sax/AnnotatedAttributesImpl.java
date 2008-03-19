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

package com.volantis.xml.sax;

import org.xml.sax.Attributes;

/**
 * Implementation of the {@link AnnotatedAttributes} interface to support
 * passing annotations.
 *
 * <p>Based on the {@link org.xml.sax.helpers.AttributesImpl}
 * implementation.</p>
 */
public class AnnotatedAttributesImpl
        implements AnnotatedAttributes {

    /**
     * The number of slots used within the data array per attribute.
     */
    private static final int SLOTS_PER_ATTRIBUTE = 6;

    /**
     * The offset of the URI within the set of slots for an attribute.
     */
    private static final int OFFSET_URI = 0;

    /**
     * The offset of the local name within the set of slots for an attribute.
     */
    private static final int OFFSET_LOCAL = 1;

    /**
     * The offset of the qualified name within the set of slots for an
     * attribute.
     */
    private static final int OFFSET_QNAME = 2;

    /**
     * The offset of the type within the set of slots for an attribute.
     */
    private static final int OFFSET_TYPE = 3;

    /**
     * The offset of the value within the set of slots for an attribute.
     */
    private static final int OFFSET_VALUE = 4;

    /**
     * The offset of the annotation within the set of slots for an attribute.
     */
    private static final int OFFSET_ANNOTATION = 5;

    private int length;
    private Object[] data;

    /**
     * Initialise.
     */
    public AnnotatedAttributesImpl() {
        length = 0;
        data = null;
    }


    /**
     * Initialise from an existing set of attributes.
     *
     * @param attributes The existing Attributes object.
     */
    public AnnotatedAttributesImpl(Attributes attributes) {
        setAttributes(attributes);
    }

    /**
     * Return the number of attributes in the list.
     *
     * @return The number of attributes in the list.
     * @see Attributes#getLength
     */
    public int getLength() {
        return length;
    }

    /**
     * Return an attribute's Namespace URI.
     *
     * @param index The attribute's index (zero-based).
     * @return The Namespace URI, the empty string if none is
     *         available, or null if the index is out of range.
     * @see Attributes#getURI
     */
    public String getURI(int index) {
        if (index >= 0 && index < length) {
            return (String) data[index * SLOTS_PER_ATTRIBUTE + OFFSET_URI];
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
     * @see Attributes#getLocalName
     */
    public String getLocalName(int index) {
        if (index >= 0 && index < length) {
            return (String) data[index * SLOTS_PER_ATTRIBUTE + OFFSET_LOCAL];
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
     * @see Attributes#getQName
     */
    public String getQName(int index) {
        if (index >= 0 && index < length) {
            return (String) data[index * SLOTS_PER_ATTRIBUTE + OFFSET_QNAME];
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
     * @see Attributes#getType(int)
     */
    public String getType(int index) {
        if (index >= 0 && index < length) {
            return (String) data[index * SLOTS_PER_ATTRIBUTE + OFFSET_TYPE];
        } else {
            return null;
        }
    }

    /**
     * Return an attribute's value by index.
     *
     * @param index The attribute's index (zero-based).
     * @return The attribute's value or null if the index is out of bounds.
     * @see Attributes#getValue(int)
     */
    public String getValue(int index) {
        if (index >= 0 && index < length) {
            return (String) data[index * SLOTS_PER_ATTRIBUTE + OFFSET_VALUE];
        } else {
            return null;
        }
    }

    // Javadoc inherited.
    public Object getAnnotation(int index) {
        if (index >= 0 && index < length) {
            return data[index * SLOTS_PER_ATTRIBUTE + OFFSET_ANNOTATION];
        } else {
            return null;
        }
    }

    /**
     * Look up an attribute's index by Namespace name.
     *
     * <p>In many cases, it will be more efficient to look up the name once and
     * use the index query methods rather than using the name query methods
     * repeatedly.</p>
     *
     * @param uri       The attribute's Namespace URI, or the empty
     *                  string if none is available.
     * @param localName The attribute's local name.
     * @return The attribute's index, or -1 if none matches.
     * @see Attributes#getIndex(java.lang.String,java.lang.String)
     */
    public int getIndex(String uri, String localName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_URI].equals(uri) &&
                    data[i + OFFSET_LOCAL].equals(localName)) {
                return i / SLOTS_PER_ATTRIBUTE;
            }
        }
        return -1;
    }

    /**
     * Look up an attribute's index by qualified (prefixed) name.
     *
     * @param qName The qualified name.
     * @return The attribute's index, or -1 if none matches.
     * @see Attributes#getIndex(java.lang.String)
     */
    public int getIndex(String qName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_QNAME].equals(qName)) {
                return i / SLOTS_PER_ATTRIBUTE;
            }
        }
        return -1;
    }

    /**
     * Look up an attribute's type by Namespace-qualified name.
     *
     * @param uri       The Namespace URI, or the empty string for a name
     *                  with no explicit Namespace URI.
     * @param localName The local name.
     * @return The attribute's type, or null if there is no
     *         matching attribute.
     * @see Attributes#getType(java.lang.String,java.lang.String)
     */
    public String getType(String uri, String localName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_URI].equals(uri) &&
                    data[i + OFFSET_LOCAL].equals(localName)) {
                return (String) data[i + OFFSET_TYPE];
            }
        }
        return null;
    }

    /**
     * Look up an attribute's type by qualified (prefixed) name.
     *
     * @param qName The qualified name.
     * @return The attribute's type, or null if there is no
     *         matching attribute.
     * @see Attributes#getType(java.lang.String)
     */
    public String getType(String qName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_QNAME].equals(qName)) {
                return (String) data[i + OFFSET_TYPE];
            }
        }
        return null;
    }

    /**
     * Look up an attribute's value by Namespace-qualified name.
     *
     * @param uri       The Namespace URI, or the empty string for a name
     *                  with no explicit Namespace URI.
     * @param localName The local name.
     * @return The attribute's value, or null if there is no
     *         matching attribute.
     * @see Attributes#getValue(java.lang.String,java.lang.String)
     */
    public String getValue(String uri, String localName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_URI].equals(uri) &&
                    data[i + OFFSET_LOCAL].equals(localName)) {
                return (String) data[i + OFFSET_VALUE];
            }
        }
        return null;
    }

    /**
     * Look up an attribute's value by qualified (prefixed) name.
     *
     * @param qName The qualified name.
     * @return The attribute's value, or null if there is no
     *         matching attribute.
     * @see Attributes#getValue(java.lang.String)
     */
    public String getValue(String qName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_QNAME].equals(qName)) {
                return (String) data[i + OFFSET_VALUE];
            }
        }
        return null;
    }

    // Javadoc inherited.
    public Object getAnnotation(String uri, String localName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_URI].equals(uri) &&
                    data[i + OFFSET_LOCAL].equals(localName)) {
                return data[i + OFFSET_ANNOTATION];
            }
        }
        return null;
    }

    // Javadoc inherited.
    public Object getAnnotation(String qName) {
        int max = length * SLOTS_PER_ATTRIBUTE;
        for (int i = 0; i < max; i += SLOTS_PER_ATTRIBUTE) {
            if (data[i + OFFSET_QNAME].equals(qName)) {
                return data[i + OFFSET_ANNOTATION];
            }
        }
        return null;
    }

    /**
     * Clear the attribute list for reuse.
     *
     * <p>Note that no memory is actually freed by this call:
     * the current arrays are kept so that they can be
     * reused.</p>
     */
    public void clear() {
        length = 0;
    }

    /**
     * Copy an entire Attributes object.
     *
     * <p>It may be more efficient to reuse an existing object
     * rather than constantly allocating new ones.</p>
     *
     * @param attributes The attributes to copy.
     */
    public void setAttributes(Attributes attributes) {
        clear();
        length = attributes.getLength();
        if (length > 0) {
            data = new Object[length * SLOTS_PER_ATTRIBUTE];
            for (int i = 0, index = 0; index < length;
                 i += SLOTS_PER_ATTRIBUTE, index += 1) {

                data[i + OFFSET_URI] = attributes.getURI(index);
                data[i + OFFSET_LOCAL] = attributes.getLocalName(index);
                data[i + OFFSET_QNAME] = attributes.getQName(index);
                data[i + OFFSET_TYPE] = attributes.getType(index);
                data[i + OFFSET_VALUE] = attributes.getValue(index);
            }
        }
    }

    /**
     * Add an attribute to the end of the list.
     *
     * <p>For the sake of speed, this method does no checking
     * to see if the attribute is already in the list: that is
     * the responsibility of the application.</p>
     *
     * @param uri        The Namespace URI, or the empty string if
     *                   none is available or Namespace processing is not
     *                   being performed.
     * @param localName  The local name, or the empty string if
     *                   Namespace processing is not being performed.
     * @param qName      The qualified (prefixed) name, or the empty string
     *                   if qualified names are not available.
     * @param type       The attribute type as a string.
     * @param value      The attribute value.
     * @param annotation The annotation.
     */
    public void addAttribute(
            String uri, String localName, String qName,
            String type, String value, Object annotation) {
        ensureCapacity(length + 1);
        int i = length * SLOTS_PER_ATTRIBUTE;
        data[i + OFFSET_URI] = uri;
        data[i + OFFSET_LOCAL] = localName;
        data[i + OFFSET_QNAME] = qName;
        data[i + OFFSET_TYPE] = type;
        data[i + OFFSET_VALUE] = value;
        data[i + OFFSET_ANNOTATION] = annotation;
        length++;
    }

    /**
     * Set an attribute in the list.
     *
     * <p>For the sake of speed, this method does no checking
     * for name conflicts or well-formedness: such checks are the
     * responsibility of the application.</p>
     *
     * @param index     The index of the attribute (zero-based).
     * @param uri       The Namespace URI, or the empty string if
     *                  none is available or Namespace processing is not
     *                  being performed.
     * @param localName The local name, or the empty string if
     *                  Namespace processing is not being performed.
     * @param qName     The qualified name, or the empty string
     *                  if qualified names are not available.
     * @param type      The attribute type as a string.
     * @param value     The attribute value.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setAttribute(
            int index, String uri, String localName,
            String qName, String type, String value) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_URI] = uri;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_LOCAL] = localName;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_QNAME] = qName;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_TYPE] = type;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_VALUE] = value;
        } else {
            badIndex(index);
        }
    }

    /**
     * Remove an attribute from the list.
     *
     * @param index The index of the attribute (zero-based).
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void removeAttribute(int index) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_URI] = null;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_LOCAL] = null;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_QNAME] = null;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_TYPE] = null;
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_VALUE] = null;
            if (index < length - 1) {
                System.arraycopy(data, (index + 1) * SLOTS_PER_ATTRIBUTE, data, index *
                        SLOTS_PER_ATTRIBUTE,
                        (length - index - 1) * SLOTS_PER_ATTRIBUTE);
            }
            length--;
        } else {
            badIndex(index);
        }
    }

    /**
     * Set the Namespace URI of a specific attribute.
     *
     * @param index The index of the attribute (zero-based).
     * @param uri   The attribute's Namespace URI, or the empty
     *              string for none.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setURI(int index, String uri) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_URI] = uri;
        } else {
            badIndex(index);
        }
    }

    /**
     * Set the local name of a specific attribute.
     *
     * @param index     The index of the attribute (zero-based).
     * @param localName The attribute's local name, or the empty
     *                  string for none.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setLocalName(int index, String localName) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_LOCAL] = localName;
        } else {
            badIndex(index);
        }
    }

    /**
     * Set the qualified name of a specific attribute.
     *
     * @param index The index of the attribute (zero-based).
     * @param qName The attribute's qualified name, or the empty
     *              string for none.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setQName(int index, String qName) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_QNAME] = qName;
        } else {
            badIndex(index);
        }
    }

    /**
     * Set the type of a specific attribute.
     *
     * @param index The index of the attribute (zero-based).
     * @param type  The attribute's type.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setType(int index, String type) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_TYPE] = type;
        } else {
            badIndex(index);
        }
    }

    /**
     * Set the value of a specific attribute.
     *
     * @param index The index of the attribute (zero-based).
     * @param value The attribute's value.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setValue(int index, String value) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_VALUE] = value;
        } else {
            badIndex(index);
        }
    }

    /**
     * Set the value and annotation of a specific attribute
     *
     * @param index      The index of the attribute (zero-based).
     * @param value      The attribute's value.
     * @param annotation The annotation.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setAnnotatedValue(int index, String value, Object annotation) {
        if (index >= 0 && index < length) {
            int i = index * SLOTS_PER_ATTRIBUTE;
            data[i + OFFSET_VALUE] = value;
            data[i + OFFSET_ANNOTATION] = annotation;
        } else {
            badIndex(index);
        }
    }

    /**
     * Set the annotation of a specific attribute
     *
     * @param index      The index of the attribute (zero-based).
     * @param annotation The annotation.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          When the
     *          supplied index does not point to an attribute
     *          in the list.
     */
    public void setAnnotation(int index, Object annotation) {
        if (index >= 0 && index < length) {
            data[index * SLOTS_PER_ATTRIBUTE + OFFSET_ANNOTATION] = annotation;
        } else {
            badIndex(index);
        }
    }

    /**
     * Ensure the internal array's capacity.
     *
     * @param n The minimum number of attributes that the array must
     *          be able to hold.
     */
    private void ensureCapacity(int n) {
        if (n <= 0) {
            return;
        }
        int max;
        if (data == null || data.length == 0) {
            max = 5 * SLOTS_PER_ATTRIBUTE;
        } else if (data.length >= n * SLOTS_PER_ATTRIBUTE) {
            return;
        } else {
            max = data.length;
        }
        while (max < n * SLOTS_PER_ATTRIBUTE) {
            max *= 2;
        }

        Object newData[] = new Object[max];
        if (length > 0) {
            System.arraycopy(data, 0, newData, 0, length * SLOTS_PER_ATTRIBUTE);
        }
        data = newData;
    }

    /**
     * Report a bad array index in a manipulator.
     *
     * @param index The index to report.
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          Always.
     */
    private void badIndex(int index)
            throws ArrayIndexOutOfBoundsException {
        String msg =
                "Attempt to modify attribute at illegal index: " + index;
        throw new ArrayIndexOutOfBoundsException(msg);
    }
}
