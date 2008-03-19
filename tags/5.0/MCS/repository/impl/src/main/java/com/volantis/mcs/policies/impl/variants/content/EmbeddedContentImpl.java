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

package com.volantis.mcs.policies.impl.variants.content;

import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.ContentVisitor;
import com.volantis.mcs.policies.variants.content.EmbeddedContent;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.InternalContent;

public class EmbeddedContentImpl
        extends EqualsHashCodeBase
        implements EmbeddedContent, InternalContent {

    private final String data;

    public EmbeddedContentImpl(EmbeddedContentBuilder builder) {
        data = builder.getData();
    }

    public ContentBuilder getContentBuilder() {
        return getEmbeddedContentBuilder();
    }

    public EmbeddedContentBuilder getEmbeddedContentBuilder() {
        return new EmbeddedContentBuilderImpl(this);
    }

    public String getData() {
        return data;
    }

    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.EMBEDDED;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof EmbeddedContentImpl) ?
                equalsSpecific((EmbeddedContentImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(EmbeddedContentImpl other) {
        return super.equalsSpecific(other) &&
                equals(data, other.data);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, data);
        return result;
    }
}
