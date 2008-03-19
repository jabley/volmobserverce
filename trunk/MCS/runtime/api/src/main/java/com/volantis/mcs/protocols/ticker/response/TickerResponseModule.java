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
 * The extension to the protocol, handling the markup
 * from the Ticker Response namespace.
 * 
 * @mock.generate
 */
public interface TickerResponseModule {
    /**
     * Opens the FeedPoller element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openFeedPoller(FeedPollerAttributes attributes) throws ProtocolException;

    /**
     * Closes the FeedPoller element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeFeedPoller(FeedPollerAttributes attributes) throws ProtocolException;
    
    /**
     * Opens the AddItem element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openAddItem(AddItemAttributes attributes) throws ProtocolException;

    /**
     * Closes the AddItem element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeAddItem(AddItemAttributes attributes) throws ProtocolException;

    /**
     * Opens the Title element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openTitle(TitleAttributes attributes) throws ProtocolException;

    /**
     * Closes the Title element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeTitle(TitleAttributes attributes) throws ProtocolException;

    /**
     * Opens the Icon element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openIcon(IconAttributes attributes) throws ProtocolException;

    /**
     * Closes the Icon element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeIcon(IconAttributes attributes) throws ProtocolException;
    
    /**
     * Opens the Description element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openDescription(DescriptionAttributes attributes) throws ProtocolException;

    /**
     * Closes the Description element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeDescription(DescriptionAttributes attributes) throws ProtocolException;

    /**
     * Opens the PlainDescription element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openPlainDescription(PlainDescriptionAttributes attributes) throws ProtocolException;

    /**
     * Closes the PlainDescription element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closePlainDescription(PlainDescriptionAttributes attributes) throws ProtocolException;
    /**
     * Opens the RemoveItem element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openRemoveItem(RemoveItemAttributes attributes) throws ProtocolException;

    /**
     * Closes the RemoveItem element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeRemoveItem(RemoveItemAttributes attributes) throws ProtocolException;

    /**
     * Opens the SetSkipTimes element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openSetSkipTimes(SetSkipTimesAttributes attributes) throws ProtocolException;

    /**
     * Closes the RemoveItem element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeSetSkipTimes(SetSkipTimesAttributes attributes) throws ProtocolException;

    /**
     * Opens the SkipTime element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openSkipTime(SkipTimeAttributes attributes) throws ProtocolException;

    /**
     * Closes the SkipTime element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeSkipTime(SkipTimeAttributes attributes) throws ProtocolException;

    /**
     * Opens the SetURL element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openSetURL(SetURLAttributes attributes) throws ProtocolException;

    /**
     * Closes the SetURL element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeSetURL(SetURLAttributes attributes) throws ProtocolException;

    /**
     * Opens the SetPollingInterval element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void openSetPollingInterval(SetPollingIntervalAttributes attributes) throws ProtocolException;

    /**
     * Closes the SetPollingInterval element.
     * 
     * @param attributes The attributes
     * @throws ProtocolException
     */
    void closeSetPollingInterval(SetPollingIntervalAttributes attributes) throws ProtocolException;
}
