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
 * $Header: /src/voyager/com/volantis/mcs/themes/mappers/KeywordMapper.java,v 1.2 2002/04/27 18:26:15 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-02    Allan           VBM:2002042404 - Created. The keyword mapper
 *                              base class.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.mappers;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.AllowableKeywords;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * This class is the base class of a set of classes which are used to map
 * from a StyleKeyword to an external string.
 */
public class DefaultKeywordMapper
        extends AbstractKeywordMapper {

    private Map keyword2String;

    public DefaultKeywordMapper() {
        this(null);
    }

    public DefaultKeywordMapper(AllowableKeywords keywords) {
        keyword2String = new HashMap();
        
        if (keywords != null) {
            List list = keywords.getKeywords();
            for (int i = 0; i < list.size(); i++) {
                StyleKeyword keyword = (StyleKeyword) list.get(i);
                addStandardMapping(keyword);
            }
        }
    }

    public void addMapping(StyleKeyword keyword, String string) {
        keyword2String.put(keyword, string);
    }

    public String getExternalString(StyleValue potentialKeyword) {
        return (String) keyword2String.get(potentialKeyword);
    }

    public String getExternalString(StyleKeyword keyword) {
        return (String) keyword2String.get(keyword);
    }

    protected void addStandardMapping(StyleKeyword keyword) {
        addMapping(keyword, keyword.getName());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10830/1	pduffin	VBM:2005121405 Allowed keyword mapper used by renderer to be overridden by CSSVersion, created keyword mapper for vertical-align

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 31-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 28-Apr-04	3937/1	byron	VBM:2004032308 Enhance Menu Support: Theme Changes: Update renderers and parsers

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
