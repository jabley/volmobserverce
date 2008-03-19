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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.sgml.SGMLDTDBuilder;

/**
 * Specialization of the configuration for the {@link HTMLVersion3_AG}
 * protocol.
 */
public class HTMLVersion3_AGConfiguration
        extends HTMLVersion3_2Configuration {

    /**
     * Override to ensure that any href attribute is treated as non replaceable
     * and so does not have any special characters replaced with XML specific
     * ones.
     */
    protected void populateSGMLDTDBuilder(
            SGMLDTDBuilder builder, InternalDevice device) {
        super.populateSGMLDTDBuilder(builder, device);

        builder.addNonReplaceableAttribute("href");
    }
}
