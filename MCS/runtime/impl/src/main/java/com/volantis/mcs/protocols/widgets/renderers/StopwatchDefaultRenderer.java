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
import com.volantis.mcs.protocols.widgets.attributes.StopwatchAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.BlockContainerAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.Styles;

public class StopwatchDefaultRenderer extends ClockDefaultRenderer {
    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        {
    		ActionName.START,
            ActionName.STOP,
            ActionName.RESET,
            ActionName.SPLIT
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
    
    private static char specialMarks[] = {'d', 'H', 'i', 's', 'S'};
    

    private SpanAttributes stopwatchSpanAttributes;
	
	protected void doRenderOpen(VolantisProtocol protocol,
			MCSAttributes attributes) throws ProtocolException {
		
        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireStandardLibraries(protocol);
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/vfc-clock.mscr",protocol);
        
        
        // Generate an ID for stopwatch widget, if not specified.
        String stopwatchId = attributes.getId();
        
        if (stopwatchId == null) {
            stopwatchId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        
        stopwatchSpanAttributes = new SpanAttributes();
        stopwatchSpanAttributes.copy(attributes);
        
        if (stopwatchSpanAttributes.getId() == null) {
            stopwatchSpanAttributes.setId(stopwatchId);
        }
        
        protocol.writeOpenSpan(stopwatchSpanAttributes);
	}

	protected void doRenderClose(VolantisProtocol protocol,
			MCSAttributes attributes) throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        StopwatchAttributes stopwatchAttributes = (StopwatchAttributes)attributes;
        
        protocol.writeCloseSpan(stopwatchSpanAttributes);
        
        // insert an empty block container which will be used to store split times
        BlockContainerAttributes blockContainerAttributes = 
            new BlockContainerAttributes();
        
        blockContainerAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        renderWidgetOpen(protocol, blockContainerAttributes);
        
        renderWidgetClose(protocol, blockContainerAttributes);
        
        
        // retrive list of attributes of clock content element which are
        // within the timer element.
        List contentAttributes = stopwatchAttributes.getContentAttributes();
        
        // create ClockContentHandler to merge contents
        ClockContextHandler clockContextHandler = new ClockContextHandler();
        
        clockContextHandler.mergeContentAttributes(contentAttributes);
		
        String[] digits = clockContextHandler.getClockContentIds("digits");
        String[] separators = clockContextHandler.getClockContentIds("separators");
		
        Styles styles = stopwatchAttributes.getStyles();
        
        StylesExtractor stylesExtractor = createStylesExtractor(
                protocol, styles);
        
        // count mode can be set to split and lap, default is split
        String count_mode = "split";
        
        if (styles.getPropertyValues().getComputedValue(StylePropertyDetails.MCS_COUNT_MODE) == StyleKeywords.LAP) {
            count_mode = "lap";
        }
        
        String dateTimeFormat = 
            getDateTimeFormat(stylesExtractor.getDateTimeFormat(),specialMarks);

        StringBuffer textBuffer = new StringBuffer();
        
        textBuffer.append(createJavaScriptWidgetRegistrationOpening(stopwatchSpanAttributes.getId()))
                .append("new Widget.Stopwatch(")
                .append(createJavaScriptString(stopwatchSpanAttributes.getId()))
                .append(", '").append(count_mode).append("', ")
                .append(createJavaScriptWidgetReference(blockContainerAttributes.getId()))
                .append(", {")
                .append("format: [").append(dateTimeFormat).append("], ")
                .append("digits: [").append(splitIds(digits)).append("], ")
                .append("separators: [")
                .append(splitIds(separators)).append("]")
                .append("})")
                .append(createJavaScriptWidgetRegistrationClosure());

        //clock-content elements must be rendered before stopwatch element
        addCreatedWidgetId(stopwatchSpanAttributes.getId());
        addUsedWidgetIds(digits);
        addUsedWidgetIds(separators);
        addUsedWidgetId(blockContainerAttributes.getId());

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
