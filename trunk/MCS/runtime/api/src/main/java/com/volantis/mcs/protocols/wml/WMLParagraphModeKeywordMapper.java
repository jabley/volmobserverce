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

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;

public class WMLParagraphModeKeywordMapper
        extends DefaultKeywordMapper {

    private static final KeywordMapper DEFAULT_INSTANCE =
            new WMLParagraphModeKeywordMapper();

    public static KeywordMapper getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private WMLParagraphModeKeywordMapper() {
        // NOTE: we map NORMAL even though it is the "default" value, because
        // paragraph mode inherits it's value from the last paragraph, which
        // means that the effective default varies.
        addMapping(WhiteSpaceKeywords.NORMAL, "wrap");
        addMapping(WhiteSpaceKeywords.PRE, "nowrap");
        addMapping(WhiteSpaceKeywords.NOWRAP, "nowrap");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10779/1	geoff	VBM:2005121202 MCS35: WML vertical whitespace fix does not handle mode settings (take 2)

 ===========================================================================
*/
