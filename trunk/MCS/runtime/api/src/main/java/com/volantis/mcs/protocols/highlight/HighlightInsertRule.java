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

package com.volantis.mcs.protocols.highlight;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.renderers.StylesExtractor;
import com.volantis.mcs.protocols.widgets.renderers.WidgetHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;

/**
 * Highlight rule to insert widget initialize function in javascript script block after all element 
 * and include javascript file in head document   
 */

public class HighlightInsertRule extends HighlightRule {
	    
    /**
     * Create a javascript with initialize widget after all element  
     *
     * @param element current element
     * @throws ProtocolException 
     */    
	void transform(Element element, DOMProtocol protocol, Document document) throws ProtocolException {
    
        if(!(protocol.getProtocolConfiguration().isFrameworkClientSupported() && protocol.supportsJavaScript())) {
            return;
        }          
        WidgetHelper.requireLibrary("/prototype.mscr", protocol);
        WidgetHelper.requireLibrary("/scriptaculous.mscr", protocol);                
        WidgetHelper.requireLibrary("/vfc-base.mscr", protocol);                
        WidgetHelper.requireLibrary("/vfc-transitions.mscr", protocol);                
        WidgetHelper.requireLibrary("/vfc-highlight.mscr", protocol);                

        String widgetID = null;
        if(element.getAttributeValue("id") == null) {
            widgetID = protocol.getMarinerPageContext().generateUniqueFCID();
            element.setAttribute("id", widgetID);
        } else {
            widgetID = element.getAttributeValue("id");                        
        }        
                        
        // Default values if not specified in styles        
        String startColor = "#ffffff";
        String endColor = startColor; 
        
        Styles styles = element.getStyles();        
        
        StyleValue startColorValue = styles.getPropertyValues().getComputedValue(StylePropertyDetails.BACKGROUND_COLOR);
        if(startColorValue != null) {
            startColor = startColorValue.getStandardCSS();
        }
        
        //if default color is set, it should be change for highlight effect to white color 
        if(startColor.equals("transparent")) {
            startColor = "#ffffff";                
        }
                
        Styles focusStyles = styles.findNestedStyles(StatefulPseudoClasses.FOCUS);
        
        if (focusStyles != null) {
            StyleValue endColorValue = focusStyles.getPropertyValues().getComputedValue(StylePropertyDetails.BACKGROUND_COLOR);
            if (endColorValue != null) {
                endColor = endColorValue.getStandardCSS();                 
                // highlight effect overrides standard behaviour of focus  
                focusStyles.getPropertyValues().setComputedAndSpecifiedValue(StylePropertyDetails.BACKGROUND_COLOR, startColorValue);   
            }            
        }
                            		
		Element newElement = element.getDOMFactory().createElement();
        newElement.setName("script");
        newElement.setAttribute("type","text/javascript");
        newElement.insertAfter(element);
		Text scriptContent = element.getDOMFactory().createText();

        StylesExtractor extractor = WidgetHelper.createStylesExtractor(protocol, styles);       
        String highlightScript = "Widget.register('"+ widgetID +"',new Widget.HighlightingNavigation('" + widgetID + "', " +
        " {" +
        " time: " + extractor.getEffectDuration() + ", " +
        " startColor: '" + startColor + "', " +
        " endColor: '" + endColor + "'" +
        "}));";
        
        scriptContent.append(highlightScript);
        newElement.addHead(scriptContent);
        
	}    
}
