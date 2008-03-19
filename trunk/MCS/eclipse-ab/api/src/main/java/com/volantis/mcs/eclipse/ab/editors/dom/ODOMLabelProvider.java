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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.common.AttributesDetails;
import com.volantis.mcs.eclipse.common.AttributesMessageFormatter;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An ILabelProvider for ODOMObservable objects.
 *
 * This label provider can be configured to show elements and attributes in
 * the same label, just attributes of an element in the label or just the
 * ODOMObservable (e.g. just the element or just the attribute).
 *
 * NOTE: This label provide will not provide images or text for
 * the schemaLocation attributes. Nor will it provide text for elements
 * whose name is ODOMElement.UNDEFINED_ELEMENT.
 */
public class ODOMLabelProvider extends LabelProvider {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ODOMLabelProvider.class);
    

    /**
     * Prefix for message resource keys for this class.
     */
    private static final String RESOURCE_PREFIX = "ODOMLabelProvider."; //$NON-NLS-1$

    /**
     * The name of the schemaLocation attribute.
     */
    private static final String SCHEMA_LOCATION = "schemaLocation"; //$NON-NLS-1$



    /**
     * The configuration for this ODOMObservableLabelProvider.
     */
    private final ODOMLabelProviderConfiguration config;

    /**
     * The image associated with element ODOMObservables.
     */
    private final Image elementImage;

    /**
     * The image associated with attribute ODOMObservables.
     */
    private final Image attributeImage;

    /**
     * The unformatted summary message.
     */
    private Map unformattedSummaries = new HashMap();

    /**
     * The ODOMLabelFormatProvider associated with this
     * ODOMLabelProviderConfiguration.
     */
    private final ODOMLabelFormatProvider labelFormatProvider;

    /**
     * A cache of images that have been created by this ODOMLabelProvider.
     */
    private Map imageCache;

    /**
     * The AttributesMessageFormatter associated with this ODOMLabelProvider.
     */
    private final AttributesMessageFormatter attributesMessageFormatter;



    /**
     * Construct a new ODOMObservableLabelProvider.
     * @param config The ODOMLabelProviderConfiguration for this
     * ODOMObservableLabelProvider. Must not be null.
     * @param elementImage The Image associated with ODOMElement type
     * ODOMObservables. Can be null and if it is, ODOMLabelProvider will
     * attempt to find the image for elements using the element name
     * in combination with the root name of the element looking in
     * the EclipseCommonMessages resources e.g. "layoutFormat.gridFormat".
     * If an image still cannot be found but is required (as specified by
     * the ODOMLabelProviderConfiguration) then an Exception will be
     * occur when an attempt is made to acquire the image.
     * @param attributeImage The Image associated with ODOMAttribute type
     * ODOMObservables. Can be null.
     * @param attributesDetails The AttributesDetails to use for query
     * information about attributes. Can be null.
     * @throws IllegalArgumentException If config is null.
     */
    public ODOMLabelProvider(ODOMLabelProviderConfiguration config,
                             Image elementImage,
                             Image attributeImage,
                             AttributesDetails attributesDetails) {
        this(config, elementImage, attributeImage, attributesDetails, null);
    }


    /**
     * Construct a new ODOMObservableLabelProvider.
     * @param config The ODOMLabelProviderConfiguration for this
     * ODOMObservableLabelProvider. Must not be null.
     * @param elementImage The Image associated with ODOMElement type
     * ODOMObservables. Can be null and if it is, ODOMLabelProvider will
     * attempt to find the image for elements using the element name
     * in combination with the root name of the element looking in
     * the EclipseCommonMessages resources e.g. "layoutFormat.gridFormat".
     * If an image still cannot be found but is required (as specified by
     * the ODOMLabelProviderConfiguration) then an Exception will be
     * occur when an attempt is made to acquire the image.
     * @param attributeImage The Image associated with ODOMAttribute type
     * ODOMObservables. Can be null.
     * @param attributesDetails The AttributesDetails to use for query
     * information about attributes. Can be null.
     * @param labelFormatProvider The ODOMLabelFormatProvider that will be
     * consulted for label formats. Can be null in which case a default
     * will be used which will require specific resources for
     * ODOMLabelProvider in the EditorMessages.properties file.
     * @throws IllegalArgumentException If config is null.
     */
    public ODOMLabelProvider(ODOMLabelProviderConfiguration config,
                             Image elementImage,
                             Image attributeImage,
                             AttributesDetails attributesDetails,
                             ODOMLabelFormatProvider labelFormatProvider) {
        if (config == null) {
            throw new IllegalArgumentException("Cannot be null: config"); //$NON-NLS-1$
        }
        this.config = config;
        this.elementImage = elementImage;
        this.attributeImage = attributeImage;
        this.attributesMessageFormatter =
                new AttributesMessageFormatter(attributesDetails);
        this.labelFormatProvider = labelFormatProvider;
    }

    /**
     * Get the image associated with a given object. The object is
     * expected to be an ODOMObservable that is either an ODOMElement or
     * an ODOMAttribute.
     * @param o The Object. Cannot be null.
     * @return The elementImage if o is an ODOMElement or the attributeImage
     * if o is an ODOMAttribute.
     * @throws IllegalArgumentException If o is null, o is not an
     * ODOMObservable or o is not an ODOMElement or an ODOMAttribute.
     */
    public Image getImage(Object o) {
        checkObject(o);
        Image image = null;
        if (o instanceof ODOMElement) {
            if (elementImage != null) {
                image = elementImage;
            } else {
                image = findImageForElement((ODOMElement) o);
            }
        } else if (o instanceof ODOMAttribute &&
                !((ODOMAttribute) o).getName().equals(SCHEMA_LOCATION)) {
            image = attributeImage;
        }

        return image;
    }

    /**
     * Depending on the config for this ODOMObservableLabelProvider return
     * the label appropriate for a given object. The object is expected to be
     * an ODOMObservable that is an ODOMElement or an ODOMAttribute.
     * @param o The object.
     * @return The label for the object.
     * @throws IllegalArgumentException If o is null, o is not an
     * ODOMObservable or o is not an ODOMElement or an ODOMAttribute.
     */
    public String getText(Object o) {
        checkObject(o);

        String label;
        ODOMObservable oo = (ODOMObservable) o;
        if (config == ODOMLabelProviderConfiguration.JUST_OBSERVABLE) {
            label = getLocalizedName(oo);
        } else {
            label = getAttributesLabel(oo, config);
            if (logger.isDebugEnabled()) {
                logger.debug("Label is: " + label); //$NON-NLS-1$
            }
        }

        return label;
    }

    /**
     * Override dispose() to dispose of any images created by this
     * ODOMLabelProvider. Callers must call this method when this
     * ODOMLabelProvider is no longer required.
     */
    // rest of javadoc inherited
    public void dispose() {
        super.dispose();
        if (imageCache != null) {
            Collection images = imageCache.values();
            Iterator iterator = images.iterator();
            while (iterator.hasNext()) {
                Image image = (Image) iterator.next();
                if (!image.isDisposed()) {
                    image.dispose();
                }
            }
            imageCache.clear();
        }
    }

    /**
     * Request an image from EclipseCommonMessages based on the name
     * of an element and its parent element name. If the element is the
     * root element then it alone is used to discover the image.
     * @param element The element whose image to locate.
     */
    private Image findImageForElement(Element element) {
        String rootName = getRootElement(element).getName();
        String imageKey = rootName;
        if (!rootName.equals(element.getName())) {
            StringBuffer buffer = new StringBuffer(imageKey);
            buffer.append('.').append(element.getName());
            imageKey = buffer.toString();
        }

        Image image = null;
        if (imageCache != null) {
            image = (Image) imageCache.get(imageKey);
        }

        if (image == null) {
            image = EclipseCommonMessages.getPolicyIcon(imageKey);

            if (image != null) {
                if (imageCache == null) {
                    imageCache = new HashMap();
                }
                imageCache.put(imageKey, image);
            }
        }

        return image;
    }

    /**
     * Climb the element hierarchy of an element until the root element
     * is reached and return it.
     * @param element The element from which to discover the root element.
     * @return The root element (which will be element if it is the root).
     */
    private Element getRootElement(Element element) {
        Element root = element;
        Element parent = element.getParent();
        while (parent != null) {
            root = parent;
            parent = parent.getParent();
        }

        return root;
    }

    /**
     * Get the localized name for an ODOMObservable that is either an
     * ODOMElement or an ODOMAttribute.
     * @param oo The ODOMObservable.
     * @return The localized name of the ODOMObservable.
     */
    private String getLocalizedName(ODOMObservable oo) {
        String label;

        Element element = oo instanceof ODOMAttribute ?
                oo.getParent() : (Element) oo;
        String elementName = element.getName();

        if (oo instanceof ODOMElement &&
                ODOMElement.UNDEFINED_ELEMENT_NAME.equals(elementName)) {
            // Elements that are undefined are rendered as an empty string.
            label = ""; //$NON-NLS-1$
        } else {
            String attributeName = null;
            if (oo instanceof Attribute) {
                attributeName = oo.getName();
            }
            // This ODOMLabelProvider does not provide a label for the
            // schemaLocation attribute that may be present on root elements.
            if (attributeName != null &&
                    attributeName.equals(SCHEMA_LOCATION)) {
                label = ""; //$NON-NLS-1$
            } else {
                label = EclipseCommonMessages.
                        getLocalizedPolicyName(elementName, attributeName);
            }
        }

        return label;
    }

    /**
     * Make a label out of the attributes of an ODOMElement.
     * @param oo The ODOMObservable that is expected to be an
     * ODOMElement.
     * @return A label consisting of the localized names of all the attributes
     * in oo separated by commas; or an empty String if there are no
     * attributes.
     * @throws IllegalArgumentException If oo is not an ODOMObservable.
     * @throws IllegalStateException If a message format for the attributes
     * label cannot be found.
     */
    private String getAttributesLabel(ODOMObservable oo,
                                      ODOMLabelProviderConfiguration config) {
        if (!(oo instanceof ODOMElement)) {
            throw new IllegalArgumentException("Tried to create a label " + //$NON-NLS-1$
                    "containing ODOMObservable attributes when the " + //$NON-NLS-1$
                    "ODOMObservable is a: " + //$NON-NLS-1$
                    oo.getClass().getName());
        }

        ODOMElement element = (ODOMElement) oo;
        String elementName = null;
        if (config == ODOMLabelProviderConfiguration.ELEMENT_AND_ATTRIBUTES &&
                !element.getName().equals(ODOMElement.UNDEFINED_ELEMENT_NAME)) {
            elementName = EclipseCommonMessages.
                    getLocalizedPolicyName(oo.getName());
        }

        Element parent = element.getParent();
        if (parent == null) {
            // We already have the root element that we need for the
            // message key.
            parent = element;
        }

        String unformattedSummary =
                (String) unformattedSummaries.get(element.getName());

        if (unformattedSummary == null) {
            if (labelFormatProvider != null) {
                unformattedSummary =
                        labelFormatProvider.provideLabelFormat(element);

            }

            // If the unfomattedSummary is still null then use the default.
            if (unformattedSummary == null) {
                unformattedSummary = EditorMessages.getString(RESOURCE_PREFIX +
                        parent.getName() + ".summary"); //$NON-NLS-1$
            }
            if (unformattedSummary == null) {
                throw new
                        IllegalStateException("Could not find unformatted summary."); //$NON-NLS-1$
            } else {
                unformattedSummaries.put(element.getName(), unformattedSummary);
            }
        }

        List attributes = element.getAttributes();
        if (logger.isDebugEnabled()) {
            logger.debug("Element was: " + oo.getName()); //$NON-NLS-1$
            logger.debug("Element attributes were: " + attributes); //$NON-NLS-1$
        }

        // There may be no attributes
        String attributesLabel = ""; //$NON-NLS-1$
        if (attributes.size() > 0 || elementName != null) {
            attributesLabel = attributesMessageFormatter.format(attributes,
                    unformattedSummary, elementName);
        }

        return attributesLabel;
    }

    /**
     * Check the given object to ensure that it meets some basic requirements:
     * not null, is an ODOMObservable, is an ODOMElement or an ODOMAttribute.
     * @param o The Object.
     * @throws IllegalArgumentException If o is null, o is not an
     * ODOMObservable or o is not an ODOMElement or an ODOMAttribute.
     */
    protected void checkObject(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("Cannot be null: o"); //$NON-NLS-1$
        }
        if (!(o instanceof ODOMObservable)) {
            throw new IllegalArgumentException("Expected o to be an " + //$NON-NLS-1$
                    "ODOMObservable but was: " + o.getClass().getName()); //$NON-NLS-1$
        }
        if (!(o instanceof ODOMElement) && !(o instanceof ODOMAttribute)) {
            throw new IllegalArgumentException("Unsupported ODOMObservable: " + //$NON-NLS-1$
                    o.getClass().getName());
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 07-May-04	4195/1	tom	VBM:2004041902 Made .java files use the relevant resource bundles

 23-Mar-04	3389/3	byron	VBM:2004030905 NLV properties files need adding to build - missed some comments and exception handling

 23-Mar-04	3389/1	byron	VBM:2004030905 NLV properties files need adding to build

 24-Feb-04	3021/5	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 20-Feb-04	3021/3	pcameron	VBM:2004020211 Undid addition of layoutFormat key to some resources

 19-Feb-04	3021/1	pcameron	VBM:2004020211 Committed for integration

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 21-Jan-04	2659/2	allan	VBM:2003112801 RuleSection basics (read only)

 19-Jan-04	2562/4	allan	VBM:2003112010 Fix imports and javadoc.

 18-Jan-04	2562/2	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/7	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
