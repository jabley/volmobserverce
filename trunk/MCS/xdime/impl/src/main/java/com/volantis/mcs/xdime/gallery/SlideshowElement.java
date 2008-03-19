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

import com.volantis.mcs.protocols.gallery.attributes.SlideshowAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

public class SlideshowElement extends BaseGalleryElement   {
    
    public SlideshowElement(XDIMEContextInternal context){
        super(GalleryElements.SLIDESHOW, context);
        
        protocolAttributes = new SlideshowAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
        
        // Process this Slideshow element for default.
        context.processElementForDefault(this);

        // Make sure, that if there's no items attribute specified,
        // there is a default items element.
        if (getSlideshowAttributes().getItems() == null) {
            checkDefaultElement(GalleryElements.ITEMS);
        }
        
        return super.callOpenOnProtocol(context, attributes);
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        SlideshowAttributes galleryAttributes = (SlideshowAttributes) protocolAttributes;
        
        galleryAttributes.setItems(attributes.getValue("", "items"));
    }
    
    protected SlideshowAttributes getSlideshowAttributes() {
        return (SlideshowAttributes) protocolAttributes;
    }
}
