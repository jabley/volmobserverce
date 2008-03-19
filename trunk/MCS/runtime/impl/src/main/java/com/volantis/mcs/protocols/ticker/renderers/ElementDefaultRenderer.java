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

package com.volantis.mcs.protocols.ticker.renderers;

import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.TickerDefaultModule;
import com.volantis.mcs.protocols.widgets.renderers.WidgetDefaultRenderer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Base class for ticker renderer suitable for HTML-based protocols.
 *
 * This implementation of renderer is stateful and is not thread-safe. 
 * It's lifecycle is supposed to match lifecycle of a single request.
 * 
 * Implements common method - open/close Div element, open/close Script element
 * 
 * This implementation extends WidgetDefaultRenderer, to avoid code duplication.
 * In the future, the generic code from WidgetDefaultRenderer should be moved
 * to a base super-class, and both Widget and Ticker default renderers should
 * extend that one.
 */
public abstract class ElementDefaultRenderer extends WidgetDefaultRenderer implements ElementRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ElementDefaultRenderer.class);
    
    /**
     * Returns owning ticker module (the one, which this renderer was created by).
     *  
     * @param protocol The protocol used.
     * @return The owning ticker module.
     */
    protected TickerDefaultModule getTickerDefaultModule(VolantisProtocol protocol) {
        return (TickerDefaultModule) protocol.getTickerModule();
    }
    
    /**
     * Suffix used to create ID for FeedPoller element.
     */
    public static final String FEED_POLLER_ID_SUFFIX = "TICKER_FEED_POLLER";
    
    protected void addUsedFeedPollerId(VolantisProtocol protocol) {
        addUsedWidgetId(protocol.getMarinerPageContext().generateFCID(FEED_POLLER_ID_SUFFIX));
    }
}
