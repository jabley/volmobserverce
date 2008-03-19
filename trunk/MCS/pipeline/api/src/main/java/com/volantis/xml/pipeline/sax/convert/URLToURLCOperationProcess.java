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

import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This process converts URLs found in specific namespace/element/attribute
 * tuples into URLCs using a {@link URLConverter}. The tuples and the
 * URLC generator are specified via the associated
 * {@link URLToURLCConfiguration}.
 */
public class URLToURLCOperationProcess extends AbstractOperationProcess {
    /**
     * The configuration for the process
     */
    private URLToURLCConfiguration configuration;

    /**
     * The DMS server as a URL
     */
    private String serverURL = null;

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
                        URLToURLCConfiguration.class);

        if (config == null ||
                !(config instanceof URLToURLCConfiguration)) {
            // cannot get hold of the configuration. As this is fatal
            // deliver a fatal error down the pipeline
            XMLPipelineException error = new XMLPipelineException(
                    "Could not retrieve the URL To URLC converter " +
                    "configuration",
                    context.getCurrentLocator());

            try {
                pipeline.getPipelineProcess().fatalError(error);
            } catch (SAXException e) {
                // cannot continue so throw a runtime exception
                throw new ExtendedRuntimeException(e);
            }
        }

        // cast the configuration to the correct type and store it away
        configuration = (URLToURLCConfiguration)config;
    }

    /**
     * Permits the attribute to be updated. Should be called before the
     * process is started.
     *
     * @param serverURL the new attribute value
     */
    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        super.startProcess();

        if (serverURL == null) {
            fatalError(new XMLProcessingException(
                    "A server URL must be specified",
                    getPipelineContext().getCurrentLocator()));
        } else {
            try {
                // Validate the server URL
                new URL(serverURL);
            } catch (MalformedURLException e) {
                fatalError(new XMLProcessingException(
                        "The server URL must be a well-formed URL",
                        getPipelineContext().getCurrentLocator(),
                        e));
            }
        }
    }

    // javadoc inherited
    public void release() {
        serverURL = null;
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attrs) throws SAXException {
        URLToURLCTuple[] tuples = (URLToURLCTuple[])
                configuration.getTuples(namespaceURI, localName);

        if (tuples != null) {
            // Check to see if any of the required attributes are listed in
            // the rules found for this namespace/element combination
            boolean found = false;

            for (int i = 0;
                 !found && (i < tuples.length);
                 i++) {
                found = (attrs.getValue(tuples[i].getAttribute()) != null);
            }

            if (!found) {
                // No appropriate attributes exist, so reset the rules for
                // later logic
                tuples = null;
            }
        }

        if (tuples == null) {
            // No rules exist that are appropriate to the
            // namespace/element/attribute combinations found in this event
            // so pass it through un-changed
            super.startElement(namespaceURI, localName, qName, attrs);
        } else {
            AttributesImpl newAttrs = new AttributesImpl(attrs);
            int index;
            String attr;
            int replaceIndex;
            String replaceAttr;
            URLConverter convert = configuration.getURLConverter();

            // Scoot through the attributes and do the URL to URLC transcoding
            // for those attributes that actually need it, renaming the
            // attribute(s) as required
            for (int i = 0;
                 i < tuples.length;
                 i++) {
                attr = tuples[i].getAttribute();

                if ((index = newAttrs.getIndex(attr)) != -1) {
                    replaceAttr = tuples[i].getReplacementAttribute();

                    // Reset the value to the new URLC value
                    try {
                        newAttrs.setValue(
                                index,
                                convert.toURLC(getPipelineContext(),
                                               newAttrs.getValue(index),
                                               serverURL));
                    } catch (URLConversionException e) {
                        fatalError(new XMLStreamingException(
                                "URL to URLC conversion failure",
                                getPipelineContext().getCurrentLocator(),
                                e));
                    }

                    // If the attribute is to be renamed, rename it
                    if (replaceAttr != null) {
                        replaceIndex = newAttrs.getIndex(replaceAttr);

                        if ((replaceIndex != -1) && (replaceIndex != index)) {
                            fatalError(new XMLStreamingException(
                                    "URL to URLC conversion failure: attempt to " +
                                    "rename attribute " +
                                    attr + " to attribute " +
                                    replaceAttr + " failed because " +
                                    replaceAttr + " already exists",
                                    getPipelineContext().getCurrentLocator()));
                        } else {
                            // @todo later handle attribute namespaces
                            newAttrs.setLocalName(index, replaceAttr);
                            newAttrs.setQName(index, replaceAttr);
                        }
                    }
                }
            }

            // Pass the modified event through
            super.startElement(namespaceURI, localName, qName, newAttrs);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 08-Aug-03	308/5	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/3	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 07-Aug-03	299/7	philws	VBM:2003080504 Provide the pipeline context to the URLConverter on URLC conversion

 07-Aug-03	299/5	philws	VBM:2003080504 Remove the relativeWidth and maxFileSize attributes from the URL to URLC converter following architectural change

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored URLToURLCConverter process to use DynamicElementRules

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 ===========================================================================
*/
