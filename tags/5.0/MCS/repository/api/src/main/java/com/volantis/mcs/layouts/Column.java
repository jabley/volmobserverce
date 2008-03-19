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

package com.volantis.mcs.layouts;

/**
 * The Column attribute container.
 *
 * @mock.generate base="SimpleAttributeContainer"
 */
public class Column extends SimpleAttributeContainer implements StyleableFormat,
                                                                StyleAttributes,
                                                                WidthAttributes{

    private AbstractGrid grid;

    /**
     * Initialise.
     *
     * @param grid The owning {@link AbstractGrid}.
     */
    public Column(AbstractGrid grid) {
        this.grid = grid;
        this.setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                          FormatConstants.WIDTH_UNITS_VALUE_PERCENT);
    }

    /**
     * Get the width of the column.
     *
     * @return The width of the column.
     *
     * @deprecated This does not do what it is supposed to at all, it returns
     * the grid's width, not its own.
     */
    public int getDeprecatedWidth() {
        String value =
                (String)grid.getAttribute(FormatConstants.WIDTH_ATTRIBUTE);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }

    /**
     * Get the units in which the width is expressed.
     *
     * @return The units in which the width is expressed.
     *
     * @deprecated This does not do what it is supposed to at all, it returns
     * the grid's width units, not its own.
     */
    public String getDeprecatedWidthUnits() {
        String units =
                (String)grid.getAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE);
        return (units == null ?
                FormatConstants.WIDTH_UNITS_VALUE_PERCENT : units);
    }


    /**
     * Get the width of the column.
     *
     * @return The width of the column.
     */
    public String getWidth() {
        return (String) getAttribute(FormatConstants.WIDTH_ATTRIBUTE);
    }

    /**
     * Get the units in which the width is expressed.
     *
     * @return The units in which the width is expressed.
     */
    public String getWidthUnits() {
        String units =
                (String)getAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE);
        return (units == null ?
                FormatConstants.WIDTH_UNITS_VALUE_PERCENT : units);
    }

    /**
     * Return the style class associated with this <code>Column</code>.
     * @return the style class associated with this <code>Column</code>.
     */
    public String getStyleClass() {
        return (String) getAttribute(FormatConstants.STYLE_CLASS);
    }

    // javadoc inherited
    public void setStyleClass(String styleClass) {
        setAttribute(FormatConstants.STYLE_CLASS,
                     styleClass);
    }

    // javadoc inherited
    public void setWidth(String width) {
        setAttribute(FormatConstants.WIDTH_ATTRIBUTE,
                     width);
    }

    // javadoc inherited
    public void setWidthUnits(String widthUnits) {
        setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE,
                     widthUnits);
    }

    // Javadoc inherited.
    public String getTypeName() {
        return "Grid.Column";
    }

    // Javadoc inherited.
    public boolean equals(final Object other) {
        return this == other ||
            other != null && getClass() == other.getClass() && super.equals(other);
    }

    // Javadoc inherited.
    public int hashCode() {
        return super.hashCode();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/2	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 06-Dec-05	10465/2	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 29-Nov-05	10465/1	geoff	VBM:2005112205 MCS35: Themes not overridng layout properties as expected at runtime

 02-Oct-05	9652/1	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 30-Sep-05	9590/5	schaloner	VBM:2005092204 finished regular JiBX bindings

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
