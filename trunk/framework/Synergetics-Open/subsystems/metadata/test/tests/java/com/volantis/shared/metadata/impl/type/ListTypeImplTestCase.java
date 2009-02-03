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

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableUniqueMemberConstraint;
import com.volantis.shared.metadata.type.mutable.MutableListType;
import com.volantis.shared.metadata.value.mutable.MutableCollectionValue;
import com.volantis.shared.metadata.value.mutable.MutableListValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;

import java.util.Collection;

/**
 * Test case for {@link ListTypeImpl}.
 */
public class ListTypeImplTestCase extends CollectionTypeImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public ListTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableListTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableListTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

    }

    // javadoc inherited
    protected MutableCollectionValue createEmptyValue() {
        return VALUE_FACTORY.createListValue();
    }

    public void testVerifyUniqueMemberConstraint() {

        final MutableListType listType = TYPE_FACTORY.createListType();

        final ImmutableUniqueMemberConstraint uniqueMemberConstraint =
            CONSTRAINT_FACTORY.getUniqueMemberConstraint();
        listType.setUniqueMemberConstraint(uniqueMemberConstraint);

        // check constraint with right value
        MutableListValue listValue =
            (MutableListValue) createCollectionValue(new String[]{"one", "two"});
        Collection errors = listType.verify(listValue);
        assertEquals(0, errors.size());

        // check constraint violation
        listValue =
            (MutableListValue) createCollectionValue(new String[]{"one", "one"});
        errors = listType.verify(listValue);
        assertEquals(1, errors.size());
        final MutableStringValue stringValue = VALUE_FACTORY.createStringValue();
        stringValue.setValue("one");
        final VerificationError error =
            (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("[2]", error.getLocation());
        assertEquals(stringValue, error.getInvalidValue());
        assertEquals(uniqueMemberConstraint, error.getConstraint());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
