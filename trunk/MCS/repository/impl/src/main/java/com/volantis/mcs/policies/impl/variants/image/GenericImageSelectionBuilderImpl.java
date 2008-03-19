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

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.impl.validation.ValidationHelper;
import com.volantis.mcs.policies.variants.image.GenericImageSelection;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.InternalSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.Selection;
import com.volantis.mcs.policies.variants.selection.SelectionBuilderVisitor;
import com.volantis.mcs.policies.variants.selection.SelectionType;

public class GenericImageSelectionBuilderImpl
        extends AbstractBuilder
        implements GenericImageSelectionBuilder, InternalSelectionBuilder {

    private GenericImageSelection genericImageSelection;

    private int widthHint;

    public GenericImageSelectionBuilderImpl() {
        this(null);
    }

    public GenericImageSelectionBuilderImpl(
            GenericImageSelection genericImageSelection) {
        if (genericImageSelection != null) {
            this.genericImageSelection = genericImageSelection;
            widthHint = genericImageSelection.getWidthHint();
        }
    }

    public Selection getSelection() {
        return getGenericImageSelection();
    }

    public GenericImageSelection getGenericImageSelection() {
        if (genericImageSelection == null) {
            // Make sure only valid instances are built.
            validate();
            genericImageSelection = new GenericImageSelectionImpl(this);
        }

        return genericImageSelection;
    }

    protected Object getBuiltObject() {
        return getGenericImageSelection();
    }

    protected void clearBuiltObject() {
        genericImageSelection = null;
    }

    // Javadoc inherited.
    public int getWidthHint() {
        return widthHint;
    }

    // Javadoc inherited.
    public void setWidthHint(int widthHint) {
        if (!equals(this.widthHint, widthHint)) {
            stateChanged();
        }

        this.widthHint = widthHint;
    }

    public void accept(SelectionBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public void validate(ValidationContext context) {
        Step step;

        // widthHint
        step = context.pushPropertyStep(PolicyModel.WIDTH_HINT);
        ValidationHelper.checkPercentage(context, sourceLocation, widthHint,
                PolicyMessages.WIDTH_HINT_RANGE);
        context.popStep(step);
    }

    public SelectionType getSelectionType() {
        return SelectionType.GENERIC_IMAGE;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof GenericImageSelectionBuilderImpl) ?
                equalsSpecific((GenericImageSelectionBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(GenericImageSelectionBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(widthHint, other.widthHint);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, widthHint);
        return result;
    }
}
