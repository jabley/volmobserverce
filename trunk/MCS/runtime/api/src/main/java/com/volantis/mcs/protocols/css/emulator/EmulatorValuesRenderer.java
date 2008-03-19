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
 * $Header: /src/voyager/com/volantis/mcs/protocols/css/emulator/EmulatorValuesRenderer.java,v 1.1 2002/06/29 01:04:52 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-02    Paul            VBM:2002051302 - Created to change the way some
 *                              values are rendered while delegating the rest.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css.emulator;

import com.volantis.mcs.css.renderer.DelegatingValuesRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleColorRenderer;
import com.volantis.mcs.css.renderer.StyleURIRenderer;
import com.volantis.mcs.css.renderer.ValuesRenderer;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleURI;

import java.io.IOException;

/**
 * This class wraps a <code>ValuesRenderer</code> object in order to change
 * the way some of the values are rendered. It cannot just extend a class
 * because there may be many different ValuesRenderer which could
 * be used.
 */
public final class EmulatorValuesRenderer
        extends DelegatingValuesRenderer {

    /**
     * Renderer used to render StyleColor objects.
     */
    private final StyleColorRenderer styleColorRenderer;

    /**
     * Renderer used to render StyleURI objects.
     */
    private final StyleURIRenderer styleURIRenderer;

    /**
     * Create a new <code>EmulatorValuesRenderer</code> which delegates to the
     * specified <code>ValuesRenderer</code>.
     *
     * @param valuesRenderer The <code>ValuesRenderer</code> to delegate to.
     */
    public EmulatorValuesRenderer(ValuesRenderer valuesRenderer) {
        super(valuesRenderer);

        styleColorRenderer = EmulatorStyleColorRenderer.getSingleton();
        styleURIRenderer = EmulatorStyleURIRenderer.getSingleton();
    }

    public void render(StyleColorName value, RendererContext context)
            throws IOException {

        styleColorRenderer.renderName(value, context);
    }

    public void render(StyleColorPercentages value, RendererContext context)
            throws IOException {

        styleColorRenderer.renderPercentages(value, context);
    }

    public void render(StyleColorRGB value, RendererContext context)
            throws IOException {

        styleColorRenderer.renderRGB(value, context);
    }

    // Javadoc inherited from super class.
    public void render(StyleURI value, RendererContext context)
            throws IOException {

        styleURIRenderer.render(value, context);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
