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

import java.util.Set;
import java.util.HashSet;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.ActionName;
import com.volantis.mcs.protocols.widgets.PropertyName;
import com.volantis.mcs.protocols.widgets.attributes.DatePickerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.EffectBlockAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModule;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;
import com.volantis.mcs.runtime.scriptlibrarymanager.ScriptModulesDefinitionRegistry;
import com.volantis.styling.Styles;

/**
 * Renderer for DatePicker element 
 *
 */
public class DatePickerDefaultRenderer extends WidgetDefaultRenderer {

    public static final ScriptModule MODULE = createAndRegisterModule();

    private static ScriptModule createAndRegisterModule() {
        Set dependencies = new HashSet();
        // always needed directly dependend module
        dependencies.add(WidgetScriptModules.EFFECT_BASE);
        dependencies.add(WidgetScriptModules.BASE_BB_EFFECT);
        dependencies.add(WidgetScriptModules.BASE_BB_CLIENT);
        // conditionals effects modules
        dependencies.add(WidgetScriptModules.CE_APPEAR);
        dependencies.add(WidgetScriptModules.CE_BLINDDOWN);
        dependencies.add(WidgetScriptModules.CE_BLINDUP);
        dependencies.add(WidgetScriptModules.CE_DROPOUT);
        dependencies.add(WidgetScriptModules.CE_FADE);
        dependencies.add(WidgetScriptModules.CE_FOLD);
        dependencies.add(WidgetScriptModules.CE_GROW);
        dependencies.add(WidgetScriptModules.CE_PUFF);
        dependencies.add(WidgetScriptModules.CE_SHAKE);
        dependencies.add(WidgetScriptModules.CE_SHRINK);
        dependencies.add(WidgetScriptModules.CE_SLIDE);
        dependencies.add(WidgetScriptModules.CE_PULSATE);

        ScriptModule module = new ScriptModule("/vfc-datepicker.mscr", dependencies,
                31400, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }


    /**
     * Array of supported action names.
     */
    private static final ActionName[] SUPPORTED_ACTION_NAMES =
        new ActionName[] {
            ActionName.NEXT_MONTH,
            ActionName.PREVIOUS_MONTH,
            ActionName.NEXT_YEAR,
            ActionName.PREVIOUS_YEAR,
            ActionName.SET_TODAY,
            ActionName.DISMISS,
            ActionName.SHOW,
        };
    
    /**
     * Array of supported property names.
     */
    private static final PropertyName[] SUPPORTED_PROPERTY_NAMES =
        new PropertyName[] {
            PropertyName.MONTH,
            PropertyName.YEAR,
        };

    /**
     * Attributes of the rendered div element of this element.
     */    
    private String calendarDisplayId;
    
    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        require(MODULE, protocol, attributes);
        
        // Generate an ID for date-picker widget, if not specified.
        if(attributes.getId() == null) {
            attributes.setId(
                   protocol.getMarinerPageContext().generateUniqueFCID());
        } 
        
        // Date Picker internally renders a block
        EffectBlockAttributes blockAttributes = new EffectBlockAttributes();
        blockAttributes.copy(attributes);
        blockAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        pushMCSAttributes(blockAttributes);
        renderWidgetOpen(protocol, blockAttributes);
        
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        // require AJAX script module
        if ( ((DatePickerAttributes)attributes).getLoadAttributes() != null ) {
            require(WidgetScriptModules.BASE_AJAX, protocol, attributes);
        }

        // Retrieve block attributes stored in doRenderOpen
        EffectBlockAttributes blockAttributes = (EffectBlockAttributes)popMCSAttributes();
        renderWidgetClose(protocol, blockAttributes);
        addUsedWidgetId(blockAttributes.getId());

        DatePickerAttributes datePickerAttributes = (DatePickerAttributes) attributes;        
        
        // Create the widget
        StringBuffer textBuffer = new StringBuffer(createJavaScriptWidgetRegistrationOpening(attributes.getId()))
            .append("new Widget.DatePicker(")
            .append(createJavaScriptString(attributes.getId()))
            .append(", ").append(createJavaScriptString(blockAttributes.getId()))
            .append(", {");
        
        	if (datePickerAttributes.getInputField() != null) {
            	textBuffer.append("inputField:").append(createJavaScriptString(datePickerAttributes.getInputField())).append(", ");        		
        	} else {
        		if (datePickerAttributes.getDayInputField() != null) {
        			textBuffer.append("dayInputField:").append(createJavaScriptString(datePickerAttributes.getDayInputField())).append(", ");
        		}
        		if (datePickerAttributes.getMonthInputField() != null) {
        			textBuffer.append("monthInputField:").append(createJavaScriptString(datePickerAttributes.getMonthInputField())).append(", ");
        		}	
        		if (datePickerAttributes.getYearInputField() != null) {
        			textBuffer.append("yearInputField:").append(createJavaScriptString(datePickerAttributes.getYearInputField())).append(", ");
        		}	        		
        	}
        	if (calendarDisplayId!=null) 
        		textBuffer.append("calendarDisplayId:").append(createJavaScriptString(calendarDisplayId)).append(", "); 
        	
        	Styles styles = attributes.getStyles();
        	StyleValue unfoldon = styles.getPropertyValues().getSpecifiedValue(StylePropertyDetails.MCS_TOGGLE_EVENT);
          
        	if (unfoldon != null) 
        		textBuffer.append("unfoldon: ").append(createJavaScriptString(unfoldon.getStandardCSS())).append(", ");
        	
        	LoadAttributes loadAttributes = datePickerAttributes.getLoadAttributes();
        	if (loadAttributes != null) {
        		String load = "load_src: " + createJavaScriptString(loadAttributes.getSrc())
                      + ", load_when: " + createJavaScriptString(loadAttributes.getWhen()) + ", ";
        		textBuffer.append(load);
        	}        	
        	        	
            textBuffer.append(getDisappearableOptions(attributes))
            .append(", ").append(getAppearableOptions(attributes))
            .append("})")
            .append(createJavaScriptWidgetRegistrationClosure());
                
            writeJavaScript(textBuffer.toString());
            addCreatedWidgetId(attributes.getId());        
    }    
    
    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        // Render date-picker contentonly if widget is supported.
        return isWidgetSupported(protocol);
    }
    
    /**
     * Set's id of Table element.
     * @param id
     */
    public void renderCalendarDisplay(String id) {
        calendarDisplayId = id;
    }

    /**
     * @inheritDoc
     */
    protected ActionName[] getSupportedActionNames() {
        return SUPPORTED_ACTION_NAMES;
    }

    /**
     * @inheritDoc
     */
    protected PropertyName[] getSupportedPropertyNames() {
        return SUPPORTED_PROPERTY_NAMES;
    }
}
