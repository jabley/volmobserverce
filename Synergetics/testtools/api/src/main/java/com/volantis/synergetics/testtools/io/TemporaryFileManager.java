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
 * Manages the creation and cleaning up of temporary files.
 */ 
public class TemporaryFileManager {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to create temporary files.
     */ 
    private TemporaryFileCreator creator;
    
    public TemporaryFileManager(TemporaryFileCreator creator) {
        
        this.creator = creator;
        
    }

    /**
     * Execute the code in the {@link TemporaryFileExecutor} "closure",
     * providing it with a temporary file created by the creator, and clean up 
     * the temporary file afterwards.
     * 
     * @param executor
     * @throws java.io.IOException
     */ 
    public void executeWith(TemporaryFileExecutor executor) 
            throws Exception {
        
        File temporaryFile = creator.createTemporaryFile();
        try {
            executor.execute(temporaryFile);
        } finally {
            if (temporaryFile.exists()) {
                temporaryFile.delete();
            }
        }
        
    }
    
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
