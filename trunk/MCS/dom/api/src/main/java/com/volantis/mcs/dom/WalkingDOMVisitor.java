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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

/**
 * A visitor that can be used to walk across the DOM.
 *
 * <p>Implementations of this do not have to worry about walking across the
 * DOM but they cannot maintain state in their call stack.</p>
 *
 * @mock.generate base="DOMVisitor"
 */
public interface WalkingDOMVisitor
        extends DOMVisitor {

    /**
     * This method is called before the children of an element are visited.
     *
     * @param element the element which is being visited.
     */
    void beforeChildren(Element element);

    /**
     * This method is called after the children of an element are visited.
     *
     * @param element the element which is being visited.
     */
    void afterChildren(Element element);

    /**
     * This method is called before the children of an element are visited.
     *
     * @param element the element which is being visited.
     */
    void beforeChildren(Document element);

    /**
     * This method is called after the children of an element are visited.
     *
     * @param element the element which is being visited.
     */
    void afterChildren(Document element);
}
