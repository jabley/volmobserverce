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

package com.volantis.mcs.layouts.common;

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;

public class Format {

    /**
     * Indicates whether the format requires a name.
     */
    private final boolean nameRequired;

    private final MutableStyleProperties styleProperties;

    public Format(boolean nameRequired) {
        this.nameRequired = nameRequired;
        styleProperties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
    }

    /**
     * The name of the format.
     *
     * <p>Most (but not all) formats have names. For some it is required, for
     * others it is optional.</p>
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MutableStyleProperties getStyleProperties() {
        return styleProperties;
    }
}
