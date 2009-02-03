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

package com.volantis.xml.pipeline.sax;

import org.xml.sax.Locator;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A SAXParseException that is used within the pipeline.  It may contain
 * zero to many exceptions which will be output
 */
public class CompositeXMLPipelineException
        extends XMLPipelineException {

    /**
     * An array of {@link Throwable} which makes up the composite.
     */
    private Throwable[] throwables;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message the exception message
     * @param locator the locator
     */
    public CompositeXMLPipelineException(String message, Locator locator,
                                         Throwable[] throwables) {
        super(message, locator);
        storeThrowablesArray(throwables);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message the exception message
     * @param locator the locator
     * @param throwable the underlying throwable
     */
    public CompositeXMLPipelineException(String message, Locator locator,
                                         Throwable throwable, Throwable[] throwables) {
        super(message, locator, throwable);
        storeThrowablesArray(throwables);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message the exception message
     * @param publicId the locator public ID
     * @param systemId the locator system ID
     * @param lineNumber the locator line number
     * @param columnNumber the locator column number
     */
    public CompositeXMLPipelineException(String message,
                                         String publicId,
                                         String systemId,
                                         int lineNumber,
                                         int columnNumber,
                                         Throwable[] throwables) {
        super(message, publicId, systemId, lineNumber, columnNumber);
        storeThrowablesArray(throwables);
    }

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param message the exception message
     * @param publicId the locator public ID
     * @param systemId the locator system ID
     * @param lineNumber the locator line number
     * @param columnNumber the locator column number
     * @param throwable the underlying throwable
     */
    public CompositeXMLPipelineException(String message,
                                         String publicId,
                                         String systemId,
                                         int lineNumber,
                                         int columnNumber,
                                         Throwable throwable,
                                         Throwable[] throwables) {
        super(message, publicId, systemId, lineNumber, columnNumber,
              throwable);
        storeThrowablesArray(throwables);
    }

    /**
     * Prints the Exception and the composite Exceptions to the specified
     * <code>PrintStream</code>.
     */
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
            super.printStackTrace(ps);
            for (int i = 0; i < throwables.length; i++) {
                Throwable throwable = throwables[i];
                ps.println();
                ps.print("Exception[" + (i + 1) + "]: ");
                throwable.printStackTrace(ps);
            }
        }
    }

    /**
     * Prints the Exception and the composite Exceptions to the specified
     * <code>PrintWriter</code>.
     */
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
            super.printStackTrace(pw);
            for (int i = 0; i < throwables.length; i++) {
                Throwable throwable = throwables[i];
                pw.println();
                pw.print("Exception[" + (i + 1) + "]: ");
                throwable.printStackTrace(pw);
            }
        }
    }

    /**
     * Initialize the {@link #throwables} field and copy the specified array
     * into it.
     * @param throwables the array to copy in the throwables field.
     */
    private void storeThrowablesArray(Throwable[] throwables) {
        this.throwables = new Throwable[throwables.length];
        System.arraycopy(throwables, 0, this.throwables, 0, throwables.length);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 ===========================================================================
*/
