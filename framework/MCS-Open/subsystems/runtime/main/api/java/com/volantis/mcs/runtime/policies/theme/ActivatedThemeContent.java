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

package com.volantis.mcs.runtime.policies.theme;

import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.themes.StyleSheet;

public class ActivatedThemeContent
        implements Content {

    private final StyleSheet styleSheet;
    private final boolean importParent;

    public ActivatedThemeContent(StyleSheet styleSheet, boolean importParent) {
        this.styleSheet = styleSheet;
        this.importParent = importParent;
    }

    public ContentType getContentType() {
        return ContentType.THEME;
    }

    public ContentBuilder getContentBuilder() {
        throw new UnsupportedOperationException();
    }

    public StyleSheet getStyleSheet() {
        return styleSheet;
    }

    public boolean getImportParent() {
        return importParent;
    }
}
