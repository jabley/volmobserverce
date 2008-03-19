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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.charset.http;

import com.volantis.mcs.devices.Device;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.servlet.AcceptCharsetSelector;
import com.volantis.charset.EncodingManager;

/**
 * Defualt implementation of CharsetHttpSelector interface. This implementation
 * simply delegates requests to an internal AcceptCharsetSelector.
 */
public class DefaultCharsetHttpSelector implements CharsetHttpSelector{

    /**
     * Encoding Manager to use for this CharsetHttpSelector
     */
    private static EncodingManager encManager;

    static {
        // slow so lets just do it once.
        encManager = new EncodingManager();
    }

    /**
     * The selector to delegate to.
     */
    private AcceptCharsetSelector selector;

    /**
     * Default constructor. This is package private to discourage direct
     * usage. An instance should be obtained through the CharsetHttpFactory.
     */
    DefaultCharsetHttpSelector() {
        selector = new AcceptCharsetSelector(encManager);
    }

    // javadoc inherited
    public String getOutputCharset(HttpHeaders headers, Device device) {
        return selector.selectCharset(headers, ((DefaultDevice) device));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 04-Aug-04	5017/3	matthew	VBM:2004073003 clean up javadoc

 02-Aug-04	5017/1	matthew	VBM:2004073003 CharsetHttpFactory and Selector

 ===========================================================================
*/
