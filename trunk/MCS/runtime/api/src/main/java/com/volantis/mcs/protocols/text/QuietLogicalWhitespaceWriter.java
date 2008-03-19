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
 * $Header: /src/voyager/com/volantis/mcs/protocols/text/QuietLogicalWhitespaceWriter.java,v 1.3 2003/01/29 14:30:57 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-02    Geoff           VBM:2002103005 - Created when refactoring the
 *                              old SMSWriter into multiple classes.
 * 27-Nov-02    Geoff           VBM:2002103005 - Created from old QuietWriter,
 *                              since we refactored stream error handling to be
 *                              more flexible.
 * 29-Jan-03    Adrian          VBM:2003012104 - Made methods writeSpace, 
 *                              writeLine, writeTrimmed public instead of 
 *                              package private. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * A LogicalWhitespaceWriter which quietly catches, logs and continues 
 * whenever it finds an {@link IOException}. 
 * <p>
 * Designed to be used in situations where an error is "impossible", for 
 * example writers which write to a buffer.
 * <p> 
 * Paul said Protocols ought not to throw exceptions as it's "too late at this 
 * point", so I presume this is class is safe to use within the protocol layer.
 */ 
public final class QuietLogicalWhitespaceWriter extends LogicalWhitespaceWriter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(QuietLogicalWhitespaceWriter.class);

    /**
     * The underlying writer.
     */ 
    private final LogicalWhitespaceWriter logical;
    
    /**
     * Construct the <code>QuietLogicalWhitespaceWriter</code>, which passes writes it's 
     * content to the writer supplied.
     *  
     * @param out
     */ 
    public QuietLogicalWhitespaceWriter(LogicalWhitespaceWriter out) {
        super(out);
        logical = out;
    }

    /**
     * Write a character, quietly.
     */ 
    public void write(int c) {
        try {
            logical.write(c);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }
    
    /**
     * Write a string, quietly.
     */ 
    public void write(String s) {
        write(s, 0, s.length());
    }

    /**
     * Write a character array, quietly.
     */ 
    public void write(char cbuf[]) {
        write(cbuf, 0, cbuf.length);
    }
    
    /**
     * Write a portion of a character array, quietly.
     */ 
    public void write(char cbuf[], int off, int len) {
        try {
            logical.write(cbuf, off, len);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }
    
    /**
     * Write a portion of a string, quietly.
     */ 
    public void write(String str, int off, int len) {
        try {
            logical.write(str, off, len);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }

    /**
     * Flush the stream, quietly.
     */ 
    public void flush() {
        try {
            logical.flush();
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }

    public void writeLine() {
        try {
            logical.writeLine();
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }

    public void writeSpace() {
        try {
            logical.writeSpace();
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }

    public void writeTrimmed(char cbuf[], int off, int len) {
        try {
            logical.writeTrimmed(cbuf, off, len);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            // and continue!
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
