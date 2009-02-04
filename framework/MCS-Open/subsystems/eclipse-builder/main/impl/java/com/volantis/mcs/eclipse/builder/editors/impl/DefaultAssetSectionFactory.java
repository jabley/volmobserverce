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
package com.volantis.mcs.eclipse.builder.editors.impl;

import com.volantis.mcs.eclipse.builder.editors.common.AssetSectionFactory;
import com.volantis.mcs.eclipse.builder.editors.common.AssetsSection;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.common.AssetAttributesSection;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

import java.util.Map;

/**
 * Asset section factory for use in standalone file-based projects.
 */
public class DefaultAssetSectionFactory extends AssetSectionFactory {
    // Javadoc inherited
    public AssetsSection createAssetsSection(Composite parent,
                                             EditorContext context) {
        return new AssetsSection(parent, SWT.NONE, context);
    }

    // Javadoc inherited
    public AssetAttributesSection createAssetAttributesSection(
            Composite parent, EditorContext context) {
        return new AssetAttributesSection(parent, SWT.NONE, context);
    }

    // Javadoc inherited
    public AssetAttributesSection createAssetAttributesSection(
            Composite parent, EditorContext context,
            PropertyDescriptor[] attributes, Map policyReferenceTypes,
            Map comboDescriptors) {
        return new AssetAttributesSection(parent, SWT.NONE, context, attributes,
                policyReferenceTypes, comboDescriptors);
    }
}
