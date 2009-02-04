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
package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;

/**
 * Define mapping between Widget elements and their factories.
 */
public class TickerElementHandler extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        builder.addMapping(TickerElements.FEED_POLLER, new ElementFactory() {
             public XDIMEElement createElement(XDIMEContextInternal context) {
                 return new FeedPollerElement(context);
             }
        });

        builder.addMapping(TickerElements.FEED, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new FeedElement(context);
            }
        });

        builder.addMapping(TickerElements.UPDATE_STATUS, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new UpdateStatusElement(context);
            }
        });

        builder.addMapping(TickerElements.ITEMS_COUNT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemsCountElement(context);
            }
        });

        builder.addMapping(TickerElements.CHANNELS_COUNT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ChannelsCountElement(context);
            }
        });

        builder.addMapping(TickerElements.ITEM_DISPLAY, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemDisplayElement(context);
            }
        });
        builder.addMapping(TickerElements.ITEM_TITLE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemTitleElement(context);
            }
        });

        builder.addMapping(TickerElements.ITEM_ICON, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemIconElement(context);
            }
        });

        builder.addMapping(TickerElements.ITEM_DESCRIPTION, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemDescriptionElement(context);
            }
        });

        builder.addMapping(TickerElements.ITEM_PLAIN_DESCRIPTION, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemPlainDescriptionElement(context);
            }
        });

        builder.addMapping(TickerElements.ITEM_CHANNEL, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new ItemChannelElement(context);
            }
        });
    }
}
