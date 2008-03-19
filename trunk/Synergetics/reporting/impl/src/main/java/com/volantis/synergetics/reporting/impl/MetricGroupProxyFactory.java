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

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.reporting.DynamicReport;
import com.volantis.synergetics.reporting.ExclusionManager;
import com.volantis.synergetics.reporting.Report;
import com.volantis.synergetics.reporting.ReportHandler;
import com.volantis.synergetics.reporting.config.ConnectionStrategy;
import com.volantis.synergetics.reporting.config.DatasourceConfiguration;
import com.volantis.synergetics.reporting.config.GenericHandler;
import com.volantis.synergetics.reporting.config.GenericHandlerParam;
import com.volantis.synergetics.reporting.config.ReportElement;
import com.volantis.synergetics.reporting.config.ReportingConfig;
import com.volantis.synergetics.reporting.config.SqlHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Synergetic reporting gathering events engine initialization based on reporting congiguration
 *
 */
public class MetricGroupProxyFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(MetricGroupProxyFactory.class);

    /**
     * The arguments used to instantiate a ReportEventHandler
     */
    private static final Class[] REPORT_HANDLER_ARGS = new Class[] { Map.class,
            String.class };

    /**
     * A map of handlers for the reporting infrastructure. It maps {@link
     * Class}'s to the {@link ReportHandler} assocaited with that class
     */
    private final Map handlers = new ConcurrentHashMap();

    /**
     * The cache for interfaces
     */
    private final BoundInterfaceCache boundInterfaceCache =
        new BoundInterfaceCache();

    /**
     * The cache for dynamic reports
     */
    private final BoundDynamicReportCache boundDynamicReportCache =
        new BoundDynamicReportCache();

    /**
     * Used to generate the ID's
     */
    private final TransactionIDGenerator idGenerator = new UUIDGenerator();

    /**
     * Constructor
     * @param config ReportingConfig
     */
    public MetricGroupProxyFactory(ReportingConfig config) {
        if (config != null) {
            Map datasourceConfigs = config.getDatasourceConfigMap();
            for (Iterator it = config.getReportElementsIterator(); it.hasNext();) {
                ReportElement report = (ReportElement) it.next();

                if (report.getEnabled()) {
                    ExclusionManager.getDefaultInstance().initializeExclusions(
                            report.getBinding(),
                            report.getReportExclusion());
                    

                    if (report.getGenericHandler() != null) {
                        GenericHandler genericHandler = report.getGenericHandler();
                        String className = genericHandler.getClassName();
                        Class handlerClass = null;
                        try {
                            handlerClass = Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            LOGGER.error("failed-to-find-class", className, e);
                        }
                        try {

                            Constructor con = null;
                            try {
                                con = handlerClass.getConstructor(REPORT_HANDLER_ARGS);
                            } catch (NoSuchMethodException e) {
                                LOGGER.error("failed-to-create-class",
                                        handlerClass,
                                        e);
                            }
                            ReportHandler reportHandler = null;
                            try {
                                Object[] args = new Object[2];
                                args[0] = buildGenericMap(genericHandler);
                                args[1] = report.getBinding();
                                reportHandler = (ReportHandler) con.newInstance(args);
                            } catch (InstantiationException e) {
                                LOGGER.error("failed-to-load-class",
                                        handlerClass,
                                        e);
                            } catch (IllegalAccessException e) {
                                LOGGER.error("failed-to-load-class",
                                        handlerClass,
                                        e);
                            } catch (InvocationTargetException e) {
                                LOGGER.error("failed-to-load-class",
                                        handlerClass,
                                        e);
                            }
                            handlers.put(report.getBinding(), reportHandler);

                        } catch (Exception e) {
                            LOGGER.error("failed-to-load-class", className);
                        }

                    } else if (report.getSqlHandler() != null) {
                        if (existsDatasourceDefinition(
                                datasourceConfigs, report.getSqlHandler())) {
                            ConnectionStrategy connStrategy =
                                DatasourceManager.getInstance().createConnectionStrategy(
                                        (DatasourceConfiguration) datasourceConfigs.get(
                                                report.getSqlHandler().getDatasourceName()));
                            ReportHandler handler =
                                new JDBCReportHandler(connStrategy, report);
                            handlers.put(report.getBinding(), handler);
                        } else {
                            LOGGER.error("sql-handler-datasource-not-found",
                                    new Object[] {report.getSqlHandler().getDatasourceName(),
                                            report.getBinding()});
                        }

                    } else {
                        LOGGER.error("handler-not-found", report.getBinding());
                    }
                }
            }
        }
    }

    /**
     * Build a map from the generic parameters in the generic handler.
     *
     * @param handler the handler whose generic parameters need to be put in the
     *            map
     * @return a Map of parameters from the provided {@link GenericHandler}
     */
    private Map buildGenericMap(GenericHandler handler) {
        Map result = new HashMap();
        for (int i = 0; i < handler.sizeReportList(); i++) {
            GenericHandlerParam param = handler.getGenericHandlerParam(i);
            if (param != null) {
                result.put(param.getName(), param.getValue());
            }
        }
        return result;
    }

    /**
     *
     * @param config ReportingConfig reporting config
     * @param handler SqlHandler
     * @return true if exists definition of datasource
     */
    private boolean existsDatasourceDefinition(
            Map config, SqlHandler handler) {
        return config.get(handler.getDatasourceName()) != null;
    }

    /**
     * Bind the proxy class to the instance.
     *
     * @param clazz the interface to create a Report object for.
     * @return an implementation of the specified interface.
     */
    public Report createReport(Class clazz) {
        return createReport(clazz, null);
    }

    /**
     * Bind the proxy class to the instance.
     *
     * @param clazz the interface to create a Report object for.
     * @param parentTransactionID a transaction ID to bind to the report
     *            instance as the parent (or enclosing) reporting transaction
     * @return an implementation of the specified interface.
     */
    public Report createReport(Class clazz, String parentTransactionID) {
        // look up the handler bound to this interface
        ReportHandler handler = (ReportHandler) handlers.get(clazz.getName());
        // we can pass a null handler to the MetricGroupProxy so no
        // check needed
        String transID = idGenerator.getTransactionID();
        MetricGroupProxy proxy = new MetricGroupProxy(boundInterfaceCache,
                                                      boundDynamicReportCache,
                                                      clazz,
                                                      handler,
                                                      transID,
                                                      parentTransactionID,
                                                      null);
        // @todo getInterfaces is not recursive so we probably need to build
        // the list of interfaces not directly extended
        return (Report) java.lang.reflect.Proxy.newProxyInstance(
                clazz.getClassLoader(), new Class[] { clazz }, proxy);
    }

    /**
     * Bind the proxy class to the instance of the DynamicReport.
     *
     * @param binding The binding key for the report handler.
     * @return an implementation of the DynamicReport interface.
     */
    public DynamicReport createDynamicReport(String binding) {
        return createDynamicReport(binding, null);
    }

    /**
     * Bind the proxy class to the instance of the DynamicReport.
     *
     * @param binding The binding key for the report handler.
     * @param parentTransactionID a transaction ID to bind to the report
     *            instance as the parent (or enclosing) reporting transaction
     * @return an implementation of the DynamicReport interface.
     */
    public DynamicReport createDynamicReport(String binding,
            String parentTransactionID) {
        // look up the handler bound to handlerKey
        ReportHandler handler = (ReportHandler) handlers.get(binding);
        // we can pass a null handler to the MetricGroupProxy so no
        // check needed
        String transID = idGenerator.getTransactionID();
        Class drc = com.volantis.synergetics.reporting.DynamicReport.class;
        MetricGroupProxy proxy = new MetricGroupProxy(boundInterfaceCache,
                                                      boundDynamicReportCache,
                                                      drc,
                                                      handler,
                                                      transID,
                                                      parentTransactionID,
                                                      binding);
        // @todo getInterfaces is not recursive so we probably need to build
        // the list of interfaces not directly extended
        return (DynamicReport) java.lang.reflect.Proxy.newProxyInstance(
                drc.getClassLoader(), new Class[] { drc }, proxy);
    }
}
