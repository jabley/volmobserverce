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
 * 12-May-03    Doug            VBM:2003030405 - Created. TestCase for the
 *                              XMLWrappingProcess class.  
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

/**
 * TestCase for the XMLWrappingProcess class. 
 */ 
public abstract class XMLWrappingProcessTestAbstract 
        extends XMLProcessImplTestCase {
     
    /**
     * Creates a new XMLWrappingProcessTestAbstract instance
     * @param name the name
     */ 
    protected XMLWrappingProcessTestAbstract(String name) {
        super(name);
    }

    // javadoc inherited from superclass
    protected abstract XMLProcess createTestableProcess();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-03	165/1	allan	VBM:2003070101 Fix bug in MessageOperationProcess.startElement()

 ===========================================================================
*/
