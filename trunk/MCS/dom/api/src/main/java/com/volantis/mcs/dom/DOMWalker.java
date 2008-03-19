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
package com.volantis.mcs.dom;

import com.volantis.shared.iteration.IterationAction;

/**
 * A class which walks a DOM, visiting the nodes within with the provided
 * visitor.
 */
public class DOMWalker implements NodeIteratee {

    /**
     * The visitor which we will visit the document content with.
     */
    private final WalkingDOMVisitor visitor;

    /**
     * Initialise.
     *
     * @param visitor the visitor which we will visit the document content with.
     */
    public DOMWalker(WalkingDOMVisitor visitor) {

        this.visitor = visitor;
    }

    /**
     * Walk the content of the document provided with the visitor provided.
     *
     * @param document the document who's content we will walk.
     */
    public void walk(Document document) {

        document.accept(visitor);

        visitor.beforeChildren(document);

        document.forEachChild(this);

        visitor.afterChildren(document);
    }

    /**
     * Walk the element and its contents with the visitor provided.
     *
     * @param element the element that will be walked.
     */
    public void walk(Element element) {
        next(element);
    }

    // Javadoc inherited.
    public IterationAction next(Node node) {

        if (node != null) {
            // Visit the node.
            node.accept(visitor);

            // Walk the node's children, if any.
            // todo: should visitContent/forEachChild operate on Nodes
            // to avoid this instanceof?
            if (node instanceof Element) {
                Element element = (Element) node;

                assertElementProperties(element);

                if (!element.isEmpty()) {
                    visitor.beforeChildren(element);
                    element.forEachChild(this);
                    visitor.afterChildren(element);
                }
            }
        }

        return IterationAction.CONTINUE;
    }

    /**
     * Hook method provided as part of a check during a {@link #next(Node)}
     * invocation.
     *
     * @param element the context <code>Element</code>
     */
    protected void assertElementProperties(Element element) {

        // Sensibility check to ensure that we prevent null named
        // elements from gumming up the works. See VBM:2005011307.
        if (element.getName() == null) {
            throw new IllegalStateException(
                    "Element may not have null name");
        }
    }

    /**
     * Walks over nodes within the specified sequence.
     *
     * @param sequence The sequence.
     */
    public void walk(NodeSequence sequence) {
        sequence.forEach(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
