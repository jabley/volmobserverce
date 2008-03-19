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

import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.selection.InternalSelection;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionType;
import com.volantis.mcs.policies.variants.selection.SelectionVisitor;
import com.volantis.mcs.policies.variants.selection.TargetedSelection;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;

import java.util.Collections;
import java.util.List;

public class TargetedSelectionImpl
        extends EqualsHashCodeBase
        implements TargetedSelection, InternalSelection {

    private final List deviceReferences;

    private final List categoryReferences;

    public TargetedSelectionImpl(TargetedSelectionBuilder builder) {
        deviceReferences = Collections.unmodifiableList(builder.getModifiableDeviceReferences());
        categoryReferences = Collections.unmodifiableList(builder.getModifiableCategoryReferences());
    }

    public SelectionBuilder getSelectionBuilder() {
        return getTargetedSelectionBuilder();
    }

    public TargetedSelectionBuilder getTargetedSelectionBuilder() {
        return new TargetedSelectionBuilderImpl(this);
    }

    public List getDeviceReferences() {
        return deviceReferences;
    }

    public List getCategoryReferences() {
        return categoryReferences;
    }

    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    public SelectionType getSelectionType() {
        return SelectionType.TARGETED;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof TargetedSelectionImpl) ?
                equalsSpecific((TargetedSelectionImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(TargetedSelectionImpl other) {
        return super.equalsSpecific(other) &&
                equals(deviceReferences, other.deviceReferences) &&
                equals(categoryReferences, other.categoryReferences);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, deviceReferences);
        result = hashCode(result, categoryReferences);
        return result;
    }
}
