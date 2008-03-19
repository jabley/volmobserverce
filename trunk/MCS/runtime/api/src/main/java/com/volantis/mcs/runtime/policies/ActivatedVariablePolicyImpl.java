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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.image.GenericImageSelection;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.InternalSingleEncoding;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DefaultSelection;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.EncodingSelection;
import com.volantis.mcs.policies.variants.selection.Selection;
import com.volantis.mcs.policies.variants.selection.TargetedSelection;
import com.volantis.mcs.runtime.RuntimeProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link ActivatedVariablePolicy}.
 */
public class ActivatedVariablePolicyImpl
        extends ActivatedConcretePolicyImpl
        implements ActivatedVariablePolicy {

    /**
     * The underlying unactivated policy.
     */
    private final VariablePolicy delegate;

    /**
     * The map from device name as a {@link String} to a {@link Variant}.
     */
    private final Map device2Variant;

    /**
     * The map from category name as a {@link String} to a {@link Variant}.
     */
    private final Map category2Variant;

    /**
     * The default variant.
     */
    private final Variant defaultVariant;

    /**
     * The map from encoding as a {@link Encoding} to a {@link Variant}.
     */
    private final Map encoding2Variant;

    /**
     * A list of {@link Variant}s with a {@link GenericImageSelection}
     * criteria.
     */
    private final List genericImages;

    /**
     * A map from a selection specific key to the selected object.
     *
     * <p>Access to this map must be synchronized on the containing object.</p>
     */
    private final Map selected;

    /**
     * Initialise.
     *
     * @param delegate       The underlying unactivated policy.
     * @param actualProject  The actual project containing the policy.
     * @param logicalProject The logical project containing the policy.
     */
    public ActivatedVariablePolicyImpl(
            VariablePolicy delegate, RuntimeProject actualProject,
            RuntimeProject logicalProject) {

        super(actualProject, logicalProject);
        this.delegate = delegate;

        Iterator variants = delegate.variantIterator();

        Map device2Variant = null;
        Map category2Variant = null;
        Map encoding2Variant = null;
        Variant defaultVariant = null;
        List genericImages = null;

        boolean hasCategories = delegate.getCategorizationScheme() != null;

        // Iterate over all the variants populating selection specific data
        // structures.
        while (variants.hasNext()) {
            Variant variant = (Variant) variants.next();
            Selection selection = variant.getSelection();
            if (selection instanceof TargetedSelection) {
                TargetedSelection targetedSelection =
                        (TargetedSelection) selection;

                // Only generate categories if they are supported by this
                // policy.
                if (hasCategories) {
                    List categories = targetedSelection.getCategoryReferences();
                    if (categories != null && !categories.isEmpty()) {
                        category2Variant =
                                targetVariantAtCategories(variant, categories,
                                        category2Variant);
                    }
                }

                List devices = targetedSelection.getDeviceReferences();
                if (devices != null && !devices.isEmpty()) {
                    device2Variant = targetVariantAtDevices(variant, devices,
                            device2Variant);
                }
            } else if (selection instanceof DefaultSelection) {
                defaultVariant = variant;
            } else if (selection instanceof EncodingSelection) {
                InternalSingleEncoding encoding = (InternalSingleEncoding)
                        variant.getMetaData();
                if (encoding2Variant == null) {
                    encoding2Variant = new HashMap();
                }
                encoding2Variant.put(encoding.getEncoding(), variant);
            } else if (selection instanceof GenericImageSelection) {
                if (genericImages == null) {
                    genericImages = new ArrayList();
                }
                genericImages.add(variant);
            }
        }

        this.device2Variant = getNonNullMap(device2Variant);
        this.category2Variant = getNonNullMap(category2Variant);
        this.defaultVariant = defaultVariant;
        this.encoding2Variant = getNonNullMap(encoding2Variant);
        this.genericImages = getUnmodifiableListOrNull(genericImages);

        this.selected = new HashMap();
    }

    /**
     * Create an unmodifiable list around the specified list.
     *
     * @param list The list to wrap, may be null.
     * @return The unmodifiable list, or null if the input list was null.
     */
    private List getUnmodifiableListOrNull(List list) {
        if (list == null) {
            return null;
        } else {
            return Collections.unmodifiableList(list);
        }
    }

    /**
     * Get a map.
     *
     * @param map The map, may be null.
     * @return The specified map, or an empty map if the input was null.
     */
    private Map getNonNullMap(Map map) {
        if (map == null) {
            map = Collections.EMPTY_MAP;
        }
        return map;
    }

    // Javadoc inherited.
    public Variant getDeviceTargetedVariant(String deviceName) {
        return (Variant) device2Variant.get(deviceName);
    }

    // Javadoc inherited.
    public Variant getCategoryTargetedVariant(String categoryName) {
        return (Variant) category2Variant.get(categoryName);
    }

    /**
     * Add a mapping from the device names to the supplied variant.
     *
     * @param variant        The variant.
     * @param devices        The list of device names as {@link String}.
     * @param device2Variant The map to update, or null if it has not yet been
     *                       created.
     * @return The supplied map, or a newly instantiated map if the supplied map
     *         was null and an entry had to be added.
     */
    private Map targetVariantAtDevices(
            Variant variant, List devices, Map device2Variant) {
        for (int j = 0; j < devices.size(); j++) {
            DeviceReference reference = (DeviceReference) devices.get(j);
            String deviceName = reference.getDeviceName();
            if (device2Variant == null) {
                device2Variant = new HashMap();
            }
            device2Variant.put(deviceName, variant);
        }
        return device2Variant;
    }

    /**
     * Add a mapping from the category names to the supplied variant.
     *
     * @param variant          The variant.
     * @param categories       The list of category names as {@link String}.
     * @param category2Variant The map to update, or null if it has not yet been
     *                         created.
     * @return The supplied map, or a newly instantiated map if the supplied map
     *         was null and an entry had to be added.
     */
    private Map targetVariantAtCategories(
            Variant variant, List categories, Map category2Variant) {
        for (int j = 0; j < categories.size(); j++) {
            CategoryReference reference = (CategoryReference) categories.get(j);
            String categoryName = reference.getCategoryName();
            if (category2Variant == null) {
                category2Variant = new HashMap();
            }
            category2Variant.put(categoryName, variant);
        }
        return category2Variant;
    }

    // Javadoc inherited.
    public Variant getDefaultVariant() {
        return defaultVariant;
    }

    // Javadoc inherited.
    public Variant getVariantWithEncoding(Encoding encoding) {
        return (Variant) encoding2Variant.get(encoding);
    }

    // Javadoc inherited.
    public List getGenericImages() {
        return genericImages;
    }

    // Javadoc inherited.
    public SelectedVariant getSelected(Object key) {
        return (SelectedVariant) selected.get(key);
    }

    // Javadoc inherited.
    public void putSelected(Object key, SelectedVariant object) {
        selected.put(key, object);
    }

    // Javadoc inherited.
    protected ConcretePolicy getConcretePolicy() {
        return delegate;
    }

    // Javadoc inherited.
    public VariablePolicyBuilder getVariablePolicyBuilder() {
        return delegate.getVariablePolicyBuilder();
    }

    // Javadoc inherited.
    public VariablePolicyType getVariablePolicyType() {
        return delegate.getVariablePolicyType();
    }

    // Javadoc inherited.
    public String getCategorizationScheme() {
        return delegate.getCategorizationScheme();
    }

    // Javadoc inherited.
    public List getVariants() {
        return delegate.getVariants();
    }

    // Javadoc inherited.
    public Iterator variantIterator() {
        return delegate.variantIterator();
    }
}
