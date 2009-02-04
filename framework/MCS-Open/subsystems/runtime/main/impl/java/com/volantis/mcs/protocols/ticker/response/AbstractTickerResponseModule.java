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
package com.volantis.mcs.protocols.ticker.response;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ticker.response.attributes.AddItemAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.DescriptionAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.FeedPollerAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.IconAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.PlainDescriptionAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.RemoveItemAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetPollingIntervalAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetSkipTimesAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SetURLAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.SkipTimeAttributes;
import com.volantis.mcs.protocols.ticker.response.attributes.TitleAttributes;

/**
 * The template class, which may be used as a base class for implementing
 * the TickerResponseModule interface. 
 * All the open/close methods do nothing.
 */
public class AbstractTickerResponseModule implements TickerResponseModule {

    // Javadoc inherited
    public void openFeedPoller(FeedPollerAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeFeedPoller(FeedPollerAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openAddItem(AddItemAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeAddItem(AddItemAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openTitle(TitleAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeTitle(TitleAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openIcon(IconAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeIcon(IconAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openDescription(DescriptionAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeDescription(DescriptionAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openPlainDescription(PlainDescriptionAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closePlainDescription(PlainDescriptionAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openRemoveItem(RemoveItemAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeRemoveItem(RemoveItemAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openSetSkipTimes(SetSkipTimesAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeSetSkipTimes(SetSkipTimesAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openSkipTime(SkipTimeAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeSkipTime(SkipTimeAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openSetURL(SetURLAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeSetURL(SetURLAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void openSetPollingInterval(SetPollingIntervalAttributes attributes) throws ProtocolException {
        // no-op
    }

    // Javadoc inherited
    public void closeSetPollingInterval(SetPollingIntervalAttributes attributes) throws ProtocolException {
        // no-op
    }
}
