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
package com.volantis.mcs.eclipse.common;

import org.jdom.Attribute;

import java.util.List;

/**
 * A formatter that works on formats that specify a attributes instead
 * of a field number. In other words a format that looks like: <code> {device}
 * {value}</code> will, when given an object that contains the properties
 * "device" and "value", produce a message that is the contents of the device
 * attribute followed by the contents of the value attribute.
 */
public class AttributesMessageFormatter {

    /**
     * The prefix for resource message keys.
     */
    private static final String RESOURCE_PREFIX =
            "AttributesMessageFormatter."; //$NON-NLS-1$

    /**
     * The maximum width for attribute values.
     */
    private final static int ATTRIBUTE_VALUE_MAX_WIDTH =
            EclipseCommonMessages.getInteger(RESOURCE_PREFIX +
            "attributeValueMaxWidth").intValue(); //$NON-NLS-1$

    /**
     * The AttributesDetails associated with this AttributesMessageFormatter.
     */
    private final AttributesDetails attributesDetails;

    /**
     * The special format argument deisgnating use of a given element. I.e
     * if a formatting arg in the given unformatted message is {element} then
     * it will be substituted by a given element name is there is one.
     */
    public static final String ELEMENT_DESIGNATOR = "element"; //$NON-NLS-1$

    /**
     * Construct a new AttributesMessageFormatter.
     * @param attributesDetails The AttributesDetails used to discover
     * presentable values for attributes. Can be null if presentable
     * attribute values are not required.
     */
    public AttributesMessageFormatter(AttributesDetails attributesDetails) {
        this.attributesDetails = attributesDetails;
    }

    /**
     * Given the name of an attribute and a List of Attributes retrieve
     * the value of the named attribute.
     * @param attributeName The attribute name.
     * @param attributes The List of Attributes.
     * @return The value of the named attribute or null if the named attribute
     * was not in the list.
     */
    private String retrieveAttributeValue(String attributeName,
                                          List attributes) {
        String value = null;
        for (int i = 0; i < attributes.size() && value == null; i++) {
            Attribute attribute = (Attribute) attributes.get(i);
            if (attributeName.equals(attribute.getName())) {
                value = attribute.getValue();
            }
        }

        if (value != null) {
            if (attributesDetails != null) {
                // Use the presentable value for the attribute.
                value = attributesDetails.getPresentableValue(attributeName,
                        value);
            }

            // Attribute values have a maximum width property. If they are
            // longer than this value they must be truncated and have ...
            // appended.
            if (value.length() > ATTRIBUTE_VALUE_MAX_WIDTH) {
                StringBuffer buffer =
                        new StringBuffer(ATTRIBUTE_VALUE_MAX_WIDTH + 3);
                buffer.append(value.substring(0, ATTRIBUTE_VALUE_MAX_WIDTH));
                buffer.append("..."); //$NON-NLS-1$
                value = buffer.toString();
            }
        }
        return value;
    }

    /**
     * Unfortunately super class versions of the format method are all final
     * so we cannot override this method. Instead we add a message
     * argument to allow the user to override the message associated with
     * this AttributesMessageFormat (and allow us to have a format method).
     *
     * A message is the message in a properties bundle that contains
     * formatting e.g. {device}, {pixelDepth}bits, {pixelsX}x{pixelsY}px
     *
     * @param attributes The List of Attributes to use in creating the
     * formatted message.
     * @param message The message to be formatted.
     * @param elementName The name of the element if this is to be included
     * in the formatted message. If so then the message should include a
     * corresponding {element}. Typically this name would be a
     * localized name for the element. Can be null.
     */
    public String format(List attributes, String message,
                         String elementName) {
        StringBuffer formatBuffer = new StringBuffer(message.length());
        StringBuffer attributeBuffer = new StringBuffer();
        String attribute = null;
        String attributeValue = null;
        StringBuffer currentBuffer = formatBuffer;
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '{') {
                currentBuffer = attributeBuffer;
            } else if (message.charAt(i) == '}') {
                currentBuffer = formatBuffer;
                attribute = attributeBuffer.toString();

                if (elementName != null &&
                        attribute.equals(ELEMENT_DESIGNATOR)) {
                    attributeValue = elementName;
                } else {
                    attributeValue =
                            retrieveAttributeValue(attribute, attributes);
                }
                if (attributeValue == null || attributeValue.length() < 1) {
                    // We have no attribute value to append so we need
                    // to remove anything associated with this attribute
                    // such as qualifying text in the message.
                    // To do this, we skip ahead to the next { or non-
                    // white-space after the next ,
                    boolean doneSkipping = false;
                    boolean reachedComma = false;
                    boolean reachedWhitespace = false;
                    int i2;
                    for (i2 = i; i2 < message.length() && !doneSkipping;
                         i2++) {
                        boolean isWhitespace = Character.isWhitespace(
                                message.charAt(i2));
                        doneSkipping = message.charAt(i2) == '{' ||
                                (reachedComma && !isWhitespace) ||
                                reachedWhitespace;
                        if (!doneSkipping) {
                            if (message.charAt(i2) == ',') {
                                reachedComma = true;
                            } else if (isWhitespace) {
                                reachedWhitespace = true;
                            }
                        }
                    }
                    if (i2 < message.length()) {
                        i = i2 - 2;
                    } else {
                        i = message.length();
                    }
                } else {
                    currentBuffer.append(attributeValue);
                }
                attributeBuffer.setLength(0);
            } else {
                currentBuffer.append(message.charAt(i));
            }
        }

        // Remove a trailing , if there is one and whitespace.
        if(formatBuffer.length()>0) {
            boolean stop = false;
            int i;
            for (i = formatBuffer.length() - 1; !stop && i>=0; i--) {
                stop = formatBuffer.charAt(i) != ',' &&
                        !Character.isWhitespace(formatBuffer.charAt(i));
            }
            formatBuffer.setLength(i + 2);
        }

        return formatBuffer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Feb-04	3181/1	byron	VBM:2004020904 Components: Dynamic Visual asset value has default value of px

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 04-Jan-04	2309/1	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
