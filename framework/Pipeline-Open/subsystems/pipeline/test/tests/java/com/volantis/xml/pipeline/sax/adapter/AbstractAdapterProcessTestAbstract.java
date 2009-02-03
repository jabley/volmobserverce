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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2003030405 - Created. Base TestCase for
 *                              AdapterProcess classes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.adapter;

import com.volantis.xml.pipeline.sax.XMLWrappingProcessTestAbstract;

/**
 * Base TestCase for testing AdapterProcess classes. 
 */ 
public abstract class AbstractAdapterProcessTestAbstract 
        extends XMLWrappingProcessTestAbstract {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
      * Creates a new AbstractAdapterProcessTestAbstract instance
      * @param name the name
      */ 
     public AbstractAdapterProcessTestAbstract(String name) {
         super(name);
     }
    
     /**
      * Method to test the processAttributes() method.
      * @throws Exception
      */ 
     public abstract void testProcessAttributes() throws Exception;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
