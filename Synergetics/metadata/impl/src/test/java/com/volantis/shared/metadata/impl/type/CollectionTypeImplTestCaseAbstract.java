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

import com.volantis.shared.metadata.type.MetaDataType;
import com.volantis.shared.metadata.type.VerificationError;
import com.volantis.shared.metadata.type.constraint.Constraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMaximumLengthConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMemberTypeConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableMinimumLengthConstraint;
import com.volantis.shared.metadata.type.mutable.MutableCollectionType;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableCollectionValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract test case for {@link CollectionTypeImpl}.
 */
public abstract class CollectionTypeImplTestCaseAbstract
        extends CompositeTypeImplTestCaseAbstract {

    /**
     * Constructor.
     * @param name The name of this test.
     */
    public CollectionTypeImplTestCaseAbstract(String name) throws Exception {
        super(name);
    }

    /**
     * Creates an empty collection value.
     */
    protected abstract MutableCollectionValue createEmptyValue();

    public void testVerify() {
        final MetaDataType collectionType = (MetaDataType) getMutableInhibitor();

        // normal case
        final MutableCollectionValue collectionValue =
            createCollectionValue(new String[]{"one", "two"});
        Collection errors = collectionType.verify(collectionValue);
        assertEquals(0, errors.size());

        // invalid type
        BooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        errors = collectionType.verify(booleanValue);
        assertEquals(1, errors.size());
        VerificationError error =
            (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_INVALID_IMPLEMENTATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(booleanValue, error.getInvalidValue());
        assertNull(error.getConstraint());
    }

    public void testVerifyMemberTypeConstraint() {

        final MutableCollectionType collectionType =
            (MutableCollectionType) getMutableInhibitor();

        final MutableMemberTypeConstraint memberTypeConstraint =
            CONSTRAINT_FACTORY.createMemberTypeConstraint();
        memberTypeConstraint.setMemberType(TYPE_FACTORY.createStringType());
        collectionType.setMemberTypeConstraint(memberTypeConstraint);

        // check constraint with right value
        final MutableCollectionValue collectionValue =
            createCollectionValue(new String[]{"one", "two"});
        Collection errors = collectionType.verify(collectionValue);
        assertEquals(0, errors.size());

        // check constraint violation
        memberTypeConstraint.setMemberType(TYPE_FACTORY.createBooleanType());
        collectionType.setMemberTypeConstraint(memberTypeConstraint);
        errors = collectionType.verify(collectionValue);
        assertEquals(4, errors.size());

        MutableStringValue stringValue = VALUE_FACTORY.createStringValue();
        stringValue.setValue("one");

        // build a set of the expected errors.
        List expectedErrors = new ArrayList();
        expectedErrors.add(new ExpectedError(
            VerificationError.TYPE_CONSTRAINT_VIOLATION, stringValue, memberTypeConstraint));
        expectedErrors.add(new ExpectedError(
            VerificationError.TYPE_INVALID_IMPLEMENTATION, stringValue, null));
        stringValue = VALUE_FACTORY.createStringValue();
        stringValue.setValue("two");
        expectedErrors.add(new ExpectedError(
            VerificationError.TYPE_CONSTRAINT_VIOLATION, stringValue, memberTypeConstraint));
        expectedErrors.add(new ExpectedError(
            VerificationError.TYPE_INVALID_IMPLEMENTATION, stringValue, null));

        // There should be the same number of expected errors as actual errors
        assertEquals(new Integer(expectedErrors.size()), new Integer(errors.size()));

        // iterate over the actual errors and compare to expected errors
        final Iterator iter = errors.iterator();
        while (iter.hasNext()) {
            VerificationError error = (VerificationError) iter.next();
            boolean finished = false;
            for (int i=0; i<expectedErrors.size() && !finished; i++) {
                ExpectedError expected = (ExpectedError) expectedErrors.get(i);
                if (expected.getErrorType().equals(error.getType())
                    && expected.getInvalidValue().equals(error.getInvalidValue())) {
                    if ( (null != expected.getConstraint() && expected.getConstraint().equals(error.getConstraint()))
                        || (null == expected.getConstraint() && null == error.getConstraint())) {
                        finished = true;
                        expectedErrors.remove(i);
                    }
                }
            }
            if (!finished) {
                fail("The error did not match any of the expected errors");
            }
        }
    }

    public void testVerifyMinimumLengthConstraint() {

        final MutableCollectionType collectionType =
            (MutableCollectionType) getMutableInhibitor();

        final MutableMinimumLengthConstraint minimumLengthConstraint =
            CONSTRAINT_FACTORY.createMinimumLengthConstraint();
        minimumLengthConstraint.setLimit(2);
        collectionType.setMinimumLengthConstraint(minimumLengthConstraint);

        // check constraint with right value
        final MutableCollectionValue collectionValue =
            createCollectionValue(new String[]{"one", "two"});
        Collection errors = collectionType.verify(collectionValue);
        assertEquals(0, errors.size());

        // check constraint violation
        minimumLengthConstraint.setLimit(3);
        collectionType.setMinimumLengthConstraint(minimumLengthConstraint);
        errors = collectionType.verify(collectionValue);
        assertEquals(1, errors.size());
        final Iterator iter = errors.iterator();
        VerificationError error = (VerificationError) iter.next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(collectionValue, error.getInvalidValue());
        assertEquals(minimumLengthConstraint, error.getConstraint());
    }

    public void testVerifyMaximumLengthConstraint() {

        final MutableCollectionType collectionType =
            (MutableCollectionType) getMutableInhibitor();

        final MutableMaximumLengthConstraint maximumLengthConstraint =
            CONSTRAINT_FACTORY.createMaximumLengthConstraint();
        maximumLengthConstraint.setLimit(2);
        collectionType.setMaximumLengthConstraint(maximumLengthConstraint);

        // check constraint with right value
        final MutableCollectionValue collectionValue =
            createCollectionValue(new String[]{"one", "two"});
        Collection errors = collectionType.verify(collectionValue);
        assertEquals(0, errors.size());

        // check constraint violation
        maximumLengthConstraint.setLimit(1);
        collectionType.setMaximumLengthConstraint(maximumLengthConstraint);
        errors = collectionType.verify(collectionValue);
        assertEquals(1, errors.size());
        final Iterator iter = errors.iterator();
        VerificationError error = (VerificationError) iter.next();
        assertEquals(VerificationError.TYPE_CONSTRAINT_VIOLATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(collectionValue, error.getInvalidValue());
        assertEquals(maximumLengthConstraint, error.getConstraint());
    }

    protected MutableCollectionValue createCollectionValue(final String[] values) {
        MutableCollectionValue collectionValue = createEmptyValue();
        final Collection contents =
            collectionValue.getContentsAsMutableCollection();
        for (int i = 0; i < values.length; i++) {
            final MutableStringValue stringValue =
                VALUE_FACTORY.createStringValue();
            stringValue.setValue(values[i]);
            contents.add(stringValue);
        }
        return collectionValue;
    }


    /**
     * A simple data holder class.
     */
    static class ExpectedError {
        VerificationError.ErrorType type;
        MetaDataValue invalidValue;
        Constraint constraint;

        public ExpectedError(VerificationError.ErrorType type,
                             MetaDataValue invalidValue,
                             Constraint constraint) {
            this.type = type;
            this.invalidValue = invalidValue;
            this.constraint = constraint;
        }

        public VerificationError.ErrorType getErrorType() {
            return type;
        }

        public MetaDataValue getInvalidValue() {
            return invalidValue;
        }
        public Constraint getConstraint() {
            return constraint;
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
