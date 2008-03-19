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
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.renderers.WidgetHelper;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.Styles;

/**
 * Corners rule to insert widget initialize function in javascript script block after all specified box element 
 * and include javascript file in head document   
 */
public class CornersInsertRule extends CornersRule {

    /**
     * Create a javascript with initialize widget after all specified box element  
     *
     * @param element current element
     * @throws ProtocolException 
     */        
    void transform(Element element, DOMProtocol protocol, Element body)
            throws ProtocolException {
        
        if(!(protocol.getProtocolConfiguration().isFrameworkClientSupported() && protocol.supportsJavaScript())) {
            return;
        }          
        WidgetHelper.requireLibrary("/prototype.mscr", protocol);
        WidgetHelper.requireLibrary("/scriptaculous.mscr", protocol);                
        WidgetHelper.requireLibrary("/vfc-base.mscr", protocol);                
        WidgetHelper.requireLibrary("/vfc-corners.mscr", protocol);                

        // in order to disable generating style properties for type
        protocol.getExtractorContext().setGenerateTypeRules(false);
        
        String widgetID = null;
        if(element.getAttributeValue("id") == null) {
            widgetID = protocol.getMarinerPageContext().generateUniqueFCID();
            element.setAttribute("id", widgetID);
        } else {
            widgetID = element.getAttributeValue("id");                        
        }        
                        
        // Default values if not specified in styles              
        Styles styles = element.getStyles();        
        
        StyleValue topLeftRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS);
        StyleValue topRightRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS);
        StyleValue bottomRightRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS);
        StyleValue bottomLeftRadius = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS);
                        
        //emulated version
        Element newElement = element.getDOMFactory().createElement();
        newElement.setName("script");
        newElement.setAttribute("type","text/javascript");
        newElement.insertAfter(element);
        Text scriptContent = element.getDOMFactory().createText();

        String cornersScript = "Widget.addStartupItem(function(){Widget.register('"+ widgetID +"',new Widget.Corners('" + widgetID + "', [";
        cornersScript += "[";
        if(topLeftRadius != null) {
            cornersScript += "'" + ((StylePair)topLeftRadius).getFirst().getStandardCSS() + "'";
            cornersScript += ",'" + ((StylePair)topLeftRadius).getSecond().getStandardCSS() + "'";
        }
        cornersScript += "]";
        cornersScript += ",[";
        if(topRightRadius != null) {
            cornersScript += "'" + ((StylePair)topRightRadius).getFirst().getStandardCSS() + "'";
            cornersScript += ",'" + ((StylePair)topRightRadius).getSecond().getStandardCSS() + "'";
        }
        cornersScript += "]";
        cornersScript += ",[";
        if(bottomRightRadius != null) {
            cornersScript += "'" + ((StylePair)bottomRightRadius).getFirst().getStandardCSS() + "'";
            cornersScript += ",'" + ((StylePair)bottomRightRadius).getSecond().getStandardCSS() + "'";
        }
        cornersScript += "]";
        cornersScript += ",[";
        if(bottomLeftRadius != null) {
            cornersScript += "'" + ((StylePair)bottomLeftRadius).getFirst().getStandardCSS() + "'";
            cornersScript += ",'" + ((StylePair)bottomLeftRadius).getSecond().getStandardCSS() + "'";
        }
        cornersScript += "]";        
        cornersScript += "]))});";
        
        scriptContent.append(cornersScript);
        newElement.addHead(scriptContent);      
       
        //set startup onload body element 
        setScriptStartup(body);
    }
    
    /**
     * Set Widget.startup() on body element in onload attribute
     * @param body body element traversed document
     */
    private void setScriptStartup(Element body) {
        // Retrieve existing 'onload' attribute.
        String currentScript = body.getAttributeValue("onload");
        // Prepare the startup script.
        String widgetScript = "Widget.startup();";     
       // Concatenate existing value with the startup script.
        String script = "";        
        if (currentScript == null) {
            script = widgetScript;
        } else {
            if(currentScript.indexOf("Widget.startup()") == -1) {
                script = currentScript + ";" + widgetScript;                
            } else {
                script = widgetScript;                
            }
        }        
        // Set concatenated attribute value
        body.setAttribute("onload", script);        
    }

}
