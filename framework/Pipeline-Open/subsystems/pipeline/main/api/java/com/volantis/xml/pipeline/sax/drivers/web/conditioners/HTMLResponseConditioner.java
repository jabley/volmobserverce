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
package com.volantis.xml.pipeline.sax.drivers.web.conditioners;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import our.w3c.tidy.Configuration;
import our.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * A ContentConditioner that expects HTML from an InputStream and
 * forwards this as SAX events to an XMLProcess. This conditioner must fix up
 * the HTML so that it is valid XML prior forwarding. JTidy is used for this.
 */
public class HTMLResponseConditioner extends XMLResponseConditioner {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(HTMLResponseConditioner.class);

    /**
     * The Tidy from JTidy that does the HTML tidying.
     */
    private final Tidy tidy;

    /**
     * Construct a new XMLResponseConditioner that uses a specified
     * XMLFilter to parse its input. This may need a default constructor later
     * with a setter for the filter.
     * @param filter The filter.
     */
    public HTMLResponseConditioner(XMLFilter filter) {
        tidy = new Tidy();
        tidy.setQuoteNbsp(true);
        tidy.setDocType("omit");
        tidy.setNumEntities(true);
        tidy.setShowWarnings(false);
        // iso latin 1 is the default HTTP/HTML encoding
        tidy.setCharEncoding(Configuration.LATIN1);
        tidy.setQuiet(true);
        tidy.setWraplen(0);
        tidy.setXHTML(true);

        // Slot a nested anchor filter between the filter provided and the
        // reader that will generate the SAX events.
        HTMLNestedAnchorFilter anchorFilter = new HTMLNestedAnchorFilter();
        filter.setParent(anchorFilter);
        setFilter(anchorFilter);
    }

    /**
     * This uses JTidy to tidy the bytes read from the input stream - where
     * an html document is expected - into xml.
     * @todo later Performance is poor since this currently writes the tidied stuff to a buffer simply to be read as an InputStream
     */
    // rest of javadoc inherited
    public void condition(InputSource source, XMLProcess output)
            throws IOException, SAXException {

        InputStream inputStream = source.getByteStream();

        if (inputStream == null) {
            throw new IOException("InputSource does not have a byte stream.");
        } else {
            // Update encoding based on input source
            updateCharEncoding(source);

            // Declare an output stream with an arbitrary size.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);

            tidy.parse(inputStream, outputStream);

            if (logger.isDebugEnabled()) {
                logger.debug("**** TIDIED OUTPUT START ****");
                logger.debug("\n" + outputStream.toString());
                logger.debug("**** TIDIED OUTPUT END ****\n");
            }

            InputStream tidiedStream =
                    new ByteArrayInputStream(outputStream.toByteArray());

            InputSource tidiedSource = new InputSource(tidiedStream);
            tidiedSource.setSystemId(source.getSystemId());

            super.condition(tidiedSource, output);
        }
    }

    /**
     * This is not currently supported.
     */
    // rest of javadoc inherited
    public void condition(Reader reader, XMLProcess consumer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Override character encoding using input source encoding.
     *
     * @param source the input source
     */
    private void updateCharEncoding(InputSource source) {
        String encoding = source.getEncoding();
        if (encoding != null) {
            if (encoding.equalsIgnoreCase("utf8")
                    || encoding.equalsIgnoreCase("utf-8")) {
                tidy.setCharEncoding(Configuration.UTF8);
            } else if (encoding.equalsIgnoreCase("iso-8859-1")) {
                tidy.setCharEncoding(Configuration.LATIN1);
            } else if (encoding.equalsIgnoreCase("iso-2020")) {
                // @todo: how about iso-2020-jp or iso-2020-cn?
                tidy.setCharEncoding(Configuration.ISO2022);
            } else if (encoding.equalsIgnoreCase("ascii")
                    || encoding.equalsIgnoreCase("us-ascii")) {
                tidy.setCharEncoding(Configuration.ASCII);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Mar-04	656/1	adrian	VBM:2004031602 Updated pipeline with DSB enhancement requests

 30-Mar-04	650/1	adrian	VBM:2004032906 Added factory for HTMLResponseConditioner

 25-Mar-04	621/1	adrian	VBM:2004031902 Updated HTMLResponseConditioner to set JTidy quoteNbsp parameter to true

 10-Feb-04	525/1	adrian	VBM:2004011902 fixed bug setting the baseuri on included content within template bindings

 05-Aug-03	294/1	allan	VBM:2003070709 Ensure nested anchor filter is before the user filter in the chain

 04-Aug-03	217/7	allan	VBM:2003071702 Filter nested anchors.

 31-Jul-03	217/5	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 30-Jul-03	217/3	allan	VBM:2003071702 Separated WebDriverAccessor from configuration. Updated type safe enums. Updated conditioners

 ===========================================================================
*/
