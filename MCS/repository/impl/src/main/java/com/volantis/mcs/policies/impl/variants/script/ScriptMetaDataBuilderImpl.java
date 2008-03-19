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

package com.volantis.mcs.policies.impl.variants.script;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataSingleEncodingBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilderVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.policies.variants.script.ScriptMetaData;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;

public class ScriptMetaDataBuilderImpl
        extends AbstractMetaDataSingleEncodingBuilder
        implements ScriptMetaDataBuilder {

    private ScriptMetaData scriptMetaData;

    private ScriptEncoding scriptEncoding;
    private String characterSet;

    public ScriptMetaDataBuilderImpl(ScriptMetaData scriptMetaData) {

        if (scriptMetaData != null) {
            this.scriptMetaData = scriptMetaData;
            characterSet = scriptMetaData.getCharacterSet();
            scriptEncoding = scriptMetaData.getScriptEncoding();
        }
    }

    public ScriptMetaDataBuilderImpl() {
        this(null);
    }

    public MetaData getMetaData() {
        return getScriptMetaData();
    }

    public ScriptMetaData getScriptMetaData() {
        if (scriptMetaData == null) {
            // Make sure only valid instances are built.
            validate();
            scriptMetaData = new ScriptMetaDataImpl(this);
        }

        return scriptMetaData;
    }

    protected void clearBuiltObject() {
        scriptMetaData = null;
    }

    public void accept(MetaDataBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getScriptEncoding();
    }

    public ScriptEncoding getScriptEncoding() {
        return scriptEncoding;
    }

    public void setScriptEncoding(ScriptEncoding scriptEncoding) {
        if (!equals(this.scriptEncoding, scriptEncoding)) {
            stateChanged();
        }
        
        this.scriptEncoding = scriptEncoding;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        if (!equals(this.characterSet, characterSet)) {
            stateChanged();
        }

        this.characterSet = characterSet;
    }

    protected void validateSingleEncodingImpl(ValidationContext context) {
        // Nothing to validate, the character set is optional and is a
        // pretty arbitrary structure.
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.SCRIPT;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ScriptMetaDataBuilderImpl) ?
                equalsSpecific((ScriptMetaDataBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ScriptMetaDataBuilderImpl other) {
        return super.equalsSpecific(other) &&
                equals(characterSet, other.characterSet);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        result = hashCode(result, characterSet);
        return result;
    }
}
