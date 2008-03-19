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

package com.volantis.styling.sheet;

/**
 * A container of style sheets.
 */
public interface StyleSheetContainer {

    /**
     * Merge a style sheet into the container.
     *
     * <p>The styles associated with the specified style sheet are merged into
     * the existing styles within the container according to the default
     * strategy.</p>
     *
     * @param styleSheet The style sheet to merge.
     */
    void pushStyleSheet(CompiledStyleSheet styleSheet);

    /**
     * Separate a nested style sheet.
     *
     * <p>Remove the styles associated with the specified style sheet from
     * the container.</p>
     *
     * @param styleSheet The style sheet whose styles should be removed.
     */
    void popStyleSheet(CompiledStyleSheet styleSheet);
}
