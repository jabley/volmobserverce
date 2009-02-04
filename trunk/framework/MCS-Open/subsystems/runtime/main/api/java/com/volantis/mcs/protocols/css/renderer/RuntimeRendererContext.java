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
* $Header: /src/voyager/com/volantis/mcs/protocols/css/renderer/RuntimeRendererContext.java,v 1.8 2002/10/14 13:23:11 sumit Exp $
* ----------------------------------------------------------------------------
* (c) Volantis Systems Ltd 2002. 
* ----------------------------------------------------------------------------
* Change History:
*
* Date         Who             Description
* ---------    --------------- -----------------------------------------------
* 28-Apr-02    Allan           VBM:2002040804 - Created. The context in which
*                              the rendering of style sheet and its component
*                              parts takes place. This is the runtime version
*                              that has a MarinerRequestContext and an
*                              VariantSelectionPolicy.
* 29-May-02    Paul            VBM:2002050301 - Added identity factory.
* 18-Jun-02    Steve           VBM:2002040807 - Added method to allow the
*                              setting of the mariner to protocol element
*                              mapping this can be called with either the
*                              protocol or the mapping from the protocol.
* 26-Jun-02    Steve           VBM:2002040807 - Changed case of support
*                              strings
* 28-Jun-02    Paul            VBM:2002051302 - Wrap ValuesRenderer in the
*                              RuntimeValuesRenderer.
* 10-Jul-02    Steve           VBM:2002040807 - Removed settings for the
*                              CSS transformation as that is now done in the
*                              stylesheet generator which has access to all
*                              the parameters needed here.
* 14-Oct-02    Sumit           VBM:2002070803 Changed constructor to get an
*                              RSB from the pool of the incoming req ctx and
*                              use this in the super call
* ----------------------------------------------------------------------------
*/

package com.volantis.mcs.protocols.css.renderer;

import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;

import java.io.Writer;

/**
 * The context in which the rendering of style sheet and its component
 * parts takes place. This is the Runtime version that has a
 * MarinerRequestContext and an VariantSelectionPolicy.
 */
public class RuntimeRendererContext
        extends RendererContext {

    /**
     * The protocol specific configuration for the renderer.
     */
    private final RuntimeRendererProtocolConfiguration
            runtimeRendererProtocolConfiguration;

    /**
     * Construct a RendererContext that will write the renderer style sheet
     * to a given Writer utilizing the given StyleSheetRenderer.
     *
     * @param writer         the Writer
     * @param renderer       the StyleSheetRenderer
     */
    public RuntimeRendererContext(
            Writer writer,
            StyleSheetRenderer renderer,
            RuntimeRendererProtocolConfiguration
                    runtimeRendererProtocolConfiguration,
            CSSVersion cssVersion) {
        super(writer, renderer,
                new ReusableStringBuffer(), cssVersion);

        this.runtimeRendererProtocolConfiguration =
                runtimeRendererProtocolConfiguration;
    }

    /**
     * Return the {@link RuntimeRendererProtocolConfiguration} for this
     * context.
     *
     * @return
     */
    public RuntimeRendererProtocolConfiguration
    getRuntimeRendererProtocolConfiguration() {

        return runtimeRendererProtocolConfiguration;
    }
}

/*
===========================================================================
Change History
===========================================================================
$Log$

04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

26-Feb-04	3233/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

25-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

===========================================================================
*/

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
