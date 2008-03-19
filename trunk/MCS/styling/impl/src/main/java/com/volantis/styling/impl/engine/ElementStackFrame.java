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

/**
 * Encapsulates all the information relating to an element that is being
 * processed by the styling engine.
 *
 * @mock.generate
 */
public interface ElementStackFrame {

    /**
     * Get the namespace.
     *
     * @return The namespace.
     */
    String getNamespace();

    /**
     * Set the namespace.
     *
     * @param namespace The new namespace.
     */
    void setNamespace(String namespace);

    /**
     * Get the local name.
     *
     * @return The local name.
     */
    String getLocalName();

    /**
     * Set the local name.
     *
     * @param localName The new local name.
     */
    void setLocalName(String localName);

    /**
     * Get the child count.
     *
     * @return The child count.
     */
    int getChildCount();

    /**
     * Set the child count.
     *
     * @param childCount The new child count.
     */
    void setChildCount(int childCount);

    /**
     * Get the style values.
     *
     * @return The style values.
     */
    StyleValues getValues();

    /**
     * Set the style values.
     *
     * @param values The new style values.
     */
    void setValues(StyleValues values);

    /**
     * Increment the child count.
     */
    void incrementChildCount();

    /**
     * Set the id of the element being processed by the styling engine
     * @param elementId
     */
    void setElementId(int elementId);

    /**
     * Get the id of the element being processed by the styling engine
     * @return the element id.
     */
    int getElementId();

    /**
     * Set the style sheet which represents any inline style value applied to
     * the element.
     * @param styleSheet
     */
    void setInlineStyleSheet(CompiledStyleSheet styleSheet);


    /**
     * Get the style sheet which represents any inline style value applied to
     * the element.
     * @return styleSheet
     */
    CompiledStyleSheet getInlineStyleSheet();

    /**
     * Get the style sheet list which represents any nested styles applied to
     * the element.
     * @return styleSheetList
     */
    List getNestedStyleSheets();
    
    /**
     * Add the style sheet to nested style sheet list
     * @param  styleSheet
     */
    void addNestedStyleSheet(CompiledStyleSheet styleSheet);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
