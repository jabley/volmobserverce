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
package com.volantis.synergetics.performance.arm.openarm;

import net.m2technologies.open_arm.OpenArmAgent;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Properties;

import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmIdentityProperties;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.opengroup.arm40.transaction.ArmUser;


/**
 * This class enables us to instanciate an OpenArm ARMTransactionFactory in the
 * standard ARM manner.
 */

public class OpenArmTransactionFactory implements ArmTransactionFactory {


    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(OpenArmTransactionFactory.class);

    /**
     * The OpenArmAgent we are using to generate our delegate.
     */
    private static OpenArmAgent openArmAgent;

    /**
     * The delegated ArmTransactionFactory
     */
    private ArmTransactionFactory delegate;

    /**
     * The System Property containing the name of the openarm config file.
     */
    private String CONFIG_FILE_PROPERTY = "openarm.config.file";

    public OpenArmTransactionFactory() {

        if (openArmAgent == null) {
            Properties p = System.getProperties();
            String configFileName = p.getProperty(CONFIG_FILE_PROPERTY);
            if (configFileName == null) {
                logger.error("property-can-not-be-found",
                             CONFIG_FILE_PROPERTY);
                throw new IllegalStateException();
            }
            openArmAgent = new OpenArmAgent(configFileName);
        }
        delegate = openArmAgent.getTransactionFactory();
    }

    //Javadoc Inherited
    public ArmApplication newArmApplication(
        ArmApplicationDefinition armApplicationDefinition,
        String s, String s1,
        String[] strings) {
        return delegate.newArmApplication(armApplicationDefinition, s, s1,
                                          strings);
    }

    //Javadoc Inherited
    public ArmApplicationDefinition newArmApplicationDefinition(String s,
                                                                ArmIdentityProperties armIdentityProperties,
                                                                ArmID armID) {
        return delegate.newArmApplicationDefinition(s, armIdentityProperties,
                                                    armID);
    }

    //Javadoc Inherited
    public ArmCorrelator newArmCorrelator(byte[] bytes) {
        return delegate.newArmCorrelator(bytes);
    }

    //Javadoc Inherited
    public ArmCorrelator newArmCorrelator(byte[] bytes, int i) {
        return delegate.newArmCorrelator(bytes, i);
    }

    //Javadoc Inherited
    public ArmID newArmID(byte[] bytes) {
        return delegate.newArmID(bytes);
    }

    //Javadoc Inherited
    public ArmID newArmID(byte[] bytes, int i) {
        return delegate.newArmID(bytes, i);
    }

    //Javadoc Inherited
    public ArmIdentityProperties newArmIdentityProperties(String[] strings,
                                                          String[] strings1,
                                                          String[] strings2) {
        return delegate.newArmIdentityProperties(strings, strings1, strings2);
    }

    //Javadoc Inherited
    public ArmIdentityPropertiesTransaction
        newArmIdentityPropertiesTransaction(String[] strings,
                                            String[] strings1,
                                            String[] strings2, String s) {
        return delegate.newArmIdentityPropertiesTransaction(strings, strings1,
                                                            strings2, s);
    }

    //Javadoc Inherited
    public ArmTransaction newArmTransaction(ArmApplication armApplication,
                                            ArmTransactionDefinition armTransactionDefinition) {
        return delegate.newArmTransaction(armApplication,
                                          armTransactionDefinition);
    }

    //Javadoc Inherited
    public ArmTransactionDefinition newArmTransactionDefinition(
        ArmApplicationDefinition armApplicationDefinition,
        String s,
        ArmIdentityPropertiesTransaction armIdentityPropertiesTransaction,
        ArmID armID) {
        return delegate.newArmTransactionDefinition(armApplicationDefinition,
                                                    s,
                                                    armIdentityPropertiesTransaction,
                                                    armID);
    }

    //Javadoc Inherited
    public ArmUser newArmUser(String s, ArmID armID) {
        return delegate.newArmUser(s, armID);
    }

    //Javadoc Inherited
    public boolean setErrorCallback(ArmErrorCallback armErrorCallback) {
        return delegate.setErrorCallback(armErrorCallback);
    }

    //Javadoc Inherited
    public int getErrorCode() {
        return delegate.getErrorCode();
    }

    //Javadoc Inherited
    public int setErrorCode(int i) {
        return delegate.setErrorCode(i);
    }

    //Javadoc Inherited
    public String getErrorMessage(int i) {
        return delegate.getErrorMessage(i);
    }

}
