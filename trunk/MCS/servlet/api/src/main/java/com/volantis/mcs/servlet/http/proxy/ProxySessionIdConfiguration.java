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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet.http.proxy;

import com.volantis.xml.pipeline.sax.convert.ConverterConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterTuple;

/**
 * A ConverterConfiguration which stores the sessionKey of the session.   
 *
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public class ProxySessionIdConfiguration
        extends ConverterConfiguration {

    /**
     * Default constructor simply calls parent constructor.
     *
     * @param tuples the tuples.
     */
    public ProxySessionIdConfiguration(ConverterTuple[] tuples, String sessionKey) {
        super(tuples);
        this.sessionKey = sessionKey;
    }

    private String sessionKey = null;


    /**
     * @return Returns the sessionKey.
     */
    public String getSessionKey() {
        return sessionKey;
    }
}
