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

package com.volantis.mcs.policies.impl.variants.validation;

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;

import java.util.HashSet;
import java.util.Set;

public class VariantContentValidator {

    /**
     * The set of allowable content types.
     */
    private final Set allowableContentTypes;

    public VariantContentValidator(ContentType[] types) {
        allowableContentTypes = new HashSet();
        for (int i = 0; i < types.length; i++) {
            ContentType type = types[i];
            allowableContentTypes.add(type);
        }
    }

    /**
     * Validate that the content is allowed.
     *
     * <p>This assumes that the caller has pushed an object on the context.</p>
     *
     * @param context The content within which the validation occurs.
     * @param sourceLocation
     * @param variantBuilder The variants being validated.
     * @param contentBuilder The content being validated.
     */
    public void validateContent(
            ValidationContext context, SourceLocation sourceLocation,
            VariantBuilder variantBuilder,
            InternalContentBuilder contentBuilder) {

        // Check to see whether the content is allowed.
        ContentType contentType;
        if (contentBuilder == null) {
            contentType = null;
        } else {
            contentType = contentBuilder.getContentType();
        }

        boolean allowed = allowableContentTypes.contains(contentType);

        // First check to see whether the content is null.
        if (contentBuilder == null) {
            if (!allowed) {
                // Content must be specified.
                String param1 =
                    sourceLocation.getSourceDocumentName() == null ? "" :
                    sourceLocation.getSourceDocumentName();

                String param2 =
                    variantBuilder.getVariantType()==null ? "UNKNOWN" :
                    variantBuilder.getVariantType().toString();

                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                            PolicyMessages.UNSPECIFIED_CONTENT,
                            param1, param2));
            }
        } else {

            if (allowed) {
                // The content type is allowed so validate it.
                contentBuilder.validate(context);
            } else {
                // The content type is not supported so report it.
                String param1 =
                    sourceLocation.getSourceDocumentName() == null ? "" :
                    sourceLocation.getSourceDocumentName();

                String param2 =
                    variantBuilder.getVariantType()==null ? "UNKNOWN" :
                    variantBuilder.getVariantType().toString();

                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.UNSUPPORTED_CONTENT_TYPE,
                                new Object[]{ param1, param2, contentType }));
            }
        }
    }
}
