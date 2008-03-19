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

package com.volantis.mcs.xdime.response;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.response.attributes.ResponseAutocompleteAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Implementation of AJAX resposne for Autocomplete widget.
 */
public class ResponseAutocompleteElement extends ResponseElement {
    
    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ResponseMessageElement.class);
    
    private UnorderedListAttributes ulAttributes;    
    
    public ResponseAutocompleteElement(XDIMEContextInternal context) {
        super(ResponseElements.AUTOCOMPLETE, context);
        protocolAttributes = new ResponseAutocompleteAttributes();
    }
    
    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);        
        ulAttributes = new UnorderedListAttributes();        
        ulAttributes.setStyles(protocolAttributes.getStyles().copy());
        
        Styles ulStyles = ulAttributes.getStyles();
        if(ulStyles != null) {
            ulStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.DISPLAY, StyleKeywords.BLOCK);
            
            //defaults styles for UL list
            if(ulStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MARGIN_LEFT) == null) {
                ulStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.MARGIN_LEFT, StyleValueFactory.getDefaultInstance().getLength(null, 0, LengthUnit.PX));                            
            }
            if(ulStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MARGIN_TOP) == null) {
                ulStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.MARGIN_TOP, StyleValueFactory.getDefaultInstance().getLength(null, 0, LengthUnit.PX));                            
            }
            if(ulStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MARGIN_BOTTOM) == null) {
                ulStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.MARGIN_BOTTOM, StyleValueFactory.getDefaultInstance().getLength(null, 0, LengthUnit.PX));                            
            }
            
            if(ulStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.PADDING_LEFT) == null) {
                ulStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.PADDING_LEFT, StyleValueFactory.getDefaultInstance().getLength(null, 0, LengthUnit.PX));                            
            }
            if(ulStyles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.WIDTH) == null) {
                ulStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.WIDTH, StyleValueFactory.getDefaultInstance().getPercentage(null, 100));                            
            }                       
        }
        
        try {
            protocol.writeOpenUnorderedList(ulAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }                       
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {
        
        // Close the 'UL' element.
        VolantisProtocol protocol = getProtocol(context);
        protocol.writeCloseUnorderedList(ulAttributes);    
                    
    }
    
    /**
     * Returns response autocomplete attributes.
     * 
     * @return the attributes.
     */
    protected ResponseAutocompleteAttributes getAutocompleteAttributes() {
        return (ResponseAutocompleteAttributes) protocolAttributes;
    }
    
    
}
