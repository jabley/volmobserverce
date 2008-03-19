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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.sheet.CompiledStyleSheet;
import java.util.List;
import java.util.ArrayList;

/**
 * Encapsulates all the information relating to an element that is being
 * processed by the styling engine.
 */
public class ElementStackFrameImpl implements ElementStackFrame {

    /**
     * The namespace of the element.
     */
    private String namespace;

    /**
     * the local name of the element.
     */
    private String localName;

    /**
     * The number of children that have been processed.
     */
    private int childCount;

    /**
     * The style values that may be inherited.
     */
    private StyleValues values;

    /**
     * identifier of the element as specified by the styling engine.
     */
    private int elementId;

    /**
     * the style sheet which represents any inline styles applied to the element
     */
    private CompiledStyleSheet inlineStyles;
    
    private List nestedStyles;

    // Javadoc inherited.
    public String getNamespace() {
        return namespace;
    }

    // Javadoc inherited.
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    // Javadoc inherited.
    public String getLocalName() {
        return localName;
    }

    // Javadoc inherited.
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    // Javadoc inherited.
    public int getChildCount() {
        return childCount;
    }

    // Javadoc inherited.
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    // Javadoc inherited.
    public StyleValues getValues() {
        return values;
    }

    // Javadoc inherited.
    public void setValues(StyleValues values) {
        this.values = values;
    }

    // Javadoc inherited.
    public void incrementChildCount() {
        childCount += 1;
    }

    //javadoc inherited
    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    //javadoc inherited
    public int getElementId() {
        return elementId;
    }

    //javadoc inherited
    public void setInlineStyleSheet(CompiledStyleSheet styleSheet) {
        inlineStyles = styleSheet;
    }

    //javadoc inherited
    public CompiledStyleSheet getInlineStyleSheet() {
        return inlineStyles;
    }
    
    //javadoc inherited
    public void addNestedStyleSheet(CompiledStyleSheet styleSheet) {
    	if (nestedStyles == null) {
    		nestedStyles = new ArrayList();
    	}
        nestedStyles.add(styleSheet);
    }

    //javadoc inherited
    public List getNestedStyleSheets() {
        return nestedStyles;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
