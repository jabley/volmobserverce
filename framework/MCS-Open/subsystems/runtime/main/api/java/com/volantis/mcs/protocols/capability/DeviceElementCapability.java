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
package com.volantis.mcs.protocols.capability;

import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Provides supporting operations of a device for a given element
 */
public class DeviceElementCapability {

    /**
     * the element for which the capabilities apply
     */ 
    private final String element;

    /**
     * the level of support provided for this element
     */
    private CapabilitySupportLevel elementSupportLevel;

    /**
     * the collection of supported style properties. Each style property maps
     * to a CapabilitySupportLevel only once.
     */
    private final Map supportedStyleProperties = new HashMap();

    /**
     * This collection represents the attributes that are supported for this
     * element, mapped to the degree of support. Each attribute maps to a
     * CapabilitySupportLevel only once.
     */
    private final Map supportedAttributes = new HashMap();

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param element               element to which these capabilities apply
     * @param elementSupportLevel   level of support provided for this element
     */
    public DeviceElementCapability(String element,
                                   CapabilitySupportLevel elementSupportLevel) {
        this.element = element;
        this.elementSupportLevel = elementSupportLevel;
    }

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param element               element to which these capabilities apply
     * @param supportLevel          level of support provided for this element
     * @param supportedAttributes   map of style properties against the level
     *                              of support provided for them
     */
    public DeviceElementCapability(String element,
                                   CapabilitySupportLevel supportLevel,
                                   Map supportedAttributes) {
        this (element, supportLevel);
        this.supportedAttributes.putAll(supportedAttributes);
    }

    /**
     * get the element type for which the capabilities apply
     * @return element
     */ 
    public String getElementType() {
        return element;
    }

    /**
     * get the level of support provided for this element
     * @return
     */
    public CapabilitySupportLevel getElementSupportLevel() {
        return elementSupportLevel;
    }

    /**
     * Set the level of support provided for this element.
     *
     * @param supportLevel  level of support provided for this element
     */
    void setElementSupportLevel(CapabilitySupportLevel supportLevel) {
        // only replace the value if the new one is non null
        if (supportLevel != null){
            // do not override if elementSupportLevel was previously set
            // by protocol and elementSupportLevel value from repository is default
            if ( (elementSupportLevel != null &&
                    !(supportLevel.toString()).equals("default")) ||
                    elementSupportLevel == null )
                this.elementSupportLevel = supportLevel;
            if (CapabilitySupportLevel.NONE.equals(supportLevel)) {
                supportedStyleProperties.clear();
            }
        }
    }

    /**
     * add a supported style property for this element
     * @param styleProperty
     * @param supportLevel
     */
    void addSupportedStyleProperty(StyleProperty styleProperty,
                                 CapabilitySupportLevel supportLevel) {
        if (styleProperty != null && supportLevel != null) {
            supportedStyleProperties.put(styleProperty, supportLevel);
        }
    }

    /**
     * Add a supported attribute for this element.
     * @param attributeName of the attribute whose support level to set
     * @param supportLevel  degree of support for this attribute
     */
    void addSupportedAttribute(String attributeName,
            CapabilitySupportLevel supportLevel) {
        if (attributeName != null && !"".equals(attributeName) &&
                supportLevel != null ){
            // do not override if elementSupportLevel was previously set
            // by protocol and elementSupportLevel value from repository is default
            if ( (supportedAttributes.get(attributeName) != null &&
                    !(supportLevel.toString()).equals("default")) ||
                    supportedAttributes.get(attributeName) == null ){
                supportedAttributes.put(attributeName, supportLevel);
            }
        }
    }

    /**
     * Get the support level for a given StyleProperty provided for this
     * element by a device
     * @param styleProperty
     * @return
     */
    public CapabilitySupportLevel getSupportType(StyleProperty styleProperty) {
        CapabilitySupportLevel supportType = null;
        if (styleProperty != null) {
            Object supportObject = supportedStyleProperties.get(styleProperty);
            if (supportObject instanceof CapabilitySupportLevel) {
                supportType = (CapabilitySupportLevel)
                    supportObject;
            }
        }
        return supportType;
    }

    /**
     * Get the support level of a collection of StyleProperties provided for
     * this element by a device
     * @param styleProperties - List of StyleProperty objects
     * @return
     */
    public Map getSupportType(MutablePropertyValues
            styleProperties) {

        Map allSupportTypes = new HashMap();

        Map nonNulls = new HashMap();

        for (Iterator iterator = styleProperties.stylePropertyIterator();
             iterator.hasNext(); ) {
            StyleProperty styleProperty = (StyleProperty) iterator.next();
            CapabilitySupportLevel supportType = getSupportType(styleProperty);

            if (supportType != null) {
                nonNulls.put(styleProperty, supportType);
            }

            allSupportTypes.put(styleProperty, supportType);
        }
        return allSupportTypes;
    }

    /**
     * Get the support level provided for a given attribute on this element by
     * a device.
     *
     * @param attributeName of the attribute whose support level to determine
     * @return CapabilitySupportLevel degree of support for this attribute on
     * this element
     */
    public CapabilitySupportLevel getSupportType(String attributeName) {
        CapabilitySupportLevel supportType = null;
        if (attributeName != null && !attributeName.equals("")) {
            Object supportObject = supportedAttributes.get(attributeName);
            if (supportObject instanceof CapabilitySupportLevel) {
                supportType = (CapabilitySupportLevel)
                    supportObject;
            }
        }
        return supportType;
    }

    // javadoc inheritted
    public String toString() {
        return "'" + element + "' capabilities {"
                + "support=" + elementSupportLevel
                + ", supportedStyleProperties=" + supportedStyleProperties
                + ", supportedAttributes=" + supportedAttributes + "}";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 24-Oct-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 24-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
