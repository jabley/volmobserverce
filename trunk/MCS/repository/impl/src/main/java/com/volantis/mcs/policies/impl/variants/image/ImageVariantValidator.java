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

package com.volantis.mcs.policies.impl.variants.image;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.VariantValidator;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.BaseURLRelativeBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;

public class ImageVariantValidator
        implements VariantValidator {

    public void validate(
            ValidationContext context, SourceLocation sourceLocation,
            VariantBuilder variantBuilder) {

        ContentBuilder content = variantBuilder.getContentBuilder();
        if (content instanceof BaseURLRelativeBuilder) {
            BaseURLRelativeBuilder relative = (BaseURLRelativeBuilder) content;
            ImageMetaDataBuilder image = (ImageMetaDataBuilder)
                    variantBuilder.getMetaDataBuilder();
            SelectionBuilder selection = variantBuilder.getSelectionBuilder();

            if (relative.getBaseLocation() == BaseLocation.DEVICE) {
                if (image.getConversionMode() ==
                        ImageConversionMode.ALWAYS_CONVERT) {
                    context.addDiagnostic(sourceLocation,
                            DiagnosticLevel.ERROR,
                            context.createMessage(
                                    "convertible-image-not-on-device"));
                } else if (selection instanceof GenericImageSelectionBuilder) {
                    context.addDiagnostic(sourceLocation,
                            DiagnosticLevel.ERROR,
                            context.createMessage(
                                    "generic-image-not-on-device"));
                }
            }
        }
    }
}
