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
package com.volantis.mcs.themes;

import com.volantis.mcs.model.copy.Copyable;
import com.volantis.mcs.model.validation.SourceLocation;

/**
 * The base interface for all objects which can be stored in a StyleProperties
 * class.
 *
 * @mock.generate
 */
public interface StyleValue extends SourceLocation, Copyable {
    /**
     * Get the type of the value.
     *
     * @return The type of the value.
     */
    StyleValueType getStyleValueType();

    /**
     * Visit the underlying type.
     */
    void visit(StyleValueVisitor visitor, Object object);

    /**
     * Get a standard CSS representation of the value.
     * @return A standard CSS representation of the value.
     */
    String getStandardCSS();

    /**
     * Get the cost in characters of the standard representation of this value.
     *
     * @return The cost.
     */
    int getStandardCost();
}
