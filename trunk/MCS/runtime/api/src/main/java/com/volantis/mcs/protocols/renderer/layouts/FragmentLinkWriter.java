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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.layouts;

import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.protocols.DeviceLayoutRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.shared.layouts.FragmentRenderer;

import java.io.IOException;

/**
 * Provides the API for writing fragment links. This is functionality required
 * by the {@link DeviceLayoutRenderer} and {@link FragmentRenderer} classes.
 *
 * @mock.generate
 */
public interface FragmentLinkWriter {
    /**
     * Writes an anchor tag to link to another fragment to the page.
     *
     * @param formatRendererContext
     * @param source                The <code>Fragment</code> to link from.
     * @param destination           The <code>Fragment</code> to link to.
     * @param isInList              If true then this link is being generated as
     *                              part of a fragment link list, rather than
     *                              inside a fragment layout, and thus the
     *                              mariner-list-* styles apply.
     * @param toEnclosing           If true then the link is being generated to
     *                              the enclosing fragment, otherwise it is
     *                              being generated to a nested fragment. @todo
     *                              invent a fragment type to replace the two
     *                              booleans we currently use.
     */
    public void writeFragmentLink(
            FormatRendererContext formatRendererContext,
            Fragment source,
            Fragment destination,
            boolean isInList, boolean toEnclosing)
            throws IOException, RendererException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/3	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
