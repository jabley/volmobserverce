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
package com.volantis.mcs.protocols.builder;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.protocols.VolantisProtocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A protocol builder which maintains a lookup from protocol name to
 * protocol factory.
 * <p>
 * This is suitable for "normal" runtime use to create protocols from a
 * device's protocol name.
 */
public final class NamedProtocolBuilder {

    /**
     * A map of name (String) to {@link ProtocolFactory}.
     */
    private final Map nameFactoryMap = new HashMap();

    /**
     * The underlying builder which creates protocols using
     * {@link ProtocolFactory}s.
     */
    private final ProtocolBuilder protocolBuilder = new ProtocolBuilder();

    /**
     * Initialise.
     */
    public NamedProtocolBuilder() {
    }

    /**
     * Register the supplied protocol factory against the supplied protocol
     * name.
     *
     * @param protocolName the name of the protocol.
     * @param protocolFactory the factory to be used to create the named
     * protocol.
     */
    public void register(String protocolName, ProtocolFactory protocolFactory) {

        nameFactoryMap.put(protocolName, protocolFactory);
    }

    /**
     * Build a configured, ready to use protocol using the protocol name
     * and device supplied.
     *
     * @param protocolName the name of the protocol to create.
     * @param device used to initialise the protocol configuration with device
     *      specific data.
     * @return the configured, ready to use protocol.
     */
    public VolantisProtocol build(String protocolName, InternalDevice device) {

        ProtocolFactory protocolFactory = (ProtocolFactory)
                nameFactoryMap.get(protocolName);
        if (protocolFactory == null) {
            throw new IllegalStateException("No protocol factory registered " +
                    "for protocol name: " + protocolName);
        }

        return protocolBuilder.build(protocolFactory, device);

    }

    /**
     * Return a collection of the names of all the protocols that have been
     * registered.
     *
     * @return a collection of the names of all the protocols that have been
     *      registered.
     */
    public Collection getProtocolNames() {
        
        return nameFactoryMap.keySet();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/2	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 ===========================================================================
*/
