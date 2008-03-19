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

package com.volantis.mcs.protocols.corners;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;

import java.util.HashMap;

/**
 * Visitor used to convert mcs-border-radius individual properties on XHTM2 elements into javascript code.
 */
public final class CornersVisitor extends WalkingDOMVisitorStub {
    
    private HashMap ruleSet = null;
    private DOMProtocol protocol = null;
    private Element body = null;
    
    protected static final CornersInsertRule INSERT = new CornersInsertRule();
    
    /**
     * Create a visitor loaded with the supplied ruleset.
     * 
     * @param ruleSet rule set used to transform the DOM. Assumed not to be null
     * @param protocol - needed for getting url for javascript files
     * 
     */   
    public CornersVisitor(HashMap ruleSet, DOMProtocol protocol) {
         this.ruleSet = ruleSet;
         this.protocol = protocol;         
    }
    
    /**
     * Check the given element, if it contains a mcs-border-radius 
     * look up the defined rule and call the transform method on it.
     *
     * @param element current element
     */    
    public void visit(Element element) {
        Styles styles = element.getStyles();
        if(styles != null) {
            StyleValue topLeftRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS);
            StyleValue topRightRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS);
            StyleValue bottomRightRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS);
            StyleValue bottomLeftRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS);
                       
            if(topLeftRadius != null || topRightRadius != null || bottomRightRadius != null || bottomLeftRadius != null) {
                
                CornersRule rule = (CornersRule)ruleSet.get(element.getName());   
                 if (rule != null) {
                     try {
                         rule.transform(element, this.protocol, this.body);
                     } catch (ProtocolException e) {
                         throw new IllegalStateException("Protocol not applicable for rounded corners widget ");                        
                     }
                 }                  
             }
         }         
    }
    
    /**
     * Set reference of body Element
     * @param body body Element
     */
    public void setBodyElement(Element body) {
        this.body = body;         
    }    
}
