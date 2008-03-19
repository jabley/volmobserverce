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

import com.volantis.mcs.policies.impl.variants.metadata.AbstractMetaDataSingleEncoding;
import com.volantis.mcs.policies.variants.metadata.MetaDataBuilder;
import com.volantis.mcs.policies.variants.metadata.MetaDataType;
import com.volantis.mcs.policies.variants.metadata.MetaDataVisitor;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.script.ScriptMetaData;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;

public class ScriptMetaDataImpl
        extends AbstractMetaDataSingleEncoding
        implements ScriptMetaData {

    private final ScriptEncoding scriptEncoding;
    private final String characterSet;

    public ScriptMetaDataImpl(ScriptMetaDataBuilder builder) {

        scriptEncoding = builder.getScriptEncoding();
        characterSet = builder.getCharacterSet();
    }

    public MetaDataBuilder getMetaDataBuilder() {
        return getScriptMetaDataBuilder();
    }

    public ScriptMetaDataBuilder getScriptMetaDataBuilder() {
        return new ScriptMetaDataBuilderImpl(this);
    }

    public void accept(MetaDataVisitor visitor) {
        visitor.visit(this);
    }

    public Encoding getEncoding() {
        return getScriptEncoding();
    }

    public ScriptEncoding getScriptEncoding() {
        return scriptEncoding;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public MetaDataType getMetaDataType() {
        return MetaDataType.SCRIPT;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ScriptMetaDataImpl) ?
                equalsSpecific((ScriptMetaDataImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ScriptMetaDataImpl other) {
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
