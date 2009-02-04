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
 * (c) Copyright Volantis Systems Ltd. 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.xhtml2;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.common.param.Parameters;
import com.volantis.mcs.protocols.MAPAttributes;
import com.volantis.mcs.protocols.MAPResponseCallback;
import com.volantis.mcs.protocols.ObjectAttribute;

/**
 */
public class MAPResponseCallbackXDIME2 extends MAPResponseCallback {

    public MAPResponseCallbackXDIME2(MAPAttributes mapAttribute, MAPResponseCallback.URLRewriter urlRewriter) {
        super(mapAttribute, urlRewriter);
    }

    // set appropriate attributes depending on XDIME1 or XDIME2
    // implemented in child class
    protected void setSpecificAttributes(Parameters params) throws Exception {

        ObjectAttribute objectAttribute = (ObjectAttribute) mapAttribute;
        // Rewrite source URL from MediaAgent response, passing it through
        // additional page rewriter.
        objectAttribute.setSrc(urlRewriter.rewrite(
            params.getParameterValue(
                MediaAgent.OUTPUT_URL_PARAMETER_NAME)));

        // Clear the srcType attribute, since the actual MIME type will be
        // known at the moment of the transcoding.
        objectAttribute.setSrcType(null);
    }
}
