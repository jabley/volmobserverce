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

package com.volantis.mcs.model.impl.validation;

import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.mcs.model.validation.SourceLocation;

public class DiagnosticImpl
        implements Diagnostic {

    private static final SourceLocation UNKNOWN_LOCATION =
            new SourceLocation() {
                public String getSourceDocumentName() {
                    return "<unknown location>";
                }

                public int getSourceLineNumber() {
                    return -1;
                }

                public int getSourceColumnNumber() {
                    return -1;
                }
            };

    private final SourceLocation location;

    private DiagnosticLevel level;

    private final Path path;

    private final I18NMessage message;

    private final String messageLine;

    public DiagnosticImpl(
            SourceLocation location, Path path, DiagnosticLevel diagnosticLevel,
            I18NMessage i18NMessage) {

        if (location == null) {
            location = UNKNOWN_LOCATION;
        }
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        if (diagnosticLevel == null) {
            throw new IllegalArgumentException("diagnosticLevel cannot be null");
        }
        if (i18NMessage == null) {
            throw new IllegalArgumentException("i18NMessage cannot be null");
        }

        this.location = location;
        this.level = diagnosticLevel;
        this.path = path;
        this.message = i18NMessage;

        String document = location.getSourceDocumentName();
        if (document == null) {
            document = "";
        } else {
            document = document + ":";
        }
        int line = location.getSourceLineNumber();
        int column = location.getSourceColumnNumber();

        this.messageLine = document + line + ":" + column + ": " +
                level + " - " + message.getMessage();
    }

    public SourceLocation getLocation() {
        return location;
    }

    public DiagnosticLevel getLevel() {
        return level;
    }

    public Path getPath() {
        return path;
    }

    public I18NMessage getMessage() {
        return message;
    }

    public String getMessageLine() {
        return messageLine;
    }

    // Javadoc inherited.
    public void markAsPruned() {
        // todo: better: add a new field to diagnostic to indicate if it
        // relates to pruned data and leave the level as it was originally.

        // For now, we use the simplest possible implementation. ;-).
        if (level == DiagnosticLevel.ERROR) {
            level = DiagnosticLevel.WARNING;
        }
    }

    public String toString() {
        return location + ", " + level + ", " + path + ", "  + message;
    }
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
