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
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import org.eclipse.jface.viewers.ILabelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

/**
 * Editor for audio policies.
 */
public class AudioPolicyEditor extends VariablePolicyEditor {
    // Javadoc inherited
    protected Map getComboDescriptors() {
        Map comboDescriptors = new HashMap();

        List encodingList = new ArrayList();
        EncodingCollection collection = AudioEncoding.COLLECTION;
        Iterator it = collection.iterator();
        while (it.hasNext()) {
            encodingList.add(it.next());
        }

        ILabelProvider labelProvider = new EncodingLabelProvider();

        ComboDescriptor encodingDescriptor = new ComboDescriptor(encodingList, labelProvider);

        comboDescriptors.put(PolicyModel.ENCODING, encodingDescriptor);

        return comboDescriptors;
    }

    // Javadoc inherited
    protected PolicyType getPolicyType() {
        return VariablePolicyType.AUDIO;
    }

    // Javadoc inherited
    protected VariantType getDefaultVariantType() {
        return VariantType.AUDIO;
    }
}
