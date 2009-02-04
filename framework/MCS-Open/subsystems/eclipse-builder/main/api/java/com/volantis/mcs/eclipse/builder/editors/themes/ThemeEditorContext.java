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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.InternalPolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilderVisitor;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.themes.CSSStyleSheet;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.StyleSheetSourceFormat;

import java.util.List;

/**
 * Specialisation of editor context for editing themes.
 */
public class ThemeEditorContext extends PolicyEditorContext {
    /**
     * Style sheet factory.
     */
    private static final StyleSheetFactory STYLE_SHEET_FACTORY =
            StyleSheetFactory.getDefaultInstance();

    private static final String RESOURCE_PREFIX = "ThemeEditorContext.";

    private static final String SAVE_ERROR_TITLE =
            EditorMessages.getString(RESOURCE_PREFIX + "cssWarning.title");

    private static final String SAVE_ERROR_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "cssWarning.message");

    private ThemeDesign designPage;

    private ThemeEditor editor;

    public ThemeEditorContext(ThemeEditor editor) {
        this.editor = editor;
    }

    protected Class getModelType() {
        return VariablePolicy.class;
    }

    /**
     * A theme context will always represent a theme variable policy type.
     *
     * @return The theme variable policy type
     */
    public PolicyType getPolicyType() {
        return VariablePolicyType.THEME;
    }

    /**
     * A theme variant will always consist of theme variant types.
     *
     * @return The theme variant type
     */
    public VariantType getDefaultVariantType() {
        return VariantType.THEME;
    }

    public ThemeEditor getThemeEditor() {
        return editor;
    }

    public void setDesignPage(ThemeDesign design) {
        designPage = design;
    }

    public ThemeDesign getDesignPage() {
        return designPage;
    }

    /**
     * Pre-process the theme policy to translate any CSS stylesheets into
     * parsed stylesheet objects similar to those generated from the XML
     * equivalent.
     *
     * @param loadedPolicy The policy to process
     * @return A processed version of the policy
     *
     * 
     */
    protected PolicyBuilder preProcessPolicy(PolicyBuilder loadedPolicy) {
        PolicyBuilderVisitor visitor = new PolicyBuilderVisitor() {
            // Javadoc inherited
            public void visit(BaseURLPolicyBuilder policy) {
                // Shouldn't exist in a theme
            }

            // Javadoc inherited
            public void visit(ButtonImagePolicyBuilder policy) {
                // Shouldn't exist in a theme
            }

            // Javadoc inherited
            public void visit(RolloverImagePolicyBuilder policy) {
                // Shouldn't exist in a theme
            }

            // Javadoc inherited
            public void visit(VariablePolicyBuilder policy) {
                boolean containsCSS = false;
                List builders = policy.getVariantBuilders();
                for (Object builderObject : builders) {
                    VariantBuilder builder = (VariantBuilder) builderObject;
                    if (builder.getVariantType() == VariantType.THEME) {
                        ContentBuilder variantContent =
                                builder.getContentBuilder();
                        InternalThemeContentBuilder themeContent =
                                (InternalThemeContentBuilder) variantContent;
                        if (themeContent.getStyleSheet() instanceof CSSStyleSheet) {
                            StyleSheet parsedStyleSheet =
                                    STYLE_SHEET_FACTORY.createStyleSheet(
                                            (CSSStyleSheet)
                                                  themeContent.getStyleSheet());
                            parsedStyleSheet.setSourceFormat(
                                    StyleSheetSourceFormat.CSS);
                            themeContent.setStyleSheet(parsedStyleSheet);
                            containsCSS = true;
                        }
                    }
                }
                if (containsCSS) {
                    showWarningDialog(SAVE_ERROR_TITLE, SAVE_ERROR_MESSAGE);
                }
            }
        };
        ((InternalPolicyBuilder) loadedPolicy).accept(visitor);
        return loadedPolicy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/2	adrianj	VBM:2005111601 Add style rule view

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
