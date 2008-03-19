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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.css.emulator.mappers;

import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;

/**
 * Map a keyword value from its internal representation to its
 * canonical external representation.
 */
public class DisplayKeywordMapper
        extends DefaultKeywordMapper {

    /**
     * The single instance of this keyword mapper.
     */
    private static KeywordMapper singleton;

    static {
        // Always initialise to prevent the synchronization
        // problem that could occur through lazy initialisation
        singleton = new DisplayKeywordMapper();
    }

    /**
     * Return the single instance of this keyword mapper.
     * @return KeywordMapper
     */
    public static KeywordMapper getSingleton() {
        return singleton;
    }

    /**
     * Initialize a new instance.
     */
    protected DisplayKeywordMapper() {
        // Add all the default mappings...
        super(new AllowableKeywords(
                DisplayKeywords.getDefaultInstance().getKeywords()));
        // ...then overwrite MCS_MARQUEE with the runtime mapping.
        addMapping(DisplayKeywords.MCS_MARQUEE, "-wap-marquee");
    }
}
