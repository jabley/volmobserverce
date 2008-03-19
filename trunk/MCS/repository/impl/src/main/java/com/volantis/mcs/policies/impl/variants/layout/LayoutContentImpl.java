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

package com.volantis.mcs.policies.impl.variants.layout;

import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.ContentVisitor;
import com.volantis.mcs.policies.variants.content.InternalContent;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContent;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;

/**
 * For now the layout content simply wraps a device layout.
 */
public class LayoutContentImpl
        extends EqualsHashCodeBase
        implements InternalLayoutContent, InternalContent {

    private final Layout layout;

    public LayoutContentImpl(InternalLayoutContentBuilder builder) {
        layout = builder.getLayout();
    }

    public ContentBuilder getContentBuilder() {
        return getInternalLayoutContentBuilder();
    }

    public InternalLayoutContentBuilder getInternalLayoutContentBuilder() {
        return new LayoutContentBuilderImpl(this);
    }

    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.LAYOUT;
    }

    public Layout getLayout() {
        return layout;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof LayoutContentImpl) ?
                equalsSpecific((LayoutContentImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    private boolean equalsSpecific(LayoutContentImpl content) {
        // todo implement in terms of device layout, for now just make sure
        // that they either both have, or neither have a device layout.
        return super.equalsSpecific(content) &&
                (layout == null
                ? content.layout == null
                : layout.equals(content.layout));
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        // todo implement in terms of device layout.
        return result;
    }
}
