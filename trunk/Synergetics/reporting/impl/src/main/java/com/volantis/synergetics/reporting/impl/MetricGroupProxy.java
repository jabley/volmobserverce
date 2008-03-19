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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl;

import com.volantis.synergetics.reporting.DynamicReport;
import com.volantis.synergetics.reporting.ReportHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The abstract superclass of all MetricGroupProxy implementations. This allows
 * subclasses to process the methods in the custom interface and then this
 * class can cache the results. This allows subsequent encounters with the
 * interface to be handled more efficiently.
 */
public class MetricGroupProxy implements InvocationHandler {

    /**
     * The cache that this proxy will use
     */
    private final BoundInterfaceCache boundInterfaceCache;

    /**
     * The cache to use for dynamic reports.
     */
    private final BoundDynamicReportCache boundDynamicReportCache;

    /**
     * The handler that will take responsibility for writing the event out
     */
    private final ReportHandler handler;

    /**
     * Map a metric name to its value
     */
    private final Map metrics = new HashMap();

    /**
     * The transaction ID
     */
    private final String transactionID;

    /**
     * The transaction ID of the parent or null if there is no parent.
     */
    private final String parentTransID;

    /**
     * The interface class that this is a proxy for.
     */
    private final Class clazz;

    /**
     * The optional name provided if this is a dynmaic report. This name
     * is used to map this entity to a configuration for the report.
     */
    private final String optionalConfigName;

    /**
     * Create a new Metric Group Proxy that is proxying for the specified
     * interface.
     *
     * @param boundInterfaceCache The interface cache this instance should use.
     *                            for. Must not be null.
     * @param clazz               the class that this is proxing for.
     * @param handler             the handler that will actually report the
     *                            data. May be null.
     * @param transactionID       the transaction ID to use.
     * @param optionalConfigName  the optional name that will be used to map
     *                            this entity to a reporting configuration.
     *                            This should be "null" unless clazz is
     *                            DynamicReport.class
     */
    public MetricGroupProxy(BoundInterfaceCache boundInterfaceCache,
                            BoundDynamicReportCache boundDynamicReportCache,
                            Class clazz,
                            ReportHandler handler,
                            String transactionID, String parentTransID,
                            String optionalConfigName) {
        this.boundInterfaceCache = boundInterfaceCache;
        this.boundDynamicReportCache = boundDynamicReportCache;
        this.clazz = clazz;
        this.handler = handler;
        this.transactionID = transactionID;
        this.parentTransID = parentTransID;
        this.optionalConfigName = optionalConfigName;
    }

    /**
     * Return the metrics bound to this object. This needs to return the actual
     * Map used so the bindings can modify its content
     *
     * @return the metrics bound to this object
     */
    Map getMetrics() {
        //noinspection ReturnOfCollectionOrArrayField
        return metrics;
    }

    /**
     * Return the class that proxies
     *
     * @return
     */
    Class getProxiedClass() {
        return this.clazz;
    }

    /**
     * Return the optional configuration name. This will be null unless this is
     * a proxy for a {@link DynamicReport} instance.
     *
     * @return null or the name of the dynamicReport if this is a DynamicReport
     */
    String getOptionalConfigurationName() {
        return this.optionalConfigName;
    }

    /**
     * Return the report handler bound to this instance or null if one was not
     * supplied.
     *
     * @return
     */
    ReportHandler getReportHandler() {
        return handler;
    }

    /**
     * Return the ID bound to this interface.
     *
     * @return the id bound to this interface.
     */
    String getTransactionID() {
        return transactionID;
    }

    /**
     * Return the ID bound to the parent transaction of this interface.
     *
     * @return the id bound to the parent transaction ofthis interface. May be
     *         null if there is no parent
     */
    String getParentTransactionID() {
        return parentTransID;
    }

    /**
     * implement the InvocationHandler interface to look up the specified
     * method in the {@link Method}->{@link Binding} cache. If a binding
     * exists
     */
    public Object invoke(Object proxied, Method method, Object[] args)
        throws Throwable {

        Object result = null;
        Binding bind = null;
        if (method.getDeclaringClass() == DynamicReport.class) {
            bind = boundDynamicReportCache.getBinding(
                optionalConfigName, method);
        } else {
            // the following method will always return a binding instance
            bind = boundInterfaceCache.getBinding(method);
        }
        if (null != bind) {
            result = bind.invoke(this, args);
        }
        return result;
    }
}
