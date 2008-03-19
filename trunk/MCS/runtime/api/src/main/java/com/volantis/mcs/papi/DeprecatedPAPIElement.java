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
package com.volantis.mcs.papi;

import com.volantis.mcs.context.MarinerRequestContext;

import java.io.Writer;

/**
 * Provides Deprecated methods for {@link PAPIElement}.
 *
 * @deprecated The methods on this interface are still in the public API but
 *             their use should be avoided where possible as future verions of
 *             the product may not fully support their usage.
 */

public interface DeprecatedPAPIElement extends PAPIElement {


    /**
     * Writes a String to the current output location.
     * <p/>
     * This method assumes that the String is already correctly encoded for the
     * destination device / protocol.
     * </p><p>
     * This must only be called between calls to elementStart and the matching
     * call to elementEnd.
     * </p>
     *
     * @param context The MarinerRequestContext within which this element is
     *                being processed.
     * @param content The String to write.
     * @throws PAPIException If there was a problem processing the content.
     */
    public void elementDirect(MarinerRequestContext context,
                              String content)
            throws PAPIException;

    /**
     * Get a <code>Writer</code> which can be used to write to the current
     * output location.
     * <p/>
     * The <code>Writer</code> write methods behaves as if the input was
     * wrapped in a String and passed to {@link #elementDirect}, although
     * it may be more efficient.
     * </p>
     */
    public Writer getDirectWriter(MarinerRequestContext context)
            throws PAPIException;
}
