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
package com.volantis.mcs.policies.impl.variants.selection;

import com.volantis.mcs.policies.variants.selection.SelectionType;
import com.volantis.mcs.policies.variants.VariantType;

import java.util.Map;
import java.util.HashMap;

/**
 * Class providing access to the selection types valid for a particular
 * variant type.
 */
public class VariantSelectionTypes {
    private static Map variantTypeMaps = new HashMap();

    static {
        SelectionType[] common = new SelectionType[] {
            SelectionType.DEFAULT,
            SelectionType.TARGETED,
        };

        SelectionType[] commonWithEncoding = new SelectionType[] {
            SelectionType.DEFAULT,
            SelectionType.ENCODING,
            SelectionType.TARGETED,
        };

        SelectionType[] image = new SelectionType[]{
            SelectionType.TARGETED,
            SelectionType.DEFAULT,
            SelectionType.GENERIC_IMAGE,
        };

        SelectionType[] chart = new SelectionType[] {
            SelectionType.DEFAULT,
        };

        variantTypeMaps.put(VariantType.AUDIO, commonWithEncoding);
        variantTypeMaps.put(VariantType.CHART, chart);
        variantTypeMaps.put(VariantType.IMAGE, image);
        variantTypeMaps.put(VariantType.LAYOUT, common);
        variantTypeMaps.put(VariantType.LINK, common);
        variantTypeMaps.put(VariantType.NULL, common);
        variantTypeMaps.put(VariantType.SCRIPT, common);
        variantTypeMaps.put(VariantType.TEXT, common);
        variantTypeMaps.put(VariantType.THEME, common);
        variantTypeMaps.put(VariantType.VIDEO, commonWithEncoding);
    }

    public static SelectionType[] getValidSelectionTypes(VariantType variantType) {
        return (SelectionType[]) variantTypeMaps.get(variantType);
    }
}
