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
package com.volantis.mcs.protocols.html.htmlversion3_2;

import com.volantis.mcs.protocols.html.XHTMLBasicTransMapper;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * This class extends the XHTMLBasicTransMapper in order to provide a different
 * set of valid div attributes.
 * <p>
 * Note this was previously the HTMLPalmWCATransMapper before it was decided
 * that HTML3.2 did all the was necessary for HTMLPalmWCA transformation.
 */
public class HTML3_2TransMapper extends XHTMLBasicTransMapper {

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param configuration {@link TransformationConfiguration} which is used
     * when remapping elements. May be null.
     */
    public HTML3_2TransMapper(TransformationConfiguration configuration) {
        super(configuration);
    }

    // javadoc inherited
    protected String[] getValidDivAttributes() {
        String validAttributes[] = {"align"};
        return validAttributes;
    }
}
