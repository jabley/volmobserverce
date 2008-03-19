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

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.css.CSSModule;
import com.volantis.mcs.protocols.css.DOMPlaceHolder;
import com.volantis.mcs.protocols.css.PlaceHolder;

/**
 * The CSS Module for XHTML.
 */
public class XHTMLBasicCSSModule
        implements CSSModule {

    /**
     * The asset resolver.
     */
    private final AssetResolver resolver;
    
    /**
     * The value for media attribute.
     */
    private String media;

    /**
     * Initialise.
     *
     * @param pageContext The page context.
     */
    public XHTMLBasicCSSModule(MarinerPageContext pageContext) {
        this.resolver = pageContext.getAssetResolver();
    }

    // Javadoc inherited.
    public PlaceHolder addExternalPlaceHolder(OutputBuffer buffer) {
        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        Element link = dom.addElement("link");
        link.setAttribute("rel", "stylesheet");
        link.setAttribute("type", "text/css");
        
        if (media != null) {
            link.setAttribute("media", media);
        }

        return new DOMPlaceHolder(link);
    }

    // Javadoc inherited.
    public void updateExternalPlaceHolder(PlaceHolder placeHolder, String url) {
        DOMPlaceHolder domPlaceHolder = (DOMPlaceHolder) placeHolder;
        Element link = domPlaceHolder.getElement();

        String rewrittenURL = resolver.rewriteURLWithPageURLRewriter(
                url, PageURLType.STYLE_SHEET);
        link.setAttribute("href", rewrittenURL);
    }

    // Javadoc inherited.
    public PlaceHolder addInlinePlaceHolder(OutputBuffer buffer) {
        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        Element style = dom.addElement("style");
        style.setAttribute("type", "text/css");
        
        if (media != null) {
            style.setAttribute("media", media);
        }

        return new DOMPlaceHolder(style);
    }

    // Javadoc inherited.
    public void updateInlinePlaceHolder(PlaceHolder placeHolder, String css) {
        DOMPlaceHolder domPlaceHolder = (DOMPlaceHolder) placeHolder;
        Element style = domPlaceHolder.getElement();

        Text text = style.getDOMFactory().createText();
        text.append(css);
        style.addTail(text);
    }

    /**
     * @param media The media to set.
     */
    public void setMedia(String media) {
        this.media = media;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10638/1	emma	VBM:2005120505 Forward port: Generated XHTML was invalid - had no head tag but had head content

 06-Dec-05	10623/1	emma	VBM:2005120505 Generated XHTML was invalid: missing head tag but head content

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 ===========================================================================
*/
