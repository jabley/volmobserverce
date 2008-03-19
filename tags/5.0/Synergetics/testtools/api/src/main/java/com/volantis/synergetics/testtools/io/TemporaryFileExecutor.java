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
package com.volantis.synergetics.testtools.io;

import java.io.File;

/**
 * Instances of this class implement operations on temporary files.
 * <p>
 * Usually they will be used via {@link TemporaryFileManager#executeWith} 
 * rather than being used directly. 
 */ 
public interface TemporaryFileExecutor {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Perform an operation on a temporary file.
     * 
     * @param temporaryFile the file to operate on
     * @throws java.lang.Exception if there was a problem during the operation.
     */ 
    void execute(File temporaryFile) throws Exception;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-03	1599/2	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 ===========================================================================
*/
