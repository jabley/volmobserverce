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
package com.volantis.xml.pipeline.sax.impl.operations.transform;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
/**
 * CollatignErrorListener that implements {@link ErrorListener} is responsible for collecting
 * error messages from {@link TransformerEexception}.
 * <p>All messages are stored in StringBuffer each message in separate line, and might
 * be returned by get method.</p>
 */
public class CollatingErrorListener implements ErrorListener {
    
    private StringBuffer errorBuffer;
    
    public CollatingErrorListener(){
        errorBuffer = new StringBuffer();
    }
    
    // javadoc inherited
    public void warning(TransformerException exception)
            throws TransformerException {
        errorBuffer.append(exception.getMessageAndLocation()).append("\n");
    }

    // javadoc inherited
    public void error(TransformerException exception)
            throws TransformerException {
        errorBuffer.append(exception.getMessageAndLocation()).append("\n");
    }

    // javadoc inherited
    public void fatalError(TransformerException exception)
            throws TransformerException {
        errorBuffer.append(exception.getMessageAndLocation()).append("\n");
    }
    
    /**
     * Returns collected error messages.
     * @return
     */
    public StringBuffer getErrorBuffer(){
        return this.errorBuffer;
    }
    
    /**
     * Reset collected error messages by creating empty buffer. 
     * @return 
     */
    public void resetErrorBuffer(){
        this.errorBuffer = new StringBuffer();
    }

}
