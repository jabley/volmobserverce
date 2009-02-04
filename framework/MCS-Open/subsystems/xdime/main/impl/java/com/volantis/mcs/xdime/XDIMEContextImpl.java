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

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MetaData;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.validation.XDIME2CompiledSchema;
import com.volantis.mcs.xdime.xforms.model.XFormBuilder;
import com.volantis.mcs.xdime.xforms.model.XFormBuilderImpl;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.mcs.xml.schema.validation.SchemaValidationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import org.xml.sax.Locator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Implementation of {@link XDIMEContext}.
 */
public class XDIMEContextImpl implements XDIMEContextInternal {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    XDIMEContextImpl.class);

    private static final SchemaValidationFactory SCHEMA_VALIDATION_FACTORY =
            SchemaValidationFactory.getDefaultInstance();

    /**
     * This is a stack of {@link XDIMEElement}s.
     */
    private Stack elementStack;

    /**
     * This is the expression context for this request.
     */
    private ExpressionContext expressionContext;

    /**
     * This is the mariner request context for this request.
     */
    private MarinerRequestContext requestContext;

    /**
     * The Document Locator for this context.
     */
    private Locator locator;

    /**
     * Reference to a FlowControlManager that can be used to suppress
     * SAX events at the root of a pipeline. This will be null if not running
     * in a pipeline
     */
    private FlowControlManager flowControlManager;

    /**
     * Flag to indicate that we should abort processing this document.
     */
    private boolean abort;

    /**
     * The {@link XFormBuilder} that should be used when building xform
     * models in this context.
     */
    private XFormBuilder xformBuilder;

    private DocumentValidator documentValidator;
    
    /**
     * The map storing singleton elements, keyed by element type. Instantiated
     * on first access.
     */
    private HashMap singletonMap; 

    /**
     * The map storing default elements, keyed by element type. Instantiated
     * on first access.
     */
    private HashMap defaultElementsMap;

    /**
     * The map storing mapping key to targetId for Access elements type.
     * Instantiated on first access.
     */

    private HashMap idAccessKeyMap;

    private ContextWriter writer;
    
    /**
     * A marker put in the defaultElementsMap indicating, that there is no
     * default element, and that there'll be no default element anymore.
     */
    private static final Object NO_DEFAULT_ELEMENT_MARKER = new Object(){};  

    /**
     * Default Constructor.
     */
    public XDIMEContextImpl() {
        initialiseStack();

        documentValidator = SCHEMA_VALIDATION_FACTORY.createDocumentValidator(
                XDIME2CompiledSchema.getCompiledSchema());

        writer = new ContextWriter(this);
    }

    // Javadoc inherited.
    public void setInitialRequestContext(MarinerRequestContext context) {
        this.requestContext = context;
    }

    // Javadoc inherited.
    public MarinerRequestContext getInitialRequestContext() {
        return requestContext;
    }

    // Javadoc inherited.
    public FastWriter getContentWriter() {
        return writer;
    }

    // Javadoc inherited.
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    // Javadoc inherited.
    public Locator getDocumentLocator() {
        return locator;
    }

    /**
     * Initialises the element stack.
     */
    private void initialiseStack() {
        elementStack = new Stack();
    }

    /**
     * Returns the stack of elements pushed onto the context.
     *
     * @return the stack of elements for this context.
     */
    public Stack getStack() {
        return elementStack;
    }

    // Javadoc inherited.
    public void pushElement(XDIMEElement element) {
        elementStack.push(element);
    }

    // Javadoc inherited.
    public XDIMEElementInternal popElement() {
        return (XDIMEElementInternal) elementStack.pop();
    }

    // Javadoc inherited.
    public XDIMEElementInternal getCurrentElement() {
        if (elementStack.isEmpty()) {
            return null;
        }
        
        Object element = elementStack.peek();
        if (!(element instanceof XDIMEElementInternal)) {
            throw new IllegalStateException(
                    exceptionLocalizer.format("xdime-bad-stack"));
        }
        return (XDIMEElementInternal)element;
    }

    // Javadoc inherited.
    public ExpressionContext getExpressionContext() {
        if (expressionContext == null) {
            expressionContext = ContextInternals.getEnvironmentContext(
                    requestContext).getExpressionContext();
        }
        return expressionContext;
    }

    // Javadoc inherited.
    public void setFlowControlManager(FlowControlManager flowControlManager) {
        this.flowControlManager = flowControlManager;
    }

    // Javadoc inherited.
    public FlowControlManager getFlowControlManager() {
        return flowControlManager;
    }

    // Javadoc inherited.
    public boolean isAbort() {
        return abort;
    }

    // Javadoc inherited.
    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    // Javadoc inherited.
    public XFormBuilder getXFormBuilder() {
        if (xformBuilder == null) {
            xformBuilder = new XFormBuilderImpl();
        }
        return xformBuilder;
    }

    // Javadoc inherited.
    public DocumentValidator getDocumentValidator() {
        return documentValidator;
    }

    public void enteringXDIMECPElement() {
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(requestContext);
        pageContext.enteringXDIMECPElement();
    }

    public void exitingXDIMECPElement() {
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(requestContext);
        pageContext.exitingXDIMECPElement();
    }

    // javadoc inherited
    public MetaData getPageMetaData() {
        final MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(requestContext);
        return pageContext.getPageMetaData();
    }

    // javadoc inherited
    public MetaData getElementMetaData(final String id) {
        final MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(requestContext);
        return pageContext.getElementMetaData(id);
    }

    /**
     * Returns map storing singleton elements keyed by element type. If
     * <code>create</code> is <code>true</code>, then the map is created if
     * it does not exist yet.
     * 
     * @param create The creation flag
     * @return The singleton map
     */
    private Map getSingletonMap(boolean create) {
        if (singletonMap == null && create) {
            singletonMap = new HashMap();
        }
        
        return singletonMap;
    }
            
    // Javadoc inherited
    public boolean addSingletonElement(XDIMEElementInternal element) {
        Map map = getSingletonMap(true);
        
        ElementType type = element.getElementType();
        
        if (map.containsKey(type)) {
            return false;
        } else {
            map.put(element.getElementType(), element);
        
            return true;
        }
    }

    // Javadoc inherited
    public XDIMEElementInternal getSingletonElement(ElementType type) {
        XDIMEElementInternal element = null;
        
        Map map = getSingletonMap(false);
        
        if (map != null) {
            Object object = map.get(type);
            
            if (object != null) {
                element = (XDIMEElementInternal) object;
            }
        }
        
        return element;
    }

    /**
     * Returns map storing default elements keyed by element type. If
     * <code>create</code> is <code>true</code>, then the map is created if
     * it does not exist yet.
     * 
     * @param create The creation flag
     * @return The singleton map
     */
    private Map getDefaultElementsMap(boolean create) {
        if (defaultElementsMap == null && create) {
            defaultElementsMap = new HashMap();
        }
        
        return defaultElementsMap;
    }
            
    /**
     * Processes specified element for defaults.
     * 
     * If it is the first processed element of its type, it becomes the default
     * element of its type. If this is second or subsequent element of its type, 
     * there'll be no default element for that type anymore.
     * 
     * @param element The element to process.
     * @throws XDIMEException 
     */
    public void processElementForDefault(XDIMEElementInternal element) throws XDIMEException {
        Map defaultElementsMap = getDefaultElementsMap(true);
        
        if (!defaultElementsMap.containsKey(element.getElementType())) {
            defaultElementsMap.put(element.getElementType(), element);
        }
    }

    /**
     * Returns default element for specified type, if exists.
     * 
     * @param type The type of the element.
     * @return The default element for specified type.
     * @throws XDIMEException 
     */
    public XDIMEElementInternal getDefaultElement(ElementType type) throws XDIMEException {
        XDIMEElementInternal element = null;
        
        Map defaultElementsMap = getDefaultElementsMap(false);
        
        if (defaultElementsMap != null) {
            Object value = defaultElementsMap.get(type);
            
            if (value != null && value != NO_DEFAULT_ELEMENT_MARKER) {
                element = (XDIMEElementInternal) value;
            }
        }
        
        return element;
    }



	// Javadoc inherited
	public Map getIdToAccessKeyMap() {
		if (idAccessKeyMap == null) {
			idAccessKeyMap = new HashMap();
		}
		return idAccessKeyMap;
	}



}
