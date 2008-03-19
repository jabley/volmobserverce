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

import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.eclipse.builder.editors.common.EncodingLabelProvider;
import com.volantis.mcs.eclipse.builder.editors.common.ComboDescriptor;
import com.volantis.mcs.eclipse.builder.editors.common.ComboDescriptorUtil;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * Editor for base URL policies (asset groups).
 */
public class BaseURLPolicyEditor extends BasicPolicyEditor {


    // Javadoc inherited
    protected PolicyType getPolicyType() {
        return PolicyType.BASE_URL;
    }

    // Javadoc inherited
    protected Map getComboDescriptors() {
         return ComboDescriptorUtil.getBaseLocationDescriptors();
    }

    // Javadoc inherited
    protected PropertyDescriptor[] getAttributeDescriptors() {
        BeanClassDescriptor bcd = (BeanClassDescriptor) PolicyModel.MODEL_DESCRIPTOR.getTypeDescriptorStrict(BaseURLPolicyBuilder.class);
        return new PropertyDescriptor[] {
            bcd.getPropertyDescriptor(PolicyModel.BASE_URL),
            bcd.getPropertyDescriptor(PolicyModel.BASE_LOCATION)
        };
    }
}
