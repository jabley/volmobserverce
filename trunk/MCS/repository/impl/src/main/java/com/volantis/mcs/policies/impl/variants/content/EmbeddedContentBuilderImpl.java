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

import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.EmbeddedContent;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;

public class EmbeddedContentBuilderImpl
        extends AbstractBuilder
        implements EmbeddedContentBuilder, InternalContentBuilder {

    private EmbeddedContent embeddedContent;

    private String data;

    public EmbeddedContentBuilderImpl() {
        this(null);
    }

    public EmbeddedContentBuilderImpl(EmbeddedContent embeddedContent) {
        if (embeddedContent != null) {
            this.embeddedContent = embeddedContent;
            data = embeddedContent.getData();
        }
    }

    public Content getContent() {
        return getEmbeddedContent();
    }

    public EmbeddedContent getEmbeddedContent() {
        if (embeddedContent == null) {
            // Make sure only valid instances are built.
            validate();
            embeddedContent = new EmbeddedContentImpl(this);
        }

        return embeddedContent;
    }

    protected Object getBuiltObject() {
        return getEmbeddedContent();
    }

    protected void clearBuiltObject() {
        embeddedContent = null;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        if (!equals(this.data, data)) {
            stateChanged();
        }

        this.data = data;
    }

    public void validate(ValidationContext context) {
        // The text must be specified
        if (data == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.TEXT_UNSPECIFIED));
        }
    }

    public void accept(ContentBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.EMBEDDED;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof EmbeddedContentBuilderImpl) ?
                equalsSpecific((EmbeddedContentBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(EmbeddedContentBuilderImpl other) {
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
