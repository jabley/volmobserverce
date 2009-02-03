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

import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import com.volantis.xml.utilities.sax.XMLReaderFactory;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

import java.io.IOException;

/**
 * A ContentConditioner that expects valid XML from an InputStream and
 * forwards this as SAX events to an XMLProcess.
 */
public class XMLResponseConditioner implements ContentConditioner {


    /**
     * The filter for this conditioner.
     */
    private XMLFilter filter;

    /**
     * The default constructor. This relies on specializations to set the
     * filter.
     */
    protected XMLResponseConditioner() {
    }

    /**
     * Construct a new XMLResponseConditioner that uses a specified
     * XMLFilter to parse its input. This may need a default constructor later
     * with a setter for the filter.
     * @param filter The filter.
     */
    public XMLResponseConditioner(XMLFilter filter) {
        this.filter = filter;
    }

    /**
     * Set the filter for this XMLResponseConditioner
     * @param filter The filter.
     */
    protected void setFilter(XMLFilter filter) {
        this.filter = filter;
    }

    /**
     * Get the filter for this XMLResponseConditioner.
     * @return The XMLFilter.
     */
    protected XMLFilter getFilter() {
        return filter;
    }

    // javadoc inherited
    public boolean supportsBinaryData() {
        return true;
    }

    // jaavadoc inherited
    public boolean supportsCharacterData() {
        return true;
    }

    /**
     * If the filter has no parent then it will be given one by this
     * method otherwise the preset filter parent will remain.
     */
    // javadoc inherited
    public void condition(InputSource source, XMLProcess consumer)
            throws IOException, SAXException {
        filter.setContentHandler(consumer);
        filter.setErrorHandler(consumer);

        if (filter.getParent() == null) {
            XMLReader reader = XMLReaderFactory.createXMLReader(false);
            reader.setFeature("http://xml.org/sax/features/validation", false);
            reader.setFeature(
                        "http://apache.org/xml/features/validation/schema",
                        false);
            filter.setParent(reader);
        }

        filter.parse(source);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Feb-05	6912/1	doug	VBM:2005011703 Fixed problem with arguments being removed before they have been processed

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 23-Mar-04	631/1	allan	VBM:2004032205 Patch performance fixes from Pipeline/MCS 3.0GA

 22-Mar-04	626/1	allan	VBM:2004032205 Pipeline performance enhancements.

 05-Aug-03	294/1	allan	VBM:2003070709 Ensure nested anchor filter is before the user filter in the chain

 04-Aug-03	217/5	allan	VBM:2003071702 Filter nested anchors.

 31-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task v4

 30-Jul-03	217/3	allan	VBM:2003071702 Separated WebDriverAccessor from configuration. Updated type safe enums. Updated conditioners

 ===========================================================================
*/
