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
 * $Header: /src/voyager/com/volantis/charset/configuration/xml/CharsetRuleSet.java,v 1.2 2003/04/28 15:36:22 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-03    Mat             VBM:2003040701 - A digester ruleset for 
 *                              charsets
 * 22-Apr-03    Mat             VBM:2003040701 - Updated javadoc.
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset.configuration.xml;

import com.volantis.charset.configuration.Alias;
import com.volantis.charset.configuration.Charset;
import com.volantis.charset.configuration.Charsets;
import our.apache.commons.digester.*;

/**
 * Define the digester rules for the charset configuration file.
 * @author  mat
 */
public class CharsetRuleSet extends RuleSetBase {

    /** Creates a new instance of DigesterDriver */
    public CharsetRuleSet() {
    }

    /**
     * Create the rules for parsing the file.
     *
     * @param digester The digester to set the rules in
     */
    public void addRuleInstances(Digester digester) {
         
         digester.addObjectCreate("charsets", Charsets.class);
         
         
         // Rules for a charset
         digester.addObjectCreate("charsets/charset", Charset.class);
         digester.addSetNext("charsets/charset", "addCharset");
         String[] properties = new String[] {"name",
                                            "MIBenum", 
                                            "complete",
                                            "preload"};
         digester.addSetProperties("charsets/charset", properties, properties);
         
         // Rules for aliases.
         digester.addObjectCreate("charsets/charset/alias", Alias.class);
         digester.addSetProperties("charsets/charset/alias", "name", "name");
         digester.addSetNext("charsets/charset/alias", "addAlias");
    }
         
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
