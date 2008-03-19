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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.synergetics.performance;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.performance.noop.NoOpMonitoringFactoryImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This factory allows the creation of {@link MonitoringFactory}
 * implementations.
 */

public class MonitoringMetaFactory {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(MonitoringMetaFactory.class);

    private static final MonitoringMetaFactory instance =
        new MonitoringMetaFactory();

    private static final MonitoringFactory armMonitoringFactory;
    static {
        // Try and load the ARM using reflection, if it could not be found then
        // just ignore it and carry on. This will not work in an OSGi
        // environment but in that case the factories should be registered as
        // services so it doesn't matter anyway.
        MonitoringFactory factory = null;
        try {
            Class clazz = Class.forName(
                    "com.volantis.synergetics.performance.arm.ArmMonitoringFactoryImpl");
            Method method = clazz.getMethod("getInstance", null);
            factory = (MonitoringFactory) method.invoke(null, null);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        armMonitoringFactory = factory;
    }

    private static MonitoringFactory noOpMonitoringFactory =
        NoOpMonitoringFactoryImpl.getInstance();

    /**
     * Protect our constructor.
     */
    private MonitoringMetaFactory() {

    }

    /**
     * Get the singleton instance of this factory.
     *
     * @return The instance of this factory.
     */
    public static MonitoringMetaFactory getInstance() {
        return instance;
    }

    /**
     * Get the default instance of the Arm Monitoring Factory.
     *
     * @return An Arm Compliant {@link MonitoringFactory}.
     */
    public MonitoringFactory getArmMonitoringFactory() {
        return armMonitoringFactory;
    }

    /**
     * Get the default instance of the NoOp Monitoring Factory.
     *
     * @return A NoOp {@link MonitoringFactory}.
     */
    public MonitoringFactory getNoOpMonitoringFactory() {
        return noOpMonitoringFactory;
    }

    /**
     * Get the active MonitoringFactory. This would usually be an Arm factory
     * but if an Arm agent is not available will fall back to a NoOp factory.
     *
     * @return The current {@link MonitoringFactory}
     */
    public MonitoringFactory getActiveMonitoringFactory() {
        MonitoringFactory monitoringFactory = null;
        // Try and initialise the Arm Monitor
        monitoringFactory =
            getArmMonitoringFactory();

        if (monitoringFactory == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("ARM logging has not been initialised.");
            }
            monitoringFactory =
                getNoOpMonitoringFactory();
        }
        return monitoringFactory;
    }
}
