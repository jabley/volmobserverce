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
import com.volantis.mcs.layouts.model.LayoutModel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContent;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;

/**
 * For now the layout content simply wraps a device layout.
 */
public class LayoutContentBuilderImpl
        extends AbstractBuilder
        implements InternalLayoutContentBuilder, InternalContentBuilder {

    private Layout layout;
    private InternalLayoutContent layoutContent;

    public LayoutContentBuilderImpl() {
        this(null);
    }

    public LayoutContentBuilderImpl(InternalLayoutContent layoutContent) {
        if (layoutContent != null) {
            this.layoutContent = layoutContent;
            layout = layoutContent.getLayout();
        }
    }

    public Content getContent() {
        return getInternalLayoutContent();
    }

    public InternalLayoutContent getInternalLayoutContent() {
        if (layoutContent == null) {
            // Make sure only valid instances are built.
            validate();
            layoutContent = new LayoutContentImpl(this);
        }

        return layoutContent;
    }

    protected Object getBuiltObject() {
        return getInternalLayoutContent();
    }

    protected void clearBuiltObject() {
        layoutContent = null;
    }

    public void accept(ContentBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.LAYOUT;
    }

    public void validate(ValidationContext context) {
        // Validate the layout contents.
        if (layout != null) {
            Step step = context.pushPropertyStep(LayoutModel.LAYOUT);
            layout.validate(context);
            context.popStep(step);
        }
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        if (!equals(this.layout, layout)) {
            stateChanged();
        }

        this.layout = layout;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof LayoutContentBuilderImpl) ?
                equalsSpecific((LayoutContentBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    private boolean equalsSpecific(LayoutContentBuilderImpl content) {
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
