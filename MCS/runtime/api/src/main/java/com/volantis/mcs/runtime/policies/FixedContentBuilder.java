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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.InternalBuilder;
import com.volantis.mcs.policies.InternalBuilderContainer;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;

public class FixedContentBuilder
        implements InternalBuilder, InternalContentBuilder {

    private final Content activated;

    public FixedContentBuilder(Content activated) {
        this.activated = activated;
    }

    public void accept(ContentBuilderVisitor visitor) {
        throw new UnsupportedOperationException();
    }

    public void validate(ValidationContext context) {
        // nothing to do.
    }

    public void setContainer(InternalBuilderContainer container) {
    }

    public Content getContent() {
        return activated;
    }

    public ContentType getContentType() {
        return activated.getContentType();
    }
}
