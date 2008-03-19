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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/MandatoryAppConfigurator.java,v 1.1 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; an abstract app 
 *                              configurator that sets default values only for 
 *                              mandatory elements.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

import com.volantis.testtools.config.ConfigValue;
import com.volantis.testtools.io.IOUtils;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

/**
 * An abstract app configurator that sets default values only for mandatory 
 * elements.
 * <p>
 * For use by subclasses that don't really care about these values in 
 * particular.
 */ 
public abstract class MandatoryAppConfigurator implements AppConfigurator {

    // Inherit javadoc.
    public void setUp(ConfigValue config) throws Exception {
        // Create the log4j config that we need - i.e. a ConsoleAppender.
        // We'd like to let the "client" control *what* was logged, but fix
        // *how* it is logged so we can collect and distribute it to the
        // correct place. So, we generate the log4j config ourselves, and
        // (in future?) let the client add Categories, but not change the
        // Appenders. Note that we may need to pass Category information in
        // from outside the VM, using -D properties seems like the correct
        // way in this case.
        File log4jFile = File.createTempFile("test-mcs-log4j", ".xml");
        PrintWriter pw = new PrintWriter(new FileWriter(log4jFile));
        pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        pw.println("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">");
        pw.println("<log4j:configuration ");
        pw.println(     "xmlns:log4j=\"http://jakarta.apache.org/log4j/\">");
        pw.println("  <appender name=\"CONSOLE\" ");
        pw.println("      class=\"org.apache.log4j.ConsoleAppender\">");
        pw.println("    <layout class=\"org.apache.log4j.PatternLayout\">");
        pw.println("      <param name=\"ConversionPattern\" ");
        pw.println("        value=\"%-4r [%t] %-5p %c %x - %m%n\"/>");
        pw.println("    </layout>");
        pw.println("  </appender>");
        pw.println("  <root>");
        pw.println("    <priority value=\"error\"/>");
        pw.println("    <appender-ref ref=\"CONSOLE\"/>");
        pw.println("  </root>");
        pw.println("</log4j:configuration>");
        pw.close();
        config.log4jXmlConfigFile = log4jFile.getPath();
    }

    public void tearDown(ConfigValue config) {
        // Clean up the log4j config file we created earlier.
        // NOTE: we do this rather than use deleteOnExit as per VM things like
        // this are not good in a test environment where we can't guarantee
        // whether each test executes in it's own VM or not.
        File log4jFile = new File(config.log4jXmlConfigFile);
        log4jFile.delete();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 13-May-05	8200/1	trynne	VBM:2005050412 Moved classes from oldtests to testtools-runtime and added testtools-runtime classes into testtools.jar so that MPS need only depend on testtools

 11-Mar-05	6842/1	emma	VBM:2005020302 Making file references in config files relative to those files

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 31-Oct-03	1593/1	mat	VBM:2003101502 Adding pluginconfigvalue

 23-Oct-03	1585/2	mat	VBM:2003101502 Add plugin config builders to ConfigFileBuilder

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 20-Aug-03	1186/1	geoff	VBM:2003032406 Remove test suite dependency on mariner-log4j.xml

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	670/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 ===========================================================================
*/
