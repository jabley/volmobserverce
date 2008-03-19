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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransElement.java,v 1.4 2003/01/09 11:41:36 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Add the parent property.
 * 09-Jan-03    Phil W-S        VBM:2003010902 - Add protocol property,
 *                              including constructor initialization. Removed
 *                              protocol parameter from process.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;

/**
 * A base class for elements of importance found when traversing a DOM to
 * perform DOM transformation. DOM transformation is actioned in two steps via
 * the preprocess and process methods.
 * 
 * The original DOM element that is being represented by an instance of this
 * class can be retrieved using the getElement method.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public abstract class TransElement {

    /**
     * The protocol which generated the DOM to be processed.
     */
    protected DOMProtocol protocol;

    /**
     * The factory used to factor any required state objects.
     *
     * @supplierRole factory
     */
    private TransFactory factory;

    /**
     * The trans element to which this element belongs.
     *
     * @supplierRole parent
     */
    private TransElement parent;

    /**
     * The context belonging to and associated with this trans element.
     *
     * @supplierRole context
     */
    private TransContext context;

    /**
     * Tracks the element's depth in the trans element hierarchy. This is
     * for use within the trackDepth method.
     */
    private int depth = 0;

    /**
     * Initializes the new instances using the given parameters.
     *
     * @param protocol the protocol which generated the DOM to be processed
     */
    protected TransElement(DOMProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * This operation performs the DOM transformation processing required for
     * the associated DOM element. It is expected to be called after
     * preprocess has been invoked. Must be overridden by concrete
     * specializations. 
     */
    public abstract void process();

    /**
     * Returns the factory association.
     *
     * @return the factory association
     */
    public TransFactory getFactory() {
        return factory;
    }

    /**
     * Allows the factory association to be set or updated.
     *
     * @param factory the new factory for the element
     */
    public void setFactory(TransFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns the parent association.
     *
     * @return the parent association
     */
    public TransElement getParent() {
        return parent;
    }

    /**
     * Allows the parent association to be set or updated.
     *
     * @param parent the new parent for the element. This can be null
     */
    public void setParent(TransElement parent) {
        this.parent = parent;
    }

    /**
     * Allows access to the DOM element represented by this instance. Must
     * be overridden by concrete specializations.
     *
     * @return the DOM element represented by this TransElement
     */
    public abstract Element getElement();

    /**
     * Returns the context belonging to and associated with this trans
     * element. The context is lazily created if required.
     *
     * @return the context for this TransElement
     */
    public TransContext getContext() {
        if (context == null) {
            // Lazy initialization of the context
            context = getFactory().getContext(null);
        }

        return context;
    }

    /**
     * Returns the depth attribute.
     *
     * @return the depth of this element in the trans element hierarchy
     */
    public int getDepth(){
        return depth;
    }

    /**
     * Allows the depth attribute to be updated.
     *
     * @param depth the new depth for this element in the trans element
     *        hierarchy
     */
    public void setDepth(int depth){
        this.depth = depth;
    }

    /**
     * In order to support the correct pre-processing of elements, a truely
     * depth-first traversal of the tree is required. This method should be
     * invoked to allow the element to track how deep its part of the
     * hierarchy goes (if the element represents a non-leaf node of the
     * hierarchy).
     * 
     * It is expected that this method will rely on the getDepth method on
     * any child elements.
     */
    public abstract void trackDepth();

    /**
     * Required pre-processing of the DOM, prior to actual transformation,
     * is performed by this operation. Must be overridden by concrete
     * specializations.
     */
    public abstract void preprocess();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
