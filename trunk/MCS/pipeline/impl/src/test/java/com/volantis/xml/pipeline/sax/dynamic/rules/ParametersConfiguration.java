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
package com.volantis.xml.pipeline.sax.dynamic.rules;

import com.volantis.xml.pipeline.sax.config.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

/**
 * This implementation of {@link Configuration} interface stores parameter name
 * -> parameter value pairs. Both the name and the value must be String.
 *
 * <p>Only one value can be store for a name, and always the last value set
 * will be kept.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class ParametersConfiguration implements Configuration {

    private final Map parameters;

    public ParametersConfiguration() {
        parameters = new HashMap();
    }

    /**
     * Sets the given parameter value for the given parameter name.
     *
     * @param name the name of the parameter
     * @param value the value of the paramter
     * @return the previous value associated for the specified parameter name
     */
    public String setParameter(final String name, final String value) {
        return (String) parameters.put(name, value);
    }

    /**
     * Returns the value associated for the given parameter name.
     *
     * <p>Null is returned if no value has been associated to the parameter.</p>
     *
     * @param name the name to look up
     * @return the value or null
     */
    public String getParameterValue(final String name) {
        return (String) parameters.get(name);
    }

    /**
     * Returns an unmodifiable iterator over the parameter names.
     *
     * @return an iterator to walk through the parameter names
     */
    public Iterator getParameterNames() {
        return Collections.unmodifiableSet(parameters.keySet()).iterator();
    }
}
