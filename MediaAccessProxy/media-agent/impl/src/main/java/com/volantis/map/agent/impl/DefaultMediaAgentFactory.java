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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.agent.impl;

import com.volantis.map.agent.MediaAgentFactory;
import com.volantis.map.agent.MediaAgent;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStore;
import com.volantis.synergetics.descriptorstore.ResourceDescriptorStoreFactory;

/**
 * Default implementation of the MediaAgentFactory
 */
public class DefaultMediaAgentFactory extends MediaAgentFactory {

    private static final ResourceDescriptorStore DESCRIPTOR_STORE =
            ResourceDescriptorStoreFactory.getDefaultInstance().
            getResourceDescriptorStore();

    private final String urlPrefix;

    public DefaultMediaAgentFactory(final String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    // javadoc inherited
    public MediaAgent getMediaAgent() {
        return new DefaultMediaAgent(DESCRIPTOR_STORE, urlPrefix);
    }
}
