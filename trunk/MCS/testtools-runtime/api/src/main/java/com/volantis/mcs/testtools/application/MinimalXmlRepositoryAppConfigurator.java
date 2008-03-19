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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/MinimalXmlRepositoryAppConfigurator.java,v 1.1 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; the simplest valid
 *                              app configurator that we can imagine,
 *                              implemented as an XML configurator with a
 *                              non-existant XML repository.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

import com.volantis.testtools.config.ConfigValue;
import com.volantis.testtools.config.ConfigProjectPoliciesXmlValue;
import com.volantis.testtools.io.IOUtils;
import com.volantis.mcs.testtools.application.MandatoryAppConfigurator;
import com.volantis.mcs.objects.FileExtension;

import java.io.File;

/**
 * The simplest valid app configurator that we can imagine, implemented as
 * an XML configurator with a non-existant XML project.
 */
public class MinimalXmlRepositoryAppConfigurator
        extends MandatoryAppConfigurator {

    private File projectDirectory;

    private File deviceRepositoryFile;

    // Inherit javadoc.
    public void setUp(ConfigValue config) throws Exception {

        // Include mandatory stuff.
        super.setUp(config);

        // Minimal requirements for an empty XML repository.
        // This should probably check that it doesn't already exist at this
        // point...
        config.repositoryType = "xml";

        ConfigProjectPoliciesXmlValue xmlPolicies =
                new ConfigProjectPoliciesXmlValue();

        projectDirectory = File.createTempFile("mcs", "");
        if (projectDirectory.exists()) {
            projectDirectory.delete();
            if (!projectDirectory.mkdirs()) {
                throw new IllegalStateException("Could not create test " +
                        "project directory" + projectDirectory);
            }
        } else {
            throw new IllegalStateException("Could not create test project " +
                    "directory" + projectDirectory);
        }
        xmlPolicies.projectDir = projectDirectory.getAbsolutePath();
        config.defaultProjectPolicies = xmlPolicies;

        // Create a device repository
        File deviceRepositoryFile = IOUtils.extractTempZipFromJarFile(
                MinimalXmlRepositoryAppConfigurator.class,
                "minimal_devices.mdpr",
                FileExtension.DEVICE_REPOSITORY.getExtension());
        config.standardFileDeviceRepositoryLocation =
                deviceRepositoryFile.getAbsolutePath();
    }

    public void tearDown(ConfigValue config) {

        // Make sure we do the superclass cleanup.
        super.tearDown(config);

        if (projectDirectory.exists()) {
            IOUtils.deleteDir(projectDirectory);
        }

        if (deviceRepositoryFile!=null && deviceRepositoryFile.exists()) {
            deviceRepositoryFile.delete();
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 13-May-05	8200/1	trynne	VBM:2005050412 Moved classes from oldtests to testtools-runtime and added testtools-runtime classes into testtools.jar so that MPS need only depend on testtools

 11-Mar-05	6842/2	emma	VBM:2005020302 Making file references in config files relative to those files

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 29-Jun-04	4733/9	allan	VBM:2004062105 Rework issues.

 29-Jun-04	4733/7	allan	VBM:2004062105 Use a minimal device repository for Volantis.initialization

 28-Jun-04	4733/5	allan	VBM:2004062105 Use a minimal device repository for Volantis.initialization

 28-Jun-04	4733/2	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 24-Jun-04	4737/1	allan	VBM:2004062202 Restrict volantis initialization.

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 30-Jan-04	2807/2	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/1	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 02-Jan-04	2302/1	andy	VBM:2003121706 test cases are now deleting repositories in tear down methods

 23-Dec-03	2252/1	andy	VBM:2003121703 change to default name for non existant repository in test suite

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	670/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 ===========================================================================
*/
