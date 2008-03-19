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

package com.volantis.xml.pipeline.sax.convert;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This process should have a configuration that identifies the
 * namespace/element/attribute tuples where URLs could be found that require
 * conversion.
 * <p>
 * A null namespace registration in this configuration will mean
 * "all namespaces", whereas an empty string namespace will mean "no (or
 * default) namespace".
 * <p>
 * The conversion will happen in startElement SAX events only. Because an
 * attribute value must be modified, the immutable attributes provided to the
 * event must be cloned into a mutable version when conversion is required.
 *
 * JSP -> Same assumption as per ConvertElementCase.
 *
 * <p>
 * MCS -> will register the process with the required namespace and local
 * name, and will currently configure this process to look for URLs in:
 *
 * <pre>
 * [namespace] [element]  [attribute]
 *  (null)       a          href
 *  (null)      form       action
 * </pre>
 * <p>
 * The configuration of this process also provides a mechanism to define a
 * global baseURL and substitution path.  These configuration values are used
 * where a baseURL or substitution path are not provided as attributes on the
 * process markup.
 */
public class AbsoluteToRelativeURLOperationProcess
        extends AbstractOperationProcess {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(
                        AbsoluteToRelativeURLOperationProcess.class);

    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The baseURL used in the conversion.
     */
    private String baseURL;

    /**
     * The substitution path which if not null is prefixed on resolved relative
     * urls.
     */
    private String substitutionPath;

    /**
     * The configuration associated with this process.
     */
    private AbsoluteToRelativeURLConfiguration configuration;

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        // get hold of the pipeline context
        XMLPipelineContext context = getPipelineContext();

        // get hold of the pipeline configuration
        XMLPipelineConfiguration pipelineConfiguration =
                context.getPipelineConfiguration();

        // retrieve the configuration
        Configuration config =
                pipelineConfiguration.retrieveConfiguration(
                        AbsoluteToRelativeURLConfiguration.class);

        if (config == null ||
                !(config instanceof AbsoluteToRelativeURLConfiguration)) {
            // cannot get hold of the configuration. As this is fatal
            // deliver a fatal error down the pipeline
            XMLPipelineException error = new XMLPipelineException(
                    "Could not retrieve the Absolute to Relative URL " +
                    "converter configuration",
                    context.getCurrentLocator());

            try {
                pipeline.getPipelineProcess().fatalError(error);
            } catch (SAXException e) {
                // cannot continue so throw a runtime exception
                throw new ExtendedRuntimeException(e);
            }
        }
        // cast the configuration to the correct type and store it away
        configuration = (AbsoluteToRelativeURLConfiguration)config;
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attrs) throws SAXException {

        Attributes newAttrs = attrs;

        ConverterTuple[] tuples =
                configuration.getTuples(namespaceURI, localName);

        if (isConversionNecessary(tuples, attrs)) {

            // Clone the existing attributes.
            AttributesImpl clonedAttrs = new AttributesImpl(attrs);
            int index;
            String attr;

            // Scoot through the attributes and do the Absolute to Relative
            // conversion for those attributes that actually need it.
            for (int i = 0; i < tuples.length; i++) {

                attr = tuples[i].getAttribute();

                if ((index = clonedAttrs.getIndex(attr)) != -1) {
                    // Reset the value to the new URLC value
                    final String newValue = convertAbsoluteURLToRelativeURL(
                            getBaseURL(), getSubstitutionPath(),
                            clonedAttrs.getValue(index));

                    clonedAttrs.setValue(index, newValue);
                }
            }
            newAttrs = clonedAttrs;
        }
        super.startElement(namespaceURI, localName, qName, newAttrs);
    }

    /**
     * Determine whether or not the conversion is necessary.
     *
     * @param  tuples the array of ConverterTuple objects.
     * @param  attrs  the attributes.
     * @return        true if the conversion is necessary, false otherwise.
     */
    private boolean isConversionNecessary(ConverterTuple[] tuples,
                                          final Attributes attrs) {
        boolean conversionNecesary = false;
        if (configuration != null) {

            if (tuples != null) {
                // Check to see if any of the required attributes are listed in
                // the rules found for this namespace/element combination
                boolean found = false;

                for (int i = 0; !found && (i < tuples.length); i++) {
                    found = (attrs.getValue(tuples[i].getAttribute()) != null);
                }

                if (!found) {
                    // No appropriate attributes exist, so reset the rules for
                    // later logic
                    tuples = null;
                }
            }
            conversionNecesary = (tuples != null);
        }
        return conversionNecesary;
    }

    /**
     * Perform the actual conversion of the url to a relative url if necessary.
     *
     * @param  baseURL the baseURL to be used to see if it matches the start of
     *                 the url.
     * @param  substitutePath the path that should replace the specified baseURL
     *                 path
     * @param  url     the url to examine to see if it contains the baseURL at
     *                 its start
     * @return         the unchanged url if no match found, or the relative
     *                 url.
     *
     * @todo This implementation is taken verbatim from a version reworked by
     * @todo Profession Services designed to fulfil the requirements of DSB.
     * @todo This overcomes a limitation in the previous implemenation whereby
     * @todo the comparisons between urls did not account for port numbers in
     * @todo the server definition.
     * @todo
     * @todo Due to serious time constraints the decision was taken to take the
     * @todo PS code and paste it into the codebase.  PM will raise a
     * @todo requirement to properly architect and re-implemenet this
     * @todo functionality after GA in early April 2004.
     * @todo
     * @todo If you are tasked with reimplementing this code then please note
     * @todo that it should be addressed in both the Pipeline_2_0 and Pipeline
     * @todo streams (and any versions between, should any be created).
     * @todo
     * @todo If you come across these comments and it is later than May 2004
     * @todo then please bring it to the attention of Colin and Martin J.
     */
    protected String convertAbsoluteURLToRelativeURL(String baseURL,
                                                     String substitutePath,
                                                     String url)
            throws XMLPipelineException {

        String result = url;

        if (logger.isDebugEnabled())
            logger.debug("Entered convert with base "
                         + baseURL + " : url " + url);

        if (baseURL != null && (url != null)) {

            // By assigning the result of resolved to 'result' variable we
            // end up rewriting every URL regardless. This is not correct.
            // If the URL is relative we can ignore it.

            String updatedBaseURL = baseURL;

            int length = baseURL.length();
            if (baseURL.charAt(length - 1) != '/') {
                updatedBaseURL += '/';
                length++;
            }

            // How we change the URL depends on whether its
            // * relative e.g. does not begin with '/' or http
            // * local absolute e.g. begins with '/'
            // * absolute e.g. begings with "http"
            if (result.startsWith("http")) {
                if (logger.isDebugEnabled())
                    logger.debug("Identified Abs URL " + result);

                String altBaseURL = null;
                try {
                    // If the baseURL has port 80 but the result does not
                    // include the port then this check fails therefore we have
                    // to also compare the URL with/without the port
                    URL bURL = new URL(updatedBaseURL);

                    if (bURL.getPort() == -1) {
                        // We create a URL without the port
                        altBaseURL = new URL(bURL.getProtocol(), bURL.
                                                                 getHost(), 80, bURL.getFile()).toExternalForm();
                    } else if (bURL.getPort() == 80) {
                        // We create a URL with the port
                        altBaseURL = new URL(bURL.getProtocol(), bURL.
                                                                 getHost(), bURL.getFile()).toExternalForm();
                    }
                } catch (MalformedURLException mue) {
                    throw new XMLPipelineException("The specified URL was " +
                                                   "not valid", getPipelineContext().
                                                                getCurrentLocator(), mue);
                }

                boolean match = false;

                if (result.startsWith(updatedBaseURL)) {
                    length = updatedBaseURL.length();
                    match = true;
                }

                if (altBaseURL != null && result.startsWith(altBaseURL)) {
                    length = altBaseURL.length();
                    match = true;
                }

                if (match) {
                    int fromPos = (result.charAt(length - 1) != '/' ?
                            length - 1 : length);
                    result = result.substring(fromPos);
                    if (substitutePath != null) {
                        result = substitutePath + result;
                    }
                }

            } else if (result.startsWith("/")) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Identified Local Abs URL " + result);
                }

                // If the prefix path of this URL matches the target mount
                // for this service then we also rewrite this local abs
                //
                try {
                    URL baseURIurl = new URL(baseURL);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Checking prefix for target mount " +
                                     baseURIurl.getFile());
                    }

                    // So if the base URL does not include a path
                    // or if the path matches the specified baseURI path
                    // then we replace it with the substitute path.
                    //
                    String path = baseURIurl.getPath();
                    if ((path.length() == 0) || (result.startsWith(path))) {

                        if (substitutePath != null) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Added substiute prefix "
                                             + substitutePath + " instead of "
                                             + path);
                            }

                            if (path.length() > 0 &&
                                    path.charAt(path.length() - 1) == '/') {
                                path = path.substring(0, path.length() - 1);
                            }

                            result = substitutePath +
                                    result.substring(path.length() + 1);
                        }
                    }

                } catch (MalformedURLException e) {
                    throw new XMLPipelineException("The specified base URL " +
                                                   "was not valid",
                                                   getPipelineContext().getCurrentLocator(), e);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Identified Relative URL " + result);
                }
            }
        }
        return result;
    }

    /**
     * Set the baseURL.
     *
     * @param baseURL the base URL.
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Get the baseURL to use in the conversion process.  A value set as an
     * attribute on the markup takes precedence over the value set in the
     * process configuration.  The configuration may be dynamically updated
     * so any value retrieved from the configuration should not be stored
     * locally.
     * @return the baseURL to use in the conversion process
     */
    protected String getBaseURL() {
        String result = baseURL;
        if (result == null) {
            result = configuration.getBaseURL();
        }
        return result;
    }

    /**
     * Set the subsitution path.
     * @param path the substitution path.
     */
    public void setSubstitutionPath(String path) {
        substitutionPath = path;
    }

    /**
     * Get the substitution path that will be prefixed to the url if it was
     * successfully resolved from absolute to relative.  A substitutionPath
     * that was set on the markup as an attribute takes precedence over the
     * value set in the process configuration.  The configuration may be
     * dynamically updated so any value retrieved from the configuration should
     * not be stored locally.
     * @return the substitution path that will be prefixed to the url if it was
     * successfully resolved.
     */
    protected String getSubstitutionPath() {
        String result = substitutionPath;
        if (result == null) {
            result = configuration.getSubstitutionPath();
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Jun-05	8852/1	pcameron	VBM:2005060604 DSB redirection issues

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Mar-04	656/1	adrian	VBM:2004031602 Updated pipeline with DSB enhancement requests

 30-Mar-04	653/1	adrian	VBM:2004033005 Updated AbsoluteToRelativeURL process to use PS updated for DSB

 23-Mar-04	624/1	adrian	VBM:2004031904 Updated AbsoluteToRelativeURL process

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 31-Jan-04	533/1	adrian	VBM:2004011906 updated AbsoluteToRelativeURL process to resolve against the base url in the pipeline context

 08-Aug-03	308/3	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 ===========================================================================
*/
