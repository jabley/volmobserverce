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

package com.volantis.styling.impl.engine.matchers.composites;

import com.volantis.styling.impl.state.State;

/**
 * Encapsulates the state associated with composite matchers.
 *
 * <p>Implementations of this must record the results of whether the
 * contextual selector matched. The way it does this is dependent on the
 * relationship between the context and subject elements.</p>
 *
 * @mock.generate
 */
public interface CompositeState
        extends State {

    /**
     * Indicates whether the current element being matched has a direct
     * relationship with the element matched by the contextual selector of the
     * composite to which this state belongs.
     *
     * <p>This means that the contextual matcher must have matched and that
     * the current element must have the appropriate relationship (as
     * determined by the type of state) with the element that was matched.</p>
     *
     * @return True if the current element has a direct relationship and
     * false if it does not.
     */
    boolean hasDirectRelationship();

    /**
     * Indicates whether the current element being matched potentially has an
     * indirect relationship with the element matched by the contextual
     * selector of the composite to which this state belongs.
     *
     * <p>An indirect relationship is one that involves a number of composites.
     * e.g. in the following markup element <code>a</code> has a direct
     * child relationship with element <code>b</code> and a potential indirect
     * relationship (possibly via another child, or descendant composite) with
     * element <code>b</code>.</p>
     *
     * <pre>
     * &lt;a>
     *     &lt;b>
     *         &lt;c>
     * </pre>
     *
     * @return True if it has, false if it does not.
     */
    boolean hasPotentialIndirectRelationship();

    /**
     * Invoked if the contextual matcher matched the current element.
     */
    void contextMatched();

    /**
     * Indicates whether the relationship defined by this state differentiates
     * between indirect and direct relationships.
     *
     * <p>e.g. a descendant matcher does not differentiate between direct
     * and indirect relationships, whereas a child matcher does.</p>
     *
     * @return True if the context should be matched against the current
     * element even when within context and false if it should not be matched.
     */
    boolean supportsIndirectRelationships();

    /**
     * Called before the start of an element.
     *
     * <p>This (and {@link #afterEndElement()}) allow the state to keep track
     * of the relationship between the contextual element(s) and the current
     * element in order to determine whether they have the correct
     * relationship.</p>
     */
    void beforeStartElement();

    /**
     * Called after the end of an element.
     *
     * @see #beforeStartElement()
     */
    void afterEndElement();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
