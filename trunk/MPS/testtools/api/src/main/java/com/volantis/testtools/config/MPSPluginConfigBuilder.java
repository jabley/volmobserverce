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
 * $Header: /src/mps/com/volantis/testtools/config/MPSApplicationConfig.java,v 1.2 2003/03/20 10:15:36 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Feb-03    Mat             VBM:2003022002 - Created. Application config 
 *                              for MPS
 * 19-Mar-03    Geoff           VBM:2003032001 - Refactored to use external
 *                              ConfigValues.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.config;

import com.volantis.testtools.ExternalApplicationConfig;
import com.volantis.testtools.config.PluginConfigFileBuilder;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

/**
 * This class knows about application configuration for MPS.
 */
public class MPSPluginConfigBuilder implements PluginConfigFileBuilder {

    private static Map configFactoryMap = new HashMap();
    static {
        addChannelConfigBuilderFactory(ConfigValueChannelMms.class,
                new ChannelConfigBuilderFactory() {
                    public ChannelConfigBuilder create(
                            ConfigValueChannel channel) {
                        return new MMSChannelConfigBuilder(
                                (ConfigValueChannelMms) channel);
                    }
                });
        addChannelConfigBuilderFactory(ConfigValueChannelSms.class,
                new ChannelConfigBuilderFactory() {
                    public ChannelConfigBuilder create(
                            ConfigValueChannel channel) {
                        return new SMSChannelConfigBuilder(
                                (ConfigValueChannelSms) channel);
                    }
                });
        addChannelConfigBuilderFactory(ConfigValueChannelSmtp.class,
                new ChannelConfigBuilderFactory() {
                    public ChannelConfigBuilder create(
                            ConfigValueChannel channel) {
                        return new SMTPChannelConfigBuilder(
                                (ConfigValueChannelSmtp) channel);
                    }
                });
    }
    
    private MPSPluginConfigValue MPSvalue;

    /** Creates a new instance of MPSApplicationConfig */
    public MPSPluginConfigBuilder() {
    }

    public String build(PluginConfigValue value) {
        MPSvalue = (MPSPluginConfigValue) value;
        String config = "<mps " + 
        "internal-base-url=\"" + MPSvalue.internalBaseUrl + "\"\n" +
        "message-recipient-info=\"" + MPSvalue.messageRecipientInfo + "\">\n" +
        "<channels>\n";
        
        if(MPSvalue.channels != null) {
            Iterator i = MPSvalue.channels.iterator();
            while(i.hasNext()) {
                ConfigValueChannel channel = (ConfigValueChannel) i.next();
                ChannelConfigBuilderFactory factory = 
                        (ChannelConfigBuilderFactory) 
                        configFactoryMap.get(channel.getClass());
                ChannelConfigBuilder ccb = factory.create(channel);
                config += ccb.renderChannelConfig() + "\n";
            }
        }
        
        config += "</channels>\n</mps>";
        
        return config;
    }

    protected static void addChannelConfigBuilderFactory(Class clazz, 
            ChannelConfigBuilderFactory factory) {
        configFactoryMap.put(clazz, factory);
    }
    
    protected static interface ChannelConfigBuilderFactory {
        ChannelConfigBuilder create(ConfigValueChannel channel);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Oct-03	45/1	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
