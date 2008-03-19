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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.impl.rewriter;

import java.io.IOException;
import java.io.StringWriter;

import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Implementation of {@link OutputStyledElementIteratee}
 * Responsible for converting styles to 'style' attribute of element 
 */
public class DOMStyleAttributeRewriter implements
        OutputStyledElementIteratee {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DOMStyleAttributeRewriter.class);    
    
    /**
     * Rewrites styleProperties from element as 'style' attribute of element
     * @param element
     */
    public IterationAction next(OutputStyledElement element) {
        OutputStyles styles = element.getStyles();
        if (styles != null && !styles.isEmpty()) {
            StyleProperties properties =
                styles.getPathProperties(PseudoStylePath.EMPTY_PATH);
            try {
                element.getElement().setAttribute("style",
                        renderProperties(properties));
            } catch (IOException e){
                // this is not critical exception style attriubte cannot be rendered
                // for current element, processing might be continued
                if(logger.isDebugEnabled()){
                    logger.debug("Style properties for element: "+element.getName()+
                            "cannot be rendered",e);
                }
            }
            element.clearStyles();
        }
        return IterationAction.CONTINUE;        
    }     

    /**
     * Use {@link StyleSheetRenderer} to render style propreries as string
     * @param properties
     * @return
     * @throws IOException
     */
    private String renderProperties(
            StyleProperties properties) throws IOException {
        StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
        StringWriter writer = new StringWriter();
        RendererContext context = new RendererContext(writer, renderer);
        renderer.renderStyleProperties(properties, context);
        context.flushStyleSheet();
        return writer.getBuffer().toString();
    }

}
