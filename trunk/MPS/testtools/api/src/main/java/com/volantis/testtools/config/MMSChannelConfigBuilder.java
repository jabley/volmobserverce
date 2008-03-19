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
 * $Header: /src/mps/com/volantis/testtools/config/MMSChannelConfigBuilder.java,v 1.2 2003/03/20 10:15:36 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Feb-03    Mat             VBM:2003022002 - Created. A class that can
 *                              build config information for MMS channels
 * 19-Mar-03    Geoff           VBM:2003032001 - Refactored to use external
 *                              ConfigValues.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.config;

import java.lang.StringBuffer;

/**
 *
 * A config builder for MMS channels.
 *
 */
public class MMSChannelConfigBuilder extends ChannelConfigBuilder {

    private ConfigValueChannelMms value;
    
    /** Creates a new instance of MMSChannelConfigBuilder */
    public MMSChannelConfigBuilder(ConfigValueChannelMms value) {
        super(value);
        this.value = value;
    }

    // javadoc inherited.
    protected void renderChannelAttributes(StringBuffer config) {
        addArgumentElement(config,"url",value.url);
        addArgumentElement(config,"default-country-code",
            value.defaultCountryCode);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 ===========================================================================
*/
