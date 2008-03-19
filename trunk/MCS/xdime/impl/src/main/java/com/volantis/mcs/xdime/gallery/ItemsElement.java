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

import com.volantis.mcs.protocols.gallery.attributes.ItemsAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.Loadable;

public class ItemsElement extends BaseGalleryElement implements Loadable {
    
    public ItemsElement(XDIMEContextInternal context){
        super(GalleryElements.ITEMS, context);
        protocolAttributes = new ItemsAttributes();
    }
    	
    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {
        
        // Process this element for default.
        context.processElementForDefault(this);
        
        return super.callOpenOnProtocol(context, attributes);
    }
    
    public void setLoadAttributes(LoadAttributes attributes){
        getItemsAttributes().setLoadAttributes(attributes);
    }
    
    public ItemsAttributes getItemsAttributes(){
        return ((ItemsAttributes)protocolAttributes);
    }
 
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        ItemsAttributes itemsAttributes = (ItemsAttributes) protocolAttributes;
        
        itemsAttributes.setCount(attributes.getValue("", "count"));
    }
}
