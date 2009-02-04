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

package com.volantis.mcs.xdime;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.xdime.xforms.XFGroupElementImpl;
import com.volantis.mcs.xdime.xforms.XFLabelElementImpl;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.mcs.xml.schema.validation.ValidationException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.cornerstone.utilities.WhitespaceUtilities;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * A writer that is owned by an XDIMEContextInternal instance and provides the
 * ability to write content within the current element.
 */
public class ContextWriter
        extends FastWriter {

    /**
     * Used for logging.
     */
     private static final LogDispatcher LOGGER =
             LocalizationFactory.createLogger(ContextWriter.class);

    /**
     * The set of elements that used to be able to contain character data but
     * for which it is now deprecated and will be removed at some point in
     * future.
     */
    private static final Set /*of ElementType*/ DEPRECATED_PCDATA_CONTAINERS;
    static {
        Set set = new HashSet();
//        set.add(XHTML2Elements.BODY);
        DEPRECATED_PCDATA_CONTAINERS = set;
    }

    /**
     * The set of ElementType for which a deprecated error has been written.
     */
    private static final Set /*of ElementType*/ DEPRECATED_ERRORS =
            new HashSet();
    
    /**
     * The owning context.
     */
    private final XDIMEContextInternal context;

    /**
     * The document validator.
     */
    private final DocumentValidator validator;

    /**
     * Initialise.
     *
     * @param context The owning context.
     */
    public ContextWriter(XDIMEContextInternal context) {
        this.context = context;
        validator = context.getDocumentValidator();
    }

    /**
     * Get the writer to which this should delegate.
     *
     * @return The delegating writer.
     */
    private Writer getDelegate() {
        MarinerPageContext pageContext = ContextInternals.getMarinerPageContext(
                context.getInitialRequestContext());

        OutputBuffer outputBuffer = pageContext.getCurrentOutputBuffer();
        return outputBuffer.getWriter();
    }

    /**
     * Determine whether the character content is significant.
     *
     * @param whitespace True if the character data is all whitespace.
     * @return True if the character data is significant and should be written
     * to the current OutputBuffer writer.
     * @throws IOException If character data is invalid.
     */
    private boolean isSignificant(boolean whitespace)
            throws IOException {

        try {
            if (!validator.pcdata(whitespace)) {
                return false;
            }

            // If the character data was consumed by the schema validator and
            // and is not all whitespace then check to see if the element
            // being validated is a deprecated container for which an error
            // should be logged.
            if (!whitespace) {
                checkDeprecatedContainer();
            }

        } catch (ValidationException e) {
            throw new IOException(e.getMessage());
        }

        // Get the information about the current element.
        XDIMEElementInternal element = context.getCurrentElement();

        // Don't output the character data if output is currently
        // being suppressed - there's nowhere for it to go.
        if (element.getOutputState().isSuppressing()) {
            // Group element labels are a special case because they can
            // be used even if the group is inactive (as the navigation
            // link text).
            XDIMEElementInternal parent  = element.getParent();
            if (!(parent instanceof XFGroupElementImpl &&
                element instanceof XFLabelElementImpl)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check to see if the current element is a deprecated container of
     * character data and if it is then log an error but do it only once for
     * each deprecated element.
     */
    private void checkDeprecatedContainer() {
        ElementType validatingElement = validator.getValidatingElement();
        if (DEPRECATED_PCDATA_CONTAINERS.contains(validatingElement)) {
            // Synchronize to ensure that only a single message is written
            // out even under load.
            synchronized(DEPRECATED_ERRORS) {
                // If an error has already been written out for the validating
                // element then don't write another.
                if (DEPRECATED_ERRORS.contains(validatingElement)) {
                    return;
                }

                // Log an error.
                LOGGER.error("validation-error-deprecated-pcdata",
                        validatingElement);

                // Remember that an error was logged so that another one is not
                // written out as that could lead to a flood of error messages.
                DEPRECATED_ERRORS.add(validatingElement);
            }
        }
    }

    public void write(char cbuf[], int off, int len) throws IOException {
        boolean whitespace = WhitespaceUtilities.isWhitespace(
                cbuf, off, len);
        if (isSignificant(whitespace)) {
            Writer delegate = getDelegate();
            delegate.write(cbuf, off, len);
        }
    }
}
