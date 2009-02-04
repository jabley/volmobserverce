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

import com.volantis.mcs.objects.PropertyValueLookUp;
import com.volantis.synergetics.ObjectHelper;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The AttributesDetails for Policy objects such as components, themes,
 * layouts and their associated assets.
 *
 * This implementation of AttributesDetails uses PropertyValueLookUp
 * to discover the attributes required.
 */
public class PolicyAttributesDetails implements AttributesDetails {

    /**
     * The prefix for resources used by this class.
     */
    private static final String RESOURCE_PREFIX =
            "PolicyAttributesDetails.";

    /**
     * A list of properties that are always excluded.
     * @todo later qualify the EXLUDED array attributes on element name
     */
    private static final String[] EXCLUDED = {
        "assetGroupProject",
        "CSSEmulator",
        "cacheThisPolicy",
        "language",
        "localSrc",
        "name",
        "project",
        "readOnly",
        "repositoryName",
        "retainDuringRetry",
        "retryFailedRetrieval",
        "retryInterval",
        "retryMaxCount",
        "rule",
        "ruleCount",
        "rules",
        "styleEngine",
        "timeToLive",
    };

    /**
     * The EXCLUDED array as a List.
     */
    private static final List EXCLUDED_LIST = Arrays.asList(EXCLUDED);

    /**
     * The available attribute value exchange objects, keyed on ControlType.
     *
     * @associates AttributeValueExchange
     * @supplierCardinality *
     */
    private static final Map ATTRIBUTE_EXCHANGE = new HashMap();

    static {
        // Initialize the set of attribute value exchangers required. The
        // mapping from control type to exchanger should match any explicit
        // use of exchangers in the {@link
        // com.volantis.mcs.eclipse.ab.core.AttributesCompositeBuilder}
        ATTRIBUTE_EXCHANGE.put(ControlType.URI_FRAGMENT,
                               new URIFragmentExchange());
    }

    /**
     * The element whose attributes or child attributes are represented
     * by this PolicyAttributesDetails.
     */
    private final String element;

    /**
     * The attributes to filter in or out of these details.
     */
    private String[] filteredAttributes;

    /**
     * Flag indicating that filteredAttributes should be included thereby
     * excluding all others, or excluded thereby including all others.
     */
    private boolean include = false;

    /**
     * Flag indicating whether or not the element itself or the elements
     * immeadiate child elements should be used to form the attributes
     * details.
     */
    private final boolean useChildAttributes;

    /**
     * The child elements if the child attributes are used.
     */
    private List childElements;

    /**
     * Cache the attributes for this PolicyAttributesDetails so as not to
     * have to recreate them.
     */
    private String[] attributes;

    /**
     * A cache of PresentableItem [] previously returned from
     * getPresentableItems().
     */
    private Map presentableItemsMap;

    /**
     * Construct a new PolicyAttributesDetails for a given element.
     * @param element The name of the element whose attributes details
     * will be represented by this PolicyAttributesDetails.
     * @param useChildAttributes If false indicates that the attributes should
     * be derived from the element itself; if true indicates that the attributes
     * should be derived from the superset of the possible child elements of the
     * given element.
     */
    public PolicyAttributesDetails(String element,
                                   boolean useChildAttributes) {
        this(element, null, useChildAttributes);
    }

    /**
     * Construct a new PolicyAttributesDetails for a given element.
     * @param element The name of the element whose attributes details
     * will be represented by this PolicyAttributesDetails.
     * @param filter The Filter that allows attributes to be filtered in or
     * out of the attributes list. Can be null indicating no filtering.
     * @param useChildAttributes If false indicates that the attributes should
     * be derived from the element itself; if true indicates that the attributes
     * should be derived from the superset of the possible child elements of the
     * given element.
     */
    public PolicyAttributesDetails(String element, Filter filter,
                                   boolean useChildAttributes) {
        this.element = element;
        if (filter != null) {
            this.filteredAttributes = (String[]) filter.filter;
            this.include = filter.type == Filter.INCLUDE;
        }
        this.useChildAttributes = useChildAttributes;

    }

    // javadoc inherited
    public String[] getAttributes() {
        if (attributes == null) {
            attributes = createAttributes();
        }

        return attributes;
    }

    // javadoc inherited
    public Object[] getAttributeValueSelection(String attribute) {
        Object[] selection = null;
        if (useChildAttributes) {
            // We need to tryHow each of the child elements looking for
            // the property array until we find it.
            if (childElements == null) {
                getAttributes();
            }
            if (childElements != null) {
                for (int i = 0; i < childElements.size() && selection == null;
                     i++) {
                    String childElement = (String) childElements.get(i);
                    String property = getPropertyFromAttribute(childElement,
                            attribute);
                    selection = PropertyValueLookUp.
                            getExternalPropertyArray(childElement, property);
                }
            }
        } else {

            selection = PropertyValueLookUp.getExternalPropertyArray(element,
                    getPropertyFromAttribute(element, attribute));
        }

        return selection;
    }

    // javadoc inherited
    public PresentableItem[] getAttributePresentableItems(String attribute) {
        PresentableItem items [] = null;

        // First try the cache.
        if (presentableItemsMap != null) {
            items = (PresentableItem[]) presentableItemsMap.get(attribute);
        }

        if (items == null) {
            Object realValues [] = getAttributeValueSelection(attribute);

            if (realValues != null) {
                String presentableValues [] = null;

                presentableValues = createPresentableValues(element,
                        attribute, realValues);

                if (presentableValues != null) {
                    if (realValues.length != presentableValues.length) {
                        throw new IllegalStateException("Number of realValues " +
                                "is " + realValues.length +
                                ". Number of presentableValues is" +
                                presentableValues.length + ".");
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

    private String[] createPresentableValues(String element,
                                             String attribute,
                                             Object[] realValues) {
        String presentableValues [] = new String[realValues.length];

        for (int i = 0; i < realValues.length; i++) {
            StringBuffer key = new StringBuffer(RESOURCE_PREFIX);
            key.append(element).append('.').append(attribute).
                    append('.').append(realValues[i].toString());
            String presentableValue =
                    EclipseCommonMessages.getString(key.toString());
            presentableValues[i] = presentableValue;
        }

        return presentableValues;
    }

    // javadoc inherited.
    public String getPresentableValue(String attribute, String value) {
        String presentableValue = value;
        PresentableItem[] items = getAttributePresentableItems(attribute);

        if (items != null) {
            for (int i = 0;
                 (i < items.length) && (presentableValue == value);
                 i++) {
                presentableValue =
                        items[i].realValue.toString().equals(value) ?
                        items[i].presentableValue :
                        value;
            }
        } else {
            // Allow the value to be modified if the control type associated
            // with the attribute requires it
            AttributeValueExchange exchange =
                    (AttributeValueExchange) ATTRIBUTE_EXCHANGE.
                    get(getAttributeControlType(attribute));

            if (exchange != null) {
                presentableValue = exchange.toControlForm(value);
            }
        }

        return presentableValue;
    }

    // javadoc inherited
    public ControlType getAttributeControlType(String attribute) {
        String controlTypeName = null;

        if (useChildAttributes) {
            // We need to try each of the child elements looking for
            // the property array until we find it.
            if (childElements == null) {
                getAttributes();
            }
            if (childElements != null) {
                for (int i = 0; i < childElements.size() &&
                        controlTypeName == null;
                     i++) {
                    String childElement = (String) childElements.get(i);
                    controlTypeName = PropertyValueLookUp.
                            getControlType(childElement,
                                    getPropertyFromAttribute(childElement,
                                            attribute));
                }
            }
        } else {
            controlTypeName = PropertyValueLookUp.getControlType(element,
                    getPropertyFromAttribute(element, attribute));
        }

        ControlType type = null;
        if (controlTypeName != null) {
            type = ControlType.getControlType(controlTypeName);
        }

        return type;
    }

    /**
     * No attribute types are currently available from PolicyAttributeDetails.
     */
    // rest of javadoc inherited
    public String getAttributeType(String attribute) {
        return null;
    }

    /**
     * No attribute supplementary values are currently available from PolicyAttributeDetails.
     */
    // rest of javadoc inherited
    public String getSupplementaryValue(String attribute) {
        return null;
    }

    /**
     * Create the attributes for this AttributeDetails.
     */
    private String[] createAttributes() {
        Collection attributes = null;
        if (useChildAttributes) {
            childElements = PropertyValueLookUp.
                    getDependentElements(element);
            attributes = new TreeSet();
            for (int i = 0; (childElements != null) &&
                    (i < childElements.size()); i++) {
                String childElement = (String) childElements.get(i);
                attributes.addAll(getAttributes(childElement));
            }
        } else {
            attributes = getAttributes(element);
            Collections.sort((List) attributes);
        }
        filterAttributes(attributes, filteredAttributes, include);

        this.attributes = new String[attributes.size()];

        return (String[]) attributes.toArray(this.attributes);
    }

    /**
     * Helper method to filter the property names. The allAttributes collection
     * will be store the result of the filtering operation or remain unchanged
     * if no filtering is required.
     *
     * @param allAttributes         a collection of property names.
     * @param filterAttributes an array of property names that may be used for
     *                      filtering (inclusion or exclusion). If this is null
     *                      all properties will be displayed.
     * @param include       true if we want to include the properties specified
     *                      in the property names array only, false otherwise.
     *                      Ignored if filterAttributes is null.
     */
    private void filterAttributes(Collection allAttributes,
                                  String[] filterAttributes,
                                  boolean include) {
        allAttributes.removeAll(EXCLUDED_LIST);
        if (filterAttributes != null) {
            ArrayList list = new ArrayList(filterAttributes.length);
            for (int i = 0; i < filterAttributes.length; i++) {
                list.add(filterAttributes[i]);
            }
            if (include) {
                allAttributes.retainAll(list);
            } else {
                allAttributes.removeAll(list);
            }
        }
    }


    /**
     * Get the attributes available on an element that represents a Policy.
     * @param element The name of the element whose attributes to get.
     * @return the element attributes.
     */
    private List getAttributes(String element) {
        Class clazz = PropertyValueLookUp.getClassForXMLElement(element);

        if (clazz == null) {
            throw new IllegalArgumentException("No class available for " +
                    "element " + element);
        }

        PropertyDescriptor descriptors [] =
                ObjectHelper.getPropertyDescriptors(clazz);

        ArrayList propertiesList = new ArrayList(descriptors.length);

        for (int i = 0; i < descriptors.length; i++) {
            String name = descriptors[i].getName();
            propertiesList.add(name);
        }

        return createAttributesListFromProperties(element, propertiesList);
    }

    /**
     * Use the array of properties and PropertyValueLookUp to create
     * the attributes that correspond to the properties array.
     * @param element The element name for the attributes.
     */
    private List createAttributesListFromProperties(String element, List properties) {
        List attrList = new ArrayList();
        for (int i = 0; i < properties.size(); i++) {
            String attr = PropertyValueLookUp.getXMLAttributeName(element,
                    (String) properties.get(i));
            if (attr == null) {
                throw new IllegalStateException("Could not find attribute " +
                        " for property" + properties.get(i));
            }

            attrList.add(attr);
        }

        return attrList;
    }

    /**
     * Get the property name for a given attribute and element.
     * @param element The element.
     * @param attribute The attribute.
     * @return The policy property associated with the element/attribute.
     */
    private String getPropertyFromAttribute(String element, String attribute) {
        return PropertyValueLookUp.getPropertyName(element, attribute);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Apr-05	7914/3	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 16-Mar-05	7426/1	philws	VBM:2004121405 Port URI fragment asset value encoding and decoding from 3.3

 15-Mar-05	7374/1	philws	VBM:2004121405 Allow asset values to contain space characters via URI encoding

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Nov-04	6083/1	adrianj	VBM:2004102619 Provide style engine in DeviceTheme

 24-Sep-04	5627/1	adrianj	VBM:2004090812 Fix for device theme overview page

 25-Mar-04	3519/1	byron	VBM:2004020403 Save as for policy results in Error - ensure save as checks full path name

 03-Feb-04	2820/3	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 01-Feb-04	2821/1	mat	VBM:2004012701 Change tests and generate scripts for Projects

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 21-Jan-04	2659/1	allan	VBM:2003112801 RuleSection basics (read only)

 08-Jan-04	2496/1	allan	VBM:2004010806 Removed language from text assets in the gui.

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 17-Dec-03	2213/6	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 16-Dec-03	2213/4	allan	VBM:2003121401 More editors and fixes for presentable values.

 15-Dec-03	2208/7	allan	VBM:2003121201 Include ImageAssetDetails in this fix.

 14-Dec-03	2208/5	allan	VBM:2003121201 Move PresentableItem to eclipse.common

 13-Dec-03	2208/3	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/5	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 27-Nov-03	2013/4	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 ===========================================================================
*/
