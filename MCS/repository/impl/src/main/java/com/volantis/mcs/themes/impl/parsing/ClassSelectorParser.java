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

package com.volantis.mcs.themes.impl.parsing;

import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.parsing.ObjectParser;

/**
 * A parser for converting class selectors to and from text.
 */
public class ClassSelectorParser implements ObjectParser {
    /**
     * The model factory to use for creating class selector instances.
     */
    private static final StyleSheetFactory MODEL_FACTORY =
            StyleSheetFactory.getDefaultInstance();

    // Javadoc inherited
    public String objectToText(Object object) {
        if (object instanceof ClassSelector) {
            StringBuffer output = new StringBuffer();
            ClassSelector idSelector = (ClassSelector) object;

            output.append(".");
            output.append(idSelector.getCssClass());

            return output.toString();
        } else {
            throw new IllegalArgumentException("Expected a class selector");
        }
    }

    // Javadoc inherited
    public Object textToObject(String text) {
        // Ignore the first '.' on the string, since this is part of the
        // representation of the class.
        if (text.startsWith(".")) {
            text = text.substring(1);
        }
        ClassSelector selector = MODEL_FACTORY.createClassSelector(text);
        return selector;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10429/1	adrianj	VBM:2005111715 Fix for editing class selectors

 07-Dec-05	10425/1	adrianj	VBM:2005111715 Fix for class selector editing

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
