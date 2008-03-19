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
 * The Row attribute container.
 *
 * @mock.generate base="SimpleAttributeContainer"
 */
public class Row extends SimpleAttributeContainer
        implements StyleableFormat, HeightAttributes, StyleAttributes {

    private AbstractGrid grid;

    /**
     * Initialise.
     *
     * @param grid The owning {@link AbstractGrid}.
     */
    public Row(AbstractGrid grid) {
        this.grid = grid;
        this.setAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE,
                          FormatConstants.HEIGHT_UNITS_VALUE_PIXELS);
    }



    /**
     * Get the height of the row.
     *
     * @return The height of the row.
     *
     * @deprecated This does not do what it is supposed to at all, it returns
     * the grid's height, not its own.
     */
    public int getDeprecatedHeight() {
        String value =
                (String)grid.getAttribute(FormatConstants.HEIGHT_ATTRIBUTE);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }

    /**
     * Get the units in which the height is expressed.
     *
     * @return The units in which the height is expressed.
     *
     * @deprecated This does not do what it is supposed to at all, it returns
     * the grid's height units, not its own.
     */
    public String getDeprecatedHeightUnits() {
        return (String)
                grid.getAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE);
    }

    /**
     * Get the height of the row.
     *
     * @return The height of the row.
     */
    public String getHeight() {
        return (String) getAttribute(FormatConstants.HEIGHT_ATTRIBUTE);
    }

    /**
     * Set the height of the row.
     *
     * @param height The height of the row.
     */
    public void setHeight(final String height) {
        setAttribute(FormatConstants.HEIGHT_ATTRIBUTE, height);
    }

    /**
     * Set the units in which the height is expressed.
     *
     * @param heightUnits The units in which the height is expressed.
     */
    public void setHeightUnits(final String heightUnits) {
        setAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE, heightUnits);
    }

    /**
     * Get the units in which the height is expressed.
     *
     * @return The units in which the height is expressed.
     */
    public String getHeightUnits() {
        return (String) getAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE);
    }

    /**
     * Affects the height attribute.
     * @param axisWidth
     */
    public void setAxisWidth(String axisWidth) {
        setAttribute(FormatConstants.HEIGHT_ATTRIBUTE,
                     axisWidth);
    }

    public String getAxisWidth() {
        return (String)getAttribute(FormatConstants.HEIGHT_ATTRIBUTE);
    }

    public void setAxisWidthUnits(String axisWidthUnits) {
    }

    public String getAxisWidthUnits() {
        return null;
    }

    /**
     * Return the style class associated with this <code>Row</code>.
     * @return the style class associated with this <code>Row</code>.
     */
    public String getStyleClass() {
        return (String) getAttribute(FormatConstants.STYLE_CLASS);
    }

    // javadoc inherited
    public void setStyleClass(String styleClass) {
        setAttribute(FormatConstants.STYLE_CLASS,
                          styleClass);
    }

    // Javadoc inherited.
    public String getTypeName() {
        return "Grid.Row";
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

 30-Sep-05	9652/2	gkoch	VBM:2005092204 Initial marshaller/unmarshaller for layoutFormat

 30-Sep-05	9590/5	schaloner	VBM:2005092204 finished regular JiBX bindings

 29-Sep-05	9590/3	schaloner	VBM:2005092204 Added width, height, and style accessor interfaces derived from CoreAttributes interface

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
