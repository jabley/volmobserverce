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
package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.layouts.StyleableFormat;

/**
 * A {@link StyleableFormat} to use as a placeholder for layout related objects
 * which require format styling but do not correspond directly to a format.
 * <p>
 * An example of this kind of thing is a grid cell. There is no format for a
 * grid cell but we still need to style them.
 * <p>
 * The synthetic type name is primarily useful for debugging purposes.
 */
public final class SyntheticStyleableFormat implements StyleableFormat {

    /**
     * Synthetic styleable format which is used as a placeholder to group
     * grid columns to ensure that they have a consistent position within
     * their parent.
     */
    public static final StyleableFormat GRID_COLUMNS =
            new SyntheticStyleableFormat("(Grid Columns)");

    /**
     * Synthetic styleable format which is used as a placeholder to group
     * grid rows to ensure that they have a consistent position within
     * their parent.
     */
    public static final StyleableFormat GRID_BODY =
            new SyntheticStyleableFormat("(Grid Body)");

    /**
     * Synthetic styleable format which is used as a placeholder to allow grid
     * cells to be styled.
     */
    public static final StyleableFormat GRID_CELL =
            new SyntheticStyleableFormat("(Grid Cell)");

    /**
     * Synthetic styleable format which is used as a placeholder to group
     * spatial columns to ensure that they have a consistent position within
     * their parent.
     */
    public static final StyleableFormat SPATIAL_COLUMNS =
            new SyntheticStyleableFormat("(Spatial Columns)");

    /**
     * Synthetic styleable format which is used as a placeholder to allow
     * spatial iterator columns to be styled.
     */
    public static final StyleableFormat SPATIAL_COLUMN =
            new SyntheticStyleableFormat("(Spatial Column)");

    /**
     * Synthetic styleable format which is used as a placeholder to group
     * spatial rows to ensure that they have a consistent position within
     * their parent.
     */
    public static final StyleableFormat SPATIAL_BODY =
            new SyntheticStyleableFormat("(Spatial Body)");

    /**
     * Synthetic styleable format which is used as a placeholder to allow
     * spatial iterator rows to be styled.
     */
    public static final StyleableFormat SPATIAL_ROW =
            new SyntheticStyleableFormat("(Spatial Row)");

    /**
     * Synthetic styleable format which is used as a placeholder to allow
     * spatial iterator cells to be styled.
     */
    public static final StyleableFormat SPATIAL_CELL =
            new SyntheticStyleableFormat("(Spatial Cell)");

    private final String typeName;

    /**
     * Initialise.
     *
     * @param typeName the type name.
     */
    private SyntheticStyleableFormat(String typeName) {
        this.typeName = typeName;
    }

    // Javadoc inherited.
    public String getTypeName() {
        return typeName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	10046/3	geoff	VBM:2005102408 Post-review modifications

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 ===========================================================================
*/
