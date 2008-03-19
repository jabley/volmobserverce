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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/DefaultAppConfigurator.java,v 1.2 2003/03/07 10:21:46 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Feb-03    Geoff           VBM:2003022004 - Created.
 * 28-Feb-03    Geoff           VBM:2003010904 - Add another explanatory 
 *                              comment.
 * 06-Mar-03    Geoff           VBM:2003010904 - Refactored to use new 
 *                              ConfigValue stuff.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

import com.volantis.testtools.config.ConfigValue;

/**
 * An app configurator which establishes a set of reasonably minimal, valid 
 * and sensible "default" values.
 * <p>
 * Useful for those test fixtures which do not wish to figure this out for 
 * themselves.
 * <p>
 * These should just be the minimal, common things needed by all test fixtures.
 * Any fixture specific stuff ought to go into the fixture itself.
 */ 
public class DefaultAppConfigurator extends MinimalXmlRepositoryAppConfigurator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    // Inherit Javadoc.
    public void setUp(ConfigValue config) throws Exception {

        // Start with the minimal XML configuration
        super.setUp(config);

        // And then add in anything that makes life easier for testing.
        // Things we usually need for testing I presume.
        config.debugLogPageOutput = Boolean.TRUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-May-05	8200/1	trynne	VBM:2005050412 Moved classes from oldtests to testtools-runtime and added testtools-runtime classes into testtools.jar so that MPS need only depend on testtools

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
