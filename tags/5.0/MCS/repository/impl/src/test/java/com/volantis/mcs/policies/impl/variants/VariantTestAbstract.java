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

package com.volantis.mcs.policies.impl.variants;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.validation.VariantValidatorSelectorMock;
import com.volantis.mcs.policies.variants.content.InternalContentMock;
import com.volantis.mcs.policies.variants.metadata.InternalMetaDataMock;
import com.volantis.mcs.policies.variants.selection.InternalSelectionMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

public abstract class VariantTestAbstract
        extends TestCaseAbstract {

    private static final ExpectedValue EXPECTS_VALIDATION_CONTEXT_INSTANCE =
            mockFactory.expectsInstanceOf(ValidationContext.class);

    private VariantValidatorSelectorMock variantValidatorSelectorMock;
    private VariantValidatorMock variantValidatorMock;
    private InternalSelectionMock internalSelectionMock;
    private InternalMetaDataMock internalMetaDataMock;
    private InternalContentMock internalContentMock;

//    protected void setUp() throws Exception {
//        super.setUp();
//
//        // =====================================================================
//        //   Create Mocks
//        // =====================================================================
//
//        variantValidatorSelectorMock = new VariantValidatorSelectorMock(
//                "variantValidatorSelectorMock", expectations);
//
//        variantValidatorMock =
//                new VariantValidatorMock("variantValidatorMock", expectations);
//
//        internalSelectionMock = new InternalSelectionMock(
//                "internalSelectionMock", expectations);
//
//        internalMetaDataMock =
//                new InternalMetaDataMock("internalMetaDataMock", expectations);
//
//        internalContentMock =
//                new InternalContentMock("internalContentMock", expectations);
//    }
//
//    /**
//     * Test that variants validation detects when no variants type is specified.
//     */
//    public void testValidationNoVariantType() {
//
//        // =====================================================================
//        //   Test Expectations
//        // =====================================================================
//
//        VariantImpl variants = new VariantImpl(variantValidatorSelectorMock);
//
//        TestValidator validator = new TestValidator();
//        validator.expectDiagnostic(DiagnosticLevel.ERROR,
//                PolicyMessages.UNSPECIFIED, new Object[]{
//                    VariantImpl.VARIANT_TYPE.getName()
//                });
//
//        validator.validate(variants);
//    }
//
//    /**
//     * Test that variants validation selects and uses a variants validator
//     * correctly when neither selection, meta data, or content is specified.
//     */
//    public void testValidationValidVariantTypeNothingSpecified() {
//
//        // =====================================================================
//        //   Set Expectations
//        // =====================================================================
//
//        variantValidatorSelectorMock.expects
//                .selectValidator(VariantType.AUDIO)
//                .returns(variantValidatorMock)
//                .any();
//
//        variantValidatorMock.expects.validateMetaData(
//                EXPECTS_VALIDATION_CONTEXT_INSTANCE, null);
//
//        variantValidatorMock.expects.validateContent(
//                EXPECTS_VALIDATION_CONTEXT_INSTANCE, null);
//
//        // =====================================================================
//        //   Test Expectations
//        // =====================================================================
//
//        VariantImpl variants = new VariantImpl(variantValidatorSelectorMock);
//        variants.setVariantType(VariantType.AUDIO);
//
//        TestValidator validator = new TestValidator();
//        validator.expectDiagnostic(DiagnosticLevel.ERROR,
//                PolicyMessages.UNSPECIFIED, new Object[]{
//                    VariantImpl.SELECTION.getName()
//                });
//
//        validator.validate(variants);
//    }
//
//    /**
//     * Test that variants validation selects and uses a variants validator
//     * correctly when all selection, meta data, and content are specified.
//     */
//    public void testValidationValidVariantTypeAllSpecified() {
//
//        // =====================================================================
//        //   Set Expectations
//        // =====================================================================
//
//        variantValidatorSelectorMock.expects
//                .selectValidator(VariantType.AUDIO)
//                .returns(variantValidatorMock)
//                .any();
//
//        variantValidatorMock.expects.validateSelection(
//                EXPECTS_VALIDATION_CONTEXT_INSTANCE,
//                internalSelectionMock);
//
//        variantValidatorMock.expects.validateMetaData(
//                EXPECTS_VALIDATION_CONTEXT_INSTANCE,
//                internalMetaDataMock);
//
//        variantValidatorMock.expects.validateContent(
//                EXPECTS_VALIDATION_CONTEXT_INSTANCE,
//                internalContentMock);
//
//        // =====================================================================
//        //   Test Expectations
//        // =====================================================================
//
//        VariantImpl variants = new VariantImpl(variantValidatorSelectorMock);
//        variants.setVariantType(VariantType.AUDIO);
//        variants.setSelection(internalSelectionMock);
//        variants.setMetaData(internalMetaDataMock);
//        variants.setContent(internalContentMock);
//
//        TestValidator validator = new TestValidator();
//        validator.validate(variants);
//    }
}
