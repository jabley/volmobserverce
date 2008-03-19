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

package com.volantis.mcs.policies.impl.variants.theme;

import com.volantis.mcs.policies.impl.EqualsHashCodeBase;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.content.ContentVisitor;
import com.volantis.mcs.policies.variants.content.InternalContent;
import com.volantis.mcs.policies.variants.theme.InternalThemeContent;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.themes.BaseStyleSheet;

/**
 * For now the theme content simply wraps a device theme.
 */
public class ThemeContentImpl
        extends EqualsHashCodeBase
        implements InternalThemeContent, InternalContent {

    private final BaseStyleSheet styleSheet;

    private final boolean importParent;

    public ThemeContentImpl(InternalThemeContentBuilder builder) {

        styleSheet = builder.getStyleSheet();
        importParent = builder.getImportParent();
    }

    public ContentBuilder getContentBuilder() {
        return getInternalThemeContentBuilder();
    }

    public InternalThemeContentBuilder getInternalThemeContentBuilder() {
        return new ThemeContentBuilderImpl(this);
    }

    public void accept(ContentVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.THEME;
    }

    public BaseStyleSheet getStyleSheet() {
        return styleSheet;
    }

    public boolean getImportParent() {
        return importParent;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ThemeContentImpl) ?
                equalsSpecific((ThemeContentImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ThemeContentImpl other) {
        return super.equalsSpecific(other) &&
                (styleSheet == null
                ? other.styleSheet == null
                : other.styleSheet != null);
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        // todo implement in terms of style sheet.
        return result;
    }
}
