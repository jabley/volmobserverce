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
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.variants.VariantType;

/**
 * Specialisation of editor context for editing themes.
 */
public class ThemeEditorContext extends PolicyEditorContext {
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
