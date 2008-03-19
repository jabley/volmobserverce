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

package com.volantis.mcs.themes.mappers;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.AllowableKeywords;

public class StandardKeywordMapper
        extends AbstractKeywordMapper {

    private final AllowableKeywords allowableKeywords;

    public StandardKeywordMapper(AllowableKeywords allowableKeywords) {
        this.allowableKeywords = allowableKeywords;
    }

    public String getExternalString(StyleKeyword keyword) {
        if (allowableKeywords.isValidKeyword(keyword)) {
            return keyword.getStandardCSS();
        } else {
            return null;
        }
    }

    public String getExternalString(StyleValue potentialKeyword) {
        if (potentialKeyword instanceof StyleKeyword) {
            return getExternalString((StyleKeyword) potentialKeyword);
        }

        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
