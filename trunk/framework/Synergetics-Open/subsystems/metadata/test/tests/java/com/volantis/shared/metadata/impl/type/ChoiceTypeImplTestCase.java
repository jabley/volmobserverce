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
import com.volantis.shared.metadata.type.mutable.MutableChoiceDefinition;
import com.volantis.shared.metadata.type.mutable.MutableChoiceType;
import com.volantis.shared.metadata.type.mutable.MutableStringType;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableChoiceValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import junitx.util.PrivateAccessor;

/**
 * Test case for {@link ChoiceTypeImpl}.
 */
public class ChoiceTypeImplTestCase extends MetaDataTypeImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case
     */
    public ChoiceTypeImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableChoiceTypeImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableChoiceTypeImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableChoiceType choiceType1 = createChoiceTypeForTests();
        MutableChoiceType choiceType2 = createChoiceTypeForTests();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2",
                choiceType1, choiceType2);

        // ensure that they have the same hash code
        int ChoiceType1Hashcode = choiceType1.hashCode();
        int ChoiceType2Hashcode = choiceType2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + ChoiceType1Hashcode + " and " + ChoiceType2Hashcode,
                ChoiceType1Hashcode == ChoiceType2Hashcode);

        // now change a single item in each list and ensure that they are different
        MutableChoiceDefinitionImpl choiceDefinition = new MutableChoiceDefinitionImpl();
        choiceDefinition.setType( new MutableBooleanTypeImpl() );
        choiceType2.getMutableChoiceDefinitions().add(choiceDefinition);
        assertNotEquals(choiceType1, choiceType2);

        // see if the hashcodes are different
        ChoiceType1Hashcode = choiceType1.hashCode();
        ChoiceType2Hashcode = choiceType2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + ChoiceType1Hashcode + " and "
                + ChoiceType2Hashcode,
                ChoiceType1Hashcode == ChoiceType2Hashcode);
    }

    /**
     * Helper method for which creates a MutableChoiceType with data in it.
     * @return a <code>MutableChoiceType</code> with a single item in its list.
     */
    private MutableChoiceType createChoiceTypeForTests() {
        MutableChoiceTypeImpl mutableChoiceType = new MutableChoiceTypeImpl();
        Set set = mutableChoiceType.getMutableChoiceDefinitions();
        set.add(new MutableChoiceDefinitionImpl());
        return mutableChoiceType;
    }

    /**
     * Tests that when a <code>MutableChoiceType</code> is created by a call to
     * {@link com.volantis.shared.metadata.type.ChoiceType#createMutable},
     * the two objects do not
     * share a common <code>List</code>.
     */
    public void testCreateMutableFromMutableDoesNotShareSet() throws Throwable {
        MutableChoiceType listValueImpl1 = (MutableChoiceType)
                getMutableInhibitor();

        MutableChoiceType listValueImpl2 =
                (MutableChoiceType) listValueImpl1.createMutable();

        Set internalSet1 = (Set) PrivateAccessor.getField(listValueImpl1,
                "choiceDefinitions");

        Set internalSet2 = (Set) PrivateAccessor.getField(listValueImpl2,
                "choiceDefinitions");

        assertNotSame("Separate instances of the same object should not share sets",
                internalSet1, internalSet2);

        // check that the sets are not linked somehow by ensuring that the
        // sizes of the lists are different after adding an object to one of them
        internalSet1.add(new MutableChoiceDefinitionImpl());

        assertFalse("The two lists should have different sizes after adding an object " +
                "to one list", internalSet1.size() == internalSet2.size());
    }

    public void testVerifyImplementation() {
        final MutableChoiceType choiceType =
            TYPE_FACTORY.createChoiceType();

        // normal case
        final MutableChoiceValue choiceValue =
            VALUE_FACTORY.createChoiceValue();
        Collection errors = choiceType.verify(choiceValue);
        assertEquals(0, errors.size());

        // invalid type
        final BooleanValue booleanValue = VALUE_FACTORY.createBooleanValue();
        errors = choiceType.verify(booleanValue);
        assertEquals(1, errors.size());
        checkError(errors, "", VerificationError.TYPE_INVALID_IMPLEMENTATION,
                booleanValue, null);
    }

    public void testVerifyChoiceNameOK() {
        final MutableChoiceType choiceType = TYPE_FACTORY.createChoiceType();

        // set up the type structure
        final Set choiceDefinitions = 
            choiceType.getMutableChoiceDefinitions();

        MutableChoiceDefinition choiceDefinition =
            TYPE_FACTORY.createChoiceDefinition("foo");
        choiceDefinition.setType(TYPE_FACTORY.createStringType());
        choiceDefinitions.add(choiceDefinition);        
    
        // set up the value structure
        final MutableChoiceValue choiceValue =
            VALUE_FACTORY.createChoiceValue();
        
        choiceValue.setChoiceName("foo");
        final MutableStringValue fooValue = VALUE_FACTORY.createStringValue();
        fooValue.setValue("Foo");
        choiceValue.setValue(fooValue);

        final Collection errors = choiceType.verify(choiceValue);
        assertEquals(0, errors.size());
    }
    
    public void testVerifyChoiceNameInvalid() {
        final MutableChoiceType choiceType = TYPE_FACTORY.createChoiceType();

        // set up the type structure
        final Set choiceDefinitions = 
            choiceType.getMutableChoiceDefinitions();

        MutableChoiceDefinition choiceDefinition =
            TYPE_FACTORY.createChoiceDefinition("foo");
        choiceDefinition.setType(TYPE_FACTORY.createStringType());
        choiceDefinitions.add(choiceDefinition);        
    
        // set up the value structure
        final MutableChoiceValue choiceValue =
            VALUE_FACTORY.createChoiceValue();
        
        choiceValue.setChoiceName("bar");

        final Collection errors = choiceType.verify(choiceValue);
        assertEquals(1, errors.size());
    }
    
    public void testVerifyChoiceNameNull() {
        final MutableChoiceType choiceType = TYPE_FACTORY.createChoiceType();

        // set up the type structure
        final Set choiceDefinitions = 
            choiceType.getMutableChoiceDefinitions();

        MutableChoiceDefinition choiceDefinition =
            TYPE_FACTORY.createChoiceDefinition("foo");
        choiceDefinition.setType(TYPE_FACTORY.createStringType());
        choiceDefinitions.add(choiceDefinition);        
    
        // set up the value structure
        final MutableChoiceValue choiceValue =
            VALUE_FACTORY.createChoiceValue();
        
        final Collection errors = choiceType.verify(choiceValue);
        assertEquals(1, errors.size());
    }
    
    public void testVerifyChoiceValueOK() {
        final MutableChoiceType choiceType =
            TYPE_FACTORY.createChoiceType();

        // set up the type strucutre
        final Set choiceDefinitions = choiceType.getMutableChoiceDefinitions();

        MutableChoiceDefinition choiceDefinition =
            TYPE_FACTORY.createChoiceDefinition("foo");
        final MutableStringType stringType = TYPE_FACTORY.createStringType();
        choiceDefinition.setType(stringType);
        choiceDefinitions.add(choiceDefinition);

        // set up the value strucutre
        final MutableChoiceValue choiceValue =
            VALUE_FACTORY.createChoiceValue();
        choiceValue.setChoiceName("foo");
        final MutableStringValue fooValue = VALUE_FACTORY.createStringValue();
        fooValue.setValue("Foo");
        choiceValue.setValue(fooValue);

        final Collection errors = choiceType.verify(choiceValue);
        assertEquals(0, errors.size());
    }

    public void testVerifyChoiceValueInvalid() {
        final MutableChoiceType choiceType =
            TYPE_FACTORY.createChoiceType();

        // set up the type strucutre
        final Set choiceDefinitions = choiceType.getMutableChoiceDefinitions();

        MutableChoiceDefinition choiceDefinition =
            TYPE_FACTORY.createChoiceDefinition("foo");
        final MutableStringType stringType = TYPE_FACTORY.createStringType();
        choiceDefinition.setType(stringType);
        choiceDefinitions.add(choiceDefinition);

        // set up the value strucutre
        final MutableChoiceValue choiceValue =
            VALUE_FACTORY.createChoiceValue();
        choiceValue.setChoiceName("foo");
        final MutableNumberValue fooValue = VALUE_FACTORY.createNumberValue();
        fooValue.setValue(new Integer(1));
        choiceValue.setValue(fooValue);

        final Collection errors = choiceType.verify(choiceValue);
        assertEquals(1, errors.size());
    }

    public void testVerifyChoiceValueNull() {
        final MutableChoiceType choiceType =
            TYPE_FACTORY.createChoiceType();

        // set up the type strucutre
        final Set choiceDefinitions = choiceType.getMutableChoiceDefinitions();

        MutableChoiceDefinition choiceDefinition =
            TYPE_FACTORY.createChoiceDefinition("foo");
        final MutableStringType stringType = TYPE_FACTORY.createStringType();
        choiceDefinition.setType(stringType);
        choiceDefinitions.add(choiceDefinition);

        // set up the value strucutre
        final MutableChoiceValue choiceValue =
            VALUE_FACTORY.createChoiceValue();
        choiceValue.setChoiceName("foo");

        final Collection errors = choiceType.verify(choiceValue);
        assertEquals(1, errors.size());
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05  6560/3  tom VBM:2004122401 Added Inhibitor base class

 13-Jan-05  6560/1  tom VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
