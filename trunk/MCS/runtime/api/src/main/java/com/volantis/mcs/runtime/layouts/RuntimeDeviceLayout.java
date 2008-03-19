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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.layouts.FormatScope;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Layout;
import com.volantis.styling.sheet.CompiledStyleSheet;

import java.util.List;

/**
 * Provides all the information from a {@link Layout} that is needed by
 * the runtime.
 *
 * @mock.generate
 */
public interface RuntimeDeviceLayout
        extends RepositoryObject {

    /**
     * @see #getFormatScope()
     */
    FormatScope getFormatScope();

    /**
     * @see #getDefaultFragmentName()
     */
    String getDefaultFragmentName();

    /**
     * @see #retrieveFormat(String, com.volantis.mcs.layouts.FormatType)
     */
    Format retrieveFormat(String fragmentName, FormatType fragment);

    /**
     * @see #getDefaultSegmentName()
     */
    String getDefaultSegmentName();

    /**
     * @see #getRootFormat()
     */
    Format getRootFormat();

    /**
     * @see #getLayoutGroupName()
     */
    String getLayoutGroupName();

    /**
     * @see #getDestinationLayout()
     */
    String getDestinationLayout();

    /**
     * @see #getFormatCount()
     */
    int getFormatCount();

    CompiledStyleSheet getCompiledStyleSheet();

    /**
     * Returns the enclosing fragments for the container identified by the
     * specified name.
     *
     * The returned list consists of DeviceLayoutActivator.ContainerPosition
     * objects.
     *
     * Never returns null.
     *
     * @param containerName name of the container
     * @return the possibly empty list of fragments that directly contain the
     * given container
     */
    public List getEnclosingFragments(final String containerName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 ===========================================================================
*/
