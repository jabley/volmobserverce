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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/MpsPluginRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Mar-03    Geoff           VBM:2002112102 - Created; test case for
 *                              MpsPluginRuleSet.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to
 *                              Synergetics.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;


import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.MpsPluginConfiguration;
import com.volantis.mcs.runtime.configuration.MpsChannelConfiguration;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Test case for {@link MpsPluginRuleSet}.
 */
public class MpsPluginRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public MpsPluginRuleSetTestCase(String s) {
        super(s);
    }

    public void testNull() throws ConfigurationException {
        checkMpsValue(null, null);
    }

    public void testEmpty() throws ConfigurationException {
        MpsValue mpsValue = new MpsValue();
        ChannelSmtpValue channelSmtpValue = new ChannelSmtpValue();
        channelSmtpValue.name = "";
        ChannelSmscValue channelSmscValue = new ChannelSmscValue();
        channelSmscValue.name = "";
        ChannelMmscValue channelMmscValue = new ChannelMmscValue();
        channelMmscValue.name = "";
        ChannelValue[] channelValues = new ChannelValue[]{
            channelSmtpValue, channelSmscValue,
            channelMmscValue
        };
        checkMpsValue(mpsValue, channelValues);
    }

    // TODO: re-enable this when MPS configuration is modified to work with
    // schema validation.
    public void testFull() throws ConfigurationException {
        // all are optional?
        MpsValue mpsValue = new MpsValue();
        mpsValue.internalBaseUrl = "default-internal-base-url";
        mpsValue.messageRecipientInfo = "defaultMessageRecipientClassName";

        ChannelSmtpValue smtp = new ChannelSmtpValue();
        smtp.name = "a default smtp name";
        smtp.hostname = "default smtp host";
        smtp.requiresAuthorisation = Boolean.TRUE;
        smtp.username = "default smtp user";
        smtp.password = "default smtp password";

        ChannelSmscValue smsc = new ChannelSmscValue();
        smsc.name = "default smsc name";
        smsc.address = "default smsc address";
        smsc.port = new Integer(80);
        smsc.username = "default smsc username";
        smsc.password = "default smsc password";
        smsc.bindType = "default smsc bind type";
        smsc.serviceType = "default smsc service type";
        smsc.serviceAddress = "default smsc service address";
        smsc.supportsMulti = Boolean.TRUE;

        ChannelMmscValue mmsc = new ChannelMmscValue();
        mmsc.name = "a default mmsc name";
        mmsc.url = "a default mmsc url";
        mmsc.defaultCountryCode = new Integer(44);

        ChannelValue[] channelValues = new ChannelValue[]{
            smtp, smsc, mmsc
        };

        checkMpsValue(mpsValue, channelValues);
    }

    /**
     * Create a subset of the mcs-config XML document from the values
     * supplied, parse it into a {@link MpsPluginConfiguration} object, and ensure
     * that the values supplied are match those found.
     *
     * @param mpsValue
     * @param channelValues
     * @throws com.volantis.mcs.runtime.configuration.ConfigurationException
     */
    private void checkMpsValue(MpsValue mpsValue,
            ChannelValue[] channelValues)
            throws ConfigurationException {

        String doc = createMpsXml(mpsValue, channelValues);
        System.err.println(doc);
        TestXmlConfigurationBuilder configBuilder =
                new TestXmlConfigurationBuilder(doc);
        // Add the digester parsing rules for the MPS plugin.
        configBuilder.addApplicationPluginRuleSet(new MpsPluginRuleSet());
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        MpsPluginConfiguration mpsPlugin = (MpsPluginConfiguration)
                config.getApplicationPlugin("MPS");

        if (mpsValue != null) {
            assertNotNull("mps", mpsPlugin);
            assertEquals(mpsValue.internalBaseUrl,
                    mpsPlugin.getInternalBaseUrl());
            assertEquals(mpsValue.messageRecipientInfo,
                    mpsPlugin.getMessageRecipientInfo());

            Iterator channels = mpsPlugin.getChannelsIterator();
            if (channelValues != null) {
                assertNotNull("channels", channels);
                assertTrue(channels.hasNext());
                for (int i = 0; i < channelValues.length; i++) {
                    MpsChannelConfiguration channel =
                            (MpsChannelConfiguration) channels.next();
                    ChannelValue channelValue = channelValues[i];
                    checkChannel(channelValue, channel);
                }
                assertTrue(!channels.hasNext());
            } else {
                assertNotNull("channels", channels);
                assertTrue(!channels.hasNext());
            }
        } else {
            assertNull("MpsConfiguration", mpsPlugin);
        }
    }

    private void checkChannel(ChannelValue channelValue,
            MpsChannelConfiguration channel) {
        if (channelValue != null) {
            assertNotNull("channel", channel);
            assertEquals(channelValue.name, channel.getName());
            assertEquals(channelValue.className, channel.getClassName());
            Map arguments = channel.getArguments();
            if (channelValue instanceof ChannelSmtpValue) {
                ChannelSmtpValue smtpValue = (ChannelSmtpValue)channelValue;
                assertEquals(smtpValue.hostname, arguments.get("host"));
                Boolean authValue = null;
                if (arguments.get("auth") != null) {
                    authValue = new Boolean((String)arguments.get("auth"));
                }
                assertEquals(smtpValue.requiresAuthorisation, authValue);
                assertEquals(smtpValue.username, arguments.get("user"));
                assertEquals(smtpValue.password, arguments.get("password"));
            } else if (channelValue instanceof ChannelSmscValue) {
                ChannelSmscValue smscValue = (ChannelSmscValue)channelValue;
                assertEquals(smscValue.address, arguments.get("smsc-ip"));
                Integer smscPortValue = null;
                if (arguments.get("smsc-port") != null) {
                    smscPortValue =
                        new Integer((String)arguments.get("smsc-port"));
                }
                assertEquals(smscValue.port, smscPortValue);
                assertEquals(smscValue.username, arguments.get("smsc-user"));
                assertEquals(smscValue.password, arguments.get("smsc-password"));
                assertEquals(smscValue.bindType, arguments.get("smsc-bindtype"));
                assertEquals(smscValue.serviceType, arguments.get("smsc-svctype"));
                assertEquals(smscValue.serviceAddress,
                    arguments.get("smsc-svcaddr"));
                Boolean smscSupportsMultiValue = null;
                if (arguments.get("smsc-supportsmulti") != null) {
                    smscSupportsMultiValue =
                        new Boolean(
                            (String)arguments.get("smsc-supportsmulti"));
                }
                assertEquals(smscValue.supportsMulti,smscSupportsMultiValue);
            } else if (channelValue instanceof ChannelMmscValue) {
                ChannelMmscValue mmscValue = (ChannelMmscValue)channelValue;
                assertEquals(mmscValue.url, arguments.get("url"));
                Integer defaultCountryCodeValue = null;
                if (arguments.get("default-country-code") != null) {
                    defaultCountryCodeValue =
                        new Integer(
                            (String)arguments.get("default-country-code"));
                }
                assertEquals(mmscValue.defaultCountryCode,
                    defaultCountryCodeValue);
            }
        } else {
            assertNull("channel", channel);
        }

    }

    private String createMpsXml(MpsValue mpsValue, ChannelValue[] channelValues) {
        String doc = "";
        doc += "  <application-plugins> \n";
        if (mpsValue != null) {
            doc += "    <mps";

            if (mpsValue.internalBaseUrl != null) {
                if (mpsValue.internalBaseUrl != null) {
                    doc += " internal-base-url=\"" +
                            mpsValue.internalBaseUrl + "\"";
                }
            }
            if (mpsValue.messageRecipientInfo != null) {
                if (mpsValue.messageRecipientInfo != null) {
                    doc += " message-recipient-info=\"" +
                            mpsValue.messageRecipientInfo + "\"";
                }
            }
            doc += "> \n";

            if (channelValues != null) {
                doc += "      <channels> \n";
                for (int i = 0; i < channelValues.length; i++) {
                    ChannelValue channelValue = channelValues[i];
                    if (channelValue != null) {
                        doc += createChannelXml(channelValue);
                    }
                }
                doc += "      </channels> \n";
            }

            doc += "    </mps> \n";
        }
        doc += "  </application-plugins> \n";

        return doc;
    }

    private String createChannelXml(ChannelValue channelValue) {
        String doc = "";
        doc += "        <channel \n";
        if (channelValue.name != null) {
            doc += "          name=\"" +
                    channelValue.name + "\" \n";
        }
        if (channelValue.className != null) {
            doc += "          class=\"" +
                    channelValue.className + "\">\n";
        }
        if (channelValue instanceof ChannelSmtpValue) {
            ChannelSmtpValue smtpValue = (ChannelSmtpValue)
                    channelValue;
            if (smtpValue.requiresAuthorisation != null) {
                doc += "          <argument name=\"auth\" value=\"" +
                        smtpValue.requiresAuthorisation + "\"/>\n";
            }
            if (smtpValue.hostname != null) {
                doc += "          <argument name=\"host\" value=\"" +
                        smtpValue.hostname + "\"/> \n";
            }
            if (smtpValue.username != null) {
                doc += "          <argument name=\"user\" value=\"" +
                        smtpValue.username + "\"/> \n";
            }
            if (smtpValue.password != null) {
                doc += "          <argument name=\"password\" value=\"" +
                        smtpValue.password + "\"/>\n";
            }
        } else if (channelValue instanceof ChannelSmscValue) {
            ChannelSmscValue smscValue = (ChannelSmscValue)
                    channelValue;
            if (smscValue.address != null) {
                doc += "          <argument name=\"smsc-ip\" value=\"" +
                        smscValue.address + "\"/>\n";
            }
            if (smscValue.port != null) {
                doc += "          <argument name=\"smsc-port\" value=\"" +
                        smscValue.port + "\"/>\n";
            }
            if (smscValue.username != null) {
                doc += "          <argument name=\"smsc-user\" value=\"" +
                        smscValue.username + "\"/>\n";
            }
            if (smscValue.password != null) {
                doc += "          <argument name=\"smsc-password\" value=\"" +
                        smscValue.password + "\"/>\n";
            }
            if (smscValue.bindType != null) {
                doc += "          <argument name=\"smsc-bindtype\" value=\"" +
                        smscValue.bindType + "\"/>\n";
            }
            if (smscValue.serviceType != null) {
                doc += "          <argument name=\"smsc-svctype\" value=\"" +
                        smscValue.serviceType + "\"/>\n";
            }
            if (smscValue.serviceAddress != null) {
                doc += "          <argument name=\"smsc-svcaddr\" value=\"" +
                        smscValue.serviceAddress + "\"/>\n";
            }
            if (smscValue.supportsMulti != null) {
                doc += "          <argument name=\"smsc-supportsmulti\" value=\"" +
                        smscValue.supportsMulti + "\"/>\n";
            }
        } else if (channelValue instanceof ChannelMmscValue) {
            ChannelMmscValue mmscValue = (ChannelMmscValue)
                    channelValue;
            if (mmscValue.url != null) {
                doc += "          <argument name=\"url\" value=\"" +
                        mmscValue.url + "\"/>\n";
            }
            if (mmscValue.defaultCountryCode != null) {
                doc += "          <argument name=\"default-country-code\" value=\"" +
                        mmscValue.defaultCountryCode + "\"/>\n";
            }
        }
        doc += "</channel> \n";
        return doc;
    }

    /**
     * A private Value Object class for holding MPS values.
     */
    private static class MpsValue {
        String internalBaseUrl;
        String messageRecipientInfo;
        List channels = new ArrayList();
    }

    private static abstract class ChannelValue {
        String name;
        String className;
    }

    private static class ChannelSmtpValue extends ChannelValue {
        {
            // May as well provide the only value which will work here.
            className = "com.volantis.mps.channels.SMTPChannelAdapter";
        }
        String hostname;
        Boolean requiresAuthorisation;
        String username;
        String password;
    }

    private static class ChannelSmscValue extends ChannelValue {
        {
            // May as well provide the only value which will work here.
            className = "com.volantis.mps.channels.LogicaSMSChannelAdapter";
        }
        String address;
        Integer port;
        String username;
        String password;
        String bindType;
        String serviceType;
        String serviceAddress;
        Boolean supportsMulti;
    }

    private static class ChannelMmscValue extends ChannelValue {
        {
            // May as well provide the only value which will work here.
            className = "com.volantis.mps.channels.NokiaMMSChannelAdapter";
        }
        String url;
        Integer defaultCountryCode;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4726/1	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 09-Jun-04	4619/9	ianw	VBM:2004060111 Fixed up testcase for MPS configuration

 07-Jun-04	4619/7	ianw	VBM:2004060111 Fixed MPS Configuration

 07-Jun-04	4619/5	ianw	VBM:2004060111 Fixed MPS Configuration

 07-Jun-04	4619/3	ianw	VBM:2004060111 Fixed MPS Configuration

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 ===========================================================================
*/
