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
package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.eclipse.builder.editors.common.ComboDescriptor;
import com.volantis.mcs.eclipse.builder.editors.common.EncodingLabelProvider;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Editor for image policies.
 */
public class ImagePolicyEditor extends VariablePolicyEditor {
    // Javadoc inherited
    protected Map getComboDescriptors() {
        Map comboDescriptors = new HashMap();

        // encoding
        List encodingList = new ArrayList();
        EncodingCollection collection = ImageEncoding.COLLECTION;
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            encodingList.add(it.next());
        }

        ILabelProvider labelProvider = new EncodingLabelProvider();

        ComboDescriptor encodingDescriptor = new ComboDescriptor(encodingList,
                labelProvider);

        comboDescriptors.put(PolicyModel.ENCODING, encodingDescriptor);

        // rendering
        List renderingList = new ArrayList(2);
        renderingList.add(ImageRendering.COLOR);
        renderingList.add(ImageRendering.GRAYSCALE);

        ILabelProvider renderingLabelProvider = new LabelProvider() {
            public String getText(Object o) {
                String renderingType = "unknown";
                if (o == ImageRendering.COLOR) {
                    renderingType = "color";
                } else if (o == ImageRendering.GRAYSCALE) {
                    renderingType = "grayscale";
                }
                return EditorMessages.getString(
                        "ImageRendering." + renderingType + ".label");
            }
        };

        ComboDescriptor renderingDescriptor = new ComboDescriptor(renderingList,
                renderingLabelProvider);

        comboDescriptors.put(PolicyModel.RENDERING, renderingDescriptor);

        // conversionMode
        List conversionmodeList = new ArrayList(2);
        conversionmodeList.add(ImageConversionMode.ALWAYS_CONVERT);
        conversionmodeList.add(ImageConversionMode.NEVER_CONVERT);

        ILabelProvider conversionmodeLabelProvider = new LabelProvider() {
            public String getText(Object o) {
                String conversionModeType = "unknown";
                if (o == ImageConversionMode.ALWAYS_CONVERT) {
                    conversionModeType = "always";
                } else if (o == ImageConversionMode.NEVER_CONVERT) {
                    conversionModeType = "never";
                }
                return EditorMessages.getString(
                        "ImageConversionMode." + conversionModeType + ".label");
            }
        };

        ComboDescriptor conversionModeDescriptor = new ComboDescriptor(
                conversionmodeList, conversionmodeLabelProvider);

        comboDescriptors.put(PolicyModel.CONVERSION_MODE,
                conversionModeDescriptor);

        return comboDescriptors;
    }

    protected PolicyType getPolicyType() {
        return VariablePolicyType.IMAGE;
    }

    protected VariantType getDefaultVariantType() {
        return VariantType.IMAGE;
    }
}
