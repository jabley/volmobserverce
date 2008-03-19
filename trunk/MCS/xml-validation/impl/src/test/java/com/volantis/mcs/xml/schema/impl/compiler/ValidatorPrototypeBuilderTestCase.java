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

package com.volantis.mcs.xml.schema.impl.compiler;

import com.volantis.mcs.xml.schema.impl.model.BoundedContentImpl;
import com.volantis.mcs.xml.schema.impl.model.ContentChoiceImpl;
import com.volantis.mcs.xml.schema.impl.model.ContentSequenceImpl;
import com.volantis.mcs.xml.schema.impl.model.EmptyContentImpl;
import com.volantis.mcs.xml.schema.impl.model.ElementSchemaImpl;
import com.volantis.mcs.xml.schema.impl.validation.ContentTypeValidatorMock;
import com.volantis.mcs.xml.schema.impl.validation.ContentValidatorMock;
import com.volantis.mcs.xml.schema.impl.validation.ElementValidator;
import com.volantis.mcs.xml.schema.impl.validation.ElementValidatorMock;
import com.volantis.mcs.xml.schema.impl.validation.PrototypeElementValidatorMock;
import com.volantis.mcs.xml.schema.model.BoundedContent;
import com.volantis.mcs.xml.schema.model.ContentModel;
import com.volantis.mcs.xml.schema.model.ElementReferenceMock;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.model.ContentModelVisitor;
import com.volantis.mcs.xml.schema.model.ElementReference;
import com.volantis.mcs.xml.schema.model.ElementSchema;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidatorPrototypeBuilderTestCase
        extends TestCaseAbstract {

    private ElementType containingElement;
    private ElementReferenceMock aReferenceMock;
    private ElementReferenceMock bReferenceMock;
    private ValidatorPrototypeFactoryMock validatorFactoryMock;
    private ContentValidatorMock contentValidatorMock;
    private PrototypeElementValidatorMock elementValidatorMock;
    private ContentValidatorMock compositeValidatorMock;
    private ContentTypeValidatorMock aValidatorMock;
    private ContentTypeValidatorMock bValidatorMock;
    private ElementValidatorMock elementValidator2Mock;
    private static final MethodAction VISIT_ELEMENT_REFERENCE =
            new MethodAction() {
                public Object perform(MethodActionEvent event)
                        throws Throwable {

                    ContentModelVisitor visitor = (ContentModelVisitor)
                            event.getArgument(ContentModelVisitor.class);
                    visitor.visit((ElementReference) event.getSource());

                    return null;
                }
            };

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        containingElement = new ElementType("", "container");

        validatorFactoryMock = new ValidatorPrototypeFactoryMock(
                "factoryMock", expectations);

        contentValidatorMock = new ContentValidatorMock(
                "contentValidatorMock", expectations);

        elementValidatorMock = new PrototypeElementValidatorMock(
                "elementValidatorMock", expectations);

        compositeValidatorMock = new ContentValidatorMock(
                "compositeValidatorMock", expectations);

        elementValidator2Mock = new PrototypeElementValidatorMock(
                "elementValidator2Mock", expectations);
    }


    protected void useElementsAB() {

        useElementA();

        useElementB();

    }

    private void useElementA() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        ElementType aElement = new ElementType("", "a");

        aReferenceMock = new ElementReferenceMock(
                "aReferenceMock", expectations);

        aValidatorMock = new ContentTypeValidatorMock(
                "aValidatorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        aReferenceMock.expects.getElementType().returns(aElement).any();

        aReferenceMock.fuzzy.accept(
                mockFactory.expectsInstanceOf(ContentModelVisitor.class))
                .does(VISIT_ELEMENT_REFERENCE).any();

        aReferenceMock.expects.excluded().returns(null).any();

        validatorFactoryMock.expects.createSingleTypeValidator(aElement)
                .returns(aValidatorMock);

        aValidatorMock.expects.requiresPerElementState()
                .returns(false).any();
    }

    private void useElementB() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        ElementType bElement = new ElementType("", "a");

        bReferenceMock = new ElementReferenceMock(
                "bReferenceMock", expectations);

        bValidatorMock = new ContentTypeValidatorMock(
                "bValidatorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        bReferenceMock.expects.getElementType().returns(bElement).any();

        bReferenceMock.fuzzy.accept(
                mockFactory.expectsInstanceOf(ContentModelVisitor.class))
                .does(VISIT_ELEMENT_REFERENCE).any();

        bReferenceMock.expects.excluded().returns(null).any();

        validatorFactoryMock.expects.createSingleTypeValidator(bElement)
                .returns(bValidatorMock);

        bValidatorMock.expects.requiresPerElementState()
                .returns(false).any();
    }

    /**
     * Test that an empty model creates an empty validator.
     */
    public void testEmpty() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        validatorFactoryMock.expects.createEmptyContent()
                .returns(contentValidatorMock).any();

        validatorFactoryMock.expects
                .createElementValidator(
                        containingElement, contentValidatorMock,
                        null, false, false, false)
                .returns(elementValidatorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentModel model = new EmptyContentImpl();

        ElementSchema schema =
                new ElementSchemaImpl(containingElement, model);

        ValidatorPrototypeBuilder builder =
                new ValidatorPrototypeBuilder(validatorFactoryMock);
        ElementValidator validator = builder.build(schema);
        assertEquals(
                "Validator should match", elementValidatorMock, validator);
    }

    public void testChoiceOfContent() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        useElementsAB();

        Set emptySet = new HashSet();
        aValidatorMock.expects.addTo(emptySet);
        bValidatorMock.expects.addTo(emptySet);

        validatorFactoryMock.expects.createMultipleTypeValidator(emptySet)
                .returns(contentValidatorMock);

        contentValidatorMock.expects.requiresPerElementState()
                .returns(false).any();

        validatorFactoryMock.expects
                .createElementValidator(
                        containingElement, contentValidatorMock,
                        null, false, false, false)
                .returns(elementValidatorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentChoiceImpl choice = new ContentChoiceImpl();
        choice.add(aReferenceMock).add(bReferenceMock);

        ElementSchema schema =
                new ElementSchemaImpl(containingElement, choice);

        ValidatorPrototypeBuilder builder =
                new ValidatorPrototypeBuilder(validatorFactoryMock);
        ElementValidator validator = builder.build(schema);
        assertEquals(
                "Element validator should match", elementValidatorMock,
                validator);
    }

    /**
     * Test that an unlimited number of choices between types works creates a
     * validator that simply checks the type of an element.
     */
    public void testUnlimited() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        useElementA();

        validatorFactoryMock.expects.createUnlimited(aValidatorMock)
                .returns(contentValidatorMock);

        validatorFactoryMock.expects
                .createElementValidator(
                        containingElement, contentValidatorMock,
                        null, false, false, false)
                .returns(elementValidatorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        BoundedContent bounded = new BoundedContentImpl(aReferenceMock);

        ElementSchema schema =
                new ElementSchemaImpl(containingElement, bounded);

        ValidatorPrototypeBuilder builder =
                new ValidatorPrototypeBuilder(validatorFactoryMock);
        ElementValidator validator = builder.build(schema);
        assertEquals(
                "Element validator should match", elementValidatorMock,
                validator);
    }

    /**
     * Test that a bounded choice of types works.
     */
    public void testBoundedChoice() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        useElementA();

        validatorFactoryMock.expects.createBoundedContentValidator(
                aValidatorMock, 1, Integer.MAX_VALUE)
                .returns(compositeValidatorMock);

        validatorFactoryMock.expects
                .createElementValidator(
                        containingElement, compositeValidatorMock,
                        null, false, false, false)
                .returns(elementValidatorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentModel bounded = new BoundedContentImpl(aReferenceMock)
                .atLeastOne();

        ElementSchema schema =
                new ElementSchemaImpl(containingElement, bounded);

        ValidatorPrototypeBuilder builder =
                new ValidatorPrototypeBuilder(validatorFactoryMock);
        ElementValidator validator = builder.build(schema);
        assertEquals(
                "Element validator should match", elementValidatorMock,
                validator);
    }

    /**
     * Test that a sequence of content creates the correct validator.
     */
    public void testSequence() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        useElementsAB();

        List expectedList = new ArrayList();
        expectedList.add(aValidatorMock);
        expectedList.add(bValidatorMock);

        validatorFactoryMock.expects.createSequenceValidator(expectedList)
                .returns(compositeValidatorMock);

        validatorFactoryMock.expects
                .createElementValidator(
                        containingElement, compositeValidatorMock,
                        null, false, false, false)
                .returns(elementValidatorMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ContentModel sequence = new ContentSequenceImpl()
                .add(aReferenceMock)
                .add(bReferenceMock);

        ElementSchema schema =
                new ElementSchemaImpl(containingElement, sequence);

        ValidatorPrototypeBuilder builder =
                new ValidatorPrototypeBuilder(validatorFactoryMock);
        ElementValidator validator = builder.build(schema);
        assertEquals(
                "Element validator should match", elementValidatorMock,
                validator);
    }

    /**
     * Check that when called with the same model the same validators are
     * returned.
     */
    public void testOptimise() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        useElementA();

        validatorFactoryMock.expects
                .createElementValidator(
                        containingElement, aValidatorMock,
                        null, false, false, false)
                .returns(elementValidatorMock);

        validatorFactoryMock.expects.createUnlimited(aValidatorMock)
                .returns(compositeValidatorMock);

        validatorFactoryMock.expects
                .createElementValidator(
                        containingElement, compositeValidatorMock,
                        null, false, false, false)
                .returns(elementValidator2Mock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        ValidatorPrototypeBuilder builder =
                new ValidatorPrototypeBuilder(validatorFactoryMock);

        ElementSchema schema1 =
                new ElementSchemaImpl(containingElement, aReferenceMock);


        // Create validators for the same model so the element validator
        // should be the same.
        ElementValidator validator1 = builder.build(schema1);

        assertEquals(
                "Element validator should match", elementValidatorMock,
                validator1);

        // Create validator for a different model but using part of the
        // previous model so the content validator should be reused.
        ContentModel bounded = new BoundedContentImpl(aReferenceMock);
        ElementSchema schema2 =
                new ElementSchemaImpl(containingElement, bounded);
        ElementValidator validator3 = builder.build(schema2);

        assertEquals(
                "Second element validator should match", elementValidator2Mock,
                validator3);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
