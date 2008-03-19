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

package com.volantis.mcs.protocols.ticker.attributes;

/**
 * ItemsCount element attributes.
 */
public class ItemsCountAttributes extends TickerAttributes {
    private String channel;
    private String read;
    private String followed;
    /**
     * @return Returns the channel.
     */
    public String getChannel() {
        return channel;
    }
    /**
     * @param channel The channel to set.
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }
    /**
     * @return Returns the followed.
     */
    public String getFollowed() {
        return followed;
    }
    /**
     * @param followed The followed to set.
     */
    public void setFollowed(String followed) {
        this.followed = followed;
    }
    /**
     * @return Returns the read.
     */
    public String getRead() {
        return read;
    }
    /**
     * @param read The read to set.
     */
    public void setRead(String read) {
        this.read = read;
    }
}
