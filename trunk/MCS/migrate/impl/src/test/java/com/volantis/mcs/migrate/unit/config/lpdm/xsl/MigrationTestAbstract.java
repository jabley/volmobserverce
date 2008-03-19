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

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.net.URL;

/**
 * Abstract class which verifies that themes are correctly migrated from one
 * version of MCS to another using the given stylesheet.
 */
public abstract class MigrationTestAbstract extends XSLTestAbstract {
    /**
     * The resource location of the stylesheet to use for transformations.
     */
    protected String stylesheetLocation;
    /**
     * The resource location of the input files.
     */
    protected String inputLocation;
    /**
     * The resource location of the expected files.
     */
    protected String expectedLocation;
    /**
     * The suffix for input files.
     */
    private static final String SUFFIX = ".xml";

    public MigrationTestAbstract(
            String STYLESHEET_LOCATION, String INPUT_LOCATION,
            String EXPECTED_LOCATION) {

        this.stylesheetLocation = STYLESHEET_LOCATION;
        this.inputLocation = INPUT_LOCATION;
        this.expectedLocation = EXPECTED_LOCATION;
    }

    /**
     * Carries out a test transforming one document to another, building the
     * filename for the file to transform from the specified test name and
     * the expected results from the specified expected name. Transforms the
     * source file, and verifies that its contents are similar to the expected
     * result, ignoring whitespace.
     *
     * @param testName The name of the test to carry out
     * @throws Exception if an error occurs
     */
    protected void doTransform(String testName, String expected)
            throws Exception {

        URL styleSheetURL = getClass().getResource(stylesheetLocation);
        InputStream stylesheet = styleSheetURL.openStream();
        String url = inputLocation + testName + SUFFIX;
        InputStream input = getClass().getResourceAsStream(url);
        InputStream output = getClass().getResourceAsStream(
                expectedLocation + expected + SUFFIX);
        // TODO: enable validation here.
        doTransformation(new StreamSource(input, url), output,
                new StreamSource(stylesheet, styleSheetURL.toExternalForm()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10830/1	pduffin	VBM:2005121405 Migrate rules containing only selector sequences for hr elements so that if they do not have a color property that it is set from the background-color property

 06-Dec-05	10619/1	ibush	VBM:2005113017 Fix xfoption typeSelectors

 06-Dec-05	10606/1	ibush	VBM:2005113017 Fix xfoption typeSelectors

 17-Nov-05	10349/1	emma	VBM:2005111604 Refactor migration tests

 ===========================================================================
*/
