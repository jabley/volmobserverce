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

package com.volantis.mcs.xdime;

import org.xml.sax.Locator;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MetaData;
import com.volantis.mcs.xdime.XDIMEContext;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;

import java.util.Map;

/**
 * Internal interface for the XDIME context.
 *
 * @mock.generate
 */
public interface XDIMEContextInternal
        extends XDIMEContext {

    /**
     * Sets the initial MarinerRequestContext.
     *
     * @param context MarinerRequestContext
     */
    void setInitialRequestContext(MarinerRequestContext context);

    /**
     * Gets the initial MarinerRequestContext
     *
     * @return MarinerRequestContext
     */
    MarinerRequestContext getInitialRequestContext();

    /**
     * Sets the document {@link org.xml.sax.Locator}.
     *
     * @param locator the document locator to set
     */
    void setDocumentLocator(Locator locator);

    /**
     * Returns the {@link org.xml.sax.Locator} used in this context.
     *
     * @return the document Locator used in this context
     */
    Locator getDocumentLocator();

    void pushElement(XDIMEElement element);

    XDIMEElementInternal popElement();

    /**
     * Returns the element currently at the top of the element stack and
     * ensures that it is an XDIMEElementImpl.
     *
     * @return XDIMEElementImpl that is currently on the top of the element
     *         stack
     * @throws IllegalStateException if the element was not an
     *                               {@link XDIMEElementInternal}
     */
    XDIMEElementInternal getCurrentElement();

    /**
     * Returns the {@link ExpressionContext} used to execute an expression in
     * this context.
     *
     * @return the expression context used in this context
     */
    ExpressionContext getExpressionContext();

    /**
     * Set the {@link com.volantis.xml.pipeline.sax.flow.FlowControlManager} which can be used to suppress SAX
     * events at the root of a pipeline.
     *
     * @param flowControlManager
     */
    void setFlowControlManager(FlowControlManager flowControlManager);

    /**
     * Return the {@link com.volantis.xml.pipeline.sax.flow.FlowControlManager} to use in this context.
     *
     * @return the flow control manager to be used in this context.
     */
    FlowControlManager getFlowControlManager();

    /**
     * Returns true if we should abort the processing of this document.
     *
     * @return true if we should abort the processing of this document, false
     *         otherwise
     */
    boolean isAbort();

    /**
     * Sets whether we should abort the processing of this document.
     *
     * @param abort true if we should abort the processing of this document,
     *              false otherwise.
     */
    void setAbort(boolean abort);

    /**
     * Get the {@link com.volantis.mcs.xdime.xforms.model.XFormBuilder} which should be used to build xform
     * models in this context.
     */
    XFormBuilder getXFormBuilder();

    /**
     * Get the validator to use to validate the document.
     *
     * @return The document validator.
     */
    DocumentValidator getDocumentValidator();

    void enteringXDIMECPElement();

    void exitingXDIMECPElement();

    /**
     * Returns the meta data that holds the page scope meta informations.
     *
     * @return the meta data
     */
    MetaData getPageMetaData();

    /**
     * Returns the meta data that holds the element scope meta informations for
     * the element with the specified ID.
     *
     * @param id the ID of the element, must not be null
     * @return the meta data
     */
    MetaData getElementMetaData(String id);
    
    /**
     * Adds specified element to the list of singleton elements, unless another
     * element of the same type has already been added.
     * 
     * @param element The element to add.
     * @return <tt>true</tt> if this is the first added element of its type,
     *         <tt>false</tt> otherwise.
     */
    boolean addSingletonElement(XDIMEElementInternal element) throws XDIMEException;
    
    /**
     * Returns singleton element by its type. If no element of the
     * specified type was added yet, it returns <tt>null</tt>.
     * 
     * @param type The type of the element.
     * @return The singleton element of specified type.
     */
    XDIMEElementInternal getSingletonElement(ElementType type) throws XDIMEException;
    
    /**
     * Processes specified element for possible default element of its type.
     * 
     * There can be at most one default element for given element type. If no
     * element of the same type was processed yet, the specified element becomes
     * the default element of its type. If there already is a default element of
     * the same type, it's not default anymore.
     * 
     * @param element The element to process.
     * @throws XDIMEException
     */
    void processElementForDefault(XDIMEElementInternal element) throws XDIMEException;
    
    /**
     * Returns the default element of specified type, if it exists.
     * 
     * @param type The element type to get default element for.
     * @return The default element, or null.
     * @throws XDIMEException
     */
    XDIMEElementInternal getDefaultElement(ElementType type) throws XDIMEException;

     /**
      * Returns map of key - targetId elements
      * 
      * @return The map of elements
      */

    Map getIdToAccessKeyMap();

}
