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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessageMock;
import com.volantis.mcs.model.validation.ValidationContextMock;
import com.volantis.mcs.themes.AttributeSelectorMock;
import com.volantis.mcs.themes.ClassSelectorMock;
import com.volantis.mcs.themes.IdSelectorMock;
import com.volantis.mcs.themes.PseudoClassSelectorMock;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.PseudoElementSelectorMock;
import com.volantis.mcs.themes.PseudoElementTypeEnum;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.SelectorVisitorMock;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.UniversalSelectorMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link DefaultSelectorSequence}.
 */
public class DefaultSelectorSequenceTestCase extends TestCaseAbstract {

    ValidationContextMock context;
    StyleSheetFactory styleSheetFactory;
    SelectorSequence sequence;
    AttributeSelectorMock attributeSelector;
    AttributeSelectorMock attributeSelector2;
    ClassSelectorMock classSelector;
    ClassSelectorMock classSelector2;
    UniversalSelectorMock elementSelector;
    UniversalSelectorMock elementSelector2;
    IdSelectorMock idSelector;
    IdSelectorMock idSelector2;
    PseudoElementSelectorMock pseudoElementSelector;
    PseudoElementSelectorMock pseudoElementSelector2;
    PseudoClassSelectorMock structuralPseudoClassSelector;
    PseudoClassSelectorMock structuralPseudoClassSelector2;
    PseudoClassSelectorMock statefulPseudoClassSelector;
    I18NMessageMock message;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // Create test objects
        context = new ValidationContextMock("context", expectations);
        styleSheetFactory = DefaultStyleSheetFactory.getDefaultInstance();
        sequence = styleSheetFactory.createSelectorSequence();

        attributeSelector = new AttributeSelectorMock("attributeSelector",
                expectations);
        attributeSelector2 = new AttributeSelectorMock("attributeSelector2",
                expectations);
        classSelector = new ClassSelectorMock("classSelector", expectations);
        classSelector2 = new ClassSelectorMock("classSelector2", expectations);
        elementSelector = new UniversalSelectorMock("elementSelector",
                expectations);
        elementSelector2 = new UniversalSelectorMock("elementSelector2",
                expectations);
        idSelector = new IdSelectorMock("idSelector",expectations);
        idSelector2 = new IdSelectorMock("idSelector2",expectations);
        pseudoElementSelector = new PseudoElementSelectorMock(
                "pseudoElementSelector", expectations);
        pseudoElementSelector2 = new PseudoElementSelectorMock(
                "pseudoElementSelector2", expectations);
        structuralPseudoClassSelector = new PseudoClassSelectorMock(
                "structuralPseudoClassSelector", expectations);
        structuralPseudoClassSelector2 = new PseudoClassSelectorMock(
                "structuralPseudoClassSelector2", expectations);
        statefulPseudoClassSelector = new PseudoClassSelectorMock(
                "statefulPseudoClassSelector", expectations);
        message = new I18NMessageMock("message", expectations);
    }

    /**
     * Test that all the children selectors are visited.
     */
    public void testVisitContextChildren() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        final SelectorVisitorMock selectorVisitorMock =
                new SelectorVisitorMock("selectorVisitorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        structuralPseudoClassSelector.expects.getPseudoClassType().returns(
                PseudoClassTypeEnum.FIRST_CHILD).any();
        elementSelector.expects.accept(selectorVisitorMock);
        idSelector.expects.accept(selectorVisitorMock);
        classSelector.expects.accept(selectorVisitorMock);
        structuralPseudoClassSelector.expects.accept(selectorVisitorMock);
        attributeSelector.expects.accept(selectorVisitorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        SelectorSequence sequence = new DefaultSelectorSequence();
        sequence.addSelector(elementSelector);
        sequence.addSelector(idSelector);
        sequence.addSelector(classSelector);
        sequence.addSelector(structuralPseudoClassSelector);
        sequence.addSelector(attributeSelector);

        sequence.visitChildren(selectorVisitorMock);
    }

    public void testVisitSubjectChildren() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================
        final SelectorVisitorMock selectorVisitorMock =
                new SelectorVisitorMock("selectorVisitorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        structuralPseudoClassSelector.expects.getPseudoClassType().returns(
                PseudoClassTypeEnum.FIRST_CHILD).any();

        elementSelector.expects.accept(selectorVisitorMock);
        idSelector.expects.accept(selectorVisitorMock);
        classSelector.expects.accept(selectorVisitorMock);
        structuralPseudoClassSelector.expects.accept(selectorVisitorMock);
        attributeSelector.expects.accept(selectorVisitorMock);
        statefulPseudoClassSelector.expects.getPseudoClassType()
                .returns(PseudoClassTypeEnum.HOVER).any();
        statefulPseudoClassSelector.expects.accept(selectorVisitorMock);
        pseudoElementSelector.expects.accept(selectorVisitorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        sequence.addSelector(elementSelector);
        sequence.addSelector(idSelector);
        sequence.addSelector(classSelector);
        sequence.addSelector(structuralPseudoClassSelector);
        sequence.addSelector(attributeSelector);
        sequence.addSelector(pseudoElementSelector);
        sequence.addSelector(statefulPseudoClassSelector);

        sequence.visitChildren(selectorVisitorMock);
    }

    /**
     * Verify that:
     * <ul>
     * <li>Any attribute selectors are valid.</li>
     * <li>Any class selectors are valid.</li>
     * <li>The element selector is valid if present.</li>
     * <li>The ID selector is valid if present.<li>
     * <li>Any pseudo class selectors are valid.</li>
     * <li>Any pseudo element selectors are valid.</li>
     * <li>The selector sequence contains at least one selector.</li>
     * </ul>
     */
    public void testValidateWithFullyPopulatedSelectorsAsSubject() {

        // Set expectations.
        attributeSelector.expects.validate(context);
        attributeSelector2.expects.validate(context);
        classSelector.expects.validate(context);
        classSelector2.expects.validate(context);
        elementSelector.expects.validate(context);
        idSelector.expects.validate(context);
        structuralPseudoClassSelector.expects.getPseudoClassType().returns(
                PseudoClassTypeEnum.FIRST_CHILD).any();
        structuralPseudoClassSelector.expects.validate(context);
        structuralPseudoClassSelector2.expects.getPseudoClassType().returns(
                PseudoClassTypeEnum.FIRST_CHILD).any();
        structuralPseudoClassSelector2.expects.validate(context);
        pseudoElementSelector.expects.getPseudoElementType().returns(
                PseudoElementTypeEnum.BEFORE).any();
        pseudoElementSelector.expects.validate(context);
        pseudoElementSelector2.expects.getPseudoElementType().returns(
                PseudoElementTypeEnum.BEFORE).any();
        pseudoElementSelector2.expects.validate(context);

        sequence.addSelector(attributeSelector);
        sequence.addSelector(attributeSelector2);
        sequence.addSelector(classSelector);
        sequence.addSelector(classSelector2);
        sequence.addSelector(elementSelector);
        sequence.addSelector(idSelector);
        sequence.addSelector(structuralPseudoClassSelector);
        sequence.addSelector(structuralPseudoClassSelector2);
        sequence.addSelector(pseudoElementSelector);
        sequence.addSelector(pseudoElementSelector2);

        // test!
        sequence.validate(context, false);
    }

    public void testValidateWithNoSelectors() {

        // Set expectations.
        context.expects.createMessage("theme-selector-sequence-empty").
                returns(message);
        context.expects.addDiagnostic(sequence.getSourceLocation(),
                DiagnosticLevel.ERROR, message);

        // Run test.
        sequence.validate(context);
    }

    public void testValidateWithAttributeSelectors() {
        // Create test objects.
        AttributeSelectorMock attributeSelector3 = new AttributeSelectorMock(
                "attributeSelector3", expectations);

        // Set expectations.
        attributeSelector.expects.validate(context);
        attributeSelector2.expects.validate(context);
        attributeSelector3.expects.validate(context);

        sequence.addSelector(attributeSelector);
        sequence.addSelector(attributeSelector2);
        sequence.addSelector(attributeSelector3);

        // test!
        sequence.validate(context);
    }

    public void testValidateWithClassSelectors() {
        // Create test objects.
        ClassSelectorMock classSelector3 = new ClassSelectorMock(
                "classSelector3", expectations);

        // Set expectations.
        classSelector.expects.validate(context);
        classSelector2.expects.validate(context);
        classSelector3.expects.validate(context);

        sequence.addSelector(classSelector);
        sequence.addSelector(classSelector2);
        sequence.addSelector(classSelector3);

        // test!
        sequence.validate(context);
    }

    public void testValidateWithSingleIDSelector() {
        // Set expectations.
        idSelector.expects.validate(context);
        sequence.addSelector(idSelector);

        // test!
        sequence.validate(context);
    }

    public void testValidateWithMultipleIDSelectors() {

        // Set expectations.
        idSelector.expects.validate(context);
        idSelector2.expects.validate(context);
        sequence.addSelector(idSelector);
        sequence.addSelector(idSelector2);

        context.expects.createMessage("duplicate-selector", "IDSelector").
                returns(message);
        context.expects.addDiagnostic(sequence.getSourceLocation(),
                DiagnosticLevel.ERROR, message);

        // test!
        sequence.validate(context);
    }

    public void testValidateWithSingleElementSelector() {
        // Set expectations.
        elementSelector.expects.validate(context);
        sequence.addSelector(elementSelector);

        // test!
        sequence.validate(context);
    }

    public void testValidateWithMultipleElementSelectors() {

        // Set expectations.
        elementSelector.expects.validate(context);
        elementSelector2.expects.validate(context);
        sequence.addSelector(elementSelector);
        sequence.addSelector(elementSelector2);

        context.expects.createMessage("duplicate-selector", "ElementSelector").
                returns(message);
        context.expects.addDiagnostic(sequence.getSourceLocation(),
                DiagnosticLevel.ERROR, message);

        // test!
        sequence.validate(context);
    }

    public void testValidateWithPseudoClassSelectorsAsSubject() {

        // Create test objects.
        PseudoClassSelectorMock statefulPseudoClassSelector =
                new PseudoClassSelectorMock("statefulPseudoClassSelector",
                        expectations);
        sequence.addSelector(structuralPseudoClassSelector);
        sequence.addSelector(structuralPseudoClassSelector2);
        sequence.addSelector(statefulPseudoClassSelector);

        // Set expectations.
        structuralPseudoClassSelector.expects.validate(context);
        structuralPseudoClassSelector.expects.getPseudoClassType().
                returns(PseudoClassTypeEnum.FIRST_CHILD);
        structuralPseudoClassSelector2.expects.validate(context);
        structuralPseudoClassSelector2.expects.getPseudoClassType().
                returns(PseudoClassTypeEnum.NTH_CHILD);
        statefulPseudoClassSelector.expects.validate(context);
        statefulPseudoClassSelector.expects.getPseudoClassType().
                returns(PseudoClassTypeEnum.ACTIVE).fixed(2);

        // test!
        sequence.validate(context, false);
    }

//    public void testValidateWithPseudoClassSelectorsAsContext() {
//
//        // Create test objects.
//        PseudoClassSelectorMock statefulPseudoClassSelector =
//                new PseudoClassSelectorMock("statefulPseudoClassSelector",
//                        expectations);
//
//        sequence.addSelector(structuralPseudoClassSelector);
//        sequence.addSelector(structuralPseudoClassSelector2);
//        sequence.addSelector(statefulPseudoClassSelector);
//
//        // Set expectations.
//        structuralPseudoClassSelector.expects.validate(context);
//        structuralPseudoClassSelector.expects.getPseudoClassType().
//                returns(PseudoClassTypeEnum.FIRST_CHILD);
//        structuralPseudoClassSelector2.expects.validate(context);
//        structuralPseudoClassSelector2.expects.getPseudoClassType().
//                returns(PseudoClassTypeEnum.NTH_CHILD);
//        statefulPseudoClassSelector.expects.validate(context);
//        statefulPseudoClassSelector.expects.getPseudoClassType().
//                returns(PseudoClassTypeEnum.ACTIVE);
//
//        context.expects.createMessage("invalid-combined-selector").
//                returns(message);
//        context.expects.addDiagnostic(sequence, DiagnosticLevel.ERROR, message);
//
//
//        // test!
//        sequence.validate(context, true);
//    }

    public void testValidateWithPseudoElementSelectors() {

        // Create test objects.
        PseudoElementSelectorMock pseudoElementSelector3 =
                new PseudoElementSelectorMock("pseudoElementSelector3",
                        expectations);

        sequence.addSelector(pseudoElementSelector);
        sequence.addSelector(pseudoElementSelector2);
        sequence.addSelector(pseudoElementSelector3);

        // Set expectations.
        pseudoElementSelector.expects.validate(context);
        pseudoElementSelector2.expects.validate(context);
        pseudoElementSelector3.expects.validate(context);

        // test!
        sequence.validate(context);
    }

    /**
     * Verify that validation of a context selector sequence containing
     * stateful pseudoclass selectors will fail, but that the same sequence is
     * a valid selector sequence
     */
//    public void testValidateSelectorSequenceWithStatefulPCSelector() {
//
//        // Create test objects.
//        final SelectorVisitorMock selectorVisitorMock =
//                new SelectorVisitorMock("selectorVisitorMock", expectations);
//
//        sequence.addSelector(statefulPseudoClassSelector);
//        sequence.addSelector(structuralPseudoClassSelector);
//
//        // Set expectations.
//        structuralPseudoClassSelector.expects.getPseudoClassType().returns(
//                PseudoClassTypeEnum.FIRST_CHILD).fixed(2);
//        statefulPseudoClassSelector.expects.getPseudoClassType()
//                .returns(PseudoClassTypeEnum.HOVER).fixed(2);
//        structuralPseudoClassSelector.expects.accept(selectorVisitorMock).fixed(2);
//        statefulPseudoClassSelector.expects.accept(selectorVisitorMock).fixed(2);
//        structuralPseudoClassSelector.expects.validate(context).fixed(2);
//        statefulPseudoClassSelector.expects.validate(context).fixed(2);
//        context.expects.createMessage("invalid-combined-selector",
//                mockFactory.expectsArrayOf(Object.class)).returns(message);
//        context.expects.addDiagnostic(sequence, DiagnosticLevel.ERROR, message);
//
//        // Test!
//        sequence.validate(context, true);
//        sequence.validate(context, false);
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 01-Nov-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 ===========================================================================
*/
