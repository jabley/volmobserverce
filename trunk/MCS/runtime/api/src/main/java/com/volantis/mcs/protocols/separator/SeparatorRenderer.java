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

package com.volantis.mcs.protocols.separator;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.renderer.RendererException;

/**
 * Implementations of this are responsible for rendering a separator to an
 * OutputBuffer.
 * <p>The separator may consist of markup, or character data, or a combination
 * of both.</p>
 * <p>Where possible implementations should not contain any information that
 * would prevent a single instance being used by multiple threads. Specifically
 * it must appear to external users to be completely immutable.</p>
 *
 * @mock.generate
 */
public interface SeparatorRenderer {

    public static final SeparatorRenderer NULL = new SeparatorRenderer() {
        public void render(OutputBuffer buffer)
                throws RendererException {
        }
    };

    /**
     * Render the separator to the specified OutputBuffer.
     * <p>This method MUST NOT change the state of the renderer in such a way
     * as to affect subsequent calls. This is because the choice as to when, if
     * ever a separator is rendered is dependent on the
     * {@link SeparatorManager}.</p>
     * @param buffer The buffer to which the separator should be rendered.
     * @throws RendererException If there was a problem rendering the separator.
     */
    public void render(OutputBuffer buffer)
        throws RendererException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
