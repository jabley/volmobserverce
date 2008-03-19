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
package com.volantis.synergetics.performance.arm;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.performance.MonitoredApplication;
import com.volantis.synergetics.performance.MonitoredTransaction;
import com.volantis.synergetics.performance.MonitoringFactory;

import java.util.Properties;

import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransactionFactory;

/**
 * This is the implementation of a {@link MonitoringFactory} that provides
 * imnplementation of Arm compliant objects.
 */

public class ArmMonitoringFactoryImpl implements MonitoringFactory,
    ArmErrorCallback {


    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ArmMonitoringFactoryImpl.class);

    private static MonitoringFactory instance =
        new ArmMonitoringFactoryImpl();

    private ArmTransactionFactory armTransactionFactory;

    // Protect our constructor.
    private ArmMonitoringFactoryImpl() {
        Properties p = System.getProperties();
        String tranFactoryName = p.getProperty(
            ArmTransactionFactory.propertyKey);
        if (tranFactoryName == null) {
            logger.error("property-can-not-be-found",
                         ArmTransactionFactory.propertyKey);
            armTransactionFactory = null;
        } else {
            Class tranFactoryClass = null;
            try {
                tranFactoryClass = Class.forName(tranFactoryName, true,
                                                 this.getClass()
                                                 .getClassLoader());
            } catch (ClassNotFoundException e) {
                logger.error("could-not-find-class", tranFactoryName);
                logger.error(e);
                armTransactionFactory = null;
            }

            if (tranFactoryClass != null) {
                try {
                    armTransactionFactory =
                        (ArmTransactionFactory) tranFactoryClass.newInstance();
                } catch (Exception e) {
                    logger.error("could-not-instantiate-class",
                                 tranFactoryName);

                    logger.error(e);
                    armTransactionFactory = null;
                }
            }
        }

        if (armTransactionFactory != null) {
            armTransactionFactory.setErrorCallback(this);
        }
    }

    /**
     * Get the single instance of this factory.
     *
     * @return The instance of {@link ArmMonitoringFactoryImpl}.
     */
    public static MonitoringFactory getInstance() {
        // Only return an instance if Arm has initialised properly
        if (((ArmMonitoringFactoryImpl) instance).armTransactionFactory
            != null) {
            return instance;
        }
        return null;
    }

    //Javadoc Inherited
    public MonitoredApplication createApplication(String appName) {
        return new ArmMonitoredApplicationImpl(armTransactionFactory,
                                               appName);
    }

    //Javadoc Inherited
    public MonitoredTransaction createTransaction(
        MonitoredApplication monitoredApplication,
        String transactionName) {
        return new ArmMonitoredTransactionImpl(armTransactionFactory,
                                               ((ArmMonitoredApplicationImpl) monitoredApplication)
                                               .getArmApplication(),
                                               transactionName);
    }

    //Javadoc Inherited
    public void errorCodeSet(ArmInterface errorObject,
                             String interfaceName, String methodName) {
        if (logger.isDebugEnabled()) {
            logger.debug("*** Error callback: ***");
            logger.debug("   Class: " +
                         errorObject.getClass().getName());
            logger.debug("   Interface: " + interfaceName);
            logger.debug("   Method: " + methodName);
            logger.debug("   Description: " +
                         errorObject.getErrorMessage(
                             errorObject.getErrorCode()) +
                         " (" + errorObject.getErrorCode() + ")");
            logger.debug("***********************");
        }
    }
}
