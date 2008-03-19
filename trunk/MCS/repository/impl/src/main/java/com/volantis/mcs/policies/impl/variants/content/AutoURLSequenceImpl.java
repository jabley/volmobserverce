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

import com.volantis.mcs.policies.variants.content.AutoURLSequence;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.InternalContent;
import com.volantis.mcs.policies.variants.content.ContentVisitor;

public class AutoURLSequenceImpl
        extends BaseURLRelativeImpl
        implements AutoURLSequence, InternalContent {

    private int sequenceSize;

    private String urlTemplate;

    public AutoURLSequenceImpl(
            AutoURLSequenceBuilder builder) {
        super(builder);

        sequenceSize = builder.getSequenceSize();
        urlTemplate = builder.getURLTemplate();
    }

    public ContentBuilder getContentBuilder() {
        return getAutomaticURLContentSequenceBuilder();
    }

    public AutoURLSequenceBuilder getAutomaticURLContentSequenceBuilder() {
        return new AutoURLSequenceBuilderImpl(this);
    }

    public int getSequenceSize() {
        return sequenceSize;
    }

    public String getURLTemplate() {
        return urlTemplate;
    }

    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.AUTOMATIC_URL_SEQUENCE;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AutoURLSequenceImpl) ?
                equalsSpecific((AutoURLSequenceImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AutoURLSequenceImpl other) {
        return super.equalsSpecific(other) &&
                equals(urlTemplate, other.urlTemplate) &&
                equals(sequenceSize, other.sequenceSize);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, urlTemplate);
        result = hashCode(result, sequenceSize);
        return result;
    }
}
