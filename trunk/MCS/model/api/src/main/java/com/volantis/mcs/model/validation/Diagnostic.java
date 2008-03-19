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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.model.validation;

import com.volantis.mcs.model.path.Path;


/**
 * A diagnostic message.
 */
public interface Diagnostic {

    /**
     * Get the location of the diagnostic method within the source document.
     *
     * <p>This is only valid if the model has been read from a document and
     * has not been modified.</p>
     *
     * @return The source location of the document.
     */
    SourceLocation getLocation();

    /**
     * Get the level of the diagnostic message..
     *
     * @return The level of the diagnostic message..
     */
    DiagnosticLevel getLevel();

    /**
     * Get the path to the source of the diagnostic message.
     *
     * @return The path to the source of the diagnostic message.
     */
    Path getPath();

    /**
     * Get the i18n message of the diagnostic.
     *
     * @return The i18n message.
     */
    I18NMessage getMessage();

    /**
     * Get the message line for this diagnostic.
     *
     * <p>The message line encapsulates the information within this
     * diagnostic.</p>
     *
     * @return The message line.
     */
    String getMessageLine();

    /**
     * Mark this diagnostic as relating to pruned data.
     * <p>
     * This must prevent this diagnostic causing validation to fail.
     */
    void markAsPruned();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
