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
import com.volantis.shared.metadata.type.constraint.Constraint;
import com.volantis.shared.metadata.type.mutable.MutableBooleanType;
import com.volantis.shared.metadata.type.mutable.MutableFieldDefinition;
import com.volantis.shared.metadata.type.mutable.MutableNumberType;
import com.volantis.shared.metadata.type.mutable.MutableStringType;
import com.volantis.shared.metadata.type.mutable.MutableStructureType;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.mutable.MutableStructureValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;
import com.volantis.shared.metadata.MetaDataObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junitx.util.PrivateAccessor;

/**
 * Test case for {@link StructureTypeImpl}.
 */
public class StructureTypeImplTestCase extends CompositeTypeImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case
     */
    public StructureTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableStructureTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableStructureTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableStructureType structureType1 = createStructureTypeForTests();
        MutableStructureType structureType2 = createStructureTypeForTests();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2",
                structureType1, structureType2);

        // ensure that they have the same hash code
        int structureType1Hashcode = structureType1.hashCode();
        int structureType2Hashcode = structureType2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + structureType1Hashcode + " and " + structureType2Hashcode,
                structureType1Hashcode == structureType2Hashcode);

        // now change a single item in each list and ensure that they are different
        MutableFieldDefinitionImpl fieldDefinition = new MutableFieldDefinitionImpl();
        fieldDefinition.setType( new MutableBooleanTypeImpl() );
        structureType2.getMutableFields().add(fieldDefinition);
        assertNotEquals(structureType1, structureType2);

        // see if the hashcodes are different
        structureType1Hashcode = structureType1.hashCode();
        structureType2Hashcode = structureType2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + structureType1Hashcode + " and "
                + structureType2Hashcode,
                structureType1Hashcode == structureType2Hashcode);
    }

    /**
     * Helper method for which creates a MutableStructureType with data in it.
     * @return a <code>MutableStructureType</code> with a single item in its list.
     */
    private MutableStructureType createStructureTypeForTests() {
        MutableStructureTypeImpl mutableStructureType = new MutableStructureTypeImpl();
        Set set = mutableStructureType.getMutableFields();
        set.add(new MutableFieldDefinitionImpl());
        return mutableStructureType;
    }

    /**
     * Tests that when a <code>MutableStructureType</code> is created by a call to
     * {@link com.volantis.shared.metadata.type.StructureType#createMutable},
     * the two objects do not
     * share a common <code>List</code>.
     */
    public void testCreateMutableFromMutableDoesNotShareSet() throws Throwable {
        MutableStructureType listValueImpl1 = (MutableStructureType)
                getMutableInhibitor();

        MutableStructureType listValueImpl2 =
                (MutableStructureType) listValueImpl1.createMutable();

        Set internalSet1 = (Set) PrivateAccessor.getField(listValueImpl1,
                "fields");

        Set internalSet2 = (Set) PrivateAccessor.getField(listValueImpl2,
                "fields");

        assertNotSame("Separate instances of the same object should not share sets",
                internalSet1, internalSet2);

        // check that the sets are not linked somehow by ensuring that the
        // sizes of the lists are different after adding an object to one of them
        internalSet1.add(new MutableFieldDefinitionImpl());

        assertFalse("The two lists should have different sizes after adding an object " +
                "to one list", internalSet1.size() == internalSet2.size());
    }

    public void testVerifyImplementation() {
        final MutableStructureType structureType =
            TYPE_FACTORY.createStructureType();

        // normal case
        final MutableStructureValue structureValue =
            VALUE_FACTORY.createStructureValue();
        Collection errors = structureType.verify(structureValue);
        assertEquals(0, errors.size());

        // invalid type
        final BooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        errors = structureType.verify(booleanValue);
        assertEquals(1, errors.size());
        VerificationError error =
            (VerificationError) errors.iterator().next();
        assertEquals(VerificationError.TYPE_INVALID_IMPLEMENTATION,
            error.getType());
        assertEquals("", error.getLocation());
        assertEquals(booleanValue, error.getInvalidValue());
        assertNull(error.getConstraint());
    }

    public void testVerifyFieldsOK() {
        final MutableStructureType structureType =
            TYPE_FACTORY.createStructureType();

        // set up the type strucutre
        final Set fieldTypes = structureType.getMutableFields();

        MutableFieldDefinition fieldDefinition =
            TYPE_FACTORY.createFieldDefinition("foo");
        final MutableStringType stringType = TYPE_FACTORY.createStringType();
        fieldDefinition.setType(stringType);
        fieldTypes.add(fieldDefinition);

        fieldDefinition = TYPE_FACTORY.createFieldDefinition("bar");
        final MutableBooleanType booleanType = TYPE_FACTORY.createBooleanType();
        fieldDefinition.setType(booleanType);
        fieldTypes.add(fieldDefinition);

        final MutableFieldDefinition childDefinition =
            TYPE_FACTORY.createFieldDefinition("child");
        final MutableStructureType childStructureType =
            TYPE_FACTORY.createStructureType();

        fieldDefinition = TYPE_FACTORY.createFieldDefinition("baz");
        final MutableNumberType numberType =
            TYPE_FACTORY.createNumberType();
        fieldDefinition.setType(numberType);
        childStructureType.getMutableFields().add(fieldDefinition);

        childDefinition.setType(childStructureType);
        fieldTypes.add(childDefinition);

        // set up the value strucutre
        final MutableStructureValue structureValue =
            VALUE_FACTORY.createStructureValue();
        final Map fieldValues = structureValue.getFieldValuesAsMutableMap();

        final MutableStringValue fooValue = VALUE_FACTORY.createStringValue();
        fooValue.setValue("Foo");
        fieldValues.put("foo", fooValue);
        fieldValues.put("bar", VALUE_FACTORY.createBooleanValue());
        final MutableStructureValue childStructureValue =
            VALUE_FACTORY.createStructureValue();
        final MutableNumberValue bazValue = VALUE_FACTORY.createNumberValue();
        bazValue.setValue(new Integer(123));
        childStructureValue.getFieldValuesAsMutableMap().put("baz", bazValue);
        fieldValues.put("child", childStructureValue);

        final Collection errors = structureType.verify(structureValue);
        assertEquals(0, errors.size());
    }

    public void testVerifyFieldsInvalidFields() {
        final MutableStructureType structureType =
            TYPE_FACTORY.createStructureType();

        // set up the type strucutre
        final Set fieldTypes = structureType.getMutableFields();

        MutableFieldDefinition fieldDefinition =
            TYPE_FACTORY.createFieldDefinition("foo");
        final MutableStringType stringType = TYPE_FACTORY.createStringType();
        fieldDefinition.setType(stringType);
        fieldTypes.add(fieldDefinition);

        fieldDefinition = TYPE_FACTORY.createFieldDefinition("bar");
        final MutableBooleanType booleanType = TYPE_FACTORY.createBooleanType();
        fieldDefinition.setType(booleanType);
        fieldTypes.add(fieldDefinition);

        final MutableFieldDefinition childDefinition =
            TYPE_FACTORY.createFieldDefinition("child");
        final MutableStructureType childStructureType =
            TYPE_FACTORY.createStructureType();

        fieldDefinition = TYPE_FACTORY.createFieldDefinition("baz");
        final MutableNumberType numberType =
            TYPE_FACTORY.createNumberType();
        fieldDefinition.setType(numberType);
        childStructureType.getMutableFields().add(fieldDefinition);

        childDefinition.setType(childStructureType);
        fieldTypes.add(childDefinition);

        // set up the value strucutre
        final MutableStructureValue structureValue =
            VALUE_FACTORY.createStructureValue();
        final Map fieldValues = structureValue.getFieldValuesAsMutableMap();

        final MutableStringValue fooValue = VALUE_FACTORY.createStringValue();
        fooValue.setValue("Foo");
        fieldValues.put("foo", fooValue);
        final MutableNumberValue numberValue = VALUE_FACTORY.createNumberValue();
        fieldValues.put("bar", numberValue);
        final MutableStructureValue childStructureValue =
            VALUE_FACTORY.createStructureValue();
        final MutableBooleanValue booleanValue =
            VALUE_FACTORY.createBooleanValue();
        childStructureValue.getFieldValuesAsMutableMap().put(
            "baz", booleanValue);
        fieldValues.put("child", childStructureValue);

        final Collection errors = structureType.verify(structureValue);
        assertEquals(2, errors.size());
        checkError(errors, "/bar", VerificationError.TYPE_INVALID_IMPLEMENTATION,
            numberValue, null);
        checkError(errors, "/child/baz",
            VerificationError.TYPE_INVALID_IMPLEMENTATION, booleanValue, null);
    }

    private void checkError(final Collection errors,
                            final String path,
                            final VerificationError.ErrorType expectedType,
                            final MetaDataValue expectedInvalidValue,
                            final Constraint expectedConstraint) {
        VerificationError error = null;
        for (Iterator iter = errors.iterator();
                iter.hasNext() && error == null; ) {
            final VerificationError test = (VerificationError) iter.next();
            if (path.equals(test.getLocation())) {
                error = test;
            }
        }
        assertNotNull("Error with path '" + path + "' found.", error);
        assertEquals(expectedType, error.getType());
        assertEquals(expectedInvalidValue, error.getInvalidValue());
        assertEquals(expectedConstraint, error.getConstraint());

    }

    public void testPersistence2() throws Exception {
        MutableStructureType mdo = new MutableStructureTypeImpl();
        Set fields = mdo.getMutableFields();
        fields.add(new ImmutableFieldDefinitionImpl("hello"));
        fields.add(new ImmutableFieldDefinitionImpl("there"));

        MetaDataObject result = checkPersistence((MetaDataObject)mdo.createImmutable());
        assertEquals("The detached object is not equal to the object " +
            "before persistence", mdo, result);
        MetaDataObject result2 = checkPersistence(mdo);

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
