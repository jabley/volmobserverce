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

package com.volantis.mcs.css.impl.parser;

import com.volantis.mcs.css.parser.AggregatingDiagnosticListener;
import com.volantis.mcs.model.validation.Diagnostic;
//import com.volantis.mcs.css.parser.Diagnostic;

/**
 * Aggregates the diagnostic messages into a string buffer and provides access
 * to it.
 */
public class AggregatingDiagnosticListenerImpl
        implements AggregatingDiagnosticListener {

    /**
     * The buffer into which the messages are aggregated.
     */
    private final StringBuffer buffer;

    /**
     * Initialise.
     */
    public AggregatingDiagnosticListenerImpl() {
        buffer = new StringBuffer(100);
    }

    // Javadoc inherited.
    public void startParsing() {
        buffer.setLength(0);
    }

    // Javadoc inherited.
    public void message(Diagnostic diagnostic) {
        String message = diagnostic.getMessageLine();
        buffer.append(message).append("\n");
    }

    // Javadoc inherited.
    public String getResults() {
        if (buffer.length() == 0) {
            return null;
        } else {
            return buffer.toString();
        }
    }

    // Javadoc inherited.
    public void endParsing() {
        String messages = getResults();
        if (messages != null) {
            handleMessages(messages);
        }
    }

    protected void handleMessages(String messages) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
