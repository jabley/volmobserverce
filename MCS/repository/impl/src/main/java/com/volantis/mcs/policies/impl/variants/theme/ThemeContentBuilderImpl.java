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

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.policies.impl.AbstractBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilderVisitor;
import com.volantis.mcs.policies.variants.content.InternalContentBuilder;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.ContentType;
import com.volantis.mcs.policies.variants.theme.InternalThemeContent;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.themes.BaseStyleSheet;
import com.volantis.mcs.themes.model.ThemeModel;

/**
 * For now the theme content simply wraps a device theme.
 */
public class ThemeContentBuilderImpl
        extends AbstractBuilder
        implements InternalThemeContentBuilder, InternalContentBuilder {

    private InternalThemeContent themeContent;

    private BaseStyleSheet styleSheet;

    private boolean importParent;

    public ThemeContentBuilderImpl() {
        this(null);
    }

    public ThemeContentBuilderImpl(InternalThemeContent themeContent) {
        if (themeContent != null) {
            this.themeContent = themeContent;
            styleSheet = themeContent.getStyleSheet();
            importParent = themeContent.getImportParent();
        }
    }

    public Content getContent() {
        return getInternalThemeContent();
    }

    public InternalThemeContent getInternalThemeContent() {
        if (themeContent == null) {
            // Make sure only valid instances are built.
            validate();
            themeContent = new ThemeContentImpl(this);
        }

        return themeContent;
    }

    protected Object getBuiltObject() {
        return getInternalThemeContent();
    }

    protected void clearBuiltObject() {
        themeContent = null;
    }

    public void accept(ContentBuilderVisitor visitor) {
        visitor.visit(this);
    }

    public ContentType getContentType() {
        return ContentType.THEME;
    }

    public void validate(ValidationContext context) {
        // Validate the theme contents.
        if (styleSheet != null) {
            Step step = context.pushPropertyStep(ThemeModel.STYLE_SHEET);
            styleSheet.validate(context);
            context.popStep(step);
        }
    }

    public BaseStyleSheet getStyleSheet() {
        return styleSheet;
    }

    public void setStyleSheet(BaseStyleSheet styleSheet) {
        if (!equals(this.styleSheet, styleSheet)) {
            stateChanged();
        }

        this.styleSheet = styleSheet;
    }

    public boolean getImportParent() {
        return importParent;
    }

    public void setImportParent(boolean importParent) {
        if (!equals(this.importParent, importParent)) {
            stateChanged();
        }

        this.importParent = importParent;
    }

    boolean jibxHasStyleSheet() {
        return styleSheet != null;
    }

    // Javadoc inherited.
    protected boolean castThenCheckEquality(Object obj) {
        return (obj instanceof ThemeContentBuilderImpl) ?
                equalsSpecific((ThemeContentBuilderImpl) obj) : false;
    }

    /**
     * @see #equalsSpecific(com.volantis.mcs.policies.impl.EqualsHashCodeBase)
     */
    protected boolean equalsSpecific(ThemeContentBuilderImpl other) {

        final boolean styleSheetsEqual =
            styleSheet == null ? other.styleSheet == null :
                other.styleSheet != null;
        return super.equalsSpecific(other) && styleSheetsEqual &&
            importParent == other.importParent;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = super.hashCode();
        // todo implement in terms of style sheet.
        return result;
    }
}
