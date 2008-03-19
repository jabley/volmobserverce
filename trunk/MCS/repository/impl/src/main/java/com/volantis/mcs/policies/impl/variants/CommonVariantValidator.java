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

import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.metadata.InternalMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.InternalSelectionBuilder;
import com.volantis.mcs.policies.impl.variants.validation.VariantContentValidator;
import com.volantis.mcs.policies.impl.variants.validation.VariantMetaDataValidator;
import com.volantis.mcs.policies.impl.variants.validation.VariantSelectionValidator;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.PolicyModel;

/**
 */
public class CommonVariantValidator
        implements VariantValidator {

    private final VariantSelectionValidator selectionValidator;
    private final VariantMetaDataValidator metaDataValidator;
    private final VariantContentValidator contentValidator;
    private final VariantValidator variantSpecificValidator;
    private static final VariantValidator NOOP_VARIANT_VALIDATOR = new VariantValidator() {
        public void validate(
                ValidationContext context, SourceLocation sourceLocation,
                VariantBuilder variant) {
        }
    };

    public CommonVariantValidator(
            VariantSelectionValidator selectionValidator,
            VariantMetaDataValidator metaDataValidator,
            VariantContentValidator contentValidator,
            VariantValidator variantSpecificValidator) {

        this.selectionValidator = selectionValidator;
        this.metaDataValidator = metaDataValidator;
        this.contentValidator = contentValidator;
        this.variantSpecificValidator = variantSpecificValidator == null ?
                NOOP_VARIANT_VALIDATOR : variantSpecificValidator;
    }

    public void validate(
            ValidationContext context, SourceLocation sourceLocation,
            VariantBuilder variantBuilder) {

        // Check that the selection is valid for the current type.
        Step selectionStep = context.pushPropertyStep(PolicyModel.SELECTION);
        InternalSelectionBuilder selectionBuilder = (InternalSelectionBuilder)
                variantBuilder.getSelectionBuilder();
        selectionValidator.validateSelection(context, sourceLocation,
                variantBuilder, selectionBuilder);
        context.popStep(selectionStep);

        // Check whether the meta data is valid for the current type.
        Step metaDataStep = context.pushPropertyStep(PolicyModel.META_DATA);
        InternalMetaDataBuilder metaDataBuilder = (InternalMetaDataBuilder)
                variantBuilder.getMetaDataBuilder();
        metaDataValidator.validateMetaData(context, sourceLocation,
                variantBuilder, metaDataBuilder);
        context.popStep(metaDataStep);

        // Check whether the contents are valid for the current type.
        Step contentStep = context.pushPropertyStep(PolicyModel.CONTENT);
        InternalContentBuilder contentBuilder = (InternalContentBuilder)
                variantBuilder.getContentBuilder();
        contentValidator.validateContent(context, sourceLocation,
                variantBuilder, contentBuilder);
        context.popStep(contentStep);

        // Perform variantBuilder specific validation.
        variantSpecificValidator.validate(context, sourceLocation,
                variantBuilder);
    }
}
