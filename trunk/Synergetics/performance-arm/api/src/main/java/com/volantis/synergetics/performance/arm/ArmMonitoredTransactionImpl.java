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

import com.volantis.synergetics.performance.MonitoredTransaction;

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmConstants;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;

/**
 * This Class provides the Arm specific implementation of {@link
 * MonitoredTransaction}.
 */

public class ArmMonitoredTransactionImpl implements MonitoredTransaction {

    /**
     * The wrapped {@link armTransactionDefinition}.
     */
    private ArmTransactionDefinition armTransactionDefinition;

    /**
     * The wrapped {@link armTransaction}.
     */
    private ArmTransaction armTransaction;


    public ArmMonitoredTransactionImpl(
        ArmTransactionFactory armTransactionFactory,
        ArmApplication armApplication,
        String transactionName) {

        armTransactionDefinition =
            armTransactionFactory.newArmTransactionDefinition(
                armApplication.getDefinition(),
                transactionName,
                null,
                null);

        armTransaction = armTransactionFactory.newArmTransaction(
            armApplication, armTransactionDefinition);

    }

    //Javadoc Inherited
    public void start() {
        armTransaction.start();
    }

    //Javadoc inherited
    public void stop(int succesful) {
        if (succesful == SUCCESSFUL) {
            armTransaction.stop(ArmConstants.STATUS_GOOD);
        } else {
            armTransaction.stop(ArmConstants.STATUS_FAILED);
        }
    }

    //Javadoc inherited
    public void stop(int succesful, String diagnostic) {
        if (succesful == SUCCESSFUL) {
            armTransaction.stop(ArmConstants.STATUS_GOOD, diagnostic);
        } else {
            armTransaction.stop(ArmConstants.STATUS_FAILED, diagnostic);
        }
    }

}
