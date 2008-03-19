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

package com.volantis.mcs.xdime.gallery;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.gallery.GalleryModule;
import com.volantis.mcs.protocols.gallery.renderers.ElementRenderer;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.StyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Base class for all Gallery elements. Implements common methods 
 */
public abstract class BaseGalleryElement extends StylableXDIMEElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(BaseGalleryElement.class);
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(BaseGalleryElement.class);

    public BaseGalleryElement(ElementType type, XDIMEContextInternal context) {
        super(type, StyledStrategy.STRATEGY, context);
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
        try {
            ElementRenderer renderer = getElementRenderer(context);
            if (null == renderer){
                // Do fallback if ticker is not supported by the protocol
                return doFallbackOpen(context, attributes);        
            }
            renderer.renderOpen(getProtocol(context), protocolAttributes);
            
            if (!renderer.shouldRenderContents(getProtocol(context), protocolAttributes)) {
                return XDIMEResult.SKIP_ELEMENT_BODY; 
            }
        } catch (ProtocolException e) {
            LOGGER.error("rendering-error", getTagName(), e);

            throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                "rendering-error", getTagName()), e);
        }    
        return XDIMEResult.PROCESS_ELEMENT_BODY;       
     }
    
    // Javadoc inherited
     public void callCloseOnProtocol(XDIMEContextInternal context) throws XDIMEException {
         try {
             ElementRenderer renderer = getElementRenderer(context);
             if (null == renderer){
                 // Do fallback if ticker is not supported by the protocol
                 doFallbackClose(context);
                 return;
             }
             renderer.renderClose(getProtocol(context), protocolAttributes);
         } catch (ProtocolException e) {
             LOGGER.error("rendering-error", getTagName(), e);

             throw new XDIMEException(EXCEPTION_LOCALIZER.format(
                 "rendering-error", getTagName()), e);
         }            // TODO: do we need to do anything here?
      }

     /**
      * Fallback behaviour for non-HTML protocols that do not support widgets 
      * 
      * Default implementation does nothing and returns PROCESS_ELEMENT_BODY.
      * Derived classes may override thismethod to provide their own fallback,
      * but it must be protocol independent (the actual rendering must be 
      * delegated to the protocol) 
      */
     protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
         // By default do nothing and process body
         return XDIMEResult.PROCESS_ELEMENT_BODY;
     }

     /**
      * Fallback behaviour for non-HTML protocols that do not support ticker
      * 
      * Default implementation does nothing.
      * Derived classes may override thismethod to provide their own fallback,
      * but it must be protocol independent (the actual rendering must be 
      * delegated to the protocol)
      */
     protected void doFallbackClose(XDIMEContextInternal context) {
         // By default do nothing 
     }
     
    /**
     * Convenience method for getting Ticker Module implementation
     * associated with currently used protocol 
     */
    protected GalleryModule getGalleryModule(XDIMEContextInternal context) {
        return getProtocol(context).getGalleryModule();        
    }

    /**
     * Convenience method for getting Ticker Element Renderer implementation
     * associated with currently used protocol 
     * 
     * @throws ProtocolException 
     */
    protected ElementRenderer getElementRenderer(XDIMEContextInternal context) 
        throws ProtocolException {
        
        // First get the ticker module, which is a part of protocol 
        // responsible for rendering ticker widgets 
        GalleryModule galleryModule =  getGalleryModule(context);
        
        // No renderer if no module 
        if (null == galleryModule){
            return null;
        }

        // Get renderer for this particular widget
        return galleryModule.getElementRenderer(protocolAttributes);
    }

    
    /**
     * Creates new instance of output buffer.
     * 
     * @param context The context used.
     * @return Created output buffer.
     */
    protected OutputBuffer createOutputBuffer(XDIMEContextInternal context) {
        return getProtocol(context).
            getOutputBufferFactory().createOutputBuffer();
    }
    
    /**
     * Checks, that there's a default element of specified type.
     */
    protected void checkDefaultElement(ElementType type) throws XDIMEException {
        if (context.getDefaultElement(type) == null) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format("gallery-no-default-element-error",
                    new Object[] {type}));
        }
    }
}
