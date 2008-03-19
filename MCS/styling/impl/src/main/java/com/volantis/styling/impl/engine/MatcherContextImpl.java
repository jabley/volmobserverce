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

package com.volantis.styling.impl.engine;

import com.volantis.styling.engine.Attributes;
import com.volantis.styling.impl.engine.matchers.MatcherContext;
import com.volantis.styling.impl.engine.matchers.InternalMatcherContext;
import com.volantis.styling.impl.state.State;
import com.volantis.styling.impl.state.StateContainer;
import com.volantis.styling.impl.state.StateIdentifier;

/**
 * Implementation of {@link MatcherContext}.
 *
 * <p>This delegates calls to the appropriate underlying object as the values
 * returned from them will change over the lifetime of instances of this class
 * and these changes will be outside of the control of this class.</p>
 */
public class MatcherContextImpl
        implements InternalMatcherContext {

    /**
     * The element stack.
     */
    private final ElementStack stack;

    /**
     * The state container.
     */
    private StateContainer container;

    /**
     * The attribute container.
     */
    private Attributes attributes;

    private boolean directRelationship;

    /**
     * The local name of the element.
     */
    private String localName;

    /**
     * The namespace of the element.
     */
    private String namespace;

    /**
     * Initialise.
     *
     * @param stack The element stack.
     */
    public MatcherContextImpl(ElementStack stack) {
        this.stack = stack;

        // Any selectors that do not have a contextual matcher always have
        // a direct relationship with the context element.
        this.directRelationship = true;
    }

    // Javadoc inherited.
    public void prepareForCascade(Attributes attributes) {
        ElementStackFrame current = stack.getCurrentElementStackFrame();
        localName = current.getLocalName();
        namespace = current.getNamespace();
        this.attributes = attributes;
    }

    // Javadoc inherited.
    public String getAttributeValue(String namespace, String localName) {
        return attributes.getAttributeValue(namespace, localName);
    }

    // Javadoc inherited.
    public String getLocalName() {
        return localName;
    }

    // Javadoc inherited.
    public String getNamespace() {
        return namespace;
    }

    // Javadoc inherited.
    public int getPosition() {
        return stack.getContainingElementStackFrame().getChildCount();
    }

    // Javadoc inherited.
    public State getState(StateIdentifier identifier) {
        return container.getState(identifier);
    }

    // Javadoc inherited.
    public boolean changeDirectRelationship(boolean directRelationship) {
        boolean oldDirectRelationship = this.directRelationship;
        this.directRelationship = directRelationship;
        return oldDirectRelationship;
    }

    // Javadoc inherited.
    public boolean hasDirectRelationship() {
        return directRelationship;
    }

    //javadoc inherited
    public int getElementId() {
        return stack.getCurrentElementStackFrame().getElementId();
    }

    public void setContainer(StateContainer container) {
        this.container = container;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
