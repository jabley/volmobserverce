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

package com.volantis.mcs.protocols.widgets.renderers;

import java.util.List;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.ClockContextHandler;
import com.volantis.mcs.protocols.widgets.EventName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.DigitalClockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.RefreshAttributes;
import com.volantis.styling.Styles;

public class DigitalClockDefaultRenderer extends ClockDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
    		ActionName.FORCE_SYNC,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {};
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {};
    

    private static char specialMarks[] = 
    	{'Y', 'y', 'F', 'm', 'D', 'd', 'h', 'H', 'i', 's', 'A', 'S'};
    
	private SpanAttributes clockSpanAttributes;
	
	protected void doRenderOpen(VolantisProtocol protocol,
			MCSAttributes attributes) throws ProtocolException {
		
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/vfc-clock.mscr",protocol);
        
        
        // Generate an ID for clock widget, if not specified.
        String clockId = attributes.getId();
        
        if (clockId == null) {
            clockId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        
        clockSpanAttributes = new SpanAttributes();
        clockSpanAttributes.copy(attributes);
        
        if (clockSpanAttributes.getId() == null) {
            clockSpanAttributes.setId(clockId);
        }
        
        protocol.writeOpenSpan(clockSpanAttributes);
	}

	protected void doRenderClose(VolantisProtocol protocol,
			MCSAttributes attributes) throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        DigitalClockAttributes digitalClockAttributes = (DigitalClockAttributes)attributes;
        
        protocol.writeCloseSpan(clockSpanAttributes);
        
        //retrive list of attributes of clock content element which are
        // within the timer element.
        List contentAttributes = digitalClockAttributes.getContentAttributes();
        
        // create ClockContentHandler to merge contents
        ClockContextHandler clockContextHandler = new ClockContextHandler();
        
        clockContextHandler.mergeContentAttributes(contentAttributes);
		
        String[] digits = clockContextHandler.getClockContentIds("digits");
        String[] days = clockContextHandler.getClockContentIds("days");
        String[] months = clockContextHandler.getClockContentIds("months");
        String[] ampm = clockContextHandler.getClockContentIds("ampm");
        String[] separators = clockContextHandler.getClockContentIds("separators");
		
        Styles styles = digitalClockAttributes.getStyles();
        
        StylesExtractor stylesExtractor = createStylesExtractor(
                protocol, styles);
                
        String dateTimeFormat = 
            getDateTimeFormat(stylesExtractor.getDateTimeFormat(),specialMarks);
        
        RefreshAttributes refreshAttributes = 
            digitalClockAttributes.getRefreshAttributes();
        
        StringBuffer textRefreshAttr = new StringBuffer();
        if(refreshAttributes != null) {
            textRefreshAttr.append(", refreshURL: ")
            .append(createJavaScriptString(refreshAttributes.getSrc()))
            .append(", refreshInterval: "+ refreshAttributes.getInterval());
        }        
        
        StringBuffer textBuffer = new StringBuffer();
        
        textBuffer.append(createJavaScriptWidgetRegistrationOpening(digitalClockAttributes.getId()))
                .append("new Widget.Clock(")
                .append(createJavaScriptString(clockSpanAttributes.getId()))
                .append(", {")
                .append("format: [").append(dateTimeFormat).append("], ")
                .append("digits: [").append(splitIds(digits)).append("], ")
                .append("days: [").append(splitIds(days)).append("], ")
                .append("months: [").append(splitIds(months)).append("], ")
                .append("ampm: [").append(splitIds(ampm)).append("], ")
                .append("separators: [")
                .append(splitIds(separators)).append("]")
                .append(textRefreshAttr)
                .append("})")
                .append(createJavaScriptWidgetRegistrationClosure());

        //clock-content elements must be rendered before digital-clock element
        addCreatedWidgetId(clockSpanAttributes.getId());
        addUsedWidgetIds(digits);
        addUsedWidgetIds(days);
        addUsedWidgetIds(months);
        addUsedWidgetIds(ampm);
        addUsedWidgetIds(separators);

        writeJavaScript(textBuffer.toString());
	}
    
    // Javadoc inherited
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }

    // Javadoc inherited
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }    
    
    // Javadoc inherited
    protected EventName[] getSupportedEventNames() {
        return SUPPORTED_EVENT_NAMES;
    }
}
