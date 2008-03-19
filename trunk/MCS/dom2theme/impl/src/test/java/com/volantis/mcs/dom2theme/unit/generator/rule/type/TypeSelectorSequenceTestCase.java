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
package com.volantis.mcs.dom2theme.unit.generator.rule.type;

import com.volantis.mcs.dom2theme.impl.generator.rule.type.TypeSelectorSequence;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.PseudoElementMock;
import com.volantis.styling.PseudoStyleEntityIterateeMock;
import com.volantis.styling.StatefulPseudoClassIteratee;
import com.volantis.styling.StatefulPseudoClassMock;
import com.volantis.styling.StatefulPseudoClassSetMock;
import com.volantis.styling.unit.StatefulPseudoClassSetIterateMethodAction;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

public class TypeSelectorSequenceTestCase extends TestCaseAbstract {

    private static final ExpectedValue ITERATEE =
            mockFactory.expectsInstanceOf(StatefulPseudoClassIteratee.class);

    private PseudoElementMock pseudoElementMock;

    private StatefulPseudoClassMock statefulPseudoClassMock;

    private PseudoStyleEntityIterateeMock entityIterateeMock;

    private MethodAction iterateOneAction;
    private StatefulPseudoClassSetMock statefulPseudoClassSetMock;

    protected void setUp() throws Exception {

        super.setUp();

        // ==================================================================
        // Create mocks.
        // ==================================================================

        pseudoElementMock = new PseudoElementMock(
                        "pseudo element", expectations);

        statefulPseudoClassMock =
                new StatefulPseudoClassMock("stateful pseudo class",
                                expectations);

        statefulPseudoClassSetMock = new StatefulPseudoClassSetMock(
                "statefulPseudoClassSetMock", expectations);

        entityIterateeMock = new PseudoStyleEntityIterateeMock(
                "entity iteratee", expectations);

        // Create an action which pretends to iterate over a single individual
        // pseudo class.
        iterateOneAction = new StatefulPseudoClassSetIterateMethodAction() {
            public void iterate(StatefulPseudoClassIteratee iteratee) {
                iteratee.next(statefulPseudoClassMock);
            }
        };

    }

    /**
     * Test that we can set and get the type.
     */
    public void testType() {

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.setType("type");
        assertEquals("", "type", typeSelectorSequence.getType());
    }

    /**
     * Test that the we can add pseudo entities and they are all in the
     * pseudo style path returned.
     */
    public void testPath() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        statefulPseudoClassSetMock.fuzzy.iterate(ITERATEE).does(iterateOneAction);

        entityIterateeMock.expects.next(pseudoElementMock).returns(
                IterationAction.CONTINUE);

        entityIterateeMock.expects.next(statefulPseudoClassSetMock).returns(
                IterationAction.CONTINUE);

        // ==================================================================
        // Do the test.
        // ==================================================================

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.addPseudoElement(pseudoElementMock);

        typeSelectorSequence.addPseudoClassSet(statefulPseudoClassSetMock);

        PseudoStylePath path = typeSelectorSequence.getPath();

        path.iterate(entityIterateeMock);

    }

    /**
     * Test composite for an empty sequence is false .
     */
    public void testEmptyComposite() {

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        assertFalse(typeSelectorSequence.isComposite());
    }

    /**
     * Test composite for a type selector is false.
     */
    public void testTypeComposite() {

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.setType("type");

        assertFalse(typeSelectorSequence.isComposite());
    }

    /**
     * Test composite for a pseudo element selector is false.
     */
    public void testPseudoElementComposite() {

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.addPseudoElement(pseudoElementMock);

        assertFalse(typeSelectorSequence.isComposite());
    }

    /**
     * Test composity for a pseudo class selector is false.
     */
    public void testPseudoClassComposite() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        statefulPseudoClassSetMock.fuzzy
                .iterate(ITERATEE)
                .does(iterateOneAction);

        // ==================================================================
        // Do the test.
        // ==================================================================

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.addPseudoClassSet(statefulPseudoClassSetMock);

        assertFalse(typeSelectorSequence.isComposite());
    }

    /**
     * Test composite for a selection of selectors.
     */
    public void testManyComposite() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        statefulPseudoClassSetMock.fuzzy.iterate(ITERATEE)
                .does(iterateOneAction);

        // ==================================================================
        // Do the test.
        // ==================================================================

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.setType("type");
        assertFalse(typeSelectorSequence.isComposite());

        typeSelectorSequence.addPseudoElement(pseudoElementMock);
        assertTrue(typeSelectorSequence.isComposite());

        typeSelectorSequence.addPseudoClassSet(statefulPseudoClassSetMock);
        assertTrue(typeSelectorSequence.isComposite());
    }

    /**
     * Test specificity for an empty sequence is 0,
     */
    public void testEmptySpecificity() {

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        assertEquals(0, typeSelectorSequence.getSpecificity());
    }

    /**
     * Test specificity for type selector is 1.
     */
    public void testTypeSpecificity() {

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.setType("type");

        assertEquals(1, typeSelectorSequence.getSpecificity());
    }

    /**
     * Test specificity for a pseudo element selector is 1.
     */
    public void testPseudoElementSpecificity() {

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.addPseudoElement(pseudoElementMock);

        assertEquals(1, typeSelectorSequence.getSpecificity());
    }

    /**
     * Test specificity for a pseudo class selector is 10.
     */
    public void testPseudoClassSpecificity() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        statefulPseudoClassSetMock.fuzzy.iterate(ITERATEE)
                .does(iterateOneAction);

        // ==================================================================
        // Do the test.
        // ==================================================================

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        typeSelectorSequence.addPseudoClassSet(statefulPseudoClassSetMock);

        assertEquals(10, typeSelectorSequence.getSpecificity());
    }

    /**
     * Test the specificity for a selection of selectors.
     */
    public void testManySpecificity() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        statefulPseudoClassSetMock.fuzzy.iterate(ITERATEE)
                .does(iterateOneAction);

        // ==================================================================
        // Do the test.
        // ==================================================================

        TypeSelectorSequence typeSelectorSequence =
                new TypeSelectorSequence();

        int specificity = 0;

        typeSelectorSequence.setType("type");
        specificity += 1;

        typeSelectorSequence.addPseudoElement(pseudoElementMock);
        specificity += 1;
        typeSelectorSequence.addPseudoElement(pseudoElementMock);
        specificity += 1;

        typeSelectorSequence.addPseudoClassSet(statefulPseudoClassSetMock);
        specificity += 10;

        assertEquals(specificity, typeSelectorSequence.getSpecificity());
    }

    public void testEquals() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        statefulPseudoClassSetMock.fuzzy.iterate(ITERATEE).returns().any();

        // ==================================================================
        // Do the test.
        // ==================================================================

        TypeSelectorSequence sequence1 = new TypeSelectorSequence();
        TypeSelectorSequence sequence2 = new TypeSelectorSequence();
        assertEquals(sequence1, sequence2);

        sequence1.setType("type");
        assertNotEquals(sequence1, sequence2);

        sequence2.setType("type");
        assertEquals(sequence1, sequence2);

        sequence1.addPseudoElement(pseudoElementMock);
        assertNotEquals(sequence1, sequence2);

        sequence2.addPseudoElement(pseudoElementMock);
        assertEquals(sequence1, sequence2);

        sequence1.addPseudoClassSet(statefulPseudoClassSetMock);
        assertNotEquals(sequence1, sequence2);

        sequence2.addPseudoClassSet(statefulPseudoClassSetMock);
        assertEquals(sequence1, sequence2);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jul-05	8668/6	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/4	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
