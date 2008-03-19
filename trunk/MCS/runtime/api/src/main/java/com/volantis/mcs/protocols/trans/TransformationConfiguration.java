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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;

/**
 * Interface which encapsulates the information that a {@link TransMapper}
 * would need to determine how an element should be treated when remapping.
 *
 * @mock.generate
 */
public interface TransformationConfiguration {

    /**
     * <p>If any of the style properties that have been deemed important for
     * this protocol (i.e. their values should not be lost during optimization)
     * have values set, then return true, otherwise return false.</p>
     *
     * <p>However if the same value for the "important" styles have been set
     * on all of the element's children, then it can be optimized away because
     * the important styling would be preserved</p>
     *
     * @param element whose Styles to check to see if any of the 'important'
     *                property values have been set
     * @return true if the element has values set for any of the 'important'
     * property values which would be lost if the element was optimized away,
     * false otherwise
     */
    boolean optimisationWouldLoseImportantStyles(Element element);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 ===========================================================================
*/
