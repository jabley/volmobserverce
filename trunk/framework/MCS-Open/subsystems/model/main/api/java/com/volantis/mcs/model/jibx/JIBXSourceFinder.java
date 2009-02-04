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
package com.volantis.mcs.model.jibx;

import org.jibx.runtime.ITrackSource;

public class JIBXSourceFinder {

    private static JIBXSourceFinder instance = new JIBXSourceFinder();

    public static JIBXSourceFinder getInstance() {
        return instance;
    }

    private ITrackSource unknownSource = new ITrackSource() {
        public String jibx_getDocumentName() {
            return "<unknown location>";
        }

        public int jibx_getLineNumber() {
            return -1;
        }

        public int jibx_getColumnNumber() {
            return -1;
        }
    };

    private JIBXSourceFinder() {
    }

    public ITrackSource getJIBXSource(Object o) {
        ITrackSource jibxSource;
        if (o instanceof ITrackSource) {
            jibxSource = (ITrackSource) o;
        } else {
            jibxSource = unknownSource;
        }
        return jibxSource;
    }

}
