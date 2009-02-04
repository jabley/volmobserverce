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

import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;

import java.util.Map;
import java.util.HashMap;

/**
 * Editor for rollover image policies.
 */
public class RolloverImagePolicyEditor extends BasicPolicyEditor {
    // Javadoc inherited
    protected PolicyType getPolicyType() {
        return PolicyType.ROLLOVER_IMAGE;
    }

    // Javadoc inherited
    protected PropertyDescriptor[] getAttributeDescriptors() {
        BeanClassDescriptor bcd = (BeanClassDescriptor) PolicyModel.MODEL_DESCRIPTOR.getTypeDescriptorStrict(RolloverImagePolicyBuilder.class);
        return new PropertyDescriptor[] {
            bcd.getPropertyDescriptor(PolicyModel.NORMAL_POLICY),
            bcd.getPropertyDescriptor(PolicyModel.OVER_POLICY)
        };
    }

    // Javadoc inherited
    protected Map getPolicyReferenceTypes() {
        Map types = new HashMap();
        types.put(PolicyModel.NORMAL_POLICY, PolicyType.IMAGE);
        types.put(PolicyModel.OVER_POLICY, PolicyType.IMAGE);
        return types;
    }
}
