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

package com.volantis.mcs.policies.impl.variants.metadata;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.PolicyMessages;
import com.volantis.mcs.policies.variants.metadata.Encoding;

public abstract class AbstractMetaDataSingleEncodingBuilder
        extends AbstractMetaDataBuilder {

    public AbstractMetaDataSingleEncodingBuilder() {
    }

    public abstract Encoding getEncoding();

    public final void validate(ValidationContext context) {

        Step step = context.pushPropertyStep(PolicyModel.ENCODING);

        if (getEncoding() == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.ENCODING_UNSPECIFIED));
        }

        context.popStep(step);

        validateSingleEncodingImpl(context);
    }

    protected abstract void validateSingleEncodingImpl(
            ValidationContext context);

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AbstractMetaDataSingleEncodingBuilder) ?
                equalsSpecific((AbstractMetaDataSingleEncodingBuilder) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AbstractMetaDataSingleEncodingBuilder other) {
        return super.equalsSpecific(other) &&
                equals(getEncoding(), other.getEncoding());
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, getEncoding());
        return result;
    }
}
