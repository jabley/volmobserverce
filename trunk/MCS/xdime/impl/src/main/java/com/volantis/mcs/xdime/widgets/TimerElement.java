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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TimerAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Clock XDIME element.
 */
public class TimerElement extends WidgetElement implements Loadable {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(TimerElement.class);
    
    /**
     * Creates and initialises new instance of Clock element.
     * @param context
     */
    public TimerElement(XDIMEContextInternal context) {
        // Initialise superclass.
        super(WidgetElements.TIMER, context);

        // Create an instance of DigitalClockContent attributes.
        // It'll be initialised later in initialiseAttributes() method.
        protocolAttributes = new TimerAttributes();
    }

    public void setLoadAttributes(LoadAttributes attributes) {
        getTimerAttributes().setLoadAttributes(attributes);		
    }
	
    public TimerAttributes getTimerAttributes(){
        return ((TimerAttributes)protocolAttributes);
    }	

    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) 
        throws XDIMEException {
        
        TimerAttributes timerAttributes = (TimerAttributes) protocolAttributes;
        //default values are 0;
        Integer start = new Integer(0);
        Integer stop = new Integer(0);
        
        String start_string = attributes.getValue("", "start-time");
        String stop_string = attributes.getValue("", "stop-time");
        
        //check if start-time attribute is correct
        if (start_string!=null){
            try {
                start = new Integer(start_string);
            }catch (NumberFormatException e){
                throw new XDIMEException(exceptionLocalizer.format(
                        "widget-unexpected-attributes-type",
                        "int"), e);
            }
            
            if (start.intValue()<0){
                throw new XDIMEException(exceptionLocalizer.format(
                        "attribute-value-incorrect", 
                        new Object[]{"start-time","start-time >= 0"}));
            }
        }

        //check if stop-time attribute is correct
        if (stop_string != null){
            try {
                stop = new Integer(stop_string);
            }catch (NumberFormatException e){
                throw new XDIMEException(exceptionLocalizer.format(
                        "widget-unexpected-attributes-type",
                        "int"), e);
            }
            
            if (stop.intValue()<0){
                throw new XDIMEException(exceptionLocalizer.format(
                        "attribute-value-incorrect", 
                        new Object[]{"stop-time","stop-time >= 0"}));
            }   
        }
        
        if (start.intValue() < stop.intValue()){
            throw new XDIMEException(exceptionLocalizer.format(
                    "attribute-value-incorrect", 
                    new Object[]{"start-time","start-time >= stop-time "}));
        }
        
        //attributes are ok
        timerAttributes.setStartTime(start);
        timerAttributes.setStopTime(stop);
    }
}
