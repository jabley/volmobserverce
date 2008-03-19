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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.project;

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.impl.io.ResourceLoader;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.project.PolicyCachePartitionConfiguration;
import com.volantis.mcs.runtime.configuration.project.PolicyTypePartitionConfiguration;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Test case for {@link ProjectConfigurationReader}.
 */
public class ProjectConfigurationReaderTestCase extends TestCaseAbstract {

    private static ResourceLoader resourceLoader = new ResourceLoader(
            ProjectConfigurationReaderTestCase.class);

    /**
     * Test that the ProjectConfiguration reader can correctly read a project.
     * @throws Exception
     */
    public void testProjectContainerReader() throws Exception {
        String name = "mcs-project.xml";
        String sourceXML = resourceLoader.getResourceAsString(name);

        Reader reader = new CharArrayReader(sourceXML.toCharArray());

        ProjectConfigurationReader projectReader = new ProjectConfigurationReader();
        String expectedLocation = name;
        RuntimeProjectConfiguration configuration =
                projectReader.readProject(reader, expectedLocation);

        checkConfiguration(configuration, expectedLocation);

    }

    private void checkConfiguration(
            RuntimeProjectConfiguration configuration,
            String expectedLocation) {
        assertNotNull("project configuration should not be null", configuration);

        assertEquals("project configuration asset baseURL should equal /assets",
                "/assets", configuration.getAssets().getBaseUrl());
        assertEquals(expectedLocation, configuration.getLocation());

        String defaultLayout = configuration.getDefaultProjectLayoutLocator()
                .getDefaultProjectLocation();
        assertEquals("/welcome.mlyt", defaultLayout);

        List themes = configuration.getProjectThemes().getThemes();
        assertEquals(Arrays.asList(new String[]{
            "/xdime-2/welcome.mthm"
        }), themes);

        PolicyCachePartitionConfiguration partition =
                configuration.getPolicyCachePartition();
        assertNotNull(partition);
        assertEquals(box(100), partition.getSize());

        RemotePolicyCacheConfiguration constraints =
                partition.getConstraintsConfiguration();
        assertNotNull(constraints);
        assertEquals(box(true), constraints.getDefaultCacheThisPolicy());
        assertEquals(box(false), constraints.getDefaultRetainDuringRetry());
        assertEquals(box(true), constraints.getDefaultRetryFailedRetrieval());
        assertEquals(box(50), constraints.getDefaultRetryInterval());
        assertEquals(box(5), constraints.getDefaultRetryMaxCount());
        assertEquals(box(Integer.MAX_VALUE), constraints.getDefaultTimeToLive());

        assertEquals(box(true), constraints.getAllowCacheThisPolicy());
        assertEquals(box(true), constraints.getAllowRetainDuringRetry());
        assertEquals(box(true), constraints.getAllowRetryFailedRetrieval());
        assertEquals(box(Integer.MAX_VALUE), constraints.getMaxRetryMaxCount());
        assertEquals(box(Integer.MAX_VALUE), constraints.getMaxTimeToLive());
        assertEquals(box(10), constraints.getMinRetryInterval());

        assertEquals(1, partition.getTypeSpecificPartitionCount());

        PolicyTypePartitionConfiguration typePartition =
                partition.getTypePartition(PolicyType.AUDIO);
        assertNotNull(typePartition);
        PolicyTypePartitionConfiguration imagePartition =
                partition.getTypePartition(PolicyType.IMAGE);
        assertSame(typePartition, imagePartition);
        assertEquals(Arrays.asList(new PolicyType[] {
            PolicyType.AUDIO, PolicyType.IMAGE
        }), typePartition.getPolicyTypes());
        assertEquals(50, typePartition.getSize());

        constraints = typePartition.getConstraints();
        assertNotNull(constraints);

        assertEquals(null, constraints.getDefaultCacheThisPolicy());
        assertEquals(null, constraints.getDefaultRetainDuringRetry());
        assertEquals(null, constraints.getDefaultRetryFailedRetrieval());
        assertEquals(null, constraints.getDefaultRetryInterval());
        assertEquals(null, constraints.getDefaultRetryMaxCount());
        assertEquals(null, constraints.getDefaultTimeToLive());

        assertEquals(box(false), constraints.getAllowCacheThisPolicy());
        assertEquals(null, constraints.getAllowRetainDuringRetry());
        assertEquals(null, constraints.getAllowRetryFailedRetrieval());
        assertEquals(null, constraints.getMaxRetryMaxCount());
        assertEquals(null, constraints.getMaxTimeToLive());
        assertEquals(null, constraints.getMinRetryInterval());
    }

    private Integer box(final int i) {
        return new Integer(i);
    }

    private Boolean box(boolean b) {
        return b ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Test that the ProjectConfiguration reader can correctly read a project.
     * @throws Exception
     */
    public void testProjectContainerStream() throws Exception {
        String name = "mcs-project.xml";

        URL url = getClass().getResource(name);
        InputStream stream = url.openStream();

        ProjectConfigurationReader projectReader = new ProjectConfigurationReader();
        String expectedLocation = url.toExternalForm();
        RuntimeProjectConfiguration configuration =
                projectReader.readProject(stream, expectedLocation);

        checkConfiguration(configuration, expectedLocation);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	9990/1	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 ===========================================================================
*/
