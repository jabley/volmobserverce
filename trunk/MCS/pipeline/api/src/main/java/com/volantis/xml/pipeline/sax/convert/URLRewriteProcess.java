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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.convert;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * An XMLProcess that rewrites urls according to a
 * URLRewriteProcessConfiguration.
 */
public class URLRewriteProcess extends XMLProcessImpl {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2004. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(URLRewriteProcess.class);
    
    /**
     * The configuration associated with this process.
     */
    private URLRewriteProcessConfiguration configuration;

    /**
     * Construct a new URLRewriteProcess with no pre-defined
     * URLRewriteProcessConfiguration. Instances created with this
     * constructor will expect a URLRewriteProcessConfiguration to be
     * available from the pipeline configuration at runtime.
     */
    public URLRewriteProcess() {
    }

    /**
     * Construct a new URLRewriteProcess with a pre-defined
     * URLRewriteProcessConfiguration. Instances created with this
     * constructor will not try to acquire a URLRewriteProcessConfiguration
     * from the pipeline configuration at runtime.
     */
    public URLRewriteProcess(URLRewriteProcessConfiguration configuration) {
        this.configuration = configuration;
    }

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);

        // We only look for the configuration in the pipeline config if it
        // has not been set on constuction.
        if (configuration == null) {

            // Get hold of the pipeline context
            XMLPipelineContext context = getPipelineContext();

            // Get hold of the pipeline configuration
            XMLPipelineConfiguration pipelineConfiguration =
                    context.getPipelineConfiguration();

            // Retrieve the configuration
            Configuration config =
                    pipelineConfiguration.retrieveConfiguration(
                            URLRewriteProcessConfiguration.class);

            if (config == null ||
                    !(config instanceof URLRewriteProcessConfiguration)) {
                // Cannot get hold of the configuration. As this is fatal
                // deliver a fatal error down the pipeline
                XMLPipelineException error = new XMLPipelineException(
                        "Could not retrieve the URL rewrite process " +
                        "configuration",
                        context.getCurrentLocator());

                try {
                    pipeline.getPipelineProcess().fatalError(error);
                } catch (SAXException e) {
                    // cannot continue so throw a runtime exception
                    throw new ExtendedRuntimeException(e);
                }
            }
            // Cast the configuration to the correct type and store it away
            configuration = (URLRewriteProcessConfiguration)config;
        }
        // We only need a cup when baseuri could be changed by the URLRewriteProcess.
        //
        // If there is no baseuri ConverterTuple in the configuration then it
        // will never be changed. We can modify the startProcess() method to
        // only add a cup if there is a baseuri ConverterTuple.
        //
        // This change has the knock-on affect that another process such as
        // HTTPRequestOperationProcess does not know if URLRewriteProcess will
        // add a cup. This means that HTTPRequestOperationProcess should always
        // add a cup.
        //
        // This change is better because it means that HTTPRequestOperationProcess
        // no longer makes the assumption that URLRewriteProcess will add a cup
        // so it removes this behavioural coupling. We also preserve
        // efficiency because the cup will only be added by URLRewriteProcess
        // when it has to.

        // todo this is commented out since the mechanism to associate a the 'base' attribute with any element isn't catered for by the current framework.
        //        boolean baseuriExists = configuration.getConverterConfiguration().
        //                getTuples(NamespaceSupport.XMLNS,
        //                        ContextManagerProcess.BASE_ATTRIBUTE) != null;
        //        if (!baseuriExists) {
        XMLProcess cup = getPipelineContext().
                getPipelineFactory().createContextUpdatingProcess();
        cup.setPipeline(getPipeline());
        cup.setNextProcess(next);
        setNextProcess(cup);
        //        }

    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attrs) throws SAXException {

        Attributes newAttrs = attrs;

        // we must get right mode from process configuration
        ConverterTuple[] tuples =
                configuration.getConverterConfiguration().
                getTuplesByMode(namespaceURI, localName,
                        configuration.getProcessMode());

        if (isConversionNecessary(tuples, attrs)) {

            // Clone the existing attributes.
            AttributesImpl clonedAttrs = new AttributesImpl(attrs);
            int index;
            String attr;

            // Iterate over the tuples and rewrite for the attributes that
            // they specify.
            for (int i = 0; i < tuples.length; i++) {

                attr = tuples[i].getAttribute();

                if ((index = clonedAttrs.getIndex(attr)) != -1) {
                    final String newValue =
                            configuration.getURLPrefixRewriteManager().
                            findAndExecuteRule(clonedAttrs.getValue(index),
                                               getPipelineContext().
                                               getCurrentBaseURI().
                                               toExternalForm());

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
        boolean conversionNecessary = false;

        if (configuration != null && tuples != null) {
            // Check to see if any of the required attributes are listed in
            // the tuples found for this namespace/element combination
            for (int i = 0; !conversionNecessary && (i < tuples.length); i++) {
                conversionNecessary =
                        attrs.getValue(tuples[i].getAttribute()) != null;
            }
        }

        return conversionNecessary;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Sep-04	740/1	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 28-May-04	715/5	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 28-May-04	715/3	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 27-May-04	715/1	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 26-May-04	708/1	allan	VBM:2004052102 Provide a URL rewriting process.

 ===========================================================================
*/
