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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.project;

import java.io.File;
import java.util.Random;

import javax.servlet.http.HttpServlet;

import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigContextMock;
import com.volantis.mcs.runtime.configuration.TestConfigContext;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * Test cases for {@link ConfigPolicySourceSelector}.
 */
public class ConfigPolicySourceSelectorTestCase extends MockTestCaseAbstract {

    private ConfigContextMock configContextMock;
    private RuntimePolicySourceFactory sourceFactoryMock;
    private ConfigPolicySourceSelector sourceSelector;

    protected void setUp() throws Exception {
        super.setUp();
        
        configContextMock = new ConfigContextMock(
                "configContextMock", expectations);
        
        sourceFactoryMock = 
            new RuntimePolicySourceFactoryMock(
                    "runtimePolicySourceFactoryMock", expectations);
        
        sourceSelector = new ConfigPolicySourceSelector(
                sourceFactoryMock, configContextMock);
        
    }
    
    
    /**
     * When the path for xml-repository in MCS config file 
     * is wrong (i.e. the provided directory does not exist)
     * then ExtendedRuntimeException is thrown 
     *
     */
    public void testNotExistPath() {
        final String notExistDir = new String("/home/notExistDirectory/");
        configContextMock.expects.getConfigRelativeFile(notExistDir,true)
            .returns(null).fixed(1);
        try {
            sourceSelector.resolveRelativeFile(notExistDir);
            fail("Exception should be thrown because supplied directory" +
                    "does not exist.");
        } catch (ExtendedRuntimeException e) {
            
        }
    }

    /**
     * No exception will should be thrown if supplied directory
     * exists.
     */
    public void testExistPath() {
        Random r = new Random();
        String tempDir = System.getProperty("java.io.tmpdir");
        String dirName = tempDir 
            + System.getProperty("file.separator") 
            + "tmp" + r.nextLong();
        (new File(dirName)).mkdir();
        
        configContextMock.expects.getConfigRelativeFile(dirName, true)
            .returns(new File(dirName)).fixed(1);

        try {
            sourceSelector.resolveRelativeFile(dirName);
        } catch (ExtendedRuntimeException e) {
            fail("Exception was thrown, but it was not expected " +
                    "because supplied directory does exist.");
        } finally {
            (new File(dirName)).delete();
        }
        
    }
    
}
