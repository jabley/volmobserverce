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
package com.volantis.map.common.param;

/**
 * Class to store mutable parameters. 
 */
public interface MutableParameters extends Parameters {

    /**
     * Set the specified parameter name to have the given value. The parameter
     * will be created if it does not already exist
     *
     * @param name  the name
     * @param value the value
     */
    public void setParameterValue(String name, String value);

    /**
     * Remove the parameter with the specified name.
     *
     * @param name the name of the name value pair to remove
     */
    public void removeParameterValue(String name);
}
