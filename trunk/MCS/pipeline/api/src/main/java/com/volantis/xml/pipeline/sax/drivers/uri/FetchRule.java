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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-03-03     Doug            VBM:2002091803 - Created. An AdapterProcess for
 *                              URL connections.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.uri;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A FetchAdapterProcess is an AdapterProcess that includes another
 * Document that is specified via a URL.
 *
 * <p>This is used by DSB.</p>
 *
 * @volantis-api-include-in InternalAPI
 */
public class FetchRule
        extends DynamicElementRuleImpl {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new FetchRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(FetchRule.class);

    /**
     * Identifier for the  parse attribute associated with a URLConnector
     * process
     */
    private static final String PARSE_ATTRIBUTE = "parse";

    /**
     * Identifier for the href attribute associated with a URLConnector
     * process
     */
    private static final String HREF_ATTRIBUTE = "href";

    /**
     * Identifier for the encoding attribute associated with a URLConnector
     * process
     */
    private static final String ENCODING_ATTRIBUTE = "encoding";

    /**
     * Identifier for the encoding attribute associated with a fetch process
     */
    private static final String TIMEOUT_ATTRIBUTE = "timeout";

    /**
     * Creates a new URLConnectorProcess
     */
    public FetchRule() {
    }

    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        Fetcher operation = new Fetcher(dynamicProcess.getPipeline());

        // OK to pass null strings to operations attribute setters
        // Operation process can validate the attributes whenever
        // it performs its operation
        operation.setHref(attributes.getValue(HREF_ATTRIBUTE));
        operation.setParse(attributes.getValue(PARSE_ATTRIBUTE));
        operation.setEncoding(attributes.getValue(ENCODING_ATTRIBUTE));

        // Handle the timeout setting. Firstly, we need to see if there is a
        // timeout attribute. We default to -1 to indicate no timeout
        String timeoutAttr = attributes.getValue(TIMEOUT_ATTRIBUTE);
        Period timeout = null;

        if (timeoutAttr != null) {
            // The attribute should contain an integer number of seconds
            int timeoutInSecs;
            try {
                timeoutInSecs = Integer.parseInt(timeoutAttr);
            } catch (NumberFormatException e) {
                LOGGER.warn("invalid-timeout", timeoutAttr);
                throw e;
            }

            timeout = Period.treatNonPositiveAsIndefinitely(
                    timeoutInSecs * 1000);
    }

        operation.setTimeout(timeout);

        operation.doInclude();
        
        return null;
    }

    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 24-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	301/2	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 18-Jul-03	213/2	doug	VBM:2003071615 Refactored XMLProcess interface

 24-Jun-03	109/4	philws	VBM:2003061913 Change pipeline:includeURI to urid:fetch and add new TLD for it

 ===========================================================================
*/
