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
package com.volantis.mcs.eclipse.common;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import com.volantis.mcs.xml.xpath.XPath;

/**
 * Used to find problem markers for a given IResource and XPath
 */
public interface ProblemMarkerFinder {
    /**
     * Find any problem type IMarkers on an IResource that have an
     * associated XPath attribute that is equal to or a child of the
     * specified XPath.
     *
     * @param resource The IResource to on which to find markers.
     * @param xPath The XPath used as a key to find IMarkers on the
     * IResource in this context.
     * @return All the problem type IMarkers on this contexts IResource that
     * are assocatied with the given XPath. Will return an empty array if
     * there are no matching markers.
     * @throws org.eclipse.core.runtime.CoreException If an error occurs.
     */
    public IMarker[] findProblemMarkers(IResource resource, XPath xPath)
        throws CoreException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
