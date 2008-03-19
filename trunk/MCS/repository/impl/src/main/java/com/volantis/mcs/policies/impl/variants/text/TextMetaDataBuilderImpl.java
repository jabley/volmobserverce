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

package com.volantis.mcs.policies.impl.variants.text;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataSingleEncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaData;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;

public class TextMetaDataBuilderImpl
        extends AbstractMetaDataSingleEncodingBuilder
        implements TextMetaDataBuilder {

    private TextMetaData textMetaData;

    private TextEncoding textEncoding;
    private String language;

    public TextMetaDataBuilderImpl(TextMetaData textMetaData) {

        if (textMetaData != null) {
            this.textMetaData = textMetaData;
            textEncoding = textMetaData.getTextEncoding();
            language = textMetaData.getLanguage();
        }
    }

    public TextMetaDataBuilderImpl() {
        this(null);
    }

    public MetaData getMetaData() {
        return getTextMetaData();
    }

    public TextMetaData getTextMetaData() {
        if (textMetaData == null) {
            // Make sure only valid instances are built.
            validate();
            textMetaData = new TextMetaDataImpl(this);
        }

        return textMetaData;
    }

    protected void clearBuiltObject() {
        textMetaData = null;
    }

    public void accept(MetaDataBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getTextEncoding();
    }

    public TextEncoding getTextEncoding() {
        return textEncoding;
    }

    public void setTextEncoding(TextEncoding textEncoding) {
        if (!equals(this.textEncoding, textEncoding)) {
            stateChanged();
        }
        
        this.textEncoding = textEncoding;
    }

    protected void validateSingleEncodingImpl(ValidationContext context) {
        // Nothing to validate.
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.TEXT;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (!equals(this.language, language)) {
            stateChanged();
        }

        this.language = language;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof TextMetaDataBuilderImpl) ?
                equalsSpecific((TextMetaDataBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(TextMetaDataBuilderImpl other) {
        return super.equalsSpecific(other);
    }

    // Javadoc inherited.
    public int hashCode() {
        return super.hashCode();
    }
}
