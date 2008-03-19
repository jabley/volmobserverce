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
import com.volantis.synergetics.reporting.DynamicReport;
import com.volantis.synergetics.reporting.Event;
import com.volantis.synergetics.reporting.impl.Metric;
import com.volantis.synergetics.reporting.ReportHandler;
import com.volantis.synergetics.reporting.Status;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class caches interfaces for which bindings have already been defined.
 * This life time scope of this class should be bound by the lifetime of the
 * OSGi bundle that requires a Reporting service.
 *
 * In other words; When a bundle that requires reporting goes out of scope the
 * cache associated with that reporting instance should go out of scope.
 */
public class BoundInterfaceCache {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(BoundInterfaceCache.class);

    /**
     * The PATTERN used to match method names. The first group has the "name"
     * of the metric in it the second has the type of the metric.
     */
    private static final Pattern PATTERN =
        Pattern.compile("^set(.+)(Counter|Gauge|Id|String)$");

    /**
     * The signature for a stop method that takes a m
     */
    private static final Class[] STOP_SIG =
        new Class[]{Status.class, String.class};

    /**
     * The signature for methods that take no parameters
     */
    private static final Class[] EMPTY_SIG =
        new Class[]{};

    /**
     * The signature for methods that take no parameters
     */
    private static final Class[] UPDATE_SIG =
        new Class[]{String.class};

    /**
     * An empty binding used to supress errors
     */
    public static final Binding NO_OP_BINDING = new Binding() {
        public Object invoke(MetricGroupProxy target, Object[] args) {
            return null;  // do nothing
        }
    };

    /**
     * This maps Method instances to Binding instances. When a method is
     * invoked its binding is looked up in this cache and the invoke method of
     * that binding is called.
     */
    private final HashMap methodBindingCache = new HashMap();

    /**
     * A convienience mthod for adding method bindings to the Method cache.
     *
     * @param method  the method to cache a binding for
     * @param binding the binding to associate with the method.
     */
    private void addToMethodBindingCache(Method method,
                                         Binding binding) {
        // this cache is thread safe
        methodBindingCache.put(method, binding);
    }

    /**
     * gets the binding for the Create a binding for the specified method.
     *
     * @param method the method for which a binding should be created
     * @return a binding for the specified method or the NO_OP_BINDING if no
     *         binding could be generated
     */
    public Binding getBinding(final Method method) {

        final Binding result;
        synchronized (methodBindingCache) {
            if (methodBindingCache.containsKey(method)) {
                // methods are equal if they come from the same class
                result = (Binding) methodBindingCache.get(method);
            } else {

                // build a binding for the specified method
                String methodName = method.getName();
                Matcher matcher = PATTERN.matcher(methodName);
                // if a match then we are in custom functions
                if (matcher.matches()) {
                    result = namedBinding(matcher, method);
                } else if (methodName.equals("start")) {
                    result = createStartBinding(method);
                } else if (methodName.equals("update")) {
                    result = createUpdateBinding(method);
                } else if (methodName.equals("stop")) {
                    result = createStopBinding(method);
                } else if (methodName.equals("getTransactionID")) {
                    result = createTransactionIDBinding(method);
                } else {
                    LOGGER.error("invalid-method-name", method);
                    result = NO_OP_BINDING;
                }
                addToMethodBindingCache(method, result);
            }
        }
        return result;
    }


    /**
     * Create the binding for the "setBla*" methods in the derived interfaces
     *
     * @param method the custom method. This is checked for appropriate
     *               arguments.
     * @return the binding for the particular method called "getTransactionID".
     *         This will be a no-op binding if the stop method was not the one
     *         we were looking for.
     */
    private static Binding namedBinding(Matcher matcher, final Method method) {

        final Binding result;
        final String metricName = matcher.group(1);
        // uncomment the line below if you are interested in the "form" of the metric
        //        String metricForm = matcher.group(2);
        Class[] params = method.getParameterTypes();
        if (params.length != 1) {
            LOGGER.error("invalid-paramter-types", method);
            result = NO_OP_BINDING;
        } else {

            result = new Binding() {
                //javadoc inherited
                public Object invoke(MetricGroupProxy target,
                                     Object[] args) {
                    Map localMetrics = target.getMetrics();
                    localMetrics.put(metricName, args[0]);
                    // we also add, to the map, the class that caused this
                    // event to be fired. This is so OSGi knows what event
                    // subtype to use
                    localMetrics.put(Class.class, target.getProxiedClass());
                    return null;
                }
            };

        }

        return result;
    }

    /**
     * Create the binding for the "getTransactionID" method in the base Report
     * interface.
     *
     * @param method the method called "getTransactionID". This is checked for
     *               the correct arguments.
     * @return the binding for the particular method called "getTransactionID".
     *         This will be a no-op binding if the stop method was not the one
     *         we were looking for.
     */
    private static Binding createTransactionIDBinding(final Method method) {

        final Binding result;
        Class[] params = method.getParameterTypes();
        if (params.length != 0) {
            LOGGER.error("method-needs-argument", method);
            result = NO_OP_BINDING;
        } else {
            result = new Binding() {
                //javadoc inherited
                public Object invoke(MetricGroupProxy target,
                                     Object[] args) {
                    return target.getTransactionID();
                }
            };
        }
        return result;
    }

    /**
     * Create the binding for the two "stop" methods in the base Report
     * interface
     *
     * @param method the method called "stop". This is checked for the correct
     *               arguments.
     * @return the binding for the particular method called "stop". This will
     *         be a no-op binding if the stop method was not the one we were
     *         looking for.
     */
    private static Binding createStopBinding(final Method method) {
        Binding result = NO_OP_BINDING;
        if (Arrays.equals(method.getParameterTypes(), STOP_SIG)) {

            return new Binding() {
                //javadoc inherited
                public Object invoke(MetricGroupProxy target,
                                     Object[] args) {

                    Map localMetrics = new HashMap(target.getMetrics());
                    // add a special substitution param for the message
                    if (args[1] != null) {
                        localMetrics.put(Metric.MESSAGE.getName(), args[1]);
                    }
                    localMetrics.put(Metric.TIMESTAMP.getName(),
                                     new Date());
                    localMetrics.put(Metric.TRANSID.getName(),
                                     target.getTransactionID());
                    localMetrics
                        .put(Metric.EVENT.getName(), Event.STOP.toString());
                    localMetrics.put(Metric.PTRANSID.getName(),
                                     target.getParentTransactionID());
                    // we also add, to the map, the class that caused this
                    // event to be fired. This is so OSGi knows what event
                    // subtype to use
                    localMetrics.put(Class.class, target.getProxiedClass());
                    ReportHandler localHandler = target.getReportHandler();
                    if (null != localHandler) {
                        localHandler.report(localMetrics);
                    } else { // we dispatch to the OSGi Event Listeners

                    }
                    localMetrics.clear();
                    return null;
                }
            };

        } else {
            LOGGER.error("invalid-paramter-types", method);
        }
        return result;
    }

    /**
     * Create the binding for the "start" method in the base Report interface
     *
     * @param method the method called "start". This is checked for the correct
     *               arguments.
     * @return the binding for the particular method called "start". This will
     *         be a no-op binding if the start method was not the one we were
     *         looking for.
     */
    private static Binding createStartBinding(final Method method) {
        Binding result = NO_OP_BINDING;
        if (Arrays.equals(method.getParameterTypes(), EMPTY_SIG)) {

            result = new Binding() {
                //javadoc inherited
                public Object invoke(MetricGroupProxy target,
                                     Object[] args) {
                    Map localMetrics = new HashMap(target.getMetrics());
                    // add a special substitution param for the message

                    localMetrics.put(Metric.TIMESTAMP.getName(),
                                     new Date());
                    localMetrics.put(Metric.TRANSID.getName(),
                                     target.getTransactionID());
                    localMetrics
                        .put(Metric.EVENT.getName(), Event.START.toString());
                    localMetrics.put(Metric.PTRANSID.getName(),
                                     target.getParentTransactionID());
                    // we also add, to the map, the class that caused this
                    // event to be fired. This is so OSGi knows what event
                    // subtype to use
                    localMetrics.put(Class.class, target.getProxiedClass());

                    ReportHandler localHandler = target.getReportHandler();
                    if (null != localHandler) {
                        localHandler.report(localMetrics);
                    } else { // we dispatch to the OSGi Event Listeners

                    }
                    localMetrics.clear();
                    return null;
                }
            };

        } else {
            LOGGER.error("invalid-paramter-types", method);
        }
        return result;
    }

    /**
     * Create bindings for the "update" method in the base Report class.
     *
     * @param method the method that is called update. This is checked for the
     *               correct arguments
     * @return the binding object for the "update" method. This will be a no-op
     *         binding if the start method was not the one we were looking
     *         for.
     */
    private static Binding createUpdateBinding(final Method method) {
        Binding result = NO_OP_BINDING;
        if (Arrays.equals(method.getParameterTypes(), UPDATE_SIG)) {
            result = new Binding() {
                //javadoc inherited
                public Object invoke(MetricGroupProxy target,
                                     Object[] args) {
                    Map localMetrics = new HashMap(target.getMetrics());
                    // add a special substitution param for the message
                    if (args[0] != null) {
                        localMetrics.put(Metric.MESSAGE.getName(), args[0]);
                    }
                    localMetrics.put(Metric.TIMESTAMP.getName(),
                                     new Date());
                    localMetrics.put(Metric.TRANSID.getName(),
                                     target.getTransactionID());
                    localMetrics.put(Metric.EVENT.getName(),
                                     Event.UPDATE.toString());
                    localMetrics.put(Metric.PTRANSID.getName(),
                                     target.getParentTransactionID());
                    // we also add, to the map, the class that caused this
                    // event to be fired. This is so OSGi knows what event
                    // subtype to use
                    localMetrics.put(Class.class, target.getProxiedClass());
                    localMetrics.put(DynamicReport.class,
                                     target.getOptionalConfigurationName());

                    ReportHandler localHandler = target.getReportHandler();
                    if (null != localHandler) {
                        localHandler.report(localMetrics);
                    } else { // we dispatch to the OSGi Event Listeners

                    }
                    localMetrics.clear();

                    return null;
                }
            };
        } else {
            LOGGER.error("invalid-paramter-types", method);
        }
        return result;
    }

}
