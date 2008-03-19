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

import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.image.GenericImageSelection;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.InternalSelection;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionType;
import com.volantis.mcs.policies.variants.selection.SelectionVisitor;

public class GenericImageSelectionImpl
        extends EqualsHashCodeBase
        implements GenericImageSelection, InternalSelection {

    private final int widthHint;

    public GenericImageSelectionImpl(GenericImageSelectionBuilder builder) {
        widthHint = builder.getWidthHint();
    }

    public SelectionBuilder getSelectionBuilder() {
        return getGenericImageSelectionBuilder();
    }

    public GenericImageSelectionBuilder getGenericImageSelectionBuilder() {
        return new GenericImageSelectionBuilderImpl(this);
    }

    // Javadoc inherited.
    public int getWidthHint() {
        return widthHint;
    }

    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    public SelectionType getSelectionType() {
        return SelectionType.GENERIC_IMAGE;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof GenericImageSelectionImpl) ?
                equalsSpecific((GenericImageSelectionImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(GenericImageSelectionImpl other) {
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
