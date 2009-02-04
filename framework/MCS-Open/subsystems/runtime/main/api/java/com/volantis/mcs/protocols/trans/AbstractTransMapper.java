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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/AbstractTransMapper.java,v 1.3 2002/10/15 11:13:14 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Ensure that this implements
 *                              the AbridgedTransMapper interface and
 *                              correctly passes "state" data required during
 *                              the visitor pattern in the visitor pattern
 *                              object parameter.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;

import java.util.HashMap;

/**
 * This abstract class provides basic implementations of the mapping process,
 * including DOM tree traversal, but does not initialize the elementMap or
 * abridgeMap attributes. The latter must be done in each specialization to
 * provide the actual mapping and abridging required.
 */
public abstract class AbstractTransMapper
    extends RecursingDOMVisitor
        implements AbridgedTransMapper {

    /**
     * Indexed by the original element name, this map provides the object that
     * will actually perform the replacement of the given element and any
     * other associated processing. In order to achieve this the value is an
     * instance of a class implementing the TransMapper interface.
     */
    protected final HashMap elementMap = new HashMap();

    /**
     * Indexed by element name, this map provides a simple manner of
     * determining whether the children of specific elements should be
     * included in the re-mapping DOM traversal or not. If left as null
     * abridging will never be applied. The value of each entry is not
     * used by this class, just the key (the element name).
     */
    protected HashMap abridgeMap = null;

    /**
     * The {@link TransformationConfiguration} which should be used when
     * remapping elements. This may be a default configuration if there is no
     * protocol specific configuration required for transformation.
     */
    protected TransformationConfiguration transformationConfiguration;

    /**
     * The state of the visitor.
     */
    protected VisitorState state;

    /**
     * Class used to hold the required state during DOM traversal
     */
    protected static final class VisitorState {

        /**
         * The helper for getting known element names
         */
        protected final ElementHelper helper;

        /**
         * Indicates if abridging should be applied to the DOM traversal
         */
        protected boolean abridge = false;

        /**
         * Initializes the instance using the suppled attributes
         *
         * @param helper the ElementHelper to be held
         */
        public VisitorState(ElementHelper helper) {
            this.helper = helper;
        }

        /**
         * Initializes the instance using the suppled attributes
         *
         * @param helper the ElementHelper to be held
         * @param abridge set true if abridging is needed
         */
        public VisitorState(ElementHelper helper,
                            boolean abridge) {
            this(helper);

            this.abridge = abridge;
        }

        /**
         * Returns the registered element helper
         *
         * @return the element helper
         */
        public ElementHelper getHelper() {
            return helper;
        }

        /**
         * Returns the abridging status
         *
         * @return true if abridging is to be applied
         */
        public boolean getAbridge() {
            return abridge;
        }
    }

    /**
     * Invoked when a non-text DOM node is visited. Determines if re-mapping
     * is required and if so performs the re-mapping using the mapper element.
     * In addition, if abridging is required this method uses the abridgeMap
     * to determine when to apply it.
     *
     * @param element the element being visited
     */
    public void visit(Element element) {
        String name = element.getName();
        TransMapper mapperElement = (TransMapper)elementMap.get(name);

        if (mapperElement != null) {
            ElementHelper helper = state.getHelper();

            mapperElement.remap(element, helper);
        }

        // Visit the children of this element if no abridging is to be
        // applied or the element is not listed as to be abridged
        if (!state.getAbridge() ||
            (abridgeMap == null) ||
            !abridgeMap.containsKey(name)) {
            element.forEachChild(this);
        }
    }

    public void remap(Element element,
                      ElementHelper helper) {
        remap(element, helper, false);
    }

    public void remap(Element element,
                      ElementHelper helper,
                      boolean abridge) {

        VisitorState savedVisitorState = state;
        state = new VisitorState(helper, abridge);
        element.accept(this);
        state = savedVisitorState;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
