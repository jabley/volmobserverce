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
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TimerAttributes;
import com.volantis.styling.Styles;

public class TimerDefaultRenderer extends ClockDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
            ActionName.START,
            ActionName.STOP,
            ActionName.RESET
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        {
            PropertyName.START_TIME,
            PropertyName.STOP_TIME
        };
    
    /**
     * Array of supported event names.
     */
    private static final EventName[] SUPPORTED_EVENT_NAMES =
        {
            EventName.FINISHED
        };

    private static char specialMarks[] = {'d', 'H', 'i', 's', 'S'};
    

	private SpanAttributes timerSpanAttributes;
	
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
        String timerId = attributes.getId();
        
        if (timerId == null) {
            timerId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        
        timerSpanAttributes = new SpanAttributes();
        timerSpanAttributes.copy(attributes);
        
        if (timerSpanAttributes.getId() == null) {
            timerSpanAttributes.setId(timerId);
        }
        
        protocol.writeOpenSpan(timerSpanAttributes);
	}

   
    
	protected void doRenderClose(VolantisProtocol protocol,
			MCSAttributes attributes) throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        TimerAttributes timerAttributes = (TimerAttributes)attributes;
        
        protocol.writeCloseSpan(timerSpanAttributes);
        
        // retrive list of attributes of clock content element which are
        // within the timer element.
        List contentAttributes = timerAttributes.getContentAttributes();
        
        // create ClockContentHandler to merge contents
        ClockContextHandler clockContextHandler = new ClockContextHandler();
        
        clockContextHandler.mergeContentAttributes(contentAttributes);
        
        String[] digits = clockContextHandler.getClockContentIds("digits");
        String[] separators = clockContextHandler.getClockContentIds("separators");
        
        Styles styles = timerAttributes.getStyles();
        
        StylesExtractor stylesExtractor = createStylesExtractor(
                protocol, styles);
        
        String dateTimeFormat = 
            getDateTimeFormat(stylesExtractor.getDateTimeFormat(),specialMarks);

        
        LoadAttributes loadAttributes = timerAttributes.getLoadAttributes();
        
        StringBuffer textLoadAttr = new StringBuffer();
        if(loadAttributes != null) {
            textLoadAttr.append(", load_src: ")
            .append(createJavaScriptString(loadAttributes.getSrc()));
        }
        
        StringBuffer startStopAttr = new StringBuffer();
        Integer start = timerAttributes.getStartTime();
        Integer stop = timerAttributes.getStopTime();
        if(start!= null && stop !=null) {
            startStopAttr.append(", start_time: '")
            .append(timerAttributes.getStartTime().toString()).append("', ")
            .append("stop_time: '")
            .append(timerAttributes.getStopTime().toString()).append("'");
        }

        StringBuffer textBuffer = new StringBuffer();
        
        textBuffer.append(createJavaScriptWidgetRegistrationOpening(timerAttributes.getId()))
                .append("new Widget.Timer(")
                .append(createJavaScriptString(timerSpanAttributes.getId()))
                .append(", {")
                .append("start_time: '").append(""+timerAttributes.getStartTime()).append("', ")
                .append("stop_time: '").append(""+timerAttributes.getStopTime()).append("', ")
                .append("format: [").append(dateTimeFormat).append("], ")
                .append("digits: [").append(splitIds(digits)).append("], ")
                .append("separators: [")
                .append(splitIds(separators)).append("]")
                .append(textLoadAttr)
                .append(startStopAttr)
                .append("})")
                .append(createJavaScriptWidgetRegistrationClosure());

        //clock-content elements must be rendered before timer element
        addCreatedWidgetId(timerSpanAttributes.getId());
        addUsedWidgetIds(digits);
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
