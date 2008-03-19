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
import com.volantis.mcs.policies.variants.content.AutoURLSequence;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;

public class AutoURLSequenceBuilderImpl
        extends BaseURLRelativeBuilderImpl
        implements AutoURLSequenceBuilder, InternalContentBuilder {

    private AutoURLSequence autoURLSequence;

    private int sequenceSize;

    private String urlTemplate;

    public AutoURLSequenceBuilderImpl(
            AutoURLSequence autoURLSequence) {
        super(autoURLSequence);

        if (autoURLSequence != null) {
            this.autoURLSequence = autoURLSequence;
            sequenceSize = autoURLSequence.getSequenceSize();
            urlTemplate = autoURLSequence.getURLTemplate();
        }
    }

    public AutoURLSequenceBuilderImpl() {
        this(null);
    }

    public Content getContent() {
        return getAutomaticURLContentSequence();
    }

    public AutoURLSequence getAutomaticURLContentSequence() {
        if (autoURLSequence == null) {
            // Make sure only valid instances are built.
            validate();
            autoURLSequence = new AutoURLSequenceImpl(this);
        }

        return autoURLSequence;
    }

    protected Object getBuiltObject() {
        return getAutomaticURLContentSequence();
    }

    protected void clearBuiltObject() {
        autoURLSequence = null;
    }

    public int getSequenceSize() {
        return sequenceSize;
    }

    public void setSequenceSize(int sequenceSize) {
        if (!equals(this.sequenceSize, sequenceSize)) {
            stateChanged();
        }

        this.sequenceSize = sequenceSize;
    }

    public String getURLTemplate() {
        return urlTemplate;
    }

    public void setURLTemplate(String urlTemplate) {
        if (!equals(this.urlTemplate, urlTemplate)) {
            stateChanged();
        }

        this.urlTemplate = urlTemplate;
    }

    public void accept(ContentBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public void validate(ValidationContext context) {

        super.validate(context);

        // The url template must be specified.
        if (urlTemplate == null) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(
                            PolicyMessages.URL_TEMPLATE_UNSPECIFIED));
        } else {
            // Make sure that the template contains the correct syntax.
            int startIndex = -1;
            int endIndex = -1;
            boolean errorFound = false;
            for (int i = 0; !errorFound && i < urlTemplate.length(); i += 1) {
                char c = urlTemplate.charAt(i);
                if (c == '{') {
                    if (startIndex == -1) {
                        startIndex = i;
                    } else {
                        // Already seen a start of the block so it is an error.
                        errorFound = true;
                    }
                } else if (c == '}') {
                    if (endIndex == -1) {
                        endIndex = i;
                    } else {
                        // Already seen an end of the block so it is an error.
                        errorFound = true;
                    }
                }
            }

            if (!errorFound) {
                if (startIndex == -1) {
                    // No start block seen.
                    errorFound = true;
                } else if (endIndex == -1) {
                    // No end block seen.
                    errorFound = true;
                } else if (startIndex > endIndex) {
                    // Start seen after end.
                    errorFound = true;
                }
            }

            if (errorFound) {
                context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                        context.createMessage(
                                PolicyMessages.URL_TEMPLATE_ERROR,
                                urlTemplate));
            }
        }

        // The sequence size must be greater than 0.
        if (sequenceSize <= 0) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage(PolicyMessages.SEQUENCE_SIZE_MINIMUM,
                            new Integer(sequenceSize)));
        }
    }

    public ContentType getContentType() {
        return ContentType.AUTOMATIC_URL_SEQUENCE;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof AutoURLSequenceBuilderImpl) ?
                equalsSpecific((AutoURLSequenceBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(AutoURLSequenceBuilderImpl other) {
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
