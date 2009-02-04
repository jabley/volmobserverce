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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.jdom;

import org.jdom.Element;
import org.jdom.Text;
import org.jdom.Attribute;

/**
 * The methods on this interface are invoked by the DOM Walker when specific
 * type of node are visited, allowing implementations of this interface to
 * perform specific processing against the various nodes in a given DOM tree.
 */
public interface Visitor {
    /**
     * This typesafe enumeration defines the values that can be returned by
     * the visitor's <code>nextHeader</code> methods. The various options direct
     * the DOM walker how to behave.
     */
    final class Action {
        /**
         * The DOM walker will abort the entire traversal when this literal is
         * returned.
         */
        public static final Action STOP = new Action("stop"); //$NON-NLS-1$

        /**
         * The DOM walker will continue the traversal when this literal is
         * returned.
         */
        public static final Action CONTINUE = new Action("continue"); //$NON-NLS-1$

        /**
         * The DOM walker will stop processing the current element, continuing
         * the traversal at the next appropriate element, when this literal is
         * returned.
         */
        public static final Action SKIP = new Action("skip"); //$NON-NLS-1$

        /**
         * The internal name for the action.
         */
        private String name;

        /**
         * Initializes the new instance using the given parameters. This is
         * private to ensure that no further instances may be created.
         *
         * @param name the internal name for the action
         */
        private Action(String name) {
            this.name = name;
        }

        /**
         * Returns the literal's internal name. Useful for debug purposes.
         *
         * @return a string form of the literal
         */
        public String toString() {
            return name;
        }
    }

    /**
     * Invoked when the DOM walker encounters an {@link org.jdom.Element} during a
     * traversal.
     *
     * @param element the element encountered
     * @return an {@link Visitor.Action} literal that directs how the walker should
     *         behave
     */
    Action visit(Element element);

    /**
     * Invoked when the DOM walker encounters an {@link org.jdom.Attribute} during a
     * traversal for which attribute visitation is enabled.
     *
     * @param attribute the attribute encountered
     * @return an {@link Visitor.Action} literal that directs how the walker should
     *         behave
     */
    Action visit(Attribute attribute);

    /**
     * Invoked when the DOM walker encounters a {@link org.jdom.Text} node during a
     * traversal for which text node visitation is enabled.
     *
     * @param text the text node encountered
     * @return an {@link Visitor.Action} literal that directs how the walker should
     *         behave
     */
    Action visit(Text text);
 }

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/2	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 17-Dec-03	2226/1	philws	VBM:2003121115 Provide JDOM traversal walker/visitor

 ===========================================================================
*/
