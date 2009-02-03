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
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.descriptorstore.Parameters;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

/**
 * An immutable Parameters object
 */
public class ProxiedParameters implements ObjectParameters {

    private Map<String, Object> objects = new HashMap<String, Object>();

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ProxiedParameters.class);


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
    public String getParameterValue(String name)
        throws MissingParameterException {

        return getString(name);
    }    

    public String getString(String name) throws MissingParameterException {

        String result;
        Object value = objects.get(name);
        if (value instanceof String) {
            result = (String)value;
        } else if (null != value) {
            throw new MissingParameterException("incompatible-parameter",
                    new String[] { name, "string", value.getClass().getName() });
        } else {
            throw new MissingParameterException("missing-parameter", name);
        }
        return result;
    }

    // javadoc inherited
    public float getFloat(String name) throws MissingParameterException {

        Float result;        
        Object value = objects.get(name);
        if (value instanceof Float) {
            result = (Float)value;
        } else if (value instanceof String) {
            result = Float.valueOf((String)value);
        } else if (value != null ) {
            throw new MissingParameterException("incompatible-parameter", 
                    new String[] { name, "float", value.getClass().getName() });
        } else {
            throw new MissingParameterException("missing-parameter", name);        
        }
        return result;
    }

    // javadoc inherited
    public int getInteger(String name) throws MissingParameterException {
        Integer result;
        Object value = objects.get(name);
        if (value instanceof Integer) {
            result = (Integer)value;
        } else if (value instanceof String) {
            result =Integer.valueOf((String)value);
        } else if (value != null ) {
            throw new MissingParameterException("incompatible-parameter",
                    new String[] { name, "int", value.getClass().getName() });
        } else {
            throw new MissingParameterException("missing-parameter", name);
        }
        return result;
    }

    // javadoc inherited
    public boolean getBoolean(String name) throws MissingParameterException {
        Boolean result;
        Object value = objects.get(name);
        if (value instanceof Boolean) {
            result = (Boolean)value;
        } else if (value instanceof String) {
            result =Boolean.valueOf((String)value);
        } else if (value != null ) {
            throw new MissingParameterException("incompatible-parameter",
                    new String[] { name, "boolean", value.getClass().getName() });
        } else {
            throw new MissingParameterException("missing-parameter", name);
        }
        return result;
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
