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
import com.volantis.mcs.protocols.gallery.attributes.GalleryAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;

public class GalleryElement extends BaseGalleryElement   {
    
    /**
     * Used to retrieve localized exception messages.
     */
    protected static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    GalleryElement.class);

    public GalleryElement(XDIMEContextInternal context){
        super(GalleryElements.GALLERY, context);
        
        protocolAttributes = new GalleryAttributes();
    }
    
    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
        
        // Process this Gallery element for default.
        context.processElementForDefault(this);

        // Make sure, that if there's no items attribute specified,
        // there is a default items element.
        if (getGalleryAttributes().getItems() == null) {
            checkDefaultElement(GalleryElements.ITEMS);
        }
        
        return super.callOpenOnProtocol(context, attributes);
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        GalleryAttributes galleryAttributes = (GalleryAttributes) protocolAttributes;
        
        galleryAttributes.setItems(attributes.getValue("", "items"));
        galleryAttributes.setSlideshow(attributes.getValue("", "slideshow"));
        galleryAttributes.setSlideshowPopup(attributes.getValue("", "slideshow-popup"));
    }
    
    protected GalleryAttributes getGalleryAttributes() {
        return (GalleryAttributes) protocolAttributes;
    }
}
