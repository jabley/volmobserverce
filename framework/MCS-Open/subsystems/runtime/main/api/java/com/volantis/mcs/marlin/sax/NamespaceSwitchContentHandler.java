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
 
package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.ProjectStack;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.project.BaseURLTracker;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.runtime.project.ProjectTracker;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.cornerstone.stack.Stack;
import com.volantis.synergetics.cornerstone.stack.ArrayListStack;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.HashMap;

/**
 * This MCSInternalContentHandler provides a mechanism to switch the underlying
 * content handler based upon the namespace of the current element. A default
 * handler is provided on construction, and handlers for specific namespaces
 * should then be added as required.
 *
 * <p>Correct initialisation of this class requires at a minimum that the
 * {@link #setInitialRequestContext} and {@link #setDocumentLocator} methods
 * are invoked in that order.</p>
 */
public class NamespaceSwitchContentHandler
        implements MCSInternalContentHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(
                        NamespaceSwitchContentHandler.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    NamespaceSwitchContentHandler.class);

    /**
     * A map from namespace to the corresponding ContentHandler.
     */
    protected NamespaceSwitchContentHandlerMap contentHandlers;

    /**
     * Map of the namespaces already encountered against the content handler
     * used to process it.
     */
    protected HashMap cachedContentHandlers;

    /**
     * The current content handler to delegate to.
     */
    protected MCSInternalContentHandler currentHandler;

    /**
     * The default content handler to delegate to.
     */
    protected MCSInternalContentHandler defaultHandler;

    /**
     * We must maintain a stack of the state (current handler and nesting
     * depth)
     */
    protected Stack stateStack;

    /**
     * Keep track of the depth of the nesting.
     */
    protected int nestingDepth = 0;

    /**
     * The document locator.
     */
    private Locator locator;

    private MarinerRequestContext requestContext;

    private ProjectTracker projectTracker;

    /**
     * Create a new NamespaceSwitchContentHandler.
     *
     * @param defaultHandler the default ContentHandler to delegate to.
     */
    public NamespaceSwitchContentHandler(
            MCSInternalContentHandler defaultHandler) {

        this.defaultHandler = defaultHandler;
        currentHandler = defaultHandler;

        // We could get the NamespaceSwitchContentHandler directly,
        // but there might be issues in some appservers with loading of
        // static objects from different class loaders, so we make sure we
        // get the one from the volantis bean.
      //  MarinerPageContext pageContext =
      //      ContextInternals.getMarinerPageContext(getCurrentRequestContext());
      //  contentHandlers =
      //      pageContext.getVolantisBean().getNamespaceSwitchContentHandlerMap();

        contentHandlers = NamespaceSwitchContentHandlerMap.getInstance();                      
        stateStack = new ArrayListStack();
        cachedContentHandlers = new HashMap();

        // initialize the marlin-cdm namespace with the default handler
        cachedContentHandlers.put(XDIMESchemata.CDM_NAMESPACE, defaultHandler);
    }


    /**
     * Get the ContentHandler associated with the specified namespace. If no
     * namespace is specified then the default handler will be used. If an
     * unrecognised handler is specified an exception will be thrown.
     *
     * @param namespaceURI The namespace of the element being processed.
     * @return The content handler for the specified namespace, otherwise the
     * default handler.
     */
    protected MCSInternalContentHandler getHandler(String namespaceURI)
            throws SAXException {

        MCSInternalContentHandler result = null;

        if (namespaceURI == null || namespaceURI.equals("")) {
            // use the default handler for backwards compatibility if no
            // namespace is specified.
            result = defaultHandler;
        } else {
            // check and see if there is a cached content handler for this namespace
            result = (MCSInternalContentHandler)cachedContentHandlers.
                    get(namespaceURI);
            if (result == null) {
                // otherwise try and create the appropriate handler
                AbstractContentHandlerFactory handlerFactory =
                        contentHandlers.getContentHandlerFactory(namespaceURI);
                if (logger.isDebugEnabled()) {
                    logger.debug("Looking up content handler for " + namespaceURI);
                    logger.debug("Factory returned is " + handlerFactory);
                }

                if (handlerFactory != null) {
                    result = handlerFactory.createContentHandler(
                            getInitialRequestContext());
                    result.setDocumentLocator(getDocumentLocator());
                    String[] handledNamespaces =
                            handlerFactory.getHandledNamespaces();
                    for (int i = 0; i < handledNamespaces.length; i++) {
                        cachedContentHandlers.put(handledNamespaces[i], result);
                    }
                }
            }
        }

        // if no handler has been found then a non null, unknown namespace was
        // specified, and this should cause an exception to be thrown.
        if (result == null) {
            throw new SAXException(exceptionLocalizer.format(
                    "unknown-namespace", namespaceURI));
        }

        return result;
    }

    // Javadoc inherited from ContentHandler interface
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes saxAttributes)
            throws SAXException {
        MCSInternalContentHandler handler = getHandler(namespaceURI);

        if (handler != currentHandler) {
            ContentHandlerState state =
                    new ContentHandlerState(currentHandler, nestingDepth);
            stateStack.push(state);
            currentHandler = handler;
            nestingDepth = 0;
        }

        // Update the project (and base URL) for the element.
        String systemId = locator.getSystemId();
        projectTracker.startElement(systemId);

        currentHandler.startElement(namespaceURI, localName, qName,
                saxAttributes);

        nestingDepth++;
    }

    // Javadoc inherited from ContentHandler interface
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        currentHandler.endElement(namespaceURI, localName, qName);
//        currentHandler = defaultHandler;

        // Update the project (and base URL) for the element.
        String systemId = locator.getSystemId();
        projectTracker.endElement(systemId);

        nestingDepth--;
        // if the depth is zero then we should restore the previous state.
        // except if the state stack is empty.  in that case we must have
        // reached the root element close event so we don't need to do anything
        if (nestingDepth == 0 && !stateStack.isEmpty()) {
            ContentHandlerState state =
                    (ContentHandlerState)stateStack.pop();
            state.restoreState();
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void startDocument() throws SAXException {
        if (currentHandler != null) {
            currentHandler.startDocument();
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void endDocument() throws SAXException {
        if (currentHandler != null) {
            currentHandler.endDocument();
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        if (currentHandler != null) {
            currentHandler.startPrefixMapping(prefix, uri);
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void endPrefixMapping(String prefix) throws SAXException {
        if (currentHandler != null) {
            currentHandler.endPrefixMapping(prefix);
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void characters(char[] characters, int start, int length)
            throws SAXException {
        if (currentHandler != null) {
            currentHandler.characters(characters, start, length);
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void ignorableWhitespace(char[] characters, int start, int length)
            throws SAXException {
        if (currentHandler != null) {
            currentHandler.ignorableWhitespace(characters, start, length);
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void processingInstruction(String target, String data)
            throws SAXException {
        if (currentHandler != null) {
            currentHandler.processingInstruction(target, data);
        }
    }

    // Javadoc inherited from ContentHandler interface
    public void skippedEntity(String name) throws SAXException {
        if (currentHandler != null) {
            currentHandler.skippedEntity(name);
        }
    }

    // javadoc inherited from MCSInternalContentHandler.
    public MarinerRequestContext getCurrentRequestContext() {
        return (currentHandler != null)
                ? currentHandler.getCurrentRequestContext()
                : defaultHandler.getCurrentRequestContext();
    }

    // Javadoc inherited from MCSInternalContentHandler.
    public void setInitialRequestContext(MarinerRequestContext requestContext) {
        if (this.requestContext != null) {
            throw new IllegalStateException("Cannot set initial request " +
                    "context multiple times");
        }

        this.requestContext = requestContext;

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(requestContext);

        // Create the base URL tracker and store it in the page context.
        BaseURLTracker baseURLTracker =
                new BaseURLTracker(pageContext.getRequestURL(false));
        pageContext.setBaseURLProvider(baseURLTracker);

        // Create the project tracker.
        ProjectManager projectManager = pageContext.getProjectManager();
        ProjectStack projectStack = pageContext.getProjectStack();
        projectTracker = new ProjectTracker(baseURLTracker, projectManager,
                projectStack);

        defaultHandler.setInitialRequestContext(requestContext);
    }

    // Javadoc inherited from MCSInternalContentHandler.
    public Locator getDocumentLocator() {
        return locator;
    }

    // Javadoc inherited
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;

        if (currentHandler != null) {
            currentHandler.setDocumentLocator(locator);
        }
        defaultHandler.setDocumentLocator(locator);
    }

    /**
     * Returns the request context set with setInitialRequestContext
     *
     * Note that it's not necessarily the current context
     */
    protected MarinerRequestContext getInitialRequestContext() {
        return requestContext;
    }

    /**
     * Implementation of ContentHandlerState that switches the state of the
     */
    private class ContentHandlerState {

        /**
         * The MCSInternalContentHandler to restore to the current handler.
         */
        private MCSInternalContentHandler handler;

        /**
         * The current nesting depth.
         */
        private int depthCount = 0;

        /**
         * Construct a new SwitchState.
         * @param handler The handler to restore on {@link #restoreState}
         * @param depth The depth of elements in the
         */
        public ContentHandlerState(MCSInternalContentHandler handler, int depth) {
            this.handler = handler;
            this.depthCount = depth;
        }

        // javadoc inherited.
        public void restoreState() {
            currentHandler = handler;
            nestingDepth = depthCount;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 05-Sep-05	9391/6	emma	VBM:2005082604 Handling empty string namespaces

 02-Sep-05	9391/4	emma	VBM:2005082604 Supermerge required

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 16-Feb-04	2966/1	ianw	VBM:2004011923 Fixed namespace issues

 02-Feb-04	2802/2	ianw	VBM:2004011921 Added mechanism to enable AppServer interfaces to configure NamespaceSwitchContentHandler

 27-Aug-03	1253/1	doug	VBM:2003082202 Restructured MarlinContentHandler class hierarchy

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
