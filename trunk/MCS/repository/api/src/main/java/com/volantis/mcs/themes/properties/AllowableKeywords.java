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

package com.volantis.mcs.themes.properties;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.values.StyleKeywords;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllowableKeywords {

    private final List allowableKeywords;

    private final Map string2Keyword;

    private final Set keywords;

    public AllowableKeywords(List allowableKeywords) {
        this.allowableKeywords = Collections.unmodifiableList(allowableKeywords);
        string2Keyword = new HashMap();
        keywords = new HashSet();
        for (int i = 0; i < allowableKeywords.size(); i++) {
            StyleKeyword keyword = (StyleKeyword) allowableKeywords.get(i);
            string2Keyword.put(keyword.getName(), keyword);
            keywords.add(keyword);
        }
    }

    protected static StyleKeyword add(List list, StyleKeyword keyword) {
        if (list.contains(keyword)) {
            throw new IllegalArgumentException("Duplicate keywords " + keyword);
        }

        list.add(keyword);
        return keyword;
    }

    public boolean isValidKeyword(String string) {
        // Keywords should not be case sensitive.
        StyleKeyword keyword = StyleKeywords.getKeywordByName(
                string.toLowerCase());
        return isValidKeyword(keyword);
    }

    public boolean isValidKeyword(StyleKeyword keyword) {
        return allowableKeywords.contains(keyword);
    }

    public List getKeywords() {
        return allowableKeywords;
    }

    public StyleKeyword getKeyword(String identifier) {
        // Keywords should not be case sensitive.
        return (StyleKeyword) string2Keyword.get(identifier.toLowerCase());
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
