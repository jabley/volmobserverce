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

package com.volantis.mcs.policies.impl.variants.selection;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.integrity.DefinitionScope;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.variants.selection.DefaultSelection;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.Selection;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionType;
import com.volantis.mcs.policies.variants.selection.InternalSelection;
import com.volantis.mcs.policies.variants.selection.SelectionVisitor;
import com.volantis.mcs.policies.variants.selection.InternalSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilderVisitor;

public class DefaultSelectionImpl
        extends AbstractBuilder
        implements DefaultSelection, InternalSelection,
        DefaultSelectionBuilder, InternalSelectionBuilder {

    private static final Object SCOPE_IDENTIFIER = new Object();

    public SelectionBuilder getSelectionBuilder() {
        return this;
    }

    public Selection getSelection() {
        return this;
    }

    public void accept(SelectionBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    protected Object getBuiltObject() {
        return this;
    }

    protected void clearBuiltObject() {
    }

    public void validate(ValidationContext context) {

        DefinitionScope scope = context.getDefinitionScope(
                PolicyModel.DEFAULT_SELECTION);
        if (scope != null) {
            scope.define(context, sourceLocation, SCOPE_IDENTIFIER);
        }
    }

    public SelectionType getSelectionType() {
        return SelectionType.DEFAULT;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof DefaultSelectionImpl) ?
                equalsSpecific((DefaultSelectionImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(DefaultSelectionImpl other) {
        return super.equalsSpecific(other);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, DefaultSelectionImpl.class);
        return result;
    }
}
