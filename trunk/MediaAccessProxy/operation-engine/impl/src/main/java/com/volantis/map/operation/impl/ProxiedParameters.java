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
package com.volantis.map.operation.impl;

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.operation.ObjectParameters;
import com.volantis.synergetics.descriptorstore.Parameters;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * An immutable Parameters object
 */
public class ProxiedParameters implements ObjectParameters {

    private Map objects = new HashMap();

    // javadoc inherited
    public ProxiedParameters(Parameters parameters) {
        Iterator it = parameters.iterator();
        while (it.hasNext()) {
            Parameters.Entry entry = (Parameters.Entry) it.next();
            objects.put(entry.getName(), entry.getValue());
        }
    }

    /**
     * Default constructor 
     */
    public ProxiedParameters() {

    }

    // javadoc inherited
    public boolean containsName(String s) {
        return objects.containsKey(s);
    }

    // javadoc inherited
    public void setParameterValue(String name, String value) {
        objects.put(name, value);
    }

    // javadoc inherited
    public String getParameterValue(String s)
        throws MissingParameterException {
        final String result;
        if (objects.containsKey(s)) {
            result = (String) objects.get(s);
        } else {
            throw new MissingParameterException("missing-parameter", s);
        }
        return result;
    }

    // javadoc inherited
    public float getFloat(String name) throws MissingParameterException {
        return Float.parseFloat(getParameterValue(name));
    }

    // javadoc inherited
    public int getInteger(String name) throws MissingParameterException {
        return Integer.parseInt(getParameterValue(name));
    }

    // javadoc inherited
    public boolean getBoolean(String name) throws MissingParameterException {
        return Boolean.valueOf(getParameterValue(name)).booleanValue();
    }

    // javadoc inherited
    public void removeParameterValue(String name) {
        objects.remove(name);
    }

    // javadoc inherited
    public Object getObject(String name) {
        return objects.get(name);
    }

    // javadoc inherited
    public void setObject(String name, Object value) {
        objects.put(name, value);
    }

    // javadoc inherited
    public Iterator getParameterNames() {
        final Iterator delegate = objects.entrySet().iterator();
        return new Iterator() {
            public boolean hasNext() {
                return delegate.hasNext();
            }

            public Object next() {
                Map.Entry entry = (Map.Entry) delegate.next();
                return entry.getKey();
            }

            public void remove() {
                delegate.remove();
            }
        };
    }
}
