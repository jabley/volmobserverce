package com.volantis.xml.pipeline.sax.drivers.googledocs;

import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestOperationProcess;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestType;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPException;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.pipeline.localization.LocalizationFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Convenience class containing behaviour common for ListDocsRule and FetchDocRule
 * 
 */
abstract class GDocsRule extends DynamicElementRuleImpl {

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(GDocsRule.class);

    XMLPipelineContext context;
    Authenticator authenticator;
    AuthData authData;

    private String id;
    protected String getXmlId() {
        return id;
    }

    /**
     * Identifier of operation, needed to report errors
     */
    protected final String ATTRIBUTE_ID = "xml:id";

    // Javadoc inherited
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {
        HashMap<String, String> atts = gatherAndValidateAttributes(attributes);
        String id = attributes.getValue(ATTRIBUTE_ID);
        if (id == null) {
            throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-invalid", ATTRIBUTE_ID), null);
        }
        atts.put(ATTRIBUTE_ID, id);
        return atts;
    }

    // Javadoc inherited
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        try {
            XMLPipeline pipeline = dynamicProcess.getPipeline();
            context = dynamicProcess.getPipelineContext();
            authData = AuthData.retrieve(context);
            this.id = ((HashMap<String,String>)object).get(ATTRIBUTE_ID);
            if (authData == null) {
                throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("gdocs-authentication-data-missing"),null);
            }

            authenticator = new Authenticator(dynamicProcess, authData, id);
            authenticator.authenticate();
            final String requestUrl = prepareRequestUrl((HashMap)object);
            performOperation(pipeline, requestUrl);
        } finally {
            if (authData != null) {
                authData.clear(context);
            }
            this.id = null;
        }
    }

    /**
     * Called from endElement.
     * All the operation must be performed in endElement, because information gathered in child element (see {@link AuthenticateRule})
     * is not availble at the moment of calling startElement.
     *
     * @param pipeline Pipeline object
     * @param urlString url Request prepated by {@link #prepareRequestUrl}
     *
     * @throws SAXException thrown from the inside
     */
    private void performOperation(final XMLPipeline pipeline, final String urlString) throws SAXException {
        HTTPRequestOperationProcess operation = new HTTPRequestOperationProcess() {
                protected void processResponse(String redirectURL,
                                   InputStream response,
                                   int statusCode,
                                   String contentType,
                                   String contentEncoding) throws IOException, SAXException {

                    if (!processSpecificResponse(redirectURL,
                                   response,
                                   statusCode,
                                   contentType,
                                   contentEncoding,
                                   pipeline,
                                   urlString)) {
                        super.processResponse(redirectURL,
                                   response,
                                   statusCode,
                                   contentType,
                                   contentEncoding);
                    }
                }
        };
        operation.setFollowRedirects("false");
        operation.setNextProcess(getTargetProcess(pipeline));
        operation.setPipeline(pipeline);
        operation.setUrlString(urlString);

        operation.setRequestType(HTTPRequestType.GET);
        operation.stopProcess();
    }

    /**
     *  This method is called from startElement.
     *
     * @param attributes passed to startElement
     * @return HashMap containing attribute keys and values. Its is then made available for {@link #endElement}
     *  
     * @throws com.volantis.xml.pipeline.sax.XMLPipelineException
     */
    protected abstract HashMap<String, String> gatherAndValidateAttributes(Attributes attributes) throws XMLPipelineException;

    /**
     * Prepares request URL to be called on Google Docs.
     * 
     * This method is called in endElement.
     *
     * @param attributes HashMap gathered in {@link #gatherAndValidateAttributes}
     *  
     * @return composed request URL
     */
    protected abstract String prepareRequestUrl(HashMap<String, String> attributes);

    /**
     * This metothod processes reponse with specified HTTP status code.
     *
     * Here, HTTP 403 reposnses are processed. It can be overriden in derived classes, to add more specific status codes,
     * but those implementations should always include call to this method (super#processSpecificResponse(...) )
     *
     * @param redirectURL see {@link HTTPRequestOperationProcess#processResponse} 
     * @param response see {@link HTTPRequestOperationProcess#processResponse}
     * @param statusCode see {@link HTTPRequestOperationProcess#processResponse}
     * @param contentType see {@link HTTPRequestOperationProcess#processResponse}
     * @param contentEncoding see {@link HTTPRequestOperationProcess#processResponse}
     * @param pipeline Current pipeline object, passed {@link #performOperation}. It is necessary to repeat performing opertaion.
     * @param urlString urlString passed to {@link #performOperation}. It is necessary to repeat performing operation.
     * 
     * @return true if response has been processed, false if not an it should be processed higher.
     * @throws SAXException exception thrown by subsequent performOperation
     **/
    protected boolean processSpecificResponse(String redirectURL,
                                   InputStream response,
                                   int statusCode,
                                   String contentType,
                                   String contentEncoding,
                                   XMLPipeline pipeline, String urlString) throws SAXException {

        if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
            //we need to clean authData, try to authorize once again, and perform operation second time
            //authenticator takes care of counting number of requests to Google, so they are not performed infinitely
            authData.setNeedsRequest(true);
            authenticator.authenticate();
            performOperation(pipeline, urlString);
            return true;
        } else {
            return false;
        }
    }
}
