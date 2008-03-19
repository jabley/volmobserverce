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

package com.volantis.mps.bms.impl.ser;

/**
 * Default implementation of the factory. This factory exists to centralise the
 * creation of the implementation. We could directly instantiate the
 * JiBXModelParser, since it is impl code and only used by other impl code, but
 * then more client classes would know about the implementation (via an
 * import), and would need updating if we ever replace JiBX with something
 * else. This way, we only update one place (this factory) and client classes
 * are unaware of the change.
 */
public class DefaultModelParserFactory extends ModelParserFactory {

    // javadoc inherited
    public ModelParser createModelParser() {
        return new JiBXModelParser();
    }
}
