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

import com.volantis.mcs.assets.SubstantiveAsset;
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.compatibility.EnumerationConverter;
import com.volantis.mcs.policies.compatibility.IntObjectMap;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.text.TextMetaData;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;

public class TextAssetCreator
        extends AbstractAssetCreator {

    private static final IntObjectMap ENCODING = EnumerationConverter.TEXT_ENCODING;

    public RepositoryObject createOldObject(
            ActivatedVariablePolicy policy, Variant variant,
            InternalDevice device) {

        // Convert the variant into an asset.
        TextMetaData metaData = (TextMetaData) variant.getMetaData();

        TextAsset asset = new TextAsset();

        setAssetIdentity(policy, asset);

        asset.setEncoding(ENCODING.get(metaData.getTextEncoding()));
        asset.setLanguage(metaData.getLanguage());

        setValue(asset, variant);

        return asset;
    }

    protected void makeLiteralValue(SubstantiveAsset asset) {
        ((TextAsset) asset).setValueType(TextAsset.LITERAL);
    }
}
