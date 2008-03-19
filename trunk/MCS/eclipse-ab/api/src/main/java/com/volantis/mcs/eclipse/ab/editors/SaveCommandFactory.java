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
package com.volantis.mcs.eclipse.ab.editors;

import org.eclipse.core.resources.IFile;

/**
 * A Factory that creates SaveCommand instances in order to allow clients
 * to perfom various save operations.
 */
public interface SaveCommandFactory {

    /**
     * Creates a {@link SaveCommand} that will be used to perfom a save
     * operation
     * @return a SaveCommand instance
     */
    public SaveCommand createSaveCommand();

    /**
     * Creates a {@link SaveCommand} that will be used to perfom a save as
     * operation
     * @param destinationFile the <code>IFile</code> that the save as command
     * should save to.
     * @return a SaveCommand instance
     */
    public SaveCommand createSaveAsCommand(final IFile destinationFile);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-May-04	4239/1	allan	VBM:2004042207 SaveAs on DeviceEditor.

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
