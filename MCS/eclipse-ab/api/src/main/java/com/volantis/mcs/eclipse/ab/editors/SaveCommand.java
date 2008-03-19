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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.CoreException;

/**
 * Command pattern that allows a save to be performed
 */
public interface SaveCommand {
    /**
     * Method that will be invoked to perform a specific save.
     * @param progressMonitor  a <code>IProgressMonitor</code> that can be
     * used to report progress
     * @throws org.eclipse.core.runtime.CoreException if an error occurs
     */
    public void save(IProgressMonitor progressMonitor) throws CoreException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
