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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Ensure that it is possible to migrate30 files to the latest.
 */
public class MigrateRepositoryToLatestTestCase
        extends TestCaseAbstract {

    public static Test suite() {
        TestSuite suite = new TestSuite();

        PolicyMigratorTestSuiteBuilder builder;

        // Migrate from v30.
        builder = new PolicyMigratorTestSuiteBuilder("lpdm/xsl/30_lpdm_files/",
                "v30");

        builder.addTestFiles(new String[] {
            "classSelectors",
            "content",
            "multipleElementSelectors",
            "multipleIDSelectors",
            "multiplePseudoClassSelectors",
            "multiplePseudoElementSelectors",
            "canvasLayout",
            "pseudoClassSelectorsIllegal",
            "selectorReordering",
            "singleIDSelector",
            "singlePseudoElementSelector",
            "singleTypeSelector",
            "singleUniversalSelector",
            "statefulPseudoClassSelector",
            "statefulPseudoClassSelectorDuplicates",
            "structurePseudoClassSelector",
            "structurePseudoClassSelectorsDuplicates",
            "styleValues",
            "attributeSelectors",
            "set-response",
            "single-response",
            "three-images",
        });

        builder.addToSuite(suite);

        // Migrate from 2005/09
        builder = new PolicyMigratorTestSuiteBuilder(
                "lpdm/xsl/200509_lpdm_files/", "2005/09");

        builder.addTestFiles(new String[] {
            "classSelectors",
            "content",
            "multipleElementSelectors",
            "multipleIDSelectors",
            "multiplePseudoClassSelectors",
            "multiplePseudoElementSelectors",
            "canvasLayout",
            "pseudoClassSelectorsIllegal",
            "selectorReordering",
            "singleIDSelector",
            "singlePseudoElementSelector",
            "singleTypeSelector",
            "singleUniversalSelector",
            "statefulPseudoClassSelector",
            "statefulPseudoClassSelectorDuplicates",
            "structurePseudoClassSelector",
            "structurePseudoClassSelectorsDuplicates",
            "styleValues",
            "attributeSelectors",
            "set-response",
            "single-response",
            "three-images",
        });

        builder.addToSuite(suite);


        // Migrate from 2005/12
        builder = new PolicyMigratorTestSuiteBuilder(
                "lpdm/xsl/200512_lpdm_files/", "2005/12");

        builder.addTestFiles(new String[] {
            "classSelectors",
            "content",
            "multipleElementSelectors",
            "multipleIDSelectors",
            "multiplePseudoClassSelectors",
            "multiplePseudoElementSelectors",
            "canvasLayout",
            "pseudoClassSelectorsIllegal",
            "selectorReordering",
            "singleIDSelector",
            "singlePseudoElementSelector",
            "singleTypeSelector",
            "singleUniversalSelector",
            "statefulPseudoClassSelector",
            "statefulPseudoClassSelectorDuplicates",
            "structurePseudoClassSelector",
            "structurePseudoClassSelectorsDuplicates",
            "styleValues",
            "attributeSelectors",
            "set-response",
            "single-response",
            "three-images",
        });

        builder.addToSuite(suite);

        return suite;
    }
}
