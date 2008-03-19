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

package com.volantis.shared.metadata.impl.type;

import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataObjectImplTestCaseAbstract;
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.mutable.MutableBooleanType;
import com.volantis.shared.metadata.type.mutable.MutableFieldDefinition;

/**
 * Test case for {@link FieldDefinitionImpl}.
 */
public class FieldDefinitionImpltestCase extends MetaDataObjectImplTestCaseAbstract {

    /**
     * Constructor.
     * @param name The name of this test case.
     */
    public FieldDefinitionImpltestCase(String name) throws Exception {
        super(name);
    }

    protected MutableInhibitor getMutableInhibitor() {
        return new MutableFieldDefinitionImpl();
    }

    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableFieldDefinitionImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableFieldDefinition fieldDefinition1 = createFieldDefinitionForTestingEquals();
        MutableFieldDefinition fieldDefinition2 = createFieldDefinitionForTestingEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", fieldDefinition1, fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesEqual(fieldDefinition1, fieldDefinition2);

        fieldDefinition2.setType(null);
        assertNotEquals(fieldDefinition1, fieldDefinition2);
        MetaDataTestCaseHelper.ensureHashcodesNotEqual(fieldDefinition1, fieldDefinition2);
    }

    /**
     * Helper method which creates a <code>MutableBooleanType</code> which can be used for
     * testing.
     * @return a mutable number type.
     */
    private MutableFieldDefinition createFieldDefinitionForTestingEquals() {
        MutableFieldDefinition fieldDefinition = new MutableFieldDefinitionImpl("tom");
        MutableBooleanType booleanType = new MutableBooleanTypeImpl();
        MutableEnumeratedConstraint constraint = MetaDataFactory.getDefaultInstance().
                getTypeFactory().getConstraintFactory().createEnumeratedConstraint();
        booleanType.setEnumeratedConstraint(constraint);
        fieldDefinition.setType(booleanType);
        return fieldDefinition;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
