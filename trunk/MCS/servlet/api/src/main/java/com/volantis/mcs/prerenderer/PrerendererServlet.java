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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.prerenderer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.volantis.mcs.application.ApplicationContextFactory;
import com.volantis.mcs.application.ApplicationRegistry;
import com.volantis.mcs.application.ApplicationRegistryContainer;
import com.volantis.mcs.application.PrerendererApplication;
import com.volantis.mcs.context.PrerendererPackageContext;

/**
 * The servlet controlling package prerendering process.
 */
public class PrerendererServlet extends HttpServlet {

    /**
     * The name of the attribute, under which an instance of current
     * PrerendererPackageContext is bound to the HttpSession.
     */
    protected static final String PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME = 
        PrerendererPackageContext.class.getName();
    
    /**
     * The prerenderer application used by this servlet.
     * Note: this field is not null only for the purpose of unit tests. 
     */
    private PrerendererApplication prerendererApplication;

    /**
     * If true, application registration is skipped.
     */
    private boolean skipApplicationRegistration = false;

    /**
     * @inheritDoc
     */
    public void init(ServletConfig config) throws ServletException {
        registerPrerendererApplication();
    }
    
    /**
     * @inheritDoc
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Step 1: Get current prerendering context.
        HttpSession session = request.getSession();
        
        PrerendererPackageContext prerendererPackageContext = 
            getPrerendererPackageContext(session);

        // Step 2: Get list of resources and prepare XML response from it.
        Map resources = prerendererPackageContext.getIncrementalRewrittenURIMap();

        Document document = new Document();

        Element resourcesElement = new Element("resources");

        document.setRootElement(resourcesElement);

        Iterator iterator = resources.keySet().iterator();

        while (iterator.hasNext()) {
            URI sourceURI = (URI) iterator.next();

            URI rewrittenURI = (URI) resources.get(sourceURI);

            Element resourceElement = new Element("resource");

            Element remoteElement = new Element("remote");

            remoteElement.setText(sourceURI.toString());

            resourceElement.addContent(remoteElement);

            Element localElement = new Element("local");

            localElement.setText(rewrittenURI.toString());

            resourceElement.addContent(localElement);

            resourcesElement.addContent(resourceElement);
        }

        // Write the response out.
        XMLOutputter outputter = createXMLOutputter();
        
        outputter.output(document, response.getWriter());

        // Invalidate the session, if there were no more new resources to send.
        if (resources.isEmpty()) {
            releasePrerenderingContext(session);
            
            session.invalidate();
        }
    }

    /**
     * @inheritDoc
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Step 1: Create an array of remote page URIs from request XML.
            List pageURIs = new ArrayList();

            SAXBuilder builder = new SAXBuilder();

            Document document = builder.build(request.getReader());

            Element pagesElement = document.getRootElement();
            
            String baseURIString = pagesElement.getAttributeValue("base");
            
            URI baseURI = new URI(baseURIString);
            
            String prefixPathURIString = pagesElement.getAttributeValue("prefix-path");
            
            if (prefixPathURIString == null) prefixPathURIString = "/";
            
            URI prefixPathURI = new URI(prefixPathURIString);
            
            List pageElements = pagesElement.getChildren();

            Iterator pageElementsIterator = pageElements.iterator();

            while (pageElementsIterator.hasNext()) {
                Element pageElement = (Element) pageElementsIterator.next();

                String uriString = pageElement.getText();

                URI uri = new URI(uriString);

                pageURIs.add(uri);
            }

            // Step 2: Create PrerendererPackageContext initialized with the
            // list of remote page URIs. The PrerendererPackageContext throws
            // IllegalArgumentException in case any of the URIs in not valid. In
            // case of error, send HTTP 400 error with exception message.
            PrerendererPackageContext prerendererPackageContext = createPrerendererPackageContext(
                        request, pageURIs, baseURI, prefixPathURI);
            
            // Step 3: Get the list of rewritten page URIs.
            List rewrittenPageURIs = prerendererPackageContext.getRewrittenPageURIs();

            // Step 4: Prepare the XML response.
            Document outputDocument = new Document();

            Element outputPagesElement = new Element("pages");

            outputDocument.setRootElement(outputPagesElement);

            for (int index = 0; index < pageURIs.size(); index++) {
                URI rewrittenPage = (URI) rewrittenPageURIs.get(index);

                Element outputPageElement = new Element("page");

                outputPageElement.addContent(rewrittenPage.toString());

                outputPagesElement.addContent(outputPageElement);
            }

            // Step 5: Write the response out.
            XMLOutputter outputter = createXMLOutputter();
            
            outputter.output(outputDocument, response.getWriter());
            
        } catch (IllegalArgumentException e) {
            // This exception may be thrown when creating an instance of
            // PrerenderPackageContext, in case of invalid URIs.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage());
            
        } catch (JDOMException e) {
            // This exception may be thrown when request XML is not well formed.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage());

        } catch (URISyntaxException e) {
            // This exception may be thrown when some of the URIs included in the request are invalid.
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage());
            
            // TODO: What about XML validation?
        }
    }
    
    /**
     * Creates new PrerenderingContext and associates it with specified session.
     * 
     * @param session The session to associate created context.
     * @throws IllegalStateException if another context is already associated
     *             with specified session.
     */
    private PrerendererPackageContext createPrerendererPackageContext(
            HttpServletRequest request, List pageURIs, URI baseURI, URI prefixPathURI) {
        HttpSession session = request.getSession();
        
        if (session.getAttribute(PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME) != null) {
            throw new IllegalStateException(
                    "PrerenderingContext already created.");
        }

        PrerendererPackageContext prerenderingContext = getPrerendererApplication()
                .createPrerendererPackageContext(pageURIs, baseURI, prefixPathURI);

        session.setAttribute(PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME,
                prerenderingContext);

        return prerenderingContext;
    }

    /**
     * Releases PrerenderingContext associated with specified session.
     * 
     * @param session The session.
     * @throws IllegalStateException if no context is currently associated with
     *             specified session.
     */
    private void releasePrerenderingContext(HttpSession session) {
        if (session.getAttribute(PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME) == null) {
            throw new IllegalStateException("No PrerenderingContext.");
        }

        session.removeAttribute(PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME);
    }

    /**
     * Returns current PrerenderingContext associated with specified session.
     * 
     * @param session The session.
     * @throws IllegalStateException if no context is currently associated with
     *             specified session.
     */
    private PrerendererPackageContext getPrerendererPackageContext(
            HttpSession session) {
        PrerendererPackageContext context = (PrerendererPackageContext) session
                .getAttribute(PRERENDERER_PACKAGE_CONTEXT_ATTRIBUTE_NAME);

        if (context == null) {
            throw new IllegalStateException("No current PrerenderingContext.");
        }

        return context;
    }
    
    private void registerPrerendererApplication() {
        // Create servlet and internal application context factory.
        // TODO: There's no internal context factory, because
        // it's not needed for now.
        if (!skipApplicationRegistration) {
            ApplicationContextFactory servletContextFactory = new ServletPrerendererApplicationContextFactory();
            ApplicationContextFactory internalContextFactory = null;

            // Create registry container with servlet and internal context
            // factories.
            ApplicationRegistryContainer registryContainer = new ApplicationRegistryContainer();
            registryContainer
                    .setInternalApplicationContextFactory(internalContextFactory);
            registryContainer
                    .setServletApplicationContextFactory(servletContextFactory);

            // Register application.
            ApplicationRegistry.getSingleton().registerApplication("prerenderer",
                    registryContainer);
        }
    }
    
    /**
     * Creates and returns an instance of XMLOutputter for use by this servlet.
     * 
     * @return created instancce of XMLOutputter.
     */
    private XMLOutputter createXMLOutputter() {
        XMLOutputter outputter = new XMLOutputter();

        /*
        outputter.setNewlines(true);

        outputter.setIndent("  ");
        */
        
        return outputter;
    }
    
    /**
     * Sets the prerenderer application for use by this servlet.
     * 
     * Note: This method exists only to make this servlet more testable.
     * 
     * @param application The application to set.
     */
    protected void setPrerendererApplication(PrerendererApplication application) {
        this.prerendererApplication = application;
    }
    
    /**
     * Returns the prerenderer application used by this servlet.
     * 
     * @return the prerenderer application used by this servlet.
     */
    private PrerendererApplication getPrerendererApplication() {
        if (prerendererApplication == null) {
            return PrerendererApplication.getInstance();
        } else {
            return prerendererApplication;
        }
    }

    /**
     * Skip application registration.
     * 
     * Note: This method exists only to make this servlet it testable.
     * Reason: Don't know how to test application registration yet.
     */
    protected void skipApplicationRegistration() {
        this.skipApplicationRegistration = true;
    }
}
