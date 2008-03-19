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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.operations.tryop;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import com.volantis.xml.pipeline.sax.impl.validation.ValidationModel;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.List;

/**
 * The model used to maintain the state for the try operation.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface TryModel
        extends ValidationModel {

    /**
     * Start the try.
     */
    void startTry();

    /**
     * End the try.
     */
    void endTry();

    /**
     * Add an exception to the collated list.
     *
     * @param exception The exception to add.
     */
    void addException(SAXParseException exception);

    /**
     * The list of collated exceptions.
     *
     * @return The list of collated exceptions.
     */
    List getExceptions();

    /**
     * Clear the exceptions.
     */
    void clearExceptions();

    /**
     * Get an exception that wraps all the exceptions.
     *
     * @param context The context.
     * @return The exception, or null if there were no exceptions.
     */
    SAXParseException getException(XMLPipelineContext context);

    /**
     * Start a block of recoverable content.
     *
     * @param element The element that wraps the block.
     * @throws SAXException If there was a problem.
     */
    void startBlock(Element element) throws SAXException;

    /**
     * End a block of recoverable content.
     *
     * @param element The element that wraps the block.
     * @throws SAXException If there was a problem.
     */
    void endBlock(Element element) throws SAXException;
}
