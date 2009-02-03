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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.googledocs;

import java.util.HashMap;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.*;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.XMLPipelineException;

import org.xml.sax.Attributes;

/**
 *
 * GDocs Fetch Document rule implementation.
 *
 * @volantis-api-include-in InternalAPI
 */
public class FetchDocRule
    extends GDocsRule {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new FetchDocRule();

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
            LocalizationFactory.createLogger(FetchDocRule.class);

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(FetchDocRule.class);
/**
     * Creates a new ListDocsRule
     */
    public FetchDocRule() {    
    }

    /**
     * Identifier for the id attribute
     */
    public static final String ATTRIBUTE_HREF = "href";

    //javadoc inherited
    protected HashMap<String, String> gatherAndValidateAttributes(Attributes attributes) throws XMLPipelineException {

        HashMap<String, String> atts = new HashMap<String, String>();

        String href = attributes.getValue(ATTRIBUTE_HREF);

        if (href == null) {
            throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-invalid", ATTRIBUTE_HREF), null);
        }

        try {
            new URL(href);
        } catch (MalformedURLException e) {
            LOGGER.debug("MalformedURLException on parsing: " + href, e);
            throw new XMLPipelineException(EXCEPTION_LOCALIZER.format("attribute-invalid", ATTRIBUTE_HREF), null);
        }
        
        //store attributes
        atts.put(ATTRIBUTE_HREF, href);

        return atts;
    }

    //javadoc inherited
    protected String prepareRequestUrl(HashMap<String, String> atts) {         
        return atts.get(ATTRIBUTE_HREF);
    }
}