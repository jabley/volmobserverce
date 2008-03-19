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

package com.volantis.mcs.policies.compatibility;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.compatibility.Old2NewConverter;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.selection.TargetedSelection;
import com.volantis.mcs.objects.RepositoryObject;

import java.util.List;
import java.util.ArrayList;

public abstract class Old2NewConverterTestAbstract
        extends TestCaseAbstract {

    protected PolicyFactory factory;
    protected Old2NewConverter converter;
    protected static final String DEVICE_NAME = "Master";

    protected void setUp() throws Exception {
        super.setUp();

        factory = PolicyFactory.getDefaultInstance();
        converter = new Old2NewConverter(factory);
    }

    protected void checkName(RepositoryObject repositoryObject,
                             PolicyBuilder policyBuilder) {
        assertEquals("Name", repositoryObject.getName(), policyBuilder.getName());
    }

    protected void checkVariablePolicyType(
            VariablePolicyBuilder policyBuilder, VariablePolicyType expectedType) {
        assertEquals("Variable policy type", expectedType,
                policyBuilder.getVariablePolicyType());
    }

    protected void checkTargetedSelection(Variant variant) {
        TargetedSelection selection = (TargetedSelection) variant.getSelection();
        List deviceReferences = selection.getDeviceReferences();

        List expectedDeviceReferences = new ArrayList();
        expectedDeviceReferences.add(factory.createDeviceReference(DEVICE_NAME));
        assertEquals("Selection", expectedDeviceReferences, deviceReferences);

        List categoryReferences = selection.getCategoryReferences();
        if (categoryReferences != null && !categoryReferences.isEmpty()) {
            fail("Expected no category references, found " +
                    categoryReferences);
        }
    }
}
