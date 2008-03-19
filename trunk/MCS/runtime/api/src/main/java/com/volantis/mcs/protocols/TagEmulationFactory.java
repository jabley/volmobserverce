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
 * $Header: /src/voyager/com/volantis/mcs/protocols/TagEmulationFactory.java,v 1.2 2002/03/28 19:14:50 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Mar-02    Allan           VBM:2002022007 - Created. A factory to 
 *                              provide emulated tags.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;

import java.util.HashMap;

/**
 * A factory to provide emulated tags. This factoryMock should have its release
 * method called at the end of any page that creates the factory and maintains
 * a reference to it.
 */
public final class TagEmulationFactory
        implements DevicePolicyConstants {

    /**
     * A cache of previously created emulation tags.
     */
    private final HashMap emulatedTags = new HashMap(10);

    /**
     * The InternalDevice in use.
     */
    private final InternalDevice device;

    /**
     * Construct a new TagEmulationFactory for the provided MarinerPageContext
     *
     * @param device the InternalDevice to use
     */
    public TagEmulationFactory(InternalDevice device) {
        this.device = device;
    }

    /**
     * Get an emulated emphasis tag appropriate for the current device and
     * protocol. The key the for tag is one of the EMULATE contants defined
     * in DevicePolicyConstants.
     *
     * @return the EmulateEmphasisTag specified by the tagKey or null if
     *         no such emphasis emulation could be created.
     */
    public EmulateEmphasisTag getTagEmphasisEmulation(String tagKey) {
        EmulateEmphasisTag tag = (EmulateEmphasisTag) emulatedTags.get(tagKey);
        if (tag != null) {
            return tag;
        }

        if (emulatedTags.containsKey(tagKey)) {
            // We have tried to create this emulate tag already and failed
            // so return null since we will just fail again anyway...
            return null;
        }

        // We need to create a new EmulateEmphasisTag
        tag = EmulateEmphasisTag.create(device, tagKey);

        emulatedTags.put(tagKey, tag);

        return tag;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
