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
package com.volantis.mcs.runtime;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.FormatReference;

/**
 * Provides a mechanism for accessing the output buffer for specified
 * {@link FormatReference} instances.
 *
 * @mock.generate
 */
public interface OutputBufferResolver {
    /**
     * Given a format reference (assumed to reference a pane), this method
     * returns the OutputBuffer associated with that pane (creating it if
     * needed).
     *
     * <p>If the reference is not null but doesn't identify an existing pane
     * this method will return null.</p>
     *
     * <p>If the reference is null, the current pane's output buffer will be
     * returned.</p>
     *
     * @param paneFormatReference
     *         a reference identifying the required pane. May be null
     * @return the specified pane's output buffer, the current pane's output
     *         buffer or null if the specified pane doesn't exist
     */
    OutputBuffer resolvePaneOutputBuffer(FormatReference paneFormatReference);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (rework issues)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
