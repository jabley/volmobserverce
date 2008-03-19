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
import com.volantis.mcs.policies.variants.metadata.InternalMetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;

import java.util.HashSet;
import java.util.Set;

public class VariantMetaDataValidator {

    /**
     * The set of allowable metadata types.
     */
    private final Set allowableMetaDataTypes;

    public VariantMetaDataValidator(MetaDataType[] types) {
        allowableMetaDataTypes = new HashSet();
        for (int i = 0; i < types.length; i++) {
            MetaDataType type = types[i];
            allowableMetaDataTypes.add(type);
        }
    }

    /**
     * Validate that the meta data is allowed.
     *
     * <p>This assumes that the caller has pushed an object on the context.</p>
     *
     * @param context The meta data within which the validation occurs.
     * @param sourceLocation
     * @param variantBuilder The variants being validated.
     * @param metaDataBuilder The meta data being validated.
     */
    public void validateMetaData(
            ValidationContext context, SourceLocation sourceLocation,
            VariantBuilder variantBuilder,
            InternalMetaDataBuilder metaDataBuilder) {

        // Check to see whether the meta data is allowed.
        MetaDataType metaDataType;
        if (metaDataBuilder == null) {
            metaDataType = null;
        } else {
            metaDataType = metaDataBuilder.getMetaDataType();
        }

        boolean allowed = allowableMetaDataTypes.contains(metaDataType);

        // First check to see whether the meta data is null.
        if (metaDataBuilder == null) {
            if (!allowed) {
                String param1 =
                    sourceLocation.getSourceDocumentName() == null ? "" :
                    sourceLocation.getSourceDocumentName();

                String param2 =
                    variantBuilder.getVariantType()==null ? "UNKNOWN" :
                    variantBuilder.getVariantType().toString();

                // meta data must be specified.
                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.UNSPECIFIED_META_DATA,
                                param1, param2));
            }
        } else {

            if (allowed) {
                // The meta data type is allowed so validate it.
                metaDataBuilder.validate(context);
            } else {
                String param1 =
                    sourceLocation.getSourceDocumentName() == null ? "" :
                    sourceLocation.getSourceDocumentName();

                String param2 =
                    variantBuilder.getVariantType()==null ? "UNKNOWN" :
                    variantBuilder.getVariantType().toString();

                // The meta data type is not supported so report it.
                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.UNSUPPORTED_META_DATA_TYPE,
                                new Object[]{param1, param2, metaDataType}));
            }
        }
    }
}
