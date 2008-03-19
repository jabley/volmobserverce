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
package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.mcs.accessors.xml.jdom.XMLAccessorConstants;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueVisitorStub;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshallable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Marshall/Unmarshall style properties.
 */
public class MarshallStyleProperties implements IMarshaller, IUnmarshaller,
        IAliasable, XMLAccessorConstants {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
            StyleValueFactory.getDefaultInstance();

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    MarshallStyleProperties.class);

    private String uri;
    private String structureName;
    private int nsIndex;

    private ConvertStyleValues convertValues;

    /**
     * Default constructor used by JIBX.
     */
    public MarshallStyleProperties() {
        this(null, 0, STYLE_PROPERTIES_ELEMENT);
    }

    /**
     * Constructor used by JIBX to set the structure name, namespace URI and
     * namespace index.
     *
     * @param givenUri   namespace URI
     * @param givenIndex namespace index
     * @param givenName  XML element or mapping name
     */
    public MarshallStyleProperties(
            String givenUri, int givenIndex,
            String givenName) {

        structureName = givenName;
        uri = givenUri;
        nsIndex = givenIndex;

        convertValues = new ConvertStyleValues();
    }

    // Javadoc interited from interface
    public boolean isExtension(int index) {
        return false;
    }

    // Javadoc interited from interface
    public void marshal(Object obj, IMarshallingContext ictx)
            throws JiBXException {

        if (ictx instanceof MarshallingContext) {
            // start by generating start tag for container
            MarshallingContext ctx = (MarshallingContext) ictx;
            ctx.startTagAttributes(nsIndex, structureName).closeStartContent();

            if (obj != null) {

                if (!(obj instanceof MutableStyleProperties)) {
                    throw new JiBXException(EXCEPTION_LOCALIZER.format(
                            "invalid-object-type",
                            obj.getClass()));
                } else {
                    MutableStyleProperties styles
                            = (MutableStyleProperties) obj;

                    // Loop round all definitions, checking the styles
                    // object to see if the definiton is set.
                    Iterator iterator = styles.propertyValueIterator();
                    while (iterator.hasNext()) {
                        PropertyValue propertyValue = (PropertyValue) iterator.next();
                        marshallSingleProperty(propertyValue, ctx);
                    }
                }
            }
            ctx.endTag(nsIndex, structureName);
        }
    }

    /**
     * Given an index and the properties to marshall see if there is a property
     * at the given nsIndex, if so write it out to the context.
     *
     * @param propertyValue The property value that is being processed.
     * @param ctx                  Context to write the property
     * @throws JiBXException
     */
    private void marshallSingleProperty(
            PropertyValue propertyValue,
            MarshallingContext ctx)
            throws JiBXException {

        StyleProperty property = propertyValue.getProperty();
        StyleValue value = propertyValue.getValue();

        // Create the property specific element.
        String elementName = property.getName();
        ctx.startTagAttributes(nsIndex, elementName);

        if (propertyValue.getPriority() == Priority.IMPORTANT) {
            ctx.attribute(0, "priority", "important");
        }

        ctx.closeStartContent();

        // Marshal the value.
        IMarshallable marshallable = (IMarshallable) value;
        marshallable.marshal(ctx);

        ctx.endTag(nsIndex, elementName);
    }

    // Javadoc inherited from interface
    public boolean isPresent(IUnmarshallingContext ictx) throws JiBXException {
        return ictx.isAt(uri, structureName);
    }

    // Javadoc interited from interface
    public Object unmarshal(Object obj, IUnmarshallingContext ictx)
            throws JiBXException {

        MutableStyleProperties newProperties =
            StyleSheetFactory.getDefaultInstance().createStyleProperties();

        if (ictx instanceof UnmarshallingContext) {

            UnmarshallingContext ctx = (UnmarshallingContext) ictx;

            // make sure we're at the appropriate start tag
            if (!ctx.isAt(uri, structureName)) {
                ctx.throwStartTagNameError(uri, structureName);
            }

            ctx.parsePastStartTag(uri, structureName);

            String elementName;

            // while there are style elements to read unmarshal them
            while (!ctx.isEnd()) {
                elementName = ctx.toStart().toLowerCase();

                if (isKnownElement(elementName)) {

                    StylePropertyDefinitions definitions
                            = StylePropertyDetails.getDefinitions();
                    StyleProperty property = definitions.getStyleProperty(elementName);

                    // Get the priorityValue.
                    String priorityValue = getAttributeValue(ctx, "", "priority");
                    Priority priority = Priority.NORMAL;
                    if ("important".equals(priorityValue)) {
                        priority = Priority.IMPORTANT;
                    }

                    // Parse past the element.
                    ctx.parsePastStartTag(uri, elementName);

                    StyleValue value = (StyleValue) ctx.unmarshalElement();

                    // Fixup any keywords after unmarshalling.
                    value = convertValues.convertValue(property, value);

                    PropertyValue propertyValue =
                        ThemeFactory.getDefaultInstance().createPropertyValue(
                            property, value, priority);

                    newProperties.setPropertyValue(propertyValue);

                    ctx.parsePastEndTag(uri, elementName);

                } else {
                    ctx.throwStartTagException(EXCEPTION_LOCALIZER.format("unknown-style",
                                                                          elementName));
                }

            }

            ctx.parsePastEndTag(uri, structureName);
        }
        return newProperties;
    }

    /**
     * Return true if the given element name is known.
     *
     * @param elementName name to check
     * @return true if XML element name is known
     */
    private boolean isKnownElement(String elementName) {
        StylePropertyDefinitions definitions
                = StylePropertyDetails.getDefinitions();

        return (definitions.getStyleProperty(elementName) != null);
    }

    private String getAttributeValue(
            UnmarshallingContext ctx,
            String namespaceURI, String name)
            throws JiBXException {
        int count = ctx.getAttributeCount();
        for (int i = 0; i < count; i += 1) {
            if (ctx.getAttributeNamespace(i).equals(namespaceURI)
                    && ctx.getAttributeName(i).equals(name)) {
                return ctx.getAttributeValue(i);
            }
        }

        return null;
    }

    public static class ConvertStyleValues
            extends StyleValueVisitorStub {

        private StyleValue converted;

        public StyleValue convertValue(StyleProperty property, StyleValue value) {
            converted = value;
            if (value != null) {
                value.visit(this, null);
            }
            return converted;
        }

        public StyleValue convertValue(StyleValue value) {
            StyleValue oldConverted = converted;
            converted = value;
            if (value != null) {
                value.visit(this, null);
            }
            StyleValue result = converted;
            converted = oldConverted;
            return result;
        }

        public void visit(StyleColorName value, Object object) {
            // Get the shared color name.
            converted = StyleColorNames.getColorByName(value.getName());
        }

        public void visit(StyleInherit value, Object object) {
            converted = STYLE_VALUE_FACTORY.getInherit();
        }

        public void visit(StyleKeyword value, Object object) {
            // Get the shared keyword.
            converted = StyleKeywords.getKeywordByName(value.getName());
        }

        public void visit(StylePair value, Object object) {

            StyleValue first = value.getFirst();
            StyleValue firstConverted = convertValue(first);

            StyleValue second = value.getSecond();
            StyleValue secondConverted = convertValue(second);

            if (first != firstConverted || second != secondConverted) {
                converted = STYLE_VALUE_FACTORY.getPair(
                    firstConverted, secondConverted);
            }
        }

        public void visit(StyleList value, Object object) {
            List list = value.getList();
            List convertedList = new ArrayList();
            boolean changed = false;
            for (int i = 0; i < list.size(); i++) {
                StyleValue item = (StyleValue) list.get(i);
                StyleValue convertedItem = convertValue(item);
                convertedList.add(convertedItem);
                if (item != convertedItem) {
                    changed = true;
                }
            }

            if (changed) {
                converted = STYLE_VALUE_FACTORY.getList(
                    convertedList, value.isUnique());
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 05-Jul-05	8552/9	pabbott	VBM:2005051902 JIBX Javadoc updates

 29-Jun-05	8552/4	pabbott	VBM:2005051902 JIBX Theme accessors

 ===========================================================================
*/
