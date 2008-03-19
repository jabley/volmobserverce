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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;

import java.util.ResourceBundle;
import java.util.Map;
import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Arrays;
import java.text.Collator;

import org.eclipse.swt.graphics.Image;

/**
 * The class for resource messages associated with device related editors.
 */
public class DevicesMessages {

    /**
     * The bundle name for DevicesMessages.
     */
    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.ab.editors.devices.DevicesMessages"; //$NON-NLS-1$

    /**
     * The prefix for resources used by this class.
     */
    private static final String RESOURCE_PREFIX =
            "DevicesMessages."; //$NON-NLS-1$

    /**
     * The ResourceBundle for DevicesMessages.
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * A Map keyed on PolicyTypeComposition which returns an array of
     * PresentableItems representing the localized names of PolicyTypes.
     */
    private static final Map COMPOSITION_TO_POLICY_TYPES;

    /**
     * A map of localized composition names to PolicyTypeComposition, keyed on
     * the localized name. This allows the map to be a TreeMap, which is
     * automatically sorted by key i.e. sorted by localized name.
     */
    private static final Map LOCALIZED_COMPOSITIONS;

   // Initialise the composition maps and arrays.
    static {
        // The comparator to use for sorting localized names according to the
        // rules of the default locale. This is used by the TreeMap to
        // automatically sort the compositions by the localized name key.
        final Comparator KEY_COMPARATOR = new Comparator() {
            final Collator collator = Collator.getInstance();

            public int compare(Object o1, Object o2) {
                return collator.compare((String) o1, (String) o2);
            }
        };

        // The comparator to use for sorting PresentableItems by their
        // presentable values, according to the rules of the default locale.
        final Comparator PI_COMPARATOR = new Comparator() {
            final Collator collator = Collator.getInstance();

            public int compare(Object o1, Object o2) {
                PresentableItem pi1 = (PresentableItem) o1;
                PresentableItem pi2 = (PresentableItem) o2;
                return collator.compare(pi1.presentableValue,
                        pi2.presentableValue);
            }
        };

        // Get all compositions.
        PolicyTypeComposition[] compositions =
                PolicyTypeComposition.getPolicyTypeCompositions();

        // Create the maps for storage. The TreeMap automatically sorts
        // the localized compositions by the localized name.
        LOCALIZED_COMPOSITIONS = new TreeMap(KEY_COMPARATOR);
        COMPOSITION_TO_POLICY_TYPES = new HashMap(compositions.length);

        for (int i = 0; i < compositions.length; i++) {
            PolicyTypeComposition ptc = compositions[i];
            String localizedCompositionName =
                        getString(RESOURCE_PREFIX + "policyTypeComposition." +
                                  ptc.name + ".name");
            // Sorted by localized name.
            LOCALIZED_COMPOSITIONS.put(localizedCompositionName, ptc);

            PolicyType[] policyTypes = ptc.getSupportedPolicyTypes();
            PresentableItem[] items = new PresentableItem[policyTypes.length];

            for (int j = 0; j < policyTypes.length; j++) {
                PolicyType pt = policyTypes[j];
                String localizedTypeName = getString(RESOURCE_PREFIX +
                                                     "policyType." +
                                                     pt.getName() + ".name");
                items[j] = new PresentableItem(pt, localizedTypeName);
            }
            Arrays.sort(items, PI_COMPARATOR);
            COMPOSITION_TO_POLICY_TYPES.put(ptc, items);
        }
    }

    private DevicesMessages() {
    }

    /**
     * Get the localized message for a given key.
     * @param key The message key.
     * @return The localized message derived from the key and the
     * property bundle.
     */
    public static String getString(String key) {
        return EclipseCommonMessages.getString(RESOURCE_BUNDLE, key);
    }

    /**
     * The EditorMessages resource bundle.
     * @return The EditorMessages resource bundle.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }

    /**
     * Get a keyed property message as an Integer. This method delegates
     * to EclipseCommonMessages.getInteger().
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static Integer getInteger(String key) {
        return EclipseCommonMessages.getInteger(getResourceBundle(), key);
    }

    /**
     * Get a keyed property message as an Image where the property message
     * is the name of an image file relative to this Class.
     * @param key The message key.
     * @return The Image derived from the keyed message.
     */
    public static Image getImage(String key) {
        String imageFile = getString(key);
        return EclipseCommonMessages.getImage(DevicesMessages.class,
                imageFile);
    }

    /**
     * Returns the localized name for a given PolicyTypeComposition instance
     * @param composition the PolicyTypeComposition whose localized name is
     * required. Cannot be null;
     * @return the localized name for the PolicyTypeComposition
     * @throws IllegalArgumentException if the composition argument is null.
     */
    public static String getLocalizedCompositionName(
                PolicyTypeComposition composition) {
        if (composition == null) {
            throw new IllegalArgumentException("composition cannot be null");
        }
        String localizedName = null;
        // iterate of the Map entries looking for an entry whose value is the
        // composition passed in
        for (Iterator i=LOCALIZED_COMPOSITIONS.entrySet().iterator();
             i.hasNext() && localizedName == null;) {
            Map.Entry entry = (Map.Entry) i.next();
            if (entry.getValue() == composition) {
                // localized name is the key.
                localizedName = (String) entry.getKey();
            }
        }
        return localizedName;
    }

    /**
     * Gets an array of the localized policy type names for the given
     * composition. The array is sorted by the localized name, according to
     * the collation sequence of the default locale.
     * @param ptc the PolicyTypeComposition. Cannot be null.
     * @return an array of PresentableItems whose real values are the
     * PolicyTypes, and whose presentable values are the localized policy type
     * names
     * @throws IllegalArgumentException if ptc is null
     */
    public static PresentableItem[]
            getLocalizedPolicyTypes(PolicyTypeComposition ptc) {
        if (ptc == null) {
            throw new IllegalArgumentException("Cannot be null: ptc");
        }
        PresentableItem[] items =
                (PresentableItem[]) COMPOSITION_TO_POLICY_TYPES.get(ptc);
        PresentableItem[] copiedItems = new PresentableItem[items.length];

        for (int i = 0; i < items.length; i++) {
            PresentableItem copy = new PresentableItem(items[i].realValue,
                    items[i].presentableValue);
            copiedItems[i] = copy;
        }

        return copiedItems;
    }

    /**
     * Get an array of the localized policy type composition names. The array
     * is sorted by the localized name, according to the collation sequence of
     * the default locale.
     * @return an array of PresentableItems whose real values are the
     * PolicyTypeCompositions, and whose presentable values are the localized
     * policy type composition names
     */
    public static PresentableItem[] getLocalizedCompositions() {
        PresentableItem[] items =
                new PresentableItem[LOCALIZED_COMPOSITIONS.size()];
        Iterator it = LOCALIZED_COMPOSITIONS.keySet().iterator();

        int count = 0;
        while (it.hasNext()) {
            String localizedName = (String) it.next();
            PolicyTypeComposition ptc = (PolicyTypeComposition)
                    LOCALIZED_COMPOSITIONS.get(localizedName);
            PresentableItem item = new PresentableItem(ptc, localizedName);
            items[count++] = item;
        }

        return items;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4161/1	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 01-Apr-04	3602/3	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 01-Apr-04	3602/1	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 ===========================================================================
*/
