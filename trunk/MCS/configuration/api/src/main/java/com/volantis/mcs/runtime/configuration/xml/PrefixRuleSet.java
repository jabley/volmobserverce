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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/PrefixRuleSet.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a base class for 
 *                              rule sets with a parent prefix. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.RuleSetBase;

/**
 * A base class for rule sets with a parent prefix.
 * <p>
 * Useful for constructing composite rule sets.   
 */ 
public abstract class PrefixRuleSet extends RuleSetBase {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Tag pattern prefix to use with each call to digester add methods.
     */ 
    protected String prefix;

    public PrefixRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public PrefixRuleSet() {
    }

    /**
     * Returns the tag pattern prefix to use with each call to digester add 
     * methods.
     * 
     * @return the tag pattern prefix to use.
     */ 
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the tag pattern prefix to use with each call to digester add 
     * methods.
     *
     * @param prefix the tag pattern prefix to use.
     */ 
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
