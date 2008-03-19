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

package com.volantis.mcs.migrate.unit.config;

import com.volantis.mcs.migrate.api.config.RemotePolicyMigrator;
import com.volantis.mcs.migrate.impl.config.RemotePolicyMigratorImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class PolicyMigratorTestSuiteBuilder {

    /**
     * The resource location of the input files.
     */
    private final String inputLocation;
    private final String version;

    /**
     * The resource location of the expected files.
     */
    private final String latestLocation;

    private List testFiles;

    public PolicyMigratorTestSuiteBuilder(
            String inputLocation, String version) {

        this.inputLocation = inputLocation;
        this.version = version;
        this.latestLocation = "lpdm/xsl/200602_lpdm_files/";
        this.testFiles = new ArrayList();
    }

    public void addTestFile(String file) {
        testFiles.remove(file);
        testFiles.add(file);
    }

    public void addTestFiles(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            addTestFile(string);
        }
    }


    public Test getSuite() {
        TestSuite suite = new TestSuite();
        addToSuite(suite);
        return suite;
    }

    /**
     * Add the built tests to the specified suite.
     *
     * @param suite The suite to which the tests will be added.
     */
    public void addToSuite(TestSuite suite) {
        for (int i = 0; i < testFiles.size(); i++) {
            final String testFile = (String) testFiles.get(i);
            suite.addTest(new MigrationTest(testFile, version));
        }
    }

    private class MigrationTest extends TestCaseAbstract {
        
        private final String testFile;

        public MigrationTest(String testFile, String version) {
            this.testFile = testFile;
            setName("migrate - " + version + " - "  + testFile);
        }
//
//        // javadoc inherited
//        public void setUp() throws Exception {
//            BasicConfigurator.configure();
//            Category.getRoot().setPriority(Priority.DEBUG);
//        }
//
//        // javadoc inherited from superclass
//        protected void tearDown() throws Exception {
//            Category.shutdown();
//        }

        protected void runTest() throws Throwable {

//            DefaultConfigurator.configure(true);

            String inputSystemId = inputLocation + testFile + ".xml";
            InputStream inputStream = getClass()
                    .getResourceAsStream(inputSystemId);
            if (inputStream == null) {
                throw new IllegalArgumentException("Cannot find " + inputSystemId);
            }

            String latestSystemId = latestLocation + testFile + ".xml";
            InputStream expectedStream = getClass()
                    .getResourceAsStream(latestSystemId);
            if (expectedStream == null) {
                throw new IllegalArgumentException("Cannot find " + latestSystemId);
            }
            String expected = readAsString(expectedStream);

            RemotePolicyMigrator migrator = new RemotePolicyMigratorImpl();
            String result = migrator.migratePolicy(inputStream, inputSystemId);

            System.out.println("Expected: " + expected);
            System.out.println("Actual: " + result);

            assertXMLEquals("Result should match", expected, result);
        }

        private String readAsString(InputStream stream) throws IOException {
            StringBuffer buffer = new StringBuffer();
            Reader reader = new InputStreamReader(stream);
            int read = 0;
            while ((read = reader.read()) != -1) {
                buffer.append((char) read);
            }
            return buffer.toString();
        }
    }
}
