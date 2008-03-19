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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.jsp;


import com.volantis.shared.servlet.ServletEnvironment;
import com.volantis.servlet.JSPPageContextStub;

import javax.servlet.jsp.PageContext;

public class DefaultJspEnvironmentFactoryTestCase 
        extends JspEnvironmentFactoryTestAbstract {

     /**
     * Creates a new DefaultJspEnvironmentFactoryTestCase instance
     * @param name the name of the test
     */ 
    public DefaultJspEnvironmentFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Tests the createEnvironmentInteraction method
     * @throws Exception if an error occurs
     */ 
    public void testCreateEnvironmentInteraction() throws Exception {
        JspEnvironmentFactory factory = new DefaultJspEnvironmentFactory();
        
        PageContext pageContext = new JSPPageContextStub();
        ServletEnvironment servletEnvironment = null;
        
        JspEnvironmentInteraction interaction = 
                factory.createEnvironmentInteraction(servletEnvironment,
                                                     pageContext);
        
        assertEquals("createEnvironmentInteraction should return a " +
                   "JspEnvironmentInteractionImpl instance", 
                   JspEnvironmentInteractionImpl.class, 
                   interaction.getClass());
    } 
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	271/1	doug	VBM:2003073002 Implemented various environment fatories

 ===========================================================================
*/
