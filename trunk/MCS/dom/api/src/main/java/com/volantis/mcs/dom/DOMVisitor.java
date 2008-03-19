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
 * A visitor interface for the MCS DOM.
 * <p>
 * See the GOF book for an explanation of the visitor pattern.
 *
 * @mock.generate
 */
public interface DOMVisitor {
    
    /**
     * This method is called if while traversing the DOM an Element node
     * is found.
     *
     * @param element The Element node which is being visited.
     */
    void visit(Element element);

    /**
     * This method is called if while traversing the DOM a Text node
     * is found.
     *
     * @param text The Text node which is being visited.
     */
    void visit(Text text);

    /**
     * This method is called if while traversing the DOM a Comment node
     * is found.
     *
     * @param comment The Comment node which is being visited.
     */
    void visit(Comment comment);

    /**
     * This method is called on the document of a DOM.
     *
     * @param document
     */
    void visit(Document document);
}
