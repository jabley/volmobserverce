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

package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.xml.pipeline.sax.drivers.web.DerivableHTTPMessageEntity;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestEntityFactory;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestHeader;

/**
 * Rule for webd:header element.
 */
public class HeaderRule
        extends HttpMessageEntityRule {

    /**
     * Initialise.
     */
    public HeaderRule() {
        this(WebRequestEntityFactory.getDefaultInstance());
    }

    /**
     * Initialise.
     *
     * @param factory The factory for creating headers.
     */
    public HeaderRule(WebRequestEntityFactory factory) {
        super(factory, COMMON_SETTERS, WebRequestHeader.class);
    }

    // Javadoc inherited.
    protected DerivableHTTPMessageEntity createEntity() {
        return factory.createHeader();
    }
}
