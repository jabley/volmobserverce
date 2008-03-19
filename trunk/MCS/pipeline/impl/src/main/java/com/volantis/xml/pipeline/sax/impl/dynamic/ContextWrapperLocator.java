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

package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import org.xml.sax.Locator;

import java.net.URL;

/**
 * A {@link Locator} which wraps a {@link XMLPipelineContext} and delegates the
 * interface methods to the current locator set in that context.
 *
 * <p>The only information that can really be relied upon from this to be
 * correct is the system id, all other are not available in all but the
 * simplest of pipelines. The system id returned from this tracks the base URL
 * for the element and while the base URL and the system id from the context's
 * current locator stay in sync then the information from the locator is
 * returned, otherwise the base URL is returned as the system id and all other
 * methods return the appropriate unavailable value.</p>
 */
public class ContextWrapperLocator
        implements Locator {

    private static final int COLUMN_UNAVAILABLE = -1;
    private static final int LINE_UNAVAILABLE = -1;

    /**
     * The {@link XMLPipelineContext} from which we get the locator to which
     * we will delegate.
     */
    private final XMLPipelineContext context;

    /**
     * Remembers the last base URL.
     */
    private URL lastURL;

    /**
     * Remembers the string representation of the last base URL.
     */
    private String lastURLAsString;

    /**
     * Constructor.
     *
     * @param context the {@link XMLPipelineContext} that is being wrapped.
     */
    public ContextWrapperLocator(XMLPipelineContext context) {
        this.context = context;
    }

    // javadoc inherited from superclass
    public String getPublicId() {
        if (systemIdMatchesBaseURL()) {
            Locator locator = getCurrentLocator();
            return (locator != null) ? locator.getPublicId() : null;
        } else {
            return null;
        }
    }

    /**
     * Check whether the system id from the locator matches the base URL.
     *
     * @return True if the system id from the locator matches the base URL,
     *         false otherwise.
     */
    private boolean systemIdMatchesBaseURL() {
        String baseAsString = getSystemId();
        String locatorSystemId = getLocatorSystemId();
        return baseAsString == null ? locatorSystemId == null :
                baseAsString.equals(locatorSystemId);
    }

    private String getLocatorSystemId() {
        Locator locator = getCurrentLocator();
        return (locator != null) ? locator.getSystemId() : null;
    }

    // javadoc inherited from superclass
    public String getSystemId() {

        // If the current base URL is not the same as the last one then get
        // the string representation and store it away along with the URL.
        // This is done because for some reason URL does not cache the result
        // of performing toExternalForm() even though it is an expensive
        // operation and the result is likely to stay the same for a long
        // time.
        URL baseURL = context.getCurrentBaseURI();
        if (baseURL != lastURL) {
            lastURL = baseURL;
            if (lastURL == null) {
                lastURLAsString = null;
            } else {
                lastURLAsString = lastURL.toExternalForm();
            }
        }

        // Return the stored string representation.
        return lastURLAsString;
    }

    // javadoc inherited from superclass
    public int getLineNumber() {
        if (systemIdMatchesBaseURL()) {
            Locator locator = getCurrentLocator();
            return (locator != null) ? locator.getLineNumber()
                    : LINE_UNAVAILABLE;
        } else {
            return LINE_UNAVAILABLE;
        }
    }

    // javadoc inherited from superclass
    public int getColumnNumber() {
        if (systemIdMatchesBaseURL()) {
            Locator locator = getCurrentLocator();
            return (locator != null) ? locator.getColumnNumber()
                    : COLUMN_UNAVAILABLE;
        } else {
            return COLUMN_UNAVAILABLE;
        }
    }

    /**
     * Returns the current locator from the context. This could be null.
     *
     * @return A Locator or null if one is not available.
     */
    private Locator getCurrentLocator() {
        return context.getCurrentLocator();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	7762/1	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jan-04	527/3	adrian	VBM:2004011903 Added Copyright statements to new classes

 20-Jan-04	527/1	adrian	VBM:2004011903 Added ContextAnnotationProcess and supporting classes

 ===========================================================================
*/
