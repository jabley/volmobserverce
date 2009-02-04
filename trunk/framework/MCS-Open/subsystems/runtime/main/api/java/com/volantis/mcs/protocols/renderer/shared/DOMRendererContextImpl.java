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
package com.volantis.mcs.protocols.renderer.shared;

import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.css.StylePropertyResolver;
import com.volantis.mcs.protocols.renderer.DOMRendererContext;
import com.volantis.mcs.runtime.OutputBufferResolver;

/**
 * This class is responsible for providing an implementation of 
 * {@link com.volantis.mcs.protocols.renderer.DOMRendererContext}
 */
public final class DOMRendererContextImpl implements DOMRendererContext {

    /**
     *  The RenderContext we will delegate to.
     */ 
    private final RendererContextImpl renderContentImpl;

    /**
     * The inserter used to insert
     * {@link com.volantis.mcs.themes.StyleValue}'s into the output
     * document.
     */
    private final Inserter inserter;

    /**
     * Initialize the new instance using the given parameters.
     *
     * @param assetResolver the asset resolver.
     * @param outputBufferResolver the ouput buffer resolver.
     * @param outputBufferFactory  the buffer factory.
     * @param styleResolver the style resolver
     * @param inserter the inserter.
     */
    public DOMRendererContextImpl(AssetResolver assetResolver,
                                  OutputBufferResolver outputBufferResolver,
                                  OutputBufferFactory outputBufferFactory,
                                  StylePropertyResolver styleResolver,
                                  Inserter inserter) {

        renderContentImpl = new RendererContextImpl(assetResolver, 
                                                    outputBufferResolver, 
                                                    outputBufferFactory, 
                                                    styleResolver);
        this.inserter = inserter;
    }

    // javadoc inherited.
    public AssetResolver getAssetResolver() {
        return renderContentImpl.getAssetResolver();
    }

    // javadoc inherited.
    public StylePropertyResolver getStylePropertyResolver() {
        return renderContentImpl.getStylePropertyResolver();
    }

    // javadoc inherited.
    public OutputBufferResolver getOutputBufferResolver() {
        return renderContentImpl.getOutputBufferResolver();
    }

    // javadoc inherited.
    public OutputBufferFactory getOutputBufferFactory() {
        return renderContentImpl.getOutputBufferFactory();
    }
    
    // javadoc inherited.
    public Inserter getInserter() {
        return inserter;
    }
}
