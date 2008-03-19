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

package com.volantis.mcs.protocols.widgets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.NavigationListAttributes;
import com.volantis.mcs.protocols.NavigationListItemAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.response.attributes.ResponseDatePickerAttributes;
import com.volantis.mcs.protocols.response.attributes.ResponseDeckAttributes;
import com.volantis.mcs.protocols.response.attributes.ResponseErrorAttributes;
import com.volantis.mcs.protocols.response.attributes.ResponseTableBodyAttributes;
import com.volantis.mcs.protocols.response.renderers.ResponseDatePickerDefaultRenderer;
import com.volantis.mcs.protocols.response.renderers.ResponseDeckDefaultRenderer;
import com.volantis.mcs.protocols.response.renderers.ResponseErrorDefaultRenderer;
import com.volantis.mcs.protocols.response.renderers.ResponseTableBodyDefaultRenderer;
import com.volantis.mcs.protocols.widgets.DefaultJavaScriptContainerFactory.OrderException;
import com.volantis.mcs.protocols.widgets.attributes.AutocompleteAttributes;
import com.volantis.mcs.protocols.widgets.attributes.BlockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.BlockContentAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ButtonAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ButtonScriptHandlerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.CalendarDisplayAttributes;
import com.volantis.mcs.protocols.widgets.attributes.CarouselAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ClockContentAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DatePickerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DeckAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DeckPageAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DetailAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DigitalClockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DismissAttributes;
import com.volantis.mcs.protocols.widgets.attributes.DisplayAttributes;
import com.volantis.mcs.protocols.widgets.attributes.FieldAttributes;
import com.volantis.mcs.protocols.widgets.attributes.FieldExpanderAttributes;
import com.volantis.mcs.protocols.widgets.attributes.FoldingItemAttributes;
import com.volantis.mcs.protocols.widgets.attributes.HandlerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.InputAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LaunchAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LogAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MapAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkersAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MessageAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MonthDisplayAttributes;
import com.volantis.mcs.protocols.widgets.attributes.MultipleValidatorAttributes;
import com.volantis.mcs.protocols.widgets.attributes.NextAttributes;
import com.volantis.mcs.protocols.widgets.attributes.NextMonthAttributes;
import com.volantis.mcs.protocols.widgets.attributes.NextYearAttributes;
import com.volantis.mcs.protocols.widgets.attributes.OptionAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PauseAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PlayAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PopupAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PreviousAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PreviousMonthAttributes;
import com.volantis.mcs.protocols.widgets.attributes.PreviousYearAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ProgressAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ScriptAttributes;
import com.volantis.mcs.protocols.widgets.attributes.SelectAttributes;
import com.volantis.mcs.protocols.widgets.attributes.SetTodayAttributes;
import com.volantis.mcs.protocols.widgets.attributes.StopAttributes;
import com.volantis.mcs.protocols.widgets.attributes.StopwatchAttributes;
import com.volantis.mcs.protocols.widgets.attributes.SummaryAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TabAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TableAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TableBodyAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TabsAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TickerTapeAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TimerAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ValidateAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WizardAttributes;
import com.volantis.mcs.protocols.widgets.attributes.YearDisplayAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptArrayAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptObjectAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.JavaScriptStringAttributes;
import com.volantis.mcs.protocols.widgets.internal.attributes.ResponseClientAttributes;
import com.volantis.mcs.protocols.widgets.internal.renderers.JavaScriptArrayDefaultRenderer;
import com.volantis.mcs.protocols.widgets.internal.renderers.JavaScriptObjectDefaultRenderer;
import com.volantis.mcs.protocols.widgets.internal.renderers.JavaScriptStringDefaultRenderer;
import com.volantis.mcs.protocols.widgets.internal.renderers.ResponseClientDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.AutocompleteDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.AutocompleteRenderer;
import com.volantis.mcs.protocols.widgets.renderers.BlockContentDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.BlockDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.ButtonDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.ButtonScriptHandlerRenderer;
import com.volantis.mcs.protocols.widgets.renderers.CalendarDisplayDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.CarouselDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.ClockContentDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DatePickerDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DeckDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DeckPageDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DetailDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DigitalClockDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DismissRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DisplayDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DynamicMenuDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DynamicMenuWidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.FieldDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.FieldExpanderDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.FieldExpanderWidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.FoldingItemDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.HandlerDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.InputDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.LaunchRenderer;
import com.volantis.mcs.protocols.widgets.renderers.LogDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MapDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MapLocationMarkerDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MapLocationMarkersDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MapRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MessageDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MonthDisplayDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MultipleValidatorDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MultipleValidatorRenderer;
import com.volantis.mcs.protocols.widgets.renderers.NextDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.NextMonthDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.NextYearDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.OptionDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.PauseDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.PlayDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.PopupRenderer;
import com.volantis.mcs.protocols.widgets.renderers.PreviousDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.PreviousMonthDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.PreviousYearDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.ProgressDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.ScriptDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.SelectDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.SetTodayDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.StopDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.StopwatchDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.SummaryDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TableBodyDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TableDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TabsDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TabsRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TickerTapeDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TimerDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.ValidateDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.WidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.WizardDefaultRenderer;
import com.volantis.mcs.protocols.widgets.renderers.WizardRenderer;
import com.volantis.mcs.protocols.widgets.renderers.YearDisplayDefaultRenderer;

/**
 * Implementation of widget module suitable for HTML-based protocols.
 * 
 * Internally stores instances of renderers. The lifetime of this
 * class MUST be the same as the lifetime of request. The instance
 * MUST NOT be shared between request.
 * 
 */
public class WidgetDefaultModule implements XHTMLBasicWidgetModule {

     /**
     * Static immutable map specifying what renderer class should 
     * be used for each widget. Actual instances of renderers are
     * created lazily, only when needed, and have lifetime of 
     * request.
     */  
    private static final Map renderersClassMap = new HashMap();     
    static {
        renderersClassMap.put(CarouselAttributes.class, CarouselDefaultRenderer.class);
        renderersClassMap.put(PopupAttributes.class, PopupRenderer.class);
        renderersClassMap.put(DismissAttributes.class, DismissRenderer.class);
        renderersClassMap.put(TickerTapeAttributes.class, TickerTapeDefaultRenderer.class);
        renderersClassMap.put(ProgressAttributes.class, ProgressDefaultRenderer.class);
        renderersClassMap.put(NavigationListAttributes.class, DynamicMenuDefaultRenderer.class);
        renderersClassMap.put(NavigationListItemAttributes.class, DynamicMenuDefaultRenderer.class);
        renderersClassMap.put(FoldingItemAttributes.class, FoldingItemDefaultRenderer.class);
        renderersClassMap.put(SummaryAttributes.class, SummaryDefaultRenderer.class);
        renderersClassMap.put(DetailAttributes.class, DetailDefaultRenderer.class);
        renderersClassMap.put(WizardAttributes.class, WizardDefaultRenderer.class);
        renderersClassMap.put(LaunchAttributes.class, LaunchRenderer.class);
        renderersClassMap.put(FieldExpanderAttributes.class, FieldExpanderDefaultRenderer.class);
        renderersClassMap.put(ValidateAttributes.class, ValidateDefaultRenderer.class);
        renderersClassMap.put(MessageAttributes.class, MessageDefaultRenderer.class);
        renderersClassMap.put(MultipleValidatorAttributes.class, MultipleValidatorDefaultRenderer.class);
        renderersClassMap.put(FieldAttributes.class, FieldDefaultRenderer.class);
        renderersClassMap.put(AutocompleteAttributes.class, AutocompleteDefaultRenderer.class);
        renderersClassMap.put(ListItemAttributes.class, AutocompleteDefaultRenderer.class);
        renderersClassMap.put(TabsAttributes.class, TabsDefaultRenderer.class);
        renderersClassMap.put(TabAttributes.class, TabsDefaultRenderer.class);
        renderersClassMap.put(MapAttributes.class, MapDefaultRenderer.class);
        renderersClassMap.put(MapLocationMarkerAttributes.class, MapLocationMarkerDefaultRenderer.class);
        renderersClassMap.put(MapLocationMarkersAttributes.class, MapLocationMarkersDefaultRenderer.class);
        renderersClassMap.put(LogAttributes.class, LogDefaultRenderer.class);
        renderersClassMap.put(ButtonAttributes.class, ButtonDefaultRenderer.class);
        renderersClassMap.put(InputAttributes.class, InputDefaultRenderer.class);        
        renderersClassMap.put(DisplayAttributes.class, DisplayDefaultRenderer.class);
        renderersClassMap.put(NextAttributes.class, NextDefaultRenderer.class);
        renderersClassMap.put(PreviousAttributes.class, PreviousDefaultRenderer.class);
        renderersClassMap.put(PlayAttributes.class, PlayDefaultRenderer.class);
        renderersClassMap.put(StopAttributes.class, StopDefaultRenderer.class);
        renderersClassMap.put(PauseAttributes.class, PauseDefaultRenderer.class);
        renderersClassMap.put(DatePickerAttributes.class, DatePickerDefaultRenderer.class);
        renderersClassMap.put(NextMonthAttributes.class, NextMonthDefaultRenderer.class);
        renderersClassMap.put(PreviousMonthAttributes.class, PreviousMonthDefaultRenderer.class);
        renderersClassMap.put(MonthDisplayAttributes.class, MonthDisplayDefaultRenderer.class);
        renderersClassMap.put(NextYearAttributes.class, NextYearDefaultRenderer.class);
        renderersClassMap.put(PreviousYearAttributes.class, PreviousYearDefaultRenderer.class);
        renderersClassMap.put(YearDisplayAttributes.class, YearDisplayDefaultRenderer.class);
        renderersClassMap.put(SetTodayAttributes.class, SetTodayDefaultRenderer.class);
        renderersClassMap.put(CalendarDisplayAttributes.class, CalendarDisplayDefaultRenderer.class);
        renderersClassMap.put(HandlerAttributes.class, HandlerDefaultRenderer.class);        
        renderersClassMap.put(ScriptAttributes.class, ScriptDefaultRenderer.class);        
        renderersClassMap.put(ButtonScriptHandlerAttributes.class, ButtonScriptHandlerRenderer.class);        
        renderersClassMap.put(BlockAttributes.class, BlockDefaultRenderer.class);
        renderersClassMap.put(DigitalClockAttributes.class, DigitalClockDefaultRenderer.class);
        renderersClassMap.put(ClockContentAttributes.class, ClockContentDefaultRenderer.class);
        renderersClassMap.put(StopwatchAttributes.class, StopwatchDefaultRenderer.class);
        renderersClassMap.put(TimerAttributes.class, TimerDefaultRenderer.class);
        renderersClassMap.put(BlockContentAttributes.class, BlockContentDefaultRenderer.class);
        renderersClassMap.put(SelectAttributes.class, SelectDefaultRenderer.class);        
        renderersClassMap.put(OptionAttributes.class, OptionDefaultRenderer.class);        
        renderersClassMap.put(DeckAttributes.class, DeckDefaultRenderer.class);        
        renderersClassMap.put(DeckPageAttributes.class, DeckPageDefaultRenderer.class);        
        renderersClassMap.put(TableAttributes.class, TableDefaultRenderer.class);        
        renderersClassMap.put(TableBodyAttributes.class, TableBodyDefaultRenderer.class);        

        // Internal renderers
        renderersClassMap.put(com.volantis.mcs.protocols.widgets.internal.attributes.EffectBlockAttributes.class, 
                com.volantis.mcs.protocols.widgets.internal.renderers.EffectBlockDefaultRenderer.class);
        renderersClassMap.put(com.volantis.mcs.protocols.widgets.internal.attributes.BlockContentAttributes.class, 
                com.volantis.mcs.protocols.widgets.internal.renderers.BlockContentDefaultRenderer.class);
        renderersClassMap.put(com.volantis.mcs.protocols.widgets.internal.attributes.InlineContentAttributes.class, 
                com.volantis.mcs.protocols.widgets.internal.renderers.InlineContentDefaultRenderer.class);
        renderersClassMap.put(com.volantis.mcs.protocols.widgets.internal.attributes.PCDATAContentAttributes.class, 
                com.volantis.mcs.protocols.widgets.internal.renderers.PCDATAContentDefaultRenderer.class);
        renderersClassMap.put(com.volantis.mcs.protocols.widgets.internal.attributes.BlockContainerAttributes.class, 
                com.volantis.mcs.protocols.widgets.internal.renderers.BlockContainerDefaultRenderer.class);
        renderersClassMap.put(com.volantis.mcs.protocols.widgets.internal.attributes.InlineContainerAttributes.class, 
                com.volantis.mcs.protocols.widgets.internal.renderers.InlineContainerDefaultRenderer.class);
        renderersClassMap.put(com.volantis.mcs.protocols.widgets.internal.attributes.ResponseClientAttributes.class,
                com.volantis.mcs.protocols.widgets.internal.renderers.ResponseClientDefaultRenderer.class);        
        
        renderersClassMap.put(JavaScriptStringAttributes.class, JavaScriptStringDefaultRenderer.class);
        renderersClassMap.put(JavaScriptArrayAttributes.class, JavaScriptArrayDefaultRenderer.class);
        renderersClassMap.put(JavaScriptObjectAttributes.class, JavaScriptObjectDefaultRenderer.class);
        renderersClassMap.put(ResponseClientAttributes.class, ResponseClientDefaultRenderer.class);
        
        // Response renderer classes (temporarily stored here, until Response module is implemented).
        renderersClassMap.put(ResponseDeckAttributes.class, ResponseDeckDefaultRenderer.class);
        renderersClassMap.put(ResponseDatePickerAttributes.class, ResponseDatePickerDefaultRenderer.class);
        renderersClassMap.put(ResponseTableBodyAttributes.class, ResponseTableBodyDefaultRenderer.class);
        renderersClassMap.put(ResponseErrorAttributes.class, ResponseErrorDefaultRenderer.class);
    }
   
    /**
     * Informs, whether the a Widget.startup() method is required to be invoked
     * on page load.
     */
    private boolean requireStartup = false;
    
    /**
     * Instances of renderers
     */
    private Map renderersMap = new HashMap();
    
    /**
     * Multiple validator builder instance, created on first access. 
     */
    private MultipleValidatorBuilder multipleValidatorBuilder;

    private Map widgetStacks = new HashMap();
    
    private Stack containerIdStack = new Stack();

    private Stack javaScriptContainerFactoryStack = new Stack();

    /**
     * A stack of currently rendered widget attributes. Attributes are put on
     * the stack before the WidgetDefaultRenderer.doRenderOpen() method is
     * invoke, and popped from the stack after the
     * WidgetDefaultRenderer.doRenderClose() method is invoked.
     */
    private Stack outerAttributesStack = new Stack();
    
    /**
     * A stack of currently rendered widget attributes. Attributes are put on
     * the stack after the WidgetDefaultRenderer.doRenderOpen() method is
     * invoke, and popped from the stack before the
     * WidgetDefaultRenderer.doRenderClose() method is invoked.
     */
    private Stack innerAttributesStack = new Stack();

    public WidgetDefaultModule() {
        javaScriptContainerFactoryStack.push(new DefaultJavaScriptContainerFactory());
    }
   
    /**
     * Returns a renderer for a widget. If this method is called several times
     * during a single request, the same instance of renderer is returned each time.
     *
     * @throws ProtocolException 
     */
    public WidgetRenderer getWidgetRenderer(MCSAttributes attributes) 
            throws ProtocolException {
        return (null != attributes) ? 
                getWidgetRenderer(attributes.getClass()) : null;
    }

    /**
     * Returns renderer for Carousel widget 
     */
    public WidgetRenderer getCarouselRenderer() 
            throws ProtocolException {
        return getWidgetRenderer(CarouselAttributes.class);
    }    

    /**
     * Returns renderer for Popup widget 
     */
    public WidgetRenderer getPopupRenderer() 
            throws ProtocolException {
        return getWidgetRenderer(PopupAttributes.class);
    }    

    /**
     * Returns renderer for DatePicker widget 
     */
    public WidgetRenderer getDatePickerRenderer() 
            throws ProtocolException {
        return getWidgetRenderer(DatePickerAttributes.class);
    }    

    
    /**
     * Returns renderer for TickerTape widget 
     */
    public WidgetRenderer getTickerTapeRenderer() 
            throws ProtocolException {
        return getWidgetRenderer(TickerTapeAttributes.class);
    };    

    /**
     * Returns renderer for Progress widget 
     */
    public WidgetRenderer getProgressRenderer() 
            throws ProtocolException {
        return getWidgetRenderer(ProgressAttributes.class);
    }   

    /**
     * Returns renderer for DynamicMenu widget 
     */
    public DynamicMenuWidgetRenderer getDynamicMenuRenderer() 
            throws ProtocolException {
        return (DynamicMenuWidgetRenderer)getWidgetRenderer(NavigationListAttributes.class);
    }  

    /**
     * Returns renderer for FoldingItem widget 
     */
    public WidgetRenderer getFoldingItemRenderer() 
            throws ProtocolException {
        return getWidgetRenderer(FoldingItemAttributes.class);
    }
    
    /**
     * Returns renderer for MapLocationMarkers widget 
     */
    public WidgetRenderer getMapLocationMarkersRenderer() 
            throws ProtocolException {
        return getWidgetRenderer(MapLocationMarkersAttributes.class);
    }  

    /**
     * Returns renderer for Wizard widget 
     */
    public WizardRenderer getWizardRenderer() 
            throws ProtocolException {
        return (WizardRenderer)getWidgetRenderer(WizardAttributes.class);
    }   
    
    /**
     * Returns renderer for Field Expander 
     */
    public FieldExpanderWidgetRenderer getFieldExpanderRenderer() 
            throws ProtocolException {
        return (FieldExpanderWidgetRenderer)
                getWidgetRenderer(FieldExpanderAttributes.class);
    }

    
    /**
     * Returns renderer for Multiple Validator 
     */
    public MultipleValidatorRenderer getMultipleValidatorRenderer() throws ProtocolException {
        return (MultipleValidatorRenderer)
                getWidgetRenderer(MultipleValidatorAttributes.class);
    }
    
    /**
     * Returns renderer for Tabs widget 
     */
    public TabsRenderer getTabsRenderer() 
            throws ProtocolException {
        return (TabsRenderer)
            getWidgetRenderer(TabsAttributes.class);
    }

    /**
     * Returns renderer for Carousel widget 
     */
    public MapRenderer getMapRenderer() 
            throws ProtocolException {
        return (MapRenderer)
            getWidgetRenderer(MapAttributes.class);
    }    

    /**
     * Returns the renderer for Validate 
     */   
    public WidgetRenderer getValidateRenderer() throws ProtocolException {        
        return getWidgetRenderer(ValidateAttributes.class);
    }
    
    /**
     * Returns a renderer for a widget. If this method is called several times
     * during a single request, the same instance of renderer is returned each time.
     *
     * @throws ProtocolException 
     */
    public WidgetRenderer getWidgetRenderer(Class attributesClass) 
            throws ProtocolException {
        WidgetRenderer renderer = (WidgetRenderer)renderersMap.get(attributesClass);
        if (null == renderer) {
            Class rendererClass = (Class)renderersClassMap.get(attributesClass);
            if (null != rendererClass) {
                try {
                    renderer = (WidgetRenderer)rendererClass.newInstance();
                    renderersMap.put(attributesClass, renderer);
                    // If we are unable to instantiate a renderer, we report a protocol error 
                    // as this is not something that we expect to happen in normal circumastances.
                } catch (InstantiationException e) {
                    throw new ProtocolException(e);
                } catch (IllegalAccessException e) {
                    throw new ProtocolException(e);
                }
            }
        }
        return renderer;
    }

    // Javadoc inherited
    public void renderClose(VolantisProtocol protocol) throws ProtocolException {
        // Do render only if Framework Client is supported.
        if (protocol.getProtocolConfiguration().isFrameworkClientSupported() && 
                protocol.supportsJavaScript()) {
            if (multipleValidatorBuilder != null) {
                multipleValidatorBuilder.render(protocol);
            }
        
            renderJavaScript(protocol);
        }
    }
    
    /**
     * Returns the instance of MultipleValidatorBuilder used
     * to build multiple validator widgets.
     * 
     * @return The instance of MultipleValidatorBuilder.
     */
    public MultipleValidatorBuilder getMultipleValidatorBuilder() {
        if (multipleValidatorBuilder == null) {
            multipleValidatorBuilder = new MultipleValidatorBuilder(this);
        }
        
        return multipleValidatorBuilder;
    }

    /**
     * Informs, that the page requires a Widget.startup() method to be called
     * after page is loaded.
     */
    public void requireStartup() {
        this.requireStartup = true;
    }

    // Javadoc inherited.
    public void processBodyElementForStartup(VolantisProtocol protocol, Element element) {
        // Process the 'onload' attribute only, if the Widget.startup() is required.
        if (requireStartup) {
            // Retrieve existing 'onload' attribute.
            String currentScript = element.getAttributeValue("onload");

            // Prepare the startup script.
            String widgetScript = "Widget.startup()";
            
            // Concatenate existing value with the startup script.
            String script;
            
            if (currentScript == null) {
                script = widgetScript;
            } else {
                script = currentScript + ";" + widgetScript;
            }
            
            // Set concatenated attribute value
            element.setAttribute("onload", script);
        }    
    }

    // return renderer for response:autocomplete in order to generate unique id for each LI elements and javascript code
    public AutocompleteRenderer getAutocompleteRenderer() 
        throws ProtocolException {
        return (AutocompleteRenderer)getWidgetRenderer(ListItemAttributes.class);
    }
    
    /**
     * Returns the string renderer.
     * 
     * @return the string renderer.
     */
    public JavaScriptStringDefaultRenderer getJavaScriptStringDefaultRenderer() throws ProtocolException {
        return (JavaScriptStringDefaultRenderer) getWidgetRenderer(JavaScriptStringAttributes.class);
    }
    
    /**
     * Pushes specified widget to be a handler for specified action.
     * 
     * @param name The name of the action.
     * @param widgetId The ID of the widget.
     */
    public void pushWidgetId(String widgetId, ActionName name) {
        pushWidgetIdFor(name, widgetId);
    }
        
    /**
     * Pops top widget for specified action.
     * 
     * @param name The name of the action.
     */
    public void popWidgetId(ActionName name) {
        popWidgetIdFor(name);
    }
        
    /**
     * Pushes specified widget to be a handler for specified property.
     * 
     * @param name The name of the property.
     * @param widgetId The ID of the widget.
     */
    public void pushWidgetId(String widgetId, PropertyName name) {
        pushWidgetIdFor(name, widgetId);
    }
        
    /**
     * Pops top widget for specified property.
     * 
     * @param name The name of the property.
     * @throws EmptyStackException if the stack is empty.
     */
    public void popWidgetId(PropertyName name) {
        popWidgetIdFor(name);
    }
    
    /**
     * Returns the top-most widget ID from the stack associated with specifed
     * ActionName, or null if not found.
     * 
     * @param name The action name
     * @return The widget ID.
     */
    public String getCurrentWidgetId(ActionName name) {
        return peekWidgetIdFor(name);
    }
    
    /**
     * Returns the top-most widget ID from the stack associated with specifed
     * PropertyName, or null if not found.
     * 
     * @param name The property name
     * @return The widget ID.
     */
    public String getCurrentWidgetId(PropertyName name) {
        return peekWidgetIdFor(name);
    }
    
    /**
     * Returns stack of widget IDs associated with specified object. If not
     * found and create is true, then a new empty stack is created and
     * associated.
     * 
     * @param object The object. 
     * @param create The creation flag.
     * @return A stack associated with specified object, or null.
     */
    private Stack getWidgetIdStackFor(Object object, boolean create) {
        Stack stack = null;
        
        if (widgetStacks == null) {
            if (create) {
                widgetStacks = new HashMap();
            }
        }

        if (widgetStacks != null) {
            stack = (Stack) widgetStacks.get(object);
            
            if (stack == null) {
                if (create) {
                    stack = new Stack();
                    
                    widgetStacks.put(object, stack);
                }
            }
        }
    
        return stack;
    }
    
    /**
     * Pushes widget ID on the stack associated with specified object.
     * 
     * @param object The object. 
     * @param id The widget ID.
     */
    private void pushWidgetIdFor(Object object, String id) {
        getWidgetIdStackFor(object, true).push(id);
    }
    
    /**
     * Pops widget ID from the stack associated with specified object.
     * 
     * @param object The object
     * @throws EmptyStackException if stack is empty or no stack is associated.
     */
    private void popWidgetIdFor(Object object) {
        Stack stack = getWidgetIdStackFor(object, false);
        
        if (stack == null) {
            throw new EmptyStackException();
        } else {
            stack.pop();
        }
    }
    
    /**
     * Returns widget ID from the top of the stack associated with specified object.
     * 
     * @param object The object.
     * @return The widget ID.
     * @throws EmptyStackException if stack is empty or no stack is associated.
     */
    private String peekWidgetIdFor(Object object) {
        String widgetId = null;

        Stack stack = getWidgetIdStackFor(object, false);
        
        if (stack != null && !stack.empty()) {
            widgetId = (String) stack.peek();
        }
        
        return widgetId;
    }
    
    private void pushJavaScriptContainerFactory() {
        DefaultJavaScriptContainerFactory factory = new DefaultJavaScriptContainerFactory();
        
        javaScriptContainerFactoryStack.push(factory);
    }
    
    private void popJavaScriptContainerFactory() {
        javaScriptContainerFactoryStack.pop();
    }
    
    private DefaultJavaScriptContainerFactory getCurrentJavaScriptContainerFactory() {
        return (DefaultJavaScriptContainerFactory) javaScriptContainerFactoryStack.peek();
    }
    
    /**
     * Creates and returns a new instance of JavaScript container, to write
     * a fragment of JavaScript content to.
     * 
     * @param protocol The protocol used.
     * @return Created JavaScript container.
     */
    public JavaScriptContainer createJavaScriptContainer(VolantisProtocol protocol) {
        return getCurrentJavaScriptContainerFactory().createJavaScriptContainer();
    }
    
    /**
     * Renders the script element including the code from all created JavaScript
     * containers in proper order.
     * 
     * @param protocol The protocol used.
     * @throws ProtocolException
     */
    private void renderJavaScript(VolantisProtocol protocol) throws ProtocolException {
        /** @todo - this is temporary solution removes empty containers from response
             it caused serious bug when json is only response content */
        DefaultJavaScriptContainerFactory factory = 
            getCurrentJavaScriptContainerFactory();
        
        // Write script content into string.
        StringWriter scriptWriter = new StringWriter();
            
        try {
            factory.writeOrderedJavaScript(scriptWriter);
        } catch (IOException e) {
            throw new ProtocolException("Error rendering JavaScript.", e);
        } catch (OrderException e) {
            throw new ProtocolException("Cyclic dependency found between widgets.");
        }
        
        String script = scriptWriter.toString();
        
        // Now, if script is not empty, render it into the page.
        if (script.length() > 0) {
            // Open <script> element
            com.volantis.mcs.protocols.ScriptAttributes scriptAttributes =
                new com.volantis.mcs.protocols.ScriptAttributes();
            
            scriptAttributes.setType("text/javascript");
            
            protocol.writeOpenScript(scriptAttributes);
    
            try {
                protocol.getContentWriter().write(script);
            } catch (IOException e) {
                throw new ProtocolException("Error rendering JavaScript.", e);
            }
    
            // Close script element.
            protocol.writeCloseScript(scriptAttributes);
        }
    }
    
    /**
     * Called, when container widget is being opened.
     * 
     * @param id The ID of the container widget.
     */
    public void openingContainer(String id) {
        containerIdStack.push(id);
    }
    
    /**
     * Called, when the content widget is being opened.
     */
    public void openingContent() {
        if(getParentContainerId() == null) {
            pushJavaScriptContainerFactory();
        }
        
        containerIdStack.push(null);
    }
    
    /**
     * Called, when the Container widget is about to be closed.
     * 
     * @return The ID of the container.
     */
    public String closingContainer() {
        return (String) containerIdStack.pop();
    }
    
    /**
     * Called, when the Content widget is about to be closed.
     * 
     * @param protocol The protocol to render.
     * @throws ProtocolException
     */
    public void closingContent(VolantisProtocol protocol) throws ProtocolException {        
        containerIdStack.pop();
        
        if(getParentContainerId() == null) {
            renderJavaScript(protocol);
        
            popJavaScriptContainerFactory();
    }
    }
    
    public String getParentContainerId() {
        return containerIdStack.isEmpty() ? null : (String) containerIdStack.peek();
    }
    
    /**
     * Returns the stack of widget attributes. Attributes are pushed on the
     * stack in "outer" fashion: before doRenderOpen() and after doRenderClose()
     * is called.
     * 
     * @return Returns the stack of widget attributes.
     */
    public Stack getOuterAttributesStack() {
        return outerAttributesStack;
    }

    /**
     * Returns the stack of widget attributes. Attributes are pushed on the
     * stack in "inner" fashion: after doRenderOpen() and before doRenderClose()
     * is called.
     * 
     * @return Returns the stack of widget attributes.
     */
    public Stack getInnerAttributesStack() {
        return innerAttributesStack;
    }
}
 
