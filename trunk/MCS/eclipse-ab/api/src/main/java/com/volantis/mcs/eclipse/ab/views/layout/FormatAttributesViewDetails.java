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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.common.AttributesDetails;
import com.volantis.mcs.eclipse.common.ControlType;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An implementation of AttributesDetails is required that:
 *
 * <ul>
 *
 * <li>Returns required list of attributes</li>
 *
 * <li>Returns (localized) combo choices for given attributes</li>
 *
 * <li>Maps attributes to required control type</li>
 *
 * <li>Maps attributes to required type (NB: Don't think this is needed
 * here)</li>
 *
 * </ul>
 *
 * <p>An instance of this implementation will be used to hold each section's
 * data. These instances will be populated when the XML file is parsed</p>
 */
public class FormatAttributesViewDetails implements AttributesDetails {

    /**
     * The prefix for resources used by this class.
     */
    private static final String RESOURCE_PREFIX =
            "FormatAttributesViewDetails."; //$NON-NLS-1$

    /**
     * Store the list of attribute names in a list. The order is important
     * and the values of other attributes may be extracted from the
     * attributesDetailsMap.
     */
    private List attributesDetails = new ArrayList();

    /**
     * Map an attribute name to an AttributesDetails object. Store the attribute
     * details for each attribute name. The order of attribute names is stored
     * in the attributeDetails List, and the details themselves are stored in
     * this map.
     */
    private Map attributeDetailsMap = new HashMap();

    /**
     * Cache of the attribute names (this is reset if attributes are added or
     * removed.
     */
    private String attributes[];

    /**
     * The cached presentable items map.
     */
    private Map presentableItemsMap;


    /**
     * Internal class used to store the attribute details. This object is placed
     * into a map where the key is the attribute name. It isn't necessary to
     * implement equals and hashCode since this map is never exposed and the
     * values are never iterated over.
     */
    private class AttributeDetails {
        private String type;
        private String attributeType;
        private String supplementary;

        /**
         * Store the list of selections.
         */
        private List selections;

        /**
         * Store a cache of the selections list. This cache is reset if the
         * selections list is modified.
         */
        private Object[] selectionsArray;

        /**
         * Create the attribute details object with the specified values.
         *
         * @param type          the type associated with this attribute.
         * @param attributeType the attribute the type.
         * @param supplementary the supplementary values associated with this
         *                      attribute.
         */
        public AttributeDetails(String type,
                                String attributeType,
                                String supplementary) {
            this.type = type;
            this.attributeType = attributeType;
            this.supplementary = supplementary;
        }

        /**
         * Add the selection value to the to the selections array.
         *
         * @param selection the selection to add to the list of selections.
         */
        public void addSelection(String selection) {
            if (selections == null) {
                selections = new ArrayList();
            }
            selections.add(selection);

            // Reset the selections cache if it is non-empty.
            if (selectionsArray != null) {
                selectionsArray = null;
            }
            // Reset the presentable items array too (this needs to be done
            // because the presentable items are 'coupled' to the selections
            // (the real values). If the selection array changes, then the
            // presentable items need to be updated too.
            if (presentableItemsMap != null) {
                presentableItemsMap = null;
            }
        }

        /**
         * Return an array of objects representing the selections. Note that the
         * array is lazily created from the selections List and stored for
         * subsequent calls to this method.
         *
         * @return the cached array of selections, or null if there aren't any.
         */
        public Object[] getSelection() {
            Object[] result = null;

            // Try the cached object array first.
            if (selectionsArray != null) {
                result = selectionsArray;
            } else if (selections != null && (selections.size() > 0)) {
                selectionsArray = selections.toArray();
                result = selectionsArray;
            }
            return result;
        }
    }

    /**
     * Extract the information from the element and store any relevant
     * information.
     */
    public FormatAttributesViewDetails() {
    }


    // javadoc inherited
    public ControlType getAttributeControlType(String attribute) {
        ControlType result = null;
        AttributeDetails details = findAttribute(attribute);
        if (details != null) {
            result = ControlType.getControlType(details.type);
        }
        return result;
    }

    /**
     * Helper method to locate the AttributeDetails object given the attribute
     * name.
     *
     * @param attribute the attribute name.
     * @return the found AttributeDetails object or null if not found.
     */
    private AttributeDetails findAttribute(String attribute) {
        AttributeDetails result =
                (AttributeDetails)attributeDetailsMap.get(attribute);
        return result;
    }

    // javadoc inherited
    public PresentableItem[] getAttributePresentableItems(String attribute) {
        PresentableItem items [] = null;

        // First try the cache.
        if (presentableItemsMap != null) {
            items = (PresentableItem[]) presentableItemsMap.get(attribute);
        }

        if (items == null) {
            Object[] realValues = getAttributeValueSelection(attribute);

            if (realValues != null) {
                String[] presentableValues = null;

                presentableValues = createPresentableValues(
                        attribute, realValues);

                if (presentableValues != null) {
                    if (realValues.length != presentableValues.length) {
                        throw new IllegalStateException("Number of realValues " + //$NON-NLS-1$
                                "is " + realValues.length + //$NON-NLS-1$
                                ". Number of presentableValues is" + //$NON-NLS-1$
                                presentableValues.length + "."); //$NON-NLS-1$
                    }

                    items = new PresentableItem[realValues.length];
                    for (int i = 0; i < realValues.length; i++) {
                        items[i] = new PresentableItem(realValues[i],
                                presentableValues[i]);
                    }

                    if (presentableItemsMap == null) {
                        presentableItemsMap = new HashMap();
                    }
                }
                presentableItemsMap.put(attribute, items);
            }
        }
        return items;
    }

    // javadoc inherited
    public String[] getAttributes() {
        if (attributes == null) {
            if (attributesDetails.size() > 0) {
                attributes = new String[attributesDetails.size()];
                attributes = (String[])attributesDetails.toArray(attributes);
            }
        }
        return attributes;
    }

    // javadoc inherited
    public String getAttributeType(String attribute) {
        String result = null;
        AttributeDetails details = findAttribute(attribute);
        if (details != null) {
            result = details.attributeType;
        }
        return result;
    }

    // javadoc inherited
    public Object[] getAttributeValueSelection(String attribute) {
        Object[] result = null;
        AttributeDetails details = findAttribute(attribute);
        if (details != null) {
            result = details.getSelection();
        }
        return result;
    }

    // javadoc inherited
    public String getPresentableValue(String attribute, String value) {
        String presentableValue = value;
        PresentableItem[] items = getAttributePresentableItems(attribute);

        if (items != null) {
            for (int i = 0; i < items.length && presentableValue == value; i++) {
                presentableValue = items[i].realValue.toString().equals(value) ?
                        items[i].presentableValue : value;
            }
        }
        return presentableValue;
    }

    // javadoc inherited
    public String getSupplementaryValue(String attribute) {
        String result = null;
        AttributeDetails details = findAttribute(attribute);
        if (details != null) {
            result = details.supplementary;
        }
        return result;
    }

    /**
     * Create the array of presentable items given the attribute and real values
     * (as an array).
     *
     * @param attribute  the attribute name.
     * @param realValues an array of real value objects.
     * @return an array of presentable Strings items that correspondss to the
     *         real items passed in.
     */
    protected String[] createPresentableValues(String attribute,
                                             Object[] realValues) {

        String presentableValues [] = new String[realValues.length];

        StringBuffer key = new StringBuffer();
        for (int i = 0; i < realValues.length; i++) {
            key.setLength(0);
            key.append(RESOURCE_PREFIX);
            key.append(attribute).append('.').append(realValues[i].toString());
            String presentableValue =
                    EclipseCommonMessages.getString(key.toString());
            presentableValues[i] = presentableValue;
        }

        return presentableValues;
    }

    /**
     * Store the attributes in a list of attributes. Add any attribute values to
     * a key/value map where the key is the attribute name and the value is an
     * object containing the type, attribute type and supplementary value.
     * <p/>
     * Note that the name and type are mandatory values.
     *
     * @param name          the name of the attribute.
     * @param type          the type of the attribuyte.
     * @param attributeType the additional attribute type (e.g. imageComponent)
     * @param supplementary the supplementary value (currently unused).
     * @throws IllegalArgumentException if the name or type is null.
     */
    public void addAttributes(String name,
                              String type,
                              String attributeType,
                              String supplementary)
            throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException("Name may not be null"); //$NON-NLS-1$
        }
        if (type == null) {
            throw new IllegalArgumentException("Type may not be null"); //$NON-NLS-1$
        }
        attributesDetails.add(name);
        attributeDetailsMap.put(name,
                new AttributeDetails(type, attributeType, supplementary));

        // Reset the cache.
        if (attributes != null) {
            attributes = null;
        }
    }

    /**
     * Associate a selection value to an attribute.
     *
     * @param attributeName the attribute to associate the selection value to.
     * @param selection     the selection to be associated with an attribute.
     * @throws IllegalArgumentException if the attribute name or selection value
     *                                  is null.
     * @throws IllegalStateException    if the attribute cannot be found.
     */
    public void associateSelectionValue(String attributeName, String selection)
            throws IllegalArgumentException, IllegalStateException {

        if (attributeName == null) {
            throw new IllegalArgumentException("Attribute name may not be null."); //$NON-NLS-1$
        }
        if (selection == null) {
            throw new IllegalArgumentException("Selection value may not be null."); //$NON-NLS-1$
        }
        AttributeDetails details = (AttributeDetails) attributeDetailsMap.get(
                attributeName);

        if (details != null) {
            details.addSelection(selection);
        } else {
            throw new IllegalStateException(
                    "Unable to locate current attribute: " + attributeName); //$NON-NLS-1$
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 13-Jan-04	2483/9	byron	VBM:2003121504 Fixed optimized array copy

 13-Jan-04	2483/7	byron	VBM:2003121504 Fixed optimized array copy

 13-Jan-04	2483/5	byron	VBM:2003121504 Updated comments and optimized array copy

 13-Jan-04	2483/3	byron	VBM:2003121504 Corrected javadoc and updated xml and xsd file (unique validation and removed namespace declaration) and test cases

 13-Jan-04	2483/1	byron	VBM:2003121504 Eclipse PM Layout Editor: Format Attributes View: XML Config

 ===========================================================================
*/
