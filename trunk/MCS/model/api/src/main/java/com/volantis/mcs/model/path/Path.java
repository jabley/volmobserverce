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

package com.volantis.mcs.model.path;

import com.volantis.mcs.model.ModelFactory;

/**
 * Defines a path from the root of a proxy model to a descendant proxy.
 *
 * <p>The path is independent of any particular proxy instances. This means
 * that even if the model is stored and reloaded and the proxies discarded and
 * new ones created that it can be used to locate the proxy associated with the
 * same model object as the proxy for which it was created.</p>
 */
public interface Path {

    /**
     * Get the path as a string.
     *
     * <p>The format of the returned string is not part of this interface and
     * is likely to change in future. The only claim that is made is that if
     * the string is passed to the {@link ModelFactory#parsePath(String)}
     * method then the result will be a {@link Path} equivalent to this one.</p>
     *
     * @return The string representation suitable for being parsed by
     *         {@link ModelFactory#parsePath(String)}.
     */
    String getAsString();

    int getStepCount();

    Step getStep(int index);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Nov-05	10287/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/5	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Added path support

 ===========================================================================
*/
