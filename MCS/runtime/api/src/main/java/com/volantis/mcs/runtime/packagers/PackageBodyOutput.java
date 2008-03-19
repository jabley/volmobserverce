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
 * PackageBodyOutput encapsulates both a Writer and an OutputStream that can
 * be used to write the content of a PackageBodySource
 * 
 * @author Steve
 *
 */
public interface PackageBodyOutput {

    /**
     * Returns a writer that can be used to write body content
     * @return a java.io.Writer derived writer
     */
    public Writer getWriter();    

    /**
     * Returns an output stream that can be used to write nody content
     * @return a java.io.OutputStream derived stream
     */
    public OutputStream getOutputStream();
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
