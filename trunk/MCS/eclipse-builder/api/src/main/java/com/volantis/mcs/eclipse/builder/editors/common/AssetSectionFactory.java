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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.common.ClassVersionProperties;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import org.eclipse.swt.widgets.Composite;

import java.util.Map;

/**
 * Factory for asset editing sections.
 */
public abstract class AssetSectionFactory {
    /**
     * Default instance of the asset section factory.
     */
    private static final AssetSectionFactory DEFAULT_INSTANCE =
            (AssetSectionFactory) ClassVersionProperties.
                    getInstance("AssetSectionFactory.class");

    /**
     * Retrieve an instance of this asset section factory.
     *
     * @return an instance of this asset section factory
     */
    public static AssetSectionFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Create an assets section for the specified editor context in the
     * specified parent composite.
     *
     * @param parent The parent composite in which the UI components should be
     *               created
     * @param context The editor context for the assets being displayed
     * @return an AssetsSection instance
     */
    public abstract AssetsSection createAssetsSection(
            Composite parent, EditorContext context);

    /**
     * Create an asset attributes section for the specified editor context in
     * the specified parent composite.
     *
     * @param parent The parent composite in which the UI components should be
     *               created
     * @param context The editor context for the asset whose attributes are
     *                being displayed
     * @return an AssetAttributesSection instance
     */
    public abstract AssetAttributesSection createAssetAttributesSection(
            Composite parent, EditorContext context);

    /**
     * Create an asset attributes section for the specified editor context in
     * the specified parent composite. Also allows explicit specification of
     * property descriptors for the attributes and supporting data.
     *
     * @param parent The parent composite in which the UI components should be
     *               created
     * @param context The editor context for the asset whose attributes are
     *                being displayed
     * @param attributes Property descriptors for attributes
     * @param policyReferenceTypes a map associating (@link PropertyIdentity}s
     *                             to {@link PolicyType}s 
     * @param comboDescriptors a map associating {@link PropertyIdentity}s to
     *                         {@link ComboDescriptor}s.
     * @return an AssetAttributesSection instance
     */
    public abstract AssetAttributesSection createAssetAttributesSection(
            Composite composite, EditorContext context,
            PropertyDescriptor[] attributes, Map policyReferenceTypes,
            Map comboDescriptors);
}
