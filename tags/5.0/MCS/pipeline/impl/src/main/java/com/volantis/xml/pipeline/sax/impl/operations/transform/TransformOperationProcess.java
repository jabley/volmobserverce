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
package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLContentManager;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.cache.GenericCache;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.ResourceNotFoundException;
import com.volantis.xml.pipeline.sax.XMLHandlerAdapter;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;
import com.volantis.xml.pipeline.sax.url.PipelineURLContentManager;
import com.volantis.xml.pipeline.sax.url.URLConfigurationFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An operation process used to perform an actual xsl transformation
 */
public class TransformOperationProcess
        extends AbstractOperationProcess
        implements ErrorListener {
    
    /**
     * Used to indicate whether any content has been transformed
     */
    private boolean hasTransformStarted = false;

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(TransformOperationProcess.class);

    /**
     * Used to localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    TransformOperationProcess.class);

    /**
     * A reference to the ContentHandler that this process is fowarding any
     * events to.
     */
    private ContentHandler contentHandler;

    /**
     * A list that will contain any TransformerHandlers that have been
     * registered
     */
    private List templatesList = new ArrayList();

    /**
     * Twin list for containing the compile option of the matching entry in the
     * templatesList
     */
    private List compilableList = new ArrayList();

    /**
     * A reference to a TemplatesHandler that is used to process inline
     * xsl templates in order to create a transformer
     */
    private TemplatesHandler templatesHandler;

    /**
     * A reference to the correct value for the compilable attribute for the
     * transform element immediately surrounding this one i.e.
     * <pre>
     * &lt;transform> &lt;!-- This is the transform element immediately surrounding
     *                  the inner one -->
     *    &lt;transformation>
     *        &lt;transform>
     *           &lt;!-- This is the current transform element -->
     *        &lt;/transform>
     *    &lt;/transformation>
     * &lt;/transform>
     * <pre>
     */
    private Boolean outerCompilable;

    /**
     * Stores the configuration for this process
     */
    TransformConfiguration configuration;

    /**
     * The standard SAXTransformerFactory.
     */
    private static SAXTransformerFactory xsltFactory;

    /**
     * The compiling SAXTransformerFactory.
     */
    private static SAXTransformerFactory xsltcFactory;

    /**
     * List of names of the parameters to add to each Transformer.
     */
    private List paramNames = new ArrayList();

    /**
     * List of values of the parameters to add to each Transformer.
     */
    private List paramValues = new ArrayList();

        
    /**
     * Listener for collecting transformer errors.
     */
    private CollatingErrorListener collatingErrorListener; 

    /**
     * Add a parameter name and value to the lists of parameters to add to each
     * Transformer.
     * @param name The name of the parameter
     * @param value The value of the parameter
     */
    public void addParameter(String name, Object value) {
        paramNames.add(name);
        paramValues.add(value);
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        // get hold of the pipeline context
        XMLPipelineContext context = getPipelineContext();

        // get hold of the pipeline configuration
        XMLPipelineConfiguration pipelineConfiguration =
                context.getPipelineConfiguration();

        configuration = (TransformConfiguration)
                pipelineConfiguration.retrieveConfiguration(
                        TransformConfiguration.class);

        if (configuration == null) {
            // cannot get hold of the configuration. As this is fatal
            // deliver a fatal error down the pipeline
            XMLPipelineException error = new XMLPipelineException(
                    "Could not retrieve the transform configuration",
                    context.getCurrentLocator());

            try {
                pipeline.getPipelineProcess().fatalError(error);
            } catch (SAXException e) {
                // cannot continue so throw a runtime exception
                throw new ExtendedRuntimeException(e);
            }
        }
    }

    /**
     * Return the number of templates that this process is using in order to
     * perform the transformation
     * @return the template count
     */
    public int getTemplateCount() {
        return templatesList.size();
    }

    /**
     * Set the ContentHandler that this process is fowarding any content
     * events to.
     * @param contentHandler the ContentHandler to set
     */
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    /**
     * Get the ContentHandler that this process is fowarding any content
     * events to.
     * @return the ContentHandler that this process is fowarding any content
     * events to.
     * @throws IllegalStateException if a href attribute is not set on this
     * element or if it does not contain a nested transformation element
     */
    public ContentHandler getContentHandler() {
        if (contentHandler == null) {
            throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("transform-href-not-specified"));
        }
        return contentHandler;
    }
    
    /**
     * Get CollatingErrorListener instance. 
     * @return instance of CollatingErrorListener class.
     */
    private CollatingErrorListener getCollatingErrorListener(){
        if(this.collatingErrorListener == null){
            this.collatingErrorListener = new CollatingErrorListener();
        }
        return this.collatingErrorListener;
    }

    /**
     * Common code for handling exceptions and redirecting to fatalError.
     * @param e
     */
    private void handleException(Exception e,String absoluteURI)throws SAXException {
        StringBuffer errorMessages = this.getCollatingErrorListener().getErrorBuffer();
        this.getCollatingErrorListener().resetErrorBuffer();
        Locator locator = getPipelineContext().getCurrentLocator();
        SAXParseException error =
                new XMLProcessingException("Error while attempting to process: "+
                        absoluteURI + "\ndue to following errors: "+
                        errorMessages,locator, e);
        fatalError(error);

    }

    /**
     * Pass in a uri to a template definition that will be used to perform
     * the transformation
     * @param templateURI String representation of the template URI. This can
     * be relative.
     * @param compilable The value of the compilable attribute in either the
     * transform or transformation elements if present.
     * @param transformation A flag stating if the current element being
     * processed is a transformation or transform element.
     * @throws SAXException
     */
    public void addTemplate(String templateURI, Boolean compilable,
                            boolean transformation)
            throws SAXException {
        String absoluteURI = templateURI; // just in case of exception
        try {
            Templates templates = null;

            absoluteURI = resolveHref(templateURI);
            boolean isCompilable = isCompilable(compilable, transformation);

            boolean isCached = configuration.isTemplateCacheRequired();
            if (isCached) {
                TransformCacheKey key = new TransformCacheKey(isCompilable,
                        absoluteURI);
                GenericCache cache = (GenericCache)
                        ((DefaultTransformConfiguration) configuration)
                                .getTemplateCache();
                synchronized (cache) {
                    templates = (Templates) cache.get(key);
                    if (templates == null) {
                        templates = createTemplates(absoluteURI, isCompilable);
                        cache.put(key, templates);
                    }
                }
            } else {
                templates = createTemplates(absoluteURI, isCompilable);
            }
            addTemplates(templates, Boolean.valueOf(isCompilable));
        } catch (MalformedURLException e) {
            StringBuffer errorMessages = this.getCollatingErrorListener().getErrorBuffer();
            this.getCollatingErrorListener().resetErrorBuffer();
            Locator locator = getPipelineContext().getCurrentLocator();
            SAXParseException error =
                    new ResourceNotFoundException("Error while attempting to process: "+
                            absoluteURI + "\ndue to following errors: "+
                            errorMessages, locator, e);
            fatalError(error);
        } catch (SAXException e) {
            handleException(e,absoluteURI);
        } catch (TransformerConfigurationException e) {
            handleException(e,absoluteURI);
        } catch (IOException e) {
            handleException(e,absoluteURI);
        }
    }

    /**
     * Generate a {@link Templates} instance which describes the stylesheet
     * found at the given URI.
     *
     * @param absoluteURI   location of the stylesheet to use to populate the
     *                      Templates
     * @param isCompilable  true if we should use xsltc, and false if we should
     *                      just use xslt
     * @return Templates which encapsulate the information given in the
     * stylesheet at the specified URI.
     *
     * @throws IOException if there was a problem creating the Templates
     * @throws SAXException if there was a problem creating the Templates
     * @throws TransformerConfigurationException if there was a problem
     * creating the Templates
     */
    private Templates createTemplates(String absoluteURI, boolean isCompilable)
            throws IOException, SAXException, TransformerConfigurationException {

        // Get the URLContent manager.
        final XMLPipelineContext context = getPipelineContext();
        URLContentManager manager = PipelineURLContentManager.retrieve(context);

        // Get the content of the template, no timeout is supplied as there
        // is no way to specify an operation specific timeout so it uses the
        // default one associated with the manager.
        final URL url = new URL(absoluteURI);
        final URLConfiguration urlConfig =
            URLConfigurationFactory.getURLConfiguration(url, context);
        URLContent content = manager.getURLContent(url, null, urlConfig);

        InputStream connectionInput = null;
        try {
            connectionInput = content.getInputStream();
            StreamSource xslSource = new StreamSource(connectionInput,
                    absoluteURI);
            SAXTransformerFactory factory = getTransformerFactory(isCompilable);
            factory.setErrorListener(this.getCollatingErrorListener());
            return factory.newTemplates(xslSource);
        } finally {
            // Ensure that we close the input stream
            if (connectionInput != null) {
                connectionInput.close();
            }
        }
    }

    /**
     * Pass in a Templates that will be used to create a TransformerHandler
     * that will perform the transformation
     * @param templates the Templates
     * @param compilable this is saved so that we know which TransformerFactory
     * to use with the Templates to create the TransformerHandler.  If it is
     * {@link Boolean#TRUE} then we should use a compiling TransformerFactory
     */
    private void addTemplates(Templates templates, Boolean compilable) {
        templatesList.add(templates);
        if (compilable == null) {
            compilable = Boolean.FALSE;
        }
        compilableList.add(compilable);
    }

    /**
     * Helper method used to resolve relative URIs
     * @param href the URI to resolve
     * @return the absolute URI
     * @throws MalformedURLException if URL is malformed
     */
    private String resolveHref(String href) throws MalformedURLException {
        URL base = getPipelineContext().getCurrentBaseURI();
        URL resolvedURL;
        if (base == null) {
            resolvedURL = new URL(href);
        } else {
            resolvedURL = new URL(base, href);
        }
        return resolvedURL.toExternalForm();
    }

    /**
     * Install a TemplatesHandler as the ContentHandler for this process so
     * that inline xsl templates can be processed
     * @param compilable The value of the compilable attribute in either the
     * transform or transformation elements if present.
     * @param transformation A flag stating if the current element being
     * processed is a transformation or transform element.
     * @throws SAXException if error encoutered
     */
    public void loadTemplatesHandler(Boolean compilable,
                                     boolean transformation)
            throws SAXException {
        try {
            boolean isCompilable
                    = isCompilable(compilable, transformation);
            templatesHandler = getTransformerFactory(isCompilable)
                    .newTemplatesHandler();
            setContentHandler(templatesHandler);
            templatesHandler.startDocument();
        } catch (TransformerConfigurationException e) {
            Locator locator = getPipelineContext().getCurrentLocator();
            String message = "Unable to load TemplatesHandler";
            fatalError(new XMLStreamingException(message, locator, e));
        }
    }

    /**
     * Uninstall the TemplatesHandler that is processing template content
     * instructions. If a TemplatesHandler has not been loaded vi a
     * call to loadTemplatesHandler() a IllegalStateException will be
     * thrown
     * @param compilable The value of the compilable attribute in either the
     * transform or transformation elements if present.
     * @param transformation A flag stating if the current element being
     * processed is a transformation or transform element.
     * @throws SAXException if error encoutered
     */
    public void unloadTemplatesHandler(Boolean compilable,
                                       boolean transformation)
            throws SAXException {

        XMLPipelineContext context = getPipelineContext();
        if (null == templatesHandler && !context.inErrorRecoveryMode()) {
            throw new IllegalStateException("No templates handler to unload");
        }

        if (templatesHandler != null) {
            templatesHandler.endDocument();
            Templates templates = templatesHandler.getTemplates();
            addTemplates(templates, compilable);
            templatesHandler = null;
            setContentHandler(getNextProcess());
        }
    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
        getContentHandler().setDocumentLocator(locator);
    }

    /**
     * We need to block incoming startDocument() events. The TRAX
     * objects that this process delegates to, use startDocument() events
     * to trigger initialization. This process needs control when these TRAX
     * based ContentHandlers are initialized, therefore incoming
     * startDocument events are blocked.
     */
    public void startDocument() throws SAXException {
        getContentHandler().startDocument();
        // block startDocument() events
    }

    /**
     * We need to block incoming endDocument() events. The TRAX
     * objects that this process delegates to, use endDocument() events
     * to trigger processing. This process needs control when these TRAX
     * based ContentHandlers run, therefore incoming
     * endDocument events are blocked.
     */
    public void endDocument() throws SAXException {
        getContentHandler().endDocument();
        // block endDocument() events
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        getContentHandler().startPrefixMapping(prefix, uri);
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        getContentHandler().endPrefixMapping(prefix);
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        getContentHandler().startElement(namespaceURI, localName, qName, atts);
    }

    // javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        getContentHandler().endElement(namespaceURI, localName, qName);
    }

    // javadoc inherited
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        getContentHandler().characters(ch, start, length);
    }

    // javadoc inherited
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {
        getContentHandler().ignorableWhitespace(ch, start, length);
    }

    // javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        getContentHandler().processingInstruction(target, data);
    }

    // javadoc inherited
    public void skippedEntity(String name) throws SAXException {
        getContentHandler().skippedEntity(name);
    }

    /**
     * Helper method to return a SAXTransformerFactory
     * @param compilable This flag determines whether we return an xsltc
     * SAXTransformerFactory or a normal xslt SAXTransformerFactory
     * @return a SAXTransformerFactory
     * @throws SAXException if an error occures
     */
    private SAXTransformerFactory getTransformerFactory(boolean compilable)
            throws SAXException {

        /* There is also another factory called
        * org.apache.xalan.xsltc.trax.SmartTransformerFactoryImpl. This is an
        * implementation of a transformer factory that uses an XSLTC
        * transformer factory for the creation of javax.xml.transform.Templates
        * objects and uses the Xalan processor transformer factory for the
        * creation of javax.xml.transform.Transformer objects. For now we will
        * replicate the functionality that PS wrote but we might look at using
        * SmartTransformerFactoryImpl in the future.
        */
        // NOTE: the transformer factory class must be hardcoded rather than
        // looked up by JAXP on our behalf since our repackaged Xalan is
        // (deliberately) not registered with JAXP.
        TransformerFactory transFactory;
        if (compilable) {
            if (logger.isDebugEnabled()) {
                logger.debug("Using xsltc transforms");
            }

            if (xsltcFactory == null) {
                xsltcFactory = new com.volantis.xml.xalan.
                        xsltc.trax.TransformerFactoryImpl();
            }
            transFactory = xsltcFactory;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Using xslt transforms");
            }

            if (xsltFactory == null) {
                xsltFactory = new com.volantis.xml.xalan.
                        processor.TransformerFactoryImpl();
            }
            transFactory = xsltFactory;
        }

        if (!transFactory.getFeature(SAXSource.FEATURE)) {
            Locator locator = getPipelineContext().getCurrentLocator();
            String message = "Unable to obtain a SAXTransformerFactory";
            fatalError(new XMLProcessingException(message, locator));
        }
        return (SAXTransformerFactory) transFactory;
    }

    /**
     * Private helper that determines whether we use xslt or xsltc
     * @param compilable The value of the compilable attribute in either the
     * transform or transformation elements if present.
     * @param transformation A flag stating if the current element being
     * processed is a transformation or transform element.
     * @return boolean is true if we should use xsltc.
     */
    private boolean isCompilable(Boolean compilable, boolean transformation) {
        // We default to not compiling xslt transforms.
        boolean isCompilable = false;

        // The value of compilable attribute in mariner-config.xml is the
        // master switch that determines whether we attempt to compile anything
        // at all i.e. if the value in mariner-config.xml is set to false then
        // no transforms will be compiled.
        if (configuration.isTemplateCompilationRequired()) {
            if (compilable != null) {
                isCompilable = compilable.booleanValue();
            } else {
                // The compilable attribute was not present in this element so
                // if this is a transformation element we'll check the parent
                // transform element.
                if (transformation) {
                    XMLPipelineContext pipelineContext = getPipelineContext();
                    Object o = pipelineContext.getProperty("TransformOperation");
                    if (o != null) {
                        isCompilable = ((Boolean) o).booleanValue();
                    }
                }
            }
        }

        return isCompilable;
    }

    /**
     * Call this method when we start a new transform element to ensure that
     * the correct value for the compilable attribute is used by the nested
     * elements.
     */
    public void pushCompilableAttribute(Boolean compilable) {

        XMLPipelineContext pipelineContext = getPipelineContext();
        Object o = pipelineContext.getProperty("TransformOperation");
        if (o != null) {
            // There is a transform element surrounding this one.
            outerCompilable = (Boolean) o;
        } else {
            // There is no transform element outside of this one
            outerCompilable = null;
        }

        pipelineContext.setProperty("TransformOperation", compilable, false);
    }

    /**
     * Call this method when we end a transform element to ensure that the
     * correct value for the compilable attribute is used by the nested
     * elements.
     */
    public void popCompilableAttribute() {
        // Restore the value of the compilable attribute for the OUTER transform
        // element to the XMLPipelineContext
        XMLPipelineContext pipelineContext = getPipelineContext();
        pipelineContext.setProperty(
                "TransformOperation", outerCompilable, false);

    }

    /**
     * This method should be called when all templates have been loaded
     * and the next SAX content encountered will be the content that
     * is to be transformed.
     * @throws SAXException if an error occurs
     */
    public void beginTransform() throws SAXException {
        XMLPipelineContext context = getPipelineContext();
        if (!context.inErrorRecoveryMode()) {
            // run through the list of Transformer handlers and set up the
            // transformations

            XMLProcess cup = new ContextManagerProcess(true);
            cup.setPipeline(getPipeline());
            final XMLProcess next = getNextProcess();
            setNextProcess(cup);
            cup.setNextProcess(next);

            TransformerHandler transformerHandler = null;
            ContentHandler handler = getNextProcess();
            for (int i = templatesList.size() - 1; i >= 0; i--) {
                Templates templates = (Templates) templatesList.get(i);

                Boolean compilable = (Boolean) compilableList.get(i);
                SAXTransformerFactory factory =
                        getTransformerFactory(compilable.booleanValue());

                try {
                    transformerHandler = factory.newTransformerHandler(templates);
                    for (int j = 0; j < paramNames.size(); j++) {
                        String name = (String) paramNames.get(j);
                        Object value = paramValues.get(j);
                        transformerHandler.
                                getTransformer().setParameter(name, value);
                    }
                } catch (TransformerConfigurationException e) {
                    Locator locator = getPipelineContext().getCurrentLocator();
                    String message = "Unable to create new TransformHandler";
                    fatalError(new XMLStreamingException(message, locator, e));
                }

                transformerHandler.setResult(new SAXResult(handler));
                handler = transformerHandler;
            }

            XMLHandlerAdapter adapter = new XMLHandlerAdapter();
            adapter.setContentHandler(transformerHandler);

            // See VBM:2007031521. The CAP needs to obtain the namespace context
            // from the next CAP in the pipeline. It simply copies the next
            // CAPs namespaces to itself to ensure that default namespaces (and
            // others will work). 
            ContextAnnotatingProcess cap = new ContextAnnotatingProcess(true);
            if (next instanceof ContextAnnotatingProcess) {
                ContextAnnotatingProcess nextCAP = (ContextAnnotatingProcess) next;
                nextCAP.copyNamespacePrefixes(cap);
            }

            cap.setPipeline(getPipeline());
            cap.setNextProcess(adapter);

            setContentHandler(cap);
            cap.startProcess();
            hasTransformStarted = true;
            // now that we have linked all the transformer handlers together
            // make sure this operation process sends its Content events through
            // the chain of transformers in order to transform the content
            //setContentHandler(transformerHandler);
            // the transformer does no work without this call.
            // the DocumentEventFilter will filter this out.
            //transformerHandler.startDocument();
        }
    }

    /**
     * This method should be called when all content that requires
     * transforming has been received.
     * @throws SAXException if an error occurs
     */
    public void endTransform() throws SAXException {
        XMLPipelineContext context = getPipelineContext();
        if (!context.inErrorRecoveryMode()) {
            if (getTemplateCount() > 0) {
                // contentHandler will be the TransformerHandler at the head of
                // the chain. Need to call endDocument() for the transformers to
                // actually perform the transformations.
                // The DocumentEventFilter will filter the endDocument() event
                // out of the pipeline.
                if (!hasTransformStarted) {
                    beginTransform();
                }

                if (getContentHandler() instanceof ContextAnnotatingProcess) {
                    ContextAnnotatingProcess cap =
                            (ContextAnnotatingProcess) getContentHandler();
                    cap.stopProcess();
                }
            }
        }
        // we always want the content handler to be the next process (even
        // if we are in error recovery mode.
        setContentHandler(getNextProcess());
    }

    // javadoc inherited
    public void error(TransformerException e) throws TransformerException {
        try {
            // pass the error on down the pipeline
            error(new XMLPipelineException(
                    "Transformation failed",
                    getPipelineContext().getCurrentLocator(),
                    e));
        } catch (SAXException se) {
            // could perhaps throw a new TransformerException. However,
            // re-throwing the original should be OK.
            throw e;
        }
    }

    // javadoc inherited
    public void fatalError(TransformerException e) throws TransformerException {
        try {
            // pass the error on down the pipeline
            fatalError(new XMLPipelineException(
                    "Transformation failed",
                    getPipelineContext().getCurrentLocator(),
                    e));
        } catch (SAXException se) {
            // could perhaps throw a new TransformerException. However,
            // re-throwing the original should be OK.
            throw e;
        }
    }
    
    // javadoc inherited
    public void warning(TransformerException e) throws TransformerException {
        try {
            // pass the error on down the pipeline
            warning(new XMLPipelineException(
                    "Transformation failed",
                    getPipelineContext().getCurrentLocator(),
                    e));
        } catch (SAXException se) {
            // could perhaps throw a new TransformerException. However,
            // re-throwing the original should be OK.
            throw e;
        }
    }

    // javadoc inherited from interface
    public void release() {
        templatesList.clear();
        compilableList.clear();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 29-Sep-05	9663/1	doug	VBM:2005080416 Ensured transform operation can handle empty transforms

 28-Sep-05	9596/4	doug	VBM:2005080416 Ensured transform process correctly tries to perform a transform even if no content was received

 27-Sep-05	9596/2	doug	VBM:2005080416 Ensured transform process only trys to perform a transform if it has actually received content to transform

 26-May-05	8546/1	matthew	VBM:2005051804 Add localized exception for when a href or nested tranformation element have not been supplied

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Apr-04	692/1	adrian	VBM:2004042603 Add parameter passing functionality to transform process

 30-Apr-04	689/1	adrian	VBM:2004043003 Added pipeline jsps for transform parameters

 30-Apr-04	686/4	adrian	VBM:2004042802 Fixed some rework issues with transform parameters

 30-Apr-04	686/2	adrian	VBM:2004042802 Add parameter support for transforms

 13-Apr-04	667/1	adrian	VBM:2004040713 Fixed transform template caching.

 13-Apr-04	665/1	adrian	VBM:2004040713 Fixed transform template caching.

 13-Apr-04	662/1	adrian	VBM:2004040713 Fixed transform template caching.

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 30-Jan-04	531/2	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 27-Jan-04	551/8	claire	VBM:2004012204 Fixed cache synchronization

 26-Jan-04	551/6	claire	VBM:2004012204 Caching clean-up

 26-Jan-04	551/4	claire	VBM:2004012204 Fixed and optimised caching code

 26-Jan-04	551/2	claire	VBM:2004012204 Implementing caching for transforms

 30-Dec-03	489/6	doug	VBM:2003120807 fixed merge problem

 19-Dec-03	489/3	doug	VBM:2003120807 Ensured that our current xml processes are recoverable when inside a try op

 19-Dec-03	491/3	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements (add export jars)

 18-Dec-03	491/1	geoff	VBM:2003121715 debrand config file

 06-Nov-03	450/1	geoff	VBM:2003110604 Prevent third party code picking up our repackaged XML tools via JAXP.

 31-Oct-03	440/2	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 08-Aug-03	268/11	chrisw	VBM:2003072905 Made pushing compilable attribute consistent for both jsp and xml markup

 07-Aug-03	268/9	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	268/6	chrisw	VBM:2003072905 implemented compilable attribute on transform

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 23-Jun-03	107/3	doug	VBM:2002121803 Transform Adapter tidy up

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 16-Jun-03	23/6	byron	VBM:2003022819 Update to get jsp TLD files with correct merge

 13-Jun-03	23/4	byron	VBM:2003022819 Integration complete

 ===========================================================================
*/
