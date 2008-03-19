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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.StringWriter;

/**
 * Base for all visitor based transformers.
 */
public abstract class AbstractVisitorBasedDOMTransformer
        implements DOMTransformer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(
                    AbstractVisitorBasedDOMTransformer.class);

    /**
     * Log the state at entry to the transform method.
     *
     * @param protocol
     * @param document
     */
    protected void logEntry(DOMProtocol protocol, Document document) {
        logDOM(protocol, document, "Incoming DOM to transform ("
                + getTransformerName() + "):");
    }

    /**
     * Log the exit result of the transform method.
     *
     * @param protocol
     * @param document
     */
    protected void logExit(DOMProtocol protocol, Document document) {
        logDOM(protocol, document, "Transformed DOM ("
                + getTransformerName() + "):");
    }

    /**
     * Return the name of the transformer - used primarily for logging
     * statements.
     *
     * @return a String.
     */
    public String getTransformerName() {
        Class thisClass = getClass();
        Package thisPackage = thisClass.getPackage();

        String packageName = "";
        if (null != thisPackage) {
            packageName = thisPackage.getName();
        }

        String className = thisClass.getName();

        int period = 0;
        if (packageName.length() > 0) {
            period = 1;
        }

        return className.substring(packageName.length() + period);
    }

    /**
     * This method is used to log a DOM.
     *
     * @param protocol the DOM protocol for which the DOM is to be logged
     * @param document the DOM document to log
     * @param prefix   a message to prefix the DOM output with
     */
    public static void logDOM(
            DOMProtocol protocol,
            Document document,
            String prefix) {
        if (logger.isDebugEnabled()) {
            try {
                StringWriter writer = new StringWriter();
                DOMDocumentOutputter outputter = new DOMDocumentOutputter(
                        new XMLDocumentWriter(writer),
                        protocol.getCharacterEncoder());
                // Tell the outputter to display null elements so we can
                // eliminate them more easily - null element names are evil!
                outputter.setDebugNullElementNames(true);

                outputter.output(document);

                logger.debug(prefix + ": " + writer.toString());
            } catch (Exception e) {
                logger.debug("Failed to log the transformer DOM", e);
            }
        }
    }
}
