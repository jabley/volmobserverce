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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.widgets.attributes.FetchAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class for XDIME2 widget:fetch element, used to load ajax content by MCS request
 */
public class FetchElement extends WidgetElement {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(FetchElement.class);    
    
    
    public FetchElement(XDIMEContextInternal context) {
        super(WidgetElements.FETCH, context);
        protocolAttributes = new FetchAttributes();
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {
        
        // Parent is a BlockElement 
        if (parent instanceof Fetchable) {
            ((Fetchable) parent).setFetchAttributes(getFetchAttributes());
        }
    }

    public FetchAttributes getFetchAttributes() {
        return (FetchAttributes) protocolAttributes;
    }

    /**
     * src - specifies of content to load
     * when - specifies when the content should be load
     * transformation - specifies of xsl template to transformation source content
     * transform-cache - indicates if transformation should be cached
     * transform-compile - indicates if transformation template should be compiled  
     */
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        ((FetchAttributes) protocolAttributes).setSrc(getSrcAttributeValue(context, attributes));
        ((FetchAttributes) protocolAttributes).setWhen(attributes.getValue("", "when"));
        ((FetchAttributes) protocolAttributes).setService(attributes.getValue("", "service"));
        ((FetchAttributes) protocolAttributes).setTransformation(getTransformationAttributeValue(context, attributes));       
        ((FetchAttributes) protocolAttributes).setTransformCache(attributes.getValue("", "transform-cache"));
        ((FetchAttributes) protocolAttributes).setTransformCompile(attributes.getValue("", "transform-compile"));
    }
    
    /**
     * Retrieves the value of the 'src' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'src' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getSrcAttributeValue(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        String src = attributes.getValue("","src");
        
        if (src != null) {
            src = rewriteURLWithPageURLRewriter(context, src, PageURLType.WIDGET);
        }
        
        return src;
    }
    
    /**
     * Retrieves the value of the 'transformation' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'transformation' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getTransformationAttributeValue(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        
        String transformation = attributes.getValue("","transformation");        
        URI transURI = null;
        try {
            transURI = new URI(transformation);
        } catch (URISyntaxException e) {
              throw new XDIMEException(exceptionLocalizer.format(
                "xdime-attribute-value-invalid", new String[] {transformation, "transformation in widget:fetch element"}), e);
        }
        
        return rewriteURLWithPageURLRewriter(context, transURI.toString(), PageURLType.WIDGET);
    }
}
