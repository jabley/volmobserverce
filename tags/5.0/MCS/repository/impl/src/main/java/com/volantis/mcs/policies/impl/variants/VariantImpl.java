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

package com.volantis.mcs.policies.impl.variants;

import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.InternalVariant;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.Selection;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;

public class VariantImpl
        extends EqualsHashCodeBase
        implements InternalVariant {

    private final VariantType variantType;

    private final Selection selection;
    private final MetaData metaData;
    private final Content content;

    public VariantImpl(VariantBuilder builder) {
        variantType = builder.getVariantType();
        SelectionBuilder selectionBuilder = builder.getSelectionBuilder();
        if (selectionBuilder == null) {
            selection = null;
        } else {
            selection = selectionBuilder.getSelection();
        }
        MetaDataBuilder metaDataBuilder = builder.getMetaDataBuilder();
        if (metaDataBuilder == null) {
            metaData = null;
        } else {
            metaData = metaDataBuilder.getMetaData();
        }
        ContentBuilder contentBuilder = builder.getContentBuilder();
        if (contentBuilder == null) {
            content = null;
        } else {
            content = contentBuilder.getContent();
        }
    }

    public VariantBuilder getVariantBuilder() {
        return new VariantBuilderImpl(this);
    }

    public VariantType getVariantType() {
        return variantType;
    }

    public Selection getSelection() {
        return selection;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public Content getContent() {
        return content;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof VariantImpl) ?
                equalsSpecific((VariantImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(VariantImpl other) {
        return super.equalsSpecific(other) &&
                equals(variantType, other.variantType) &&
                equals(selection, other.selection) &&
                equals(metaData, other.metaData) &&
                equals(content, other.content);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, variantType);
        result = hashCode(result, selection);
        result = hashCode(result, metaData);
        result = hashCode(result, content);
        return result;
    }

}
