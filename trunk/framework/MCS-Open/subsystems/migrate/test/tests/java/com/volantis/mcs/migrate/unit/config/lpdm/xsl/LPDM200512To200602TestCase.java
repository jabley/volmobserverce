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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.unit.config.lpdm.xsl;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;

/**
 * Tests for the stylesheet to convert themes from version 3.0 to 2005/09.
 */
public class LPDM200512To200602TestCase
        extends TestCaseAbstract {

    public static Test suite() {
        MigrationTestSuiteBuilder builder = new MigrationTestSuiteBuilder(
                "/com/volantis/mcs/migrate/impl/config/lpdm/xsl/lpdm-200512-to-200602.xsl",
                "200512_lpdm_files/", "200602_lpdm_files/");

        builder.addTestFiles(new String[] {
            "assetGroup",
            "assetGroupOnDevice",
            "attributeSelectors",
            "audioComponent",
            "buttonImageComponent",
            "chartComponent",
            "cssDeviceTheme",
            "dynamicVisualComponent",
            "classSelectors",
            "combinedSelector",
            "content",
            "imageComponent",
            "emptyImageComponent",
            "linkComponent",
            "multipleElementSelectors",
            "multipleIDSelectors",
            "multiplePseudoClassSelectors",
            "multiplePseudoElementSelectors",
            "canvasLayout",
            "pseudoClassSelectorsIllegal",
//            "resourceComponent",
            "rolloverImageComponent",
            "scriptComponent",
            "selectorReordering",
            "singleIDSelector",
            "singlePseudoElementSelector",
            "singleTypeSelector",
            "singleUniversalSelector",
            "singleUniversalSelectorWithNS",
            "statefulPseudoClassSelector",
            "structurePseudoClassSelector",
            "structurePseudoClassSelectorsDuplicates",
            "styleValues",
            "statefulPseudoClassSelectorDuplicates",
            "textComponent",
        });

        return builder.getSuite();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10369/1	emma	VBM:2005111604 Forward port: refactor migration tests

 17-Nov-05	10349/1	emma	VBM:2005111604 Refactor migration tests

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 16-Sep-05	9512/3	pduffin	VBM:2005091408 Added support for invalid style values

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/3	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 18-May-05	8181/4	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 13-May-05	8181/1	adrianj	VBM:2005050505 XSL for theme migration

 ===========================================================================
*/
