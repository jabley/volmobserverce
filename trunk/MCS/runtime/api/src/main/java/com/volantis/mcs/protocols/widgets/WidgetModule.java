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

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.renderers.AutocompleteRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DynamicMenuWidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.FieldExpanderWidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MapRenderer;
import com.volantis.mcs.protocols.widgets.renderers.MultipleValidatorRenderer;
import com.volantis.mcs.protocols.widgets.renderers.TabsRenderer;
import com.volantis.mcs.protocols.widgets.renderers.WidgetRenderer;
import com.volantis.mcs.protocols.widgets.renderers.WizardRenderer;

/**
 * Interface for a protocol module responsible for rendering widgets
 * 
 * @mock.generate
 */
public interface WidgetModule {
    /**
     * Performs rendering just before the page is closed.
     * It can be used to render content, which needs to be deferred
     * until the whole content of the page is rendered.
     * 
     * @param protocol The protocol used for rendering.
     */
    public void renderClose(VolantisProtocol protocol) throws ProtocolException;

    /**
     * Returns a renderer for a widget based on type of attributes
     */
    public WidgetRenderer getWidgetRenderer(MCSAttributes attributes) throws ProtocolException;
    
    /**
     * Returns renderer for Carousel widget 
     */
    public WidgetRenderer getCarouselRenderer() throws ProtocolException;    

    /**
     * Returns renderer for Popup widget 
     */
    public WidgetRenderer getPopupRenderer() throws ProtocolException;    

    /**
     * Returns renderer for TickerTape widget 
     */
    public WidgetRenderer getTickerTapeRenderer() throws ProtocolException;    

    /**
     * Returns renderer for Progress widget 
     */
    public WidgetRenderer getProgressRenderer() throws ProtocolException;    

    /**
     * Returns renderer for DynamicMenu widget 
     */
    public DynamicMenuWidgetRenderer getDynamicMenuRenderer() throws ProtocolException;    

    /**
     * Returns renderer for FoldingItem widget 
     */
    public WidgetRenderer getFoldingItemRenderer() throws ProtocolException;    

    /**
     * Returns renderer for Wizard widget 
     */
    public WizardRenderer getWizardRenderer() throws ProtocolException;    

    /**
     * Returns the renderer for FieldExpander widget 
     */
    public FieldExpanderWidgetRenderer getFieldExpanderRenderer() throws ProtocolException;
    
    /**
     * Returns the renderer for MultipleValidator widget 
     */
    public MultipleValidatorRenderer getMultipleValidatorRenderer() throws ProtocolException;

    /**
     * Returns the renderer for Tabs widget 
     */
    public TabsRenderer getTabsRenderer() throws ProtocolException;    

    /**
     * Returns the renderer for Map widget 
     */
    public MapRenderer getMapRenderer() throws ProtocolException;    

    /**
     * Returns the renderer for ValidateElement 
     */
    public WidgetRenderer getValidateRenderer() throws ProtocolException;
    
    /**
     * Returns the renderer for AutocompleteElement 
     */
    public AutocompleteRenderer getAutocompleteRenderer() throws ProtocolException;

    
}
