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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.impl.parsing.ClassSelectorParser;

/**
 * Basic implementation of the {@link com.volantis.mcs.themes.ClassSelector} interface.
 */
public class DefaultClassSelector
        extends AbstractSelector
        implements ClassSelector {

    /**
     * The class which this selector will match.
     */
    private String cssClass;

    /**
     * Used to identify the cssClass property of this class when logging
     * validation errors.
     */
    PropertyIdentifier CSS_CLASS = new PropertyIdentifier(ClassSelector.class,
            "cssClass");

    public DefaultClassSelector(String cssClass) {
        this.cssClass = cssClass;
    }

    public DefaultClassSelector() {
    }

    // Javadoc inherited
    public String getCssClass() {
        return cssClass;
    }

    // Javadoc inherited
    public void setCssClass(String newClass) {
        cssClass = newClass;
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        Step step = context.pushPropertyStep(CSS_CLASS);
        if (!isValidToken(cssClass)) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                    context.createMessage("theme-invalid-class", cssClass));
        }
        context.popStep(step);
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == DefaultClassSelector.class) {
            DefaultClassSelector other = (DefaultClassSelector) o;
            equal = (cssClass == null ? other.cssClass == null :
                     cssClass.equals(other.cssClass));
        }
        return equal;
    }

    // Javadoc inherited
    public int hashCode() {
        int hash = 52349;
        hash = hash * 17 + cssClass == null ? 0 : cssClass.hashCode();
        return hash;
    }

    // Javadoc inherited
    public String toString() {
        return new ClassSelectorParser().objectToText(this);
    }

    // Javadoc inherited.
    public void accept(SelectorVisitor visitor) {
        visitor.visit(this);
    }

    public Object copy() {
        ClassSelector copy = new DefaultClassSelector(cssClass);
        return copy;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/2	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9992/3	emma	VBM:2005101811 Adding new style property validation

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/3	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
