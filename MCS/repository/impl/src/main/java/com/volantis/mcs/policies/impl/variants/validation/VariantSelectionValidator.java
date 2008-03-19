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
import com.volantis.mcs.policies.variants.selection.InternalSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionType;

import java.util.HashSet;
import java.util.Set;

public class VariantSelectionValidator {

    /**
     * The set of allowable selection types.
     */
    private final Set allowableSelectionTypes;

    public VariantSelectionValidator(SelectionType[] types) {
        allowableSelectionTypes = new HashSet();
        for (int i = 0; i < types.length; i++) {
            SelectionType type = types[i];
            allowableSelectionTypes.add(type);
        }
    }

    /**
     * Validate that the selectionBuilder is allowed.
     *
     * <p>This assumes that the caller has pushed an object on the context.</p>
     *
     * @param context The selectionBuilder within which the validation occurs.
     @param sourceLocation
     @param variantBuilder The variants being validated.
     @param selectionBuilder The selectionBuilder being validated.
     */
    public void validateSelection(
            ValidationContext context, SourceLocation sourceLocation,
            VariantBuilder variantBuilder,
            InternalSelectionBuilder selectionBuilder) {

        // Check to see whether the selectionBuilder is allowed.
        SelectionType selectionType;
        if (selectionBuilder == null) {
            selectionType = null;
        } else {
            selectionType = selectionBuilder.getSelectionType();
        }

        boolean allowed = allowableSelectionTypes.contains(selectionType);

        // First check to see whether the selectionBuilder is null.
        if (selectionBuilder == null) {
            if (!allowed) {
                // Selection must be specified.
                String param1 =
                    sourceLocation.getSourceDocumentName() == null ? "" :
                    sourceLocation.getSourceDocumentName();

                String param2 =
                    variantBuilder.getVariantType()==null ? "UNKNOWN" :
                    variantBuilder.getVariantType().toString();

                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.UNSPECIFIED_SELECTION,
                                param1, param2));
            }
        } else {

            if (allowed) {
                // The selectionBuilder type is allowed so validate it.
                selectionBuilder.validate(context);
            } else {
                // The selectionBuilder type is not supported so report it.
                String param1 =
                    sourceLocation.getSourceDocumentName() == null ? "" :
                    sourceLocation.getSourceDocumentName();

                String param2 =
                    variantBuilder.getVariantType()==null ? "UNKNOWN" :
                    variantBuilder.getVariantType().toString();

                String param3 =
                    selectionType == null ? "UNKNOWN" :
                    selectionType.toString();

                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.UNSUPPORTED_SELECTION_TYPE,
                                new Object[]{ param1, param2, param3 }));
            }
        }
    }
}
