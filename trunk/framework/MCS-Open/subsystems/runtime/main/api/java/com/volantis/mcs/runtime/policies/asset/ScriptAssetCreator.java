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

package com.volantis.mcs.runtime.policies.asset;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.assets.SubstantiveAsset;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.compatibility.EnumerationConverter;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.script.ScriptMetaData;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;

import java.util.Map;
import java.util.Iterator;

public class ScriptAssetCreator
        extends AbstractAssetCreator {

    private static final Map ENCODING =
        EnumerationConverter.SCRIPT_ENCODING_TO_LANGUAGE;

    public RepositoryObject createOldObject(
            ActivatedVariablePolicy policy, Variant variant,
            InternalDevice device) {

        // Convert the variant into an asset.
        ScriptMetaData metaData = (ScriptMetaData) variant.getMetaData();

        ScriptAsset asset = new ScriptAsset();

        setAssetIdentity(policy, asset);

        final ScriptEncoding scriptEncoding = metaData.getScriptEncoding();
        if (scriptEncoding != null) {
            asset.setProgrammingLanguage((String) ENCODING.get(scriptEncoding));
            // use the first mime type if there is any
            final Iterator iter = scriptEncoding.mimeTypes();
            if (iter.hasNext()) {
                asset.setMimeType((String) iter.next());
            }
        }
        asset.setCharacterSet(metaData.getCharacterSet());

        setValue(asset, variant);

        return asset;
    }

    protected void makeLiteralValue(SubstantiveAsset asset) {
        ((ScriptAsset) asset).setValueType(ScriptAsset.LITERAL);
    }
}
