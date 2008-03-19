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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.version;

import com.volantis.styling.properties.StyleProperty;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleSyntax;

import java.util.Map;

/**
 * The main entry point for an object structure which describes which parts
 * of MCS Themes a particular version of CSS supports.
 *
 * @mock.generate
 */
public interface CSSVersion {

    /**
     * Return the CSS property for the property provided.
     * 
     * The CSS property describes which parts of the MCS property is supported
     * in this version of CSS.
     *
     * @param property the MCS property to query for.
     * @return the CSS property which describes which parts of the MCS
     *      property supplied is supported.
     */
    CSSProperty getProperty(StyleProperty property);

    /**
     * Return the CSS property for the property name provided.
     * 
     * The CSS property describes which parts of the MCS property is supported
     * in this version of CSS.
     *
     * @param propertyName the MCS property name to query for.
     * @return the CSS property which describes which parts of the MCS
     *      property supplied is supported.
     */
    CSSProperty getProperty(String propertyName);
    
    /**
     * Returns name of CSS Property which is an alias for 
     * the specified MCS property in this version of CSS.
     *
     * @param property the MCS property to query for
     * @return name of CSS property or null if no alias supported
     */
    String getPropertyAlias(StyleProperty property);

    /**
     * Returns true if the shorthand is supported in this
     * version of CSS.
     *
     * @param shorthand the shorthand.
     * @return true if it shorthand is supported.
     */
    boolean supportsShorthand(StyleShorthand shorthand);

    /**
     * Returns true if the shorthand is supported in this
     * version of CSS.
     *
     * @param shorthandName the shorthand name.
     * @return true if it shorthand is supported.
     */
    boolean supportsShorthand(String shorthandName);

    /**
     * Returns true if the pseudo selector id supplied is supported in this
     * version of CSS.
     *
     * @param selectorId the id of the pseudo selector.
     * @return true if the id is supported.
     */
    boolean supportsPseudoSelectorId(String selectorId);

    /**
     * Returns true if the syntax is supported in this version of CSS.
     *
     * @param syntax    which may be supported
     * @return true if the syntax is supported, false otherwise
     */
    boolean supportsSyntax(StyleSyntax syntax);

    IterationAction iterate(CSSPropertyIteratee iteratee);

    /**
     * Return a Map (may be empty, but will NOT be <code>null</code>) keyed by
     * element names that can require remapping since the device doesn't
     * support specifying a style via CSS - instead, an attribute on an element
     * should be used.
     *
     * Each value in the Map is another map, keyed by attribute name, with the
     * value being the corresponding expression used to create the attribute
     * value.
     *
     * Changes to the returned Map will be be reflected in the underlying field
     * in a CSSVersion instance.
     *
     * @return a Map - not null.
     */
    Map getRemappableElements();

    /**
     * Set the map of remappable elements. The expected structure of the
     * contents of the map is defined in {@link #getRemappableElements()}.
     *
     * @param remappableElements a Map - not null.
     * @see #getRemappableElements()
     */
    void setRemappableElements(Map remappableElements);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/2	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/6	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
