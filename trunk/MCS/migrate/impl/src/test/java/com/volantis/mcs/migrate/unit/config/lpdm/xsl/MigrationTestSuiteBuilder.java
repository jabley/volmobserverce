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

package com.volantis.mcs.migrate.unit.config.lpdm.xsl;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.List;
import java.util.ArrayList;

public class MigrationTestSuiteBuilder {

    /**
     * The resource location of the stylesheet to use for transformations.
     */
    private final String stylesheetLocation;

    /**
     * The resource location of the input files.
     */
    private final String inputLocation;

    /**
     * The resource location of the expected files.
     */
    private final String expectedLocation;

    private List testFiles;

    public MigrationTestSuiteBuilder(String stylesheetLocation, String inputLocation,
                                     String expectedLocation) {

        this.stylesheetLocation = stylesheetLocation;
        this.inputLocation = inputLocation;
        this.expectedLocation = expectedLocation;
        this.testFiles = new ArrayList();
    }

    private void addTestFile(InputOutput file) {
//        testFiles.remove(file);
        testFiles.add(file);
    }

    public void addTestFiles(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            addTestFile(new InputOutput(string));
        }
    }

    public void addTestFiles(InputOutput[] files) {
        for (int i = 0; i < files.length; i++) {
            InputOutput file = files[i];
            addTestFile(file);
        }
    }

    public Test getSuite() {
        TestSuite suite = new TestSuite();
        for (int i = 0; i < testFiles.size(); i++) {
            final InputOutput testFile = (InputOutput) testFiles.get(i);
            suite.addTest(new MigrationTest(testFile));
        }
        return suite;
    }

    private class MigrationTest extends MigrationTestAbstract {
        
        private final String input;
        private final String output;

        public MigrationTest(InputOutput testFile) {
            super(MigrationTestSuiteBuilder.this.stylesheetLocation,
                    MigrationTestSuiteBuilder.this.inputLocation,
                    MigrationTestSuiteBuilder.this.expectedLocation);
            this.input = testFile.getInput();
            this.output = testFile.getOutput();
            String description;
            if (input.equals(output)) {
                description = input;
            } else {
                description = input + " -> " + output;
            }
            setName("migrate - " + description);
        }

        protected void runTest() throws Throwable {
            doTransform(input, output);
        }
    }
}
