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

package com.volantis.mcs.protocols.renderer;

import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.css.StylePropertyResolver;
import com.volantis.mcs.runtime.OutputBufferResolver;

/**
 * Encapsulates contextual information passed to protocol renderers.
 *
 * <p>Contextual information includes things like objects for resolving assets
 * and OutputBuffers. It does not include markup outputting objects, or
 * customisation information. These both have separate mechanisms.</p>
 *
 * <p>While it may be easy and convenient to add additional information into
 * this object that should only be done after due consideration has been given
 * as to whether this is an appropriate place for that information.</p>
 *
 * <p>Some points to consider when making that decision:</p>
 * <ul>
 *     <li>Dependencies of this object and on this object should be
 *         minimised.</li>
 *
 *     <li>Module specific information should not be added here as that would
 *         make all modules dependent on that module.</li>
 * </ul>
 */
public interface RendererContext {

    /**
     * Get the object to use in order to resolve assets.
     */
    public AssetResolver getAssetResolver();

    /**
     * Get the object to use in order to resolve style properties.
     */
    public StylePropertyResolver getStylePropertyResolver();

    /**
     * Get the object to use in order to resolve a format reference to an
     * OutputBuffer.
     */
    public OutputBufferResolver getOutputBufferResolver();

    /**
     * Get the object to use in order to create OutputBuffers.
     */
    public OutputBufferFactory getOutputBufferFactory();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
