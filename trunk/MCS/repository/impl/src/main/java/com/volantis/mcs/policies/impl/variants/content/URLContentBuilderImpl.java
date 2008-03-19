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
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.URLContent;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;

public class URLContentBuilderImpl
        extends BaseURLRelativeBuilderImpl
        implements URLContentBuilder, InternalContentBuilder {

    private URLContent urlContent;

    private String url;

    public URLContentBuilderImpl(URLContent urlContent) {
        super(urlContent);

        if (urlContent != null) {
            this.urlContent = urlContent;
            url = urlContent.getURL();
        }
    }

    public URLContentBuilderImpl() {
        this(null);
    }

    public Content getContent() {
        return getURLContent();
    }

    public URLContent getURLContent() {
        if (urlContent == null) {
            // Make sure only valid instances are built.
            validate();
            urlContent = new URLContentImpl(this);
        }

        return urlContent;
    }

    protected Object getBuiltObject() {
        return getURLContent();
    }

    protected void clearBuiltObject() {
        urlContent = null;
    }

    // Javadoc inherited.
    public String getURL() {
        return url;
    }

    // Javadoc inherited.
    public void setURL(String url) {
        if (!equals(this.url, url)) {
            stateChanged();
        }

        this.url = url;
    }

    public void validate(ValidationContext context) {

        super.validate(context);
        
        // The url must be specified and must not be empty
        if (url == null || url.length() == 0) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.URL_UNSPECIFIED));
        }
    }

    public void accept(ContentBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.URL;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof URLContentBuilderImpl) ?
                equalsSpecific((URLContentBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(URLContentBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(url, other.url);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, url);
        return result;
    }
}
