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
package com.volantis.map.common.impl.param;

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.MutableParameters;

import java.util.Properties;
import java.util.Iterator;

/**
 * Simple implementation of the Parameters interface
 */
public class DefaultMutableParameters implements MutableParameters {
    private final Properties params;

    public DefaultMutableParameters() {
        params = new Properties();
    }


    // javadoc inherited
    public void setParameterValue(final String name, final String value) {
        params.put(name, value);
    }

    // javadoc inherited
    public void removeParameterValue(String name) {
        params.remove(name);
    }

    // javadoc inherited
    public String getParameterValue(final String name)
            throws MissingParameterException {
        checkParameter(name);
        return params.getProperty(name);
    }

    private void checkParameter(String name) throws MissingParameterException {
        if (!params.containsKey(name)) {
            throw new MissingParameterException("Missing parameter: " + name);
        }
    }

    // javadoc inherited
    public int getInteger(final String name) throws MissingParameterException {
        checkParameter(name);
        return Integer.parseInt(params.getProperty(name));
    }

    // javadoc inherited
    public float getFloat(final String name) throws MissingParameterException {
        checkParameter(name);
        return Float.parseFloat(params.getProperty(name));
    }

    // javadoc inherited
    public boolean getBoolean(final String name) throws MissingParameterException {
        checkParameter(name);
        return Boolean.valueOf(params.getProperty(name));
    }

    // javadoc inherited
    public boolean containsName(final String name) {
        return params.containsKey(name);
    }

    // javadoc inherited
    public Iterator getParameterNames() {
        return params.keySet().iterator();
    }
}
