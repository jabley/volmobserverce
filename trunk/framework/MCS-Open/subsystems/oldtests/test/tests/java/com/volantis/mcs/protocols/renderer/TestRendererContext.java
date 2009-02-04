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

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.css.StylePropertyResolver;
import com.volantis.mcs.runtime.OutputBufferResolver;
import com.volantis.mcs.runtime.TestOutputBufferResolver;

/**
 * Test implementation of the renderer context.
 */
public class TestRendererContext
        implements RendererContext {


    /**
     * The object for resolving assets.
     */
    private AssetResolver assetResolver;

    /**
     * The object for resolving format references to
     * {@link com.volantis.mcs.protocols.OutputBuffer}.
     */
    private OutputBufferResolver outputBufferResolver;

    /**
     * Factory for creating {@link com.volantis.mcs.protocols.OutputBuffer}.
     */
    private OutputBufferFactory outputBufferFactory;

    /**
     * Used to resolve menu related style info.
     */
    private StylePropertyResolver styleResolver;

    /**
     * Initialise.
     * @param assetResolver The object for resolving assets.
     * @param outputBufferResolver The object for resolving format references to
     * {@link com.volantis.mcs.protocols.OutputBuffer}.
     * @param outputBufferFactory Factory for creating
     * {@link com.volantis.mcs.protocols.OutputBuffer}.
     */
    public TestRendererContext(AssetResolver assetResolver,
                               OutputBufferResolver outputBufferResolver,
                               OutputBufferFactory outputBufferFactory) {

        if (outputBufferResolver == null) {
            DOMOutputBuffer buffer = new TestDOMOutputBuffer();
            outputBufferResolver = new TestOutputBufferResolver(buffer);
        }
        if (outputBufferFactory == null) {
            outputBufferFactory = new TestDOMOutputBufferFactory();
        }

        this.assetResolver = assetResolver;
        this.outputBufferResolver = outputBufferResolver;
        this.outputBufferFactory = outputBufferFactory;
        this.styleResolver = new DefaultStylePropertyResolver(null, null);
    }

    /**
     * Initialise.
     * @param assetResolver
     */
    public TestRendererContext(AssetResolver assetResolver) {
        this(assetResolver, null, null);
    }

    public void setAssetResolver(AssetResolver assetResolver) {
        this.assetResolver = assetResolver;
    }

    // Javadoc inherited.
    public AssetResolver getAssetResolver() {
        return assetResolver;
    }

    public void setOutputBufferResolver(OutputBufferResolver outputBufferResolver) {
        this.outputBufferResolver = outputBufferResolver;
    }

    // Javadoc inherited.
    public OutputBufferResolver getOutputBufferResolver() {
        return outputBufferResolver;
    }

    public void setOutputBufferFactory(OutputBufferFactory outputBufferFactory) {
        this.outputBufferFactory = outputBufferFactory;
    }

    // Javadoc inherited.
    public OutputBufferFactory getOutputBufferFactory() {
        return outputBufferFactory;
    }

    /**
     * Get the underlying buffer.
     * @return The underlying buffer.
     */
    public DOMOutputBuffer getBuffer() {
        OutputBufferResolver resolver = getOutputBufferResolver();
        DOMOutputBuffer buffer
                = (DOMOutputBuffer) resolver.resolvePaneOutputBuffer(null);
        return buffer;
    }

    public StylePropertyResolver getStylePropertyResolver() {
        return styleResolver;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
