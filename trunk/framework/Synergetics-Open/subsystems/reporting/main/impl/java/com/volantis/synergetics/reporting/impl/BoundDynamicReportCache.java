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
package com.volantis.synergetics.reporting.impl;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to cache the bindings generated for dynamic reports.
 * The scope of this class is to the bundle that requested the Reporting service
 * instance that this Cache is contained within. Therefore when that bundle
 * goes away so does this cache.
 */
public class BoundDynamicReportCache {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(BoundDynamicReportCache.class);

    /**
     * The name of the method that is used to set dynamic metrics
     */
    private static final String DYNAMIC_METRIC_PATTERN = "setParameter";

    /**
     * The map of named bindings for dynamic reports
     */
    private final Map NAMED_CONFIGS = new HashMap();

    /**
     * Return the Binding associated with the binding name and method.
     *
     * @param name the name that will link this method to a reporting
     * configuration
     * @param method the method (setParameter) that will be called.
     * @return a binding that can invoke the correct handler for the specified
     * method.
     */
    public Binding getBinding(String name, Method method) {
        Binding result = BoundInterfaceCache.NO_OP_BINDING;
        if (null != name) {
            synchronized (NAMED_CONFIGS) {
                if (NAMED_CONFIGS.containsKey(name)) {
                    result = (Binding) NAMED_CONFIGS.get(name);
                } else {
                    result = createSetParameterBinding(method);
                    NAMED_CONFIGS.put(name, result);
                }
            }
        }
        return result;
    }

    /**
     * Create a binding for the specified method.
     *
     * @param method the method to create a binding for
     * @return the binding for the specified method
     */
    private static Binding createSetParameterBinding(final Method method) {
        Binding result = BoundInterfaceCache.NO_OP_BINDING;
        Class[] params = method.getParameterTypes();
        if (params.length != 2) {
            LOGGER.error("method-needs-two-arguments", method);
        } if (method.getName().equals(DYNAMIC_METRIC_PATTERN)) {
            result = new Binding() {
                //javadoc inherited
                public Object invoke(MetricGroupProxy target,
                                                  Object[] args) {
                    Map localMetrics = target.getMetrics();
                    localMetrics.put((String) args[0],
                                     (Object) args[1]);
                    return null;
                }
            };
        }
        return result;
    }
}
