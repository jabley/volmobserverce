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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-May-03    steve      		VBM:2003042914   Created.
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.runtime.packagers;

import java.io.OutputStream;
import java.io.Writer;

/**
 * AbstractPackageBodyOutput is a simple abstract implementation of 
 * PackageBodyOutput. Extensions to this class must provide implementations
 * of getRealOutputStream() and getRealWriter(). Note that these methods will 
 * only be called once by calling getOutputStream or getWriter.
 * 
 * Once getWriter() or getOutputStream() has been called, it is not legal to
 * call the other method, doing so will cause an IllegalStateException to be thrown.
 * 
 * 
 * @author steve
 *
 */
public abstract class AbstractPackageBodyOutput implements PackageBodyOutput {

    /** The writer we are writing to */
    private Writer writer = null;
                        
    /** The output stream we are writing to */
    private OutputStream outputStream = null;
         
    /** Return the output stream that is to be written to
     * 
     * @return java.io.OutputStream that the package body content is to
     * be written to.
     */
    protected abstract OutputStream getRealOutputStream();
    
    /** Return the writer that is to be used to write the package body content. 
     * 
     * @return java.io.Writer to use to write the content.
     */
    protected abstract Writer getRealWriter();
    
    
    /** Return the output stream that is to be written to. If getWriter() has 
     * already been called then an IllegalStateException will be thrown
     * 
     * @return java.io.OutputStream that the package body content is to
     * be written to.
     */
    public OutputStream getOutputStream() {
        if( writer != null ) {
            throw new IllegalStateException( "Writer has already been accessed" );
        }
        if( outputStream == null ){
            outputStream = getRealOutputStream();
        }
        return outputStream;
    }

    /** Return the writer that is to be written to. If getOutputStream() has 
     * already been called then an IllegalStateException will be thrown
     * 
     * @return java.io.Writer that the package body content is to
     * be written to.
     */
    public Writer getWriter() {
        if( outputStream != null ) {
            throw new IllegalStateException( "OutputStream has already been accessed" );
        }
        if( writer == null ) {
            writer = getRealWriter();
        }
        return writer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
