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

package com.volantis.styling.impl.engine.matchers;

import com.volantis.styling.engine.Attributes;
import com.volantis.styling.impl.engine.StylerContext;
import com.volantis.styling.impl.engine.selectionstates.SelectionState;
import com.volantis.styling.impl.state.State;
import com.volantis.styling.impl.state.StateIdentifier;

import java.util.Iterator;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface MatcherContext {

    /**
     * Prepare this context for the cascade.
     *
     * <p>This is called before performing the cascade on an element and
     * provides an opportunity for the context to initialise element specific
     * information that is needed during matching.</p>
     * @param attributes
     */
    void prepareForCascade(Attributes attributes);

    /**
     * Get the local name of the context element.
     *
     * @return The local name of the context element.
     */
    String getLocalName();

    /**
     * Get the namespace of the context element.
     *
     * @return The namespace of the context element.
     */
    String getNamespace();

    /**
     * Get the value of the attribute of the context element.
     *
     * @param namespace The namespace of the attribute, null if the attribute
     * does not belong in a namespace, i.e. it belongs to the element.
     * @param localName The local name of the attribute, may not be null.
     *
     * @return The value of the attribute, or null if the attribute does not
     * exist.
     */
    String getAttributeValue(String namespace, String localName);

    /**
     * Get the position of the element within its parent.
     *
     * <p>This is <code>1</code> based, i.e. the first child has a position of
     * <code>1</code>, the second has a position of <code>2</code> and so on,
     * @return
     */
    int getPosition();

    /**
     * Returns an iterator of state markers that could be active.
     *
     * @return and iterator of {@link StateMarker}s that could be active
     */
    Iterator stateMarkers();

    /**
     * Flag a particular state as matched.
     *
     * @param matchedState
     */
    void stateMatched(SelectionState matchedState, StylerContext context);

    boolean changeDirectRelationship(boolean directRelationship);

    boolean hasDirectRelationship();

    /**
     * Get the id of underlying element.
     * @return the underlying element id.
     */
    int getElementId();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
