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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/digester/BogusSetNextRule.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a bogus Rule 
 *                              implementation calls a method on the (top-n) 
 *                              (parent/grandparent/etc) object, passing the 
 *                              top object (child) as an argument. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.SetNextRule;
import our.apache.commons.beanutils.MethodUtils;

/**
 * A bogus Rule implementation that calls a method on the (top-n) 
 * (parent/grandparent/etc) object, passing the top object (child) as an 
 * argument. It is commonly used to establish parent-child relationships when 
 * you wish to skip up the tree to find the parent rather than just using the
 * immediate parent.
 * <p>
 * This is considered to be bogus because it subverts the overall design of 
 * the Digester, and is required to support something in our config file that 
 * is hard to understand and should never have been implemented, namely 
 * having inconsistent nesting for remote caches and quotas.
 * 
 * @deprecated Do not use this for new functionality; it is strictly to 
 * support the bogus existing format of mcs-config.dtd. 
 */
public class BogusSetNextRule extends SetNextRule {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    private int parentIndexHack = 1;
    
    public BogusSetNextRule(String methodName) {
        super(methodName);
    }

    /**
     * Sets the parent index in the stack; 1 is the normal parent (default),
     * 2 is the grandparent, etc.
     * <p>
     * NOTE: This is a hack and should not be used for new code/XML, since it 
     * indicates something dodgy in the XML structure being parsed.    
     * 
     * @param parentIndexHack the index of the parent in the stack.
     */ 
    public void setBogusParentIndex(int parentIndexHack) {
        this.parentIndexHack = parentIndexHack;
    }
    
    // Inherit javadoc.
    public void end() throws Exception {
        // NOTE: following code copied from SetNextRule only so we can change 
        // the parent stack index used. Yes, it's a hack.
        
        // Identify the objects to be used
        Object child = digester.peek(0);
        Object parent = digester.peek(parentIndexHack);
        if (digester.getLogger().isDebugEnabled()) {
            if (parent == null) {
                digester.getLogger().debug("[SetNextRule]{" + digester.getMatch() +
                        "} Call [NULL PARENT]." +
                        methodName + "(" + child + ")");
            } else {
                digester.getLogger().debug("[SetNextRule]{" + digester.getMatch() +
                        "} Call " + parent.getClass().getName() + "." +
                        methodName + "(" + child + ")");
            }
        }

        // Call the specified method
        Class paramTypes[] = new Class[1];
        if (paramType != null) {
            paramTypes[0] =
                    digester.getClassLoader().loadClass(paramType);
        } else {
            paramTypes[0] = child.getClass();
        }
        
        if (useExactMatch) {
        
            MethodUtils.invokeExactMethod(parent, methodName,
                new Object[]{ child }, paramTypes);
                
        } else {
        
            MethodUtils.invokeMethod(parent, methodName,
                new Object[]{ child }, paramTypes);
        
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
