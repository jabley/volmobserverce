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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicyMock;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReferenceMock;
import com.volantis.mcs.runtime.policies.SelectedVariantMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is abstract test class for {@link AbstractComponentImageAssetReference}
 * and any implementing classes.  It provides basic tests for the functionality
 * contained in the abstract class.  These tests will be run on all subclasses.
 * Any functionality specific to the subclasses must be included in those
 * test class, which themselves should extend this class.
 */
public abstract class AbstractComponentImageAssetReferenceTestAbstract
        extends TestCaseAbstract {
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/6	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3753/5	claire	VBM:2004040612 Fixed supermerge, tabs, and JavaDoc

 07-Apr-04	3753/1	claire	VBM:2004040612 Increasing laziness of reference resolution

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
