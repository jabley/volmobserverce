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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.StyleSheetConfiguration;
import com.volantis.mcs.runtime.TestableVolantis;
import junitx.util.PrivateAccessor;

/**
 * This class provides code that is useful for testing inline css generation.
 * This requires stylesheet configuration information (within volantis bean)
 * in order to handle internal caching.
 */
public class InlineCssTestHelper {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";


    /**
     * Create a specialised implementation of the volantis bean.  The
     * specialisations are necessary to configure the behaviour for the style
     * sheet tests and force the desired path through the code with no
     * exceptions.
     *
     * @param configuration         The stylesheet configuration to use.  May
     *                              be null.  In which case a new configuration
     *                              will be created.
     * @param enableExternalCaching True if external caching should be enabled
     *                              in this setup.
     *
     * @return                      The volantis bean created.
     */
    public static Volantis createVolantisBean(
            final StyleSheetConfiguration configuration,
            boolean enableExternalCaching) throws Exception {

        Volantis bean = new TestableVolantis() {
            // JavaDoc inherited
            public StyleSheetConfiguration getStyleSheetConfiguration() {
                StyleSheetConfiguration config;
                if (configuration == null) {
                    // No config specified so create a default one
                    config = new StyleSheetConfiguration();
                } else {
                    // Allow embedding of specified config
                    config = configuration;
                }
                return config;
            }
        };

        return bean;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/1	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 01-Jul-04	4775/3	claire	VBM:2004062911 Caching of inline stylesheets internally

 ===========================================================================
*/
