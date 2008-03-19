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

package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableUniqueMemberConstraint;

/**
 * Test case for {@link UniqueMemberConstraintImpl}.
 */
public class UniqueMemberConstraintImplTestCase extends ConstraintImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public UniqueMemberConstraintImplTestCase(String name) throws Exception {
        super(name);
    }

    /**
     * Overridden as there is not a mutable form of
     * {@link com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint}.
     * @return null
     */
    protected MutableInhibitor getMutableInhibitor() {
        return null;
    }

    /**
     * Overridden as there is not a mutable form of
     * {@link com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint}.
     */
    public void testCreateImmutableFromMutableReturnsSameHashcode() {
    }

    /**
     * Overridden as there is not a mutable form of
     * {@link com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint}.
     */
    public void testCreateImmutableFromMutableReturnsEqualObject() {
    }

    /**
     * Overridden as there is not a mutable form of
     * {@link com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint}.
     */
    public void testCreateMutableFromMutableReturnsSameHashcode() {
    }

    /**
     * Overridden as there is not a mutable form of
     * {@link com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint}.
     */
    public void testCreateMmutableFromMutableReturnsEqualObject() {
    }

    /**
     * Overridden as there is not a mutable form of
     * {@link com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint}.
     */
    public void testCreateMutableFromImmutableReturnsSameHashcode() {
    }

    /**
     * Overridden as there is not a mutable form of
     * {@link com.volantis.shared.metadata.type.constraint.UniqueMemberConstraint}.
     */
    public void testCreateMutableFromImmutableReturnsEqualObject() {
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableUniqueMemberConstraintImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {

        ImmutableUniqueMemberConstraint uniqueMemberConstraint1 =
                new ImmutableUniqueMemberConstraintImpl();
        ImmutableUniqueMemberConstraint uniqueMemberConstraint2 =
                new ImmutableUniqueMemberConstraintImpl();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2", uniqueMemberConstraint1, 
                uniqueMemberConstraint2);

        // ensure that they have the same hash code
        int uniqueMemberConstraint1Hashcode = uniqueMemberConstraint1.hashCode();
        int uniqueMemberConstraint2Hashcode = uniqueMemberConstraint2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + uniqueMemberConstraint1Hashcode + " and " +
                uniqueMemberConstraint2Hashcode,
                uniqueMemberConstraint1Hashcode == uniqueMemberConstraint2Hashcode);
    }

    /**
     * This ensures that an exception is thrown if an attempt is made to create a
     * mutable version of an {@link ImmutableUniqueMemberConstraintImpl}.
     */
    public void testCreatingMutableFromImmutableThrowsException() {
        ImmutableUniqueMemberConstraint immutable
                = new ImmutableUniqueMemberConstraintImpl();
        try {
            immutable.createMutable();
            fail("It should not be possible to create an Immutable form of an " +
                    "ImmutableUniqueMemberConstraint");
        } catch(UnsupportedOperationException e) {
            // expected.
        }
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
