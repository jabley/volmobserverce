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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Information about style properties relating to how they are edited in the
 * GUI.
 */
public final class StylePropertyMetadata {
    /**
     * A list of style properties that should be edited in the GUI as pairs,
     * with each part of the pair edited separately.
     */
    private static final List pairStyleProperties = new ArrayList();

    /**
     * A list of style properties that should be edited in the GUI as a fraction,
     * with each part edited separately.
     */
    private static final List fractionStyleProperties = new ArrayList();

    /**
     * A map associating style properties with the browse actions used to edit
     * them in the GUI.
     */
    private static final Map stylePropertyWizards = new HashMap();

    /**
     * A map associating style properties with one or more
     * {@link StyleValuePostProcessor} instances that should be applied after
     * parsing the property.
     */
    private static final Map stylePropertyPostProcessors = new HashMap();

    // Define the style properties to be edited as pairs
    static {
        pairStyleProperties.add(StylePropertyDetails.BACKGROUND_POSITION);
        pairStyleProperties.add(StylePropertyDetails.BORDER_SPACING);
        pairStyleProperties.add(StylePropertyDetails.MCS_MMFLASH_SCALED_ALIGN);
    }

    // Define the style properties to be edited as fractions.
    static {
        fractionStyleProperties.add(StylePropertyDetails.MCS_MARQUEE_SPEED);
    }

    // Define the browse actions for style properties
    static {
        stylePropertyWizards.put(StylePropertyDetails.BACKGROUND_IMAGE,
                new PolicySelectorBrowseAction(FileExtension.IMAGE_COMPONENT));
        stylePropertyWizards.put(StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL,
                new PolicySelectorBrowseAction(FileExtension.DYNVIS_COMPONENT));
        stylePropertyWizards.put(StylePropertyDetails.MCS_CHART_FOREGROUND_COLORS,
                new ColorListBrowseAction());
        stylePropertyWizards.put(StylePropertyDetails.FONT_FAMILY,
                new FontFamilyBrowseAction());
        stylePropertyWizards.put(StylePropertyDetails.MCS_FORM_ACTION_IMAGE,
                new PolicySelectorBrowseAction(FileExtension.IMAGE_COMPONENT));
        stylePropertyWizards.put(StylePropertyDetails.MCS_IMAGE,
                new PolicySelectorBrowseAction(FileExtension.IMAGE_COMPONENT));
        stylePropertyWizards.put(StylePropertyDetails.MCS_MENU_SEPARATOR_IMAGE,
                new PolicySelectorBrowseAction(FileExtension.IMAGE_COMPONENT));
        stylePropertyWizards.put(StylePropertyDetails.LIST_STYLE_IMAGE,
                new PolicySelectorBrowseAction(FileExtension.IMAGE_COMPONENT));
    }

    // Define the post-processors for style properties
    static {
        List processors = new ArrayList();
        processors.add(new StyleListPostProcessor(new StringValuePostProcessor(
                StylePropertyDetails.LIST_STYLE_IMAGE)));
        stylePropertyPostProcessors.put(StylePropertyDetails.FONT_FAMILY,
                processors);

        // Treat content as a special case: if it can't be parsed, try quoting
        // it even though it isn't a string per se.
        processors = new ArrayList();
        processors.add(
                new StringValuePostProcessor(StylePropertyDetails.CONTENT));
        stylePropertyPostProcessors.put(StylePropertyDetails.CONTENT,
                processors);

        // Add component URI and string post-processors to the appropriate
        // properties
        Iterator it = StylePropertyDetails.getDefinitions().stylePropertyIterator();
        while (it.hasNext()) {
            StyleProperty property = (StyleProperty) it.next();
            Set supportedTypes = property.getStandardDetails().getSupportedTypes();
            if (supportedTypes.contains(StyleValueType.COMPONENT_URI)) {
                addPostProcessor(property, new PolicyValuePostProcessor(property));
            }
            if (supportedTypes.contains(StyleValueType.STRING)) {
                addPostProcessor(property, new StringValuePostProcessor(property));
            }
        }
    }

    /**
     * Internal helper method for adding a post-processor for a specified
     * style property.
     *
     * <p>If there are already post-processors associated with the property,
     * the new one is added to the existing list. Otherwise a new list is
     * created.</p>
     *
     * @param property The property which will be post-processed
     * @param postProcessor The post-processor which will be applied
     */
    private static void addPostProcessor(StyleProperty property,
                                        StyleValuePostProcessor postProcessor) {
        List existing = (List) stylePropertyPostProcessors.get(property);
        if (existing == null) {
            existing = new ArrayList();
            stylePropertyPostProcessors.put(property, existing);
        }
        existing.add(postProcessor);
    }

    /**
     * Returns the post-processors (if any) for the specified property.
     *
     * @param property The property for which post-processors are required
     * @return A list of {@link StyleValuePostProcessor} instances, or null if
     *         none are registered against the specified property
     */
    public static List getPostProcessors(StyleProperty property) {
        return (List) stylePropertyPostProcessors.get(property);
    }

    /**
     * Checks whether the specified property should be edited as a pair in the
     * GUI.
     *
     * @param property The property to check
     * @return True if the specified property should be edited as a pair, false
     *         otherwise
     */
    public static boolean editAsPair(StyleProperty property) {
        return pairStyleProperties.contains(property);
    }

    /**
     * Checks whether the specified property should be edited as a fraction in
     * the GUI.
     *
     * @param property The property to check
     * @return True if the specified property should be edited as a fraction,
     * false otherwise
     */
    public static boolean editAsFraction(StyleProperty property) {
        return fractionStyleProperties.contains(property);
    }

    /**
     * Returns the {@link StylePropertyBrowseAction} associated with a property,
     * if any exists, which can be used to edit it within the GUI.
     *
     * @param property The property for which a browse action is required
     * @return The browse action associated with the specified property, or null
     *         if none exists
     */
    public static StylePropertyBrowseAction getBrowseAction(
            StyleProperty property) {
        return (StylePropertyBrowseAction) stylePropertyWizards.get(property);
    }

    /**
     * Returns the appropriate seperator for a
     * {@link com.volantis.mcs.themes.StyleList}.
     *
     * @param propertyName The property name of this list.
     * @return The seperator string.
     */
    public static String getListSeperator(String propertyName) {
        String result = " ";
        if (StylePropertyDetails
                .FONT_FAMILY.getName().equals(propertyName) ||
            StylePropertyDetails
                .MCS_CHART_FOREGROUND_COLORS.getName().equals(propertyName)) {
            result=",";
        }
        return result;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10610/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 06-Dec-05	10608/1	ianw	VBM:2005120206 Added browse actions for font-family and mcs-chart-forground-color

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 25-Nov-05	10455/1	adrianj	VBM:2005112310 Post-processing for content values

 09-Nov-05	10197/3	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 25-Nov-05	10459/1	adrianj	VBM:2005112310 Post processing for content style property

 25-Nov-05	10455/1	adrianj	VBM:2005112310 Post-processing for content values

 10-Nov-05	10246/1	adrianj	VBM:2005110434 Allow user-friendly data entry for style properties

 09-Nov-05	10197/3	adrianj	VBM:2005110434 Allow user-friendly entry of strings and component URIs

 01-Nov-05	9886/4	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New theme GUI

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
