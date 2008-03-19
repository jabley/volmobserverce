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
package com.volantis.synergetics.reporting.impl.osgi;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.cm.ConfigurationException;

public class Log4JReportHandlerTestCase extends TestCaseAbstract {


    public ReportEventHandler createteReportEventHandler()
        throws ConfigurationException {
        Dictionary dict = new Hashtable();
        dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, "LoggerName");
        dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY,
                 "This is a message format string {:TSTAMP}");
        return new Log4JReportHandler(dict);
    }


    public void testRequiredConfigProperties() throws Exception {

        ReportEventHandler ref = createteReportEventHandler();

        {
            Dictionary dict = new Hashtable();
            try {
                ref.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, "LoggerName");
            try {
                ref.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY,
                     "This is a message format string {:TSTAMP}");
            try {
                ref.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }


        {
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, "LoggerName");
            dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY,
                     "This is a message format string {:TSTAMP}");
            ref.setConfiguration(dict);
        }

    }

    public void checkValidParams() throws Exception {

        ReportEventHandler ref = createteReportEventHandler();
        {
            // logger name cannot be emtpy
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, "");
            dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY,
                     "This is a message format string {:TSTAMP}");
            try {
                ref.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            // message format string can be empty
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, "LoggerName");
            dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY, "");
            ref.setConfiguration(dict);
        }

        {
            // logger name cannot be null
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, null);
            dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY,
                     "This is a message format string {:TSTAMP}");
            try {
                ref.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            // message format cannot be null
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, null);
            dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY,
                     "This is a message format string {:TSTAMP}");
            try {
                ref.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

        {
            // both cannot be null
            Dictionary dict = new Hashtable();
            dict.put(Log4JReportHandler.LOGGER_NAME_PROPERTY, null);
            dict.put(Log4JReportHandler.MESSAGE_FORMAT_PROPERTY,null);
            try {
                ref.setConfiguration(dict);
                fail("ConfigurationException should have been thrown");
            } catch (ConfigurationException ce) {
                // success
            }
        }

    }
}
