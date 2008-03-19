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

import com.volantis.synergetics.performance.MonitoredApplication;

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;

/**
 * The ARM implementation of {@link MonitoredApplication}
 */

public class ArmMonitoredApplicationImpl implements MonitoredApplication {

    /**
     * The wrapped {@link ArmApplicationDefinition}
     */
    private ArmApplicationDefinition armApplicationDefinition;

    /**
     * The wrapped {@link ArmApplication}
     */
    private ArmApplication armApplication;

    /**
     * The name of the application.
     */
    private String appName;

    public ArmMonitoredApplicationImpl(
        ArmTransactionFactory armTransactionFactory,
        String appName) {

        this.appName = appName;

        armApplicationDefinition =
            armTransactionFactory.newArmApplicationDefinition(appName,
                                                              null,
                                                              null);

        armApplication = armTransactionFactory.newArmApplication(
            armApplicationDefinition,
            null,
            null,
            null);

    }

    /**
     * Allow internal access to the {@link ArmApplicationDefinition}.
     *
     * @return The wrapped {@link ArmApplicationDefinition}.
     */
    ArmApplicationDefinition getArmApplicationDefinition() {
        return armApplicationDefinition;
    }


    /**
     * Allow internal access to the {@link ArmApplication}.
     *
     * @return The wrapped {@link ArmApplication}.
     */
    ArmApplication getArmApplication() {
        return armApplication;
    }

    //Javadoc inherited
    public String getAppName() {
        return appName;
    }
}
