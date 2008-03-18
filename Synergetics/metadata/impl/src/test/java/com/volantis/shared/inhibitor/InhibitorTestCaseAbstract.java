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

package com.volantis.shared.inhibitor;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.inhibitor.ImmutableInhibitor;

/**
 * Test case for the {@link com.volantis.shared.inhibitor.Inhibitor} base class.
 */
public abstract class InhibitorTestCaseAbstract extends TestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public InhibitorTestCaseAbstract(String name) {
        super(name);
    }

    /**
     * Helper method which gets a <code>MutableInhibitor</code>. This method must be
     * overridden for every test relating to concrete implementations of
     * <code>MutableInhibitor</code>.
     * @return a <strong>mutable</strong>. version of a </code>ImmutableInhibitor</code>.
     */
    protected abstract MutableInhibitor getMutableInhibitor();

    /**
     * Helper method which gets an <code>ImmutableInhibitor</code>. This method must
     * be overridden for every test relating to subclasses of
     * <code>ImmutableInhibitor</code>.
     * @return an <strong>immutable</strong> version of a </code>ImmutableInhibitor</code>.
     */
    protected abstract ImmutableInhibitor getImmutableInhibitor();

    /**
     * Method which tests that equals() and hashcode() have been implemented correctly on
     * this <code>MetaDataObject</code>. This method must be overridden in every test
     * relating to a concrete implementation of a <code>MetaDataObject</code>.
     *
     * <p>The idea of this test is to ensure that if we have two equal
     * <code>MetaDataObject</code> objects, then changing a single externally visible state
     * of one of these objects will result in the two objects not being equal. This change
     * should be done for every externally visible state, one at a time.</p>
     *
     * <p>At every stage where we check equality, we should ensure that when the objects
     * are equal, they have the same hashcode.</p>
     *
     * <p>NB: It would also be nice to ensure that if the objects are not equal then they
     * have different hashcodes. This may be achievable by careful construction of the
     * objects used for this test.</p>
     *
     */
    public abstract void testEqualsAndHashcodeImplementedCorrectly();

    // ***********************************************************************************
    // ***********************************************************************************
    // Tests for Creating an ImmutableInhibitor from a MutableInhibitor
    // ***********************************************************************************

    /**
     * Tests that a valid {@link com.volantis.shared.inhibitor.ImmutableInhibitor} is returned when
     * {@link com.volantis.shared.inhibitor.MutableInhibitor#createImmutable() is called.
     *
     * <p>This test ensures that the original and created objects have the same hashcode.
     * </p>
     */
    public void testCreateImmutableFromMutableReturnsSameHashcode() {
        MutableInhibitor mutableMDO = getMutableInhibitor();
        ImmutableInhibitor immutableMDO = mutableMDO.createImmutable();

        assertEquals("The original mutable object and the created immutable object " +
                "should shave the same hashcode",
                mutableMDO.hashCode(), immutableMDO.hashCode());
    }

    /**
     * Tests that a valid {@link com.volantis.shared.inhibitor.ImmutableInhibitor} is returned when
     * {@link com.volantis.shared.inhibitor.MutableInhibitor#createImmutable() is called.
     *
     * <p>This test ensures that the original and created objects have the same state. This
     * is achieved by using the equals() method. There is a separate test
     * {@link #testEqualsAndHashcodeImplementedCorrectly} which ensures that the equals
     * method returns valid results.</p>
     */
    public void testCreateImmutableFromMutableReturnsEqualObject() {
        MutableInhibitor mutableMDO = getMutableInhibitor();
        ImmutableInhibitor immutableMDO = mutableMDO.createImmutable();

        assertEquals("The original mutable object and the created immuatable object " +
                "should be equal", mutableMDO, immutableMDO);
    }

    // ***********************************************************************************
    // ***********************************************************************************
    // Tests for Creating an ImmutableInhibitor from an ImmutableInhibitor
    // ***********************************************************************************

    /**
     * Tests that a valid {@link com.volantis.shared.inhibitor.ImmutableInhibitor} is returned when
     * {@link com.volantis.shared.inhibitor.ImmutableInhibitor#createImmutable() is called.
     *
     * <p>This test ensures that the original and the created objects are the same object.
     * </p>
     */
    public final void testCreateImmutableFromImmutableReturnsSameObject() {
        ImmutableInhibitor originalImmutableMDO = getImmutableInhibitor();
        ImmutableInhibitor createdImmutableMDO = originalImmutableMDO.
                createImmutable();

        assertSame("A created immutable object should be the same as the original " +
                "immuatable object", originalImmutableMDO, createdImmutableMDO);
    }

    // ***********************************************************************************
    // ***********************************************************************************
    // Tests for Creating an MmutableInhibitor a MutableInhibitor
    // ***********************************************************************************

    /**
     * Tests that a valid {@link com.volantis.shared.inhibitor.MutableInhibitor} is returned when
     * {@link com.volantis.shared.inhibitor.MutableInhibitor#createMutable() is called.
     *
     * <p>This test ensures that the original and created objects have the same
     * hashcode.</p>
     *
     */
    public void testCreateMutableFromMutableReturnsSameHashcode() {
        MutableInhibitor originalMutableMDO = getMutableInhibitor();
        MutableInhibitor createdMutableMDO =
                originalMutableMDO.createMutable();

        assertEquals("The original mutable object and the created mutable object should" +
                "have the same hashcode",
                originalMutableMDO.hashCode(), createdMutableMDO.hashCode());
    }

    /**
     * Tests that a valid {@link com.volantis.shared.inhibitor.MutableInhibitor} is returned when
     * {@link com.volantis.shared.inhibitor.MutableInhibitor#createImmutable() is called.
     *
     * <p>This test ensures that the original and created objects have the same state. This
     * is achieved by using the equals() method. There is a separate test
     * {@link #testEqualsAndHashcodeImplementedCorrectly} which ensures that the equals
     * method returns valid results.</p>
     */
    public void testCreateMmutableFromMutableReturnsEqualObject() {
        MutableInhibitor originalMutableMDO = getMutableInhibitor();
        MutableInhibitor createdMutableMDO =
                originalMutableMDO.createMutable();

        assertEquals("The original mutable object and the created muatable object " +
                "should be equal", originalMutableMDO, createdMutableMDO);
    }

    // ***********************************************************************************
    // ***********************************************************************************
    // Tests for Creating an MutableInhibitor from an ImmutableInhibitor
    // ***********************************************************************************


    /**
     * Tests that a valid {@link com.volantis.shared.inhibitor.MutableInhibitor} is returned when
     * {@link com.volantis.shared.inhibitor.ImmutableInhibitor#createMutable() is called.
     *
     * <p>This test ensures that the original and created objects have the same
     * hashcode.</p>
     */
    public void testCreateMutableFromImmutableReturnsSameHashcode() {
        ImmutableInhibitor immutableMDO = getImmutableInhibitor();
        MutableInhibitor mutableMDO = immutableMDO.
                createMutable();

        assertEquals("The original immutable object and the created muatable object " +
                "should have the same hashcodes",
                immutableMDO.hashCode(), mutableMDO.hashCode());
    }

    /**
     * Tests that a valid {@link com.volantis.shared.inhibitor.ImmutableInhibitor} is returned when
     * {@link com.volantis.shared.inhibitor.MutableInhibitor#createImmutable() is called.
     *
     * <p>This test ensures that the original and created objects have the same state. This
     * is achieved by using the equals() method. There is a separate test
     * {@link #testEqualsAndHashcodeImplementedCorrectly} which ensures that the equals
     * method returns valid results.</p>
     */
    public void testCreateMutableFromImmutableReturnsEqualObject() {
        ImmutableInhibitor immutableMDO = getImmutableInhibitor();
        MutableInhibitor mutableMDO = immutableMDO.
                createMutable();

        assertEquals("The original immutable object and the created muatable object " +
                "should be equal", immutableMDO, mutableMDO);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/1	tom	VBM:2004122401 Added Inhibitor base class

 ===========================================================================
*/
