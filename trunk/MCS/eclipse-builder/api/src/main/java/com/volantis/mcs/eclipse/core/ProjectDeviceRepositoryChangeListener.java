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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.core;

/**
 * Listener for changes to the device repository that is associated with a
 * project.
 */
public interface ProjectDeviceRepositoryChangeListener {
    /**
     * Called when the project device repository has changed either because
     * a different device repository is now associated with the project
     * or the device repository currently associated with the project has
     * been modified.
     */
    public void changed();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10539/3	adrianj	VBM:2005111712 fixed up merge conflicts

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 ===========================================================================
*/
